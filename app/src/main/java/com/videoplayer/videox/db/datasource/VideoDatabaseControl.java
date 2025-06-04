package com.videoplayer.videox.db.datasource;

import android.os.Build;
import android.util.Log;
import android.util.LongSparseArray;

import com.videoplayer.videox.db.entity.video.VideoInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class VideoDatabaseControl {
    private static VideoDatabaseControl sInstance;
    private List<VideoInfo> mRecentlyVideo;
    private final LongSparseArray<VideoInfo> mSparseVideos = new LongSparseArray<>();
    private final Set<VideoInfo> mSetVideos = new HashSet();

    private VideoDatabaseControl() {
    }

    public static VideoDatabaseControl getInstance() {
        if (sInstance == null) {
            sInstance = new VideoDatabaseControl();
        }
        return sInstance;
    }

    public void addVideo(VideoInfo videoInfo) {
        this.mSparseVideos.put(videoInfo.getVideoId(), videoInfo);
        this.mSetVideos.add(videoInfo);
        this.mRecentlyVideo = null;
    }

    public void clearAll() {
        this.mSparseVideos.clear();
        this.mSetVideos.clear();
        this.mRecentlyVideo = null;
    }

    public Set<VideoInfo> getAllVideos() {
        return this.mSetVideos;
    }

    public VideoInfo getVideoById(long j) {
        return this.mSparseVideos.get(j);
    }

    public void removeVideoById(long j) {
        this.mSetVideos.remove(this.mSparseVideos.get(j));
        this.mSparseVideos.remove(j);
        this.mRecentlyVideo = null;
    }

    public List<VideoInfo> searchVideoByVideoName(final String str) {
        ArrayList arrayList = new ArrayList();
        if (Build.VERSION.SDK_INT >= 24) {
            return (List) this.mSetVideos.stream().filter((Predicate) obj -> {
                boolean contains;
                contains = ((VideoInfo) obj).getDisplayName().toLowerCase().contains(str.trim().toLowerCase());
                return contains;
            }).collect(Collectors.toList());
        }
        for (VideoInfo videoInfo : this.mSetVideos) {
            if (videoInfo.getDisplayName().toLowerCase().contains(str.trim().toLowerCase())) {
                arrayList.add(videoInfo);
            }
        }
        return arrayList;
    }

    public List<VideoInfo> getAllRecentlyVideo() {
        List<VideoInfo> list = this.mRecentlyVideo;
        if (list != null) {
            return list;
        }
        this.mRecentlyVideo = new ArrayList();
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                this.mRecentlyVideo = (List) this.mSetVideos.stream().filter(videoInfo -> getAllRecentlyVideo(videoInfo)).collect(Collectors.toList());
            } catch (Exception unused) {
                Log.e("TAG", "getAllRecentlyVideo: "+unused.getMessage());
                this.mRecentlyVideo = new ArrayList();
                Iterator it = new ArrayList(this.mSetVideos).iterator();
                while (it.hasNext()) {
                    VideoInfo videoInfo = (VideoInfo) it.next();
                    if (videoInfo.getDate() > System.currentTimeMillis() - 604800000) {
                        this.mRecentlyVideo.add(videoInfo);
                    }
                }
            }
        } else {
            this.mRecentlyVideo = new ArrayList();
            Iterator it2 = new ArrayList(this.mSetVideos).iterator();
            while (it2.hasNext()) {
                VideoInfo videoInfo2 = (VideoInfo) it2.next();
                if (videoInfo2.getDate() > System.currentTimeMillis() - 604800000) {
                    this.mRecentlyVideo.add(videoInfo2);
                }
            }
        }
        return this.mRecentlyVideo;
    }


    public static boolean getAllRecentlyVideo(VideoInfo videoInfo) {
        return videoInfo.getDate() > System.currentTimeMillis() - 604800000;
    }
}
