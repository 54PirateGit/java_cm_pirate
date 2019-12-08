package com.chengm.pirate.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * program: CmPirate
 * description: 用户基础信息
 * author: ChengMo
 * create: 2019-12-08 16:51
 **/
@Setter
@Getter
@ToString
public class UserBase {

    /**
     * 用户ID
     */
    private long uid;

    /**
     * 1超级用户，管理员 2正常用户 3禁言用户 4虚拟用户 5运营
     */
    private int userRole;

    /**
     * 注册来源 1手机号 2微信 3QQ 4邮箱
     */
    private int registerSource;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户性别 0-female 1-male
     */
    private int gender;

    /**
     * 用户生日
     */
    private long birthday;

    /**
     * 用户个人签名
     */
    private String signature;

    /**
     * 手机号码(唯一)
     */
    private String mobile;

    /**
     * 手机号码绑定时间
     */
    private long mobileBindTime;

    /**
     * 邮箱(唯一)
     */
    private String email;

    /**
     * 邮箱绑定时间
     */
    private long emailBindTime;

    /**
     * 头像
     */
    private String face;

    /**
     * 头像 200x200x80
     */
    private String face200;

    /**
     * 原图头像
     */
    private String srcface;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 用户设备push_token
     */
    private String pushToken;

    /**
     * 城市
     */
    private String city;

}
