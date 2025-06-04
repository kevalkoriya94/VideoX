package com.videoplayer.videox.db.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.videoplayer.videox.db.dao.music.MusicHistoryDAO;
import com.videoplayer.videox.db.dao.music.MusicPlaylistDAO;
import com.videoplayer.videox.db.dao.video.VideoHistoryDAO;
import com.videoplayer.videox.db.dao.video.VideoPlaylistDAO;
import com.videoplayer.videox.db.entity.music.MusicHistory;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.music.MusicPlaylist;
import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.entity.video.VideoPlaylist;


@Database(
        entities = {
                MusicHistory.class,
                MusicInfo.class,
                VideoHistory.class,
                VideoPlaylist.class,
                MusicPlaylist.class
        },
        version = 1,
        exportSchema = false
)
public abstract class MyDatabase extends RoomDatabase {
    private static volatile MyDatabase sInstance;

    public abstract MusicHistoryDAO musicHistoryDAO();
    public abstract MusicPlaylistDAO musicPlaylistDAO();
    public abstract VideoHistoryDAO videoHistoryDAO();
    public abstract VideoPlaylistDAO videoPlaylistDAO();

    public static MyDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MyDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    MyDatabase.class,
                                    "MyDatabase.db"
                            )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Optional: Add your initialization logic here (e.g., insert default values)
                                }
                            })
                            .build();
                }
            }
        }
        return sInstance;
    }
}
