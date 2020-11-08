package com.itheima.mm.vx.service.impl;

import com.alibaba.fastjson.JSON;
import com.itheima.framework.annotation.HmComponent;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.common.QuestionConst;
import com.itheima.mm.database.MmDaoException;
import com.itheima.mm.pojo.Question;
import com.itheima.mm.vx.dao.CategoryDao;
import com.itheima.mm.vx.dao.QuestionDao;
import com.itheima.mm.vx.dao.WxMemberDao;
import com.itheima.mm.vx.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/4
 * @description ：
 * @version: 1.0
 */
@HmComponent("CategoryService")
@Slf4j
public class CategoryServiceImpl extends BaseService implements CategoryService {
    @Override
    public List<Map> selectCategoryList(HashMap<String, Object> paramData) {
        SqlSession sqlSession = getSession();
        try{
            //判断展示的列表的类型（按学科目录、企业、行业方向）
            Integer type = (Integer) paramData.get("categoryKind");
            if(type == QuestionConst.CategoryKind.CATALOG.getId()){
                //按学科目录
                CategoryDao categoryDao = sqlSession.getMapper(CategoryDao.class);
                List<Map> categoryList = categoryDao.selectCategoryByQueryParams(paramData);
                closeSession(sqlSession);
                return categoryList;
            } else if(type == QuestionConst.CategoryKind.COMPANY.getId()){
                //按企业
            } else if(type == QuestionConst.CategoryKind.INDUSTRY.getId()){
                //按方向
            }
        } catch (MmDaoException e){
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }

    @Override
    public Map<String, Object> selectCategoryQuestionList(HashMap paramsData) {
        SqlSession sqlSession = getSession();
        Map resultMap = null;
        try{
            log.info("###CategoryService-selectCategoryQuestionList###:{}",paramsData);
            //获取分类的类型，执行不同的操作
            Integer type = (Integer) paramsData.get("categoryKind");
            if(type == QuestionConst.CategoryKind.CATALOG.getId()){
                CategoryDao categoryDao = sqlSession.getMapper(CategoryDao.class);
                //复用面试题库按学科分类
                log.info("按某一学科目录获取基本信息");
                //加了categoryId的条件，肯定只有一条数据
                List<Map> list = categoryDao.selectCategoryByQueryParams(paramsData);
                if(list != null && list.size() > 0){
                    resultMap = list.get(0);
                }
                log.info("按某一学科目录获取题目列表");
                //题目中要嵌套子查询标签和选项（选择题）
                QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
                List<Question> questionList = questionDao.selectQuestionListByQueryParams(paramsData);
                resultMap.put("items",questionList);
                return resultMap;
            } else if(type == QuestionConst.CategoryKind.COMPANY.getId()){
                log.info("按某一公司目录获取基本信息");
            } else if(type == QuestionConst.CategoryKind.INDUSTRY.getId()){
                log.info("按某一行业方向目录获取基本信息");
            }
        } catch (MmDaoException e){

            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }

    /**
     * 提交题目
     * @param paramsData
     */
    @Override
    public void commitQuestion(Map paramsData) {
        SqlSession sqlSession = getSession();
        try{
            QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
            WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);
            log.info("CategoryService-commitQuestion-params:{}",paramsData);
            //获取题目的类型
            Integer type = (Integer) paramsData.get("type");
            if(type == QuestionConst.Type.SINGLE.getId() || type == QuestionConst.Type.MULTIPLE.getId()){
                //单选或者多选
                //设置答题情况answerTag
                boolean answerIsRight = (boolean) paramsData.get("answerIsRight");
                if(answerIsRight){
                    paramsData.put("answerTag",QuestionConst.AnswerTag.PERFECT.ordinal());
                } else {
                    paramsData.put("answerTag",QuestionConst.AnswerTag.WRONG.ordinal());
                }
            } else if(type == QuestionConst.Type.SIMPLE.getId()){
                //简答题
                //设置答题情况answerTag
                boolean answerIsRight = (boolean) paramsData.get("answerIsRight");
                if(answerIsRight){
                    paramsData.put("answerTag", QuestionConst.AnswerTag.GOOD.ordinal());
                } else {
                    paramsData.put("answerTag", QuestionConst.AnswerTag.BAD.ordinal());
                }
            }
            //把单选和多选的答案转为json串
            String jsonAns = JSON.toJSONString(paramsData.get("answerResult"));
            paramsData.put("answerResult",jsonAns);
            //处理收藏标记
            if(paramsData.get("isFavorite") != null){
                boolean isFavorite = (boolean) paramsData.get("isFavorite");
                paramsData.put("isFavorite",isFavorite?1:0);
            } else {
                paramsData.put("isFavorite",null);
            }
            log.info("CategoryService-commitQuestion-save-params:{}",paramsData);
            //直接保存做题记录了，应先update判断一下
            Integer row = questionDao.updateMemberQuestion(paramsData);
            //之前的表没有做题记录
            if(row == 0){
                questionDao.addMemberQuestion(paramsData);
            }
            //更新微信用户表的最后做题信息
            wxMemberDao.updateMemberLastRecord(paramsData);
            commitAndCloseSession(sqlSession);
        } catch (RuntimeException e){
            rollbackAndCloseSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
    }
}
