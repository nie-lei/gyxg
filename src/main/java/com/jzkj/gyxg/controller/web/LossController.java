package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.LossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/loss/")
public class LossController extends BaseController{
    @Autowired
    private LossService lossService;

    /**
     * 报损新增
     * @return
     */
    @RequestMapping("lossadd")
    public ResponseJson lossAdd() throws AjaxOperationFailException {
        return lossService.lossAdd();
    }

    /**
     * 报损修改
     * @return
     */
    @RequestMapping("lossedit")
    public ResponseJson lossEdit() throws AjaxOperationFailException {
        return lossService.lossEdit();
    }

    /**
     * 报损取消
     * @return
     */
    @RequestMapping("losscancel")
    public ResponseJson lossCancel() throws AjaxOperationFailException {
        return lossService.lossCancel();
    }

    /**
     * 报损审核
     * @return
     */
    @RequestMapping("lossauth")
    public ResponseJson lossAuth() throws AjaxOperationFailException {
        return lossService.lossAuth();
    }

    /**
     * 报损列表
     * @return
     */
    @RequestMapping("losslist")
    public ResponseJson lossList() throws AjaxOperationFailException {
        return lossService.lossList();
    }

    /**
     * 报损原料新增
     * @return
     */
    @RequestMapping("lossgoodsadd")
    public ResponseJson lossGoodsAdd() throws AjaxOperationFailException {
        return lossService.lossGoodsAdd();
    }

    /**
     * 报损原料修改
     * @return
     */
    @RequestMapping("lossgoodsedit")
    public ResponseJson lossGoodsEdit() throws AjaxOperationFailException {
        return lossService.lossGoodsEdit();
    }

    /**
     * 报损原料删除
     * @return
     */
    @RequestMapping("lossgoodsdel")
    public ResponseJson lossGoodsDel() throws AjaxOperationFailException {
        return lossService.lossGoodsDel();
    }

    /**
     * 报损原料列表
     * @return
     */
    @RequestMapping("lossgoodslist")
    public ResponseJson lossGoodsList() throws AjaxOperationFailException {
        return lossService.lossGoodsList();
    }
}
