package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private TeachplanRepository teachplanRepository;

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Resource
    private CourseMarketRepository courseMarketRepository;

    @Resource
    private CoursePicRepository coursePicRepository;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private CmsPageClient cmsPageClient;

    @Resource
    private CoursePubRepository coursePubRepository;

    @Resource
    private TeachplanMediaRepository teachplanMediaRepository;

    @Resource
    private TeachplanMediaPubRepository teachplanMediaPubRepository;

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
    public QueryResponseResult<CourseInfo> findCourseList(String company_id, int page, int size, CourseListRequest courseListRequest) {
        if (courseListRequest == null)
            courseListRequest = new CourseListRequest();

        // 企业id
        courseListRequest.setCompanyId(company_id);

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
        //先创建一个coursePub对象
        CoursePub coursePub = createCoursePub(id);
        //将coursePub对象保存到数据库
        saveCoursePub(id,coursePub);

        //缓存课程的信息
        //...
        //得到页面的url
        String pageUrl = cmsPostPageResult.getPageUrl();

        // 向teachplanMediaPub中保存课程媒资信息
        saveTeachplanMediaPub(id);

        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    //向teachplanMediaPub中保存课程媒资信息
    private void saveTeachplanMediaPub(String courseId){
        //先删除teachplanMediaPub中的数据
        teachplanMediaPubRepository.deleteByCourseId(courseId);
        //从teachplanMedia中查询
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(courseId);
        List<TeachplanMediaPub> teachplanMediaPubs = new ArrayList<>();
        //将teachplanMediaList数据放到teachplanMediaPubs中
        for(TeachplanMedia teachplanMedia:teachplanMediaList){
            TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
            //添加时间戳
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPubs.add(teachplanMediaPub);
        }

        //将teachplanMediaList插入到teachplanMediaPub
        teachplanMediaPubRepository.saveAll(teachplanMediaPubs);
    }

    //将coursePub对象保存到数据库
    private CoursePub saveCoursePub(String id,CoursePub coursePub){

        CoursePub coursePubNew = null;
        //根据课程id查询coursePub
        Optional<CoursePub> coursePubOptional = coursePubRepository.findById(id);
        if(coursePubOptional.isPresent()){
            // coursePubOptional 存在就更新
            coursePubNew = coursePubOptional.get();
        }else{
            coursePubNew = new CoursePub();
        }

        //将coursePub对象中的信息保存到coursePubNew中
        BeanUtils.copyProperties(coursePub,coursePubNew);
        coursePubNew.setId(id);
        //时间戳,给logstach使用
        coursePubNew.setTimestamp(new Date());
        //发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePubNew.setPubTime(date);
        coursePubRepository.save(coursePubNew);
        return coursePubNew;
    }
    //创建coursePub对象(这个对象包含课程信息、课程图片、营销信息、课程计划信息,这些对象的汇总)
    private CoursePub createCoursePub(String id){
        CoursePub coursePub = new CoursePub();
        //根据课程id查询course_base
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(id);
        if(baseOptional.isPresent()){
            CourseBase courseBase = baseOptional.get();
            //将courseBase属性拷贝到CoursePub中
            BeanUtils.copyProperties(courseBase,coursePub);
        }

        //查询课程图片
        Optional<CoursePic> picOptional = coursePicRepository.findById(id);
        if(picOptional.isPresent()){
            CoursePic coursePic = picOptional.get();
            BeanUtils.copyProperties(coursePic, coursePub);
        }

        //课程营销信息
        Optional<CourseMarket> marketOptional = courseMarketRepository.findById(id);
        if(marketOptional.isPresent()){
            CourseMarket courseMarket = marketOptional.get();
            BeanUtils.copyProperties(courseMarket, coursePub);
        }

        //课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.selectList(id);
        String jsonString = JSON.toJSONString(teachplanNode);
        //将课程计划信息json串保存到 course_pub中
        coursePub.setTeachplan(jsonString);
        return coursePub;
    }

    //更新课程状态为已发布 202002
    private CourseBase  saveCoursePubState(String courseId){
        CourseBase courseBaseById = this.findCourseBaseById(courseId);
        courseBaseById.setStatus("202002");
        courseBaseRepository.save(courseBaseById);
        return courseBaseById;
    }

    //保存课程计划与媒资文件的关联信息
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        if(teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //校验课程计划是否是3级
        //课程计划
        String teachplanId = teachplanMedia.getTeachplanId();
        //查询到课程计划
        Optional<Teachplan> optional = teachplanRepository.findById(teachplanId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //查询到教学计划
        Teachplan teachplan = optional.get();
        //取出等级
        String grade = teachplan.getGrade();
        // 如果级别为空或者级别不为3
        if(StringUtils.isEmpty(grade) || !grade.equals("3")){
            //只允许选择第三级的课程计划关联视频
            ExceptionCast.cast(CourseCode.COURSE_MEDIA_TEACHPLAN_GRADEERROR);
        }
        //根据teachplanId去teachplan_media表中查询teachplanMedia, 看是否已经存在
        Optional<TeachplanMedia> mediaOptional = teachplanMediaRepository.findById(teachplanId);
        TeachplanMedia one = null;
        if(mediaOptional.isPresent()){
            // 已经存在就获取该 teachplanMedia,然后更新
            one = mediaOptional.get();
        }else{
            // 不存在则创建
            one = new TeachplanMedia();
        }

        //将one保存到数据库
        one.setCourseId(teachplan.getCourseid());//课程id
        one.setMediaId(teachplanMedia.getMediaId());//媒资文件的id
        one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName());//媒资文件的原始名称
        one.setMediaUrl(teachplanMedia.getMediaUrl());//媒资文件的url
        one.setTeachplanId(teachplanId);
        teachplanMediaRepository.save(one);

        return new ResponseResult(CommonCode.SUCCESS);
    }
}
