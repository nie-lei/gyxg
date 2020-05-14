package com.jzkj.gyxg.pay;

import com.jzkj.gyxg.common.RedisSession;
import com.jzkj.gyxg.entity.Payinfo;
import com.jzkj.gyxg.util.StringUtil;

import javax.annotation.Resource;
import java.util.Map;
import java.util.TimerTask;

/**
 * 扫码盒支付查询定时器
 */
public class PayQueryTimer extends TimerTask {
    @Resource
    private RedisSession redisSession;

    @Override
    public void run() {
        Payinfo payinfo = (Payinfo) redisSession.getValue("payInfo");
        if (payinfo == null) {
            return;
        }
        String sn = payinfo.getTerminalSn();
        String key = payinfo.getTerminalKey();
        if (StringUtil.isNull(sn) || StringUtil.isNull(key)) {
            return;
        }
        HttpProxy hp = new HttpProxy("https://api.shouqianba.com");
        Map<String, Object> result = hp.query(sn, key, "7895231814397243", "2908947314912861");
        System.out.println(result);
    }
}
