package com.itheima.mm.dao;

import com.itheima.mm.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserDao {
    User selectUserByName(@Param("username") String username);
}
