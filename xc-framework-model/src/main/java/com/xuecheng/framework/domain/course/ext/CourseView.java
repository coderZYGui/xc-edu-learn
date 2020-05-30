package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Description: 课程详情页面模型
 *
 * @author zygui
 * @date Created on 2020/5/29 16:17
 */
@Data
@NoArgsConstructor
@ToString
public class CourseView implements Serializable {
    private CourseBase courseBase;  // 课程的基本信息
    private CoursePic coursePic;    // 课程图片
    private CourseMarket courseMarket; // 课程的营销信息
    private TeachplanNode teachplanNode; // 课程计划
}
