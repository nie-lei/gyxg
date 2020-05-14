package com.jzkj.gyxg.util;

import com.alibaba.fastjson.JSON;
import com.jzkj.gyxg.common.RedisSession;

public class WechatMessage {

    /**
     * 发送模板消息
     * @param redisSession
     * @param openid        openid
     * @param templateId    模板id
     * @param title         标题
     * @param remark        备注
     * @param k1            键1
     * @param k2            键2
     * @param k3            键3
     * @param k4            键4
     * @param k5            键5
     */

    public static void sendMessage(RedisSession redisSession, String openid, String templateId, String title, String remark,
                                   String k1, String k2, String k3, String k4, String k5, String url) {
        sendWebChatMsg(redisSession, WebChatTemplateMessage.build()
                .setTouser(openid)
                .setTemplate_id(templateId)
                .setUrl(url)
                .setData(title, k1, k2, k3,k4,k5,remark));
    }

    private static void sendWebChatMsg(RedisSession redisSession, WebChatTemplateMessage msg) {
        System.out.println(JSON.toJSONString(msg));
        String access_token = StringUtil.getToken(redisSession);
        if (StringUtil.isNull(access_token)) {
            System.out.println("发送失败");
        } else {
            String responseStr = HttpUtil.doPostJson( "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token, JSON.toJSONString(msg), "utf-8");
            System.out.println(responseStr);

        }
    }
}
