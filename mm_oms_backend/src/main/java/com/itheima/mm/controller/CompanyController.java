package com.itheima.mm.controller;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.framework.annotation.HmSetter;
import com.itheima.mm.base.BaseController;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Company;
import com.itheima.mm.service.CompanyService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/27
 * @description ：
 * @version: 1.0
 */
@HmComponent
@Slf4j
public class CompanyController extends BaseController {
    @HmSetter("companyService")
    private CompanyService companyService;

    @HmRequestMapping("/company/findListAll")
    public void findListAll (HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        try{
            //不需要获取参数，直接调用service
            List<Company> companyList = companyService.findListAll();
            Result result = new Result(true, "查找公司列表成功", companyList);
            printResult(response,result);
        } catch (RuntimeException e){
            printResult(response,new Result(false,"查找公司列表失败"));
            e.printStackTrace();
            log.error("",e);
        }
    }
}
