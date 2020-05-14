package com.jzkj.gyxg.controller.web;

import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.service.web.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("api/common")
@RestController
public class CommonController extends BaseController{

    @Autowired
    private PositionService positionService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DishesService dishesService;
    @Autowired
    private RolesService rolesService;
    @Autowired
    private AreasService areasService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private PrintService printService;

    @Autowired
    private AttachsService attachsService;
    @Autowired
    private SuppliersService suppliersService;

    /**
     * 部门下拉列表数据
     * @return
     */
    @RequestMapping("selectdept")
    public ResponseJson selectDept(){
        return departmentService.selectDept();
    }

    /**
     * 职位下拉列表数据
     * @return
     */
    @RequestMapping("selectposition")
    public ResponseJson selectPosition(){
        return positionService.selectPosition();
    }


    /**
     * 打印分类下拉列表数据
     * @return
     */
    @RequestMapping("selectprintclassfy")
    public ResponseJson selectPrintClassfy(){
        return printService.selectPrintClassfy();
    }


    /**
     * 菜品分类下拉列表数据
     * @return
     */
    @RequestMapping("selectdishesclassfy")
    public ResponseJson selectDishesClassfy(){
        return dishesService.selectDishesClassfy();
    }

    /**
     * 烹饪方式下拉列表数据
     * @return
     */
    @RequestMapping("selectdishesmethods")
    public ResponseJson selectDishesMethods(){
        return dishesService.selectDishesMethods();
    }

    /**
     * 角色下拉
     * @return
     */
    @RequestMapping("selectroles")
    public ResponseJson selectRoles(){
        return rolesService.selectRoles();
    }

    /**
     * 区域信息下拉
     * @return
     */
    @RequestMapping("selectareas")
    public ResponseJson selectAreas(){
        return areasService.selectAreas();
    }

    /**
     * 负责人下拉
     * @return
     */
    @RequestMapping("selectemp")
    public ResponseJson selectEmp(){
        return employeeService.selectEmp();
    }


    /**
     *图片上传
     * @return
     */
    @RequestMapping("imgupload")
    public ResponseJson imgupload(MultipartFile file) throws AjaxOperationFailException {
        return attachsService.imgupload(file);
    }

    /**
     *获取原因，单位
     * @return
     */
    @RequestMapping("selectattachs")
    public ResponseJson selectAttachs() throws AjaxOperationFailException {
        return attachsService.selectAttachs();
    }

    /**
     *原料分类父级下拉
     * @return
     */
    @RequestMapping("selecmaterialclassifys")
    public ResponseJson selecmMaterialclassifys() throws AjaxOperationFailException {
        return attachsService.selecmMaterialclassifys();
    }

    /**
     *原料分类联动
     * @return
     */
    @RequestMapping("selecmaterialclassifys2")
    public ResponseJson selecmMaterialclassifys2() throws AjaxOperationFailException {
        return attachsService.selecmMaterialclassifys2();
    }

    /**
     *原料下拉专属接口
     * @return
     */
    @RequestMapping("selectgoods")
    public ResponseJson selectGoods() throws AjaxOperationFailException {
        return attachsService.selectGoods();
    }

    /**
     *原料下拉专属接口最后一层为id
     * @return
     */
    @RequestMapping("selectgoods2")
    public ResponseJson selectGoods2() throws AjaxOperationFailException {
        return attachsService.selectGoods2();
    }

    /**
     *供应商下拉选择（2019-12-20）
     * @return
     */
    @RequestMapping("selectsupplier")
    public ResponseJson selectSupplier() throws AjaxOperationFailException {
        return suppliersService.selectSupplier();
    }

    /**
     *仓库下拉选择（2019-12-20）
     * @return
     */
    @RequestMapping("selectwarehouse")
    public ResponseJson selectwarehouse() throws AjaxOperationFailException {
        return suppliersService.selectwarehouse();
    }

    /**
     * 获取折扣
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("selectdiscount")
    public ResponseJson selectdiscount() throws AjaxOperationFailException {
        return suppliersService.selectdiscount();
    }

}
