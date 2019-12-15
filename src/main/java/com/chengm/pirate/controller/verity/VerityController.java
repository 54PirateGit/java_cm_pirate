package com.chengm.pirate.controller.verity;

import com.chengm.pirate.base.impl.BaseBizController;
import com.chengm.pirate.config.AppConfig;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.pojo.UserAuth;
import com.chengm.pirate.pojo.UserExtra;
import com.chengm.pirate.service.user.UserAuthService;
import com.chengm.pirate.service.user.UserBaseService;
import com.chengm.pirate.service.user.UserExtraService;
import com.chengm.pirate.service.mail.EmailService;
import com.chengm.pirate.utils.*;
import com.chengm.pirate.utils.constant.CodeConstants;
import com.chengm.pirate.utils.constant.CodeStatus;
import com.chengm.pirate.utils.constant.Constants;
import com.chengm.pirate.utils.constant.IdentityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * program: CmPirate
 * description: 验证用户
 * author: ChengMo
 * create: 2019-12-08 20:33
 **/
@Controller
@ResponseBody
public class VerityController extends BaseBizController {

    private UserAuthService mUserAuthService;
    private UserExtraService mUserExtraService;
    private UserBaseService mUserBaseService;
    private EmailService mEmailService;

    @Autowired
    public VerityController(UserAuthService service1, UserExtraService service2, UserBaseService service3, EmailService service4) {
        this.mUserAuthService = service1;
        this.mUserExtraService = service2;
        this.mUserBaseService = service3;
        this.mEmailService = service4;
    }

    /**
     * 发送验证码
     */
    @RequestMapping("/verity/sendCode")
    public AjaxResult sendCode() {
        int identityType = requireIntParam("identityType");
        String identifier = requireStringParam("identifier");
        return sendCode(identityType, identifier);
    }

    /**
     * 验证device
     */
    @RequestMapping("/verity/device")
    public AjaxResult verityDevice() {
        int identityType = requireIntParam("identityType");
        String identifier = requireStringParam("identifier");
        String deviceId = getDeviceId();
        String code = requireStringParam("code");

        UserAuth userAuth = mUserAuthService.getUser(identifier);
        if (userAuth == null) {
            return AjaxResult.failUserNotExist();
        }

        if (identityType == IdentityType.IDENTITY_TYPE_PHONE) {
            if (StringUtil.isEmpty(code)) {
                return AjaxResult.failInvalidParameter("code");
            }
            if (!code.equals("123456")) {
                return AjaxResult.fail(CodeConstants.PARAM_ERROR, "验证码不正确");
            }
        } else if (identityType == IdentityType.IDENTITY_TYPE_EMAIL) {
            if (StringUtil.isEmpty(code)) {
                return AjaxResult.failInvalidParameter("code");
            }
            if (StringUtil.getLength(code) != Constants.VERIFICATION_CODE_LENGTH) {
                return AjaxResult.fail(CodeConstants.PARAM_ERROR, "验证码不正确");
            } else {
                int codeStatus = mEmailService.checkRegisterEmailCode(identifier, code);
                if (codeStatus == CodeStatus.CODE_EXPIRE) {
                    return AjaxResult.fail(CodeConstants.PARAM_ERROR, "验证码已过期，请重新获取");
                } else if (codeStatus == CodeStatus.CODE_FAIL) {
                    return AjaxResult.fail(CodeConstants.PARAM_ERROR, "验证码错误");
                } else {
                    // 验证码正确
                }
            }
        } else {
            return AjaxResult.failInvalidParameter("identityType");
        }

        UserExtra userExtra = mUserExtraService.getUserExtra(userAuth.getUid());
        if (userExtra == null) {
            return AjaxResult.fail(CodeConstants.USER_NOT_EXIST, "账号异常，请联系管理员");
        }
        String loginDeviceId = userExtra.getDeviceId();
        if (!StringUtil.isEmpty(loginDeviceId)) {
            deviceId = DeviceUtils.addUserDeviceId(userExtra.getDeviceId(), deviceId);;
        }
        mUserExtraService.updateDeviceId(userAuth.getUid(), deviceId);
        return AjaxResult.success();
    }

    /**
     * 给系统中已存在账户发送验证码
     */
    @RequestMapping("/verity/codeAccount")
    public AjaxResult sendCodeToAccount() {
        String identifier = requireStringParam("identifier");
        UserAuth userAuth = mUserAuthService.getUser(identifier);
        if (userAuth == null) {
            return AjaxResult.failUserNotExist();
        }

        int identityType = userAuth.getIdentityType();
        return sendCode(identityType, identifier);
    }

    // 发送验证码操作
    private AjaxResult sendCode(int identityType, String identifier) {
        if (identityType == IdentityType.IDENTITY_TYPE_EMAIL) {
            // 邮箱验证
            if (!VerifyUtil.isEmail(identifier)) {
                return AjaxResult.fail("邮箱格式不正确");
            }

            // 邮箱验证码每天获取次数限制
            Integer count = (Integer) mRedisUtil.get(RedisKeyUtils.getMailCodeKey(identifier));
            if (count == null) {
                count = 0;
            }
            if (count > AppConfig.MAIL_CODE_LIMIT_COUNT) {
                return AjaxResult.fail(CodeConstants.OBTAIN_CODE_LIMIT, "验证码次数获取达上限");
            }

            String mailCode = VerifyUtil.generateVerCode();
            try {
                mEmailService.sendEmailVerCode(identifier, mailCode);

                // 将验证码放入到redis中，验证用户验证码的时候需要使用
                mRedisUtil.set(identifier, mailCode);
                // 设置有效期为 10 分钟
                mRedisUtil.expire(identifier, AppConfig.CODE_EXPIRE_TIME);

                count++;
                long time = DateUtils.getSecondsToday();
                mRedisUtil.set(RedisKeyUtils.getMailCodeKey(identifier), count, time);
            } catch (MailSendException e) {
                e.printStackTrace();
                return AjaxResult.success("获取失败");
            }
        } else if (identityType == IdentityType.IDENTITY_TYPE_PHONE) {
            // 手机号验证
            if (!VerifyUtil.isPhone(identifier)) {
                return AjaxResult.fail("手机号码格式不正确");
            }
        } else {
            return AjaxResult.fail(CodeConstants.PARAM_ERROR, "identifier param error");
        }

        return AjaxResult.success("发送成功");
    }

}
