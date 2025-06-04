package com.videoplayer.videox.vie.vid;

import com.videoplayer.videox.db.entity.video.VideoFolder;
import com.videoplayer.videox.vie.BasVie;

import java.util.List;


public interface VidFoldVie extends BasVie {
    void updateFolderList(List<VideoFolder> list);
}
