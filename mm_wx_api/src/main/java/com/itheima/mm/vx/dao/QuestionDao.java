package com.itheima.mm.vx.dao;

import com.itheima.mm.pojo.Question;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注意和微信小程序前端数据的对应关系（和pojo类Question相对应即可）
 * @author ：XXXX
 * @date ：Created in 2020/11/5
 * @description ：
 * @version: 1.0
 */
public interface QuestionDao {

    /**
     * 根据条件查询完整的题目信息发送到微信小程序前端
     * @param paramsData
     * @return
     */
    List<Question> selectQuestionListByQueryParams(@Param("paramsData") HashMap paramsData);

    /**
     * 保存做题记录
     * @param paramsData
     */
    void addMemberQuestion(@Param("paramsData") Map paramsData);

    /**
     * 更新做题记录
     * @param paramsData
     */
    Integer updateMemberQuestion(@Param("paramsData") Map paramsData);
}
