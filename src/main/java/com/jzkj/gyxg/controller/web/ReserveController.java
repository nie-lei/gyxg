package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/reserveds/")
public class ReserveController extends BaseController {

    @Autowired
    private ReserveService reserveService;

    /**
     * 预定新增
     * @return
     */
    @RequestMapping("add")
    public ResponseJson reservedAdd() throws AjaxOperationFailException {
        return reserveService.reservedAdd();
    }

    /**
     * 预定修改
     * @return
     */
    @RequestMapping("edit")
    public ResponseJson reservedEdit() throws AjaxOperationFailException {
        return reserveService.reservedEdit();
    }

    /**
     * 预定列表
     * @return
     */
    @RequestMapping("list")
    public ResponseJson reservedList() throws AjaxOperationFailException {
        return reserveService.reservedList();
    }

    /**
     * 预定取消
     * @return
     */
    @RequestMapping("cancel")
    public ResponseJson reservedCancel() throws AjaxOperationFailException {
        return reserveService.reservedCancel();
    }

    /**
     * 预定到达
     * @return
     */
    @RequestMapping("arrive")
    public ResponseJson reservedArrive() throws AjaxOperationFailException {
        return reserveService.reservedArrive();
    }

}
