package com.chengm.pirate.utils.token;

import com.alibaba.fastjson.JSONObject;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.utils.log.LogLevel;
import com.chengm.pirate.utils.log.LogUtil;
import com.chengm.pirate.utils.constant.CodeConstants;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * program: CmPirate
 * description: 拦截并验证token
 * author: ChengMo
 * create: 2019-12-01 15:11
 **/
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("token");
        if (token != null) {
            LogUtil.logValue("Token", token, LogLevel.LEVEL_ERROR);
            if (TokenUtil.tokenVerify(token)) {
                // token 验证通过, 验证头部公共参数


                return true;
            }
        }

        // token 验证不通过
        response.getWriter().write(JSONObject.toJSON(
                AjaxResult.fail(CodeConstants.TOKEN_ERROR, "TOKEN ERROR")).toString()
        );

        LogUtil.logValue("TOKEN ERROR");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
