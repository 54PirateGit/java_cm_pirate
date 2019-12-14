package com.chengm.pirate.service.user;

import com.chengm.pirate.dao.UserLocalDao;
import com.chengm.pirate.pojo.UserLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * program: CmPirate
 * description: 用户定位信息
 * author: ChengMo
 * create: 2019-12-14 14:48
 **/
@Service
public class UserLocalService {

    private UserLocalDao mDao;

    @Autowired
    public UserLocalService(UserLocalDao dao) {
        this.mDao = dao;
    }

    /**
     * 获取用户定位信息
     *
     * @param uid 用户id
     * @return 所有定位信息
     */
    public UserLocal getUserLocal(long uid) {
        return mDao.getUserLocal(uid);
    }

    /**
     * 新增用户定位信息
     */
    public void insert(UserLocal local) {
        mDao.insert(local);
    }

    /**
     * 删除用户定位信息
     */
    public void delete(long uid) {
        mDao.delete(uid);
    }

    /**
     * 更细用户所有定位信息
     */
    public void update(UserLocal local) {
        mDao.update(local);
    }

    /**
     * 按需更新用户定位信息
     */
    public void updateLocal(Map<String, Object> fields, long uid) {
        Map<String, Object> param = new HashMap<>();
        param.put("uid", uid);
        param.put("fields", fields);
        mDao.updateLocal(param);
    }

}
