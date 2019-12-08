package com.chengm.pirate.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * program: CmPirate
 * description: 用户扩展信息
 * author: ChengMo
 * create: 2019-12-08 15:20
 **/
@Setter
@Getter
@ToString
public class UserExtra {

    /**
     * 用户 ID
     */
    private long uid;

    /**
     * 手机厂商：apple|htc|samsung，很少用
     */
    private String vendor;

    /**
     * 客户端名称，如hjskang
     */
    private String clientName;

    /**
     * 客户端版本号，如7.0.1
     */
    private String clientVersion;

    /**
     * 设备号:android|ios
     */
    private String osName;

    /**
     * 系统版本号:2.2|2.3|4.0|5.1
     */
    private String osVersion;

    /**
     * 设备型号，如:iphone6s、u880、u8800
     */
    private String deviceName;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 苹果设备的IDFA
     */
    private String idfa;

    /**
     * 苹果设备的IDFV
     */
    private String idfv;

    /**
     * 扩展字段1
     */
    private String extend1;

    /**
     * 扩展字段2
     */
    private String extend2;

    /**
     * 扩展字段3
     */
    private String extend3;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

}
