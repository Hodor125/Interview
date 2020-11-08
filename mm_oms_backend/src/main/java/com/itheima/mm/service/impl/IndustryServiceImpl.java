package com.itheima.mm.service.impl;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.dao.IndustryDao;
import com.itheima.mm.database.MmDaoException;
import com.itheima.mm.database.SqlSessionUtils;
import com.itheima.mm.entity.PageResult;
import com.itheima.mm.pojo.Industry;
import com.itheima.mm.service.IndustryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/26
 * @description ：
 * @version: 1.0
 */

@Slf4j
@HmComponent("industryService")
public class IndustryServiceImpl extends BaseService implements IndustryService {
    @Override
    public List<Industry> findAllList() {
        SqlSession sqlSession = SqlSessionUtils.openSession();
        try{
            IndustryDao industryDao = sqlSession.getMapper(IndustryDao.class);
            List<Industry> allList = industryDao.findAllList();
            closeSession(sqlSession);
            return allList;
        } catch (MmDaoException e){
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }
}
