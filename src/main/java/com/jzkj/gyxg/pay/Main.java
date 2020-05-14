package com.jzkj.gyxg.pay;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.jzkj.gyxg.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ycc on 15/12/15.
 */
public class Main {
    private static String vendor_sn = "91800920";                              //需要联系收钱吧技术支持申请获得
    private static String vendor_key = "1222df4864f36499a9cf5f1eb871f931";     //需要联系收钱吧技术支持申请获得
    private static String appid="2019121000002416";                            //需要登录收钱吧服务商平台选择相应业务场景生成
    private static String code = "36470624";                                   //需联系收钱吧提供激活码
    private static String api_domain = "https://api.shouqianba.com";

    public static void main(String []args){
        HttpProxy hp = new HttpProxy(api_domain);
        String terminal_sn = "100009200009978067";
        String terminal_key = "54c7cb1f2ca5cf63b4305843dd9d4342";

//        System.out.println("1&2&3&4&5&6&7".substring(0, "1&2&3&4&5&6&7".lastIndexOf('&')));
//        String terminal_key = "6889e314458b15dd47d699857f58babb";

//        int ordernum = StringUtil.getRandom(15);
//        Map<String, Object> result = hp.pay(terminal_sn, terminal_key, ordernum + "", "1", "",
//                "", "", "结账", "001");
//        System.out.println(result);


        JSONObject terminal = hp.checkin(terminal_sn, terminal_key); //签到
        System.out.println(terminal);
//        String orderNum = StringUtil.getRandomString(20);
//        String result = hp.precreate(terminal_sn, terminal_key, orderNum, "1", "1", "古渝雄关火锅店", "gyxg001", "");     //预下单
//        System.out.println(result);
//        result = hp.precreate(terminal_sn, terminal_key, orderNum, "1", "3", "古渝雄关火锅店", "gyxg001", "");     //预下单
//        System.out.println(result);

//        Map<String, Object>  result = hp.query(terminal_sn, terminal_key, "7895231814397243", "2908947314912861");
//        System.out.println(result);

//        Map<String, Object>  result = hp.cancel(terminal_sn, terminal_key, "7895232754977929", "aBVaxNFFYaajn3PNBJAb");
//        System.out.println(result);
//
//        String result = hp.getWay(terminal_sn, terminal_key, ordernum + "", "0.01", "古渝雄关老火锅", "gyxg001", "http://www.cqgyxg.com");
//        System.out.println(result);
//        Map<String, Object> result = hp.refund(terminal_sn, terminal_key, "7895231808648188", "2908947314912861", "1", "gyxg0022aaa", "gyxg001");   //退款
//        System.out.println(result);
//        JSONObject result = hp.activate(vendor_sn,vendor_key,appid,code); //激活
//        if(result != null){
//            try{
//                String terminal_sn = result.getString("terminal_sn");
//                String terminal_key = result.getString("terminal_key");
//
//                JSONObject terminal = hp.checkin(terminal_sn, terminal_key); //签到
//                if(terminal != null) {
//                    String new_terminal_sn = terminal.getString("terminal_sn");
//                    String new_terminal_key = terminal.getString("terminal_key");
//
//                    hp.pay(new_terminal_sn, new_terminal_key);
//                    hp.refund(new_terminal_sn, new_terminal_key);
//                    hp.query(new_terminal_sn, new_terminal_key, "", "");
//                    hp.cancel(new_terminal_sn, new_terminal_key);
////                    hp.revoke(new_terminal_sn, new_terminal_key);
//                    hp.precreate(new_terminal_sn,new_terminal_key);
//                }
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//        }

    }

}

