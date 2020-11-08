package com.itheima.mm.dao;

import com.itheima.mm.pojo.Industry;

import java.util.List;

/**
 * @author ：XXXX
 * @date ：Created in 2020/10/26
 * @description ：
 * @version: 1.0
 */
public interface IndustryDao {
    /**
     * 查找所有的企业信息
     * @return
     */
    List<Industry> findAllList();

    /**
     * 根据公司id查询所有的方向id
     * @param id
     * @return
     */
    List<Industry> selectIndustryListByCompanyId(Integer id);
}
