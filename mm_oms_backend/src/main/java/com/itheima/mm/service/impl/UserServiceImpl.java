package com.itheima.mm.service.impl;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.PermissionDao;
import com.itheima.mm.dao.RoleDao;
import com.itheima.mm.dao.UserDao;
import com.itheima.mm.database.MmDaoException;
import com.itheima.mm.database.SqlSessionUtils;
import com.itheima.mm.pojo.Permission;
import com.itheima.mm.pojo.Role;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@HmComponent("userService")
public class UserServiceImpl extends BaseService implements UserService {
    @Override
    public User findUserByName(String username) {
        log.debug("username:{}",username);
        SqlSession sqlSession = SqlSessionUtils.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        User user = userDao.selectUserByName(username);
        sqlSession.close();
        return user;
    }

    @Override
    public List<String> getAuthsList(Integer id) {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        List<String> auths = new ArrayList<>();
        try{
            RoleDao roleDao = sqlSession.getMapper(RoleDao.class);
            PermissionDao permissionDao = sqlSession.getMapper(PermissionDao.class);
            //根据用户权限获取角色列表
            List<Role> authorityList = roleDao.getRolesByUserId(id);
            //遍历角色表，将用户权限信息存入集合
            for (Role role : authorityList) {
                auths.add(role.getKeyword());
                Set<Permission> permissions = permissionDao.selectPermissionByRoleId(role.getId());
                for (Permission permission : permissions) {
                    auths.add(permission.getKeyword());
                }
            }
            closeSession(sqlSession);
            return auths;
        } catch (MmDaoException e){
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }
}
