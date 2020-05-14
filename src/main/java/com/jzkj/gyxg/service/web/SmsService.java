package com.jzkj.gyxg.service.web;

import com.jzkj.gyxg.entity.Sms;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import com.jzkj.gyxg.mapper.SmsMapper;
import com.jzkj.gyxg.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Transactional
@Service
public class SmsService {

    @Autowired
    private SmsMapper smsMapper;

    public static final Logger log = LoggerFactory.getLogger(SmsService.class);

    /**
     * 发送短信
     */
    public void sendMessage(String phone, String content) throws AjaxOperationFailException {
        //准备发送短信
        //计算短信条数
        int N = content.length() + 8;
        final int num ;
        if (N > 70) {
            num = new BigDecimal(N).divide(new BigDecimal(67), BigDecimal.ROUND_UP).intValue();
        }else{
            num = 1;
        }
        Map responseMap = null;
        responseMap = MessageUtil.sendSms(phone,content);
        Sms message = new Sms();
        if (responseMap == null) {
            message.setReason("短信接口无响应");
        }else{
            message.setContent(content);
            message.setPhone(phone);
            message.setSendtime(new Date());
            message.setMsgid(responseMap.get("msgid") + "");
            message.setReason(responseMap.get("msg")+"");
            if ((int) responseMap.get("state") != 0) {
                message.setSmsnum(0);
                message.setIssend("0");//失败
            }else{
                message.setSmsnum(num);
                message.setIssend("1");//成功
            }
            smsMapper.insert(message);
        }
    }


}
