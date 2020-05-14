package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.StoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/stores")
public class StroresController extends BaseController{

    @Autowired
    private StoresService storesService;

    /**
     * 店铺参数设置
     * @return
     */
    @RequestMapping("/set")
    public ResponseJson set() throws AjaxOperationFailException {
        return storesService.set();
    }

    /**
     * 店铺参数获取
     * @return
     */
    @RequestMapping("/get")
    public ResponseJson get() throws AjaxOperationFailException {
        return storesService.get();
    }
}
