package com.chengm.pirate.base.impl;

import com.chengm.pirate.base.BaseController;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.exception.InvokeException;
import com.chengm.pirate.utils.StringUtil;
import com.chengm.pirate.utils.VerifyUtil;
import com.chengm.pirate.utils.constant.CodeConstants;
import com.chengm.pirate.utils.constant.Constants;
import com.chengm.pirate.utils.log.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * program: CmPirate
 * description: 业务处理器基类
 * author: ChengMo
 * create: 2019-12-01 21:34
 **/
public class BaseBizController implements BaseController {

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    protected HttpSession session;

    private Logger logger = LoggerFactory.getLogger(BaseBizController.class);

    // 用户设备信息，用于风控校验
    private String deviceId;
    private String osName;
    private String osVersion;

    // 非必须，有的话就传
    private String clientVersion;// 客户端版本号
    private String vendor;// 手机厂商
    private String deviceName;// 设备型号，如:iphone6s、u880、u8800
    private String idfa;// 苹果设备的IDFA
    private String idfv;// 苹果设备的IDFV

    /**
     * spring ModelAttribute
     * 放置在方法上面：表示请求该类的每个Action前都会首先执行它，也可以将一些准备数据的操作放置在该方法里面
     */
    @ModelAttribute
    public void setBaseBizController(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();

        // 获取头部参数
        this.deviceId = request.getHeader("deviceId");
        this.osName = request.getHeader("osName");
        this.osVersion = request.getHeader("osVersion");

        if (StringUtil.isEmpty(deviceId) || StringUtil.isEmpty(osName) || StringUtil.isEmpty(osVersion)) {
            throw new InvokeException(CodeConstants.ERROR_CODE_NOT_REQUIRED_PARAM, "参数缺失");
        }
        // 客户端验证， 目前支持 ANDROID IOS
        if (!VerifyUtil.isClient(getOsName())) {
            throw new InvokeException(CodeConstants.ERROR_CODE_INVALID_PARAMETER, "osName");
        }

        // 头部非必须参数，有的话就传，必要的时候易于扩展
        this.clientVersion = request.getHeader("clientVersion");
        this.vendor = request.getHeader("vendor");
        this.deviceName = request.getHeader("deviceName");
        this.idfa = request.getHeader("idfa");
        this.idfv = request.getHeader("idfv");

        LogUtil.logValue("DeviceInfo : " + this.deviceId + "," + this.osName + "," +
                this.osVersion + "," + this.clientVersion + "," + this.vendor + "," + this.deviceName);
    }

    @Override
    public HttpServletRequest getRequest() {
        return this.request;
    }

    @Override
    public HttpServletResponse getResponse() {
        return this.response;
    }

    @Override
    public HttpSession getSession() {
        return this.session;
    }

