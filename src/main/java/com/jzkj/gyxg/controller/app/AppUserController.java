package com.jzkj.gyxg.controller.app;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.app.AppUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/app/user/")
@RestController
public class AppUserController extends BaseController {
    @Resource
    private AppUserService appUserService;

    /**
     * 登录
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/login")
    public ResponseJson login() throws AjaxOperationFailException {
        return appUserService.login();
    }


    /**
     * 下单
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/order")
    public ResponseJson order() throws AjaxOperationFailException {
        return appUserService.order();
    }

    /**
     * 获取就餐区域
     * @return
     */
    @RequestMapping("/getArea")
    public ResponseJson getArea() throws AjaxOperationFailException {
        return appUserService.getArea();
    }

    /**
     * 获取餐桌信息
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getTablesInfo")
    public ResponseJson getTablesInfo() throws AjaxOperationFailException {
        return appUserService.getTablesInfo(0);
    }

    /**
     * 退菜
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/removeDishes")
    public ResponseJson removeDishes() throws AjaxOperationFailException {
        return appUserService.removeDishes();
    }

    /**
     * 修改餐桌人数
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/tablesNumEdit")
    public ResponseJson tablesNumEdit() throws AjaxOperationFailException {
        return appUserService.tablesNumEdit();
    }

    /**
     * 获取退菜原因
     * @return
     */
    @RequestMapping("/getAttachs")
    public ResponseJson getAttachs() throws AjaxOperationFailException {
        return appUserService.getAttachs();
    }

    /**
     * 存储支付编号
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/redisPayNo")
    public  ResponseJson redisPayNo() throws AjaxOperationFailException {
        return appUserService.redisPayNo();
    }

}
