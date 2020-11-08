package com.itheima.mm.vx.service.impl;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.mm.base.BaseService;
import com.itheima.mm.database.MmDaoException;
import com.itheima.mm.pojo.WxMember;
import com.itheima.mm.vx.dao.WxMemberDao;
import com.itheima.mm.vx.service.WxmemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/3
 * @description ：
 * @version: 1.0
 */
@HmComponent("WxmemberService")
@Slf4j
public class WxmemberServiceImpl extends BaseService implements WxmemberService {
    @Override
    public WxMember findWxMemberByOpenId(String openId) {
        SqlSession sqlSession = getSession();
        try{
            WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);
            WxMember wxMember = wxMemberDao.selectWxmemberByOpenId(openId);
            closeSession(sqlSession);
            return wxMember;
        } catch (MmDaoException e){
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }

    @Override
    public void insertWxmember(WxMember wxMemberByOpenId) {
        SqlSession sqlSession = getSession();
        try{
            WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);
            wxMemberDao.insertWxmember(wxMemberByOpenId);
            commitAndCloseSession(sqlSession);
        } catch (MmDaoException e){
            rollbackAndCloseSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
    }

    @Override
    public void updateCityCourseByOpenId(HashMap map) {
        SqlSession sqlSession = getSession();
        try{
            WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);
            wxMemberDao.updateCityCourseByOpenId(map);
            commitAndCloseSession(sqlSession);
        } catch (MmDaoException e){
            rollbackAndCloseSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
    }

    @Override
    public Map<String, Object> selectUserCenterById(String openId) {
        SqlSession sqlSession = getSession();
        try{
            log.info("###WxmemberServiceImpl-selectUserCenterById###");
            WxMemberDao wxMemberDao = sqlSession.getMapper(WxMemberDao.class);
            //调用dao查询用户中心数据
            Map<String, Object> resultMap = wxMemberDao.selectUserCenter(openId);
            //根据categoryID和categoryKind获取categoryTitle
            String categoryTitle = wxMemberDao.selectCategoryTitleByCategory(resultMap);
            resultMap.put("categoryTitle",categoryTitle);
            closeSession(sqlSession);
            return resultMap;
        } catch (MmDaoException e){
            closeSession(sqlSession);
            e.printStackTrace();
            log.error("",e);
        }
        return null;
    }
}
