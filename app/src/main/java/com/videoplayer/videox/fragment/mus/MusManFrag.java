package com.videoplayer.videox.fragment.mus;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.MusPlayActivity;
import com.videoplayer.videox.adapter.mus.MusTabPaAdapter;
import com.videoplayer.videox.databinding.FragmentMusicManagerBinding;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.BasDialFrag;
import com.videoplayer.videox.fragment.mus.p014df.SeaMusDiaFrag;
import com.videoplayer.videox.fragment.mus.tab.MusAlbTabFrag;
import com.videoplayer.videox.fragment.mus.tab.MusArtTabFrag;
import com.videoplayer.videox.fragment.mus.tab.MusHistTabFrag;
import com.videoplayer.videox.fragment.mus.tab.MusInfTabFrag;
import com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.mus.MusicPresenter;
import com.videoplayer.videox.ser.MusServ;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.uti.mus.MusPlUti;

import java.util.ArrayList;
import java.util.List;


public class MusManFrag extends BasDialFrag<MusicPresenter> implements ServiceConnection {
    FragmentMusicManagerBinding binding;
    private MusicInfo currentMusic;
    private Context mContext;
    private MusTabPaAdapter mMusicTabPagerAdapter;
    private MusServ musicService;
    private int position;
    private boolean isPlaying = false;
    private final List<Long> musicIdList = new ArrayList();
    private int mViewMode = 1;
    final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.1
        @Override // java.lang.Runnable
        public void run() {
            if (MusManFrag.this.musicService != null) {
                int currentSeek = MusManFrag.this.musicService.getCurrentSeek();
                int totalDuration = MusManFrag.this.musicService.getTotalDuration();
                MusManFrag.this.binding.progressControl.setProgress(currentSeek);
                MusManFrag.this.binding.progressControl.setMax(totalDuration);
                if (MusManFrag.this.handler != null) {
                    MusManFrag.this.handler.postDelayed(this, 500L);
                }
            }
        }
    };
    private final ViewPager2.OnPageChangeCallback pageListener = new ViewPager2.OnPageChangeCallback() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.2
        @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
        public void onPageSelected(int i) {
            if (i == 0) {
                MusManFrag.this.binding.ivSort.setVisibility(View.VISIBLE);
                MusManFrag.this.binding.ivViewMode.setVisibility(View.GONE);
                MusManFrag.this.binding.ivDelete.setVisibility(View.GONE);
            } else if (i == 1) {
                MusManFrag.this.binding.ivSort.setVisibility(View.GONE);
                MusManFrag.this.binding.ivViewMode.setVisibility(View.VISIBLE);
                MusManFrag.this.binding.ivDelete.setVisibility(View.GONE);
            } else if (i == 2) {
                MusManFrag.this.binding.ivSort.setVisibility(View.GONE);
                MusManFrag.this.binding.ivViewMode.setVisibility(View.GONE);
                MusManFrag.this.binding.ivDelete.setVisibility(View.GONE);
            } else if (i == 3) {
                MusManFrag.this.binding.ivSort.setVisibility(View.GONE);
                MusManFrag.this.binding.ivViewMode.setVisibility(View.GONE);
                MusManFrag.this.binding.ivDelete.setVisibility(View.GONE);
            } else if (i == 4) {
                MusManFrag.this.binding.ivSort.setVisibility(View.GONE);
                MusManFrag.this.binding.ivViewMode.setVisibility(View.GONE);
                MusManFrag.this.binding.ivDelete.setVisibility(View.VISIBLE);
            }
            super.onPageSelected(i);
        }
    };
    private boolean isRunning = false;
    private boolean isBindService = false;
    final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.3
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            MusicInfo musicInfo = (MusicInfo) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_MUSIC_SONG);
            MusManFrag.this.isPlaying = intent.getBooleanExtra(AppCon.IntentExtra.EXTRA_MUSIC_PLAYING, false);
            MusManFrag.this.position = intent.getIntExtra(AppCon.IntentExtra.EXTRA_MUSIC_NUMBER, 0);
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra(AppCon.IntentExtra.EXTRA_MUSIC_ARRAY);
            if (arrayList != null && !arrayList.isEmpty()) {
                MusManFrag.this.musicIdList.clear();
                MusManFrag.this.musicIdList.addAll(arrayList);
            }
            Log.d("binhnk08", " onReceive = " + MusManFrag.this.isPlaying + " " + MusManFrag.this.position + " " + MusManFrag.this.isRunning + " " + MusManFrag.this.isBindService + " " + musicInfo);
            if (MusManFrag.this.isRunning) {
                if (musicInfo == null) {
                    if (MusManFrag.this.isBindService) {
                        MusManFrag.this.isBindService = false;
                        if (MusManFrag.this.handler != null) {
                            MusManFrag.this.handler.removeCallbacks(MusManFrag.this.runnable);
                        }
                        MusManFrag.this.requireActivity().unbindService(MusManFrag.this);
                    }
                } else if (!MusManFrag.this.isBindService) {
                    Intent intent2 = new Intent(MusManFrag.this.mContext, (Class<?>) MusServ.class);
                    MusManFrag musManFrag = MusManFrag.this;
                    musManFrag.isBindService = musManFrag.requireActivity().bindService(intent2, MusManFrag.this, 1);
                }
                if (MusManFrag.this.currentMusic == null || !MusManFrag.this.currentMusic.equals(musicInfo)) {
                    MusManFrag.this.currentMusic = musicInfo;
                    MusManFrag.this.setCurrentMusicContent();
                } else {
                    MusManFrag.this.binding.ivMusicPlay.setImageResource(MusManFrag.this.isPlaying ? R.drawable.baseline_pause_circle_24 : R.drawable.baseline_play_circle_24);
                    MusManFrag.this.binding.layoutSongPlaying.setVisibility(View.VISIBLE);
                }
            }
            MusManFrag.this.currentMusic = musicInfo;
        }
    };

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static MusManFrag newInstance() {
        return new MusManFrag();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = FragmentMusicManagerBinding.inflate(getLayoutInflater());
        ArrayList arrayList = new ArrayList();
        arrayList.add(MusInfTabFrag.newInstance());
        arrayList.add(MusPlylistTabFrag.newInstance());
        arrayList.add(MusAlbTabFrag.newInstance());
        arrayList.add(MusArtTabFrag.newInstance());
        arrayList.add(MusHistTabFrag.newInstance());
        MusTabPaAdapter musTabPaAdapter = new MusTabPaAdapter(requireActivity(), arrayList);
        this.mMusicTabPagerAdapter = musTabPaAdapter;
        this.binding.viewPagerMusic.setAdapter(musTabPaAdapter);
        this.binding.viewPagerMusic.setOffscreenPageLimit(5);
        new TabLayoutMediator(this.binding.tabLayoutMusic, this.binding.viewPagerMusic, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag$$ExternalSyntheticLambda0
            @Override
            public final void onConfigureTab(TabLayout.Tab tab, int i) {
                MusManFrag.onCreateView$0(tab, i);
            }
        }).attach();
        this.binding.viewPagerMusic.registerOnPageChangeCallback(this.pageListener);
        this.binding.ivMusicPlay.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.4
            @Override 
            public void onClick(View v) {
                MusManFrag.this.playCurrentMusic();
            }
        });
        this.binding.ivMusicClose.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.5
            @Override 
            public void onClick(View v) {
                MusManFrag.this.stopPlayMusic();
            }
        });
        this.binding.layoutSongPlaying.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.6
            @Override 
            public void onClick(View v) {
                MusManFrag.this.openMusicPlayer();
            }
        });
        this.binding.ivSort.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.7
            @Override 
            public void onClick(View v) {
                MusManFrag.this.onSortClick();
            }
        });
        this.binding.ivDelete.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.8
            @Override 
            public void onClick(View v) {
                MusManFrag.this.onDeleteClick();
            }
        });
        this.binding.ivViewMode.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.9
            @Override 
            public void onClick(View v) {
                MusManFrag.this.onGridClick();
            }
        });
        this.binding.ivSearch.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.10
            @Override 
            public void onClick(View v) {
                MusManFrag.this.onSearchClick();
            }
        });
        return this.binding.getRoot();
    }

    public static void onCreateView$0(TabLayout.Tab tab, int i) {
        if (i == 0) {
            tab.setText(R.string.music);
            return;
        }
        if (i == 1) {
            tab.setText(R.string.playlist);
            return;
        }
        if (i == 2) {
            tab.setText(R.string.album);
        } else if (i == 3) {
            tab.setText(R.string.artist);
        } else if (i == 4) {
            tab.setText(R.string.history);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.isRunning = true;
        setCurrentMusicContent();
        this.isBindService = requireActivity().bindService(new Intent(this.mContext, (Class<?>) MusServ.class), this, 1);
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mReceiver, new IntentFilter("RECEIVER_CURRENT_MUSIC"));
    }

    @Override
    public void onPause() {
        super.onPause();
        this.isRunning = false;
        if (this.isBindService) {
            requireActivity().unbindService(this);
            this.isBindService = false;
        }
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacks(this.runnable);
        }
    }

    public void setCurrentMusicContent() {
        MusicInfo musicInfo = this.currentMusic;
        if (musicInfo != null) {
            Glide.with(this.mContext).load(MusPlUti.getThumbnailOfSong(this.mContext, musicInfo.getPath(), 40)).centerCrop().error(R.drawable.disc_icn).into(this.binding.ivThumbnail);
            String artist = this.currentMusic.getArtist();
            if (TextUtils.isEmpty(artist)) {
                this.binding.tvArtist.setVisibility(View.GONE);
            } else {
                this.binding.tvArtist.setText(artist);
            }
            this.binding.tvSong.setText(this.currentMusic.getDisplayName());
            this.binding.ivMusicPlay.setImageResource(this.isPlaying ? R.drawable.baseline_pause_circle_24 : R.drawable.baseline_play_circle_24);
            this.binding.layoutSongPlaying.setVisibility(View.VISIBLE);
            return;
        }
        this.binding.layoutSongPlaying.setVisibility(View.GONE);
    }

    public void playCurrentMusic() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        MusServ musServ = this.musicService;
        if (musServ != null) {
            musServ.playSong();
        }
    }

    public void stopPlayMusic() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        MusServ musServ = this.musicService;
        if (musServ != null) {
            musServ.stopService();
        }
    }

    public void openMusicPlayer() {
        if (this.musicIdList.isEmpty()) {
            return;
        }
        Intent intent = new Intent(this.mContext, (Class<?>) MusPlayActivity.class);
        intent.putExtra(AppCon.IntentExtra.EXTRA_FROM_NOTIFICATION, true);
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_NUMBER, this.position);
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_ARRAY, (ArrayList) this.musicIdList);
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_SONG, this.currentMusic);
        intent.addFlags(65536);
        intent.addFlags(C.ENCODING_PCM_32BIT);
        this.mContext.startActivity(intent);
        AdmobAdsHelper.ShowFullAds(this.mContext, false);
    }

    public void onSortClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        if (this.binding.viewPagerMusic.getCurrentItem() == 0) {
            BtmMenDialCont.getInstance().showSortDialogForMusic(this.mContext, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag$$ExternalSyntheticLambda1
                @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
                public final void onClick(int i, boolean z) {
                    MusManFrag.this.m640x87c54f76(i, z);
                }
            });
        }
    }

    /* renamed from: lambda$onSortClick$1$com-videoplayer-videox-fragment-mus-MusManFrag */
    void m640x87c54f76(int i, boolean z) {
        this.mMusicTabPagerAdapter.sortData(0, i, z);
    }

    public void onDeleteClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        new QueDiaBuil(this.mContext, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusManFrag.11
            @Override 
            public void onCancelClick() {
            }

            @Override 
            public void onOkClick() {
                MusManFrag.this.mMusicTabPagerAdapter.deleteAllMusicHistory();
                if (MusManFrag.this.mPresenter != null) {
                    ((MusicPresenter) MusManFrag.this.mPresenter).deleteAllMusicHistory();
                }
            }
        }).setTitle(R.string.delete_all, ContextCompat.getColor(this.mContext, R.color.color_FF6666)).setQuestion(R.string.question_remove_all_history_video).build().show();
    }

    public void onGridClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        if (this.binding.viewPagerMusic.getCurrentItem() == 1) {
            if (this.mViewMode == 1) {
                this.mViewMode = 2;
            } else {
                this.mViewMode = 1;
            }
        }
        setViewModeIcon(this.mViewMode);
        this.mMusicTabPagerAdapter.setViewMode(this.mViewMode);
    }

    private void setViewModeIcon(int i) {
        if (i == 1) {
            this.binding.ivViewMode.setImageResource(R.drawable.baseline_view_list_24);
        } else {
            this.binding.ivViewMode.setImageResource(R.drawable.baseline_grid_view_24);
        }
    }

    public void onSearchClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        SeaMusDiaFrag.newInstance().show(getChildFragmentManager().beginTransaction(), "dialog_search");
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mReceiver);
        this.binding.viewPagerMusic.unregisterOnPageChangeCallback(this.pageListener);
    }

    @Override // com.videoplayer.videox.fragment.BasDialFrag
    public MusicPresenter createPresenter() {
        Context context = this.mContext;
        return new MusicPresenter(context, null, new MusicDataRepository(context));
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusServ service = ((MusServ.MyBinder) iBinder).getService();
        this.musicService = service;
        if (service != null) {
            this.currentMusic = service.getCurrentSong();
            this.position = this.musicService.getIndex();
            this.isPlaying = this.musicService.isSongPlaying();
            this.musicIdList.clear();
            this.musicIdList.addAll(this.musicService.getMusicIdList());
            setCurrentMusicContent();
        }
        requireActivity().runOnUiThread(this.runnable);
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.musicService = null;
    }
}
