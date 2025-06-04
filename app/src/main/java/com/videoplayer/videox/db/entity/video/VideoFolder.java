package com.videoplayer.videox.db.entity.video;

import java.util.ArrayList;
import java.util.List;


public class VideoFolder {
    private String mId;
    private String mName;
    private List<VideoInfo> mVideosList = new ArrayList();

    public String getId() {
        return this.mId;
    }

    public String getFolderName() {
        String str = this.mName;
        return str == null ? "Root" : str;
    }

    public String getPath() {
        List<VideoInfo> list = this.mVideosList;
        if (list == null || list.isEmpty()) {
            return "";
        }
        VideoInfo videoInfo = this.mVideosList.get(0);
        String path = videoInfo.getPath();
        int lastIndexOf = path.lastIndexOf(videoInfo.getDisplayName()) - 1;
        return lastIndexOf > -1 ? path.substring(0, lastIndexOf) : "";
    }

    public void setId(String str) {
        this.mId = str;
    }

    public void setFolderName(String str) {
        this.mName = str;
    }

    public List<VideoInfo> getVideoList() {
        List<VideoInfo> list = this.mVideosList;
        return list == null ? new ArrayList() : list;
    }

    public void addVideo(VideoInfo videoInfo) {
        this.mVideosList.add(videoInfo);
    }

    public void setVideosList(List<VideoInfo> list) {
        this.mVideosList = list;
    }

    public long getSize() {
        List<VideoInfo> list = this.mVideosList;
        long j = 0;
        if (list == null) {
            return 0L;
        }
        for (VideoInfo videoInfo : list) {
            j += videoInfo.getSize();
        }
        return j;
    }
}
