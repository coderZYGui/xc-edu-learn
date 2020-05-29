package com.xuecheng.manage_course;

import com.netflix.discovery.converters.Auto;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/29 14:34
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFeign {

    @Autowired
    private CmsPageClient cmsPageClient;

    @Test
    public void testFeign() {
        // 发起远程调用
        CmsPage cmsPage = cmsPageClient.findCmsPageById("5a754adf6abb500ad05688d9");
        System.out.println("cmsPage = " + cmsPage);
    }
}
