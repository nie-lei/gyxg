package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.entity.Paytype;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.PaytypeMapper;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaytypeService {

    @Autowired
    private PaytypeMapper paytypeMapper;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 支付信息列表
     * @return
     */
    public ResponseJson list() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String is_quan = HttpRequestParamter.getString("isquan",true);
        Map params = new HashMap<>();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        if(!StringUtil.isNull(is_quan)){
            params.put("is_quan",is_quan);
        }
        PageHelper.startPage(page,size,"paycode asc");
        List<Paytype> list = paytypeMapper.selectList(params);
        PageInfo<Paytype> pageinfo = new PageInfo<Paytype>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 新增支付方式
     * @return
     */
    public ResponseJson add() throws AjaxOperationFailException {
        Integer quannum = HttpRequestParamter.getInt("quannum",1);
        String payname = HttpRequestParamter.getString("payname",true);
        Double quanmoney = HttpRequestParamter.getDouble("quanmoney",0.00);
        Double discountmoney = HttpRequestParamter.getDouble("discountmoney",0.00);
        Double minmoney = HttpRequestParamter.getDouble("minmoney",0.00);
        Paytype paytype = new Paytype();
        if(StringUtil.isNull(payname)){
            throw new AjaxOperationFailException("优惠券名称不能为空");
        }
        if(quanmoney == 0.00){
            throw new AjaxOperationFailException("优惠券金额不能为空");
        }
        if(discountmoney == 0.00){
            throw new AjaxOperationFailException("折扣金额不能为空");
        }
        Employees loginuser = employeeService.getLoginUser();
        paytype.setCompanyid(loginuser.getCompanyid());
        paytype.setStoreid(loginuser.getStoreid());
        paytype.setQuannum(quannum);
        paytype.setPayname(payname);
        paytype.setQuanmoney(quanmoney);
        paytype.setDiscountmoney(discountmoney);
        paytype.setMinmoney(minmoney);
        paytype.setStatus("1");
        paytype.setIsQuan("1");
        Map params = new HashMap();
        params.put("companyid",loginuser.getCompanyid());
        params.put("storeid",loginuser.getStoreid());
        int maxpaycode = paytypeMapper.selectMaxPaycode(params);
        paytype.setPaycode(maxpaycode+1);
        paytypeMapper.insert1(paytype);
        return new ResponseJson("新增成功");
    }

    public ResponseJson edid() throws AjaxOperationFailException {
        Integer quannum = HttpRequestParamter.getInt("quannum",0);
        Integer id = HttpRequestParamter.getInt("id",0);
        String payname = HttpRequestParamter.getString("payname",true);
        Double quanmoney = HttpRequestParamter.getDouble("quanmoney",0.00);
        Double discountmoney = HttpRequestParamter.getDouble("discountmoney",0.00);
        Double minmoney = HttpRequestParamter.getDouble("minmoney",0.00);
        if(id == 0){
            throw new AjaxOperationFailException("id缺失");
        }
        if(StringUtil.isNull(payname)){
            throw new AjaxOperationFailException("优惠券名称不能为空");
        }
        if(quanmoney == 0.00){
            throw new AjaxOperationFailException("优惠券金额不能为空");
        }
        if(discountmoney == 0.00){
            throw new AjaxOperationFailException("折扣金额不能为空");
        }
        if(minmoney == 0.00){
            throw new AjaxOperationFailException("最低消费金额不能为空");
        }
        Paytype paytype = paytypeMapper.selectByPrimaryId(id);
        if(paytype == null ){
            throw new AjaxOperationFailException("优惠券信息不存在");
        }
        Employees loginuser = employeeService.getLoginUser();
        paytype.setCompanyid(loginuser.getCompanyid());
        paytype.setStoreid(loginuser.getStoreid());
        paytype.setQuannum(quannum);
        paytype.setPayname(payname);
        paytype.setQuanmoney(quanmoney);
        paytype.setDiscountmoney(discountmoney);
        paytype.setMinmoney(minmoney);
        paytype.setStatus("1");
        paytype.setIsQuan("1");
        Map params = new HashMap();
        params.put("companyid",loginuser.getCompanyid());
        params.put("storeid",loginuser.getStoreid());
        paytypeMapper.updateByPrimaryId(paytype);
        return new ResponseJson("新增成功");
    }


    public ResponseJson changestatus() throws AjaxOperationFailException {
        Integer id = HttpRequestParamter.getInt("id",0);
        String status = HttpRequestParamter.getString("status",true);
        if(id == 0){
            throw new AjaxOperationFailException("id缺失");
        }
        if(StringUtil.isNull(status)){
            throw new AjaxOperationFailException("上下架状态不能为空");
        }
        Paytype paytype = paytypeMapper.selectByPrimaryId(id);
        if(paytype == null ){
            throw new AjaxOperationFailException("优惠券信息不存在");
        }
        paytype.setStatus(status);
        paytypeMapper.updateByPrimaryId(paytype);
        return new ResponseJson("操作成功");
    }


    public ResponseJson del() throws AjaxOperationFailException {
        Integer id = HttpRequestParamter.getInt("id",0);
        if(id == 0){
            throw new AjaxOperationFailException("id缺失");
        }
        Paytype paytype = paytypeMapper.selectByPrimaryId(id);
        if(paytype == null ){
            throw new AjaxOperationFailException("优惠券信息不存在");
        }
        paytypeMapper.deleteByPrimaryId(id);
        return new ResponseJson("删除成功");
    }


    /**
     * 支付方式类型
     * @return
     */
    public ResponseJson selectpaytype() {
        Employees loginuser = employeeService.getLoginUser();
        Map params = new HashMap();
        params.put("companyid",loginuser.getCompanyid());
        params.put("storeid",loginuser.getStoreid());
        List<Integer>  list = paytypeMapper.selectStatus0(params);

        //新版本获取支付类型信息（等待启用）
//        Paytype paytype = new Paytype();
//        paytype.setCompanyid(loginuser.getCompanyid());
//        paytype.setStoreid(loginuser.getStoreid());
//        paytype.setStatus("0");//状态：0-上架，1-下架
//        paytype.setIsQuan("0");//0-不是优惠券，1-优惠券
//        List<Paytype>  lpayList = paytypeMapper.findPayTypeList(paytype);

        return new ResponseJson(list);
    }


    /**
     * 获取所有优惠券
     * @return
     */
    public ResponseJson selectallquan() {
        Employees loginuser = employeeService.getLoginUser();
        Map params = new HashMap();
        params.put("companyid",loginuser.getCompanyid());
        params.put("storeid",loginuser.getStoreid());
        List<Paytype>  list = paytypeMapper.selectquan(params);
        return new ResponseJson(list);
    }

    /**
     * 充值订金处获取支付方式
     * @return
     */
    public ResponseJson selectpay() {
        return selectpaytype();
    }
}
