package com.jzkj.gyxg.controller.app;

import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.PreReadUploadConfig;
import com.jzkj.gyxg.common.RedisSession;
import com.jzkj.gyxg.common.ResponseJson;
import com.jzkj.gyxg.controller.BaseController;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.interceptor.WechatLoginCheckInterceptor;
import com.jzkj.gyxg.service.app.AppUserService;
import com.jzkj.gyxg.util.FileUploadUtil;
import com.jzkj.gyxg.util.HexString;
import com.jzkj.gyxg.util.StringUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/wechat/store")
public class WechatStoreController extends BaseController {
    @Resource
    private AppUserService appUserService;
    @Resource
    private RedisSession redisSession;
    @Resource
    private PreReadUploadConfig preReadUploadConfig;

    /**
     * 保存餐桌信息
     * @return
     */
    @RequestMapping("/wechatinfo")
    public void wechatinfo(HttpServletResponse resp) throws IOException {
        int uid = HttpRequestParamter.getUid();
        int tableid = HttpRequestParamter.getTableid();
//        String tableid = HttpRequestParamter.getString("tableid");


        String token = HexString.encode(StringUtil.getUUid());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uid", uid);
        map.put("tableid", tableid);
        redisSession.setMapValue(token, map, 1, TimeUnit.DAYS);
        resp.sendRedirect("http://www.cqgyxg.com/dist/index.html?token=" + token);
    }

    /**
     * 模拟登录
     * @param resp
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/login")
    public ResponseJson login(HttpServletRequest req, HttpServletResponse resp) throws AjaxOperationFailException, IOException {
//        ResponseJson responseJson = appUserService.wechatLogin(req, resp);
//        resp.sendRedirect("http://www.baidu.com");
        return appUserService.wechatLogin();
    }

    /**
     * 根据店铺id获取菜品分类信息
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getSecondDisheClassifyByStoreid")
    public ResponseJson getSecondDisheClassifyByStoreid() throws AjaxOperationFailException {
        return appUserService.getSecondDisheClassifyByStoreid();
    }

    /**
     * 根据分类id获取菜品列表信息
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getDisheByClassifyId")
    public ResponseJson getDisheByClassifyId() throws AjaxOperationFailException {
        return appUserService.getDisheByClassifyId();
    }

    /**
     * 下单
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/orderDishes")
    public ResponseJson orderDishes() throws AjaxOperationFailException {
        return appUserService.orderDishes();
    }

    /**
     * 根据名称搜索菜品
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getDishesByName")
    public ResponseJson getDishesByName() throws AjaxOperationFailException {
        return appUserService.getDishesBySearchName(1);
    }

    /**
     * 获取餐桌信息
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getTablesInfoById")
    public ResponseJson getTablesInfoById() throws AjaxOperationFailException {
        return appUserService.getTablesInfo(1);
    }

    /**
     * 获取餐台名称
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getTablesNameById")
    public ResponseJson getTablesNameById() throws AjaxOperationFailException {
        return appUserService.getTablesNameById();
    }

    /**
     * 获取餐桌开台状态
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getTablesStatus")
    public ResponseJson getTablesStatus() throws AjaxOperationFailException {
        return appUserService.getTablesStatus();
    }

    /**
     * 获取首页banner
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/getBanner")
    public ResponseJson getBanner() throws AjaxOperationFailException {
        return appUserService.getBanner();
    }

    /**
     * 微信支付
     * @return
     * @throws AjaxOperationFailException
     */
    @RequestMapping("/preOrder")
    public void payOrder(HttpServletResponse resp) throws AjaxOperationFailException {
        appUserService.payOrder(resp);
    }

    @RequestMapping("/getToken")
    public ResponseJson getToken(){
        return new ResponseJson("未登录", 101);
    }

    @RequestMapping("/uploadImg")
    public ResponseJson uploadImg(MultipartFile file) throws AjaxOperationFailException {
        return new ResponseJson("上传成功", FileUploadUtil.uploadImg(file, "", preReadUploadConfig));
    }
}
