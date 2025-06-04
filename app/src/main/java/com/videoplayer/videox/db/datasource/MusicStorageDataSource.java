package com.videoplayer.videox.db.datasource;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.videoplayer.videox.db.entity.music.MusicAlbum;
import com.videoplayer.videox.db.entity.music.MusicArtist;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.uti.SharPrefUti;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MusicStorageDataSource {
    private final Context mContext;

    public MusicStorageDataSource(Context context) {
        this.mContext = context;
    }

    public List<MusicInfo> getAllMusicFromStorage() {
        int totalsize = 0;
        int size;
        Uri uri;
        MusicDatabaseControl.getInstance().clearAll();
        HashSet hashSet = new HashSet();
        String[] strArr = {"_id", "_display_name", "artist", "album", "_data", "duration", "date_added", "_size"};
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Audio.Media.getContentUri("external");
        } else {
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor query = this.mContext.getContentResolver().query(uri, strArr, null, null, null);
        if (query != null) {
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("_id");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("_display_name");
                int columnIndexOrThrow3 = query.getColumnIndexOrThrow("artist");
                int columnIndexOrThrow4 = query.getColumnIndexOrThrow("album");
                int columnIndexOrThrow5 = query.getColumnIndexOrThrow("_data");
                int columnIndexOrThrow6 = query.getColumnIndexOrThrow("duration");
                int columnIndexOrThrow7 = query.getColumnIndexOrThrow("date_added");
                int columnIndexOrThrow8 = query.getColumnIndexOrThrow("_size");
                while (query.moveToNext()) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setId(query.getLong(columnIndexOrThrow));
                    musicInfo.setPath(query.getString(columnIndexOrThrow5));
                    musicInfo.setDisplayName(query.getString(columnIndexOrThrow2));
                    musicInfo.setArtist(query.getString(columnIndexOrThrow3));
                    musicInfo.setAlbum(query.getString(columnIndexOrThrow4));
                    musicInfo.setDuration(query.getLong(columnIndexOrThrow6));
                    musicInfo.setDate(query.getLong(columnIndexOrThrow7));
                    musicInfo.setSize(query.getLong(columnIndexOrThrow8));
                    hashSet.add(musicInfo);
                    MusicDatabaseControl.getInstance().addMusic(musicInfo);

                    size = Math.toIntExact(query.getLong(columnIndexOrThrow8));
                    totalsize = size + totalsize;
                }
                SharPrefUti.putTotalSizeAudio(this.mContext, totalsize);
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
        for (MusicInfo musicInfo2 : getAllOtherMusicFile()) {
            if (MusicDatabaseControl.getInstance().getMusicById(musicInfo2.getId()) == null) {
                hashSet.add(musicInfo2);
                MusicDatabaseControl.getInstance().addMusic(musicInfo2);
            }
        }
        return new ArrayList(hashSet);
    }

    public List<MusicAlbum> getAllMusicAlbum() {
        HashSet hashSet = null;
        try {
            Uri uri;
            hashSet = new HashSet();
            if (Build.VERSION.SDK_INT >= 29) {
                uri = MediaStore.Audio.Albums.getContentUri("external");
            } else {
                uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
            }
            Cursor query = this.mContext.getContentResolver().query(uri, new String[]{"_id", "album", "artist", "numsongs", "maxyear"}, null, null, "album ASC");
            if (query != null) {
                try {
                    int columnIndexOrThrow = query.getColumnIndexOrThrow("_id");
                    int columnIndexOrThrow2 = query.getColumnIndexOrThrow("album");
                    int columnIndexOrThrow3 = query.getColumnIndexOrThrow("artist");
                    int columnIndexOrThrow4 = query.getColumnIndexOrThrow("numsongs");
                    int columnIndexOrThrow5 = query.getColumnIndexOrThrow("maxyear");
                    while (query.moveToNext()) {
                        MusicAlbum musicAlbum = new MusicAlbum();
                        musicAlbum.setAlbumId(query.getLong(columnIndexOrThrow));
                        musicAlbum.setAlbumName(query.getString(columnIndexOrThrow2));
                        musicAlbum.setArtistName(query.getString(columnIndexOrThrow3));
                        musicAlbum.setNumberOfSongs(query.getLong(columnIndexOrThrow4));
                        musicAlbum.setLastYear(query.getLong(columnIndexOrThrow5));
                        Log.d("binhnk08", "musicAlbum = " + musicAlbum);
                        hashSet.add(musicAlbum);
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
        } catch (Exception e) {
        }
        return new ArrayList(hashSet);
    }

    public List<MusicInfo> getAllMusicFromAlbum(long j) {
        Uri uri;
        HashSet hashSet = new HashSet();
        Log.d("binhnk08", "getAllMusicFromAlbum albumId = " + j);
        String[] strArr = {String.valueOf(j)};
        String[] strArr2 = {"_id", "_display_name", "artist", "album", "_data", "duration", "date_added", "_size"};
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Audio.Media.getContentUri("external");
        } else {
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor query = this.mContext.getContentResolver().query(uri, strArr2, "album_id = ? AND _data NOT NULL", strArr, "_display_name ASC");
        if (query != null) {
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("_id");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("_display_name");
                int columnIndexOrThrow3 = query.getColumnIndexOrThrow("artist");
                int columnIndexOrThrow4 = query.getColumnIndexOrThrow("album");
                int columnIndexOrThrow5 = query.getColumnIndexOrThrow("_data");
                int columnIndexOrThrow6 = query.getColumnIndexOrThrow("duration");
                int columnIndexOrThrow7 = query.getColumnIndexOrThrow("date_added");
                int columnIndexOrThrow8 = query.getColumnIndexOrThrow("_size");
                while (query.moveToNext()) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setId(query.getLong(columnIndexOrThrow));
                    musicInfo.setPath(query.getString(columnIndexOrThrow5));
                    musicInfo.setDisplayName(query.getString(columnIndexOrThrow2));
                    musicInfo.setArtist(query.getString(columnIndexOrThrow3));
                    musicInfo.setAlbum(query.getString(columnIndexOrThrow4));
                    musicInfo.setDuration(query.getLong(columnIndexOrThrow6));
                    musicInfo.setDate(query.getLong(columnIndexOrThrow7));
                    musicInfo.setSize(query.getLong(columnIndexOrThrow8));
                    hashSet.add(musicInfo);
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

    public List<MusicArtist> getAllMusicArtist() {
        Log.d("binhnk08", "getAllMusicArtist");
        HashSet hashSet = new HashSet();
        Cursor query = this.mContext.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, new String[]{"_id", "artist"}, null, null, null);
        if (query != null) {
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("_id");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("artist");
                while (query.moveToNext()) {
                    MusicArtist musicArtist = new MusicArtist();
                    musicArtist.setArtistId(query.getLong(columnIndexOrThrow));
                    musicArtist.setArtistName(query.getString(columnIndexOrThrow2));
                    musicArtist.setMusicList(getAllSongsOfArtist(query.getLong(columnIndexOrThrow)));
                    hashSet.add(musicArtist);
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

    public List<MusicInfo> getAllSongsOfArtist(long j) {
        Uri uri;
        HashSet hashSet = new HashSet();
        String[] strArr = {String.valueOf(j)};
        String[] strArr2 = {"_id", "_display_name", "artist", "album", "_data", "duration", "date_added", "_size"};
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Audio.Media.getContentUri("external");
        } else {
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        Cursor query = this.mContext.getContentResolver().query(uri, strArr2, "artist_id = ? AND _data NOT NULL", strArr, "_display_name ASC");
        if (query != null) {
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("_id");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("_display_name");
                int columnIndexOrThrow3 = query.getColumnIndexOrThrow("artist");
                int columnIndexOrThrow4 = query.getColumnIndexOrThrow("album");
                int columnIndexOrThrow5 = query.getColumnIndexOrThrow("_data");
                int columnIndexOrThrow6 = query.getColumnIndexOrThrow("duration");
                int columnIndexOrThrow7 = query.getColumnIndexOrThrow("date_added");
                int columnIndexOrThrow8 = query.getColumnIndexOrThrow("_size");
                while (query.moveToNext()) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setId(query.getLong(columnIndexOrThrow));
                    musicInfo.setPath(query.getString(columnIndexOrThrow5));
                    musicInfo.setDisplayName(query.getString(columnIndexOrThrow2));
                    musicInfo.setArtist(query.getString(columnIndexOrThrow3));
                    musicInfo.setAlbum(query.getString(columnIndexOrThrow4));
                    musicInfo.setDuration(query.getLong(columnIndexOrThrow6));
                    musicInfo.setDate(query.getLong(columnIndexOrThrow7));
                    musicInfo.setSize(query.getLong(columnIndexOrThrow8));
                    hashSet.add(musicInfo);
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

    public List<MusicAlbum> getAllAlbumOfArtist(MusicArtist musicArtist) {
        Uri uri;
        HashSet hashSet = new HashSet();
        String[] strArr = {String.valueOf(musicArtist.getArtistId())};
        if (Build.VERSION.SDK_INT >= 29) {
            uri = MediaStore.Audio.Albums.getContentUri("external");
        } else {
            uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        }
        Cursor query = this.mContext.getContentResolver().query(uri, new String[]{"_id", "album", "artist", "numsongs", "maxyear"}, "artist_id = ?", strArr, "album ASC");
        if (query != null) {
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("_id");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("album");
                int columnIndexOrThrow3 = query.getColumnIndexOrThrow("artist");
                int columnIndexOrThrow4 = query.getColumnIndexOrThrow("numsongs");
                int columnIndexOrThrow5 = query.getColumnIndexOrThrow("maxyear");
                while (query.moveToNext()) {
                    MusicAlbum musicAlbum = new MusicAlbum();
                    musicAlbum.setAlbumId(query.getLong(columnIndexOrThrow));
                    musicAlbum.setAlbumName(query.getString(columnIndexOrThrow2));
                    musicAlbum.setArtistName(query.getString(columnIndexOrThrow3));
                    musicAlbum.setNumberOfSongs(query.getLong(columnIndexOrThrow4));
                    musicAlbum.setLastYear(query.getLong(columnIndexOrThrow5));
                    hashSet.add(musicAlbum);
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

    public List<MusicInfo> getAllOtherMusicFile() {
        HashSet hashSet = new HashSet();
        Cursor query = this.mContext.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_display_name", "_data", "date_added", "_size"}, "_data LIKE '%.mp3' OR _data LIKE '%.ac3'", null, "_id DESC");
        if (query != null) {
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("_id");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("_display_name");
                int columnIndexOrThrow3 = query.getColumnIndexOrThrow("_data");
                int columnIndexOrThrow4 = query.getColumnIndexOrThrow("date_added");
                int columnIndexOrThrow5 = query.getColumnIndexOrThrow("_size");
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                while (query.moveToNext()) {
                    String string = query.getString(columnIndexOrThrow3);
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setId(query.getLong(columnIndexOrThrow));
                    musicInfo.setPath(string);
                    musicInfo.setDisplayName(query.getString(columnIndexOrThrow2));
                    musicInfo.setDate(query.getLong(columnIndexOrThrow4));
                    musicInfo.setSize(query.getLong(columnIndexOrThrow5));
                    try {
                        mediaMetadataRetriever.setDataSource(string);
                        musicInfo.setDuration(Long.parseLong(mediaMetadataRetriever.extractMetadata(9)));
                        musicInfo.setAlbum(mediaMetadataRetriever.extractMetadata(1));
                        musicInfo.setArtist(mediaMetadataRetriever.extractMetadata(2));
                    } catch (Exception unused) {
                    }
                    Log.d("binhnk08", "music = " + musicInfo);
                    hashSet.add(musicInfo);
                }
                mediaMetadataRetriever.release();
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                try {
                    throw th;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (query != null) {
            query.close();
        }
        return new ArrayList(hashSet);
    }
}
