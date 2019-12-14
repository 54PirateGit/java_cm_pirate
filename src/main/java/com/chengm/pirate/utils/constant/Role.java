package com.chengm.pirate.utils.constant;

/**
 * program: CmPirate
 * description: 用户角色
 * author: ChengMo
 * create: 2019-12-14 22:11
 **/
public @interface Role {

    /**
     * 管理员
     */
    int ROLE_ADMIN = 1;

    /**
     * 正常用户
     */
    int ROLE_NORMAL = 2;

    /**
     * 禁止、冻结用户
     */
    int ROLE_FROZEN = 3;

    /**
     * 虚拟用户
     */
    int ROLE_FICTITIOUS = 4;

    /**
     * 运营
     */
    int ROLE_OPERATE = 5;

}
