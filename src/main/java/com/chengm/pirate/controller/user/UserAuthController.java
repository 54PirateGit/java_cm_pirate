package com.chengm.pirate.controller.user;

import com.chengm.pirate.base.impl.BaseBizController;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.service.UserAuthService;
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

    private UserAuthService mService;

    @Autowired
    public UserAuthController(UserAuthService service) {
        this.mService = service;
    }

    /**
     * 获取用户信息
     */
    @RequestMapping("/user/getUser")
    public AjaxResult getUser() {
        String uid = requireStringParam("uid");
        return AjaxResult.success(mService.getUser(uid));
    }

}
