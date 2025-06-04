package com.videoplayer.videox.pre.vid;

import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.vid.VidHidVie;

import java.util.List;


public class VidHidPre extends BasePre<VidHidVie> {
    private final VideoDataRepository mVideoRepository;

    public VidHidPre(VidHidVie videoHiddenView, VideoDataRepository videoDataRepository) {
        super(videoHiddenView);
        this.mVideoRepository = videoDataRepository;
    }

    public void openVideoHidden() {
        this.mVideoRepository.getAllVideoHidden(new ILoaderRepository.LoadDataListener<VideoInfo>() { // from class: com.videoplayer.videox.pre.vid.VidHidPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<VideoInfo> list) {
                if (VidHidPre.this.mView != null) {
                    ((VidHidVie) VidHidPre.this.mView).updateVideoHiddenList(list);
                }
            }
        });
    }
}
