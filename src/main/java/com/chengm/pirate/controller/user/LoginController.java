package com.chengm.pirate.controller.user;

import com.chengm.pirate.base.impl.BaseBizController;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.utils.log.LogLevel;
import com.chengm.pirate.utils.log.LogUtil;
import com.chengm.pirate.pojo.UserAuth;
import com.chengm.pirate.service.UserAuthService;
import com.chengm.pirate.utils.MD5Util;
import com.chengm.pirate.utils.StringUtil;
import com.chengm.pirate.utils.VerifyUtil;
import com.chengm.pirate.utils.constant.CodeConstants;
import com.chengm.pirate.utils.constant.Constants;
import com.chengm.pirate.utils.ids.IdGenerator;
import com.chengm.pirate.utils.constant.IdentityType;
import com.chengm.pirate.utils.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * program: CmPirate
 * description: 登录相关操作
 * author: ChengMo
 * create: 2019-12-01 10:20
 **/
@Controller
@ResponseBody
public class LoginController extends BaseBizController {

    private UserAuthService mService;

    @Autowired
    public LoginController(UserAuthService service) {
        this.mService = service;
    }

    /**
     * 用户使用密码登录
     */
    @RequestMapping("/user/accountLogin")
    public AjaxResult accountLogin() {

        /*
         * 需要的参数
         * identity_type  identifier password
         */
        int identityType = requireIntParam("identityType");
        String identifier = requireStringParam("identifier");
        String password = requireStringParam("password");

        /*
         * 需要根据 identityType 来验证此参数
         * 1 手机号  2 微信  3 QQ  4 邮箱
         */
        if (identityType == IdentityType.IDENTITY_TYPE_PHONE) {
            // 验证手机号码的正确性
            if (!VerifyUtil.isPhone(identifier)) {
                return AjaxResult.fail("手机号码格式不正确");
            }

        } else if (identityType == IdentityType.IDENTITY_TYPE_WECHAT) {
            // 不需要验证
        } else if (identityType == IdentityType.IDENTITY_TYPE_QQ) {
            // 不需要验证
        } else if (identityType == IdentityType.IDENTITY_TYPE_EMAIL) {
            // 验证邮箱
            if (!VerifyUtil.isEmail(identifier)) {
                return AjaxResult.fail("邮箱格式不正确");
            }

        } else {
            return AjaxResult.fail("missing parameters:identityType");
        }

        // 到数据库中查询是否已经存在此用户
        UserAuth userAuth = mService.getUser(identifier);
        if (userAuth != null) {
            // 验证密码的正确性, 前端需要对密码d5加密传输
            if (!userAuth.getPassword().equals(password)) {
                return AjaxResult.fail("密码错误");
            }

            // 用户存在则执行登录操作
            userAuth = userLogin(userAuth);
        } else {
            return AjaxResult.fail(CodeConstants.USER_NOT_EXIST, "用户不存在");
        }

        return AjaxResult.success(userAuth);
    }

    /**
     * 用户使用验证码登录
     */
    @RequestMapping("/user/codeLogin")
    public AjaxResult login() {
        /*
         * 需要的参数
         * identity_type  identifier code
         */
        int identityType = requireIntParam("identityType");
        String identifier = requireStringParam("identifier");
        String code = requireStringParam("code");

        // 参数无效
        if (identityType < 1 || identityType > 4) {
            return AjaxResult.failInvalidParameter("identityType");
        }

        /*
         * 验证码
         * 目前未接入验证码，此参数不作为验证条件
         */
        if (StringUtil.getLength(code) != Constants.VERIFICATION_CODE_LENGTH) {
            LogUtil.logValue("CODE", "目前未接入相关短信验证码平台", LogLevel.LEVEL_WARN);
        }

        /*
         * 需要根据 identityType 来验证此参数
         * 1 手机号  2 微信  3 QQ  4 邮箱
         */
        if (identityType == IdentityType.IDENTITY_TYPE_PHONE) {
            // 验证手机号码的正确性
            if (!VerifyUtil.isPhone(identifier)) {
                return AjaxResult.fail("手机号码格式不正确");
            }

        } else if (identityType == IdentityType.IDENTITY_TYPE_WECHAT) {
            // 不需要验证
        } else if (identityType == IdentityType.IDENTITY_TYPE_QQ) {
            // 不需要验证
        } else if (identityType == IdentityType.IDENTITY_TYPE_EMAIL) {
            // 验证邮箱
            if (!VerifyUtil.isEmail(identifier)) {
                return AjaxResult.fail("邮箱格式不正确");
            }

        } else {
            return AjaxResult.failInvalidParameter("identityType");
        }

        // 到数据库中查询是否已经存在此用户
        UserAuth userAuth = mService.getUser(identifier);
        if (userAuth != null) {
            // 用户存在则执行登录操作
            userAuth = userLogin(userAuth);
        } else {
            // 用户不存在则创建新用户执行注册操作
            UserAuth ua = new UserAuth();
            ua.setIdentityType(identityType);
            ua.setIdentifier(identifier);

            userAuth = userRegister(ua);
        }

        return AjaxResult.success(userAuth);
    }

    // 执行用户登录成功操作，更新用户登录信息
    private UserAuth userLogin(UserAuth userAuth) {
        if (userAuth == null) {
            return null;
        }
        // 需要重新生成token
        userAuth.setToken(TokenUtil.sign(userAuth.getUid()));
        mService.updateUserToken(userAuth);

        return userAuth;
    }

    // 用户注册, 在数据库中生成一个用户对象并将用户信息返回
    private UserAuth userRegister(UserAuth userAuth) {

        // 生成uid
        userAuth.setUid(IdGenerator.getDefault().nextId());
        // 给用户生成一个默认密码
        userAuth.setPassword(MD5Util.getMD5DefaultPwd());
        // 生成token
        userAuth.setToken(TokenUtil.sign(userAuth.getUid()));

        // 将数据插入数据库中
        mService.insert(userAuth);

        return userAuth;
    }

}
