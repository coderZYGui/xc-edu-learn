package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * Description: 封装一下抛出异常的类
 *
 * @author zygui
 * @date Created on 2020/5/10 16:06
 */
public class ExceptionCast {

    // 使用此静态方法抛出自定义异常
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
}
