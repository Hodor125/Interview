package com.itheima.mm.vx.dao;

import com.itheima.mm.pojo.Course;

import java.util.List;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/2
 * @description ：学科接口
 * @version: 1.0
 */
public interface CourseDao {
    /**
     * 查询学科列表，并对icon进行处理
     * @return
     */
    public List<Course> selectCourseList();
}
