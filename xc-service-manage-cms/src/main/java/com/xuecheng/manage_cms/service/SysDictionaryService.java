package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/20 14:14
 */
@Service
public class SysDictionaryService {

    @Resource
    private SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary findDictionaryByType(String type) {
        SysDictionary sd = sysDictionaryRepository.findBydType(type);
        return sd;
    }
}
