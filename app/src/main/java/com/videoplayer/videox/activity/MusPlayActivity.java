package com.videoplayer.videox.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.facebook.ads.NativeAdLayout;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.NextInMusPlaAdapter;
import com.videoplayer.videox.dialog.ChaVolBtmSheet;
import com.videoplayer.videox.dialog.MusPlayNxtInDialBuil;
import com.videoplayer.videox.db.datasource.MusicDatabaseControl;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.utils.MusicFavoriteUtil;
import com.videoplayer.videox.ser.MusServ;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.uti.mus.MusPlUti;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MusPlayActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection, MusServ.OnSongCallBack {
    private ImageView btnPlayPause;
    private ImageView imgFavorite;
    private ImageView imgMusicArt;
    private ImageView imgRepeat;
    private ImageView imgShuffle;
    private LottieAnimationView ivHeart;
    private LottieAnimationView ivWaves;
    private int mPosition;
    private MusPlayNxtInDialBuil musicBuilder;
    private MusServ musicService;
    private SeekBar seekBar;
    private TextView tvArtist;
    private TextView tvCurrentTime;
    private TextView tvSong;
    private TextView tvTitle;
    private TextView tvTotalTime;
    private final List<MusicInfo> mSongList = new ArrayList();
    private Handler handler = new Handler();
    private final Runnable runnable = new Runnable() { // from class: com.videoplayer.videox.activity.MusPlayActivity.1
        @Override // java.lang.Runnable
        public void run() {
            Log.d("binhnk08 ", "startMediaController");
            if (MusPlayActivity.this.musicService != null) {
                int currentSeek = MusPlayActivity.this.musicService.getCurrentSeek();
                int totalDuration = MusPlayActivity.this.musicService.getTotalDuration();
                MusPlayActivity.this.seekBar.setProgress(currentSeek);
                MusPlayActivity.this.seekBar.setMax(totalDuration);
                MusPlayActivity.this.tvCurrentTime.setText(Utility.convertLongToDuration(currentSeek));
                MusPlayActivity.this.tvTotalTime.setText(Utility.convertLongToDuration(totalDuration));
                if (MusPlayActivity.this.handler != null) {
                    MusPlayActivity.this.handler.postDelayed(this, 500L);
                }
            }
        }
    };
    private int repeatState = 0;
    private boolean updateData = false;
    String[] permissions = {"android.permission.RECORD_AUDIO"};

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_music_player);
        AdmobAdsHelper.smallnativeAds(this, (ViewGroup) findViewById(R.id.layout_ads), (TextView) findViewById(R.id.adspace), (NativeAdLayout) findViewById(R.id.native_banner_ad_container), 1);
        this.ivHeart = (LottieAnimationView) findViewById(R.id.ivHeart);
        this.ivWaves = (LottieAnimationView) findViewById(R.id.ivWaves);
        this.btnPlayPause = (ImageView) findViewById(R.id.img_btn_play);
        ImageView imageView = (ImageView) findViewById(R.id.img_btn_previous);
        ImageView imageView2 = (ImageView) findViewById(R.id.img_btn_next);
        TextView textView = (TextView) findViewById(R.id.tv_song);
        this.tvSong = textView;
        textView.setSelected(true);
        TextView textView2 = (TextView) findViewById(R.id.tv_title);
        this.tvTitle = textView2;
        textView2.setSelected(true);
        this.tvArtist = (TextView) findViewById(R.id.tv_artist);
        this.tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        this.tvTotalTime = (TextView) findViewById(R.id.tv_total_time);
        this.seekBar = (SeekBar) findViewById(R.id.seekbar_controller);
        this.imgMusicArt = (ImageView) findViewById(R.id.iv_music_art);
        ImageView imageView3 = (ImageView) findViewById(R.id.iv_back);
        this.imgFavorite = (ImageView) findViewById(R.id.iv_favorite);
        this.imgRepeat = (ImageView) findViewById(R.id.img_repeat);
        ImageView imageView4 = (ImageView) findViewById(R.id.img_volume);
        ImageView imageView5 = (ImageView) findViewById(R.id.iv_music_playlist);
        this.imgShuffle = (ImageView) findViewById(R.id.img_shuffle);
        this.btnPlayPause.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        this.imgFavorite.setOnClickListener(this);
        this.imgRepeat.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        this.imgShuffle.setOnClickListener(this);
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.videoplayer.videox.activity.MusPlayActivity.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (MusPlayActivity.this.musicService != null) {
                    MusPlayActivity.this.musicService.seekTo(seekBar.getProgress());
                }
            }
        });
        if (getIntent() != null) {
            this.mSongList.clear();
            Intent intent = getIntent();
            if (intent.getData() != null) {
                this.mSongList.add(MusPlUti.getMusicInfoFromUri(this, intent.getData()));
                this.mPosition = 0;
            } else {
                ArrayList arrayList = (ArrayList) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_MUSIC_ARRAY);
                if (arrayList != null) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        MusicInfo musicById = MusicDatabaseControl.getInstance().getMusicById(((Long) it.next()).longValue());
                        if (musicById != null) {
                            this.mSongList.add(musicById);
                        }
                    }
                }
                MusicInfo musicInfo = (MusicInfo) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_MUSIC_SONG);
                if (this.mSongList.isEmpty() && musicInfo != null) {
                    this.mSongList.add(musicInfo);
                }
                this.mPosition = intent.getIntExtra(AppCon.IntentExtra.EXTRA_MUSIC_NUMBER, 0);
            }
        }
        this.updateData = true;
        startService(new Intent(this, (Class<?>) MusServ.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, (Class<?>) MusServ.class), this, 1);
    }

    @Override
    public void onBackPressed() {
        AdmobAdsHelper.ShowFullAds(this, true);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusServ musServ = this.musicService;
        if (musServ != null) {
            musServ.setCallBack(null);
        }
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacks(this.runnable);
        }
        unbindService(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean booleanExtra = intent.getBooleanExtra(AppCon.IntentExtra.EXTRA_FROM_NOTIFICATION, false);
        Log.d("binhnk08", "onNewIntent fromNotification = " + booleanExtra);
        if (booleanExtra) {
            this.mSongList.clear();
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_MUSIC_ARRAY);
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    MusicInfo musicById = MusicDatabaseControl.getInstance().getMusicById(((Long) it.next()).longValue());
                    if (musicById != null) {
                        this.mSongList.add(musicById);
                    }
                }
            }
            MusicInfo musicInfo = (MusicInfo) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_MUSIC_SONG);
            if (this.mSongList.isEmpty() && musicInfo != null) {
                this.mSongList.add(musicInfo);
            }
            this.mPosition = intent.getIntExtra(AppCon.IntentExtra.EXTRA_MUSIC_NUMBER, 0);
        } else if (intent.getData() != null) {
            this.mSongList.clear();
            this.mSongList.add(MusPlUti.getMusicInfoFromUri(this, intent.getData()));
            this.mPosition = 0;
        }
        this.updateData = true;
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.img_btn_next) {
            AdmobAdsHelper.showAdsNumberCount++;
            MusServ musServ = this.musicService;
            if (musServ != null) {
                musServ.nextSong();
                return;
            }
            return;
        }
        if (id == R.id.img_btn_play) {
            AdmobAdsHelper.showAdsNumberCount++;
            MusServ musServ2 = this.musicService;
            if (musServ2 != null) {
                musServ2.playSong();
                return;
            }
            return;
        }
        if (id == R.id.img_btn_previous) {
            AdmobAdsHelper.showAdsNumberCount++;
            MusServ musServ3 = this.musicService;
            if (musServ3 != null) {
                musServ3.backSong();
                return;
            }
            return;
        }
        if (id == R.id.img_repeat) {
            AdmobAdsHelper.showAdsNumberCount++;
            int i = this.repeatState;
            if (i == 0) {
                this.repeatState = 1;
                this.imgRepeat.setImageResource(R.drawable.baseline_repeat_one_24px);
            } else if (i == 1) {
                this.repeatState = 2;
                this.imgRepeat.setImageResource(R.drawable.baseline_repeat_24px);
            } else {
                this.repeatState = 0;
                this.imgRepeat.setImageResource(R.drawable.icn_no_repeat);
            }
            MusServ musServ4 = this.musicService;
            if (musServ4 != null) {
                musServ4.setRepeatState(this.repeatState);
                return;
            }
            return;
        }
        if (id == R.id.img_shuffle) {
            AdmobAdsHelper.showAdsNumberCount++;
            boolean isActivated = this.imgShuffle.isActivated();
            this.imgShuffle.setActivated(!isActivated);
            MusServ musServ5 = this.musicService;
            if (musServ5 != null) {
                musServ5.setShuffle(!isActivated);
                return;
            }
            return;
        }
        if (id == R.id.img_volume) {
            AdmobAdsHelper.showAdsNumberCount++;
            new ChaVolBtmSheet().show(getSupportFragmentManager(), "Change volume");
            return;
        }
        if (id == R.id.iv_back) {
            onBackPressed();
            return;
        }
        if (id == R.id.iv_favorite) {
            AdmobAdsHelper.showAdsNumberCount++;
            this.imgFavorite.setActivated(!imgFavorite.isActivated());
            MusServ musServ6 = this.musicService;
            if (musServ6 == null || musServ6.getCurrentSong() == null) {
                return;
            }
            MusicFavoriteUtil.addFavoriteMusicId(this, this.musicService.getCurrentSong().getId(), this.imgFavorite.isActivated());
            return;
        }
        if (id == R.id.iv_music_playlist) {
            AdmobAdsHelper.showAdsNumberCount++;
            if (this.mSongList.isEmpty()) {
                return;
            }
            if (this.mSongList.size() == 1) {
                Toast.makeText(this, R.string.playlist_has_only_one_song, Toast.LENGTH_SHORT).show();
                return;
            }
            MusPlayNxtInDialBuil musPlayNxtInDialBuil = new MusPlayNxtInDialBuil(this, this.mSongList, this.mPosition, new NextInMusPlaAdapter.Callback() { // from class: com.videoplayer.videox.activity.MusPlayActivity$$ExternalSyntheticLambda0
                @Override // com.videoplayer.videox.adapter.mus.NextInMusPlaAdapter.Callback
                public final void onMusicPlay(int i2) {
                    MusPlayActivity.this.m859lambda$onClick$0$comvideoplayervideoxactivityMusPlayActivity(i2);
                }
            });
            this.musicBuilder = musPlayNxtInDialBuil;
            musPlayNxtInDialBuil.build().show();
        }
    }

    /* renamed from: onClick_MusicPlayerActivity, reason: merged with bridge method [inline-methods] */
    public void m859lambda$onClick$0$comvideoplayervideoxactivityMusPlayActivity(int i) {
        this.mPosition = i;
        if (i < this.mSongList.size()) {
            this.musicBuilder.updateCurrentSong(i);
        }
        MusServ musServ = this.musicService;
        if (musServ != null) {
            musServ.setIndex(i);
            this.musicService.playMusic();
        }
    }

    public static boolean hasStoragePermissions33(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.RECORD_AUDIO") == 0;
    }

    private void startMediaController() {
        this.handler = new Handler();
        runOnUiThread(this.runnable);
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusServ service = ((MusServ.MyBinder) iBinder).getService();
        this.musicService = service;
        if (service == null || this.mSongList.isEmpty()) {
            return;
        }
        this.musicService.setCallBack(this);
        startMediaController();
        MusicInfo currentSong = this.musicService.getCurrentSong();
        Log.e("TAG1 ", currentSong + "");
        if (currentSong != null) {
            if (this.updateData) {
                this.musicService.setMusicInfoList(this.mSongList);
                this.musicService.setIndex(this.mPosition);
                this.updateData = false;
            }
            if (this.mPosition >= this.mSongList.size() || currentSong.equals(this.mSongList.get(this.mPosition))) {
                Log.e("TAG2 ", currentSong.getDisplayName() + "");
                this.tvTitle.setText(currentSong.getDisplayName());
                this.tvSong.setText(currentSong.getDisplayName());
                this.tvArtist.setText(currentSong.getArtist());
                this.imgFavorite.setActivated(MusicFavoriteUtil.checkFavoriteMusicIdExisted(this, currentSong.getId()));
            } else {
                this.musicService.playMusic();
            }
        } else {
            this.musicService.setMusicInfoList(this.mSongList);
            this.musicService.setIndex(this.mPosition);
            this.musicService.setRepeatState(0);
            this.musicService.playMusic();
        }
        this.imgShuffle.setActivated(this.musicService.isShuffle());
        this.repeatState = this.musicService.getRepeatState();
        setRepeatIcon();
        if (this.musicService.isSongPlaying()) {
            this.ivHeart.playAnimation();
            this.ivWaves.playAnimation();
            this.btnPlayPause.setImageResource(R.drawable.baseline_pause_circle_24);
        } else {
            this.ivHeart.pauseAnimation();
            this.ivWaves.pauseAnimation();
            this.btnPlayPause.setImageResource(R.drawable.baseline_play_circle_24);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        Log.e("onServiceDisconnected ", this.musicService + "");
        MusServ musServ = this.musicService;
        if (musServ != null) {
            this.mPosition = musServ.getIndex();
        }
        this.musicService = null;
    }

    @Override // com.videoplayer.videox.ser.MusServ.OnSongCallBack
    public void updateUISong(MusicInfo musicInfo, int i) {
        this.mPosition = i;
        this.tvTitle.setText(musicInfo.getDisplayName());
        this.tvSong.setText(musicInfo.getDisplayName());
        this.tvArtist.setText(musicInfo.getArtist());
        Glide.with((FragmentActivity) this).load(MusPlUti.getThumbnailOfSong(this, musicInfo.getPath(), 280)).centerCrop().error(R.drawable.disc_icn).into(this.imgMusicArt);
        this.imgFavorite.setActivated(MusicFavoriteUtil.checkFavoriteMusicIdExisted(this, musicInfo.getId()));
        this.btnPlayPause.setImageResource(R.drawable.baseline_pause_circle_24);
        this.ivHeart.playAnimation();
        this.ivWaves.playAnimation();
    }

    @Override // com.videoplayer.videox.ser.MusServ.OnSongCallBack
    public void updatePlayPauseState(boolean z) {
        if (z) {
            this.btnPlayPause.setImageResource(R.drawable.baseline_pause_circle_24);
            this.ivHeart.playAnimation();
            this.ivWaves.playAnimation();
        } else {
            this.btnPlayPause.setImageResource(R.drawable.baseline_play_circle_24);
            this.ivHeart.pauseAnimation();
            this.ivWaves.pauseAnimation();
        }
    }

    @Override // com.videoplayer.videox.ser.MusServ.OnSongCallBack
    public void notifyError(Exception exc) {
        Toast.makeText(this, R.string.something_when_wrong, Toast.LENGTH_SHORT).show();
    }

    @Override // com.videoplayer.videox.ser.MusServ.OnSongCallBack
    public void onStopService() {
        finish();
    }

    public void setRepeatIcon() {
        int i = this.repeatState;
        if (i == 1) {
            this.imgRepeat.setImageResource(R.drawable.baseline_repeat_one_24px);
        } else if (i == 2) {
            this.imgRepeat.setImageResource(R.drawable.baseline_repeat_24px);
        } else {
            this.imgRepeat.setImageResource(R.drawable.icn_no_repeat);
        }
    }
}
