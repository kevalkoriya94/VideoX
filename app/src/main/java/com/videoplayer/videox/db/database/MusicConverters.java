package com.videoplayer.videox.db.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.videoplayer.videox.db.entity.music.MusicInfo;

import java.util.ArrayList;
import java.util.List;


public class MusicConverters {
    @TypeConverter
    public static List<MusicInfo> fromString(String str) {
        if (str == null) {
            return null;
        }
        return (List) new Gson().fromJson(str, new TypeToken<ArrayList<MusicInfo>>() {
        }.getType());
    }

    @TypeConverter
    public static String fromArrayList(List<MusicInfo> list) {
        if (list == null) {
            return null;
        }
        return new Gson().toJson(list, new TypeToken<ArrayList<MusicInfo>>() {
        }.getType());
    }
}
