package com.xuecheng.manage_course;

import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/29 14:10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRibbon(){
        // 确定要获取的服务名称
        String serviceId = "XC-SERVICE-MANAGE-CMS";
        // ribbon客户端从eurekaServer中获取服务列表, 根据服务名获取服务列表
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://"+serviceId+"/cms/page/get/5a754adf6abb500ad05688d9", Map.class);
        Map body = forEntity.getBody();
        System.out.println("body = " + body);
    }
}
