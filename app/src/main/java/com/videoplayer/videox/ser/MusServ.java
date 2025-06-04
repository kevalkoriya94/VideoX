package com.videoplayer.videox.ser;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.MusPlayActivity;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.pre.mus.MusPlaPre;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.uti.mus.MusPlUti;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class MusServ extends Service {
    public static final int PAUSE = 2;
    public static final int PLAY = 1;
    private OnSongCallBack callBack;
    private MusicInfo currentSong;
    private int index;
    private MediaSessionCompat mediaSession;
    private MusPlaPre musicPlayerPresenter;
    private int state;
    private final IBinder iBinder = new MyBinder();
    public MediaPlayer player = new MediaPlayer();
    private final List<MusicInfo> musicInfoList = new ArrayList();
    private final List<Long> musicIdList = new ArrayList();
    private boolean isShuffle = false;
    private int repeatState = -1;
    private final ArrayList<Integer> indexShuffle = new ArrayList<>();
    private final BroadcastReceiver receiver = new BroadcastReceiver() { // from class: com.videoplayer.videox.ser.MusServ.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String string;
            if (intent.getExtras() == null || (string = intent.getExtras().getString("actionname")) == null) {
                return;
            }
            string.hashCode();
            string.hashCode();
            switch (string) {
                case "ACTION_NEXT":
                    MusServ.this.nextSong();
                    break;
                case "ACTION_PLAY":
                    MusServ.this.playSong();
                    break;
                case "ACTION_PREV":
                    MusServ.this.backSong();
                    break;
                case "ACTION_CLOSE":
                    MusServ.this.stopService();
                    break;
            }
        }
    };
    private final BroadcastReceiver stopMusicReceiver = new BroadcastReceiver() { // from class: com.videoplayer.videox.ser.MusServ.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            MusServ.this.stopService();
        }
    };

    public interface OnSongCallBack {
        void notifyError(Exception exc);

        void onStopService();

        void updatePlayPauseState(boolean z);

        void updateUISong(MusicInfo musicInfo, int i);
    }

    public void setMusicInfoList(List<MusicInfo> list) {
        this.musicInfoList.clear();
        this.musicInfoList.addAll(list);
        this.musicIdList.clear();
        Iterator<MusicInfo> it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            this.musicIdList.add(Long.valueOf(it.next().getId()));
            this.indexShuffle.add(Integer.valueOf(i));
            i++;
        }
        Collections.shuffle(this.indexShuffle);
    }

    public List<Long> getMusicIdList() {
        return this.musicIdList;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public void setState(int i) {
        this.state = i;
    }

    public void setCallBack(OnSongCallBack onSongCallBack) {
        this.callBack = onSongCallBack;
    }

    public void playSong() {
        int i = this.state;
        if (i == 1) {
            MediaPlayer mediaPlayer = this.player;
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
            this.state = 2;
            OnSongCallBack onSongCallBack = this.callBack;
            if (onSongCallBack != null) {
                onSongCallBack.updatePlayPauseState(false);
            }
        } else if (i == 2) {
            MediaPlayer mediaPlayer2 = this.player;
            if (mediaPlayer2 != null) {
                mediaPlayer2.start();
            }
            sendBroadcastPauseVideo();
            this.state = 1;
            OnSongCallBack onSongCallBack2 = this.callBack;
            if (onSongCallBack2 != null) {
                onSongCallBack2.updatePlayPauseState(true);
            }
        }
        showNotification();
    }

    public void playMusic() {
        try {
            this.state = 1;
            MediaPlayer mediaPlayer = this.player;
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                int i = this.index;
                if (i < 0 || i >= this.musicInfoList.size()) {
                    return;
                }
                MusicInfo musicInfo = this.musicInfoList.get(this.index);
                this.currentSong = musicInfo;
                if (TextUtils.isEmpty(musicInfo.getPath())) {
                    this.player.setDataSource(this, Uri.parse(this.currentSong.getUri()));
                } else {
                    this.player.setDataSource(this.currentSong.getPath());
                    this.musicPlayerPresenter.updateMusicHistoryData(this.currentSong);
                }
                this.player.prepare();
                this.player.start();
                sendBroadcastPauseVideo();
                return;
            }
            OnSongCallBack onSongCallBack = this.callBack;
            if (onSongCallBack != null) {
                onSongCallBack.updateUISong(this.currentSong, this.index);
            }
            showNotification();
        } catch (Exception e) {
            e.printStackTrace();
            OnSongCallBack onSongCallBack2 = this.callBack;
            if (onSongCallBack2 != null) {
                onSongCallBack2.notifyError(e);
            }
        }
    }

    public void nextSong() {
        if (this.index >= this.musicInfoList.size() - 1) {
            this.index = -1;
        }
        int i = this.index + 1;
        this.index = i;
        if (this.isShuffle && i >= 0 && i < this.indexShuffle.size()) {
            this.index = this.indexShuffle.get(this.index).intValue();
        }
        playMusic();
    }

    public void backSong() {
        if (this.index == 0) {
            this.index = this.musicInfoList.size();
        }
        int i = this.index - 1;
        this.index = i;
        if (this.isShuffle && i >= 0 && i < this.indexShuffle.size()) {
            this.index = this.indexShuffle.get(this.index).intValue();
        }
        playMusic();
    }

    public void stopService() {
        sendBroadcastCurrentMusic(null);
        OnSongCallBack onSongCallBack = this.callBack;
        if (onSongCallBack != null) {
            onSongCallBack.onStopService();
        }
        stopSelf();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.cancel(1);
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (this.player == null) {
            this.player = new MediaPlayer();
        }
        final SettingPrefUtils settingPrefUtils = new SettingPrefUtils(this);
        this.player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.videoplayer.videox.ser.MusServ$$ExternalSyntheticLambda0
            @Override // android.media.MediaPlayer.OnCompletionListener
            public final void onCompletion(MediaPlayer mediaPlayer) {
                MusServ.this.m914lambda$onCreate$0$comvideoplayervideoxserMusServ(settingPrefUtils, mediaPlayer);
            }
        });
        this.musicPlayerPresenter = new MusPlaPre(new MusicDataRepository(this));
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "play audio");
        this.mediaSession = mediaSessionCompat;
        mediaSessionCompat.setMetadata(MediaMetadataCompat.fromMediaMetadata(new MediaMetadata.Builder().putLong(MediaMetadataCompat.METADATA_KEY_DURATION, -1L).build()));
        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(this.receiver, new IntentFilter("MediaPlayer"), 2);
        } else {
            registerReceiver(this.receiver, new IntentFilter("MediaPlayer"));
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(this.stopMusicReceiver, new IntentFilter("RECEIVER_STOP_MUSIC"));
    }

    /* renamed from: onCreate0MusicService, reason: merged with bridge method [inline-methods] */
    public void m914lambda$onCreate$0$comvideoplayervideoxserMusServ(SettingPrefUtils settingPrefUtils, MediaPlayer mediaPlayer) {
        int i = this.repeatState;
        if (i != 0) {
            if (i == 1) {
                playMusic();
                return;
            }
            if (i != 2) {
                return;
            }
            if (!settingPrefUtils.isAutoPlayNextMusic()) {
                this.player.pause();
                this.state = 2;
                OnSongCallBack onSongCallBack = this.callBack;
                if (onSongCallBack != null) {
                    onSongCallBack.updatePlayPauseState(false);
                }
                showNotification();
                return;
            }
            nextSong();
            return;
        }
        if (!settingPrefUtils.isAutoPlayNextMusic()) {
            this.player.pause();
            this.state = 2;
            OnSongCallBack onSongCallBack2 = this.callBack;
            if (onSongCallBack2 != null) {
                onSongCallBack2.updatePlayPauseState(false);
            }
            showNotification();
            return;
        }
        if (this.isShuffle || this.index != this.musicInfoList.size() - 1) {
            nextSong();
            return;
        }
        this.player.pause();
        this.state = 2;
        OnSongCallBack onSongCallBack3 = this.callBack;
        if (onSongCallBack3 != null) {
            onSongCallBack3.updatePlayPauseState(false);
        }
        showNotification();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        MediaPlayer mediaPlayer = this.player;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        return this.iBinder;
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        createNotificationChanel();
        showNotification();
        return 1;
    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(MusNotRec.CHANNEL_ID, "Chanel", 4);
            notificationChannel.setDescription("Chanel Description");
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    public void showNotification() {
        MusicInfo currentSong = getCurrentSong();
        sendBroadcastCurrentMusic(currentSong);

        if (currentSong == null) {
            stopForeground(true);
            return;
        }

        // Choose play/pause icon based on state
        int playPauseIcon = (this.state == 1) ? R.drawable.baseline_pause_circle_24 : R.drawable.ic_noti_play;

        // Intent to launch music player activity
        Intent activityIntent = new Intent(this, MusPlayActivity.class);
        activityIntent.putExtra(AppCon.IntentExtra.EXTRA_FROM_NOTIFICATION, true);
        activityIntent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_NUMBER, this.index);
        activityIntent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_SONG, this.currentSong);
        activityIntent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_ARRAY, new ArrayList<>(this.musicIdList));
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE
        );

        // Broadcast intents for notification actions
        PendingIntent prevIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(this, MusNotRec.class).setAction(MusNotRec.ACTION_PREV),
                PendingIntent.FLAG_IMMUTABLE
        );

        PendingIntent playIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(this, MusNotRec.class).setAction(MusNotRec.ACTION_PLAY),
                PendingIntent.FLAG_IMMUTABLE
        );

        PendingIntent nextIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(this, MusNotRec.class).setAction(MusNotRec.ACTION_NEXT),
                PendingIntent.FLAG_IMMUTABLE
        );

        PendingIntent closeIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(this, MusNotRec.class).setAction(MusNotRec.ACTION_CLOSE),
                PendingIntent.FLAG_IMMUTABLE
        );

        try {
            Bitmap largeIcon = MusPlUti.getThumbnailOfSong(this, currentSong.getPath(), 80);

            Notification notification = new NotificationCompat.Builder(this, MusNotRec.CHANNEL_ID)
                    .setContentTitle(currentSong.getDisplayName())
                    .setContentText(currentSong.getArtist())
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.ic_noti_play)
                    .setContentIntent(contentIntent)
                    .addAction(R.drawable.ic_noti_prev, "Previous", prevIntent)
                    .addAction(playPauseIcon, "Play/Pause", playIntent)
                    .addAction(R.drawable.ic_noti_next, "Next", nextIntent)
                    .addAction(R.drawable.ic_noti_close, "Close", closeIntent)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(this.mediaSession.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setOnlyAlertOnce(true)
                    .build();

            startForeground(1, notification);

        } catch (Exception e) {
            e.printStackTrace(); // Or use Log.e(TAG, "Notification error", e);
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        MediaPlayer mediaPlayer = this.player;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        unregisterReceiver(this.receiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.stopMusicReceiver);
        super.onDestroy();
    }

    public int getCurrentSeek() {
        MediaPlayer mediaPlayer = this.player;
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getTotalDuration() {
        MediaPlayer mediaPlayer = this.player;
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    public void seekTo(int i) {
        MediaPlayer mediaPlayer = this.player;
        if (mediaPlayer != null) {
            try {
                mediaPlayer.seekTo(i);
            } catch (Exception e) {
                OnSongCallBack onSongCallBack = this.callBack;
                if (onSongCallBack != null) {
                    onSongCallBack.notifyError(e);
                }
            }
        }
    }

    public MusicInfo getCurrentSong() {
        return this.currentSong;
    }

    public boolean isSongPlaying() {
        MediaPlayer mediaPlayer = this.player;
        return (mediaPlayer == null || !mediaPlayer.isPlaying() || this.currentSong == null) ? false : true;
    }

    public int getSessionIdMusic() {
        MediaPlayer mediaPlayer = this.player;
        if (mediaPlayer != null) {
            return mediaPlayer.getAudioSessionId();
        }
        return -1;
    }

    public boolean isShuffle() {
        return this.isShuffle;
    }

    public void setShuffle(boolean z) {
        this.isShuffle = z;
    }

    public int getRepeatState() {
        return this.repeatState;
    }

    public void setRepeatState(int i) {
        this.repeatState = i;
    }

    private void sendBroadcastCurrentMusic(MusicInfo musicInfo) {
        Intent intent = new Intent("RECEIVER_CURRENT_MUSIC");
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_SONG, musicInfo);
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_PLAYING, this.state == 1);
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_NUMBER, this.index);
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_ARRAY, (ArrayList) this.musicIdList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendBroadcastPauseVideo() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("RECEIVER_STOP_VIDEO"));
    }

    public class MyBinder extends Binder {
        public MyBinder() {
        }

        public MusServ getService() {
            return MusServ.this;
        }
    }
}
