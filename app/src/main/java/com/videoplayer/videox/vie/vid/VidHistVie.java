package com.videoplayer.videox.vie.vid;

import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.vie.BasVie;

import java.util.List;


public interface VidHistVie extends BasVie {
    void updateHistoryVideos(List<VideoHistory> list);
}
