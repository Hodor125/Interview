package com.itheima.mm.service.impl;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.common.QuestionConst;
import com.itheima.mm.dao.QuestionDao;
import com.itheima.mm.dao.ReviewLogDao;
import com.itheima.mm.database.MmDaoException;
import com.itheima.mm.pojo.ReviewLog;
import com.itheima.mm.service.ReviewLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/30
 * @description ：
 * @version: 1.0
 */

@HmComponent("reviewLogService")
@Slf4j
public class ReviewLogServiceImpl extends BaseService implements ReviewLogService {
    @Override
    public void addReviewLog(ReviewLog reviewLog) {
        SqlSession sqlSession = getSession();
        try{
            ReviewLogDao reviewLogDao = sqlSession.getMapper(ReviewLogDao.class);
            QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
            //保存日志
            reviewLogDao.insertReviewLog(reviewLog);
            //更改t_question表中的审核和发布状态
            Map map = new HashMap();
            map.put("id",reviewLog.getQuestionId());
            if(reviewLog.getStatus() == QuestionConst.ReviewStatus.REVIEWED.ordinal()){
                //提交审核
                map.put("reviewStatus",QuestionConst.ReviewStatus.REVIEWED.ordinal());
                map.put("status",QuestionConst.Status.PUBLISHED.ordinal());
            } else if(reviewLog.getStatus() == QuestionConst.ReviewStatus.REJECT_REVIEW.ordinal()) {
                map.put("reviewStatus",QuestionConst.ReviewStatus.REJECT_REVIEW.ordinal());
                map.put("status",QuestionConst.Status.PRE_PUBLISH.ordinal());
            }
            questionDao.updateStatus(map);
            commitAndCloseSession(sqlSession);
        } catch (MmDaoException e){
            rollbackAndCloseSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
    }
}
