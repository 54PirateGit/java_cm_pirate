package com.chengm.pirate.controller.verity;

import com.chengm.pirate.base.impl.BaseBizController;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.pojo.UserAuth;
import com.chengm.pirate.pojo.UserExtra;
import com.chengm.pirate.service.UserAuthService;
import com.chengm.pirate.service.UserBaseService;
import com.chengm.pirate.service.UserExtraService;
import com.chengm.pirate.utils.CollectionsUtil;
import com.chengm.pirate.utils.StringUtil;
import com.chengm.pirate.utils.constant.CodeConstants;
import com.chengm.pirate.utils.constant.IdentityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Autowired
    public VerityController(UserAuthService service1, UserExtraService service2, UserBaseService service3) {
        this.mUserAuthService = service1;
        this.mUserExtraService = service2;
        this.mUserBaseService = service3;
    }

    /**
     * 发送验证码
     */
    @RequestMapping("/verity/sendMsg")
    public AjaxResult sendMsg() {
        int identityType = requireIntParam("identityType");
        String identifier = requireStringParam("identifier");

        if (identityType == IdentityType.IDENTITY_TYPE_EMAIL) {
            // 邮箱验证

        } else {
            // 手机号验证

        }
        return AjaxResult.success("发送成功");
    }

    /**
     * 验证device
     */
    @RequestMapping("/verity/device")
    public AjaxResult verityDevice() {
        int identityType = requireIntParam("identityType");
        String identifier = requireStringParam("identifier");
        String deviceId = requireStringParam("deviceId");
        String code = requireStringParam("code");

        if (identityType == IdentityType.IDENTITY_TYPE_PHONE) {
            if (StringUtil.isEmpty(code)) {
                return AjaxResult.failInvalidParameter("code");
            }
            if (!code.equals("123456")) {
                return AjaxResult.fail(CodeConstants.ERROR_CODE_PARAM_ERROR, "验证码不正确");
            }
        } else if (identityType == IdentityType.IDENTITY_TYPE_EMAIL) {
            if (StringUtil.isEmpty(code)) {
                return AjaxResult.failInvalidParameter("code");
            }
            if (!code.equals("123456")) {
                return AjaxResult.fail(CodeConstants.ERROR_CODE_PARAM_ERROR, "验证码不正确");
            }
        } else {
            return AjaxResult.failInvalidParameter("identityType");
        }

        UserAuth userAuth = mUserAuthService.getUser(identifier);
        if (userAuth == null) {
            return AjaxResult.fail(CodeConstants.USER_NOT_EXIST, "用户不存在");
        }

        UserExtra userExtra = mUserExtraService.getUserExtra(userAuth.getUid());
        if (userExtra == null) {
            return AjaxResult.fail(CodeConstants.USER_NOT_EXIST, "账号异常，请联系管理员");
        }
        String loginDeviceId = userExtra.getDeviceId();
        if (!StringUtil.isEmpty(loginDeviceId)) {
            String[] dIds = loginDeviceId.split(",");
            List<String> ids = Arrays.asList(dIds);
            if (!ids.contains(deviceId)) {
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
                    String deviceIds = builder.toString();
                    deviceId = deviceIds + deviceId;
                }
            }
        }
        mUserExtraService.updateDeviceId(userAuth.getUid(), deviceId);
        return AjaxResult.success();
    }

}
