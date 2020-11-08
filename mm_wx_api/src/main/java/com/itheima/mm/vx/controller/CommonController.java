package com.itheima.mm.vx.controller;

import com.alibaba.fastjson.JSON;
import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.framework.annotation.HmSetter;
import com.itheima.mm.base.BaseController;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.pojo.Dict;
import com.itheima.mm.utils.JedisUtils;
import com.itheima.mm.vx.service.CommonService;
import com.itheima.mm.vx.utils.HttpUtil;
import com.itheima.mm.vx.utils.LocationUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.itheima.mm.common.GlobalConst.REDIS_KEY_WX_COURSE_LIST;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/1
 * @description ：
 * @version: 1.0
 */
@HmComponent
@Slf4j
public class CommonController extends BaseController {
    //初始化redis连接池
    static{
        JedisUtils.init(ResourceBundle.getBundle("jedis"));
    }

    @HmSetter("commonService")
    private CommonService commonService;

    @HmRequestMapping("/common/citys")
    public void getCitys (HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        try{
            Map data = parseJSON2Object(request, HashMap.class);
            log.info("CommonController-getCitys-params:{}",data);
            //调用service
            //通过地址信息，解析出城市名称
            String cityName = LocationUtil.parseLocation((String) data.get("location"));
            log.info("###location###:{}",cityName);
//            commonService.selectDictCityByName()
            //调用service,根据城市名称，获取城市信息location(id,city)
            Dict city = commonService.selectDictCityByName(cityName);
            //根据fs,查询获取citys[]
            Integer fs = (Integer) data.get("fs");
            List<Dict> cityList = commonService.selectDictListByFs(fs);
            //返回数据
            Map map = new HashMap();
            map.put("location",city);
            map.put("citys",cityList);
            printResult(response,new Result(true,"查询城市成功",map));
        } catch (RuntimeException e){
            printResult(response,new Result(false,"查询城市失败"));
            e.printStackTrace();
            log.error("",e);
        }
    }

    @HmRequestMapping("/common/courseList")
    public void getCourseList (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        try{
            //如果jedis可用
            if(JedisUtils.isUsed()){
                log.info("jedis可用...");
                Jedis jedis = JedisUtils.getResource();
                String courseList = jedis.get(REDIS_KEY_WX_COURSE_LIST);
                //如果redis中没有数据，读取数据并存储
                if(courseList == null || courseList.length() == 0){
                    log.info("redis可用，没有数据...");
                    List<Course> _courseList = commonService.selectCourseList();
                    log.info("courseList转为json:{}",JSON.toJSONString(_courseList));
                    jedis.set(REDIS_KEY_WX_COURSE_LIST,JSON.toJSON(_courseList).toString());
                    //返回结果
                    printResult(response,new Result(true,"查询学科列表成功",_courseList));
                } else {
                    log.info("reis可用，有数据...");
                    //读取数据并返回,不需要解析成字符串，printResult中解析了
                    log.info("json解析为对象:{}",JSON.parse(courseList));
                    printResult(response,new Result(true,"查询学科列表成功",JSON.parse(courseList)));
                }
            } else {
                //无需获取参数
                //调用service
                List<Course> courseList = commonService.selectCourseList();
                //返回结果
                printResult(response,new Result(true,"查询学科列表成功",courseList));
            }
        } catch (RuntimeException e){
            printResult(response,new Result(false,"查询学科列表失败"));
            e.printStackTrace();
            log.error("",e);
        }
    }
}
