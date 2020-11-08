package com.itheima.mm.vx.service;

import com.itheima.mm.pojo.WxMember;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/3
 * @description ：微信会员接口
 * @version: 1.0
 */
public interface WxmemberService {
    /**
     * 登录操作
     * @param openId
     * @return
     */
    public WxMember findWxMemberByOpenId(String openId);

    /**
     * 注册新用户
     * @param wxMemberByOpenId
     */
    void insertWxmember(WxMember wxMemberByOpenId);

    /**
     * 更新用户信息（学科和城市id）
     */
    void updateCityCourseByOpenId(HashMap map);

    /**
     * 根据微信小程序用户id获取会员中心信息
     * @param openId
     * @return
     */
    Map<String, Object> selectUserCenterById(String openId);
}
