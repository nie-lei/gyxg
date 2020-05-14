package com.jzkj.gyxg.entity;

public class Item {

    private String Title;
    private String Description;
    private String PicUrl;
    private String Url;

    public String getTitle() {
        return Title;
    }

    public Item setTitle(String title) {
        Title = title;
        return this;
    }

    public String getDescription() {
        return Description;
    }

    public Item setDescription(String description) {
        Description = description;
        return this;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public Item setPicUrl(String picUrl) {
        PicUrl = picUrl;
        return this;
    }

    public String getUrl() {
        return Url;
    }

    public Item setUrl(String url) {
        Url = url;
        return this;
    }
}