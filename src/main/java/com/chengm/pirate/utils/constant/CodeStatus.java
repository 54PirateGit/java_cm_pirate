package com.chengm.pirate.utils.constant;

/**
 * program: CmPirate
 * description: 验证码状态
 * author: ChengMo
 * create: 2019-12-14 18:58
 **/
public @interface CodeStatus {

    /**
     * 验证码已过期
     */
    int CODE_EXPIRE = 0XAAA;

    /**
     * 验证码错误
     */
    int CODE_FAIL = 0XBBB;

    /**
     * 验证码正确
     */
    int CODE_SUCCESS = 0xCCC;

}
