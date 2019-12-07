package com.chengm.pirate.utils.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.chengm.pirate.utils.log.LogUtil;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * program: CmPirate
 * description: token
 * author: ChengMo
 * create: 2019-12-01 15:02
 **/
public class TokenUtil {

    // 过期时间
    private static final long overdeuTime = 30 * 24 * 60 * 60 * 1000L;

    // 私钥uuid生成，确定唯一性
    private static final String tokenSecRet = "7ff0e0f1-a944-465b-bbcf-9aac45caed2e";

    /**
     * 生成token，用户退出后消失
     */
    public static String sign(long userId) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + overdeuTime);
            // token私钥加密
            Algorithm algorithm = Algorithm.HMAC256(tokenSecRet);
            // 设置头部信息
            Map<String, Object> requestHender = new HashMap<>(2);
            requestHender.put("type", "JWT");
            requestHender.put("encryption", "HS256");
            long date1 = new Date().getTime();

            // 返回带有用户信息的签名
            return JWT.create().withHeader(requestHender)
                    .withClaim("userCode", "CM_PIRATE")
                    .withClaim("uid", userId)
                    .withClaim("Time", date1)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 验证token是否正确
     */
    public static boolean tokenVerify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(tokenSecRet);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwtToken = verifier.verify(token);

            LogUtil.logValue("JWT TOKEN", jwtToken.getToken());
            LogUtil.logValue("JWT HEADER", jwtToken.getHeader());
            LogUtil.logValue("JWT SIGN", jwtToken.getSignature());
            LogUtil.logValue("JWT PAYLOAD", jwtToken.getPayload());

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取登陆用户token中的用户ID
     */
    public static long getUserId(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("uid").asLong();
    }

}
