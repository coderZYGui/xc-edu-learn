package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/7 20:44
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig, String> {


}
