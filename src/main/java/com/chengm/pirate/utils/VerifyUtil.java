package com.chengm.pirate.utils;

import com.chengm.pirate.exception.InvokeException;
import com.chengm.pirate.utils.constant.CodeConstants;
import com.chengm.pirate.utils.constant.CodeStatus;
import com.chengm.pirate.utils.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * program: CmPirate
 * description: 手机号，邮箱 等验证其正确性
 * author: ChengMo
 * create: 2019-12-01 11:03
 **/
public class VerifyUtil {

    private static final String SYMBOLS = "0123456789";
    private static final Random RANDOM = new SecureRandom();

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

    /**
     * 验证是不是我们支持的客户端
     */
    public static boolean isClient(String osName) {
        if (StringUtil.isEmpty(osName)) {
            return false;
        }
        return osName.equals(Constants.CLIENT_ANDROID) || osName.equals(Constants.CLIENT_IOS);
    }

    /**
     * 生成6位随机验证码
     */
    public static String generateVerCode() {
        char[] nonceChars = new char[6];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

}
