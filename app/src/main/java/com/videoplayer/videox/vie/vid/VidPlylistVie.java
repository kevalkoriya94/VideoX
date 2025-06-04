package com.videoplayer.videox.vie.vid;

import com.videoplayer.videox.db.entity.video.VideoPlaylist;
import com.videoplayer.videox.vie.BasVie;

import java.util.List;


public interface VidPlylistVie extends BasVie {
    void onCreatePlaylist(boolean z, VideoPlaylist videoPlaylist);

    void onDuplicationPlaylist(VideoPlaylist videoPlaylist);

    void onUpdatePlaylistName(int i, String str, boolean z);

    void updateVideoPlaylist(List<VideoPlaylist> list);
}