    @Override
    public String getIp() {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public String getStringParam(String paramName) {
        String result = request.getParameter(paramName);
        result = StringUtil.decodeHtml(result);
        return result;
    }

    @Override
    public String getStringParam(String paramName, String defaultValue) {
        String result = getStringParam(paramName);
        if (StringUtil.isEmpty(result)) {
            return defaultValue;
        }
        return result;
    }

    @Override
    public String requireStringParam(String paramName) {
        return requireStringParam(paramName, paramName + " is required.");
    }

    @Override
    public String requireStringParam(String paramName, String tips) {
        String result = getStringParam(paramName);
        if (StringUtil.isEmpty(result)) {
            throw new InvokeException(CodeConstants.ERROR_CODE_NOT_REQUIRED_PARAM, tips);
        }
        return result;
    }

    @Override
    public int getIntParam(String paramName) {
        return parseIntParam(paramName, 0);
    }

    @Override
    public int getIntParam(String paramName, int defaultValue) {
        return parseIntParam(paramName, defaultValue);
    }

    @Override
    public int requireIntParam(String paramName) {
        return requireIntParam(paramName, paramName + " is required.", paramName + " is not a number.");
    }

    @Override
    public int requireIntParam(String paramName, String tipsEmpty, String tipsNaN) {
        String result = request.getParameter(paramName);
        if (StringUtil.isEmpty(result)) {
            throw new InvokeException(CodeConstants.ERROR_CODE_NOT_REQUIRED_PARAM, tipsEmpty);
        }
        int value;
        try {
            value = Integer.parseInt(result);
        } catch (Exception ex) {
            logger.debug("参数`" + paramName + "`对应的值`" + result + "`不是数字，返回0", ex);
            throw new InvokeException(CodeConstants.ERROR_CODE_PARAM_ERROR, tipsNaN);
        }
        return value;
    }

    @Override
    public long getLongParam(String paramName) {
        return parseLongParam(paramName, 0);
    }

    @Override
    public long getLongParam(String paramName, long defaultValue) {
        return parseLongParam(paramName, defaultValue);
    }

    @Override
    public long requireLongParam(String paramName) {
        return requireLongParam(paramName, paramName + " is required.", paramName + " is not a number.");
    }

    @Override
    public long requireLongParam(String paramName, String tipsEmpty, String tipsNaN) {
        String result = request.getParameter(paramName);
        if (StringUtil.isEmpty(result)) {
            throw new InvokeException(CodeConstants.ERROR_CODE_NOT_REQUIRED_PARAM, tipsEmpty);
        }
        long value;
        try {
            value = Long.parseLong(result);
        } catch (Exception ex) {
            logger.debug("参数`" + paramName + "`对应的值`" + result + "`不是数字，返回0", ex);
            throw new InvokeException(CodeConstants.ERROR_CODE_PARAM_ERROR, tipsNaN);
        }
        return value;
    }

    @Override
    public double getDoubleParam(String paramName) {
        return parseDoubleParam(paramName, 0);
    }

    @Override
    public double getDoubleParam(String paramName, double defaultValue) {
        return parseDoubleParam(paramName, defaultValue);
    }

    @Override
    public double requireDoubleParam(String paramName) {
        return requireDoubleParam(paramName, paramName + " is required.", paramName + " is not a number.");
    }

    @Override
    public double requireDoubleParam(String paramName, String tipsEmpty, String tipsNaN) {
        String result = request.getParameter(paramName);
        if (StringUtil.isEmpty(result)) {
            throw new InvokeException(CodeConstants.ERROR_CODE_NOT_REQUIRED_PARAM, tipsEmpty);
        }
        double value;
        try {
            value = Double.parseDouble(result);
        } catch (Exception ex) {
            logger.debug("参数`" + paramName + "`对应的值`" + result + "`不是数字，返回0", ex);
            throw new InvokeException(CodeConstants.ERROR_CODE_PARAM_ERROR, tipsNaN);
        }
        return value;
    }

    @Override
    public Map getParamMap() {
        // request.getParameterMap 防止重名参数问题，所以把 value 做成了数组，我们这里不考虑参数重名问题
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> retMap = new HashMap<>();
        parameterMap.forEach((key, val) -> {
            retMap.put(key, val[0]);
        });
        return retMap;
    }

    @Override
    public String getRequestMethod() {
        return StringUtil.getNotNullStr(request.getMethod());
    }

    /**
     * 限定请求方式为post
     */
    public void requestMethodPost() {
        if (!getRequestMethod().equals("POST")) {
            throw new InvokeException(CodeConstants.ERROR_REQUEST_METHOD, "Request mode should be post");
        }
    }

    /**
     * 对 request 的 get/setAttribute 进行一层包装
     */
    protected Object attr(String key) {
        return request.getAttribute(key);
    }

    protected void attr(String key, Object value) {
        request.setAttribute(key, value);
    }

    private int parseIntParam(String paramName, int defaultValue) {
        String result = request.getParameter(paramName);
        if (StringUtil.isEmpty(result)) {
            return defaultValue;
        }
        int value;
        try {
            value = Integer.parseInt(result);
        } catch (Exception ex) {
            logger.debug("参数`" + paramName + "`对应的值`" + result + "`不是数字，返回0", ex);
            value = 0;
        }
        return value;
    }

    private long parseLongParam(String paramName, long defaultValue) {
        String result = request.getParameter(paramName);
        if (StringUtil.isEmpty(result)) {
            return defaultValue;
        }
        long value;
        try {
            value = Long.parseLong(result);
        } catch (Exception ex) {
            logger.debug("参数`" + paramName + "`对应的值`" + result + "`不是数字，返回0", ex);
            value = 0;
        }
        return value;
    }

    private double parseDoubleParam(String paramName, double defaultValue) {
        String result = request.getParameter(paramName);
        if (StringUtil.isEmpty(result)) {
            return defaultValue;
        }
        double value;
        try {
            value = Double.parseDouble(result);
        } catch (Exception ex) {
            logger.debug("参数`" + paramName + "`对应的值`" + result + "`不是数字，返回0", ex);
            value = 0;
        }
        return value;
    }

    /**
     * 是否启用校验
     */
    public boolean enableVerify() {
        if (StringUtil.isEmpty(this.deviceId)) {
            throw new InvokeException(CodeConstants.ERROR_CODE_NOT_REQUIRED_PARAM, "参数缺失");
        }

        // 如果获取到了用户deviceId则需要校验
        // 没有获取到则不在关键地方校验用户设备，用户账号容易存在风险
        // 默认必须要前端在头部参数中携带，但是如果用户权限拒绝则前端无法获取用户设备信息，前端提示，用户自己承担风险
        return Constants.DEFAULT_DEVICE_ID.equals(this.deviceId);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getOsName() {
        return osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getClientVersion() {
        return StringUtil.getNotNullStr(clientVersion);
    }

    public String getVendor() {
        return StringUtil.getNotNullStr(vendor);
    }

    public String getDeviceName() {
        return StringUtil.getNotNullStr(deviceName);
    }

    public String getIdfa() {
        return StringUtil.getNotNullStr(idfa);
    }

    public String getIdfv() {
        return StringUtil.getNotNullStr(idfv);
    }
}
