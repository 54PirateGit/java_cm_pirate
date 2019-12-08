package com.chengm.pirate.dao;

import com.chengm.pirate.pojo.UserExtra;

import java.util.Map;

/**
 * program: CmPirate
 * description: 用户扩展信息
 * author: ChengMo
 * create: 2019-12-08 15:19
 **/
public interface UserExtraDao {

    /**
     * 根据 id 获取用户
     */
    UserExtra getUserExtra(long uid);

    /**
     * 新增用户
     */
    void insert(UserExtra userAuth);

    /**
     * 删除用户
     */
    void delete(long uid);

    /**
     * 更新用户信息
     */
    void update(UserExtra userAuth);

    /**
     * 更新用户信息
     */
    void updateDeviceId(Map<String, Object> param);

}
