package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/5/20 14:05
 */
@Api(value = "数据字典接口",description = "提供数据字典接口的管理、查询功能")
public interface SysDictionaryControllerApi {

    // 数据字典
    @ApiOperation("数据字典查询接口")
    public SysDictionary getByType(String type);
}
