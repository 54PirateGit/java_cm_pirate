package com.chengm.pirate.utils.token;

import com.alibaba.fastjson.JSONObject;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.utils.StringUtil;
import com.chengm.pirate.utils.constant.Constants;
import com.chengm.pirate.utils.log.LogLevel;
import com.chengm.pirate.utils.log.LogUtil;
import com.chengm.pirate.utils.constant.CodeConstants;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

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
                boolean isTokenPass = verityToken(TokenUtil.getUserId(token), token);
                if (!isTokenPass) {
                    // token 验证不通过
                    response.getWriter().write(JSONObject.toJSON(
                            AjaxResult.fail(CodeConstants.TOKEN_RE_LOGIN, "请重新登录")).toString());
                    return false;
                }

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

    // 验证用户登陆 token
    private boolean verityToken(long uid, String token) {
        if (StringUtil.isEmpty(token)) {
            return false;
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String drive = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(drive);
            conn = DriverManager.getConnection(Constants.SQL_JDBC);
            System.out.println("数据库连接成功");
            String sql = "select token from user_auth where uid=?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, uid);
            rs = stmt.executeQuery();
            String loginToken = null;
            while (rs.next()) {
                loginToken = rs.getString("token");
            }

            // 之前没有token或者token一样都验证通过，一般不会存在没有token的情况
            if (StringUtil.isEmpty(loginToken) || loginToken.equals(token)) {
                return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // 注意关闭原则：从里到外
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
