package com.videoplayer.videox.pre.vid;

import android.content.Context;

import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.vid.VidHistVie;

import java.util.List;


public class VidHisPre extends BasePre<VidHistVie> {
    private final VideoDataRepository mVideoRepository;

    public VidHisPre(Context context, VidHistVie videoHistoryView, VideoDataRepository videoDataRepository) {
        super(videoHistoryView);
        this.mVideoRepository = videoDataRepository;
    }

    public void openHistoryTab() {
        this.mVideoRepository.getHistoryVideos(new ILoaderRepository.LoadDataListener<VideoHistory>() { // from class: com.videoplayer.videox.pre.vid.VidHisPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<VideoHistory> list) {
                if (VidHisPre.this.mView != null) {
                    ((VidHistVie) VidHisPre.this.mView).updateHistoryVideos(list);
                }
            }
        });
    }

    public void deleteAHistoryVideoById(long j) {
        this.mVideoRepository.deleteAHistoryVideoById(j);
    }
}
