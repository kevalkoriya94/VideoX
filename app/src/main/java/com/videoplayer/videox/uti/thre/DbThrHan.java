package com.videoplayer.videox.uti.thre;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;


public class DbThrHan {
    private static final String TAG = "DatabaseThreadHandler";
    private final Handler mDatabaseHandler;

    private static class InstanceHolder {
        private static final DbThrHan INSTANCE = new DbThrHan();

        private InstanceHolder() {
        }
    }

    private DbThrHan() {
        HandlerThread handlerThread = new HandlerThread("database-thread");
        handlerThread.start();
        this.mDatabaseHandler = new Handler(handlerThread.getLooper());
    }

    public static DbThrHan getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void run(Runnable runnable) {
        if (ThrMan.isMainThread()) {
            this.mDatabaseHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public <T> T run(Callable<T> callable) {
        return (T) run(callable, null);
    }

    public <T> T run(Callable<T> callable, T t) {
        T call;
        long currentTimeMillis = System.currentTimeMillis();
        try {
            if (ThrMan.isMainThread()) {
                FutureTask futureTask = new FutureTask(callable);
                this.mDatabaseHandler.post(futureTask);
                call = (T) futureTask.get(5000L, TimeUnit.MILLISECONDS);
            } else {
                call = callable.call();
            }
            t = call;
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            if (currentTimeMillis2 >= 5000) {
                Log.w(TAG, "Execution Time = " + currentTimeMillis2 + "Thread Info : id = " + Thread.currentThread().getId() + " name = " + Thread.currentThread().getName());
            }
        } catch (Exception e) {
            long currentTimeMillis3 = System.currentTimeMillis() - currentTimeMillis;
            int i = (currentTimeMillis3 > 5000L ? 1 : (currentTimeMillis3 == 5000L ? 0 : -1));
            Log.w(TAG, "Execution Time = " + currentTimeMillis3 + "Thread Info : id = " + Thread.currentThread().getId() + " name = " + Thread.currentThread().getName());
            e.printStackTrace();
        } catch (Throwable th) {
            long currentTimeMillis4 = System.currentTimeMillis() - currentTimeMillis;
            if (currentTimeMillis4 >= 5000) {
                Log.w(TAG, "Execution Time = " + currentTimeMillis4 + "Thread Info : id = " + Thread.currentThread().getId() + " name = " + Thread.currentThread().getName());
            }
            throw th;
        }
        return t;
    }
}
