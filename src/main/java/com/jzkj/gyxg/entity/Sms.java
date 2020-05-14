package com.jzkj.gyxg.entity;

import java.util.Date;

public class Sms extends BaseEntity {
    private Integer id;

    private String content;

    private String phone;

    private Integer smsnum;

    private String issend;

    private Date sendtime;

    private String reason;

    private String msgid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getSmsnum() {
        return smsnum;
    }

    public void setSmsnum(Integer smsnum) {
        this.smsnum = smsnum;
    }

    public String getIssend() {
        return issend;
    }

    public void setIssend(String issend) {
        this.issend = issend;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }
}