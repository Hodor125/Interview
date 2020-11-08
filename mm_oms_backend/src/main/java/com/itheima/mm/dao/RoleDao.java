package com.itheima.mm.dao;

import com.itheima.mm.pojo.Role;

import java.util.List;

/**
 * @author ：XXXX
 * @date ：Created in 2020/10/29
 * @description ：role接口
 * @version: 1.0
 */
public interface RoleDao {
    /**
     * 根据用户id获取角色列表
     * @param id
     * @return
     */
    List<Role> getRolesByUserId(Integer id);
}
