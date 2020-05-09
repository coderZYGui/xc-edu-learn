package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/7 20:44
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {

    // 根据方法命名规则来查询(和Spring data JPA一样)
    CmsPage findByPageName(String pageName);

}
