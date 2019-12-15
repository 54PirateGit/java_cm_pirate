package com.chengm.pirate.config;

/**
 * program: CmPirate
 * description: 参数配置
 * author: ChengMo
 * create: 2019-11-30 22:12
 **/
public class AppConfig {

    private AppConfig() {

    }

    /**
     * 生产环境时此变量必须设置 false
     */
    public static boolean IS_DEBUG = true;

    /**
     * 用户密码24小时之内错误次数
     */
    public static int USER_PWD_LIMIT_COUNT = 7;

    /**
     * 邮箱验证码每日获取次数
     */
    public static int MAIL_CODE_LIMIT_COUNT = 7;

    /**
     * 短信验证码每日获取次数
     */
    public static int PHONE_CODE_LIMIT_COUNT = 7;

    /**
     * 账号密码登录锁定时间 24小时
     */
    public static long ACCOUNT_LIMIT_TIME = 24 * 60 * 60;

    /**
     * 验证码有效时间
     */
    public static long CODE_EXPIRE_TIME = 10 * 60;

}
