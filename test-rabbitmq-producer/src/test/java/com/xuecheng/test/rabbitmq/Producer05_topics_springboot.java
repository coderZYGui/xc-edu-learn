package com.xuecheng.test.rabbitmq;

import com.xuecheng.config.RabbitMQConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/13 18:35
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer05_topics_springboot {

    // 使用rabbitTemplate发送消息
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendEmail() {
        String message = "send email message to user";
        /**
         * arg1: 交换机名称
         * arg2: 路由键
         * arg3: 消息内容
         */
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPICS_INFORM, "inform.email", message);
    }

}
