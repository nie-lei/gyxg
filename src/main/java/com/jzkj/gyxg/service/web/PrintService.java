package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.entity.PrintClassfy;
import com.jzkj.gyxg.entity.Printers;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.PrintClassfyMapper;
import com.jzkj.gyxg.mapper.PrintersMapper;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PrintService {

    @Autowired
    private PrintersMapper printersMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PrintClassfyMapper printClassfyMapper;

    /**
     * 打印类别新增
     * @return
     */
    public ResponseJson printAdd() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name",true);
        String code = HttpRequestParamter.getString("code",true);
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("分类名称必填!");
        }
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("分类编码必填!");
        }
        PrintClassfy printClassfy = new PrintClassfy();
        printClassfy.setCode(code);
        printClassfy.setName(name);
        printClassfy.setStatus("0");
        printClassfy.setCompanyid(employeeService.getLoginUser().getCompanyid());
        printClassfy.setStoreid(employeeService.getLoginUser().getStoreid());
        if(checkPrintClassfy(printClassfy)){
            throw new AjaxOperationFailException("分类编码或者名称已经存在!");
        }
        int flag = printClassfyMapper.insert(printClassfy);
        ResponseJson resp = new ResponseJson();
        if(flag>0){
            resp.setMsg("新增成功!");
        }
        return resp;
    }

    /**
     * 打印类别修改
     * @return
     */
    public ResponseJson printEdit() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name",true);
        String code = HttpRequestParamter.getString("code",true);
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        if(classifyid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("分类名称必填!");
        }
        if(StringUtil.isEmpty(code)){
            throw new AjaxOperationFailException("分类编码必填!");
        }
        PrintClassfy printClassfy = printClassfyMapper.selectByPrimaryKey(classifyid);
        if(printClassfy!=null){
            printClassfy.setCode(code);
            printClassfy.setName(name);
            printClassfy.setStatus("0");
            printClassfy.setCompanyid(employeeService.getLoginUser().getCompanyid());
            printClassfy.setStoreid(employeeService.getLoginUser().getStoreid());
            if(!isPomissionClassfy(printClassfy)){
                throw new AjaxOperationFailException("没有权限!");
            }
            if(!code.equals(printClassfy.getCode())){
                printClassfy.setCode(code);
                int count = printClassfyMapper.checkPrintClassfybycode(printClassfy);
                if(count > 0){
                    throw new AjaxOperationFailException("分类名称已经存在!");
                }
            }
            if(!name.equals(printClassfy.getName())){
                printClassfy.setName(name);
                int count = printClassfyMapper.checkPrintClassfybyname(printClassfy);
                if(count > 0){
                    throw new AjaxOperationFailException("分类名称已经存在!");
                }
            }
            int flag = printClassfyMapper.updateByPrimaryKey(printClassfy);
            ResponseJson resp = new ResponseJson();
            if(flag>0){
                resp.setMsg("修改成功!");
            }
            return resp;
        }else {
            throw new AjaxOperationFailException("信息不存在!");
        }
    }

    /**
     * 检查打印类别是否重复
     * @return
     */
    public boolean checkPrintClassfy(PrintClassfy printClassfy){
        int count = printClassfyMapper.checkPrintClassfy(printClassfy);
        if(count>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean isPomissionClassfy(PrintClassfy printClassfy){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)printClassfy.getCompanyid()) && ((int)loginuser.getStoreid()==(int)printClassfy.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 打印方式分类列表
     * @return
     */
    public ResponseJson printList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap();
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        PageHelper.startPage(page,size,"classifyid asc");
        List<Map> list = printClassfyMapper.selectPrintList(params);
        ResponseJson resp = new ResponseJson();
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        resp.setData(list);
        resp.setCount(pageinfo.getTotal());
        return resp;
    }

    /**
     * 打印分类下拉
     * @return
     */
    public ResponseJson selectPrintClassfy() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = printClassfyMapper.selectPrintClassfy(params);
        resp.setData(list);
        return resp;
    }


    /**
     * 打印机信息列表
     * @return
     */
    public ResponseJson printerList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap();
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        PageHelper.startPage(page,size,"printerid asc");
        List<Map> list = printersMapper.selectPrinterList(params);
        ResponseJson resp = new ResponseJson();
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        resp.setData(list);
        resp.setCount(pageinfo.getTotal());
        return resp;
    }

    /**
     * 打印机修改
     * @return
     */
    public ResponseJson printerEdit() throws AjaxOperationFailException {
        Integer printerid = HttpRequestParamter.getInt("printerid",0);
        String name = HttpRequestParamter.getString("name",true);
        String printercode = HttpRequestParamter.getString("printercode",true);
        String printerip = HttpRequestParamter.getString("printerip",true);
        String memo = HttpRequestParamter.getString("memo",true);
        Integer num = HttpRequestParamter.getInt("num",1);
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        Integer areaid = HttpRequestParamter.getInt("areaid",0);
        if(printerid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("打印机名称不能为空!");
        }
        if(StringUtil.isEmpty(printercode)){
            throw new AjaxOperationFailException("打印机编号不能为空!");
        }
        if(StringUtil.isEmpty(printerip)){
            throw new AjaxOperationFailException("打印机ip不能为空!");
        }
        if(classifyid==0){
            throw new AjaxOperationFailException("请选择打印类别!");
        }

        Printers printers = printersMapper.selectByPrimaryKey(printerid);
        if(printers == null){
            throw new AjaxOperationFailException("打印机信息不存在!");
        }
        if(areaid!=0){
            printers.setAreaid(areaid);
        }
        printers.setClassifyid(classifyid);
        printers.setMemo(memo);
        printers.setName(name);
        if(!isPomissionPrinter(printers)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(!printercode.equals(printers.getPrintercode())){
            printers.setPrintercode(printercode);
            int count = printersMapper.checkPrinterbycode(printers);
            if(count > 0){
                throw new AjaxOperationFailException("打印机名称重复!");
            }
        }
        if(!name.equals(printers.getName())){
            printers.setName(name);
            int count = printersMapper.checkPrinterbyname(printers);
            if(count > 0){
                throw new AjaxOperationFailException("打印机名称重复!");
            }
        }
        printers.setNum(num);
        printers.setPrinterip(printerip);
        int count  = printersMapper.updateByPrimaryKey(printers);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改成功!");
        }
        return resp;
    }

    /**
     * 打印机新增
     * @return
     */
    public ResponseJson printerAdd() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name",true);
        String printercode = HttpRequestParamter.getString("printercode",true);
        String printerip = HttpRequestParamter.getString("printerip",true);
        String memo = HttpRequestParamter.getString("memo",true);
        Integer num = HttpRequestParamter.getInt("num",1);
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        Integer areaid = HttpRequestParamter.getInt("areaid",0);

        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("打印机名称不能为空!");
        }
        if(StringUtil.isEmpty(printercode)){
            throw new AjaxOperationFailException("打印机编号不能为空!");
        }
        if(StringUtil.isEmpty(printerip)){
            throw new AjaxOperationFailException("打印机ip不能为空!");
        }
        if(classifyid==0){
            throw new AjaxOperationFailException("请选择打印类别!");
        }

        Printers printers = new Printers();
        if(areaid!=0){
            printers.setAreaid(areaid);
        }
        printers.setClassifyid(classifyid);
        printers.setCompanyid(employeeService.getLoginUser().getCompanyid());
        printers.setMemo(memo);
        printers.setName(name);
        printers.setStoreid(employeeService.getLoginUser().getStoreid());
        printers.setStatus("0");
        printers.setPrintercode(printercode);
        printers.setNum(num);
        printers.setPrinterip(printerip);
        if(checkPrinter(printers)){
            throw new AjaxOperationFailException("打印机编号重复!");
        }
        int count  = printersMapper.insert(printers);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("添加成功!");
        }
        return resp;
    }


    public boolean checkPrinter(Printers printers){
        int count = printersMapper.checkPrinter(printers);
        if(count > 0){
            return true;
        }else {
            return false;
        }
    }

    public boolean isPomissionPrinter(Printers printers){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)printers.getCompanyid()) && ((int)loginuser.getStoreid()==(int)printers.getStoreid())){
            return true;
        }else {
            return false;
        }
    }


    /**
     *
     * 打印机删除
     * @return
     */
    public ResponseJson printerDel() throws AjaxOperationFailException {
        Integer printerid = HttpRequestParamter.getInt("printerid",0);
        if(printerid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        Printers printers = printersMapper.selectByPrimaryKey(printerid);
        if(printers == null){
            throw new AjaxOperationFailException("打印机信息不存在!");
        }
        if(!isPomissionPrinter(printers)){
            throw new AjaxOperationFailException("没有权限!");
        }
        int count = printersMapper.deleteByPrimaryKey(printerid);
        ResponseJson resp = new ResponseJson();
        if(count>0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }

    /**
     * 打印机启用禁用
     * @return
     */
    public ResponseJson printerChange() throws AjaxOperationFailException {
        Integer printerid = HttpRequestParamter.getInt("printerid",0);
        String status = HttpRequestParamter.getString("status",true);
        if(printerid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        Printers printers = printersMapper.selectByPrimaryKey(printerid);
        if(printers == null){
            throw new AjaxOperationFailException("打印机信息不存在!");
        }
        if(!isPomissionPrinter(printers)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(StringUtil.isEmpty(status)){
            throw new AjaxOperationFailException("启用禁用参数不能为空!");
        }
        printers.setStatus(status);
        int count = printersMapper.updateByPrimaryKey(printers);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            if("0".equals(status)){
                resp.setMsg("启用成功!");
            }else {
                resp.setMsg("禁用成功!");
            }
        }
        return resp;
    }


    public ResponseJson printDel() throws AjaxOperationFailException {
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        if(classifyid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        PrintClassfy printClassfy = printClassfyMapper.selectByPrimaryKey(classifyid);
        if(printClassfy == null){
            throw new AjaxOperationFailException("打印类别不存在!");
        }
        Integer count = printersMapper.selectByClassfyid(classifyid);
        if(count > 0){
            throw new AjaxOperationFailException("打印类别下面绑定了打印机，请解绑打印机!");
        }
        printClassfyMapper.deleteByPrimaryKey(classifyid);
        return new ResponseJson("删除成功");
    }
}
