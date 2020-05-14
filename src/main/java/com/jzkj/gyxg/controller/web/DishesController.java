package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.entity.DishePages;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.DishesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/dishes")
public class DishesController extends BaseController{

    @Autowired
    private DishesService dishesService;



    @RequestMapping("/dishespagelist")
    public ResponseJson dishesPageList(){
        return dishesService.dishesPageList();
    }


    @RequestMapping("/dishespageadd")
    public ResponseJson dishesPageAdd() throws AjaxOperationFailException {
        return dishesService.dishesPageAdd();
    }


    /**
     * 修改菜谱页
     */
    @PostMapping("/dishesPageEdit")
    public ResponseJson dishesPageEdit(@RequestBody DishePages dishePages) throws AjaxOperationFailException {
        return dishesService.dishesPageEdit(dishePages);
    }

    @RequestMapping("/dishespagedel")
    public ResponseJson dishesPagedel() throws AjaxOperationFailException {
        return dishesService.dishesPagedel();
    }

    @RequestMapping("/dishespagechangestatus")
    public ResponseJson dishesPageChangeStatus() throws AjaxOperationFailException {
        return dishesService.dishesPageChangeStatus();
    }

    @RequestMapping("/disheslist")
    public ResponseJson dishesList(){
        return dishesService.dishesList();
    }


    @RequestMapping("/setisauto")
    public ResponseJson setIsauto() throws AjaxOperationFailException {
        return dishesService.setIsauto();
    }

    @RequestMapping("/setstate")
    public ResponseJson setState() throws AjaxOperationFailException {
        return dishesService.setState();
    }


    @RequestMapping("/dishesadd")
    public ResponseJson dishesAdd() throws AjaxOperationFailException {
        return dishesService.dishesAdd();
    }

    @RequestMapping("/dishesedit")
    public ResponseJson dishesEdit() throws AjaxOperationFailException {
        return dishesService.dishesEdit();
    }

    @RequestMapping("/dishesdel")
    public ResponseJson dishesDel() throws AjaxOperationFailException {
        return dishesService.dishesDel();
    }


    @RequestMapping("/disheslistmethods")
    public ResponseJson dishesListMethods() throws AjaxOperationFailException {
        return dishesService.dishesListMethods();
    }

    @RequestMapping("/dishesaddmethods")
    public ResponseJson dishesAddMethods() throws AjaxOperationFailException {
        return dishesService.dishesAddMethods();
    }

    @RequestMapping("/dishesdelmethods")
    public ResponseJson dishesDelMethods() throws AjaxOperationFailException {
        return dishesService.dishesDelMethods();
    }

    @RequestMapping("/disheschangestatus")
    public ResponseJson dishesChangeStatus() throws AjaxOperationFailException {
        return dishesService.dishesChangeStatus();
    }

    @RequestMapping("/dishesguqinglist")
    public ResponseJson dishesGuqingList() throws AjaxOperationFailException {
        return dishesService.dishesGuqingList();
    }

    @RequestMapping("/dishesguqing")
    public ResponseJson dishesGuqing() throws AjaxOperationFailException {
        return dishesService.dishesGuqing();
    }

    @RequestMapping("/dishesamountguqing")
    public ResponseJson dishesAmountGuqing() throws AjaxOperationFailException {
        return dishesService.dishesAmountGuqing();
    }

    @RequestMapping("/methodslist")
    public ResponseJson methosdList(){
        return dishesService.methosdList();
    }


    @RequestMapping("/methodsadd")
    public ResponseJson methodsAdd() throws AjaxOperationFailException {
        return dishesService.methodsAdd();
    }

    @RequestMapping("/methodsedit")
    public ResponseJson methodsEdit() throws AjaxOperationFailException {
        return dishesService.methodsEdit();
    }

    @RequestMapping("/dishesclassfylist")
    public ResponseJson dishesClassfyList() throws AjaxOperationFailException {
        return dishesService.dishesClassfyList();
    }

    @RequestMapping("/dishesclassfyadd")
    public ResponseJson dishesClassfyAdd() throws AjaxOperationFailException {
        return dishesService.dishesClassfyAdd();
    }


    @RequestMapping("/dishesclassfyedit")
    public ResponseJson dishesclassfyEdit() throws AjaxOperationFailException {
        return dishesService.dishesclassfyEdit();
    }

    @RequestMapping("/dishesclassfydel")
    public ResponseJson dishesclassfyDel() throws AjaxOperationFailException {
        return dishesService.dishesclassfyDel();
    }



    @RequestMapping("/dishesclassfychangeisshow")
    public ResponseJson dishesclassfyChangeIsshow() throws AjaxOperationFailException {
        return dishesService.dishesclassfyChangeIsshow();
    }

    @RequestMapping("/dishesclassfychangestatus")
    public ResponseJson dishesclassfyChangeStatus() throws AjaxOperationFailException {
        return dishesService.dishesclassfyChangeStatus();
    }


}
