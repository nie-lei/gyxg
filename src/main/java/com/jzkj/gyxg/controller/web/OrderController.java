package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.common.UtilConfig;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.entity.OrderMaster;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.OrderMasterMapper;
import com.jzkj.gyxg.service.web.OrderService;
import com.jzkj.gyxg.util.GetMAC;
import com.jzkj.gyxg.util.HttpUtil;
import com.jzkj.gyxg.util.MacUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/order/")
@RestController
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMasterMapper orderMasterMapper;
    @Autowired
    private UtilConfig utilConfig;

    /**
     *餐桌
     * @return
     */
    @RequestMapping("tablelist")
    public ResponseJson selectTables() throws AjaxOperationFailException {
        return orderService.selectTables();
    }

    /**
     * 点击已经开台的桌子，详情
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("orderinfo")
    public ResponseJson tableDetail() throws AjaxOperationFailException {
        return orderService.tableDetail();
    }


    @RequestMapping("arealist")
    public ResponseJson selectAreas() throws AjaxOperationFailException {
        return orderService.selectAreas();
    }

    /**
     * 开台
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("addorderinfo")
    public ResponseJson addOrderInfo() throws AjaxOperationFailException {
        return orderService.addOrderInfo();
    }

    /**
     * 就餐人数变更
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("editnums")
    public ResponseJson editNums() throws AjaxOperationFailException {
        return orderService.editNums();
    }


    /**
     * 合台
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("hetai")
    public ResponseJson hetai() throws AjaxOperationFailException {
        return orderService.hetai();
    }


    /**
     * 转台
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("change")
    public ResponseJson change() throws AjaxOperationFailException {
        return orderService.change();
    }


    /**
     * 取消开台
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("cancel")
    public ResponseJson cancel() throws AjaxOperationFailException {
        return orderService.cancel();
    }



    /**
     * 点菜提交
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("adddishes")
    public ResponseJson addDishes() throws AjaxOperationFailException {
        Integer tag = HttpRequestParamter.getInt("tag",1);
        return orderService.addDishes(tag);
    }

    /**
     * 点菜(头部分类只有第2级)
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("dishesclassfy2")
    public ResponseJson dishesClassfy2() throws AjaxOperationFailException {
        return orderService.dishesClassfy2();
    }

    /**
     * 底部菜品列表
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("disheslist")
    public ResponseJson dishesList() throws AjaxOperationFailException {
        return orderService.dishesList();
    }

    /**
     * 通过拼音查询菜品点菜
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("selectdishesbypinyin")
    public ResponseJson selectdishesBypinyin() throws AjaxOperationFailException {
        return orderService.selectdishesBypinyin();
    }

    /**
     * 转菜（从一个订单到另一个订单下面）
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("disheschangeorder")
    public ResponseJson dishesChangeOrder() throws AjaxOperationFailException {
        return orderService.dishesChangeOrder();
    }

    /**
     * 获取订单打折金额
     * @param orderId 订单号
     * @param discountratio 折扣比例
     * @param isall 是否整单 1是0否
     * @param settleType 1单台，2连台
     * @return
     * @throws AjaxOperationFailException
     */
    @PostMapping("getOrderDiscountAmount")
    public ResponseJson getOrderDiscountAmount(@RequestParam("orderId") Integer orderId,
                                               @RequestParam("discountratio") Double discountratio,
                                               @RequestParam("isall") String isall,
                                               @RequestParam("settleType") Integer settleType) throws AjaxOperationFailException {
        return orderService.getOrderDiscountAmount(orderId,discountratio,isall,settleType);
    }


    /**
     * 结算菜单以及价格
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("orderdisheslist")
    public ResponseJson orderDishesList() throws AjaxOperationFailException {
        Integer tag = HttpRequestParamter.getInt("tag",1);//默认为1，单台或者合台或者连台单结算（当连台多结时候参数>0）
        if(tag==1){
           return  orderService.orderDishesList();
        }else {
            return orderService.orderDishesListAll();
        }
    }


    /**
     * 结账
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("settleaccounts1")
    public ResponseJson useAccount1() throws AjaxOperationFailException {
        return orderService.useAccount1();
    }

    /**
     * 结账
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("settleaccounts")
    public ResponseJson useAccount() throws AjaxOperationFailException {
        return orderService.useAccount();
    }



    /**
     * 结账(反结)
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("settleaccounts2")
    public ResponseJson useAccount2() throws AjaxOperationFailException {
        return orderService.useAccount2();
    }



    /**
     * overtype 状态：1-签单 2-三倍充值免单 3-霸王餐  4-免单
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("settleaccountsovertype")
    public ResponseJson useAccountOvertype() throws AjaxOperationFailException {
        return orderService.useAccountOvertype();
    }



    @RequestMapping("settleaccountsovertype2")
    public ResponseJson useAccountOvertype2() throws AjaxOperationFailException {
        return orderService.useAccountOvertype2();
    }


    /**
     * 消费查询
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("ordermasterlistinfo")
    public ResponseJson orderMasterListInfo() throws AjaxOperationFailException {
        return orderService.selectOrderInfoList();
    }


    /**
     * 消费明细查询
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("orderinfodetail")
    public ResponseJson orderInfoDetail() throws AjaxOperationFailException {
        return orderService.orderInfoDetail();
    }



    /**
     * 支付明细查询
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("orderpayinfodetail")
    public ResponseJson orderPayInfoDetail() throws AjaxOperationFailException {
        return orderService.orderPayInfoDetail();
    }


    /**
     * 点菜删除
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("dishesdel")
    public ResponseJson dishesDel() throws AjaxOperationFailException {
        return orderService.dishesDel();
    }


    /**
     * 签单支付
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("signpay")
    public ResponseJson signPay() throws AjaxOperationFailException {
        return orderService.signPay();
    }



    /**
     * 返回所有点的菜
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("fanjiefirst")
    public ResponseJson fanjieFirst() throws AjaxOperationFailException {
        return orderService.fanjieFirst();
    }


    /**
     * 选中菜进行修改（数量）
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("changedishesamount")
    public ResponseJson changeDishesAmount() throws AjaxOperationFailException {
        return orderService.changeDishesAmount();
    }


    /**
     * （反结计算价格并且返回点菜统计）
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("fanjiesecond")
    public ResponseJson fanjieSecond() throws AjaxOperationFailException {
        return orderService.fanjieSecond();
    }


    /**
     * 点击退款（自动操作）
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("fanjiethird")
    public ResponseJson fanjieThird() throws AjaxOperationFailException {
        return orderService.fanjieThird();
    }

    /**
     * 反结
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("fanjie")
    public ResponseJson fanjie() throws AjaxOperationFailException {
        return orderService.fanjie();
    }

    /**
     * 补打印
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("printdishes")
    public ResponseJson printDishes() throws AjaxOperationFailException {
        return orderService.printDishes();
    }


    /**
     * 菜单打印
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("printalldishes")
    public ResponseJson printAllDishes() throws AjaxOperationFailException {
        Integer orderid = HttpRequestParamter.getInt("orderid",0);
        Integer tag = HttpRequestParamter.getInt("tag",0);
        if(orderid == 0){
            throw new AjaxOperationFailException("订单id缺失!");
        }
        if(tag == 0){
            throw new AjaxOperationFailException("参数缺失!");
        }
        OrderMaster master = orderMasterMapper.selectByPrimaryKey(orderid);
        if(tag == 1){
            master.setIsmultiple(1);
        }else {
            master.setIsmultiple(2);
        }
        if(master == null){
            throw new AjaxOperationFailException("订单信息不存在!");
        }
        orderMasterMapper.updateByPrimaryKey(master);
        return orderService.printAllDishes(master,"4");
    }

    /**
     * 退款
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/refund")
    public ResponseJson refund() throws AjaxOperationFailException{
        return orderService.refund();
    }


    @RequestMapping("/getMac")
    public ResponseJson getMac() {
        String type = utilConfig.getTypename();
        System.out.println(type);
        String ip = HttpUtil.getClientIp();

        if ("windows".equals(type)) {
            return new ResponseJson(MacUtil.getMacInWindows(ip));
        } else {
            return new ResponseJson(MacUtil.getMacInLinux(ip));
        }
    }
}
