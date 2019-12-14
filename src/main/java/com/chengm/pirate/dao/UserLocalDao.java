package com.chengm.pirate.dao;

import com.chengm.pirate.pojo.UserLocal;

import java.util.Map;

/**
 * program: CmPirate
 * description: 用户定位信息
 * author: ChengMo
 * create: 2019-12-14 14:47
 **/
public interface UserLocalDao {

    /**
     * 获取用户定位信息
     * @param uid 用户id
     * @return 所有定位信息
     */
    UserLocal getUserLocal(long uid);

    /**
     * 新增用户定位信息
     */
    void insert(UserLocal local);

    /**
     * 删除用户定位信息
     */
    void delete(long uid);

    /**
     * 更细用户所有定位信息
     */
    void update(UserLocal local);

    /**
     * 按需更新用户定位信息
     */
    void updateLocal(Map<String, Object> param);
}
