package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/16 23:30
 */
@Api(value = "课程管理接口", description = "课程管理接口,提供数据模型的管理、查询接口")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询我的课程列表")
    QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程基础信息")
    AddCourseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("获取课程基础信息")
    CourseBase getCourseById(String courseId);

    @ApiOperation("更新课程基础信息")
    ResponseResult updateCourseBase(String id, CourseBase courseBase);

    @ApiOperation("更新课程营销信息")
    ResponseResult updateCourseMarket(String id,CourseMarket courseMarket);

    @ApiOperation("获取课程营销信息")
    CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("添加课程图片")
    ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("查询课程图片")
    CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("课程详情(视图)查询")
    CourseView courseview(String id);

    @ApiOperation("预览课程")
    CoursePublishResult preview(String id);

    @ApiOperation("课程发布")
    CoursePublishResult publish(String id);

    @ApiOperation("保存课程计划与媒资文件关联")
    ResponseResult savemedia(TeachplanMedia teachplanMedia);
}
