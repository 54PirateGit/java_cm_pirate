package com.chengm.pirate.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

/**
 * program: CmPirate
 * description: 用户
 * author: ChengMo
 * create: 2019-11-30 17:41
 **/
@Setter
@Getter
@ToString
public class UserAuth {

    /**
     * 用户 id，唯一性
     */
    private long uid;

    /**
     * 用户登录身份， 1手机号 2微信 3QQ 4邮箱
     */
    private int identityType;

    /**
     * 手机号 邮箱 微信 QQ等登录唯一标识
     */
    private String identifier;

    /**
     * 密码凭证， MD5加密
     */
    private String password;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * token
     */
    private String token;

}
