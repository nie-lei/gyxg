package com.jzkj.gyxg.controller.app;

import com.jzkj.gyxg.entity.Articles;
import com.jzkj.gyxg.entity.Item;
import com.jzkj.gyxg.entity.ReplyTextMessage;
import com.jzkj.gyxg.entity.ReplyTuwenMessage;
import com.jzkj.gyxg.util.SignUtil;
import com.jzkj.gyxg.util.StringUtil;
import com.jzkj.gyxg.util.WeiXinUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/wxtoken/auth")
public class WechatController {
    @RequestMapping(value = "/token")
    public void wxyz(HttpServletRequest req, HttpServletResponse resp) {
//        String roomid = req.getParameter("roomid");
        String token = "gyxghgd";
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        String echostr = req.getParameter("echostr");
        try {
            resp.setCharacterEncoding("utf-8");
            PrintWriter out = resp.getWriter();
            Map<String, Object> map = WeiXinUtils.parseXml(req);
            if (!StringUtil.isMapNull(map)) {
                String message = messageHandler(map);
                out.print(message);
            } else {
                if (SignUtil.checkSignature(signature, timestamp, nonce, token)) {
                    out.print(echostr);
                }
            }
            out.close();
            out = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String messageHandler(Map<String, Object> map) {
        String MsgType = map.get("MsgType") + "";
        String Event = map.get("Event") + "";
        int nowtime = (int) (new Date().getTime() / 1000);
        String message = "";
        String ToUserName = map.get("ToUserName") + "";
        String FromUserName = map.get("FromUserName") + "";
        if ("event".equals(MsgType)) { //事件推送消息处理
            if ("SCAN".equals(Event)) {  //扫描带参二维码
                //回复图文消息
                String EventKey = map.get("EventKey") + "";
                Item item = new Item();
                item.setTitle("古渝雄关");
                String[] str = EventKey.split("_");
                item.setDescription("点击进入"+str[2]+"餐桌");
                item.setPicUrl("http://api.cqgyxg.com:8081/image/202001101059445634.png");
                item.setUrl("http://api.cqgyxg.com/wechat/store/wechatinfo?storeid=" + str[0] + "&tablesid=" + str[1] + "&tablesname=" + str[2]);
                Articles articles = new Articles();
                articles.setItem(item);
                ReplyTuwenMessage replyTuwenMessage = new ReplyTuwenMessage();
                replyTuwenMessage.setFromUserName(ToUserName);
                replyTuwenMessage.setToUserName(FromUserName);
                replyTuwenMessage.setArticleCount(1);
                replyTuwenMessage.setArticles(articles);
                replyTuwenMessage.setCreateTime(nowtime + "");
                replyTuwenMessage.setMsgType("news");
                message = WeiXinUtils.tuwenMessageToXml(replyTuwenMessage);
            } else if ("subscribe".equals(Event)) {   //关注公众号事件
                String EventKey = map.get("EventKey") + "";
                String Ticket = map.get("Ticket") + "";
                if (StringUtil.isNull(EventKey)) { //直接关注公众号
                    ReplyTextMessage textMessage = new ReplyTextMessage();
                    textMessage.setContent("欢迎您加入公众平台");
                    textMessage.setCreateTime(nowtime + "");
                    textMessage.setFromUserName(ToUserName);
                    textMessage.setToUserName(FromUserName);
                    textMessage.setMessageType("text");
                    message = WeiXinUtils.textMessageToXml(textMessage);
                } else {  //扫描带参二维码关注
                    //回复图文消息
                    EventKey = EventKey.replace("qrscene_", "");
                    Item item = new Item();
                    item.setTitle("古渝雄关");
                    String[] str = EventKey.split("_");
                    item.setDescription("点击进入"+str[2]+"餐桌");
                    item.setPicUrl("http://api.cqgyxg.com:8081/image/202001101059445634.png");
                    item.setUrl("http://api.cqgyxg.com/wechat/store/wechatinfo?storeid=" + str[0] + "&tablesid=" + str[1] + "&tablesname=" + str[2]);
                    Articles articles = new Articles();
                    articles.setItem(item);
                    ReplyTuwenMessage replyTuwenMessage = new ReplyTuwenMessage();
                    replyTuwenMessage.setFromUserName(ToUserName);
                    replyTuwenMessage.setToUserName(FromUserName);
                    replyTuwenMessage.setArticleCount(1);
                    replyTuwenMessage.setArticles(articles);
                    replyTuwenMessage.setCreateTime(nowtime + "");
                    replyTuwenMessage.setMsgType("news");
                    message = WeiXinUtils.tuwenMessageToXml(replyTuwenMessage);
                }
            } else if ("unsubscribe".equals(Event)) { //取消关注公众号事件

            } else if ("LOCATION".equals(Event)) { //上报地理位置事件

            } else if ("CLICK".equals(Event)) { //自定义菜单啦取消息事件

            } else if ("VIEW".equals(Event)) { //点击菜单跳转链接时间

            }
        } else if("text".equals(MsgType)) {
            /*String Content = map.get("Content") + "";
            if("玩".equals(Content)) {
                //回复图文消息
                Item item = new Item();
                item.setTitle("");
                item.setDescription("");
                item.setPicUrl("http://proya.oss-cn-shanghai.aliyuncs.com/images/upload/20170825/716JpXn.jpg");
                item.setUrl("http://mtl.tairui88.com/ins?roomid=45&openid=" + FromUserName);
                Articles articles = new Articles();
                articles.setItem(item);
                ReplyTuwenMessage replyTuwenMessage = new ReplyTuwenMessage();
                replyTuwenMessage.setFromUserName(ToUserName);
                replyTuwenMessage.setToUserName(FromUserName);
                replyTuwenMessage.setArticleCount(1);
                replyTuwenMessage.setArticles(articles);
                replyTuwenMessage.setCreateTime(nowtime + "");
                replyTuwenMessage.setMsgType("news");
                message = WeiXinUtils.tuwenMessageToXml(replyTuwenMessage);
            }*/
        }
        return message;
    }
}
