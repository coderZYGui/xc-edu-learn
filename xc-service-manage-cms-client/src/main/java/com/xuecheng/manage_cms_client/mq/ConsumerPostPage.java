package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Description: 监听消息队列,接收页面发布的消息
 *
 * @author zygui
 * @date Created on 2020/5/15 16:59
 */
@Component
public class ConsumerPostPage {

    private static  final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Resource
    private PageService pageService;

    // 用来监听哪个消息队列
    @RabbitListener(queues = "${xuecheng.mq.queue}")
    // 用来接收消息队列发送过来的消息
    public void postPage(String msg) {
        // 解析消息
        Map map = JSON.parseObject(msg, Map.class);
        // 得到消息中的页面Id
        String pageId = (String) map.get("pageId");
        // 验证页面是否合法
        CmsPage cmsPage = pageService.findCmsPageById(pageId);
        if (cmsPage == null) {
            LOGGER.error("receive postpage msg, cmsPage is null, pageId:{}", pageId);
            return;
        }
        // 调用Service方法,将页面从GridFs中下载到服务器
        pageService.savePageToServerPath(pageId);
    }

}
