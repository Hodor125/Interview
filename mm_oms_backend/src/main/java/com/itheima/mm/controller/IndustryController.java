package com.itheima.mm.controller;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.framework.annotation.HmSetter;
import com.itheima.mm.base.BaseController;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Industry;
import com.itheima.mm.service.IndustryService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/26
 * @description ：题目方向业务
 * @version: 1.0
 */

@HmComponent
@Slf4j
public class IndustryController extends BaseController {
    @HmSetter("industryService")
    private IndustryService industryService;
    /**
     * 查询方向列表，新增题目使用
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @HmRequestMapping("/industry/findListAll")
    public void findListAll (HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        try{
            //没有参数，直接调用service
            List<Industry> industryList = industryService.findAllList();
            printResult(response,new Result(true,"查询企业列表成功",industryList));
        } catch (RuntimeException e){
            e.printStackTrace();
            log.error("",e);
            printResult(response,new Result(false,"查询企业列表失败"));
        }
    }
}
