package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
}
