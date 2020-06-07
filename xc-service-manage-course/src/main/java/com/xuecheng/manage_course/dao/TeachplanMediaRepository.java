package com.xuecheng.manage_course.dao;


import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/17 0:45
 */
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia, String> {
    // 根据课程id查询teachplan_media表中的信息
    List<TeachplanMedia> findByCourseId(String courseId);
}
