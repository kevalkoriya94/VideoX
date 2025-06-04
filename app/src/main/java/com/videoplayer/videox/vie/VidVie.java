package com.videoplayer.videox.vie;

import com.videoplayer.videox.db.entity.video.VideoInfo;

import java.util.List;


public interface VidVie extends BasVie {
    void onSearchVideo(List<VideoInfo> list);

    void openFavoriteVideo(List<VideoInfo> list);
}
