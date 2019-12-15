package com.chengm.pirate.utils.constant;

import org.springframework.stereotype.Component;

/**
 * program: CmPirate
 * description: 常量
 * author: ChengMo
 * create: 2019-12-01 16:39
 **/
@Component
public class Constants {

    /**
     * SQL
     */
    public final static String SQL_JDBC = "jdbc:mysql://localhost:3306/cm_pirate_dev?user=root&password=170904&serverTimezone=UTC";
//    public final static String SQL_JDBC = "jdbc:mysql://localhost:3306/cm_pirate?user=root&password=170904&serverTimezone=UTC";

    /**
     * 验证码长度
     */
    public final static int VERIFICATION_CODE_LENGTH = 6;

    /**
     * 客户端 ANDROID
     */
    public final static String CLIENT_ANDROID = "ANDROID";

    /**
     * 客户端 IOS
     */
    public final static String CLIENT_IOS = "IOS";

    /**
     * 无deviceId时默认值
     */
    public final static String DEFAULT_DEVICE_ID = "0";

    /**
     * 旧密码修改
     */
    public final static int MODIFY_PWD_WAY_OLD = 1;

    /**
     * 验证码修改密码
     */
    public final static int MODIFY_PWD_WAY_CODE = 2;

}
