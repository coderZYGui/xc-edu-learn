package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/20 13:34
 */
@Mapper
public interface CategoryMapper {
    //查询分类
    CategoryNode selectList();
}
