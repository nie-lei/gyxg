package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.AreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/areas")
public class AreasController extends BaseController{

    @Autowired
    private AreasService areasService;

    @RequestMapping("/list")
    public ResponseJson areasList(){
       return areasService.areasList();
    }

    @RequestMapping("/add")
    public ResponseJson areasAdd() throws AjaxOperationFailException {
        return areasService.areasAdd();
    }

    @RequestMapping("/edit")
    public ResponseJson areasEdit() throws AjaxOperationFailException {
        return areasService.areasEdit();
    }

    @RequestMapping("/del")
    public ResponseJson areasDel() throws AjaxOperationFailException {
        return areasService.areasDel();
    }

    @RequestMapping("/change")
    public ResponseJson areasChange() throws AjaxOperationFailException {
        return areasService.areasChange();
    }
}
