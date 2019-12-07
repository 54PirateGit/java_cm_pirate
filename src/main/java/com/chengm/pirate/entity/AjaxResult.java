package com.chengm.pirate.entity;

import com.chengm.pirate.utils.constant.CodeConstants;

/**
 * program: CmPirate
 * description: 返回数据
 * author: ChengMo
 * create: 2019-11-30 19:05
 **/
public class AjaxResult<T> {

    /**
     * 请求成功统一返回值
     */
    private static final int CODE_SUCCESS = CodeConstants.SUCCESS;
    private static final String MSG_SUCCESS = "success";

    /**
     * 请求失败统一返回值
     */
    private static final int CODE_FAIL = CodeConstants.FAIL;
    private static final String MSG_FAIL = "failed";

    private int code;
    private T data;
    private String msg;

    public AjaxResult() {

    }

    public AjaxResult(int code) {
        this.code = code;
    }

    public AjaxResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public AjaxResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public AjaxResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 请求成功无返回值
     */
    public static AjaxResult success() {
        return new AjaxResult(CODE_SUCCESS, MSG_SUCCESS);
    }

    /**
     * 请求成功并将数据返回
     */
    public static AjaxResult success(Object data) {
        return new AjaxResult(CODE_SUCCESS, MSG_SUCCESS, data);
    }

    /**
     * 请求失败无错误信息返回
     */
    public static AjaxResult fail() {
        return new AjaxResult(CODE_FAIL, MSG_FAIL);
    }

    /**
     * 请求失败并返回错误信息
     */
    public static AjaxResult fail(String msg) {
        return new AjaxResult(CODE_FAIL, msg);
    }

    /**
     * 无效的参数
     */
    public static AjaxResult failInvalidParameter() {
        return new AjaxResult(CodeConstants.ERROR_CODE_INVALID_PARAMETER, "Invalid parameter");
    }

    /**
     * 无效的参数
     */
    public static AjaxResult failInvalidParameter(Object param) {
        return new AjaxResult(CodeConstants.ERROR_CODE_INVALID_PARAMETER, "Invalid parameter:" + param);
    }

    /**
     * 请求失败，自定义错误码并返回错误信息
     */
    public static AjaxResult fail(int code, String msg) {
        return new AjaxResult(code, msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
