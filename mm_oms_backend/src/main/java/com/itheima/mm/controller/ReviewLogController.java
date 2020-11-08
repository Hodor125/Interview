package com.itheima.mm.controller;

import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.framework.annotation.HmSetter;
import com.itheima.mm.base.BaseController;
import com.itheima.mm.common.GlobalConst;
import com.itheima.mm.entity.Result;
import com.itheima.mm.pojo.ReviewLog;
import com.itheima.mm.pojo.User;
import com.itheima.mm.security.HmAuthority;
import com.itheima.mm.service.ReviewLogService;
import com.itheima.mm.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * @author ：hodor007
 * @date ：Created in 2020/10/30
 * @description ：
 * @version: 1.0
 */
@HmComponent
@Slf4j
public class ReviewLogController extends BaseController {
    @HmSetter("reviewLogService")
    private ReviewLogService reviewLogService;

    @HmAuthority("QUESTION_REVIEW_UPDATE")
    @HmRequestMapping("/review/add")
    public void addReview (HttpServletRequest request, HttpServletResponse response) throws
            IOException, ServletException {
        //获取参数并完善
        ReviewLog reviewLog = parseJSON2Object(request, ReviewLog.class);
        log.info("###reviewLog-ReviewLogController-params###:{}",reviewLog);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(GlobalConst.SESSION_KEY_USER);
        //方便调试
        int userId = user == null ? 1 : user.getId();
        reviewLog.setUserId(userId);
        reviewLog.setCreateDate(DateUtils.parseDate2String(new Date()));
        log.info("完善###reviewLog-ReviewLogController-params###:{}",reviewLog);
        try{
            //调用service
            reviewLogService.addReviewLog(reviewLog);

            printResult(response,new Result(true,"审核成功"));
        } catch (RuntimeException e){

            printResult(response,new Result(true,"审核失败"));
            e.printStackTrace();
            log.error("",e);
        }
    }
}
