package com.itheima.mm.dao;

import com.itheima.mm.pojo.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/10/26
 * @description ：
 * @version: 1.0
 */
public interface TagDao {
    /**
     * 根据学科id查询标签，新增题目使用
     * @param id
     * @return
     */
    List<Tag> selectTagsByCourseId(Integer id);

    /**
     * 新增或者修改问题删除旧的关系
     * @param id question_id
     */
    void deleteTagByQuestionId(Integer id);

    /**
     * 新增标签关系
     * @param map
     */
    void addQuestionTag(@Param("map") Map map);

    /**
     * 查询问题的标签
     * @param id
     * @return
     */
    List<Tag> selectTagsByQuestionId(int id);
}
