package com.chengm.pirate.exception;

import com.chengm.pirate.utils.constant.CodeConstants;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * program: CmPirate
 * description:
 * author: ChengMo
 * create: 2019-11-30 15:08
 **/
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public Map<String, Object> handleException(Exception e) {
        e.printStackTrace();
        Map<String, Object> map = new HashMap<>();

        // 捕获自定义异常信息返回前端显示
        if (e instanceof InvokeException) {
            InvokeException ex = (InvokeException)e;
            map.put("code", ex.getErr());
            map.put("msg", ex.getMessage());
            return map;
        }

        map.put("code", CodeConstants.EXCEPTION);
        map.put("msg", "未知异常");
        return map;
    }

    @ExceptionHandler
    @ResponseBody
    public Map<String, Object>handleArithmeticException(ArithmeticException e) {
        e.printStackTrace();
        Map<String, Object> map = new HashMap<>();
        map.put("code", CodeConstants.ARITHMETIC_EXCEPTION);
        map.put("msg", "算术异常");
        return map;
    }

}
