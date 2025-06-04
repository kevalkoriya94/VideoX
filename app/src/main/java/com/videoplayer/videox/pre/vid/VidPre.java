package com.videoplayer.videox.pre.vid;

import android.content.Context;

import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoPlaylist;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.VidVie;

import java.util.List;


public class VidPre extends BasePre<VidVie> {
    private final VideoDataRepository mVideoRepository;

    public VidPre(Context context, VidVie videoView, VideoDataRepository videoDataRepository) {
        super(videoView);
        this.mVideoRepository = videoDataRepository;
    }

    public void addVideoToPlaylist(VideoPlaylist videoPlaylist, List<VideoInfo> list) {
        this.mVideoRepository.updateVideoListForPlaylist(videoPlaylist, list);
    }

    public void deleteAllHistoryVideo() {
        this.mVideoRepository.deleteAllHistoryVideo();
    }

    public void getAllFavoriteVideo() {
        this.mVideoRepository.getAllFavoriteVideo(new ILoaderRepository.LoadDataListener<VideoInfo>() { // from class: com.videoplayer.videox.pre.vid.VidPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<VideoInfo> list) {
                if (VidPre.this.mView != null) {
                    ((VidVie) VidPre.this.mView).openFavoriteVideo(list);
                }
            }
        });
    }

    public void searchVideoByVideoName(String str) {
        this.mVideoRepository.searchVideoByVideoName(new ILoaderRepository.LoadDataListener<VideoInfo>() { // from class: com.videoplayer.videox.pre.vid.VidPre.2
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<VideoInfo> list) {
                if (VidPre.this.mView != null) {
                    ((VidVie) VidPre.this.mView).onSearchVideo(list);
                }
            }
        }, str);
    }
}
