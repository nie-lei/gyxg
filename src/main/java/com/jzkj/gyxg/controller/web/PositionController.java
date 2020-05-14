package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/positions")
public class PositionController extends BaseController{

    @Autowired
    private PositionService positionService;

    /**
     * 查询职位列表
     * @return
     */
    @RequestMapping("list")
    public ResponseJson list(){
        return positionService.list();
    }

    /**
     * 新增职位信息
     * @return
     */
    @RequestMapping("add")
    public ResponseJson add() throws AjaxOperationFailException {
        return positionService.add();
    }

    /**
     * 修改职位信息
     * @return
     */
    @RequestMapping("edit")
    public ResponseJson edit() throws AjaxOperationFailException {
        return positionService.edit();
    }
    /**
     * 删除职位信息
     * @return
     */
    @RequestMapping("del")
    public ResponseJson del() throws AjaxOperationFailException {
        return positionService.del();
    }
}
