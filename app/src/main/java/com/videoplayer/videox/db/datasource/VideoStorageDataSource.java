package com.videoplayer.videox.db.datasource;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.videoplayer.videox.db.entity.video.VideoFolder;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoSubtitle;
import com.videoplayer.videox.uti.SharPrefUti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class VideoStorageDataSource {
    private final Context mContext;

    public VideoStorageDataSource(Context context) {
        this.mContext = context;
    }

    public List<VideoInfo> getAllVideoFromStorage() {
        long totalsize = 0;
        long size;
        Uri uri;
        VideoDatabaseControl.getInstance().clearAll();
        HashSet hashSet = new HashSet();
        String[] strArr = {"_id", "_data", "_display_name", "duration", "_size", "resolution", "date_modified", "mime_type"};
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Video.Media.getContentUri("external");
        } else {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor query = this.mContext.getContentResolver().query(uri, strArr, null, null, "date_modified DESC");
        if (query != null) {
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("_id");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("_data");
                int columnIndexOrThrow3 = query.getColumnIndexOrThrow("_display_name");
                int columnIndexOrThrow4 = query.getColumnIndexOrThrow("duration");
                int columnIndexOrThrow5 = query.getColumnIndexOrThrow("_size");
                int columnIndexOrThrow6 = query.getColumnIndexOrThrow("resolution");
                int columnIndexOrThrow7 = query.getColumnIndexOrThrow("date_modified");
                int columnIndexOrThrow8 = query.getColumnIndexOrThrow("mime_type");
                while (query.moveToNext()) {
                    VideoInfo videoInfo = new VideoInfo();
                    long j = query.getLong(columnIndexOrThrow);
                    videoInfo.setVideoId(j);
                    videoInfo.setUri(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, j).toString());
                    videoInfo.setPath(query.getString(columnIndexOrThrow2));
                    videoInfo.setDisplayName(query.getString(columnIndexOrThrow3));
                    videoInfo.setDuration(query.getLong(columnIndexOrThrow4));
                    videoInfo.setSize(query.getLong(columnIndexOrThrow5));
                    videoInfo.setResolution(query.getString(columnIndexOrThrow6));
                    videoInfo.setDate(query.getLong(columnIndexOrThrow7) * 1000);
                    videoInfo.setMimeType(query.getString(columnIndexOrThrow8));
                    hashSet.add(videoInfo);
                    VideoDatabaseControl.getInstance().addVideo(videoInfo);

                    try {
                        size = query.getLong(columnIndexOrThrow5);
                        totalsize = size + totalsize;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                SharPrefUti.putTotalSize(this.mContext, totalsize);
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return new ArrayList(hashSet);
    }


    public List<VideoInfo> getUnwatchedVideoFromStorage() {
        Uri uri;
        VideoDatabaseControl.getInstance().clearAll();
        HashSet hashSet = new HashSet();
        String[] strArr = {"_id", "_data", "_display_name", "duration", "_size", "resolution", "date_modified", "mime_type"};
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Video.Media.getContentUri("external");
        } else {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor query = this.mContext.getContentResolver().query(uri, strArr, null, null, "date_modified DESC");
        if (query != null) {
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("_id");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("_data");
                int columnIndexOrThrow3 = query.getColumnIndexOrThrow("_display_name");
                int columnIndexOrThrow4 = query.getColumnIndexOrThrow("duration");
                int columnIndexOrThrow5 = query.getColumnIndexOrThrow("_size");
                int columnIndexOrThrow6 = query.getColumnIndexOrThrow("resolution");
                int columnIndexOrThrow7 = query.getColumnIndexOrThrow("date_modified");
                int columnIndexOrThrow8 = query.getColumnIndexOrThrow("mime_type");
                while (query.moveToNext()) {
                    VideoInfo videoInfo = new VideoInfo();
                    long j = query.getLong(columnIndexOrThrow);
                    Log.e("TAG", "onSuccess: "+query.getString(columnIndexOrThrow2));
                }
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return new ArrayList(hashSet);
    }

    public List<VideoFolder> getAllVideoOfFolder() {
        ArrayList arrayList = new ArrayList();
        HashMap hashMap = new HashMap();
        Cursor query = this.mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"bucket_id", "bucket_display_name", "_id", "_data", "_display_name", "duration", "_size", "resolution", "date_modified", "mime_type"}, null, null, "date_modified DESC");
        if (query != null) {
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("bucket_id");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("bucket_display_name");
                int columnIndexOrThrow3 = query.getColumnIndexOrThrow("_id");
                int columnIndexOrThrow4 = query.getColumnIndexOrThrow("_data");
                int columnIndexOrThrow5 = query.getColumnIndexOrThrow("_display_name");
                int columnIndexOrThrow6 = query.getColumnIndexOrThrow("duration");
                int columnIndexOrThrow7 = query.getColumnIndexOrThrow("_size");
                int columnIndexOrThrow8 = query.getColumnIndexOrThrow("resolution");
                int columnIndexOrThrow9 = query.getColumnIndexOrThrow("date_modified");
                int columnIndexOrThrow10 = query.getColumnIndexOrThrow("mime_type");
                while (query.moveToNext()) {
                    String string = query.getString(columnIndexOrThrow);
                    VideoInfo videoInfo = new VideoInfo();
                    long j = query.getLong(columnIndexOrThrow3);
                    videoInfo.setVideoId(j);
                    videoInfo.setUri(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, j).toString());
                    videoInfo.setPath(query.getString(columnIndexOrThrow4));
                    videoInfo.setDisplayName(query.getString(columnIndexOrThrow5));
                    videoInfo.setDuration(query.getLong(columnIndexOrThrow6));
                    videoInfo.setSize(query.getLong(columnIndexOrThrow7));
                    videoInfo.setResolution(query.getString(columnIndexOrThrow8));
                    videoInfo.setDate(1000 * query.getLong(columnIndexOrThrow9));
                    videoInfo.setMimeType(query.getString(columnIndexOrThrow10));
                    if (!hashMap.containsKey(string)) {
                        VideoFolder videoFolder = new VideoFolder();
                        videoFolder.setId(string);
                        columnIndexOrThrow2 = columnIndexOrThrow2;
                        videoFolder.setFolderName(query.getString(columnIndexOrThrow2));
                        videoFolder.addVideo(videoInfo);
                        hashMap.put(string, videoFolder);
                    } else {
                        columnIndexOrThrow2 = columnIndexOrThrow2;
                        VideoFolder videoFolder2 = (VideoFolder) hashMap.get(string);
                        if (videoFolder2 != null) {
                            videoFolder2.addVideo(videoInfo);
                        }
                    }
                    columnIndexOrThrow = columnIndexOrThrow;
                }
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            arrayList.add(((Map.Entry) iterator.next()).getValue());
        }
        return arrayList;
    }

    public List<VideoSubtitle> getAllSubFile() {
        ArrayList arrayList = new ArrayList();
        Cursor query = this.mContext.getContentResolver().query(MediaStore.Files.getContentUri("external"), null, "_data LIKE '%.srt'", null, "_id DESC");
        if (query != null) {
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("title");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("_data");
                while (query.moveToNext()) {
                    VideoSubtitle videoSubtitle = new VideoSubtitle(query.getString(columnIndexOrThrow2), query.getString(columnIndexOrThrow));
                    arrayList.add(videoSubtitle);
                    Log.d("binhnk08", "videoSubtitle = " + videoSubtitle);
                }
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return arrayList;
    }
}
