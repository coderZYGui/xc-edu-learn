package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //创建jwt令牌
    @Test
    public void testRedis(){
        //定义key
        String key = "b1527e74-3b52-494e-a21f-acb6ab6e3bba";
        //定义value
        Map<String,String> value = new HashMap<>();
        value.put("jwt","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU5MTkxNjY3OCwianRpIjoiYjE1MjdlNzQtM2I1Mi00OTRlLWEyMWYtYWNiNmFiNmUzYmJhIiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.E5divV4noR-MC0ruSQLCqYRuMqNxhcTJbKDXOfA715sy8G6CcntULzrwaYxFTAWlqUg6iTk8AB4Xwr9WpUG_E_fr6NWTFOfovKJ8oUPTfi4_E3J5k5xWjkC-H176b2HgJZB7b0TXeRL7Y7vZ7WmE6jEaglwWGJq7QYuRx2OfbuXL-NslYNOYK2BOQC2Zn0vThBzooyVrDaiI28sSX35zuBHAYX_gkcPfyt7ry8UqWde0YxLYQkZoLm6cxJDwCoPSkr5O4xgYX0dE-rC65mtfA23XYfdn1agQlsMSAVTQ-LzuSR2t6r3uowAmQeA3pklxzYcAtEwUX4YDF8UvjIuGbQ");
        value.put("refresh_token","eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOm51bGwsInVzZXJwaWMiOm51bGwsInVzZXJfbmFtZSI6Iml0Y2FzdCIsInNjb3BlIjpbImFwcCJdLCJhdGkiOiJiMTUyN2U3NC0zYjUyLTQ5NGUtYTIxZi1hY2I2YWI2ZTNiYmEiLCJuYW1lIjpudWxsLCJ1dHlwZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MTU5MTkxNjY3OCwianRpIjoiZGM2YTJkM2YtNTM4ZS00MDU0LWFlZGMtMWM0MzU3Yzg5ZDQ2IiwiY2xpZW50X2lkIjoiWGNXZWJBcHAifQ.AtF2xngozdC0lt80lro7yW2_m3f0TKujGcuGOHc53l8EEygTPIhAQKQB5JikS9wtSdsog3WxYF6JX2xwuntfQCHz_HutHFdujHyiY1we3wj7OF8deZDhvW7SVpLy9OVl047-ugSvdKou8JEW7xXRPY9d8TSv3ssGyKU7OeLPPxXKaqoNlEexi97-8SelRqtOe583T3gyLIEfsw6lCVx-eCDOAKr6F2TX9yTDGAF7DQ-PsXAaGYi7gDDv24d0F60NDf8Pra-8iGxztSiTl8JCkBQgjVeJpcIpnK0wRIbbPccXvRBJobrtMA8yF392j6ZKYgyoU3TErD0r1BO_1LAw3w");
        String jsonString = JSON.toJSONString(value);
        //校验key是否存在，如果不存在则返回-2
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        System.out.println(expire);
        //存储数据
        stringRedisTemplate.boundValueOps(key).set(jsonString,30, TimeUnit.SECONDS);
        //获取数据
        String string = stringRedisTemplate.opsForValue().get(key);
        System.out.println(string);


    }


}
