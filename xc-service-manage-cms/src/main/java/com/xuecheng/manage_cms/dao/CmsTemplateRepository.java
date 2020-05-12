package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/12 21:02
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String> {
}
