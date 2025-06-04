package com.videoplayer.videox.uti.down;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.videoplayer.videox.uti.SharPrefUti;

import java.util.HashSet;
import java.util.Set;


public class WebViwHistUti {
    public static Set<String> getAllHistorySearch(Context context) {
        String string = SharPrefUti.getString(context, "HISTORY_SEARCH_WEB", "");
        return TextUtils.isEmpty(string) ? new HashSet() : (Set) new Gson().fromJson(string, new TypeToken<Set<String>>() { // from class: com.videoplayer.videox.uti.down.WebViwHistUti.1
        }.getType());
    }

    public static void putHistorySearch(Context context, Set<String> set) {
        SharPrefUti.putString(context, "HISTORY_SEARCH_WEB", new Gson().toJson(set, new TypeToken<Set<String>>() { // from class: com.videoplayer.videox.uti.down.WebViwHistUti.2
        }.getType()));
    }

    public static void addHistorySearch(Context context, String str) {
        Set<String> allHistorySearch = getAllHistorySearch(context);
        if (!TextUtils.isEmpty(str)) {
            if (str.startsWith("https://www.")) {
                str = str.replace("https://www.", "");
            } else if (str.startsWith("http://www.")) {
                str = str.replace("http://www.", "");
            } else if (str.startsWith("https://m.")) {
                str = str.replace("https://m.", "");
            } else if (str.startsWith("http://m.")) {
                str = str.replace("http://m.", "");
            } else if (str.startsWith("https://")) {
                str = str.replace("https://", "");
            } else if (str.startsWith("http://")) {
                str = str.replace("http://", "");
            }
            if (!TextUtils.isEmpty(str) && str.endsWith("/")) {
                str = str.substring(0, str.length() - 1);
            }
        }
        allHistorySearch.remove(str);
        allHistorySearch.add(str);
        putHistorySearch(context, allHistorySearch);
    }
}
