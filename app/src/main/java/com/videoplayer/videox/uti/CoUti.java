package com.videoplayer.videox.uti;

import android.content.Context;
import android.text.TextUtils;

import com.appizona.yehiahd.fastsave.FastSave;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CoUti {
    public static Set<Integer> getAllThemeRedeem(Context context) {
        String string = SharPrefUti.getString(context, "THEME_REDEEM", "");
        return TextUtils.isEmpty(string) ? new HashSet() : (Set) new Gson().fromJson(string, new TypeToken<Set<Integer>>() { // from class: com.videoplayer.videox.uti.CoUti.1
        }.getType());
    }

    public static void addThemeRedeem(Context context, int i) {
        Set<Integer> allThemeRedeem = getAllThemeRedeem(context);
        allThemeRedeem.add(Integer.valueOf(i));
        SharPrefUti.putString(context, "THEME_REDEEM", new Gson().toJson(allThemeRedeem, new TypeToken<Set<Integer>>() { // from class: com.videoplayer.videox.uti.CoUti.2
        }.getType()));
    }

    public static boolean checkThemeRedeem(Context context, int i) {
        return getAllThemeRedeem(context).contains(Integer.valueOf(i));
    }

    public static List<Integer> getAllThemeNeedRedeemInShop() {
        ArrayList arrayList = new ArrayList();
        int length = AppCon.Themes.THEMES.length;
        for (int i = 0; i < length; i++) {
            if (AppCon.Themes.THEMES_COIN[i] > 0) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        return arrayList;
    }

    public static int getTotalCoin(Context context) {
        return SharPrefUti.getInt(context, "NUMBER_COIN", 0);
    }

    public static void setTotalCoin(Context context, int i) {
        SharPrefUti.putInt(context, "NUMBER_COIN", i);
    }

    public static void redeem7DayPremium(Context context) {
        setTimePremium(context, getTimePremium(context) + 604800000);
    }

    public static void setTimePremium(Context context, long j) {
        Utility.sTimeExpires = j;
        SharPrefUti.putLong(context, "TIME_PREMIUM", j);
    }

    public static long getTimePremium(Context context) {
        return Math.max(SharPrefUti.getLong(context, "TIME_PREMIUM", 0L), System.currentTimeMillis());
    }

    public static boolean isPremium() {
        return FastSave.getInstance().getBoolean(AppCon.REMOVE_ADS_KEY, false);
    }

    public static boolean checkTimeShowFloatingButtonGift(Context context) {
        return System.currentTimeMillis() - SharPrefUti.getLong(context, "TIME_FLOATING_GIFT_BUTTON", 0L) > 54000000;
    }

    public static void putTimeShowFloatingButtonGift(Context context) {
        SharPrefUti.putLong(context, "TIME_FLOATING_GIFT_BUTTON", System.currentTimeMillis());
    }
}
