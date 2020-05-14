package com.jzkj.gyxg.entity;

public class Music {
    private String title;
    private String description;
    private String musicUrl;
    private String hqMusicUrl;

    public String getTitle() {
        return title;
    }

    public Music setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Music setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public Music setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
        return this;
    }

    public String getHqMusicUrl() {
        return hqMusicUrl;
    }

    public Music setHqMusicUrl(String hqMusicUrl) {
        this.hqMusicUrl = hqMusicUrl;
        return this;
    }
}