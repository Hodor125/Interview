package com.itheima.mm.service;

import com.itheima.mm.entity.PageResult;
import com.itheima.mm.entity.QueryPageBean;
import com.itheima.mm.pojo.Course;

import java.util.List;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/24
 * @description ：
 * @version: 1.0
 */
public interface CourseService {
    /**
     * 添加学科
     * @param course
     */
    void add(Course course);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult findListByPage(QueryPageBean queryPageBean);

    /**
     * 更新学科
     * @param course
     */
    void updateCourse(Course course);

    /**
     * 删除学科
     * @param id
     */
    void deleteCourseById(Integer id);

    /**
     * 查询所有学科列表
     * @return
     */
    List<Course> findAllCourseList();
}
