package com.jzkj.gyxg.common;

import java.io.Serializable;

public class ResponseJson implements Serializable {
    private int code = 0;
    private String msg = "";
    private long count = 0;
    private Object data;

    public ResponseJson(int code, String msg, int count, Object data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    public ResponseJson() {}

    public ResponseJson(String msg) {
        this.msg = msg;
    }

    public ResponseJson(String msg, Object data) {
        this.msg = msg;
        this.data = data;
    }

    public ResponseJson(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
