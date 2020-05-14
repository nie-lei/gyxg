package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.PrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/print")
@RestController
public class PrintController extends BaseController{

    @Autowired
    private PrintService printService;

    /**
     * 打印类别列表
     * @return
     */
    @RequestMapping("/printlist")
    public ResponseJson printList() throws AjaxOperationFailException {
        return printService.printList();
    }

    /**
     * 打印类别新增
     * @return
     */
    @RequestMapping("/printadd")
    public ResponseJson printAdd() throws AjaxOperationFailException {
        return printService.printAdd();
    }

    /**
     * 打印类别修改
     * @return
     */
    @RequestMapping("/printedit")
    public ResponseJson printEdit() throws AjaxOperationFailException {
        return printService.printEdit();
    }

    /**
     * 打印类别删除
     * @return
     */
    @RequestMapping("/printdel")
    public ResponseJson printDel() throws AjaxOperationFailException {
        return printService.printDel();
    }



    /**
     * 打印机信息列表
     * @return
     */
    @RequestMapping("/printerlist")
    public ResponseJson printerList() throws AjaxOperationFailException {
        return printService.printerList();
    }

    /**
     * 打印机新增
     * @return
     */
    @RequestMapping("/printeradd")
    public ResponseJson printerAdd() throws AjaxOperationFailException {
        return printService.printerAdd();
    }

    /**
     * 打印机修改
     * @return
     */
    @RequestMapping("/printeredit")
    public ResponseJson printerEdit() throws AjaxOperationFailException {
        return printService.printerEdit();
    }

    /**
     * 打印机删除
     * @return
     */
    @RequestMapping("/printerdel")
    public ResponseJson printerDel() throws AjaxOperationFailException {
        return printService.printerDel();
    }

    /**
     * 打印机启用禁用
     * @return
     */
    @RequestMapping("/printerchange")
    public ResponseJson printerChange() throws AjaxOperationFailException {
        return printService.printerChange();
    }
}
