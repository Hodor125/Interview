package com.itheima.mm.service;

import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Question;

import java.util.List;

/**
 * @author ：XXXX
 * @date ：Created in 2020/10/26
 * @description ：题库业务
 * @version: 1.0
 */
public interface QuestionService {

    /**
     * 查询分页
     * @param queryParams
     * @return
     */
    PageResult findListByPage(QueryPageBean queryParams);

    /**
     * 保存题目
     * @param question
     */
    void addOrUpdate(Question question);

    /**
     * 精选题库分页
     * @param queryPageBean
     * @return
     */
    PageResult findClassicListByPage(QueryPageBean queryPageBean);

    /**
     * 通过题目id查找题目的所有信息（初始化精选实体审核预览使用）
     * @param id
     * @return
     */
    Question findQuestionById(int id);
}
