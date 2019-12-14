package com.chengm.pirate.utils.constant;

/**
 * program: CmPirate
 * description: code 常量
 * author: ChengMo
 * create: 2019-11-30 20:32
 **/
public interface CodeConstants {

    /**
     * 成功
     */
    int SUCCESS = 200;

    /**
     * 失败
     */
    int FAIL = 400;

    /**
     * 异常
     */
    int EXCEPTION = 401;

    /**
     * 算术异常
     */
    int ARITHMETIC_EXCEPTION = 402;

    /**
     * 用户不存在
     */
    int USER_NOT_EXIST = 403;

    /**
     * 请求方式不对
     */
    int ERROR_REQUEST_METHOD = 406;

    /**
     * Token 错误
     */
    int TOKEN_ERROR = 407;

    /**
     * 参数缺失
     */
    int ERROR_CODE_NOT_REQUIRED_PARAM = 408;

    /**
     * 参数错误
     */
    int ERROR_CODE_PARAM_ERROR = 409;

    /**
     * 无效的参数
     */
    int ERROR_CODE_INVALID_PARAMETER = 410;

    /**
     * 在新的设备登陆
     */
    int USER_NEW_DEVICE = 411;

    /**
     * Token 错误, 用户登录之后，在此之前的token不可再用了，用于用户登录被顶的情况
     */
    int TOKEN_RE_LOGIN = 412;

}
