package com.xuecheng.mq;

import com.xuecheng.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/13 18:45
 */
@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_EMAIL})
    public void receiveMsg(String msg) {
        System.out.println("接收到的消息是 = " + msg);
    }

}
