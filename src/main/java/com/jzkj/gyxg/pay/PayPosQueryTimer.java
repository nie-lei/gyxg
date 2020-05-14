package com.jzkj.gyxg.pay;

import com.jzkj.gyxg.common.RedisSession;
import com.jzkj.gyxg.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * pos机支付查询
 */
public class PayPosQueryTimer extends TimerTask {
    @Resource
    private RedisSession redisSession;

    public boolean flag = false;

    public static PayPosQueryTimer PayPosQueryTimer = null;
    public void taskCancle() {
        flag = false;
        if (PayPosQueryTimer == null)
            return;
        PayPosQueryTimer.cancel();
        PayPosQueryTimer = null;
    }

    @Override
    public void run() {
        System.out.println("PayPosQuery:================");
        List<Map<String, Object>> list = (List<Map<String, Object>>) redisSession.getValue("posQuery");
        synchronized (list) {
            if (StringUtil.isListNull(list)) {
                taskCancle();
            } else {
                Map<String, Object> map;
                String orderSn;
                String orderNum;
                int time;
                int count;
                for (int i = 0; i < list.size(); i ++) {
                    map = list.get(i);
                    orderSn = map.get("orderSn") + "";
                    orderNum = map.get("orderNum") + "";
                    time = Integer.parseInt(map.get("time") + "");
                    count = Integer.parseInt(map.get("count") + "");
                    Map<String, Object> result = HttpProxyPos.query(orderNum, orderSn);
                    if ("200".equals(result.get("result_code") + "") && "4".equals(result.get("order_status") + "")) {
                        String tender_sn = result.get("tender_sn") + "";
                        String pay_status = result.get("pay_status") + "";
                        String tender_type = result.get("tender_type") + "";
                        String sub_tender_type = result.get("sub_tender_type") + "";
                        //todo 修改订单信息
                        //删除缓存信息
                        list.remove(i);
                        i --;

                        if ((System.currentTimeMillis() / 1000 - time) / 60 > 6 || count > 37) {
                            list.remove(i);
                            i --;
                            //todo 修改订单信息为失败
                            //手动取消订单
                            HttpProxyPos.cancle("0", orderNum, orderSn, "", StringUtil.getRandomString(15,1));
                        }

                        count ++;
                        map.put("count", count);
                    }
                }
                redisSession.setValue("posQuery", list);

            }
        }
    }

}
