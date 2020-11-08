package com.itheima.mm.service.impl;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.common.QuestionConst;
import com.itheima.mm.dao.*;
import com.itheima.mm.database.MmDaoException;
import com.itheima.mm.database.SqlSessionUtils;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.*;
import com.itheima.mm.service.QuestionService;
import com.itheima.mm.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/26
 * @description ：
 * @version: 1.0
 */
@HmComponent("questionService")
@Slf4j
public class QuestionServiceImpl extends BaseService implements QuestionService {
    @Override
    public PageResult findListByPage(QueryPageBean queryPageBean) {
        log.info("###QuestionServiceImpl-queryParams:{}###",queryPageBean);
        if(queryPageBean.getQueryParams() == null){
            log.info("###ReplaceNewQueryParams-QuestionService###");
            Map map = new HashMap<>();
            map.put("isClassic",0);
            queryPageBean.setQueryParams(map);
        }
        log.info("###QuestionServiceImpl-queryParams:{}###",queryPageBean);
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try {
            QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
            Long count = questionDao.selectCount(queryPageBean);
            List<Question> list = questionDao.selectListByPage(queryPageBean);
            closeSession(sqlSession);
            return new PageResult(count,list);
        } catch (MmDaoException e) {
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }

    @Override
    public void addOrUpdate(Question question) {
        SqlSession sqlSession = getSession();
        try{
            log.info("###addOrUpdate-QuestionService###:{}",question);
            //isClassic=null, status=null, reviewStatus=null, createDate=null赋值题目初始化
            //保存题目信息
            updateQuestion(sqlSession,question);
            //保存题目选项信息
            updateQuestionItem(sqlSession,question);
            //保存标签信息（题目与标签的关系）
            updateQuestionTag(sqlSession,question);
            //更新企业以及行业的信息
            updateCompanyIndustry(sqlSession,question);
            commitAndCloseSession(sqlSession);
        } catch (MmDaoException e){
            rollbackAndCloseSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
            //为什么还要抛出异常,在controller中捕获
            throw new MmDaoException(e.getMessage());
        }
    }

    //更新题目信息，根据传入的参数是question是否有id判断是新增还是修改
    private void updateQuestion(SqlSession sqlSession, Question question){
        question.setIsClassic(QuestionConst.ClassicStatus.CLASSIC_STATUS_NO.ordinal());
        question.setStatus(QuestionConst.Status.PRE_PUBLISH.ordinal());
        question.setReviewStatus(QuestionConst.ReviewStatus.PRE_REVIEW.ordinal());
        question.setCreateDate(DateUtils.parseDate2String(new Date()));
        //获取dao
        QuestionDao questionDao = getDao(sqlSession, QuestionDao.class);
        //根据是否有id判断是新增还是修改
        if(question.getId() == 0){
            questionDao.add(question);
            log.info("###新增题干成功：###{}",question.getId());
        } else {
            questionDao.update(question);
            log.info("###更新题干成功###");
        }
    }

    private void updateQuestionItem(SqlSession sqlSession,Question question){
        QuestionItemDao questionItemDao = getDao(sqlSession, QuestionItemDao.class);
        //遍历题目的选项,逐个保存
        for (QuestionItem questionItem : question.getQuestionItemList()) {
            //初始化question_id
            questionItem.setQuestionId(question.getId());
            //如果content和imageUrl都不为空则保存
            if(questionItem.getContent() == null || questionItem.getContent().length() == 0){
                if(questionItem.getImgUrl() == null || questionItem.getImgUrl().length() == 0){
                    continue;
                }
            }
            //判断是保存新题目还是修改题目
            if(questionItem.getId() == 0){
                questionItemDao.add(questionItem);
                log.info("###保存题目选项###:{}",questionItem.getId());
            } else {
                questionItemDao.update(questionItem);
                log.info("###更新题目选项###:{}",questionItem);
            }
        }
    }

    /**
     * 更新问题标签
     * @param sqlSession
     * @param question
     */
    private void updateQuestionTag(SqlSession sqlSession,Question question){
        //获取dao
        TagDao tagDao = getDao(sqlSession, TagDao.class);

        //删除原tagList关系表，新增题目的情况下没有tag但是删除不存在的数据不会报错
        tagDao.deleteTagByQuestionId(question.getId());
        //遍历tagList,添加新关系，首先要生成参数Map
        for (Tag tag : question.getTagList()) {
            Map map = new HashMap();
            map.put("questionId",question.getId());
            map.put("tagId",tag.getId());
            tagDao.addQuestionTag(map);
        }
        log.info("###updateQuestionTag-QuestionService：###:{}",question.getTagList());
    }

    private void updateCompanyIndustry(SqlSession sqlSession,Question question){
        //获取dao
        CompanyDao companyDao = getDao(sqlSession, CompanyDao.class);
        //获取并完善参数，添加用户id
        Company company = question.getCompany();
        company.setUserId(question.getUserId());
        //调用dao更新company信息
        companyDao.updateCompanyCity(company);
        //删除旧的关系表
        companyDao.deleteCompanyIndustryByCompanyId(company.getId());
        //遍历添加新的关系表
        for (Industry industry : company.getIndustryList()) {
            Map map = new HashMap();
            map.put("companyId",company.getId());
            map.put("industryId",industry.getId());
            companyDao.addCompanyIndustry(map);
        }
    }

    @Override
    public PageResult findClassicListByPage(QueryPageBean queryPageBean) {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try{
            log.info("findClassicListByPage-params-QuestionServiceImpl:{}",queryPageBean);
            QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
            ReviewLogDao reviewLogDao = sqlSession.getMapper(ReviewLogDao.class);
            if(queryPageBean.getQueryParams() == null){
                log.info("***queryParams为空***");
                Map map = new HashMap();
                queryPageBean.setQueryParams(map);
            }
            log.info("***new_findClassicListByPage-params-QuestionServiceImpl:{}***",queryPageBean);
            //查询本分页的所有精选问题
            queryPageBean.getQueryParams().put("isClassic",1);
            List<Question> questions = questionDao.selectClassicListByPage(queryPageBean);
            //加入审核日志
            for (Question question : questions) {
                ReviewLog reviewLog = reviewLogDao.selectReviewLogByQuestionId(question.getId());
                //没有审核日志的情况
                if(reviewLog == null){
                    reviewLog = new ReviewLog();
                    reviewLog.setComments("");
                }
                question.setReviewLog(reviewLog);
            }
            //查询总数
            Long count = questionDao.selectClassicCount(queryPageBean);
            PageResult pageResult = new PageResult(count, questions);
            return pageResult;
        } catch (MmDaoException e){
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }

    @Override
    public Question findQuestionById(int id) {
        log.info("###QuestionService-findQuestionById-params:{}###",id);
        SqlSession sqlSession = getSession();
        try{
            QuestionDao questionDao = sqlSession.getMapper(QuestionDao.class);
            TagDao tagDao = sqlSession.getMapper(TagDao.class);
            CatalogDao catalogDao = sqlSession.getMapper(CatalogDao.class);
            CompanyDao companyDao = sqlSession.getMapper(CompanyDao.class);
            QuestionItemDao questionItemDao = sqlSession.getMapper(QuestionItemDao.class);
            //查询问题
            Question question = questionDao.selectQuestionById(id);
            //查询学科目录
            Catalog catalog =  catalogDao.selectCatalogById(question.getCatalogId());
            question.setCatalog(catalog);
            //查询公司以及行业方向
            Company company = companyDao.selectCompanyById(question.getCompanyId());
            question.setCompany(company);
            //查询所有的选项
            List<QuestionItem> questionItems = questionItemDao.selectQuestionItemByQuestionId(id);
            question.setQuestionItemList(questionItems);
            //查询标签
            List<Tag> tags = tagDao.selectTagsByQuestionId(id);
            List<String> tagNameList = new ArrayList<>();
            for (Tag tag : tags) {
                tagNameList.add(tag.getName());
            }
            question.setTagList(tags);
            question.setTagNameList(tagNameList);
            closeSession(sqlSession);
            return question;
        } catch (MmDaoException e){
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
            throw new MmDaoException("根据id查询题目失败-QuestionService-findQuestionById-params");
        }
    }
}
