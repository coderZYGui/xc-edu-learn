package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/20 13:31
 */
@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    public CategoryNode findList() {
        return categoryMapper.selectList();
    }
}
