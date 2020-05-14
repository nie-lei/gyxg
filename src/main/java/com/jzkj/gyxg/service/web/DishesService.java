package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.*;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.*;
import com.jzkj.gyxg.util.Pinyin;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class DishesService {

    @Autowired
    private DishesClassfysMapper dishesClassfysMapper;

    @Autowired
    private MethodsMapper methodsMapper;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DishesMapper dishesMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private DishePagesMapper dishePagesMapper;

    /**
     * 菜品列表
     * @return
     */
    public ResponseJson dishesList() {
        String name = HttpRequestParamter.getString("name",true);
        Integer dishesid = HttpRequestParamter.getInt("classifyid",0);
        String isgoods = HttpRequestParamter.getString("isgoods",true);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap();
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        if(!StringUtil.isEmpty(name)){
            params.put("name",name);
        }
        if(!StringUtil.isEmpty(isgoods)){
            params.put("isgoods",isgoods);
        }
        if(dishesid!=0){
            params.put("dishesid",dishesid);
        }
        PageHelper.startPage(page,size,"disheid asc");
        List<Map> list = dishesMapper.selectList(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        for (Map dmap : list){
            List<Methods> list1 = methodsMapper.selectAllByMid((Integer) dmap.get("disheid"));
            String mstr = "";
            if(list1!=null){
                for (Methods m : list1){
                    mstr+=m.getName()+",";
                }
            }
            dmap.put("mstr",mstr);
        }
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }


    /**
     * 烹饪方法列表
     * @return
     */
    public ResponseJson methosdList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String name = HttpRequestParamter.getString("name",true);
        Map params = new HashMap();
        if(!StringUtil.isEmpty(name)){
            params.put("name",name);
        }
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        PageHelper.startPage(page,size,"methodid asc");
        List<Map> list = methodsMapper.selectMethodsList(params);
        ResponseJson resp = new ResponseJson();
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        resp.setData(list);
        resp.setCount(pageinfo.getTotal());
        return resp;
    }

    /**
     * 新增烹饪方式
     * @return
     */
    public ResponseJson methodsAdd() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name",true);
        Integer dishesid = HttpRequestParamter.getInt("dishesid",0);
        double attachmoney = HttpRequestParamter.getDouble("attachmoney",0.00);
        double attachratio = HttpRequestParamter.getDouble("attachratio",0.00);
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("烹饪方式必填!");
        }
        if(dishesid == 0){
            throw new AjaxOperationFailException("请选择菜品分类!");
        }
        Methods methods = new Methods();
        methods.setName(name);
        methods.setStatus("0");
        methods.setDishesid(dishesid);
        methods.setStoreid(employeeService.getLoginUser().getStoreid());
        methods.setCompanyid(employeeService.getLoginUser().getCompanyid());
        methods.setAttachmoney(attachmoney);
        methods.setAttachratio(attachratio);
        if(checkMethods(methods)){
            throw new AjaxOperationFailException("烹饪方式已经存在!");
        }
        int flag = methodsMapper.insert(methods);
        ResponseJson resp = new ResponseJson();
        if(flag>0){
            resp.setMsg("添加烹饪方式成功!");
        }
        return resp;
    }


    /**
     * 校验烹饪方式是否存在
     * @return
     */
    public boolean checkMethods(Methods methods){
        int count = methodsMapper.checkMethods(methods);
        if(count>0){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 检查是否修改的烹饪方式是否属于本公司
     * @param methods
     * @return
     */
    public boolean isPomission(Methods methods){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)methods.getCompanyid()) && ((int)loginuser.getStoreid()==(int)methods.getStoreid())){
            return true;
        }else {
            return false;
        }
    }



    /**
     * 修改烹饪方式
     * @return
     */
    public ResponseJson methodsEdit() throws AjaxOperationFailException {
        Integer methodid = HttpRequestParamter.getInt("methodid",0);
        String name = HttpRequestParamter.getString("name",true);
        Integer dishesid = HttpRequestParamter.getInt("dishesid",0);
        double attachmoney = HttpRequestParamter.getDouble("attachmoney",0.00);
        double attachratio = HttpRequestParamter.getDouble("attachratio",0.00);
        if(methodid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        if(dishesid == 0){
            throw new AjaxOperationFailException("请选择菜品分类!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("烹饪方式必填!");
        }
        Methods methods = methodsMapper.selectByPrimaryKey(methodid);
        if(methods!=null) {
            methods.setAttachmoney(attachmoney);
            methods.setAttachratio(attachratio);
            methods.setDishesid(dishesid);
            if(!isPomission(methods)){
                throw new AjaxOperationFailException("没有权限!");
            }
            if(name.equals(methods.getName())){

            }else {
                methods.setName(name);
                if(checkMethods(methods)){
                    throw new AjaxOperationFailException("烹饪方式已经存在!");
                }
            }
            int flag = methodsMapper.updateByPrimaryKey(methods);
            ResponseJson resp = new ResponseJson();
            if(flag>0){
                resp.setMsg("修改烹饪方式成功!");
            }
            return resp;
        }else {
            throw new AjaxOperationFailException("信息不存在!");
        }
    }



    /**
     * 菜品分类列表
     * @return
     */
    public ResponseJson dishesClassfyList() {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap();
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        PageHelper.startPage(page,size,"classifyid asc");
        List<Map> list = dishesClassfysMapper.selectDishesClassfyList(params);
        ResponseJson resp = new ResponseJson();
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        resp.setData(list);
        resp.setCount(pageinfo.getTotal());
        return resp;
    }


    /**
     * 菜品分类新增
     * @return
     */
    public ResponseJson dishesClassfyAdd() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name",true);
        Integer printid = HttpRequestParamter.getInt("printid",0);
        Integer parentid = HttpRequestParamter.getInt("parentid",0);
        if(printid==0){
            throw new AjaxOperationFailException("打印分类必选!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("分类名称必填!");
        }
        DishesClassfys dishesClassfys = new DishesClassfys();

        if(parentid!=0){
            DishesClassfys parent = dishesClassfysMapper.selectByPrimaryKey(parentid);
            if(parent==null){
                throw new AjaxOperationFailException("父级信息不存在!");
            }
            dishesClassfys.setParentid(parentid);
            dishesClassfys.setLevel((Integer.valueOf(parent.getLevel())+1)+"");
        }else {
            dishesClassfys.setParentid(0);
            dishesClassfys.setLevel("1");
        }
        dishesClassfys.setName(name);
        dishesClassfys.setPrintid(printid);
        dishesClassfys.setIsShow("0");
        dishesClassfys.setStatus("0");
        dishesClassfys.setCompanyid(employeeService.getLoginUser().getCompanyid());
        dishesClassfys.setStoreid(employeeService.getLoginUser().getStoreid());

        if(checkDishesClassfy(dishesClassfys)){
            throw new AjaxOperationFailException("分类名称已经存在!");
        }
        int count = dishesClassfysMapper.insert(dishesClassfys);
        ResponseJson resp = new ResponseJson();
        if(count>0){
            resp.setMsg("新增成功!");
        }
        return resp;
    }

    /**
     * 校验菜品分类是否存在
     * @param dishesClassfys
     * @return
     */
    public boolean checkDishesClassfy(DishesClassfys dishesClassfys){
        int count = dishesClassfysMapper.checkDishesClassfy(dishesClassfys);
        if(count>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean isPomissionDishesClassfy(DishesClassfys dishesClassfys){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)dishesClassfys.getCompanyid()) && ((int)loginuser.getStoreid()==(int)dishesClassfys.getStoreid())){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 菜品分类修改
     * @return
     */
    public ResponseJson dishesclassfyEdit() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name",true);
        Integer printid = HttpRequestParamter.getInt("printid",0);
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        Integer parentid = HttpRequestParamter.getInt("parentid",0);
        if(classifyid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        if(printid==0){
            throw new AjaxOperationFailException("打印分类必选!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("分类名称必填!");
        }

        DishesClassfys dishesClassfys = dishesClassfysMapper.selectByPrimaryKey(classifyid);
        if(dishesClassfys != null){
            dishesClassfys.setPrintid(printid);
            if(!isPomissionDishesClassfy(dishesClassfys)){
                throw new AjaxOperationFailException("没有权限!");
            }
            if(!name.equals(dishesClassfys.getName())){
                dishesClassfys.setName(name);
                if(checkDishesClassfy(dishesClassfys)){
                    throw new AjaxOperationFailException("分类名称已经存在!");
                }
            }
            if(parentid!=0){
                DishesClassfys parent = dishesClassfysMapper.selectByPrimaryKey(parentid);
                if(parent==null){
                    throw new AjaxOperationFailException("父级信息不存在!");
                }
                dishesClassfys.setParentid(parentid);
                dishesClassfys.setLevel((Integer.valueOf(parent.getLevel())+1)+"");
            }else {
                dishesClassfys.setParentid(0);
                dishesClassfys.setLevel("1");
            }

            int count = dishesClassfysMapper.updateByPrimaryKey(dishesClassfys);
            ResponseJson resp = new ResponseJson();
            if(count>0){
                resp.setMsg("修改成功!");
            }
            return resp;
        }else {
            throw new AjaxOperationFailException("信息不存在!");
        }

    }

    /**
     * 菜品分类下拉
     * @return
     */
    public ResponseJson selectDishesClassfy() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = dishesClassfysMapper.selectDishesClassfy(params);
        for (Map p : list) {
            params.put("pid",p.get("value"));
            List<Map> list2 = dishesClassfysMapper.selectDishesClassfy2(params);
            if(list2!=null && list2.size()>0){
                p.put("children",list2);
            }
        }
        resp.setData(list);
        return resp;
    }

    /**
     * 烹饪方法下拉列表
     * @return
     */
    public ResponseJson selectDishesMethods() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = methodsMapper.selectDishesMethods(params);
        resp.setData(list);
        return resp;
    }


    /**
     * 菜品分类删除
     * @return
     */
    public ResponseJson dishesclassfyDel() throws AjaxOperationFailException {
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        if(classifyid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        DishesClassfys dishesClassfys = dishesClassfysMapper.selectByPrimaryKey(classifyid);
        ResponseJson resp = new ResponseJson();
        if(dishesClassfys != null){
            if(!isPomissionDishesClassfy(dishesClassfys)){
                throw new AjaxOperationFailException("没有权限!");
            }
            int countdishes = dishesMapper.selectCountByClassfy(classifyid);
            if(countdishes > 0){
                throw new AjaxOperationFailException("该菜品下有"+countdishes+"个菜品信息，无法删除!");
            }
            int count = dishesClassfysMapper.deleteByPrimaryKey(classifyid);
            if(count > 0){
                resp.setMsg("删除成功!");
            }
        }else {
            throw new AjaxOperationFailException("信息不存在!");
        }
        return resp;
    }

    /**
     * 修改展示状态
     * @return
     */
    public ResponseJson dishesclassfyChangeIsshow() throws AjaxOperationFailException {
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        String isshow = HttpRequestParamter.getString("isshow",true);
        if(classifyid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        if(StringUtil.isEmpty(isshow)){
            throw new AjaxOperationFailException("显示隐藏状态不能为空!");
        }
        DishesClassfys dishesClassfys = dishesClassfysMapper.selectByPrimaryKey(classifyid);
        ResponseJson resp = new ResponseJson();
        if(dishesClassfys != null) {
            if (!isPomissionDishesClassfy(dishesClassfys)) {
                throw new AjaxOperationFailException("没有权限!");
            }
            dishesClassfys.setIsShow(isshow);
            int count = dishesClassfysMapper.updateIsshow(dishesClassfys);
            if(count > 0){
                resp.setMsg("操作成功!");
            }
        }else {
            throw new AjaxOperationFailException("信息不存在!");
        }
        return resp;
    }


    /**
     * 修改上下架状态
     * @return
     */
    public ResponseJson dishesclassfyChangeStatus() throws AjaxOperationFailException {
        Integer classifyid = HttpRequestParamter.getInt("classifyid",0);
        String status = HttpRequestParamter.getString("status",true);
        if(classifyid==0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        if(StringUtil.isEmpty(status)){
            throw new AjaxOperationFailException("上下架状态不能为空!");
        }
        DishesClassfys dishesClassfys = dishesClassfysMapper.selectByPrimaryKey(classifyid);
        ResponseJson resp = new ResponseJson();
        if(dishesClassfys != null) {
            if (!isPomissionDishesClassfy(dishesClassfys)) {
                throw new AjaxOperationFailException("没有权限!");
            }
            dishesClassfys.setStatus(status);
            int count = dishesClassfysMapper.updateStatus(dishesClassfys);
            if(count > 0){
                resp.setMsg("操作成功!");
            }
        }else {
            throw new AjaxOperationFailException("信息不存在!");
        }
        return resp;
    }

    /**
     * 菜品新增
     * @return
     */
    public ResponseJson dishesAdd() throws AjaxOperationFailException {
        Integer dishesid = HttpRequestParamter.getInt("classifyid",0);//下拉选择菜品分类中的classifyid
        String goodscode = HttpRequestParamter.getString("goodscode",true);
        String is_goods = HttpRequestParamter.getString("is_goods",true);//原料：0-否，1-是
        String code = HttpRequestParamter.getString("code",true);//菜品编号
        String name = HttpRequestParamter.getString("name",true);//菜品名称
        String specs = HttpRequestParamter.getString("specs",true);//规格
        String unit = HttpRequestParamter.getString("unit",true);//单位（单位下拉选择）
        double price = HttpRequestParamter.getDouble("price",0.00);//价格
        String words = HttpRequestParamter.getString("words",true);//文字介绍
        String pic = HttpRequestParamter.getString("pic",true);//图片介绍url（调用通用接口中的图片上传返回图片地址作为参数）
        String memo = HttpRequestParamter.getString("memo",true);//备注
        String state = HttpRequestParamter.getString("state",true,"1");
        String isauto = HttpRequestParamter.getString("isauto",true,"1");//0表示不自动销售，1表示自动销售

        Dishes dishes = new Dishes();
        if(dishesid==0){
            throw new AjaxOperationFailException("请选择菜品分类!");
        }
        if(StringUtil.isEmpty(is_goods)){
            throw new AjaxOperationFailException("请选择是否原料!");
        }
        dishes.setCompanyid(employeeService.getLoginUser().getCompanyid());
        dishes.setStoreid(employeeService.getLoginUser().getStoreid());
        dishes.setStatus("0");//新增默认0
        dishes.setIntime(new Date());
        dishes.setDishesid(dishesid);
        dishes.setIsGoods(is_goods);
        dishes.setState(state);//折扣
        //选择原材料则不用填写下面的内容(从原料库获取资料)
        if(!is_goods.equals("1")){
            if(StringUtil.isEmpty(code)){
                throw new AjaxOperationFailException("菜品编码不能为空!");
            }
            if(StringUtil.isEmpty(name)){
                throw new AjaxOperationFailException("菜品名称不能为空!");
            }
            if(StringUtil.isEmpty(unit)){
                throw new AjaxOperationFailException("请选择单位!");
            }
            dishes.setCode(code);
            dishes.setName(name);
            dishes.setPinyin(Pinyin.getPinYinHeadChar(name));
            dishes.setSpecs(specs);
            dishes.setUnit(unit);
            dishes.setPrice(price);
            dishes.setAmount(0);
            if(checkDishes(dishes)){
                throw new AjaxOperationFailException("编号名称重复!");
            }
        }else {
            if(StringUtil.isEmpty(goodscode)){
                throw new AjaxOperationFailException("请选择原料!");
            }
            Map params = new HashMap();
            params.put("companyid",employeeService.getLoginUser().getCompanyid());
            params.put("storeid",employeeService.getLoginUser().getStoreid());
            params.put("code",goodscode);
            Goods goods = goodsMapper.selectBycode(params);
            if(goods==null){
                throw new AjaxOperationFailException("原料信息不存在!");
            }
            dishes.setCode(goods.getCode());
            dishes.setName(goods.getName());
            dishes.setPinyin(goods.getPinyin());
            dishes.setSpecs(goods.getSpecs());
            dishes.setMethodid(null);
            dishes.setUnit(goods.getUnit());
            dishes.setPrice(price);
            dishes.setAmount(0);//留着不晓得怎么办！
            dishes.setIsAuto(isauto);
        }
        dishes.setWords(words);
        dishes.setPic(pic);
        dishes.setFlagAmoun("0");
        dishes.setFlagGuqing("0");
        dishes.setMemo(memo);

        int count = dishesMapper.insert(dishes);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增菜品成功");
        }
        return resp;
    }

    /**
     * 菜品修改
     * @return
     */
    public ResponseJson dishesEdit() throws AjaxOperationFailException {
        Integer disheid = HttpRequestParamter.getInt("disheid",0);
        Integer dishesid = HttpRequestParamter.getInt("classifyid",0);//下拉选择菜品分类中的classifyid
        String goodscode = HttpRequestParamter.getString("goodscode",true);
        String is_goods = HttpRequestParamter.getString("is_goods",true);//原料：0-否，1-是
        String code = HttpRequestParamter.getString("code",true);//菜品编号
        String name = HttpRequestParamter.getString("name",true);//菜品名称
        String specs = HttpRequestParamter.getString("specs",true);//规格
        String unit = HttpRequestParamter.getString("unit",true);//单位（单位下拉选择）
        double price = HttpRequestParamter.getDouble("price",0.00);//价格
        String words = HttpRequestParamter.getString("words",true);//文字介绍
        String pic = HttpRequestParamter.getString("pic",true);//图片介绍url（调用通用接口中的图片上传返回图片地址作为参数）
        String memo = HttpRequestParamter.getString("memo",true);//备注
        String state = HttpRequestParamter.getString("state",true);
        String isauto = HttpRequestParamter.getString("isauto",true,"1");//0表示不自动销售，1表示自动销售
        Dishes dishes = dishesMapper.selectByPrimaryKey(disheid);
        if(dishes==null){
            throw new AjaxOperationFailException("菜品信息不存在!");
        }
        if(!isPromission(dishes)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(dishesid==0){
            throw new AjaxOperationFailException("请选择菜品分类!");
        }
        if(StringUtil.isEmpty(is_goods)){
            throw new AjaxOperationFailException("请选择是否原料!");
        }

        dishes.setCompanyid(employeeService.getLoginUser().getCompanyid());
        dishes.setStoreid(employeeService.getLoginUser().getStoreid());
        dishes.setStatus("0");//新增默认0
        dishes.setIntime(new Date());
        dishes.setDishesid(dishesid);
        dishes.setIsGoods(is_goods);
        dishes.setState(state);//折扣
        //选择原材料则不用填写下面的内容(从原料库获取资料)
        if(!is_goods.equals("1")){
            if(StringUtil.isEmpty(code)){
                throw new AjaxOperationFailException("菜品编码不能为空!");
            }
            if(StringUtil.isEmpty(name)){
                throw new AjaxOperationFailException("菜品名称不能为空!");
            }
            if(StringUtil.isEmpty(unit)){
                throw new AjaxOperationFailException("请选择单位!");
            }
            dishes.setName(name);
            dishes.setPinyin(Pinyin.getPinYinHeadChar(name));
            dishes.setSpecs(specs);
            dishes.setUnit(unit);
            dishes.setPrice(price);
            dishes.setAmount(0);
            if(!code.equals(dishes.getCode())){//查询编号是否重复
                dishes.setCode(code);
                int count = dishesMapper.checkDishescode(dishes);
                if(count > 0){
                    throw new AjaxOperationFailException("原料编号已经存在!");
                }
            }
            if(!name.equals(dishes.getName())){
                dishes.setName(name);
                int count = dishesMapper.checkDishesname(dishes);
                if(count > 0){
                    throw new AjaxOperationFailException("原料名称已经存在!");
                }
            }
            dishes.setCode(code);
        }else {
            if(StringUtil.isEmpty(goodscode)){
                throw new AjaxOperationFailException("请选择原料!");
            }
            Map params = new HashMap();
            params.put("companyid",employeeService.getLoginUser().getCompanyid());
            params.put("storeid",employeeService.getLoginUser().getStoreid());
            params.put("code",goodscode);
            Goods goods = goodsMapper.selectBycode(params);
            if(goods==null){
                throw new AjaxOperationFailException("原料信息不存在!");
            }
            dishes.setCode(goods.getCode());
            dishes.setName(goods.getName());
            dishes.setSpecs(goods.getSpecs());
            dishes.setMethodid(null);
            dishes.setUnit(goods.getUnit());
            dishes.setPrice(price);
            dishes.setAmount(0);//留着不晓得怎么办！
            dishes.setIsAuto(isauto);
        }
        dishes.setWords(words);
        dishes.setPic(pic);
        dishes.setMemo(memo);
        int count = dishesMapper.updateByPrimaryKey(dishes);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改菜品成功");
        }
        return resp;
    }


    private boolean checkDishes(Dishes dishes) {
        int count = dishesMapper.checkDishes(dishes);
        if(count>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean isPromission(Dishes dishes){
        Employees loginUser = employeeService.getLoginUser();
        if(((int)loginUser.getCompanyid()==(int)dishes.getCompanyid()) && ((int)loginUser.getStoreid()==(int)dishes.getStoreid())){
            return true;
        }else {
            return false;
        }
    }

    public boolean isPromission(DishePages dishes){
        Employees loginUser = employeeService.getLoginUser();
        if(((int)loginUser.getCompanyid()==(int)dishes.getCompanyid()) && ((int)loginUser.getStoreid()==(int)dishes.getStoreid())){
            return true;
        }else {
            return false;
        }
    }


    /**
     * 菜品上下架
     * @return
     */
    public ResponseJson dishesChangeStatus() throws AjaxOperationFailException {
        String status = HttpRequestParamter.getString("status",true);//备注
        Integer disheid = HttpRequestParamter.getInt("disheid",0);
        if(disheid == 0){
            throw new AjaxOperationFailException("参数id缺失!");
        }
        Dishes dishes = dishesMapper.selectByPrimaryKey(disheid);
        if(dishes == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPromission(dishes)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(!StringUtil.isEmpty(status)){
            if(status.equals("0") || status.equals("1")){
                dishes.setStatus(status);
                int count = dishesMapper.updateByPrimaryKey(dishes);
            }else {
                throw new AjaxOperationFailException("上下架参数错误!");
            }
        }else {
            throw new AjaxOperationFailException("上下架参数缺失!");
        }
        ResponseJson resp = new ResponseJson();
        resp.setMsg("操作成功!");
        return resp;
    }

    /**
     * 菜品估清列表
     * @return
     */
    public ResponseJson dishesGuqingList() {
        String name = HttpRequestParamter.getString("name",true);
        Integer dishesid = HttpRequestParamter.getInt("classifyid",0);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap();
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        if(!StringUtil.isEmpty(name)){
            params.put("name",name);
        }
        if(dishesid!=0){
            params.put("dishesid",dishesid);
        }
        PageHelper.startPage(page,size,"disheid asc");
        List<Map> list = dishesMapper.selectListGuqing(params);
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }



    public ResponseJson dishesGuqing() throws AjaxOperationFailException {
        String flag_guqing = HttpRequestParamter.getString("flag_guqing",true);
        Integer disheid = HttpRequestParamter.getInt("disheid",0);
        if(StringUtil.isEmpty(flag_guqing)){
            throw new AjaxOperationFailException("估清标志不能为空!");
        }else {
            if(!flag_guqing.equals("0") && !flag_guqing.equals("1")){
                throw new AjaxOperationFailException("估清标志参数错误!");
            }
        }
        if(disheid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        Dishes dishes = dishesMapper.selectByPrimaryKey(disheid);
        if(dishes==null){
            throw new AjaxOperationFailException("菜品信息不存在!");
        }
        if(!isPromission(dishes)){
            throw new AjaxOperationFailException("没有权限!");
        }
        dishes.setFlagGuqing(flag_guqing);
        int count = dishesMapper.updateByPrimaryKey(dishes);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("设置成功!");
        }
        return resp;
    }

    /**
     * 设置限量估清
     * @return
     */
    public ResponseJson dishesAmountGuqing() throws AjaxOperationFailException {
        String flag_amoun = HttpRequestParamter.getString("flag_amoun",true);
        Integer disheid = HttpRequestParamter.getInt("disheid",0);
        double amount = HttpRequestParamter.getDouble("amount",0);
        String memo = HttpRequestParamter.getString("memo",true);
        if(StringUtil.isEmpty(flag_amoun)){
            throw new AjaxOperationFailException("限量估清标志不能为空!");
        }else {
            if(!flag_amoun.equals("0") && !flag_amoun.equals("1")){
                throw new AjaxOperationFailException("限量估清标志参数错误!");
            }
        }
        if(disheid==0){
            throw new AjaxOperationFailException("id缺失!");
        }
        Dishes dishes = dishesMapper.selectByPrimaryKey(disheid);
        if(dishes==null){
            throw new AjaxOperationFailException("菜品信息不存在!");
        }
        if(!isPromission(dishes)){
            throw new AjaxOperationFailException("没有权限!");
        }
        ResponseJson resp = new ResponseJson();
        if(flag_amoun.equals("1")){//设置限量估清，则amount
            if(amount==0){
                throw new AjaxOperationFailException("设置限量估清后请填写总量!");
            }
        }else {
            amount=0;

        }
        dishes.setFlagAmoun(flag_amoun);
        dishes.setAmount(amount);
        dishes.setMemo(memo);
        dishes.setFlagGuqing("0");
        int count = dishesMapper.updateByPrimaryKey(dishes);
        if(count > 0){
           resp.setMsg("限量估清设置成功,估清标志变为未估清!");
        }
        return resp;

    }

    /**
     * 菜谱新增
     * @return
     */
    public ResponseJson dishesPageAdd() throws AjaxOperationFailException {
        Integer dishesid = HttpRequestParamter.getInt("dishesid",0);
        Integer sequence = HttpRequestParamter.getInt("sequence",0);
        String pagemould = HttpRequestParamter.getString("pagemould",true);
        String disheidstr = HttpRequestParamter.getString("disheidstr",true);
        if(dishesid==null){
            throw new AjaxOperationFailException("请选择菜品分类!");
        }
        if(sequence==0){
            throw new AjaxOperationFailException("请填写菜品排序页面!");
        }
        if(StringUtil.isEmpty(pagemould)){
            throw new AjaxOperationFailException("请选择菜品模板!");
        }
        if(StringUtil.isEmpty(disheidstr)){
            throw new AjaxOperationFailException("请选择菜品!");
        }
        String [] array = disheidstr.split(",");
        if(!pagemould.equals("14")){
            if(array.length != Integer.valueOf(pagemould)){
                throw new AjaxOperationFailException("所选择菜品数量和模板不符合!");
            }
        }
        Map param = new HashMap();
        param.put("companyid",employeeService.getLoginUser().getCompanyid());
        param.put("storeid",employeeService.getLoginUser().getStoreid());
        //查重
        List<Integer> idlist = new ArrayList<>();//已经添加的菜id集合
        List<String> list = dishePagesMapper.selectListByxxxx(param);
        for(String ids : list){
            for(String id : ids.split(",")){
                idlist.add(Integer.valueOf(id));
            }
        }
        //新增得几个菜品id集合
        List<String> listdisheidstr = Arrays.asList(array);
        for(String tjid : listdisheidstr){
            for(Integer id : idlist){
                if(Integer.valueOf(tjid) == id){
                    Dishes dishes = dishesMapper.selectByPrimaryKey(id);
                    throw new AjaxOperationFailException("菜品名为"+dishes.getName()+"的菜已经添加过了，不能重复添加!");
                }
            }
        }
        DishePages dishePages = new DishePages();
        dishePages.setCompanyid(employeeService.getLoginUser().getCompanyid());
        dishePages.setStoreid(employeeService.getLoginUser().getStoreid());
        dishePages.setStatus("0");
        dishePages.setDishesid(dishesid);
        dishePages.setDisheid(disheidstr);
        dishePages.setSequence(sequence);
        dishePages.setPagemould(pagemould);
        int count = dishePagesMapper.save(dishePages);
        ResponseJson responseJson = new ResponseJson();
        if(count > 0){
            responseJson.setMsg("新增菜谱成功!");
        }
        return responseJson;
    }

    /**
     * 菜谱修改
     * @return
     */
    public ResponseJson dishesPageEdit(DishePages dishePages) throws AjaxOperationFailException {
        if(dishePages==null)throw new AjaxOperationFailException("请选择要修改的数据！");
        //顺序查重
        DishePages dp2 = new DishePages();
        dp2.setDishesid(dishePages.getDishesid());
        dp2.setSequence(dishePages.getSequence());
        dp2 = dishePagesMapper.selectOne(dp2);
        if(dp2!=null&& dp2.getPageid()!=dishePages.getPageid()){
            throw new AjaxOperationFailException("显示顺序不能重复!");
        }

        String[] ids = dishePages.getDisheid().split(",");
        //不能添加超出数量
        if(ids==null || ids.length>Integer.valueOf(dishePages.getPagemould())){
            throw new AjaxOperationFailException("菜品数量不正确!");
        }
        DishePages dp = new DishePages();
        dp.setDishesid(dishePages.getDishesid());
        dp.setPageid(dishePages.getPageid());
        //菜单查重
        for (int i = 0; i < ids.length; i++) {
            dp.setDisheid(ids[i]);
            DishePages dp3 = dishePagesMapper.findBydisheId(dp);
            if(dp3!=null){
                Dishes dishes = dishesMapper.selectByPrimaryKey(Integer.valueOf(ids[i]));
                throw new AjaxOperationFailException("菜品名为"+dishes.getName()+"的菜已经添加过了，不能重复添加!");
            }
        }
        int count = dishePagesMapper.updateByPrimaryKeySelective(dishePages);
        ResponseJson responseJson = new ResponseJson();
        if(count > 0){
            responseJson.setMsg("修改菜谱成功!");
        }
        return responseJson;
    }


    /**
     * 菜谱列表
     * @return
     */
    public ResponseJson dishesPageList() {
        String name = HttpRequestParamter.getString("name",true);
        Integer dishesid = HttpRequestParamter.getInt("dishesid",0);
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        Map params = new HashMap();
        params.put("companyid",employeeService.getLoginUser().getCompanyid());
        params.put("storeid",employeeService.getLoginUser().getStoreid());
        if(StringUtil.isEmpty(name)){
            params.put("name",name);
        }
        if(dishesid!=0){
            params.put("dishesid",dishesid);
        }
        List<Map> listdishes = dishesMapper.selectAllidname(params);//所有的菜
        PageHelper.startPage(page,size,"sequence asc");
        List<Map> list = dishePagesMapper.selectList(params);
        for (Map map : list) {
            String[] array= map.get("disheid").toString().split(",");
            StringBuffer namestr = new StringBuffer();
            for (Map z : listdishes) {
                for(String s: array){
                    if(s.equals(z.get("disheid").toString())) {
                        namestr.append(z.get("name")+",");
                    }
                }
            }
            map.put("name",namestr);
        }
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }


    public ResponseJson dishesDel() throws AjaxOperationFailException {
        Integer disheid = HttpRequestParamter.getInt("disheid",0);
        if(disheid == 0){
            throw new AjaxOperationFailException("参数id缺失!");
        }
        Dishes dishes = dishesMapper.selectByPrimaryKey(disheid);
        if(dishes == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPromission(dishes)){
            throw new AjaxOperationFailException("没有权限!");
        }
        int count = dishesMapper.deleteByPrimaryKey(disheid);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }

    /**
     * 菜谱删除
     * @return
     */
    public ResponseJson dishesPagedel() throws AjaxOperationFailException {
        Integer pageid = HttpRequestParamter.getInt("pageid",0);
        if(pageid == 0){
            throw new AjaxOperationFailException("参数id缺失!");
        }
        DishePages dishes = dishePagesMapper.selectByPrimaryId(pageid);
        if(dishes == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPromission(dishes)){
            throw new AjaxOperationFailException("没有权限!");
        }
        int count = dishePagesMapper.deleteByPrimaryId(pageid);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }


    public ResponseJson dishesPageChangeStatus() throws AjaxOperationFailException {
        Integer pageid = HttpRequestParamter.getInt("pageid",0);
        String status = HttpRequestParamter.getString("status",true);
        if(pageid == 0){
            throw new AjaxOperationFailException("参数id缺失!");
        }
        if(StringUtil.isEmpty(status)){
            throw new AjaxOperationFailException("开放关闭参数缺失!");
        }else {
            if(!status.equals("0") && !status.equals("1") ){
                throw new AjaxOperationFailException("开放关闭参数错误!");
            }
        }
        DishePages dishes = dishePagesMapper.selectByPrimaryId(pageid);
        if(dishes == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPromission(dishes)){
            throw new AjaxOperationFailException("没有权限!");
        }
        dishes.setStatus(status);
        int count = dishePagesMapper.updateByPrimaryId(dishes);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("操作成功!");
        }
        return resp;
    }

    /**
     * 设置是否自动销售
     * @return
     */
    public ResponseJson setIsauto() throws AjaxOperationFailException {
        Integer disheid = HttpRequestParamter.getInt("disheid",0);
        String isauto = HttpRequestParamter.getString("isauto",true);
        if(disheid == 0){
            throw new AjaxOperationFailException("菜品id不能为空");
        }
        Dishes dishes = dishesMapper.selectByPrimaryKey(disheid);
        if(dishes == null){
            throw new AjaxOperationFailException("菜品信息不存在");
        }else {
            if(dishes.getIsGoods().equals("0")){
                throw new AjaxOperationFailException("该菜品非原料无法设置自动销售");
            }
        }
        if(StringUtil.isEmpty(isauto)){
            throw new AjaxOperationFailException("自动销售参数缺失");
        }
        dishes.setIsAuto(isauto);
        dishesMapper.updateByPrimaryKey(dishes);
        return new ResponseJson();
    }

    /**
     *
     * 设置是否可打折
     * @return
     */
    public ResponseJson setState() throws AjaxOperationFailException {
        Integer disheid = HttpRequestParamter.getInt("disheid",0);
        String state = HttpRequestParamter.getString("state",true);
        if(disheid == 0){
            throw new AjaxOperationFailException("菜品id不能为空");
        }
        Dishes dishes = dishesMapper.selectByPrimaryKey(disheid);
        if(dishes == null){
            throw new AjaxOperationFailException("菜品信息不存在");
        }
        if(StringUtil.isEmpty(state)){
            throw new AjaxOperationFailException("是否可打折参数缺失");
        }
        dishes.setState(state);
        dishesMapper.updateByPrimaryKey(dishes);
        return new ResponseJson();
    }

    /**
     * 菜品新增规格
     * @return
     */
    public ResponseJson dishesAddMethods() throws AjaxOperationFailException {
        Integer disheid = HttpRequestParamter.getInt("disheid",0);
        String name = HttpRequestParamter.getString("name",true);
        if(disheid == 0){
            throw new AjaxOperationFailException("菜品id不能为空");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("规格名称不能为空！");
        }
        Methods methods = new Methods();
        methods.setCompanyid(employeeService.getLoginUser().getCompanyid());
        methods.setStoreid(employeeService.getLoginUser().getStoreid());
        methods.setDishesid(disheid);
        methods.setName(name);
        methodsMapper.insert(methods);
        return new ResponseJson();
    }


    public ResponseJson dishesListMethods() throws AjaxOperationFailException {
        Integer disheid = HttpRequestParamter.getInt("disheid",0);
        if(disheid == 0){
            throw new AjaxOperationFailException("菜品id不能为空");
        }
        List<Methods> listmethods = methodsMapper.selectAllByMid(disheid);//所有的菜
        PageHelper.startPage(1,100,"methodid asc");
        PageInfo<Methods> pageinfo = new PageInfo<Methods>(listmethods);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(listmethods);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    public ResponseJson dishesDelMethods() throws AjaxOperationFailException {
        Integer methodid = HttpRequestParamter.getInt("methodid",0);
        if(methodid == 0){
            throw new AjaxOperationFailException("规格id不能为空");
        }
        methodsMapper.deleteByPrimaryKey(methodid);
        return new ResponseJson();
    }
}
