package com.chengm.pirate.dao;

import com.chengm.pirate.pojo.UserBase;
import com.chengm.pirate.pojo.UserExtra;

import java.util.Map;

/**
 * program: CmPirate
 * description: 用户扩展信息
 * author: ChengMo
 * create: 2019-12-08 15:19
 **/
public interface UserBaseDao {

    /**
     * 根据 id 获取用户
     */
    UserBase getUserBase(long uid);

    /**
     * 新增用户
     */
    void insert(UserBase userBase);

    /**
     * 删除用户
     */
    void delete(long uid);

    /**
     * 更新用户信息
     */
    void update(Map<String, Object> param);

}
