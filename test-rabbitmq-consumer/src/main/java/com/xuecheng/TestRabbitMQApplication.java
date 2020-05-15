package com.xuecheng;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/13 18:16
 */
@SpringBootApplication
@EnableRabbit
public class TestRabbitMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestRabbitMQApplication.class, args);
    }
}
