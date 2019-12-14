package com.chengm.pirate.controller.user;

import com.chengm.pirate.base.impl.BaseBizController;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.pojo.UserBase;
import com.chengm.pirate.pojo.UserExtra;
import com.chengm.pirate.service.UserBaseService;
import com.chengm.pirate.service.UserExtraService;
import com.chengm.pirate.utils.*;
import com.chengm.pirate.utils.log.LogLevel;
import com.chengm.pirate.utils.log.LogUtil;
import com.chengm.pirate.pojo.UserAuth;
import com.chengm.pirate.service.UserAuthService;
import com.chengm.pirate.utils.constant.CodeConstants;
import com.chengm.pirate.utils.constant.Constants;
import com.chengm.pirate.utils.ids.IdGenerator;
import com.chengm.pirate.utils.constant.IdentityType;
import com.chengm.pirate.utils.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * program: CmPirate
 * description: 登录相关操作
 * author: ChengMo
 * create: 2019-12-01 10:20
 **/
@RestController
public class LoginController extends BaseBizController {

    private UserAuthService mUserAuthService;
    private UserExtraService mUserExtraService;
    private UserBaseService mUserBaseService;

    @Autowired
    public LoginController(UserAuthService service1, UserExtraService service2, UserBaseService service3) {
        this.mUserAuthService = service1;
        this.mUserExtraService = service2;
        this.mUserBaseService = service3;
    }

