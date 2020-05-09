package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
