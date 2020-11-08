package com.itheima.mm.service.impl;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.CompanyDao;
import com.itheima.mm.database.MmDaoException;
import com.itheima.mm.database.SqlSessionUtils;
import com.itheima.mm.pojo.Company;
import com.itheima.mm.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/27
 * @description 查询公司下拉列表
 * @version: 1.0
 */
@HmComponent("companyService")
@Slf4j
public class CompanyServiceImpl extends BaseService implements CompanyService {
    @Override
    public List<Company> findListAll() {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try{
            CompanyDao companyDao = sqlSession.getMapper(CompanyDao.class);
            List<Company> companyList = companyDao.findListAll();
            closeSession(sqlSession);
            return companyList;
        } catch (MmDaoException e){
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }
}
