package com.itheima.mm.vx.dao;

import com.itheima.mm.pojo.Dict;

import java.util.List;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/1
 * @description ：查询城市列表数据库接口
 * @version: 1.0
 */
public interface DictDao {
    /**
     * 通过城市名查询城市信息
     * @param cityName
     * @return
     */
    public Dict selectDictCityByName(String cityName);

    /**
     * 通过fs查询城市列表
     * @param fs
     * @return
     */
    public List<Dict> selectDictListByFs(Integer fs);
}
