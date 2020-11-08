package com.itheima.mm.dao;

import com.itheima.mm.pojo.ReviewLog;

/**
 * @author ：XXXX
 * @date ：Created in 2020/10/30
 * @description ：
 * @version: 1.0
 */
public interface ReviewLogDao {
    /**
     * 根据问题id查询审核日志
     * @param id
     * @return
     */
    ReviewLog selectReviewLogByQuestionId(Integer id);

    /**
     * 保存日志
     * @param reviewLog
     */
    void insertReviewLog(ReviewLog reviewLog);
}
