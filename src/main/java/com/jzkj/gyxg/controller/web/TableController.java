package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tables")
public class TableController extends BaseController {

    @Autowired
    private TableService tableService;

    /**
     * 餐桌信息新增
     * @return
     */
    @RequestMapping("/tablesadd")
    public ResponseJson tableAdd() throws AjaxOperationFailException {
        return tableService.tableAdd();
    }

    /**
     * 餐桌信息修改
     * @return
     */
    @RequestMapping("/tablesedit")
    public ResponseJson tableEdit() throws AjaxOperationFailException {
        return tableService.tableEdit();
    }

    /**
     * 餐桌开启关闭
     * @return
     */
    @RequestMapping("/change")
    public ResponseJson tableChange() throws AjaxOperationFailException {
        return tableService.tableChange();
    }

    /**
     * 餐桌信息删除
     * @return
     */
    @RequestMapping("/tablesdel")
    public ResponseJson tablesDel() throws AjaxOperationFailException {
        return tableService.tablesDel();
    }

    /**
     * 餐桌信息列表
     * @return
     */
    @RequestMapping("/tableslist")
    public ResponseJson tablesList() throws AjaxOperationFailException {
        return tableService.tablesList();
    }
}
