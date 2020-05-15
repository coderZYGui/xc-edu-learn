package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/7 20:44
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {

}
