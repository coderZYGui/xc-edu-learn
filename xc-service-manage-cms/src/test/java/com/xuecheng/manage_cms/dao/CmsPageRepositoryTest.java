package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/7 20:45
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    // 查询总记录数
    @Test
    public void testFindAll() {
        List<CmsPage> all = cmsPageRepository.findAll();
        for (CmsPage cmsPage : all) {
            System.out.println("cmsPage = " + cmsPage);
        }
    }

    // 分页查询
    @Test
    public void testFindPage() {
        // 分页参数
        int page = 0; // 从0开启
        int size = 10; // 每页10条记录
        PageRequest pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        for (CmsPage cmsPage : all) {
            System.out.println("cmsPage = " + cmsPage);
        }
    }

    /**
     * 根据条件查询
     */
    @Test
    public void testFindAllByCondition() {
        // 分页参数
        int page = 0; // 从0页开始
        int size = 20; // 每页显示10条数据
        Pageable pageable = PageRequest.of(page, size);

        // 条件值对象(将条件封装到CmsPage对象中)
        CmsPage cmsPage = new CmsPage();
        // 根据站点id、模板id来查询页面
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsPage.setTemplateId("5a925be7b00ffc4b3c1578b5");
        // 条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        // 定义Example
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        List<CmsPage> content = all.getContent();
        System.out.println("content = " + content);
    }

    /**
     * 根据条件查询(模糊查询)
     */
    @Test
    public void testFindAllByCondition2() {
        // 分页参数
        int page = 0; // 从0页开始
        int size = 20; // 每页显示10条数据
        Pageable pageable = PageRequest.of(page, size);

        // 条件值对象(将条件封装到CmsPage对象中)
        CmsPage cmsPage = new CmsPage();
        // 根据站点id、模板id来查询页面
        cmsPage.setPageAliase("轮播");
        // 条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        // 定义Example
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        List<CmsPage> content = all.getContent();
        System.out.println("content = " + content);
    }

    // 添加 cmsPageRepository.save(), 删除: deleteById(主键)

    // 修改
    @Test
    public void testUpdate() {
        // 查询对象
        Optional<CmsPage> optional = cmsPageRepository.findById("5ad99fb768db523ef42cd02dXXXXXXXXXX");
        if (optional.isPresent()) { // 是否存在值
            // 设置要修改的值
            CmsPage cmsPage = optional.get();
            cmsPage.setPageAliase("xxxxxxxxxx");
            // ...

            // 修改操作
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println("save = " + save);
        }
    }

    // 根据方法命名规则来查询
    @Test
    public void testFindByPageName() {
        CmsPage pageName = cmsPageRepository.findByPageName("preview_123.html");
        System.out.println("pageName = " + pageName);
    }

}
