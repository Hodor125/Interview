package com.itheima.mm.dao;

import com.itheima.mm.pojo.Permission;

import java.util.Set;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/29
 * @description ：
 * @version: 1.0
 */
public interface PermissionDao {
    /**
     * 通过角色id获取权限列表
     * @param id
     * @return
     */
    Set<Permission> selectPermissionByRoleId(Integer id);
}
