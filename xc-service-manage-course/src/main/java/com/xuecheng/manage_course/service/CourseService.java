package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.corba.se.impl.naming.cosnaming.NamingUtils;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/17 0:15
 */
@Service
public class CourseService {

    @Resource
    private TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Resource
    private CourseMarketRepository courseMarketRepository;

    @Resource
    private CoursePicRepository coursePicRepository;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private CmsPageClient cmsPageClient;

    // 课程计划查询
    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    // 添加课程计划(增删改要加事务)
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {

        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid())
                || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        // 课程id
        String courseId = teachplan.getCourseid();
        // 页面传入过来的parentId
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)) {
            // 到这里,也就是说,在表单中没有选择 `上级节点` 的表格,
            // 所以要从数据库中根据课程id取出根节点
            parentid = this.getTeachplanRoot(courseId);
        }
        // 查询根节点信息
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        Teachplan teachplan1 = optional.get();
        //父结点的级别
        String parent_grade = teachplan1.getGrade();
        // 新节点
        Teachplan newTeachplan = new Teachplan();
        // 将页面提交的teachplan信息拷贝到newTeachplan对象中
        BeanUtils.copyProperties(teachplan, newTeachplan);
        newTeachplan.setParentid(parentid);
        newTeachplan.setCourseid(courseId);
        if (parent_grade.equals("1")) {
            newTeachplan.setGrade("2");
        } else {
            newTeachplan.setGrade("3");
        }
        teachplanRepository.save(newTeachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //获取课程的根结点
    public String getTeachplanRoot(String courseId) {

        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();

        // 查询课程的根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplanList == null || teachplanList.size() <= 0) {
            // 到这里说明没有查到课程的根节点,说明该课程是新建的课程,要自动往数据库中添加一个根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName()); // 课程名称
            teachplan.setStatus("0");
            // 将这个课程计划的保存到数据库中(也就是一个根节点)
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }
        return teachplanList.get(0).getId();
    }

    // 获取我的课程; courseListRequest用来后续扩展,对课程排序、等其他操作的时候使用
    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if (courseListRequest == null)
            courseListRequest = new CourseListRequest();
        if (page <= 0) page = 0;
        if (size <= 0) size = 20;

        // 使用分页插件进行分页
        PageHelper.startPage(page, size);
        Page<CourseInfo> courseList = courseMapper.findCourseList(courseListRequest);
        List<CourseInfo> resultList = courseList.getResult();
        // 记录总数
        long total = courseList.getTotal();

        // 封装结果集
        QueryResult<CourseInfo> courseInfo = new QueryResult();
        courseInfo.setList(resultList);
        courseInfo.setTotal(total);

        return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS, courseInfo);
    }

    public AddCourseResult addCourseBase(CourseBase courseBase) {
        // 课程状态默认为未发布
        courseBase.setStatus("202001");
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }

    public CourseBase getCourseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        CourseBase cb = this.getCourseById(id);
        if (cb == null)
            throw new RuntimeException("回写数据失败!");

        // 修改课程信息
        cb.setName(courseBase.getName());
        cb.setSt(courseBase.getSt());
        cb.setMt(courseBase.getMt());
        cb.setGrade(courseBase.getGrade());
        cb.setStudymodel(courseBase.getStudymodel());
        cb.setUsers(courseBase.getUsers());
        cb.setDescription(courseBase.getDescription());
        // 将修改后的课程信息保存到数据库中
        courseBaseRepository.save(cb);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CourseMarket getCourseMarketById(String courseid) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseid);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Transactional
    public CourseMarket updateCourseMarket(String id, CourseMarket courseMarket) {
        // 首先回写之前的数据到表单
        CourseMarket one = this.getCourseMarketById(id);
        if (one != null) {
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());  // 课程有效期,开始时间
            one.setEndTime(courseMarket.getEndTime());  // 课程有效期结束时间
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
            courseMarketRepository.save(one);
        } else {
            // 添加课程营销信息
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, one);
            // 设置课程id
            one.setId(id);
            courseMarketRepository.save(one);
        }
        return one;
    }

    // 向课程管理 添加课程与图片的关联信息
    @Transactional
    public ResponseResult addCoursePic(String courseId, String pic) {
        // 课程图片信息
        CoursePic coursePic = null;
        // 查询课程图片
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()) {
            // 如果有图片,可以更新
            coursePic = optional.get();
        }
        if (coursePic == null) {
            coursePic = new CoursePic();
        }
        coursePic.setPic(pic);
        coursePic.setCourseid(courseId);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    // 查询课程图片
    public CoursePic findCoursePic(String courseId) {
        // 查询课程图片
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()) {
            CoursePic coursePic = optional.get();
            return coursePic;
        }
        return null;
    }

    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {

        // 删除图片
        long count = coursePicRepository.deleteByCourseid(courseId);
        if (count > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    // 查询课程详情(视图), 包括基本信息、图片、营销信息、课程计划
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        // 查询课程基本信息
        CourseBase courseBase = this.getCourseById(id);
        courseView.setCourseBase(courseBase);

        // 查询课程图片
        CoursePic coursePic = this.findCoursePic(id);
        courseView.setCoursePic(coursePic);

        // 查询营销信息
        CourseMarket courseMarket = this.getCourseMarketById(id);
        courseView.setCourseMarket(courseMarket);

        // 查询课程计划信息
        TeachplanNode teachplanNode = this.findTeachplanList(id);
        courseView.setTeachplanNode(teachplanNode);

        return courseView;
    }


/*    @Value("${course‐publish.pagePhysicalPath}")
    private String pagePhysicalPath;

    @Value("${course‐publish.pageWebPath}")
    private String pageWebPath;

    @Value("${course‐publish.siteId}")
    private String siteId;

    @Value("${course‐publish.templateId}")
    private String templateId;

    @Value("${course‐publish.previewUrl}")
    private String previewUrl;*/

    //根据id查询课程基本信息
    public CourseBase findCourseBaseById(String courseId) {
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if (baseOptional.isPresent()) {
            CourseBase courseBase = baseOptional.get();
            return courseBase;
        }
        ExceptionCast.cast(CourseCode.COURSE_DENIED_DELETE);
        return null;
    }

    // 课程预览
    public CoursePublishResult preview(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseBaseById(id);
        //请求cms添加页面
        //准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("5b30cba5f58b4411fc6cb1e5");//站点id
        cmsPage.setDataUrl("http://localhost:31200/course/courseview/" + id);//数据模型url
        cmsPage.setPageName(id + ".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath("/course/detail/");//页面物理路径
        cmsPage.setPageWebPath("/course/detail/");//页面webpath
        cmsPage.setTemplateId("5b345a6b94db44269cb2bfec");//页面模板id

        //远程调用cms
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }

        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //拼装页面预览的url
        String url = "http://www.xuecheng.com/cms/preview/" + pageId;
        //返回CoursePublishResult对象（当中包含了页面预览的url）
        return new CoursePublishResult(CommonCode.SUCCESS, url);
    }

    //课程发布
    @Transactional
    public CoursePublishResult publish(String id) {
        //查询课程
        CourseBase courseBaseById = this.findCourseBaseById(id);

        //准备cmsPage信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("5b30cba5f58b4411fc6cb1e5");//站点id
        cmsPage.setDataUrl("http://localhost:31200/course/courseview/" + id);//数据模型url
        cmsPage.setPageName(id + ".html");//页面名称
        cmsPage.setPageAliase(courseBaseById.getName());//页面别名，就是课程名称
        cmsPage.setPagePhysicalPath("/course/detail/");//页面物理路径
        cmsPage.setPageWebPath("/course/detail/");//页面webpath
        cmsPage.setTemplateId("5b345a6b94db44269cb2bfec");//页面模板id
        //调用cms一键发布接口将课程详情页面发布到服务器
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if(!cmsPostPageResult.isSuccess()){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        //保存课程的发布状态为“已发布”
        CourseBase courseBase = this.saveCoursePubState(id);
        if(courseBase == null){
            return new CoursePublishResult(CommonCode.FAIL,null);
        }

        //保存课程索引信息
        //...

        //缓存课程的信息
        //...
        //得到页面的url
        String pageUrl = cmsPostPageResult.getPageUrl();
        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    //更新课程状态为已发布 202002
    private CourseBase  saveCoursePubState(String courseId){
        CourseBase courseBaseById = this.findCourseBaseById(courseId);
        courseBaseById.setStatus("202002");
        courseBaseRepository.save(courseBaseById);
        return courseBaseById;
    }
}
