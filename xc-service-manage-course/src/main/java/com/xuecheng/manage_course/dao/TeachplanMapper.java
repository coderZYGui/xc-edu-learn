package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: 课程计划接口
 *
 * @author zygui
 * @date Created on 2020/5/16 23:51
 */

@Mapper
public interface TeachplanMapper {

    // 课程计划查询
    public TeachplanNode selectList(String courseId);
}
