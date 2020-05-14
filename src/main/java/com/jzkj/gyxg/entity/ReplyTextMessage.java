package com.jzkj.gyxg.entity;

public class ReplyTextMessage {

    private String ToUserName;
    private String FromUserName;
    private String CreateTime;
    private String MsgType;
    private String Content;
    private String FuncFlag;
    // getters,setters


    public String getToUserName() {
        return ToUserName;
    }

    public ReplyTextMessage setToUserName(String toUserName) {
        ToUserName = toUserName;
        return this;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public ReplyTextMessage setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
        return this;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public ReplyTextMessage setCreateTime(String createTime) {
        CreateTime = createTime;
        return this;
    }

    public String getMessageType() {
        return MsgType;
    }

    public ReplyTextMessage setMessageType(String messageType) {
        MsgType = messageType;
        return this;
    }

    public String getContent() {
        return Content;
    }

    public ReplyTextMessage setContent(String content) {
        Content = content;
        return this;
    }

    public String getFuncFlag() {
        return FuncFlag;
    }

    public ReplyTextMessage setFuncFlag(String funcFlag) {
        FuncFlag = funcFlag;
        return this;
    }
}