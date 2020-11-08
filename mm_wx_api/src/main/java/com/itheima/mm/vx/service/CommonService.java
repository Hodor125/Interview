package com.itheima.mm.vx.service;

import com.itheima.mm.pojo.Course;
import com.itheima.mm.pojo.Dict;

import java.util.List;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/1
 * @description ：
 * @version: 1.0
 */
public interface CommonService {
    /**
     * 通过城市名查询信息location
     * @param cityName
     * @return
     */
    Dict selectDictCityByName(String cityName);

    /**
     * 通过fs查询城市列表 0 全部    1 首屏推荐
     * @param fs
     * @return
     */
    List<Dict> selectDictListByFs(Integer fs);

    /**
     * 查询所有学科
     * @return
     */
    List<Course> selectCourseList();
}
