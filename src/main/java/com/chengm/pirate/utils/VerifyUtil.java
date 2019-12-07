package com.chengm.pirate.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * program: CmPirate
 * description: 手机号，邮箱 等验证其正确性
 * author: ChengMo
 * create: 2019-12-01 11:03
 **/
public class VerifyUtil {

    private VerifyUtil() {
    }

    /**
     * 验证手机号
     */
    public static boolean isPhone(String phone) {
        if (StringUtil.isEmpty(phone) || phone.length() != 11) {
            return false;
        }
        String regexMobile = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0-1,5-9]))\\d{8}$";
        return phone.matches(regexMobile);
    }

    /**
     * 验证邮箱
     */
    public static boolean isEmail(String email) {
        if (StringUtil.isEmpty(email)) {
            return false;
        }
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
