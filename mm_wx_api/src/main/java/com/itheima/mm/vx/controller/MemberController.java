package com.itheima.mm.vx.controller;

import com.alibaba.fastjson.JSONObject;
import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.framework.annotation.HmSetter;
import com.itheima.mm.base.BaseController;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.WxMember;
import com.itheima.mm.utils.DateUtils;
import com.itheima.mm.vx.service.WxmemberService;
import com.itheima.mm.vx.utils.WxUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.itheima.mm.common.GlobalConst.HEADER_AUTHORIZATION;

/**
 * @author ：hodor007
 * @date ：Created in 2020/11/3
 * @description ：
 * @version: 1.0
 */
@HmComponent
@Slf4j
public class MemberController extends BaseController {
    @HmSetter("WxmemberService")
    private WxmemberService wxmemberService;

    @HmRequestMapping("/member/login")
    public void userLogin (HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        try{
            //获取数据
            Map<String, String> data = parseJSON2Object(request, HashMap.class);
            log.info("###MemberController-userLogin-params###:{}",data);
            String code = data.get("code");    //登录凭证
            String encryptedData = data.get("encryptedData");    //加密数据
            String iv = data.get("iv");    //解密参数

            //根据微信工具类获取OpenId和SessionKey
            JSONObject jsonObject = WxUtil.get(code);
            log.info("###微信工具类获取OpenId和SessionKey:{}###",jsonObject);
            String session_key = jsonObject.getString("session_key");
            String openid = jsonObject.getString("openid");

            //调用service,完成登录与注册
            WxMember wxMemberByOpenId = wxmemberService.findWxMemberByOpenId(openid);

            //如果数据库中不存在，则注册微信会员
            if(wxMemberByOpenId == null){
                JSONObject userInfo = WxUtil.getUserInfo(encryptedData, session_key, iv);
                wxMemberByOpenId = new WxMember();
                wxMemberByOpenId.setCountry(userInfo.getString("country"));
                wxMemberByOpenId.setNickName(userInfo.getString("nickName"));
                wxMemberByOpenId.setAvatarUrl(userInfo.getString("avatarUrl"));
                wxMemberByOpenId.setGender(userInfo.getString("gender"));
                wxMemberByOpenId.setCity(userInfo.getString("city"));
                wxMemberByOpenId.setProvince(userInfo.getString("province"));
                wxMemberByOpenId.setCountry(userInfo.getString("country"));
                wxMemberByOpenId.setLanguage(userInfo.getString("language"));
                wxMemberByOpenId.setOpenId(userInfo.getString("openId"));
                wxMemberByOpenId.setUnionId(userInfo.getString("unionId"));
                wxMemberByOpenId.setCreateTime(DateUtils.parseDate2String(new Date()));
                log.debug("调用Service,保存会员数据 wxMember:{}",wxMemberByOpenId);
                wxmemberService.insertWxmember(wxMemberByOpenId);
            }

            //返回数据

            Map map = new HashMap();
            map.put("token",openid);
            map.put("userInfo",wxMemberByOpenId);
            printResult(response,new Result(true,"注册成功",map));
        } catch (RuntimeException e){
            e.printStackTrace();
            log.error("",e);
        }
    }

    @HmRequestMapping("/member/updateCityCourse")
    public void updateCityCourse (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        try{
            //获取数据，从请求头中获取openId
            HashMap map = parseJSON2Object(request, HashMap.class);
            //从请求头获取openId,调用父类函数
            String openId = request.getHeader(HEADER_AUTHORIZATION);
            map.put("openId",openId);
            //调用service更新学科和城市id
            wxmemberService.updateCityCourseByOpenId(map);
            log.info("updateCityCourse-MemberController-params:{}",map);
            printResult(response,new Result(true,"更新成功"));
        } catch (RuntimeException e){
            printResult(response,new Result(false,"更新失败"));
            e.printStackTrace();
            log.error("",e);
        }
    }

    @HmRequestMapping("/member/center")
    public void getUserCenter (HttpServletRequest request, HttpServletResponse response) throws
                IOException, ServletException{
        try{
            //从请求头中获取openId
            String openId = getHeaderAuthorization(request);
            //调用serrvice根据openId查询用户中心数据，categoryTitle根据categoryKid变化
            Map<String,Object> resultMap = wxmemberService.selectUserCenterById(openId);
            printResult(response,new Result(true,"查询用户中心信息成功",resultMap));
        } catch (RuntimeException e){
            printResult(response,new Result(false,"查询用户中心信息失败"));
            e.printStackTrace();
            log.error("",e);
        }
    }
}
