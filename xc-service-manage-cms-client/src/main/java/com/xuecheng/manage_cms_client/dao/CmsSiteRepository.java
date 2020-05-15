package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/7 20:44
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {

}
