package com.chengm.pirate.controller.user;

import com.chengm.pirate.base.impl.BaseBizController;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.pojo.UserLocal;
import com.chengm.pirate.service.user.UserLocalService;
import com.chengm.pirate.utils.StringUtil;
import com.chengm.pirate.utils.constant.CodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * program: CmPirate
 * description: 用户定位信息
 * author: ChengMo
 * create: 2019-12-14 15:15
 **/
@RestController
public class UserLocalController extends BaseBizController {

    private UserLocalService mLocalService;

    @Autowired
    public UserLocalController(UserLocalService localService) {
        this.mLocalService = localService;
    }

    /**
     * 获取用户所有定位信息
     */
    @RequestMapping("/user/getLocal")
    public AjaxResult getUserLocal() {
        long uid = requireLongParam("uid");
        UserLocal local = mLocalService.getUserLocal(uid);
        if (local == null) {
            return AjaxResult.fail(CodeConstants.USER_NOT_EXIST, "用户不存在");
        }
        return AjaxResult.success(local);
    }

    /**
     * 删除用户定位信息
     */
    @RequestMapping("/user/deleteLocal")
    public AjaxResult deleteLocal() {
        long uid = requireLongParam("uid");
        UserLocal local = mLocalService.getUserLocal(uid);
        mLocalService.delete(uid);
        return AjaxResult.success();
    }

    /**
     * 按需更新用户定位信息
     */
    @RequestMapping("/user/updateLocal")
    public AjaxResult updateLocal() throws IOException {
        long uid = requireLongParam("uid");
        UserLocal local = mLocalService.getUserLocal(uid);
        if (local == null) {
//            response.sendRedirect("/user/local");
            return userLocal();
        }
        Map<String, Object> fields = new HashMap<>();

        String currNation = getStringParam("currNation");
        if (!StringUtil.isEmpty(currNation)) {
            fields.put("curr_nation", currNation);
        }
        String currProvince = getStringParam("currProvince");
        if (!StringUtil.isEmpty(currProvince)) {
            fields.put("curr_province", currProvince);
        }
        String currCity = getStringParam("currCity");
        if (!StringUtil.isEmpty(currCity)) {
            fields.put("curr_city", currCity);
        }
        String currDistrict = getStringParam("currDistrict");
        if (!StringUtil.isEmpty(currDistrict)) {
            fields.put("curr_district", currDistrict);
        }
        String location = getStringParam("location");
        if (!StringUtil.isEmpty(location)) {
            fields.put("location", location);
        }
        String areaCode = getStringParam("areaCode");
        if (!StringUtil.isEmpty(areaCode)) {
            fields.put("area_code", areaCode);
        }
        String longitude = getStringParam("longitude");
        if (!StringUtil.isEmpty(longitude)) {
            fields.put("longitude", longitude);
        }
        String latitude = getStringParam("latitude");
        if (!StringUtil.isEmpty(latitude)) {
            fields.put("latitude", latitude);
        }

        if (fields.size() <= 0) {
            return AjaxResult.success();
        }

        mLocalService.updateLocal(fields, uid);
        return AjaxResult.success();
    }

    /**
     * 保存用户定位信息，如果用户在设备上拒绝了定位权限，则前端拿不到用户定位信息，则不要调用此接口
     */
    @RequestMapping("/user/local")
    public AjaxResult userLocal() {
        // 获取用户定位信息
        long uid = requireLongParam("uid");
        String currNation = getStringParam("currNation", "");
        String currProvince = getStringParam("currProvince", "");
        String currCity = getStringParam("currCity", "");
        String currDistrict = getStringParam("currDistrict", "");
        String location = getStringParam("location", "");
        String areaCode = getStringParam("areaCode", "");
        double longitude = getDoubleParam("longitude");
        double latitude = getDoubleParam("latitude");

        UserLocal local = mLocalService.getUserLocal(uid);
        if (local == null) {
            local = new UserLocal();
            local.setUid(uid);
            setLocal(local, currNation, currProvince, currCity, currDistrict, location, areaCode, longitude, latitude);
            mLocalService.insert(local);
        } else {
            setLocal(local, currNation, currProvince, currCity, currDistrict, location, areaCode, longitude, latitude);
            mLocalService.update(local);
        }

        return AjaxResult.success();
    }

    private void setLocal(UserLocal local, String currNation, String currProvince, String currCity, String currDistrict,
                          String location, String areaCode, double longitude, double latitude) {
        local.setCurrNation(currNation);
        local.setCurrProvince(currProvince);
        local.setCurrCity(currCity);
        local.setCurrDistrict(currDistrict);
        local.setLocation(location);
        local.setAreaCode(areaCode);
        local.setLongitude(longitude);
        local.setLatitude(latitude);
    }

}
