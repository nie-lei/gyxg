package com.jzkj.gyxg.service.web;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.PreReadUploadConfig;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.entity.Attachs;
import com.jzkj.gyxg.entity.Employees;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.AttachsMapper;
import com.jzkj.gyxg.mapper.GoodsMapper;
import com.jzkj.gyxg.mapper.MaterialClassfyMapper;
import com.jzkj.gyxg.util.FileUploadUtil;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AttachsService {

    @Autowired
    private AttachsMapper attachsMapper;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MaterialClassfyMapper materialClassfyMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private PreReadUploadConfig preReadUploadConfig;
    /**
     * 辅助信息列表
     * @return
     */
    public ResponseJson list() throws AjaxOperationFailException {
        Integer page = HttpRequestParamter.getInt("page",1);
        Integer size = HttpRequestParamter.getInt("size",10);
        String type = HttpRequestParamter.getString("type",true);
        Map params = new HashMap<>();
        if(StringUtil.isEmpty(type)){
            throw new AjaxOperationFailException("type不能为空!");

        }
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        PageHelper.startPage(page,size,"attachid asc");
        List<Map> list = null;
        params.put("flag",type);
        if("2".equals(type)){
            list = attachsMapper.selectList(params);
        }else if("0".equals(type)){
            list = attachsMapper.selectList0(params);
        }else {
            list = attachsMapper.selectList1(params);
        }
        PageInfo<Map> pageinfo = new PageInfo<Map>(list);
        ResponseJson responseJson = new ResponseJson();
        responseJson.setData(list);
        responseJson.setCount(pageinfo.getTotal());
        return responseJson;
    }

    /**
     * 辅助信息新增
     * @return
     */
    public ResponseJson add() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name",true);
        String flag = HttpRequestParamter.getString("flag",true);
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("名称不能为空!");
        }
        if(StringUtil.isEmpty(flag)){
            throw new AjaxOperationFailException("标志不能为空!");
        }
        Attachs attachs = new Attachs();
        attachs.setCompanyid(employeeService.getLoginUser().getCompanyid());
        attachs.setStoreid(employeeService.getLoginUser().getStoreid());
        attachs.setName(name);
        attachs.setFlag(flag);
        attachs.setStatus("0");
        if(checkAttachs(attachs)){
            throw new AjaxOperationFailException("名称重复!");
        }
        int count = attachsMapper.insert(attachs);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("新增成功!");
        }
        return resp;
    }

    /**
     * 辅助信息新增
     * @return
     */
    public ResponseJson edit() throws AjaxOperationFailException {
        String name = HttpRequestParamter.getString("name",true);
        String flag = HttpRequestParamter.getString("flag",true);
        Integer attachid = HttpRequestParamter.getInt("attachid",0);
        if(attachid == 0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        if(StringUtil.isEmpty(name)){
            throw new AjaxOperationFailException("名称不能为空!");
        }
        if(StringUtil.isEmpty(flag)){
            throw new AjaxOperationFailException("标志不能为空!");
        }
        Attachs attachs = attachsMapper.selectByPrimaryKey(attachid);
        if(attachs == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPomissionAttachs(attachs)){
            throw new AjaxOperationFailException("没有权限!");
        }
        if(name.equals(attachs.getName()) && flag.equals(attachs.getFlag())){

        }else {
            attachs.setName(name);
            attachs.setFlag(flag);
            if(checkAttachs(attachs)){
                throw new AjaxOperationFailException("名称重复!");
            }
        }
        int count = attachsMapper.updateByPrimaryKey(attachs);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("修改成功!");
        }
        return resp;
    }

    public boolean checkAttachs(Attachs attachs){
        int count = attachsMapper.checkAttachs(attachs);
        if(count > 0){
            return true;
        }else {
            return false;
        }
    }
    public boolean isPomissionAttachs(Attachs attachs){
        Employees loginuser = employeeService.getLoginUser();
        if(((int)loginuser.getCompanyid()==(int)attachs.getCompanyid()) && ((int)loginuser.getStoreid()==(int)attachs.getStoreid())){
            return true;
        }else {
            return false;
        }
    }


    public ResponseJson del() throws AjaxOperationFailException {
        Integer attachid = HttpRequestParamter.getInt("attachid",0);
        if(attachid == 0){
            throw new AjaxOperationFailException("缺失参数id!");
        }
        Attachs attachs = attachsMapper.selectByPrimaryKey(attachid);
        if(attachs == null){
            throw new AjaxOperationFailException("信息不存在!");
        }
        if(!isPomissionAttachs(attachs)){
            throw new AjaxOperationFailException("没有权限!");
        }
        int count = attachsMapper.deleteByPrimaryKey(attachid);
        ResponseJson resp = new ResponseJson();
        if(count > 0){
            resp.setMsg("删除成功!");
        }
        return resp;
    }



    /**
     * 文件上传
     * @param file
     * @return
     */
    public ResponseJson imgupload(MultipartFile file) throws AjaxOperationFailException {
        ResponseJson resp = new ResponseJson();
        String imgpath = FileUploadUtil.uploadImg(file,"",preReadUploadConfig);
        resp.setData(imgpath);
        return resp;
    }

    public ResponseJson selectAttachs() {
        String type = HttpRequestParamter.getString("type",true);
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list = null;
        if(!StringUtil.isEmpty(type)){
            if(type.equals("0") || type.equals("1") || type.equals("3") ||type.equals("4")){
                params.put("flag",type);
                list = attachsMapper.selectAttachs(params);
            }else{
                list = attachsMapper.selectAttachs2(params);
            }
        }
        resp.setData(list);
        return resp;
    }


    public ResponseJson selecmMaterialclassifys() throws AjaxOperationFailException {
        Integer level = HttpRequestParamter.getInt("level",0);
        if(level==0){
            throw new AjaxOperationFailException("等级参数为空!");
        }
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        params.put("level",level);
        ResponseJson resp = new ResponseJson();
        List<Map> list = materialClassfyMapper.selecmMaterialclassifys(params);
        resp.setData(list);
        return resp;
    }

    /**
     * 原料分类联动下拉
     * @return
     */
    public ResponseJson selecmMaterialclassifys2() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list1 = materialClassfyMapper.selecmMaterialclassifys1(params);
        for (Map p : list1) {
            params.put("pid",p.get("value"));
            List<Map> list2 = materialClassfyMapper.selecmMaterialclassifys2(params);
            if(list2!=null && list2.size()>0){
                p.put("children",list2);
            }
        }
        resp.setData(list1);
        return resp;
    }


    public ResponseJson selectGoods() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list1 = materialClassfyMapper.selecmMaterialclassifys1(params);
        for (Map p : list1) {
            params.put("pid",p.get("value"));
            List<Map> list2 = materialClassfyMapper.selecmMaterialclassifys2(params);
            if(list2!=null && list2.size()>0){
                for (Map p2:list2) {
                    params.put("cid",p2.get("value"));
                    List<Map> list3 = goodsMapper.selectGoods(params);
                    if(list3!=null && list3.size()>0){
                        p2.put("children",list3);
                    }
                }
                p.put("children",list2);
            }

        }
        resp.setData(list1);
        return resp;
    }
    public ResponseJson selectGoods2() {
        Map params = new HashMap();
        Employees employees = employeeService.getLoginUser();
        params.put("companyid",employees.getCompanyid());
        params.put("storeid",employees.getStoreid());
        ResponseJson resp = new ResponseJson();
        List<Map> list1 = materialClassfyMapper.selecmMaterialclassifys1(params);
        for (Map p : list1) {
            params.put("pid",p.get("value"));
            List<Map> list2 = materialClassfyMapper.selecmMaterialclassifys2(params);
            if(list2!=null && list2.size()>0){
                for (Map p2:list2) {
                    params.put("cid",p2.get("value"));
                    List<Map> list3 = goodsMapper.selectGoods2(params);
                    if(list3!=null && list3.size()>0){
                        p2.put("children",list3);
                    }
                }
                p.put("children",list2);
            }
        }
        resp.setData(list1);
        return resp;
    }

}
