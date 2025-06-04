package com.videoplayer.videox.pre.vid;

import android.content.Context;

import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.vid.VidInfVie;

import java.util.List;


public class VidInfoPre extends BasePre<VidInfVie> {
    private final VideoDataRepository mVideoRepository;

    public VidInfoPre(Context context, VidInfVie videoInfoView, VideoDataRepository videoDataRepository) {
        super(videoInfoView);
        this.mVideoRepository = videoDataRepository;
    }

    public void openVideosTab() {
        this.mVideoRepository.getAllVideos(new ILoaderRepository.LoadDataListener<VideoInfo>() { // from class: com.videoplayer.videox.pre.vid.VidInfoPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<VideoInfo> list) {
                if (VidInfoPre.this.mView != null) {
                    ((VidInfVie) VidInfoPre.this.mView).updateVideoList(list);
                }
            }
        });
    }
}
