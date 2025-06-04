package com.videoplayer.videox.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.ads.NativeAdLayout;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.MusInfAdapter;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class AudConActivity extends AppCompatActivity implements MusInfAdapter.Callback {
    MusInfAdapter.Callback callback;
    private FrameLayout frameAds;
    private Context mContext;
    private boolean mIsSelectMode;
    private List<MusicInfo> mMusicSelected;
    private RecyclerView mRvContent;
    NativeAdLayout native_banner_ad_container;

    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_converter);
        this.mContext = this;
        findViewById(R.id.ivAdd).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.AudConActivity$$ExternalSyntheticLambda0
            @Override
            public final void onClick(View view) {
                AudConActivity.this.m852lambda$onCreate$0$comvideoplayervideoxactivityAudConActivity(view);
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.AudConActivity$$ExternalSyntheticLambda1
            @Override
            public final void onClick(View view) {
                AudConActivity.this.m853lambda$onCreate$1$comvideoplayervideoxactivityAudConActivity(view);
            }
        });
        this.callback = this;
        this.mRvContent = (RecyclerView) findViewById(R.id.rv_content_tab);
        this.frameAds = (FrameLayout) findViewById(R.id.layout_ads);
        this.native_banner_ad_container = (NativeAdLayout) findViewById(R.id.native_banner_ad_container);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        if (getAudios().size() != 0) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setVisibility(View.GONE);
        }
        this.mRvContent.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemAnimator itemAnimator = this.mRvContent.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        getAudios();
        setAdapter();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.activity.AudConActivity$$ExternalSyntheticLambda2
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                AudConActivity.this.setAdapter();
            }
        });
        AudFolActivity.setCallback(this.callback);
        AdmobAdsHelper.smallnativeAds(this.mContext, (ViewGroup) findViewById(R.id.layout_ads), (TextView) findViewById(R.id.adspace), (NativeAdLayout) findViewById(R.id.native_banner_ad_container), 1);
    }

    /* renamed from: lambda$onCreate$0$com-videoplayer-videox-activity-AudConActivity, reason: not valid java name */
    void m852lambda$onCreate$0$comvideoplayervideoxactivityAudConActivity(View view) {
        startActivity(new Intent(this, (Class<?>) AudFolActivity.class).addFlags(65536));
        AdmobAdsHelper.ShowFullAds(this, false);
    }

    /* renamed from: lambda$onCreate$1$com-videoplayer-videox-activity-AudConActivity, reason: not valid java name */
    void m853lambda$onCreate$1$comvideoplayervideoxactivityAudConActivity(View view) {
        onBackPressed();
    }

    public void setAdapter() {
        getAudios();
        MusInfAdapter musInfAdapter = new MusInfAdapter(this, getAudios(), this.mIsSelectMode, this, this.mMusicSelected);
        this.mRvContent.setAdapter(musInfAdapter);
        musInfAdapter.notifyDataSetChanged();
    }

    public List<MusicInfo> getAudios() {
        HashSet hashSet = new HashSet();
        Cursor query = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_display_name", "artist", "album", "_data", "duration", "date_added", "_size"}, "_data like ? ", new String[]{"%MusicConvert%"}, null);
        if (query != null) {
            while (query.moveToNext()) {
                try {
                    int columnIndexOrThrow = query.getColumnIndexOrThrow("_id");
                    int columnIndexOrThrow2 = query.getColumnIndexOrThrow("_display_name");
                    int columnIndexOrThrow3 = query.getColumnIndexOrThrow("artist");
                    int columnIndexOrThrow4 = query.getColumnIndexOrThrow("album");
                    int columnIndexOrThrow5 = query.getColumnIndexOrThrow("_data");
                    int columnIndexOrThrow6 = query.getColumnIndexOrThrow("duration");
                    int columnIndexOrThrow7 = query.getColumnIndexOrThrow("date_added");
                    int columnIndexOrThrow8 = query.getColumnIndexOrThrow("_size");
                    while (query.moveToNext()) {
                        MusicInfo musicInfo = new MusicInfo();
                        musicInfo.setId(query.getLong(columnIndexOrThrow));
                        musicInfo.setPath(query.getString(columnIndexOrThrow5));
                        musicInfo.setDisplayName(query.getString(columnIndexOrThrow2));
                        musicInfo.setArtist(query.getString(columnIndexOrThrow3));
                        musicInfo.setAlbum(query.getString(columnIndexOrThrow4));
                        musicInfo.setDuration(query.getLong(columnIndexOrThrow6));
                        musicInfo.setDate(query.getLong(columnIndexOrThrow7));
                        musicInfo.setSize(query.getLong(columnIndexOrThrow8));
                        hashSet.add(musicInfo);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "getAudios: " + e.getMessage());
                    if (query != null) {
                        try {
                            query.close();
                        } catch (Throwable th) {
                            e.addSuppressed(th);
                        }
                    }
                }
            }
            if (query != null) {
                query.close();
            }
        }
        Log.e("TAG", "getAudios: " + hashSet);
        return new ArrayList(hashSet);
    }

    @Override // com.videoplayer.videox.adapter.mus.MusInfAdapter.Callback
    public void onFavoriteUpdate(int i, boolean z) {
        setAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();
    }

    @Override
    public void onBackPressed() {
        AdmobAdsHelper.ShowFullAds(this, true);
        finish();
    }
}
