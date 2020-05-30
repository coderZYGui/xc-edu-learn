package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description: FeignClient接口
 *
 * @author zygui
 * @date Created on 2020/5/29 14:29
 */
@FeignClient("XC-SERVICE-MANAGE-CMS") // 指定远程调用的服务名
public interface CmsPageClient {

    // 根据页面id查询页面信息, 远程调用cms请求数据
    @GetMapping("/cms/page/get/{id}") // GetMapping来表示远程调用http的方法类型
    CmsPage findCmsPageById(@PathVariable("id") String id);

    // 添加页面, 用于课程预览
    @PostMapping("/cms/page/save")
    CmsPageResult saveCmsPage(@RequestBody CmsPage cmsPage);
}
