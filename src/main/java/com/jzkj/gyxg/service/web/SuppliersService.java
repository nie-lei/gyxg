package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Discount;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.entity.Suppliers;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.DiscountMapper;
import com.jzkj.gyxg.mapper.SuppliersMapper;
import com.jzkj.gyxg.mapper.WarehousesMapper;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SuppliersService {
    @Autowired
    private SuppliersMapper suppliersMapper;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private WarehousesMapper warehousesMapper;
    @Autowired
    private DiscountMapper discountMapper;
    /**
     * 供应商新增
     * @return
     */
    public ResponseJson add() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name",true);
        String legalperson = HttpRequestParamter.getString("legalperson",true);
        String contact = HttpRequestParamter.getString("contact",true);
        String phone = HttpRequestParamter.getString("phone",true);
        String fax = HttpRequestParamter.getString("fax",true);
        String address = HttpRequestParamter.getString("address",true);
        String brief = HttpRequestParamter.getString("brief",true);
        String mainproducts = HttpRequestParamter.getString("mainproducts",true);
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("名称不能为空!");
        }
        if(StringUtil.isEmpty(contact)){
            throw new AjaxOperationFailException("联系人不能为空!");
        }
        if(StringUtil.isEmpty(phone)){
            throw new AjaxOperationFailException("联系电话不能为空!");
        }
        if(StringUtil.isEmpty(address)){
            throw new AjaxOperationFailException("地址不能为空!");
        }

        Suppliers suppliers = new Suppliers();
        suppliers.setCompanyid(employeeService.getLoginUser().getCompanyid());
        suppliers.setStoreid(employeeService.getLoginUser().getStoreid());
        suppliers.setIntime(new Date());
        suppliers.setStatus("0");
        suppliers.setName(name);
        suppliers.setLegalperson(legalperson);
        suppliers.setContact(contact);
        suppliers.setPhone(phone);
        suppliers.setFax(fax);
        suppliers.setAddress(address);
        suppliers.setBrief(brief);
        suppliers.setMainproducts(mainproducts);
        suppliers.setEmployeeid(employeeService.getLoginUser().getEmployeeid());
        if(checkSuppliers(suppliers)){
            throw new AjaxOperationFailException("供应商已经存在!");
        }
        int i = suppliersMapper.insert(suppliers);
        ResponseJson resp = new ResponseJson();
        if(i>0){
            resp.setMsg("新增成功!");
        }
        return resp;
    }


    private boolean checkSuppliers(Suppliers suppliers) {
        int count = suppliersMapper.checkSuppliers(suppliers);
        if(count > 0){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 供应商信息修改
     * @return
     */
    public ResponseJson edit() throws AjaxOperationFailException {
        Integer supplierid = HttpRequestParamter.getInt("supplierid",0);
        String name = HttpRequestParamter.getString("name",true);
        String legalperson = HttpRequestParamter.getString("legalperson",true);
        String contact = HttpRequestParamter.getString("contact",true);
        String phone = HttpRequestParamter.getString("phone",true);
        String fax = HttpRequestParamter.getString("fax",true);
        String address = HttpRequestParamter.getString("address",true);
        String brief = HttpRequestParamter.getString("brief",true);
        String mainproducts = HttpRequestParamter.getString("mainproducts",true);
        if(supplierid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        Suppliers suppliers = suppliersMapper.selectByPrimaryKey(supplierid);
        if(suppliers==null){
            throw new AjaxOperationFailException("供应商不存在!");
        }
        if(!isPomissionSuppliers(suppliers)){
            throw new AjaxOperationFailException("没有权限!");
        }

        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("名称不能为空!");
        }

        if(StringUtil.isEmpty(contact)){
            throw new AjaxOperationFailException("联系人不能为空!");
        }
        if(StringUtil.isEmpty(phone)){
            throw new AjaxOperationFailException("联系电话不能为空!");
        }
        if(StringUtil.isEmpty(address)){
            throw new AjaxOperationFailException("地址不能为空!");
        }

        suppliers.setCompanyid(employeeService.getLoginUser().getCompanyid());
        suppliers.setStoreid(employeeService.getLoginUser().getStoreid());
        suppliers.setStatus("0");
        suppliers.setLegalperson(legalperson);
        suppliers.setContact(contact);
        suppliers.setPhone(phone);
        suppliers.setFax(fax);
        suppliers.setAddress(address);
        suppliers.setBrief(brief);
        suppliers.setMainproducts(mainproducts);
        if(!name.equals(suppliers.getName())){
            suppliers.setName(name);
            if(checkSuppliers(suppliers)){
                throw new AjaxOperationFailException("供应商已经存在!");
            }
        }
        int i = suppliersMapper.updateByPrimaryKey(suppliers);
        ResponseJson resp = new ResponseJson();
        if(i>0){
            resp.setMsg("修改成功!");
        }
        return resp;
    }

    public boolean isPomissionSuppliers(Suppliers suppliers){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)suppliers.getCompanyid()) && ((int)loginuser.getStoreid()==(int)suppliers.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 供应商删除
     * @return
     */
    public ResponseJson del() throws AjaxOperationFailException {
        Integer supplierid = HttpRequestParamter.getInt("supplierid",0);
        if(supplierid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        Suppliers suppliers = suppliersMapper.selectByPrimaryKey(supplierid);
        ResponseJson resp = new ResponseJson();
        if(suppliers != null){
            if(!isPomissionSuppliers(suppliers)){
                throw new AjaxOperationFailException("没有权限!");
            }
            suppliers.setStatus("1");
            int count = suppliersMapper.updateByPrimaryKey(suppliers);
            if(count > 0){
                resp.setMsg("删除成功!");
            }
        }else {
            throw new AjaxOperationFailException("信息不存在!");
        }
        return resp;
    }

    /**
     * 供应商列表
     * @return
     */
    public ResponseJson list() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap();
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        PageHelper.startPage(page,size,"supplierid asc");
        List<Map> list = suppliersMapper.selectList(params);
        ResponseJson resp = new ResponseJson();
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        resp.setData(list);
        resp.setCount(pageinfo.getTotal());
        return resp;
    }


    public ResponseJson selectSupplier() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = suppliersMapper.select(params);
        resp.setData(list);
        return resp;
    }


    public ResponseJson selectwarehouse() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = warehousesMapper.select(params);
        resp.setData(list);
        return resp;
    }


    public ResponseJson selectdiscount() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Discount> list = discountMapper.select(params);
        resp.setData(list);
        return resp;
    }
}
