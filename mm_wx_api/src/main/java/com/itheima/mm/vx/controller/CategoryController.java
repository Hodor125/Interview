package com.itheima.mm.vx.controller;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.framework.annotation.HmSetter;
import com.itheima.mm.base.BaseController;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.WxMember;
import com.itheima.mm.vx.service.CategoryService;
import com.itheima.mm.vx.service.WxmemberService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/4
 * @description ：
 * @version: 1.0
 */
@HmComponent
@Slf4j
public class CategoryController extends BaseController {
    @HmSetter("WxmemberService")
    private WxmemberService wxmemberService;

    @HmSetter("CategoryService")
    private CategoryService categoryService;

    /**
     * 题库分类列表
     * 需要 Token 验证，Token 写在请求头 Authorization
     * categoryKind
     *    1-按学科目录  2- 按企业  3-按行业方向
     * categoryType
     *   101-刷题,、201-错题本、202-我的练习、203-收藏题目
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @HmRequestMapping("/category/list")
    public void getCategoryList (HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        try{
            //获取数据
            HashMap data = parseJSON2Object(request, HashMap.class);
            //从请求头获取openId
            String openId = getHeaderAuthorization(request);
            //获取Wxmenber
            WxMember wxMemberByOpenId = wxmemberService.findWxMemberByOpenId(openId);
            //设置参数memberId,cityId,courseId
            data.put("openId",wxMemberByOpenId.getOpenId());
            data.put("memberId",wxMemberByOpenId.getId());
            data.put("courseId",wxMemberByOpenId.getCourseId());
            data.put("cityId",wxMemberByOpenId.getCity());
            log.info("调用service查询题库分类列表:{}",data);

            //调用service查询列表
            List<Map> categoryList = categoryService.selectCategoryList(data);
            //返回数据
            printResult(response,new Result(true,"查询题库分类列表成功",categoryList));
        } catch (RuntimeException e){
            printResult(response,new Result(false,"查询题库列表失败"));
            e.printStackTrace();
            log.error("",e);
        }
    }

    /**
     * 查询所有的题目详情，一次性传输
     * "categoryID" : 1,
     * "categoryKind" : 1,
     * "categoryType" : 101
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @HmRequestMapping("/category/question/list")
    public void getCategoryQuestionList (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        try{
            //获取数据
            HashMap paramsData = parseJSON2Object(request, HashMap.class);
            log.info("###CategoryController-getCategoryQuestionList###:{}",paramsData);
            //从请求头获取openId
            String openId = getHeaderAuthorization(request);
            //获取wxmember
            WxMember wxMemberByOpenId = wxmemberService.findWxMemberByOpenId(openId);
            //填入参数memberId,cityId,courseId
            paramsData.put("memberId",wxMemberByOpenId.getId());
            paramsData.put("cityId",wxMemberByOpenId.getCityId());
            paramsData.put("courseId",wxMemberByOpenId.getCourseId());
            //调用service查询分类的基本信息和分类下的题目列表，返回的参数没有对应的pojo类，使用map存储
            Map<String,Object> resultMap = categoryService.selectCategoryQuestionList(paramsData);
            printResult(response,new Result(true,"查询分类基本信息和分类下的答案列表",resultMap));
        } catch (RuntimeException e){
            printResult(response,"查询失败");
            e.printStackTrace();
            log.error("",e);
        }
    }

    /**
     * 提交答案
     */
    @HmRequestMapping("/category/question/commit")
    public void commitQuestion (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        try{
            Map paramsData = parseJSON2Object(request, HashMap.class);
            log.info("CategoryController-commitQuestion-params:{}",paramsData);
            //从请求头获取openId
            String openId = getHeaderAuthorization(request);
            WxMember wxMemberByOpenId = wxmemberService.findWxMemberByOpenId(openId);
            paramsData.put("memberId",wxMemberByOpenId.getId());
            //调用service
            // 如果之前数据库中没有数据
            categoryService.commitQuestion(paramsData);
            printResult(response,new Result(true,"提交题目成功"));
        } catch (RuntimeException e){
            printResult(response,new Result(false,"提交题目失败"));
            e.printStackTrace();
            log.error("",e);
        }
    }
}
