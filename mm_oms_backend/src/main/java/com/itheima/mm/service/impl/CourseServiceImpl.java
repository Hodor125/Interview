package com.itheima.mm.service.impl;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.CourseDao;
import com.itheima.mm.database.MmDaoException;
import com.itheima.mm.database.SqlSessionUtils;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.Course;
import com.itheima.mm.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.omg.CORBA.INTERNAL;

import java.util.List;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/24
 * @description ：
 * @version: 1.0
 */
@Slf4j
@HmComponent("courseService")
public class CourseServiceImpl extends BaseService implements CourseService {
    /**
     * 新增学科
     * @param course
     */
    @Override
    public void add(Course course) {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try {
            CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
            Integer row = courseDao.insertCourse(course);
            if(row == 0){
                throw new MmDaoException("添加学科失败");
            }
            commitAndCloseSession(sqlSession);
        } catch (MmDaoException e) {
            rollbackAndCloseSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
    }

    /**
     * 由于queryPageBean的Map属性无法被mybatis解析，只能手动解析
     * @param queryPageBean
     * @return
     */
    public PageResult findListByPage(QueryPageBean queryPageBean){
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try {
            //获取Dao
            CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
            //获取记录数和结果集
            //被迫手动解析
//            String searchName = (String) queryPageBean.getQueryParams().get("name");
//            //if(queryPageBean.getQueryParams().get("name") != null)
//            //    searchName = "%" + (String) queryPageBean.getQueryParams().get("name") + "%";
//
//            //如果是null得到的就是null
//            Integer status = (Integer) queryPageBean.getQueryParams().get("status");
//
//            Integer offset = queryPageBean.getOffset();
//            Integer pageSize = queryPageBean.getPageSize();

            Long count = courseDao.selectCount(queryPageBean);
            List<Course> courseList = courseDao.selectListByPage(queryPageBean);

            closeSession(sqlSession);
            return new PageResult(count,courseList);
        } catch (MmDaoException e) {
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }

    /**
     * 更新学科
     * @param course
     */
    @Override
    public void updateCourse(Course course) {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try {
            //获取Dao
            CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
            Integer row= courseDao.updateCourse(course);
            if(row == 0){
                throw new MmDaoException("更新失败");
            }
            commitAndCloseSession(sqlSession);
        } catch (MmDaoException e) {
            rollbackAndCloseSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
    }

    /**
     * 删除学科
     * @param id
     */
    @Override
    public void deleteCourseById(Integer id) {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try {
            CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
            Long tagCount = courseDao.selectTagCountById(id);
            Long catalogCount = courseDao.selectCatalogCountById(id);
            //学科下有目录或者标签，不能直接删除
            if(tagCount > 0 || catalogCount > 0){
                throw new MmDaoException("删除学科失败");
            }

            Integer row = courseDao.deleteCourse(id);
            //如果行数为0则删除失败
            if(row == 0){
                throw new MmDaoException("删除学科失败");
            }
            commitAndCloseSession(sqlSession);
        } catch (MmDaoException e) {
            rollbackAndCloseSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
    }

    @Override
    public List<Course> findAllCourseList() {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try{
            CourseDao courseDao = sqlSession.getMapper(CourseDao.class);
            List<Course> courseList = courseDao.selectAllCourseList();
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
