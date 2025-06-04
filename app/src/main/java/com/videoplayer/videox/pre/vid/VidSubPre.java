package com.videoplayer.videox.pre.vid;

import com.videoplayer.videox.db.entity.video.VideoSubtitle;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.vie.vid.SubttlVie;

import java.util.List;


public class VidSubPre {
    private final VideoDataRepository mVideoRepository;
    private final SubttlVie mView;

    public VidSubPre(SubttlVie subtitleView, VideoDataRepository videoDataRepository) {
        this.mVideoRepository = videoDataRepository;
        this.mView = subtitleView;
    }

    public void getAllSubtitleFile() {
        this.mVideoRepository.getAllSubtitleFile(new ILoaderRepository.LoadDataListener<VideoSubtitle>() { // from class: com.videoplayer.videox.pre.vid.VidSubPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<VideoSubtitle> list) {
                if (VidSubPre.this.mView != null) {
                    VidSubPre.this.mView.updateSubtitle(list);
                }
            }
        });
    }
}
