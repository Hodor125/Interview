package com.itheima.mm.service;

import com.itheima.mm.pojo.Industry;

import java.util.List;

/**
 * @author ：XXXX
 * @date ：Created in 2020/10/26
 * @description ：
 * @version: 1.0
 */
public interface IndustryService {
    /**
     * 查询所有企业
     * @return
     */
    List<Industry> findAllList();
}
