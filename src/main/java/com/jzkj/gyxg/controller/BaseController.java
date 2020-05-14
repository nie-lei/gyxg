package com.jzkj.gyxg.controller;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseJson handException(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        Logger logger = Logger.getLogger("error");
        ResponseJson responseJson = new ResponseJson();
        if (e instanceof AjaxOperationFailException) {
            responseJson.setCode(1003);
            responseJson.setMsg(((AjaxOperationFailException) e).getErrorMessage());
        } else {
            e.printStackTrace();
            logger.error("error", e);
            responseJson.setMsg("服务器内部异常");
            responseJson.setCode(1004);
        }
        return responseJson;
    }
}