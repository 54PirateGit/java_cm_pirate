package com.chengm.pirate.service.user;

import com.chengm.pirate.dao.UserExtraDao;
import com.chengm.pirate.pojo.UserExtra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * program: CmPirate
 * description: 用户扩展信息
 * author: ChengMo
 * create: 2019-12-8
 **/
@Service
public class UserExtraService {

    private UserExtraDao mDao;

    @Autowired
    public UserExtraService(UserExtraDao dao) {
        this.mDao = dao;
    }

    /**
     * 根据 uid 获取用户
     */
    public UserExtra getUserExtra(long uid) {
        return mDao.getUserExtra(uid);
    }

    /**
     * 新增用户
     */
    public void insert(UserExtra userExtra) {
        mDao.insert(userExtra);
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
    public void update(UserExtra userExtra) {
        mDao.update(userExtra);
    }

    /**
     * 更新用户登陆设备
     */
    public void updateDeviceId(long uid, String deviceId) {
        Map<String, Object> param = new HashMap<>();
        param.put("uid", uid);
        param.put("deviceId", deviceId);
        mDao.updateDeviceId(param);
    }
}
