package com.jzkj.gyxg.service.web;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.RedisSession;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Employeelog;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.entity.Menu;
import com.jzkj.gyxg.entity.WhiteMac;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.*;
import com.jzkj.gyxg.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeesMapper employeesMapper;

    @Autowired
    private RedisSession redisSession;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private EmployeelogMapper employeelogMapper;

    @Autowired
    private WhiteMacMapper whiteMacMapper;


    /**
     * 登录
     * @return
     */
    public ResponseJson ajaxLogin() throws AjaxOperationFailException {
        String ip;
//        ip = ContextUtil.getRequest().getRemoteAddr();

//        ip = IpUtil.getIpAddr(ContextUtil.getRequest());
//        System.out.println(ip);
//        String mac = MacUtil.getMacAddress(ip).toUpperCase();
//        System.out.println("mac:"+mac);
//        WhiteMac whiteMac = whiteMacMapper.findByMac(mac);
//        if(whiteMac==null) {
//            throw new AjaxOperationFailException("当前机器不允许登录");
//        }
//        if("1".equals(whiteMac.getStatus())){
//            throw new AjaxOperationFailException("当前用户不允许登录，请联系管理员");
//        }

        String phone = HttpRequestParamter.getString("phone");
        String pwd = HttpRequestParamter.getString("pwd");
        String employeeno = HttpRequestParamter.getString("employeeno",true,"0");
        ResponseJson responseJson = new ResponseJson();
        Employees user = null;
        String token;
        Map data = new HashMap();
        if (StringUtil.isEmpty(phone)) {
            throw new AjaxOperationFailException("手机号不能同时为空!");
        }
        if (StringUtil.isEmpty(employeeno)) {
            throw new AjaxOperationFailException("工号不能同时为空!");
        }

        if (StringUtil.isEmpty(pwd)) {
            throw new AjaxOperationFailException("密码不能为空!");
        }
        user = this.selectUserByPhoneOrNo(phone, employeeno);
        if(user != null){
            if(user.getPwd() != null){
                if (StringUtil.md5(pwd).equals(user.getPwd())) {
                    if (user.getStatus().equals("0")) {
                        token = HexString.encode(StringUtil.getUUid());
                        //存入redis
                        redisSession.setValue(token, user, 5, TimeUnit.HOURS);
                        //查询统计页面
                        responseJson.setMsg("登录成功!");
                    } else {
                        throw new AjaxOperationFailException("账号异常，请联系管理员");
                    }
                } else {
                    throw new AjaxOperationFailException("密码错误");
                }
            }else {
                throw new AjaxOperationFailException("密码信息不存在,请联系管理员!");
            }
        }else {
            throw new AjaxOperationFailException("登录的用户不存在!");
        }
        data.put("token",token);
        data.put("name",user.getName());
        responseJson.setData(data);
        return responseJson;
    }

    /**
     * 退出登录
     * @return
     */
    public ResponseJson ajaxLogout(HttpServletRequest req) {
        String token = req.getHeader("token");
//        String token = HttpRequestParamter.getString("token");
        redisSession.setValue(token,null,2,TimeUnit.SECONDS);
        return new ResponseJson();
    }

    /**
     * 查询权限
     * @param
     * @param requestUrl
     * @return
     */
    public boolean checkAuth(Employees user, String requestUrl, String ip) {
        boolean flag = false;
        List<Menu> menu = menuMapper.selectByUrl(requestUrl);
        if(menu!=null && menu.size() > 0 ){
            Integer count = roleMenuMapper.selectAuthUrl(menu.get(0).getMenuid(),user.getRoleid());
            if(count>0){
                //记录日志
                Employeelog log = new Employeelog();
                log.setActiontime(new Date());
                log.setIp(ip);
                log.setEmployeeid(user.getEmployeeid());
                log.setMemo(menu.get(0).getName());
                employeelogMapper.insert(log);
                flag=true;
            }
            return flag;
        }else {
            return flag;
        }
    }


    /**
     * 通过手机号工号查询用户
     * @param phone
     * @param employeeno
     * @return
     */
    private Employees selectUserByPhoneOrNo(String phone, String employeeno) {
        return employeesMapper.selectUserByPhoneOrNo(phone,employeeno);
    }

    /**
     * 获取当前登录用户
     * @param
     * @return
     */
    public Employees getLoginUser() {
        if(StringUtil.isNull(ContextUtil.getRequest().getHeader("token"))){
            return null;
        }
//        Employees user = (Employees) redisSession.getValue(HttpRequestParamter.getString("token"));
        Employees user = (Employees) redisSession.getValue(ContextUtil.getRequest().getHeader("token"));
        return user;
    }

    /**
     * 员工列表
     * @return
     */
    public ResponseJson list() {
        String phone = HttpRequestParamter.getString("phone");
        String name = HttpRequestParamter.getString("name");
        String employeeno = HttpRequestParamter.getString("employeeno",true,"0");
        String starttime = HttpRequestParamter.getString("starttime");
        String endtime = HttpRequestParamter.getString("endtime");
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap<> ();
        if(!StringUtil.isEmpty(phone)){
            params.put("phone",phone);
        }
        if(!StringUtil.isEmpty(name)){
            params.put("name",name);
        }
        if(!StringUtil.isEmpty(employeeno)){
            params.put("employeeno",employeeno);
        }
        if(!StringUtil.isEmpty(starttime) && !StringUtil.isEmpty(endtime)){
            params.put("starttime",starttime);
            params.put("endtime",endtime);
        }
        Employees loginuser = getLoginUser();
        params.put("companyid",loginuser.getCompanyid());
        params.put("storeid",loginuser.getStoreid());
        PageHelper.startPage(page,size," opentime asc");
        List<Map> list = employeesMapper.selectList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 新增员工信息
     * @return
     */
    public ResponseJson add() throws AjaxOperationFailException {
        String phone = HttpRequestParamter.getString("phone");
        String name = HttpRequestParamter.getString("name");
        String employeeno = HttpRequestParamter.getString("employeeno");
        Integer departmentid = HttpRequestParamter.getInt("departmentid",0);
        Integer roleid = HttpRequestParamter.getInt("roleid",0);
        Integer positionid = HttpRequestParamter.getInt("positionid",0);
        if(StringUtil.isEmpty(phone)){
            throw new AjaxOperationFailException("电话号码为空或者!");
        }else {
            if(!StringUtil.isPhone(phone)){
                throw new AjaxOperationFailException("电话号码错误!");
            }
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("员工姓名不能为空!");
        }
        if(StringUtil.isEmpty(employeeno)){
            throw new AjaxOperationFailException("员工编号不能为空!");
        }
        if(departmentid==0){
            throw new AjaxOperationFailException("请选择部门!");
        }
        if(roleid==0){
            throw new AjaxOperationFailException("请选择角色!");
        }
        if(positionid==0){
            throw new AjaxOperationFailException("请选择职位!");
        }
        Employees employees = new Employees();
        employees.setCompanyid(getLoginUser().getCompanyid());
        employees.setStoreid(getLoginUser().getStoreid());
        employees.setDepartmentid(departmentid);
        employees.setName(name);
        employees.setPhone(phone);
        employees.setEmployeeno(employeeno);
        employees.setDiscountamount(0.0);
        employees.setOpentime(new Date());
        employees.setPositionid(positionid);
        employees.setRoleid(roleid);
        employees.setStatus("0");
        employees.setPwd(StringUtil.md5(phone.substring(5)));
        if(isExistEmployee(employees)){
            throw new AjaxOperationFailException("员工工号存在!");
        }
        int flag = employeesMapper.insert(employees);
        ResponseJson responseJson = new ResponseJson();
        if(flag>0){
            responseJson.setMsg("添加成功!");
        }
        return responseJson;
    }


    /**
     * 检查是否存在
     * @param employees
     * @return
     */
    public boolean isExistEmployee(Employees employees){
        int count = employeesMapper.isExistEmployee(employees);
        if(count>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 修改员工信息
     * @return
     */
    public ResponseJson edit() throws AjaxOperationFailException {
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);
        String phone = HttpRequestParamter.getString("phone");
        String name = HttpRequestParamter.getString("name");
        String employeeno = HttpRequestParamter.getString("employeeno");
        Integer departmentid = HttpRequestParamter.getInt("departmentid",0);
        Integer roleid = HttpRequestParamter.getInt("roleid",0);
        Integer positionid = HttpRequestParamter.getInt("positionid",0);
        if(employeeid==0){
            throw new AjaxOperationFailException("缺失参数员工id!");
        }
        if(StringUtil.isEmpty(phone)){
            throw new AjaxOperationFailException("电话号码为空或者!");
        }else {
            if(!StringUtil.isPhone(phone)){
                throw new AjaxOperationFailException("电话号码错误!");
            }
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("员工姓名不能为空!");
        }
        if(StringUtil.isEmpty(employeeno)){
            throw new AjaxOperationFailException("员工编号不能为空!");
        }
        if(departmentid==0){
            throw new AjaxOperationFailException("请选择部门!");
        }
        if(roleid==0){
            throw new AjaxOperationFailException("请选择角色!");
        }
        if(positionid==0){
            throw new AjaxOperationFailException("请选择职位!");
        }
        Employees employees = employeesMapper.selectByPrimaryKey(employeeid);
        if(employees==null){
            throw new AjaxOperationFailException("修改的员工信息不存在!");
        }
        employees.setDepartmentid(departmentid);
        employees.setName(name);
        employees.setOpentime(new Date());
        employees.setPositionid(positionid);
        employees.setRoleid(roleid);
        employees.setStatus("0");
        employees.setPwd(StringUtil.md5(phone.substring(5)));
        if(!isPromission(employees)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(!employeeno.equals(employees.getEmployeeno())){//修改了编号
            employees.setEmployeeno(employeeno);
            int count = employeesMapper.isExistEmployeebyno(employees);
            if(count > 0){
                throw new AjaxOperationFailException("工号存在!");
            }
        }
//        if(!phone.equals(employees.getPhone())){
//            employees.setPhone(phone);
//            int count = employeesMapper.isExistEmployeebyphone(employees);
//            if(count > 0){
//                throw new AjaxOperationFailException("电话已经存在!");
//            }
//        }
        int flag = employeesMapper.updateByPrimaryKey(employees);
        ResponseJson responseJson = new ResponseJson();
        if(flag>0){
            responseJson.setMsg("修改成功!");
        }
        return responseJson;
    }

    /**
     * 校验登录的用户操作的员工是否属于本公司
     * @param employees
     * @return
     */
    public boolean isPromission(Employees employees){
        Employees loginUser = getLoginUser();
        if(((int)loginUser.getCompanyid()==(int)employees.getCompanyid()) && ((int)loginUser.getStoreid()==(int)employees.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 删除员工信息
     * @return
     */
    public ResponseJson del() throws AjaxOperationFailException {
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);
        if(employeeid==0){
            throw new AjaxOperationFailException("缺失参数员工id!");
        }
        Employees employees = employeesMapper.selectByPrimaryKey(employeeid);
        if(employees==null){
            throw new AjaxOperationFailException("想要删除员工信息不存在!");
        }
        if(!isPromission(employees)){
            throw new AjaxOperationFailException("没有权限!");
        }else {
            employeesMapper.deleteByPrimaryKey(employeeid);
        }
        ResponseJson resp = new ResponseJson();
        resp.setMsg("删除成功!");
        return resp;
    }


    public ResponseJson selectEmp() {
        Map params = new HashMap();
        Employees employees = getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = employeesMapper.selectEmp(params);
        resp.setData(list);
        return resp;
    }

    /**
     * 密码修改
     * @return
     */
    public ResponseJson updatePassword(HttpServletRequest req) throws AjaxOperationFailException {
        String oldpassword = HttpRequestParamter.getString("oldpassword",true);
        String newpassword = HttpRequestParamter.getString("newpassword",true);
        if(StringUtil.isEmpty(oldpassword)){
            throw new AjaxOperationFailException("旧密码不能为空");
        }
        if(StringUtil.isEmpty(newpassword)){
            throw new AjaxOperationFailException("新密码不能为空");
        }
        if(oldpassword.equals(newpassword)){
            throw new AjaxOperationFailException("新密码不能和旧密码一致");
        }
        Employees loginuser = getLoginUser();
        if(!StringUtil.md5(oldpassword).equals(loginuser.getPwd())){
            throw new AjaxOperationFailException("旧密码验证不通过!");
        }
        if(newpassword.length()<6){
            throw new AjaxOperationFailException("设置新密码至少为6位!");
        }
//        String reg = "^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";
//        if(!newpassword.matches(reg)){
//            throw new AjaxOperationFailException("密码必须包含字母和数字!");
//        }
        loginuser.setPwd(StringUtil.md5(newpassword));
        employeesMapper.updateByPrimaryKey(loginuser);
        String token = req.getHeader("token");
        redisSession.setValue(token,null,1,TimeUnit.SECONDS);
        return new ResponseJson();
    }


    public ResponseJson menu() {
        List<Menu> list = menuMapper.selectByRoleid(getLoginUser().getRoleid());
        //创建一个父菜单集合
        List<Map> f_list = new ArrayList<Map>();
        for(int i =0;i<list.size();i++){
            Map f_map = (Map) JSON.toJSON(list.get(i));
            if(f_map.get("level").toString().equals("1")){
                f_list.add(f_map);
            }
        }
        for(Map f_map : f_list){
            List c_list = new ArrayList();
            for(int i=0;i<list.size();i++){
                Map c_map = (Map) JSON.toJSON(list.get(i));
                if(c_map.get("parentid").toString().equals(f_map.get("menuid").toString())){
                    c_list.add(c_map);
                }
                f_map.put("c_list", c_list);
            }
        }
        return new ResponseJson(f_list);
    }

    /**
     * 首页数据展示
     * @return
     */
    public ResponseJson home() {
        Map param = new HashMap();
        param.put("companyid",getLoginUser().getCompanyid());
        param.put("storeid",getLoginUser().getStoreid());
        Map resultMap = new HashMap();
        //左上
        Map todayMap = employeesMapper.selectTodayData(param);
        List today = new ArrayList();
        today.add(todayMap.get("pcount"));
        today.add(todayMap.get("tcount"));
        today.add(todayMap.get("mcount"));
        Map yesterdayMap = employeesMapper.selectYesterdayData(param);
        List yesterday = new ArrayList();
        yesterday.add(yesterdayMap.get("pcount"));
        yesterday.add(yesterdayMap.get("tcount"));
        yesterday.add(yesterdayMap.get("mcount"));
        resultMap.put("todaylist",today);
        resultMap.put("yesterdaylist",yesterday);
        //右上
        List<Map> list = employeesMapper.selectDishesList(param);
        resultMap.put("topright",list);
        //下方
        List<Map> listbelow = employeesMapper.selectGoodsStock(param);
        resultMap.put("below",listbelow);
        return new ResponseJson(resultMap);
    }

    /**
     * 超级管理员修改员工密码
     * @return
     */
    public ResponseJson editPasswd() throws AjaxOperationFailException {
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);
        String passwd = HttpRequestParamter.getString("passwd",true);
        if(employeeid==0){
            throw new AjaxOperationFailException("缺失参数员工id!");
        }
        Employees employees = employeesMapper.selectByPrimaryKey(employeeid);
        if(employees==null){
            throw new AjaxOperationFailException("员工信息不存在!");
        }
        Employees loginuser = getLoginUser();
        if(loginuser.getEmployeeid() == employeeid){
            throw new AjaxOperationFailException("修改个人密码请到头部修改密码修改！");
        }
        if(!isPromission(employees)){
            throw new AjaxOperationFailException("没有权限!");
        }else {
            employees.setPwd(StringUtil.md5(passwd));
            employeesMapper.updateByPrimaryKey(employees);
        }
        ResponseJson resp = new ResponseJson();
        return resp;
    }


    public ResponseJson editDiscountamount() throws AjaxOperationFailException {
        Integer employeeid = HttpRequestParamter.getInt("employeeid",0);
        Double discountamount = HttpRequestParamter.getDouble("discountamount",0.00);
        if(employeeid==0){
            throw new AjaxOperationFailException("缺失参数员工id!");
        }
        Employees employees = employeesMapper.selectByPrimaryKey(employeeid);
        if(employees==null){
            throw new AjaxOperationFailException("员工信息不存在!");
        }
        if(!isPromission(employees)){
            throw new AjaxOperationFailException("没有权限!");
        }else {
            employees.setDiscountamount(discountamount);
            employeesMapper.updateByPrimaryKey(employees);
        }
        ResponseJson resp = new ResponseJson();
        return resp;
    }

    /**
     * 根据员工id获取与员工信息
     * @return
     */
    public ResponseJson selectEmpInfoById() throws AjaxOperationFailException {
        Integer employeeid = getLoginUser().getEmployeeid();
        if (employeeid == null || employeeid == 0) {
            throw new AjaxOperationFailException("该账号未登录");
        }
        Employees employees = employeesMapper.selectByPrimaryKey(employeeid);
        if (employees == null) {
            throw new AjaxOperationFailException("该账号信息不存在");
        }
        return new ResponseJson("获取成功", employees.getDiscountamount());
    }
}
