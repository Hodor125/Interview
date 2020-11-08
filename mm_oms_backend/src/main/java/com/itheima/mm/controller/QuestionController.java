package com.itheima.mm.controller;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.framework.annotation.HmSetter;
import com.itheima.mm.base.BaseController;
import com.itheima.mm.common.GlobalConst;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Question;
import com.itheima.mm.pojo.User;
import com.itheima.mm.service.QuestionService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/26
 * @description ：
 * @version: 1.0
 */

@HmComponent
@Slf4j
public class QuestionController extends BaseController {
    @HmSetter("questionService")
    private QuestionService questionService;

    @HmRequestMapping("/question/findListByPage")
    public void findListByPage (HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        try {
            QueryPageBean queryPageBean = parseJSON2Object(request, QueryPageBean.class);
            log.info("###queryParams-QuestionController:###:{}",queryPageBean);

            if(queryPageBean == null){
                queryPageBean = new QueryPageBean();
                queryPageBean.setCurrentPage(1);
                queryPageBean.setPageSize(10);
            }

            PageResult pageResult = questionService.findListByPage(queryPageBean);

            Result result = new Result(true, "题库查询分页成功", pageResult);
            printResult(response,result);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error("",e);
            printResult(response,new Result(false,"题库分页查询失败"));
        }
    }

    @HmRequestMapping("/question/addOrUpdate")
    public void addOrUpdate (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        try{
            //获取数据
            Question question = parseJSON2Object(request, Question.class);
            log.info("###addOrUpdate-QuestionController:{}###",question);
            //设置用户id,从session中获取
            User sessionUser = getSessionUser(request, GlobalConst.SESSION_KEY_USER);
            //方便调试使用
            question.setUserId(sessionUser == null ? 1 : sessionUser.getId());
            //调用service
            questionService.addOrUpdate(question);

            //返回json,没有result数据
            printResult(response,new Result(true,"新增题目成功"));
        } catch (RuntimeException e){
            e.printStackTrace();
            log.error("",e);
            printResult(response,new Result(false,"新增题目失败" + e.getMessage()));
        }
    }

    /**
     * 精选试题列表分页查询
     */
    @HmRequestMapping("/question/findClassicListByPage")
    public void findClassicListByPage (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        try{
            //获取参数
            QueryPageBean queryPageBean = parseJSON2Object(request, QueryPageBean.class);
            //防止意外情况？
            if(queryPageBean == null){
                queryPageBean = new QueryPageBean();
                queryPageBean.setPageSize(10);
                queryPageBean.setCurrentPage(1);
            }
            PageResult pageResult = questionService.findClassicListByPage(queryPageBean);
//            PageResult pageResult = new PageResult(33L, new ArrayList());
            printResult(response,new Result(true,"精选题库分页查询成功",pageResult));
        } catch (RuntimeException e){
            e.printStackTrace();
            log.error("",e);
            printResult(response,new Result(false,"精选题库分页查询失败"));
        }
    }

    /**
     * 精选题目审核
     */
    @HmRequestMapping("/question/findQuestionById")
    public void findQuestionById (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        try{
            String _id = request.getParameter("id");
            log.info("###QuestionController-findQuestionById-params###:{}",_id);
            int id = Integer.parseInt(_id);
            Question question =  questionService.findQuestionById(id);

            printResult(response,new Result(true,"根据id查询精选题目成功",question));

        } catch (RuntimeException e){
            e.printStackTrace();
            log.error("",e);
            printResult(response,new Result(true,"根据id查询精选题目失败"));
        }
    }
}
