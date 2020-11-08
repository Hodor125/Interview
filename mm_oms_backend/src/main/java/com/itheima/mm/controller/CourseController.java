package com.itheima.mm.controller;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.framework.annotation.HmSetter;
import com.itheima.mm.base.BaseController;
import com.itheima.mm.common.GlobalConst;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.pojo.User;
import com.itheima.mm.security.HmAuthority;
import com.itheima.mm.service.CourseService;
import com.itheima.mm.service.impl.CourseServiceImpl;
import com.itheima.mm.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 学科接口
 * @author ：hodor007
 * @date ：Created in 2020/10/24
 * @description ：
 * @version: 1.0
 */

@HmComponent
@Slf4j
public class CourseController extends BaseController {
    @HmSetter("courseService")
    private CourseService courseService;

    /**
     * 添加学科
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @HmRequestMapping("/course/add")
    public void addUser (HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        /*
        定义RuntimeException方便定位错误，（如数据库连接错误是运维的工作）
         */
        try {
            //获取参数
            Course course = parseJSON2Object(request, Course.class);
            log.debug("course:{}",course);
            //完善数据
            User sessionUser = getSessionUser(request, GlobalConst.SESSION_KEY_USER);
            course.setUserId(sessionUser.getId());
            String date = DateUtils.parseDate2String(new Date());
            course.setCreateDate(date);

            //调用Servlet
            courseService.add(course);

            printResult(response,new Result(true,"添加成功"));
        } catch (RuntimeException e) {
            log.error("",e);
            e.printStackTrace();
            printResult(response,new Result(false,"学科添加失败"));
        }
    }

    /**
     * 查询分页
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @HmRequestMapping("/course/findByListByPage")
    public void findListByPage (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        //获取参数
        try {
            QueryPageBean queryPageBean = parseJSON2Object(request, QueryPageBean.class);
            log.debug("queryParams:{}",queryPageBean);
            log.debug("###查询参数###:{}",queryPageBean.getQueryParams().get("name"));
            log.debug("###查询参数###:{}",queryPageBean.getQueryParams().get("status"));
            log.info("###queryParams为空:{}###",queryPageBean.getQueryParams().size());

            if(queryPageBean == null){
                queryPageBean = new QueryPageBean();
                queryPageBean.setCurrentPage(1);
                queryPageBean.setPageSize(10);
                queryPageBean.setQueryParams(new HashMap());
            }

            //调用service
            PageResult pageResult = courseService.findListByPage(queryPageBean);

            //查询结果
//            PageResult pageResult = new PageResult(33L,new ArrayList());
            Result result = new Result(true, "查询分页成功", pageResult);
            printResult(response,result);
        } catch (RuntimeException e) {
            log.error("",e);
            e.printStackTrace();
            printResult(response,new Result(false,"查询学科列表失败"));
        }
    }

    @HmRequestMapping("/course/update")
    public void updateCourse (HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException{
        try {
            Course course = parseJSON2Object(request, Course.class);
            log.info("###学科更新参数###:{}",course);

            courseService.updateCourse(course);
            printResult(response,new Result(true,"更新学科成功"));
        } catch (RuntimeException e) {
            log.error("",e);
            e.printStackTrace();
            printResult(response,new Result(false,"更新学科失败"));
        }
    }

    @HmRequestMapping("/course/delete")
    public void deleteCourse (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        try {
            String _id =  request.getParameter("id");
            Integer id = Integer.parseInt(_id);
            log.info("###删除学科参数###:{}",id);

            //调用service
            courseService.deleteCourseById(id);

            printResult(response,new Result(true,"删除学科成功"));
        } catch (RuntimeException e) {
            log.error("",e);
            e.printStackTrace();
            printResult(response,new Result(false,"更新学科失败"));
        }
    }

    /**
     * 新增题目使用
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @HmAuthority("COURSE_LIST")
    @HmRequestMapping("/course/findListAll")
    public void findListForQuestion (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        //无需获取数据
        try{
            String _status = request.getParameter("status");
//            Integer status = Integer.parseInt(_status);
            log.info("###CourseController-FindListForQuestion-params###:{}",_status);
            List<Course> courseList = courseService.findAllCourseList();
            Result result = new Result(true,"查询学科列表成功",courseList);
            printResult(response,result);
        } catch (RuntimeException e){
            e.printStackTrace();
            log.error("",e);
            printResult(response,new Result(false,"查询学科列表失败"));
        }
    }
}
