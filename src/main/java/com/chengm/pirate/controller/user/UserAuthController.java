package com.chengm.pirate.controller.user;

import com.chengm.pirate.base.impl.BaseBizController;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.service.UserAuthService;
import com.chengm.pirate.service.UserBaseService;
import com.chengm.pirate.service.UserExtraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * program: CmPirate
 * description: 操作用户信息
 * author: ChengMo
 * create: 2019-11-30 18:41
 **/
@Controller
@ResponseBody
public class UserAuthController extends BaseBizController {

    private UserAuthService mUserAuthService;
    private UserExtraService mUserExtraService;
    private UserBaseService mUserBaseService;

    @Autowired
    public UserAuthController(UserAuthService service1, UserExtraService service2, UserBaseService service3) {
        this.mUserAuthService = service1;
        this.mUserExtraService = service2;
        this.mUserBaseService = service3;
    }

    /**
     * 获取用户信息
     */
    @RequestMapping("/user/getUser")
    public AjaxResult getUser() {
        long uid = requireLongParam("uid");
        return AjaxResult.success(mUserAuthService.getUser(uid));
    }

    /**
     * 获取用户扩展信息
     */
    @RequestMapping("/user/getUserExtraInfo")
    public AjaxResult getUserExtraInfo() {
        long uid = requireLongParam("uid");
        return AjaxResult.success(mUserExtraService.getUserExtra(uid));
    }

    /**
     * 获取用户基础信息
     */
    @RequestMapping("/user/getUserBaseInfo")
    public AjaxResult getUserBaseInfo() {
        long uid = requireLongParam("uid");
        return AjaxResult.success(mUserBaseService.getUserBase(uid));
    }

}
