package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/goods")
@RestController
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;


    /**
     * 仓库新增
     * @return
     */
    @RequestMapping("/warehouseadd")
    public ResponseJson warehouseAdd() throws AjaxOperationFailException {
        return goodsService.warehouseAdd();
    }

    /**
     * 仓库修改
     * @return
     */
    @RequestMapping("/warehouseedit")
    public ResponseJson warehouseEdit() throws AjaxOperationFailException {
        return goodsService.warehouseEdit();
    }

    /**
     * 仓库删除
     * @return
     */
    @RequestMapping("/warehousedel")
    public ResponseJson warehouseDel() throws AjaxOperationFailException {
        return goodsService.warehouseDel();
    }

    /**
     * 仓库列表
     * @return
     */
    @RequestMapping("/warehouselist")
    public ResponseJson warehouseList(){
        return goodsService.warehouseList();
    }


    /**
     * 原料分类列表
     * @return
     */
    @RequestMapping("/materialclassifyslist")
    public ResponseJson materialClassifysList(){
        return goodsService.materialClassifysList();
    }

    /**
     * 原料分类新增
     * @return
     */
    @RequestMapping("/materialclassifysadd")
    public ResponseJson materialClassifysAdd() throws AjaxOperationFailException {
        return goodsService.materialClassifysAdd();
    }

    /**
     * 原料分类修改
     * @return
     */
    @RequestMapping("/materialclassifysedit")
    public ResponseJson materialClassifysEdit() throws AjaxOperationFailException {
        return goodsService.materialClassifysEdit();
    }

    /**
     * 原料分类删除
     * @return
     */
    @RequestMapping("/materialclassifysdel")
    public ResponseJson materialClassifysDel() throws AjaxOperationFailException {
        return goodsService.materialClassifysDel();
    }

    /**
     * 原料新增
     * @return
     */
    @RequestMapping("/goodsadd")
    public ResponseJson goodsAdd() throws AjaxOperationFailException {
        return goodsService.goodsAdd();
    }

    /**
     * 原料修改
     * @return
     */
    @RequestMapping("/goodsedit")
    public ResponseJson goodsEdit() throws AjaxOperationFailException {
        return goodsService.goodsEdit();
    }

    /**
     * 原料删除
     * @return
     */
    @RequestMapping("/goodsdel")
    public ResponseJson goodsDel() throws AjaxOperationFailException {
        return goodsService.goodsDel();
    }

    /**
     * 原料列表
     * @return
     */
    @RequestMapping("/goodslist")
    public ResponseJson goodsList() throws AjaxOperationFailException {
        return goodsService.goodsList();
    }


    /**
     * 入库新增
     * @return
     */
    @RequestMapping("/rukuadd")
    public ResponseJson rukuAdd() throws AjaxOperationFailException {
        return goodsService.rukuAdd("1");
    }

    /**
     * 入库修改
     * @return
     */
    @RequestMapping("/rukuedit")
    public ResponseJson rukuEdit() throws AjaxOperationFailException {
        return goodsService.rukuEdit("1");
    }

    /**
     * 入库取消
     * @return
     */
    @RequestMapping("/rukucancel")
    public ResponseJson rukuCancel() throws AjaxOperationFailException {
        return goodsService.rukuCancel();
    }

    /**
     * 入库列表
     * @return
     */
    @RequestMapping("/rukulist")
    public ResponseJson rukuList() throws AjaxOperationFailException {
        return goodsService.rukuList("1");
    }



    /**
     * 入库审核
     * @return
     */
    @RequestMapping("/rukuauth")
    public ResponseJson rukuAuth() throws AjaxOperationFailException {
        return goodsService.rukuAuth();
    }

    /**
     * 入库原料新增
     * @return
     */
    @RequestMapping("/goodsrukuadd")
    public ResponseJson goodsRukuAdd() throws AjaxOperationFailException {
        return goodsService.goodsRukuAdd();
    }

    /**
     * 入库原料修改
     * @return
     */
    @RequestMapping("/goodsrukuedit")
    public ResponseJson goodsRukuEdit() throws AjaxOperationFailException {
        return goodsService.goodsRukuEdit();
    }

    /**
     * 入库原料删除
     * @return
     */
    @RequestMapping("/goodsrukudel")
    public ResponseJson goodsRukuDel() throws AjaxOperationFailException {
        return goodsService.goodsRukuDel();
    }

    /**
     * 入库原料明细
     * @return
     */
    @RequestMapping("/goodsrukulist")
    public ResponseJson goodsRukuList() throws AjaxOperationFailException {
        return goodsService.goodsRukuList();
    }


    /**
     * 直拨新增
     * @return
     */
    @RequestMapping("/zhiboadd")
    public ResponseJson zhiboAdd() throws AjaxOperationFailException {
        return goodsService.rukuAdd("0");
    }

    /**
     * 直拨修改
     * @return
     */
    @RequestMapping("/zhiboedit")
    public ResponseJson zhiboEdit() throws AjaxOperationFailException {
        return goodsService.rukuEdit("0");
    }

    /**
     * 直拨列表
     * @return
     */
    @RequestMapping("/zhibolist")
    public ResponseJson zhiboList() throws AjaxOperationFailException {
        return goodsService.rukuList("0");
    }


    /**
     * 出库新增
     * @return
     */
    @RequestMapping("/chukuadd")
    public ResponseJson chuKuAdd() throws AjaxOperationFailException {
        return goodsService.chuKuAdd();
    }

    /**
     * 出库修改
     * @return
     */
    @RequestMapping("/chukuedit")
    public ResponseJson chukuEdit() throws AjaxOperationFailException {
        return goodsService.chukuEdit();
    }

    /**
     * 出库列表
     * @return
     */
    @RequestMapping("/chukulist")
    public ResponseJson chukuList() throws AjaxOperationFailException {
        return goodsService.chukuList();
    }


    /**
     * 出库取消操作
     * @return
     */
    @RequestMapping("/chukucancel")
    public ResponseJson chukuCancel() throws AjaxOperationFailException {
        return goodsService.chukuCancel();
    }

    /**
     * 出库审核
     * @return
     */
    @RequestMapping("/chukuauth")
    public ResponseJson chukuAuth() throws AjaxOperationFailException {
        return goodsService.chukuAuth();
    }


    @RequestMapping("/goodschukuadd")
    public ResponseJson goodsChukuAdd() throws AjaxOperationFailException {
        return goodsService.goodsChukuAdd();
    }
    @RequestMapping("/goodschukuedit")
    public ResponseJson goodsChukuEdit() throws AjaxOperationFailException {
        return goodsService.goodsChukuEdit();
    }

    @RequestMapping("/goodschukudel")
    public ResponseJson goodsChukuDel() throws AjaxOperationFailException {
        return goodsService.goodsChukuDel();
    }

    @RequestMapping("/goodschukulist")
    public ResponseJson goodsChukuList() throws AjaxOperationFailException {
        return goodsService.goodsChukuList();
    }

    @RequestMapping("/selectbygoodsid")
    public ResponseJson selectbyGoodsid() throws AjaxOperationFailException {
        return goodsService.selectbyGoodsid();
    }

    /**
     * 通过goodsid查询库存原料信息
     * @return
     * @throws AjaxOperationFailException
     */
    @GetMapping("/findkucunBygoodsId")
    public ResponseJson findGoodsStockByGoodsId()throws AjaxOperationFailException{
        return goodsService.findKucunGoodsByGoodsid();
    }

    /**
     * 库存管理
     * @return
     */
    @RequestMapping("/goodstocklist")
    public ResponseJson goodStockList() throws AjaxOperationFailException {
        return goodsService.goodStockList();
    }


    /**
     * 通过原料首拼查询
     * @return
     */
    @RequestMapping("/selectbypinyin")
    public ResponseJson selectbyPinyin() throws AjaxOperationFailException {
        return goodsService.selectbyPinyin();
    }

    @RequestMapping("/aa")
    public ResponseJson aa() throws AjaxOperationFailException {
        return goodsService.aa();
    }
}
