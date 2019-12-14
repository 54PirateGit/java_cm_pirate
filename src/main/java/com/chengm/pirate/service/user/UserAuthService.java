package com.chengm.pirate.service.user;

import com.chengm.pirate.dao.UserAuthDao;
import com.chengm.pirate.pojo.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * program: CmPirate
 * description:
 * author: ChengMo
 * create: 2019-11-30 18:13
 **/
@Service
public class UserAuthService {

    private UserAuthDao mDao;

    @Autowired
    public UserAuthService(UserAuthDao dao) {
        this.mDao = dao;
    }

    /**
     * 根据 uid 获取用户
     */
    public UserAuth getUser(long uid) {
        return mDao.getUser(uid);
    }

    /**
     * 根据 identifier 获取用户
     */
    public UserAuth getUser(String identifier) {
        return mDao.getUserByIdentifier(identifier);
    }

    /**
     * 新增用户
     */
    public void insert(UserAuth userAuth) {
        mDao.insert(userAuth);
    }

    /**
     * 删除用户
     */
    public void delete(long uid) {
        mDao.delete(uid);
    }

    /**
     * 更新用户信息
     */
    public void update(UserAuth userAuth) {
        mDao.update(userAuth);
    }

    /**
     * 更新用户信息
     */
    public void updateUserToken(UserAuth userAuth) {
        Map<String, Object> param = new HashMap<>();
        param.put("token", userAuth.getToken());
        param.put("uid", userAuth.getUid());
        mDao.updateUserToken(param);
    }

    /**
     * 更新用户信息
     */
    public void updateUserPwd(UserAuth userAuth) {
        Map<String, Object> param = new HashMap<>();
        param.put("pwd", userAuth.getPassword());
        param.put("uid", userAuth.getUid());
        mDao.updateUserPwd(param);
    }

    /**
     * 更新用户信息
     */
    public void updateUserIdentifier(UserAuth userAuth) {
        Map<String, Object> param = new HashMap<>();
        param.put("identifier", userAuth.getIdentifier());
        param.put("uid", userAuth.getUid());
        mDao.updateUserIdentifier(param);
    }

}
