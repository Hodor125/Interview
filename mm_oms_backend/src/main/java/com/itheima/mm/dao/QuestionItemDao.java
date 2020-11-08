package com.itheima.mm.dao;

import com.itheima.mm.pojo.Question;
import com.itheima.mm.pojo.QuestionItem;

import java.util.List;

/**
 * @author ：XXXX
 * @date ：Created in 2020/10/28
 * @description ：
 * @version: 1.0
 */
public interface QuestionItemDao {
    /**
     * 新增题目选项
     * @param questionItem
     */
    public void add(QuestionItem questionItem);

    /**
     * 新增题目选项
     * @param questionItem
     */
    public void update(QuestionItem questionItem);

    /**
     * 根据问题id查询所有的选项
     * @param id
     * @return
     */
    List<QuestionItem> selectQuestionItemByQuestionId(int id);
}
