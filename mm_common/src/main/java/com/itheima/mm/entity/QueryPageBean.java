package com.itheima.mm.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author ：seanyang
 * @date ：Created in 2019/8/12
 * @description : 封装查询条件
 * @version: 1.0
 */

@Data
public class QueryPageBean implements Serializable{
    private Integer currentPage; // 页码
    private Integer pageSize;   //每页记录数
    private Map queryParams;    //查询条件
    private Integer offset; // 分页查询，开始记录下标

    /**
     * 获取分页起始记录位置
     * 根据分页页数，计算limit其实记录
     * @return
     */
    public Integer getOffset(){
        return (currentPage-1)*pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Map getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map queryParams) {
        this.queryParams = queryParams;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}