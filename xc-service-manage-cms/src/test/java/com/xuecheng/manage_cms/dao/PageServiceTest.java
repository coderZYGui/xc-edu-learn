package com.xuecheng.manage_cms.dao;

import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/12 21:40
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {

    @Autowired
    private PageService pageService;

    @Test
    public void testgetPageHtml() {
        String pageHtml = pageService.getPageHtml("5eb7b9a7fc7dbf08c4f3af95");
        System.out.println("pageHtml = " + pageHtml);
    }

}
