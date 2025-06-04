package com.videoplayer.videox.uti.thre;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThrExeWithP {
    private static final int CORE_POOL_SIZE = 5;
    private static final int KEEP_ALIVE_TIME = 120;
    private static final int MAX_POOL_SIZE = 5;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> WORK_QUEUE = new LinkedBlockingQueue();
    private final ThreadPoolExecutor mThreadPoolExecutor;

    private ThrExeWithP() {
        this.mThreadPoolExecutor = new ThreadPoolExecutor(5, 5, 120L, TIME_UNIT, WORK_QUEUE);
    }

    public void execute(Runnable runnable) {
        this.mThreadPoolExecutor.submit(runnable);
    }

    private static class ThrExeHolder {
        private static final ThrExeWithP INSTANCE = new ThrExeWithP();

        private ThrExeHolder() {
        }
    }

    public static ThrExeWithP getInstance() {
        return ThrExeHolder.INSTANCE;
    }
}
