package com.videoplayer.videox.pre.vid;

import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.VidPlayVie;


public class VidPlayPre extends BasePre<VidPlayVie> {
    private final VideoDataRepository mVideoRepository;

    public VidPlayPre(VidPlayVie videoPlayerView, VideoDataRepository videoDataRepository) {
        super(videoPlayerView);
        this.mVideoRepository = videoDataRepository;
    }

    public void updateVideoHistoryData(VideoInfo videoInfo) {
        this.mVideoRepository.updateVideoHistoryData(videoInfo);
    }

    public void updateVideoTimeData(long j, long j2) {
        this.mVideoRepository.updateVideoTimeData(j, j2);
    }

    public int getAVideoTimeData(long j) {
        return this.mVideoRepository.getAVideoTimeData(j);
    }
}
