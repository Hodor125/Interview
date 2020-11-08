package com.itheima.mm.dao;

import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/10/26
 * @description ：题库数据库操作
 * @version: 1.0
 */
public interface QuestionDao {

    /**
     * 根据参数查询总数
     * @param queryPageBean
     * @return
     */
    Long selectCount(QueryPageBean queryPageBean);

    /**
     * 根据参数查询记录
     * @param queryPageBean
     * @return
     */
    List<Question> selectListByPage(QueryPageBean queryPageBean);

    /**
     * 添加题目
     * @param question
     * @return
     */
    Integer add(Question question);

    /**
     * 更新题目
     * @param question
     * @return
     */
    Integer update(Question question);

    /**
     * 查询精选题目列表
     * @param queryPageBean
     * @return
     */
    List<Question> selectClassicListByPage(QueryPageBean queryPageBean);


    /**
     * 查询精选题目总数
     * @param queryPageBean
     * @return
     */
    Long selectClassicCount(QueryPageBean queryPageBean);

    /**
     * 通过题目id查询题目信息，提供给精选试题审核初始化使用
     * @param id
     * @return
     */
    Question selectQuestionById(int id);

    /**
     * 更新题目的发布状态和审核状态
     * @param map
     */
    void updateStatus(@Param("map") Map map);
}
