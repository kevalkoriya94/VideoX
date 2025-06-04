package com.videoplayer.videox.ser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class MusNotRec extends BroadcastReceiver {
    public static final String ACTION_CLOSE = "ACTION_CLOSE";
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PREV = "ACTION_PREV";
    public static final String CHANNEL_ID = "CHANNEL_ID";

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Intent intent2 = new Intent("MediaPlayer");
        intent2.putExtra("actionname", intent.getAction());
        context.sendBroadcast(intent2);
    }
}
