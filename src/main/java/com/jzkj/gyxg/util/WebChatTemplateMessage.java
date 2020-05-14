package com.jzkj.gyxg.util;

import java.util.HashMap;
import java.util.Map;

public class WebChatTemplateMessage {
    private String touser;
    private String template_id;
    private String url;
    private Map<String,Object> miniprogram;
    private Map<String,Object> data;

    public static WebChatTemplateMessage build(){
        return new WebChatTemplateMessage();
    }

    public WebChatTemplateMessage setTouser(String touser) {
        this.touser = touser;
        return this;
    }

    public WebChatTemplateMessage setTemplate_id(String template_id) {
        this.template_id = template_id;
        return this;
    }

    public WebChatTemplateMessage setUrl(String url) {
        this.url = url;
        return this;
    }

    public WebChatTemplateMessage setMiniprogram(Map<String, Object> miniprogram) {
        this.miniprogram = miniprogram;
        return this;
    }

    public WebChatTemplateMessage setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public WebChatTemplateMessage setData(String first,String v1,String v2,String v3,String v4,String v5,String remark){
        Map<String,Object> map = new HashMap<>();

        map.put("first",createDataInfo(first + "\n",null));
        map.put("keyword1",createDataInfo(v1, "#173177"));
        map.put("keyword2",createDataInfo(v2,"#173177"));
        if(!StringUtil.isEmpty(v3)){
            map.put("keyword3",createDataInfo(v3,"#173177"));
        }
        if(!StringUtil.isEmpty(v4)){
            map.put("keyword4",createDataInfo(v4,"#173177"));
        }
        if(!StringUtil.isEmpty(v5)){
            map.put("keyword5",createDataInfo(v5,"#173177"));
        }
        map.put("remark",createDataInfo(remark,"#173177"));

        this.data = map;
        return this;
    }

    private Map<String,Object> createDataInfo(String value,String color){
        Map<String,Object> map = new HashMap<>();
        map.put("value",value);
        if(!StringUtil.isEmpty(color)){
            map.put("color",color);
        }
        return map;
    }

    public String getTouser() {
        return touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Object> getMiniprogram() {
        return miniprogram;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
