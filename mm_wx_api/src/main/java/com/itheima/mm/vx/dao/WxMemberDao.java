package com.itheima.mm.vx.dao;

import com.itheima.mm.pojo.WxMember;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：XXXX
 * @date ：Created in 2020/11/3
 * @description ：
 * @version: 1.0
 */
public interface WxMemberDao {
    /**
     * 登录操作
     * @param openId
     * @return
     */
    public WxMember selectWxmemberByOpenId(String openId);

    /**
     * 注册操作
     * @param wxMemberByOpenId
     */
    void insertWxmember(WxMember wxMemberByOpenId);

    /**
     * 更新城市和学科信息（登录之后的操作）
     */
    void updateCityCourseByOpenId(@Param("map") HashMap map);

    /**
     * 更新微信小程序用户的最后做题信息
     * @param paramsData
     */
    void updateMemberLastRecord(@Param("paramsData") Map paramsData);

    /**
     * 根据openId查询用户中心数据
     * @param openId
     * @return
     */
    Map<String, Object> selectUserCenter(String openId);


    /**
     * 个人中心展示-根据分类不同使用不同的CategoryTitle
     * @param resultMap
     * @return
     */
    String selectCategoryTitleByCategory(@Param("resultMap") Map<String, Object> resultMap);
}
