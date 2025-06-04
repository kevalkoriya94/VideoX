package com.videoplayer.videox.vie.vid;

import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.vie.BasVie;

import java.util.List;


public interface VidInfVie extends BasVie {
    void updateVideoList(List<VideoInfo> list);
}
