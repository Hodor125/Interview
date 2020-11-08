package com.itheima.mm.vx.service.impl;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.database.MmDaoException;
import com.itheima.mm.database.SqlSessionUtils;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.pojo.Dict;
import com.itheima.mm.vx.dao.CourseDao;
import com.itheima.mm.vx.dao.DictDao;
import com.itheima.mm.vx.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/1
 * @description ：
 * @version: 1.0
 */
@HmComponent("commonService")
@Slf4j
public class CommonServiceImpl extends BaseService implements CommonService {
    /**
     * 根据城市名查询location
     * @param cityName
     * @return
     */
    @Override
    public Dict selectDictCityByName(String cityName) {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try{
            DictDao dictDao = sqlSession.getMapper(DictDao.class);
            Dict dict = dictDao.selectDictCityByName(cityName);
            closeSession(sqlSession);
            return dict;
        } catch (MmDaoException e){
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }

    /**
     * 根据fs查询城市列表
     * 0 全部 1 精选
     * @param fs
     * @return
     */
    @Override
    public List<Dict> selectDictListByFs(Integer fs) {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try{
            DictDao dictDao = sqlSession.getMapper(DictDao.class);
            List<Dict> dicts = null;
            if(fs == 0){
                Integer i = null;
                dicts = dictDao.selectDictListByFs(i);
            } else if(fs == 1){
                dicts = dictDao.selectDictListByFs(fs);
            } else {
                throw new MmDaoException("发送的fs没有意义");
            }
            closeSession(sqlSession);
            return dicts;
        } catch (MmDaoException e){
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }

    /**
     * 查询所有学科
     * @return
     */
    @Override
    public List<Course> selectCourseList() {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try{
            CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
            List<Course> courseList = courseDao.selectCourseList();
            closeSession(sqlSession);
            return courseList;
        } catch (MmDaoException e){
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }
}
