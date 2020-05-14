package com.jzkj.gyxg.interceptor;


import com.alibaba.fastjson.JSON;
import com.jzkj.gyxg.common.RedisSession;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.service.web.EmployeeService;
import com.jzkj.gyxg.util.HttpUtil;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class HtmlLoginCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RedisSession redisSession;

    private List<String> executorList = new ArrayList<>();   //存储需登陆不需要权限接口地址
    {
        executorList.add("/api/employees/updatepassword");//密码修改
        executorList.add("/api/employees/home");//首页数据
        executorList.add("/api/employees/menu");//首页数据
        executorList.add("/api/common/selectwarehouse");//仓库下拉选择
        executorList.add("/api/common/selectsupplier");//供应商下拉选择
        executorList.add("/api/common/selectgoods");//原料下拉专属接口
        executorList.add("/api/common/selecmaterialclassifys2");//原料分类下拉
        executorList.add("/api/common/selecmaterialclassifys");//原料分类父级下拉
        executorList.add("/api/common/selectattachs");//获取原因，单位
        executorList.add("/api/common/imgupload");//图片上传
        executorList.add("/api/common/selectemp");//负责人下拉
        executorList.add("/api/common/selectareas");//区域信息下拉
        executorList.add("/api/common/selectroles");//角色下拉
        executorList.add("/api/common/selectdishesmethods");//烹饪方式下拉列表
        executorList.add("/api/common/selectprintclassfy");//打印分类下拉列表
        executorList.add("/api/common/selectposition");//职位下拉列表
        executorList.add("/api/common/selectdept");//部门信息下拉列表
        executorList.add("/api/common/selectgoods2");//原料下拉专属接口...
        executorList.add("/api/common/selectdishesclassfy");//菜品分类下拉列表
        executorList.add("/api/goods/selectbygoodsid");//通过原料id获取原料单位价格
        executorList.add("/api/goods/findkucunBygoodsId");//通过原料id获取库存原料单位价格
        executorList.add("/api/order/arealist");//点餐台头部
        executorList.add("/api/order/dishesclassfy2");//点菜头部分类
        executorList.add("/api/order/disheslist");//根据分类获取菜用作点菜
        executorList.add("/api/order/selectdishesbypinyin");//根据拼音查询
        executorList.add("/api/paytype/selectallquan");//动态获取支付方式
        executorList.add("/api/paytype/selectpay");
        executorList.add("/api/common/selectdiscount");
        executorList.add("/api/dishes/dishesPageEdit");//修改显示菜谱
        executorList.add("/api/order/getOrderDiscountAmount");//获取折扣后金额

        executorList.add("/api/report/tuicaimingxi");//退菜明细报表
        executorList.add("/api/report/jiushuidan");//酒水单报表报表
        executorList.add("/api/report/zengcaimingxi");//赠菜明细报表
        executorList.add("/api/report/caipinxiaoshoutongji");//菜品销售统计表
        executorList.add("/api/report/qiandan");//签单汇总报表
        executorList.add("/api/report/zhangdanfukuanmingxi");//结算方式明细报表
        executorList.add("/api/report/jiesuanfangshihuizongchaxun");//结算方式汇总报表
        executorList.add("/api/report/yingyehuizong");//营业汇总报表
        executorList.add("/api/report/kaorderdetail");//优惠卡往来账目明细

    }

    private List<String> executorListNotLogin = new ArrayList<>();   //存储不需验证登陆的接口地址
    {
        executorListNotLogin.add("/api/goods/aa");
        executorListNotLogin.add("/");
//        executorListNotLogin.add("/api/report/exportExceltuicaimingxi");//退菜明细导出
        executorListNotLogin.add("/api/employees/login");//登录
        executorListNotLogin.add("/api/employees/logout");//退出登录
    }

    public boolean isContains(String url) {
        boolean flag = false;
        for (String str: executorList) {
            if(str.contains(url)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public boolean isContainsNotLogin(String url) {
        boolean flag = false;
        for (String str: executorListNotLogin) {
            if(str.contains(url)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object o) throws Exception {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        resp.setHeader("Access-Control-Allow-Headers", "token");
        String requestUrl = req.getRequestURI();
        String ip = HttpUtil.getClientIp();
        if (employeeService == null) {//解决service为null无法注入问题
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getServletContext());
            employeeService = (EmployeeService) factory.getBean("employeeService");
        }
        if (redisSession == null) {//解决service为null无法注入问题
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getServletContext());
            redisSession = (RedisSession) factory.getBean("redisSession");
        }
        //不需要登录就放行的请求
        if(isContainsNotLogin(requestUrl)) {
            return true;
        }
        String token = req.getHeader("token");
        if(StringUtil.isNull(token)){//未登录
            resp.sendError(401);
            writeJsonOutput(resp,1);
            return false;
        }else {
            Employees user = (Employees) employeeService.getLoginUser();
            if(user==null) {//token过期
                resp.sendError(403);
                writeJsonOutput(resp,0);
                return false;
            } else {
                //包含以内的url直接放行
                if(isContains(requestUrl)) {
                    return true;
                }else {
//                    return true;
                    boolean flag = employeeService.checkAuth(user,requestUrl,ip);
                    if(flag){
                        //存入redis
                        redisSession.setValue(token, user, 5, TimeUnit.HOURS);
                        return true;
                    }else {
                        writeJsonOutput(resp,2);
                        return false;
                    }
                }
            }
        }
    }


    private void writeJsonOutput(HttpServletResponse resp, int type) throws IOException {
        ResponseJson responseResult = new ResponseJson();
        if (type == 0) {
            responseResult.setCode(101);
            responseResult.setMsg("请登录后再操作！");
        }else if (type == 1) {
            responseResult.setCode(103);
            responseResult.setMsg("缺失参数token！");
        }else {
            responseResult.setCode(102);
            responseResult.setMsg("对不起，您没有权限进行此操作！");
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(JSON.toJSONString(responseResult));
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {


    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
