package com.chengm.pirate.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * program: CmPirate
 * description: 用户设备
 * author: ChengMo
 * create: 2019-12-14 12:36
 **/
public class DeviceUtils {

    private DeviceUtils() {

    }

    /**
     * 用户新增登录设备
     */
    public static String addUserDeviceId(String devices, String deviceId) {
        String[] dIds = devices.split(",");
        List<String> ids = Arrays.asList(dIds);
        if (!ids.contains(deviceId)) {
            String deviceIds = "";
            if (dIds.length >= 5) {
                List<String> idList = new ArrayList<>();
                for (int i = dIds.length - 1; i >= 0; i--) {
                    idList.add(dIds[i]);
                    if (idList.size() == 4) {
                        break;
                    }
                }
                if (CollectionsUtil.getListSize(idList) > 0) {
                    Collections.reverse(idList);
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < idList.size(); i++) {
                        builder.append(idList.get(i));
                        builder.append(",");
                    }
                    deviceIds = builder.toString();
                }
            } else {
                deviceIds = devices + ",";
            }
            devices = deviceIds + deviceId;
        }

        return devices;
    }

}
