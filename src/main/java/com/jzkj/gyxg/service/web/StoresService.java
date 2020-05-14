package com.jzkj.gyxg.service.web;

import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.entity.Stores;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.StoresMapper;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StoresService {
    @Autowired
    private StoresMapper storesMapper;

    @Autowired
    private EmployeeService employeeService;
    /**
     * 店铺参数设置
     * @return
     */
    public ResponseJson set() throws AjaxOperationFailException {
        String ycjz = HttpRequestParamter.getString("ycjz",true);//异常结账：0-不提醒 1-提醒
        String yczs = HttpRequestParamter.getString("yczs",true);//反结赠送：0-bu 提醒 1-提醒
        String fjtc = HttpRequestParamter.getString("fjtc",true);//反结退菜：0-不提醒 1-提醒
        String fyjzs = HttpRequestParamter.getString("fyjzs",true);//反预结赠送：0不提醒 1-提醒
        String fyjtc = HttpRequestParamter.getString("fyjtc",true);//反预结退菜：0不提醒 1-提醒
        String fjhyh = HttpRequestParamter.getString("fjhyh",true);//反结后优惠：0不提醒,1提醒
        Integer kxgcs = HttpRequestParamter.getInt("kxgcs",0);//会员每日消费次数，0不提醒
        double diczje = HttpRequestParamter.getDouble("diczje",0);//会员单次充值金额,0不提醒
        double dcxfje = HttpRequestParamter.getDouble("dcxfje",0);//会员单次消费金额，0不提醒
        String phones = HttpRequestParamter.getString("phones",true);//提醒手机号，多个/隔开
        String vendor_sn = HttpRequestParamter.getString("vendor_sn",true);//支付参数1
        String vendor_key = HttpRequestParamter.getString("vendor_key",true);//支付参数2
        String appid = HttpRequestParamter.getString("appid",true);//支付参数3
        String servicer_flag = HttpRequestParamter.getString("servicer_flag",true);//收取服务费:0-不收 1-收取
        double seatmoney = HttpRequestParamter.getDouble("seatmoney",0);//茶位费
        if(StringUtil.isEmpty(phones)){
            throw new AjaxOperationFailException("提醒手机号码不能为空!");
        }
        if(StringUtil.isEmpty(vendor_sn)){
            throw new AjaxOperationFailException("vendor_sn不能为空!");
        }
        if(StringUtil.isEmpty(vendor_key)){
            throw new AjaxOperationFailException("vendor_key不能为空!");
        }
        if(StringUtil.isEmpty(appid)){
            throw new AjaxOperationFailException("appid不能为空!");
        }
        Employees user = employeeService.getLoginUser();
        Stores stores = storesMapper.selectByPrimaryKey1(user.getStoreid(),user.getCompanyid());
        if(stores==null){
            throw new AjaxOperationFailException("店铺信息不存在!");
        }
        stores.setYcjz(ycjz);
        stores.setYczs(yczs);
        stores.setFjtc(fjtc);
        stores.setFyjzs(fyjzs);
        stores.setFyjtc(fyjtc);
        stores.setFjhyh(fjhyh);
        stores.setDcxfje(dcxfje);
        stores.setKxgcs(kxgcs);
        stores.setDiczje(diczje);
        stores.setPhones(phones);
        stores.setVendor_key(vendor_key);
        stores.setVendor_sn(vendor_sn);
        stores.setAppid(appid);
        stores.setSeatmoney(seatmoney);
        stores.setServicer_flag(servicer_flag);
        int count = storesMapper.updateByPrimaryKey(stores);
        ResponseJson responseJson = new ResponseJson();
        if(count > 0){
            responseJson.setMsg("设置参数成功!");
        }
        return responseJson;
    }

    /**
     * 店铺参数获取
     * @return
     */
    public ResponseJson get() throws AjaxOperationFailException {
        Employees user = employeeService.getLoginUser();
        Stores stores = storesMapper.selectByPrimaryKey2(user.getStoreid(),user.getCompanyid());
        if(stores == null){
            throw new AjaxOperationFailException("店铺信息不存在!");
        }
        ResponseJson resp = new ResponseJson();
        resp.setData(stores);
        return resp;
    }
}
