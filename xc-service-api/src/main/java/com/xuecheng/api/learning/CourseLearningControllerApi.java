package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 课程学习接口
 *
 * @author zygui
 * @date Created on 2020/6/7 17:15
 */
@Api(value = "录播课程学习管理", description = "录播课程学习管理")
public interface CourseLearningControllerApi {

    @ApiOperation("获取课程学习地址")
    GetMediaResult getmedia(String courseId, String teachplanId);
}
