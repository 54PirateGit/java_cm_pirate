package com.chengm.pirate.exception;

import com.chengm.pirate.utils.constant.CodeConstants;

/**
 * program: CmPirate
 * description: 业务异常类，WebExceptionHandler 中会统一处理这个异常返回到接口
 * author: ChengMo
 * create: 2019-12-01 21:38
 **/
public class InvokeException extends RuntimeException {

    private int err;

    public int getErr(){
        return err;
    }

    public InvokeException(String msg) {
        super(msg);
        this.err = CodeConstants.EXCEPTION;
    }

    public InvokeException(int err, String msg) {
        super(msg);
        this.err = err;
    }

}
