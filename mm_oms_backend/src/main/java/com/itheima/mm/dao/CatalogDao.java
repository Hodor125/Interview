package com.itheima.mm.dao;

import com.itheima.mm.pojo.Catalog;

import java.util.List;

/**
 * @author ：XXXX
 * @date ：Created in 2020/10/26
 * @description ：
 * @version: 1.0
 */
public interface CatalogDao {
    /**
     * 根据学科id查询标签，新增题目使用
     * @param id
     * @return
     */
    List<Catalog> selectTagListByCourseId(Integer id);

    /**
     * 根据id查询学科目录，审核问题预览使用
     * @param catalogId
     * @return
     */
    Catalog selectCatalogById(Integer catalogId);
}
