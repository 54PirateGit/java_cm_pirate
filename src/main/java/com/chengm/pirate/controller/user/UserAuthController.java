package com.chengm.pirate.controller.user;

import com.chengm.pirate.base.impl.BaseBizController;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.pojo.UserAuth;
import com.chengm.pirate.pojo.UserExtra;
import com.chengm.pirate.service.mail.EmailService;
import com.chengm.pirate.service.user.UserAuthService;
import com.chengm.pirate.service.user.UserBaseService;
import com.chengm.pirate.service.user.UserExtraService;
import com.chengm.pirate.utils.DeviceUtils;
import com.chengm.pirate.utils.StringUtil;
import com.chengm.pirate.utils.constant.CodeConstants;
import com.chengm.pirate.utils.constant.CodeStatus;
import com.chengm.pirate.utils.constant.Constants;
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
    private EmailService mEmailService;

    @Autowired
    public UserAuthController(UserAuthService service1, UserExtraService service2, UserBaseService service3, EmailService service4) {
        this.mUserAuthService = service1;
        this.mUserExtraService = service2;
        this.mUserBaseService = service3;
        this.mEmailService = service4;
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

    /**
     * 修改用户登录密码
     */
    @RequestMapping("/user/modifyPwd")
    public AjaxResult modifyPwd() {
        String identifier = requireStringParam("identifier");
        UserAuth userAuth = mUserAuthService.getUser(identifier);
        if (userAuth == null) {
            return AjaxResult.failUserNotExist();
        }

        int modifyWay = requireIntParam("modifyWay");
        String newPwd = requireStringParam("newPwd");
        if (modifyWay == Constants.MODIFY_PWD_WAY_OLD) {
            String oldPwd = requireStringParam("oldPwd");
            return oldWayModify(userAuth, newPwd, oldPwd);
        } else if (modifyWay == Constants.MODIFY_PWD_WAY_CODE) {
            String code = requireStringParam("code");
            return codeWayModify(userAuth, newPwd, code);
        } else {
            return AjaxResult.failInvalidParameter("modifyWay");
        }
    }

    // 执行旧密码修改逻辑
    private AjaxResult oldWayModify(UserAuth userAuth, String newPwd, String oldPwd) {
        // 比较旧密码
        if (!userAuth.getPassword().equals(oldPwd)) {
            return AjaxResult.fail("密码错误");
        }
        // 如果旧密码一样，但是登录设备不一样，需要验证设备，账号风险控制
        String deviceId = getDeviceId();
        UserExtra userExtra = mUserExtraService.getUserExtra(userAuth.getUid());
        if (userExtra == null) {
            return AjaxResult.fail(CodeConstants.ACCOUNT_EXCEPTION, "账号异常，请联系管理员");
        }
        if (DeviceUtils.isNeedDeviceVerity(deviceId, userExtra.getDeviceId())) {
            return AjaxResult.fail(CodeConstants.USER_NEW_DEVICE, "在新的设备操作，需要先验证");
        }

        // 更新用户密码
        mUserAuthService.updateUserPwd(userAuth.getUid(), newPwd);

        return AjaxResult.success();
    }

    // 执行验证码修改密码逻辑
    private AjaxResult codeWayModify(UserAuth userAuth, String newPwd, String code) {
        // 验证码比较
        if (StringUtil.getLength(code) != Constants.VERIFICATION_CODE_LENGTH) {
            return AjaxResult.fail(CodeConstants.PARAM_ERROR, "验证码不正确");
        } else {
            int codeStatus = mEmailService.checkRegisterEmailCode(userAuth.getIdentifier(), code);
            if (codeStatus == CodeStatus.CODE_EXPIRE) {
                return AjaxResult.fail(CodeConstants.PARAM_ERROR, "验证码已过期，请重新获取");
            } else if (codeStatus == CodeStatus.CODE_FAIL) {
                return AjaxResult.fail(CodeConstants.PARAM_ERROR, "验证码错误");
            } else {
                // 验证码正确
                UserExtra userExtra = mUserExtraService.getUserExtra(userAuth.getUid());
                if (userExtra == null) {
                    return AjaxResult.fail(CodeConstants.USER_NOT_EXIST, "账号异常，请联系管理员");
                }
                String loginDeviceId = userExtra.getDeviceId();
                String deviceId = getDeviceId();
                if (!StringUtil.isEmpty(loginDeviceId)) {
                    deviceId = DeviceUtils.addUserDeviceId(userExtra.getDeviceId(), deviceId);;
                }
                mUserExtraService.updateDeviceId(userAuth.getUid(), deviceId);
                mUserAuthService.updateUserPwd(userAuth.getUid(), newPwd);
            }
        }
        return AjaxResult.success();
    }

}
