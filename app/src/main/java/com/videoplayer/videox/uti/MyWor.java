package com.videoplayer.videox.uti;

import android.content.Context;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class MyWor extends Worker {
    private static final String TAG = "MyWorker";

    public MyWor(Context appContext, WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @Override // androidx.work.Worker
    public Result doWork() {
        Log.d(TAG, "Performing long running task in scheduled job");
        return Result.success();
    }
}
