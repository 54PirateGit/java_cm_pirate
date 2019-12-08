package com.chengm.pirate.utils;

import java.util.List;
import java.util.Map;

/**
 * program: CmPirate
 * description:
 * author: ChengMo
 * create: 2019-11-30 18:56
 **/
public class CollectionsUtil {

    /**
     * 获取Map数量并判空处理
     */
    public static int getMapSize(Map map) {
        if (CollectionsUtil.isEmpty(map)) {
            return 0;
        }
        return map.size();
    }

    /**
     * 判断map是否为空
     */
    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断List是否为空
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 获取List数量并判空处理
     */
    public static int getListSize(List list) {
        if (CollectionsUtil.isEmpty(list)) {
            return 0;
        }
        return list.size();
    }

}
