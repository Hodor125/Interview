package com.itheima.mm.vx.dao;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/4
 * @description ：学科目录接口
 * @version: 1.0
 */
public interface CategoryDao {
    /**
     * 面试题库按学科分类
     * @param paramData
     * @return
     */
    List<Map> selectCategoryByQueryParams(@Param("paramData") HashMap<String, Object> paramData);
}
