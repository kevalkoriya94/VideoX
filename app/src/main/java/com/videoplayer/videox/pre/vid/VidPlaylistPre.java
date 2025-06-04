package com.videoplayer.videox.pre.vid;

import android.content.Context;

import com.videoplayer.videox.db.entity.video.VideoPlaylist;
import com.videoplayer.videox.db.repository.ILoaderRepository;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.BasePre;
import com.videoplayer.videox.vie.vid.VidPlylistVie;

import java.util.List;


public class VidPlaylistPre extends BasePre<VidPlylistVie> {
    private final VideoDataRepository mVideoRepository;

    public VidPlaylistPre(Context context, VidPlylistVie videoPlaylistView, VideoDataRepository videoDataRepository) {
        super(videoPlaylistView);
        this.mVideoRepository = videoDataRepository;
    }

    public void openPlaylistTab() {
        this.mVideoRepository.getAllPlaylistVideos(new ILoaderRepository.LoadDataListener<VideoPlaylist>() { // from class: com.videoplayer.videox.pre.vid.VidPlaylistPre.1
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onError() {
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.LoadDataListener
            public void onSuccess(List<VideoPlaylist> list) {
                if (VidPlaylistPre.this.mView != null) {
                    ((VidPlylistVie) VidPlaylistPre.this.mView).updateVideoPlaylist(list);
                }
            }
        });
    }

    public void createVideoPlaylist(String str) {
        final VideoPlaylist videoPlaylist = new VideoPlaylist();
        videoPlaylist.setPlaylistName(str.trim());
        videoPlaylist.setDateAdded(System.currentTimeMillis());
        this.mVideoRepository.createVideoPlaylist(new ILoaderRepository.InsertDataListener() { // from class: com.videoplayer.videox.pre.vid.VidPlaylistPre.2
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onSuccess() {
                if (VidPlaylistPre.this.mView != null) {
                    ((VidPlylistVie) VidPlaylistPre.this.mView).onCreatePlaylist(true, videoPlaylist);
                }
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onError() {
                if (VidPlaylistPre.this.mView != null) {
                    ((VidPlylistVie) VidPlaylistPre.this.mView).onCreatePlaylist(false, videoPlaylist);
                }
            }
        }, videoPlaylist);
    }

    public void deletePlaylist(VideoPlaylist videoPlaylist) {
        this.mVideoRepository.deletePlaylist(videoPlaylist);
    }

    public void duplicateVideoPlaylist(String str, VideoPlaylist videoPlaylist) {
        final VideoPlaylist videoPlaylist2 = new VideoPlaylist(str);
        videoPlaylist2.setDateAdded(System.currentTimeMillis());
        videoPlaylist2.setVideoList(videoPlaylist.getVideoList());
        this.mVideoRepository.duplicateVideoPlaylist(new ILoaderRepository.InsertDataListener() { // from class: com.videoplayer.videox.pre.vid.VidPlaylistPre.3
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onSuccess() {
                if (VidPlaylistPre.this.mView != null) {
                    ((VidPlylistVie) VidPlaylistPre.this.mView).onDuplicationPlaylist(videoPlaylist2);
                }
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onError() {
                if (VidPlaylistPre.this.mView != null) {
                    ((VidPlylistVie) VidPlaylistPre.this.mView).onDuplicationPlaylist(null);
                }
            }
        }, videoPlaylist2);
    }

    public void updatePlaylistName(VideoPlaylist videoPlaylist, final String str, final int i) {
        this.mVideoRepository.updatePlaylistName(new ILoaderRepository.InsertDataListener() { // from class: com.videoplayer.videox.pre.vid.VidPlaylistPre.4
            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onSuccess() {
                if (VidPlaylistPre.this.mView != null) {
                    ((VidPlylistVie) VidPlaylistPre.this.mView).onUpdatePlaylistName(i, str, true);
                }
            }

            @Override // com.videoplayer.videox.db.repository.ILoaderRepository.InsertDataListener
            public void onError() {
                if (VidPlaylistPre.this.mView != null) {
                    ((VidPlylistVie) VidPlaylistPre.this.mView).onUpdatePlaylistName(i, str, false);
                }
            }
        }, videoPlaylist, str);
    }
}
