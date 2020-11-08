package com.itheima.mm.dao;

import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/24
 * @description ：
 * @version: 1.0
 */
public interface CourseDao {
    /**
     * 添加学科
     * @param course
     */
    Integer insertCourse(Course course);


    Long selectCount(QueryPageBean queryPageBean);


    /**
     * 查询分页内容，被迫手动映射
     * @return
     */
    List<Course> selectListByPage(QueryPageBean queryPageBean);


    /**
     * 删除对应学科的id
     * @param course
     * @return
     */
    Integer updateCourse(Course course);

    /**
     * 查询对应学科下的标签数量
     * @param id
     * @return
     */
    Long selectTagCountById(@Param("id") Integer id);

    /**
     * 查询学科目录的数量
     * @param id
     * @return
     */
    Long selectCatalogCountById(@Param("id") Integer id);

    /**
     * 删除学科
     * @param id
     * @return
     */
    Integer deleteCourse(@Param("id") Integer id);

    /**
     * 新增题目使用，查询所有学科列表
     * @return
     */
    List<Course> selectAllCourseList();
}
