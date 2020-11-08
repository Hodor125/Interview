package com.itheima.mm.service;

import com.itheima.mm.pojo.User;

import java.util.List;

/**
 * 用户dao接口
 */
public interface UserService {
    /**
     * g根据用户名获取
     * @param username
     * @return
     */
    User findUserByName(String username);

    /**
     * 根据用户id获取权限列表
     * @param id
     * @return
     */
    List<String> getAuthsList(Integer id);
}
