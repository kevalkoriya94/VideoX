package com.videoplayer.videox.db.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.videoplayer.videox.uti.SharPrefUti;

import java.util.HashSet;
import java.util.Set;


public class VideoFavoriteUtil {
    public static Set<Long> getAllFavoriteVideoId(Context context) {
        String string = SharPrefUti.getString(context, "FAVORITE_VIDEO_ID", "");
        return TextUtils.isEmpty(string) ? new HashSet() : (Set) new Gson().fromJson(string, new TypeToken<Set<Long>>() {
        }.getType());
    }

    public static void setFavoriteVideoId(Context context, Set<Long> set) {
        putFavoriteVideoListId(context, set);
    }

    private static void putFavoriteVideoListId(Context context, Set<Long> set) {
        SharPrefUti.putString(context, "FAVORITE_VIDEO_ID", new Gson().toJson(set, new TypeToken<Set<Long>>() {
        }.getType()));
    }

    public static void addFavoriteVideoId(Context context, long j, boolean z) {
        Set<Long> allFavoriteVideoId = getAllFavoriteVideoId(context);
        if (z) {
            allFavoriteVideoId.add(j);
        } else {
            allFavoriteVideoId.remove(j);
        }
        putFavoriteVideoListId(context, allFavoriteVideoId);
    }

    public static boolean checkFavoriteVideoIdExisted(Context context, long j) {
        return getAllFavoriteVideoId(context).contains(j);
    }
}
