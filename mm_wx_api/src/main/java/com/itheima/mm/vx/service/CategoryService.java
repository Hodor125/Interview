package com.itheima.mm.vx.service;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/4
 * @description ：
 * @version: 1.0
 */
public interface CategoryService {
    /**
     * 查询题库分页列表
     * @param paramData
     * @return
     */
    List<Map> selectCategoryList(HashMap<String, Object> paramData);

    /**
     * 根据参数查询分类的基本信息和分类下的题目列表
     * @param paramsData
     * @return
     */
    Map<String, Object> selectCategoryQuestionList(HashMap paramsData);

    /**
     * 提交做题记录
     * @param paramsData
     */
    void commitQuestion(Map paramsData);
}