    /**
     * 用户使用密码登录
     */
    @RequestMapping("/user/accountLogin")
    public AjaxResult accountLogin() {

        // 校验请求方式
        requestMethodPost();

        /*
         * 需要的参数
         * identity_type  identifier password
         */
        int identityType = requireIntParam("identityType");
        String identifier = requireStringParam("identifier");
        String password = requireStringParam("password");

        // 客户端验证， 目前支持 ANDROID IOS
        if (!VerifyUtil.isClient(getOsName())) {
            return AjaxResult.failInvalidParameter("osName");
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
        UserAuth userAuth = mUserAuthService.getUser(identifier);
        if (userAuth != null) {
            if (identityType == IdentityType.IDENTITY_TYPE_PHONE || identityType == IdentityType.IDENTITY_TYPE_EMAIL) {
                // 验证密码的正确性, 前端需要对密码d5加密传输
                if (!userAuth.getPassword().equals(password)) {
                    return AjaxResult.fail("密码错误");
                }

                // 用户存在则执行登录操作
                userAuth = userLogin(userAuth);
            }
        } else {
            return AjaxResult.fail(CodeConstants.USER_NOT_EXIST, "用户不存在");
        }

        long uid = userAuth.getUid();

        // 验证 deviceId, 微信登陆和QQ登陆不验证
        if (identityType == IdentityType.IDENTITY_TYPE_PHONE || identityType == IdentityType.IDENTITY_TYPE_EMAIL) {
            UserExtra userExtra = mUserExtraService.getUserExtra(uid);
            if (userExtra == null) {
                return AjaxResult.fail(CodeConstants.USER_NOT_EXIST, "账号异常，请联系管理员");
            }

            String loginDeviceId = userExtra.getDeviceId();
            if (!StringUtil.isEmpty(loginDeviceId)) {
                String[] dIds = loginDeviceId.split(",");
                List<String> ids = Arrays.asList(dIds);
                if (!ids.contains(getDeviceId())) {
                    return AjaxResult.fail(CodeConstants.USER_NEW_DEVICE, "在新的设备登陆，需要先验证");
                }
            }
        }

        // 更新用户扩展信息
        updateUserExtra(uid);

        // 将数据返回
        return AjaxResult.success(getResultMap(userAuth));
    }

    /**
     * 用户使用验证码登录
     */
    @RequestMapping("/user/codeLogin")
    public AjaxResult codeLogin() {
        /*
         * 需要的参数
         * identity_type  identifier code
         */
        int identityType = requireIntParam("identityType");
        String identifier = requireStringParam("identifier");
        String code = requireStringParam("code");

        // 客户端验证， 目前支持 ANDROID IOS
        if (!VerifyUtil.isClient(getOsName())) {
            return AjaxResult.failInvalidParameter("osName");
        }

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
        UserAuth userAuth = mUserAuthService.getUser(identifier);
        if (userAuth != null) {
            // 用户存在则执行登录操作
            userAuth = userLogin(userAuth);
        } else {
            // 用户不存在则创建新用户执行注册操作
            UserAuth ua = new UserAuth();
            ua.setIdentityType(identityType);
            ua.setIdentifier(identifier);

            userAuth = userRegister(ua);

            // 更新用户基础信息
            initUserBase(userAuth.getUid(), identityType, identifier, userAuth.getToken());
        }

        long uid = userAuth.getUid();

        // 更新用户扩展信息
        updateUserExtra(uid);

        // 将数据返回
        return AjaxResult.success(getResultMap(userAuth));
    }

    // 执行用户登录成功操作，更新用户登录信息
    private UserAuth userLogin(UserAuth userAuth) {
        if (userAuth == null) {
            return null;
        }
        // 需要重新生成token
        userAuth.setToken(TokenUtil.sign(userAuth.getUid()));
        mUserAuthService.updateUserToken(userAuth);

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
        mUserAuthService.insert(userAuth);

        return userAuth;
    }

    // 登陆成功时更新用户扩展信息
    private void updateUserExtra(long uid) {
        UserExtra userExtra = mUserExtraService.getUserExtra(uid);
        if (userExtra == null) {
            userExtra = new UserExtra();
            setUserExtra(userExtra, uid, getDeviceId(), getOsName(),
                    getOsVersion(), getClientVersion(), getVendor(), getDeviceName(), getIdfa(), getIdfv());
            mUserExtraService.insert(userExtra);
        } else {
            setUserExtra(userExtra, uid, getDeviceId(), getOsName(),
                    getOsVersion(), getClientVersion(), getVendor(), getDeviceName(), getIdfa(), getIdfv());
            mUserExtraService.update(userExtra);
        }
    }

    private void setUserExtra(UserExtra userExtra, long uid, String deviceId, String osName, String osVersion,
                               String clientVersion, String vendor, String deviceName, String idfa, String idfv) {
        if (uid != 0) {
            userExtra.setUid(uid);
        }

        // 最多保存 5 个deviceId
        if (!StringUtil.isEmpty(userExtra.getDeviceId())) {
            deviceId = DeviceUtils.addUserDeviceId(userExtra.getDeviceId(), deviceId);;
        }
        userExtra.setDeviceId(deviceId);
        userExtra.setOsName(osName);
        userExtra.setOsVersion(osVersion);
        userExtra.setClientVersion(clientVersion);
        userExtra.setVendor(vendor);
        userExtra.setDeviceName(deviceName);
        if (osName.equals(Constants.CLIENT_IOS)) {
            userExtra.setIdfa(idfa);
            userExtra.setIdfv(idfv);
        } else {
            userExtra.setIdfa("");
            userExtra.setIdfv("");
        }
        userExtra.setClientName("");
        userExtra.setExtend1("");
        userExtra.setExtend2("");
        userExtra.setExtend3("");
    }

    // 设置用户基础信息
    private void initUserBase(long uid, int identityType, String identifier, String pushToken) {
        UserBase userBase = mUserBaseService.getUserBase(uid);
        if (userBase == null) {
            userBase = new UserBase();
            userBase.setUid(uid);
            userBase.setRegisterSource(identityType);
            if (identityType == IdentityType.IDENTITY_TYPE_PHONE) {
                userBase.setMobile(identifier);
                userBase.setMobileBindTime(System.currentTimeMillis());
            } else if (identityType == IdentityType.IDENTITY_TYPE_EMAIL) {
                userBase.setEmail(identifier);
                userBase.setEmailBindTime(System.currentTimeMillis());
            }
            // 新注册用户都是正常用户
            userBase.setUserRole(2);
            userBase.setPushToken(pushToken);
            userBase.setUserName(identifier);
            mUserBaseService.insert(userBase);
        }
    }

    private Map<String, Object> getResultMap(UserAuth userAuth) {
        // token放头部返回
        response.addHeader("token", userAuth.getToken());

        // 查询用户基础信息返回
        UserBase userBase = mUserBaseService.getUserBase(userAuth.getUid());
        Map<String, Object> result = new HashMap<>();
        if (userBase == null) {
            result.put("uid", userAuth.getUid());
            result.put("userName", userAuth.getIdentifier());
        } else {
            result.put("uid", userBase.getUid());
            result.put("userName", userBase.getUserName());
            if (!StringUtil.isEmpty(userBase.getFace())) {
                result.put("face", userBase.getFace());
            }
            if (!StringUtil.isEmpty(userBase.getMobile())) {
                result.put("mobile", userBase.getMobile());
            }
            if (!StringUtil.isEmpty(userBase.getEmail())) {
                result.put("email", userBase.getEmail());
            }
            if (!StringUtil.isEmpty(userBase.getCity())) {
                result.put("city", userBase.getCity());
            }
            if (!StringUtil.isEmpty(userBase.getPushToken())) {
                result.put("pushToken", userBase.getPushToken());
            }
            result.put("gender", userBase.getGender());
            if (userBase.getBirthday() > 0) {
                result.put("birthday", DateUtils.formatDate(userBase.getBirthday()));
            }
        }
        return result;
    }

}
