package com.jzkj.gyxg.entity;

public class ReplyTuwenMessage {

    private String ToUserName;
    private String FromUserName;
    private String CreateTime;
    private String MsgType;
    private int ArticleCount;

    private Articles Articles;
    private String funcFlag;


    public String getToUserName() {
        return ToUserName;
    }

    public ReplyTuwenMessage setToUserName(String toUserName) {
        ToUserName = toUserName;
        return this;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public ReplyTuwenMessage setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
        return this;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public ReplyTuwenMessage setCreateTime(String createTime) {
        CreateTime = createTime;
        return this;
    }

    public String getMsgType() {
        return MsgType;
    }

    public ReplyTuwenMessage setMsgType(String msgType) {
        MsgType = msgType;
        return this;
    }

    public int getArticleCount() {
        return ArticleCount;
    }

    public ReplyTuwenMessage setArticleCount(int articleCount) {
        ArticleCount = articleCount;
        return this;
    }

    public Articles getArticles() {
        return Articles;
    }

    public ReplyTuwenMessage setArticles(Articles articles) {
        Articles = articles;
        return this;
    }

    public String getFuncFlag() {
        return funcFlag;
    }

    public ReplyTuwenMessage setFuncFlag(String funcFlag) {
        this.funcFlag = funcFlag;
        return this;
    }
}