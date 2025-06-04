package com.videoplayer.videox.db.datasource;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;

import com.videoplayer.videox.db.database.MyDatabase;
import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoPlaylist;
import com.videoplayer.videox.db.utils.VideoFavoriteUtil;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.thre.ThrExe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class VideoDatabaseDataSource {
    final Context mContext;

    public VideoDatabaseDataSource(Context context) {
        this.mContext = context;
    }

    public List<VideoPlaylist> getAllPlaylistVideos() {
        List<VideoPlaylist> allPlaylist = MyDatabase.getInstance(this.mContext).videoPlaylistDAO().getAllPlaylist();
        for (VideoPlaylist videoPlaylist : allPlaylist) {
            videoPlaylist.setVideoList(removeVideoNotExisted(videoPlaylist.getVideoList()));
        }
        return allPlaylist;
    }

    private List<VideoInfo> removeVideoNotExisted(List<VideoInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (VideoInfo videoInfo : list) {
            if (VideoDatabaseControl.getInstance().getVideoById(videoInfo.getVideoId()) != null) {
                arrayList.add(videoInfo);
            }
        }
        return arrayList;
    }

    public boolean createVideoPlaylist(final VideoPlaylist videoPlaylist) {
        if (checkPlaylistNameExisted(videoPlaylist.getPlaylistName())) {
            return false;
        }
        ThrExe.runOnDatabaseThread(() -> VideoDatabaseDataSource.this.createVideoPlaylist_VideoDatabaseDataSource(videoPlaylist));
        return true;
    }

    public void createVideoPlaylist_VideoDatabaseDataSource(VideoPlaylist videoPlaylist) {
        MyDatabase.getInstance(this.mContext).videoPlaylistDAO().insertNewPlaylist(videoPlaylist);
    }

    public boolean duplicateVideoPlaylist(final VideoPlaylist videoPlaylist) {
        if (checkPlaylistNameExisted(videoPlaylist.getPlaylistName())) {
            return false;
        }
        ThrExe.runOnDatabaseThread(() -> VideoDatabaseDataSource.this.duplicateVideoPlaylist_VideoDatabaseDataSource(videoPlaylist));
        return true;
    }

    public void duplicateVideoPlaylist_VideoDatabaseDataSource(VideoPlaylist videoPlaylist) {
        MyDatabase.getInstance(this.mContext).videoPlaylistDAO().insertNewPlaylist(videoPlaylist);
    }

    public void updateVideoListForPlaylist(VideoPlaylist videoPlaylist, List<VideoInfo> list) {
        MyDatabase.getInstance(this.mContext).videoPlaylistDAO().updateVideoListForPlaylist(videoPlaylist.getDateAdded(), list);
    }

    public boolean updatePlaylistName(final VideoPlaylist videoPlaylist, final String str) {
        if (checkPlaylistNameExisted(str)) {
            return false;
        }
        ThrExe.runOnDatabaseThread(() -> VideoDatabaseDataSource.this.updatePlaylistName_VideoDatabaseDataSource(videoPlaylist, str));
        return true;
    }

    public void updatePlaylistName_VideoDatabaseDataSource(VideoPlaylist videoPlaylist, String str) {
        MyDatabase.getInstance(this.mContext).videoPlaylistDAO().updatePlaylistName(videoPlaylist.getDateAdded(), str);
    }

    public void deletePlaylist(final VideoPlaylist videoPlaylist) {
        ThrExe.runOnDatabaseThread(() -> VideoDatabaseDataSource.this.deletePlaylist_VideoDatabaseDataSource(videoPlaylist));
    }

    public void deletePlaylist_VideoDatabaseDataSource(VideoPlaylist videoPlaylist) {
        MyDatabase.getInstance(this.mContext).videoPlaylistDAO().deletePlaylist(videoPlaylist.getDateAdded());
    }

    private boolean checkPlaylistNameExisted(String str) {
        String playlistContainingSpecificName = MyDatabase.getInstance(this.mContext).videoPlaylistDAO().getPlaylistContainingSpecificName(str);
        return !TextUtils.isEmpty(playlistContainingSpecificName) && playlistContainingSpecificName.equals(str);
    }

    public List<VideoHistory> getHistoryVideos() {
        ArrayList arrayList = new ArrayList();
        for (VideoHistory videoHistory : MyDatabase.getInstance(this.mContext).videoHistoryDAO().getAllHistoryVideo()) {
            VideoInfo videoById = VideoDatabaseControl.getInstance().getVideoById(videoHistory.getId());
            if (videoById != null) {
                videoHistory.setVideo(videoById);
            } else {
//                MyDatabase.getInstance(this.mContext).videoHistoryDAO().deleteVideoHistoryById(videoHistory.getId());
            }
            arrayList.add(videoHistory);
        }
        return arrayList;
    }


    public void deleteVideoHistoryById(final long j) {
        ThrExe.runOnDatabaseThread(() -> VideoDatabaseDataSource.this.deleteVideoHistoryById_VideoDatabaseDataSource(j));
    }

    public void deleteVideoHistoryById_VideoDatabaseDataSource(long j) {
        MyDatabase.getInstance(this.mContext).videoHistoryDAO().deleteVideoHistoryById(j);
    }

    public void deleteAllVideoHistory() {
        ThrExe.runOnDatabaseThread(() -> VideoDatabaseDataSource.this.deleteAllVideoHistory_VideoDatabaseDataSource());
    }

    public void deleteAllVideoHistory_VideoDatabaseDataSource() {
        MyDatabase.getInstance(this.mContext).videoHistoryDAO().deleteAllVideoHistory();
    }

    public void updateVideoHistoryData(final VideoInfo videoInfo) {
        ThrExe.runOnDatabaseThread(() -> VideoDatabaseDataSource.this.updateVideoHistoryData_VideoDatabaseDataSource(videoInfo));
    }

    public void updateVideoHistoryData_VideoDatabaseDataSource(VideoInfo videoInfo) {
        VideoHistory videoHistory = new VideoHistory();
        videoHistory.setId(videoInfo.getVideoId());
        videoHistory.setVideo(videoInfo);
        videoHistory.setDateAdded(System.currentTimeMillis());
        MyDatabase.getInstance(this.mContext).videoHistoryDAO().insertNewHistoryVideo(videoHistory);
    }

    public void updateVideoTimeData(final long j, final long j2) {
        ThrExe.runOnDatabaseThread(() -> VideoDatabaseDataSource.this.updateVideoTimeData_VideoDatabaseDataSource(j, j2));
    }

    public void updateVideoTimeData_VideoDatabaseDataSource(long j, long j2) {
        MyDatabase.getInstance(this.mContext).videoHistoryDAO().updateVideoTimeData(j, j2);
    }

    public int getAVideoTimeData(long j) {
        return MyDatabase.getInstance(this.mContext).videoHistoryDAO().getAVideoTimeData(j);
    }

    public List<VideoInfo> getAllFavoriteVideo() {
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        for (Long l : VideoFavoriteUtil.getAllFavoriteVideoId(this.mContext)) {
            VideoInfo videoById = VideoDatabaseControl.getInstance().getVideoById(l);
            if (videoById != null) {
                arrayList.add(videoById);
                hashSet.add(l);
            }
        }
        VideoFavoriteUtil.setFavoriteVideoId(this.mContext, hashSet);
        return arrayList;
    }

    public List<VideoInfo> searchVideoByVideoName(String str) {
        if (TextUtils.isEmpty(str.trim())) {
            return new ArrayList(VideoDatabaseControl.getInstance().getAllVideos());
        }
        return VideoDatabaseControl.getInstance().searchVideoByVideoName(str);
    }

    public List<VideoInfo> getAllVideoHidden() {
        ArrayList arrayList = new ArrayList();
        File file = new File(Utility.sHiddenVideoPath);
        if (!file.exists()) {
            file.mkdir();
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            for (File file2 : listFiles) {
                String name = file2.getName();
                if (!file2.isDirectory() && (name.endsWith(".mp4") || name.endsWith(".flv") || name.endsWith(".avi") || name.endsWith(".mkv") || name.endsWith(".mov") || name.endsWith(".3gp") || name.endsWith(".m4v") || name.endsWith(".webm") || name.endsWith(".qt") || name.endsWith(".wmv"))) {
                    VideoInfo videoInfo = new VideoInfo();
                    mediaMetadataRetriever.setDataSource(file2.getPath());
                    videoInfo.setDuration(Long.parseLong(mediaMetadataRetriever.extractMetadata(9)));
                    videoInfo.setDisplayName(file2.getName());
                    videoInfo.setSize(file2.length());
                    videoInfo.setPath(file2.getPath());
                    videoInfo.setUri(Uri.fromFile(file2).toString());
                    arrayList.add(videoInfo);
                }
            }
            try {
                mediaMetadataRetriever.release();
            } catch (IOException e) {
            }
        }
        arrayList.sort((obj, obj2) -> {
            int compareToIgnoreCase;
            compareToIgnoreCase = ((VideoInfo) obj2).getDisplayName().compareToIgnoreCase(((VideoInfo) obj).getDisplayName());
            return compareToIgnoreCase;
        });
        return arrayList;
    }
}
