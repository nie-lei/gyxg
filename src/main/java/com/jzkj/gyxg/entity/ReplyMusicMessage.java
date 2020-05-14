package com.jzkj.gyxg.entity;

public class ReplyMusicMessage {

    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String messageType;
    private Music Music;
    private String funcFlag;
    //这里的funcFlag，微信平台接口文档里没有，但是必须写上，不然会收不到返回的信息
    //getters、setters

    public String getToUserName() {
        return toUserName;
    }

    public ReplyMusicMessage setToUserName(String toUserName) {
        this.toUserName = toUserName;
        return this;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public ReplyMusicMessage setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public ReplyMusicMessage setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getMessageType() {
        return messageType;
    }

    public ReplyMusicMessage setMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public Music getMusic() {
        return Music;
    }

    public ReplyMusicMessage setMusic(Music music) {
        Music = music;
        return this;
    }

    public String getFuncFlag() {
        return funcFlag;
    }

    public ReplyMusicMessage setFuncFlag(String funcFlag) {
        this.funcFlag = funcFlag;
        return this;
    }
}