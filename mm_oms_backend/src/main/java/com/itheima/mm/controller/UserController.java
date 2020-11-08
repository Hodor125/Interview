package com.itheima.mm.controller;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.framework.annotation.HmSetter;
import com.itheima.mm.base.BaseController;
import com.itheima.mm.common.GlobalConst;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.UserService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@HmComponent
@Slf4j
public class UserController extends BaseController{
    //控制器需要一个用户业务对象，通过自定义注解注入到当前controller随想控制器
    @HmSetter("userService")
    private UserService userService;

    /**
     * 登录方法
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @HmRequestMapping("/user/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException{
        //获取前端数据，封装到pojo对象
        User user = parseJSON2Object(request, User.class);
        //使用日志框架输出（基于lombok）
        log.debug("user:{}",user);
        //调用service
        //UserService userService = new UserServiceImpl();
        User daoUser = userService.findUserByName(user.getUsername());

        if(daoUser == null){
            Result result = new Result(false, "登录失败,用户不存在！");
            printResult(response,result);
            return;
        }

        if(daoUser.getPassword() != null && daoUser.getPassword().equals(user.getPassword())){
            //获取并设置权限列表
            List<String> authorityList = userService.getAuthsList(daoUser.getId());
            daoUser.setAuthorityList(authorityList);
            log.info("当前用户权限:{}",daoUser);

            Result result = new Result(true, "登录成功", user.getUsername());
            //把对象存入Session
            //true，如果没有Session则创建Session
            HttpSession session = request.getSession();
            session.setAttribute(GlobalConst.SESSION_KEY_USER,daoUser);
            printResult(response,result);
        }
    }

    @HmRequestMapping("/user/logout")
    public void logout (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        //销毁Session，如果没有Session则不需要创建Session
        HttpSession session = request.getSession(false);
        if(session != null){
            log.debug("销毁Session...");
            session.invalidate();
        } else {
            log.debug("Session已失效...");
        }
        printResult(response,new Result(true,"登出成功"));
    }

}
