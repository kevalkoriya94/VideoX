package com.videoplayer.videox.db.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.videoplayer.videox.uti.SharPrefUti;

import java.util.HashSet;
import java.util.Set;


public class MusicFavoriteUtil {
    public static Set<Long> getAllFavoriteMusicId(Context context) {
        String string = SharPrefUti.getString(context, "FAVORITE_MUSIC_ID", "");
        return TextUtils.isEmpty(string) ? new HashSet() : (Set) new Gson().fromJson(string, new TypeToken<Set<Long>>() {
        }.getType());
    }

    public static void setFavoriteMusicId(Context context, Set<Long> set) {
        putFavoriteMusicListId(context, set);
    }

    private static void putFavoriteMusicListId(Context context, Set<Long> set) {
        SharPrefUti.putString(context, "FAVORITE_MUSIC_ID", new Gson().toJson(set, new TypeToken<Set<Long>>() {
        }.getType()));
    }

    public static void addFavoriteMusicId(Context context, long j, boolean z) {
        Set<Long> allFavoriteMusicId = getAllFavoriteMusicId(context);
        if (z) {
            allFavoriteMusicId.add(j);
        } else {
            allFavoriteMusicId.remove(j);
        }
        putFavoriteMusicListId(context, allFavoriteMusicId);
    }

    public static boolean checkFavoriteMusicIdExisted(Context context, long j) {
        return getAllFavoriteMusicId(context).contains(j);
    }
}
