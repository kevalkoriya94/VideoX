package com.videoplayer.videox.pre.vid;

import android.content.Context;

import com.videoplayer.videox.db.entity.video.VideoFolder;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.vid.VidFoldVie;

import java.util.List;


public class VidFolPre extends BasePre<VidFoldVie> {
    private final VideoDataRepository mVideoRepository;

    public VidFolPre(Context context, VidFoldVie videoFolderView, VideoDataRepository videoDataRepository) {
        super(videoFolderView);
        this.mVideoRepository = videoDataRepository;
    }

    public void openFoldersTab() {
        this.mVideoRepository.getAllFolders(new ILoaderRepository.LoadDataListener<VideoFolder>() { // from class: com.videoplayer.videox.pre.vid.VidFolPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<VideoFolder> list) {
                if (VidFolPre.this.mView != null) {
                    ((VidFoldVie) VidFolPre.this.mView).updateFolderList(list);
                }
            }
        });
    }
}
