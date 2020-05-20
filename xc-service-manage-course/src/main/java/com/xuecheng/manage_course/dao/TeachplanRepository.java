package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/17 0:45
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {
    // 查询根据课程id和parentid查询teachplan
    public List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}
