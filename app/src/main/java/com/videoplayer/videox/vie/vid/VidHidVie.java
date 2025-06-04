package com.videoplayer.videox.vie.vid;

import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.vie.BasVie;

import java.util.List;


public interface VidHidVie extends BasVie {
    void updateVideoHiddenList(List<VideoInfo> list);
}
