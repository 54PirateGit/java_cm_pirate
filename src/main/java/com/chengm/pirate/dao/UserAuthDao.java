package com.chengm.pirate.dao;

import com.chengm.pirate.pojo.UserAuth;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * program: CmPirate
 * description: 操作用户数据
 * author: ChengMo
 * create: 2019-11-30 17:40
 **/
@Mapper
public interface UserAuthDao {

    /**
     * 根据 id 获取用户
     */
    UserAuth getUser(long uid);

    /**
     * 根据 identifier 获取用户
     */
    UserAuth getUserByIdentifier(String identifier);

    /**
     * 新增用户
     */
    void insert(UserAuth userAuth);

    /**
     * 删除用户
     */
    void delete(long uid);

    /**
     * 更新用户信息
     */
    void update(UserAuth userAuth);

    /**
     * 更新部分字段
     */
    void updateUserToken(Map<String, Object> param);

    /**
     * 更新部分字段
     */
    void updateUserPwd(Map<String, Object> param);

    /**
     * 更新部分字段
     */
    void updateUserIdentifier(Map<String, Object> param);

}
