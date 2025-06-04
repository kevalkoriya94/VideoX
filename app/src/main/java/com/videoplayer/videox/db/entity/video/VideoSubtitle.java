package com.videoplayer.videox.db.entity.video;


import androidx.annotation.NonNull;

public class VideoSubtitle {
    private String name;
    private String uri;

    public VideoSubtitle(String str, String str2) {
        this.uri = str;
        this.name = str2;
    }

    public String getUri() {
        String str = this.uri;
        return str == null ? "" : str;
    }

    public void setUri(String str) {
        this.uri = str;
    }

    public String getName() {
        String str = this.name;
        return str == null ? "" : str;
    }

    public void setName(String str) {
        this.name = str;
    }

    @NonNull
    public String toString() {
        return "VideoSubtitle{uri='" + this.uri + "', name='" + this.name + "'}";
    }
}
