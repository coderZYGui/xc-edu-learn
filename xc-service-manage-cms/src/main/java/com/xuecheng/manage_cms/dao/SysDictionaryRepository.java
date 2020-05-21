package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/20 14:10
 */
public interface SysDictionaryRepository extends MongoRepository<SysDictionary, String> {

    // 根据字典分类查询字典信息
    SysDictionary findBydType(String dType);
}
