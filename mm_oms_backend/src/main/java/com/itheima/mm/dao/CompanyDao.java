package com.itheima.mm.dao;

import com.itheima.mm.pojo.Company;

import java.util.List;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/10/27
 * @description ：公司Dao接口
 * @version: 1.0
 */
public interface CompanyDao {
    /**
     * 获取全部公司信息
     * @return
     */
    List<Company> findListAll();

    /**
     * 更新公司信息(城市和用户id)
     * @param company
     */
    void updateCompanyCity(Company company);

    /**
     * 删除公司和行业方向的关系
     * @param id
     */
    void deleteCompanyIndustryByCompanyId(Integer id);

    /**
     * 添加公司和行业方向的关系
     * @param map
     */
    void addCompanyIndustry(Map map);

    /**
     * 根据id查询公司信息
     * @param companyId
     * @return
     */
    Company selectCompanyById(Integer companyId);
}
