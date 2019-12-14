package com.chengm.pirate.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * program: CmPirate
 * description: 用户定位信息
 * author: ChengMo
 * create: 2019-12-14 14:48
 **/
@Setter
@Getter
@ToString
public class UserLocal {

    /**
     * 用户id
     */
    private long uid;

    /**
     * 所在地国
     */
    private String currNation;

    /**
     * 所在省份
     */
    private String currProvince;

    /**
     * 所在地市
     */
    private String currCity;

    /**
     * 所在地地区
     */
    private String currDistrict;

    /**
     * 具体地址
     */
    private String location;

    /**
     * 地区编码
     */
    private String areaCode;

    /**
     * 经纬度
     */
    private double longitude;

    private double latitude;

    /**
     * 更新时间
     */
    private String updateTime;

}
