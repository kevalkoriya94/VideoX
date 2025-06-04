package com.videoplayer.videox.uti.thre;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;


public class ThrExe {
    private static final Handler sMainHandler = new Handler(Looper.getMainLooper());

    public static void runOnMainThread(Runnable runnable) {
        runOnMainThread(runnable, false);
    }

    public static void runOnMainThread(Runnable runnable, boolean z) {
        if (!z) {
            sMainHandler.post(runnable);
        } else if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            sMainHandler.post(runnable);
        }
    }

    public static void runOnMainThread(Runnable runnable, long j) {
        sMainHandler.postDelayed(runnable, j);
    }

    public static void runOnDatabaseThread(Runnable runnable) {
        DbThrHan.getInstance().run(runnable);
    }

    public static <T> T runOnDatabaseThread(Callable<T> callable) {
        return (T) DbThrHan.getInstance().run(callable);
    }

    public static <T> T runOnDatabaseThread(Callable<T> callable, T t) {
        return (T) DbThrHan.getInstance().run(callable, t);
    }

    public static void runOnWorkThread(Runnable runnable) {
        new Thread(runnable).start();
    }
}
