package com.videoplayer.videox.uti.thre;

import android.os.Looper;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThrMan {
    private ExecutorService mThrExe;

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public ExecutorService getExecutorService(int i) {
        ExecutorService executorService = this.mThrExe;
        if (executorService == null || executorService.isShutdown()) {
            StringBuilder sb = new StringBuilder("getExecutorService() ] Create new FixedThreadPool(");
            sb.append(i > 0 ? i : 1);
            sb.append(")");
            Log.d("binhnk08", sb.toString());
            if (i <= 0) {
                i = 1;
            }
            this.mThrExe = Executors.newFixedThreadPool(i);
        }
        return this.mThrExe;
    }

    public void shutdownThrExe() {
        try {
            ExecutorService executorService = this.mThrExe;
            if (executorService == null || executorService.isShutdown()) {
                return;
            }
            this.mThrExe.shutdownNow();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public boolean canExecute() {
        boolean isShutdown = this.mThrExe.isShutdown();
        boolean isTerminated = this.mThrExe.isTerminated();
        if (!isShutdown && !isTerminated) {
            return true;
        }
        Log.d("binhnk08", "mThrExe is shutdown or terminated. isShutdown :" + isShutdown + ",isTerminated :" + isTerminated);
        return false;
    }
}
