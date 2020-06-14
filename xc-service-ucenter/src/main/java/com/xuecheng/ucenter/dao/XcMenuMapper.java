package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/6/14 12:00
 */
@Mapper
public interface XcMenuMapper {
    //根据用户id查询用户的权限
    List<XcMenu> selectPermissionByUserId(String userid);
}
