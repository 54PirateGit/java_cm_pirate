package com.chengm.pirate.utils;

/**
 * program: CmPirate
 * description: 生产key
 * author: ChengMo
 * create: 2019-12-15 10:19
 **/
public class RedisKeyUtils {

    /**
     * 用户密码输入错误次数
     */
    public static String getPwdInputCountKey(String tag) {
        return tag + "_pwd_error_count";
    }

    /**
     * 邮箱验证码每天限制次数
     */
    public static String getMailCodeKey(String tag) {
        return tag + "_mail_code";
    }

    /**
     * 短信验证码每天限制次数
     */
    public static String getPhoneCodeKey(String tag) {
        return tag + "_phone_code";
    }

}
