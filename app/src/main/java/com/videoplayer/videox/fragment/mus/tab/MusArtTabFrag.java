package com.videoplayer.videox.fragment.mus.tab;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.MusArtAdapter;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.fragment.mus.p014df.MusPlylistDialFrag;
import com.videoplayer.videox.db.entity.music.MusicArtist;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.mus.MusArtPre;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.vie.mus.MusArtVie;

import java.util.ArrayList;
import java.util.List;


public class MusArtTabFrag extends BasFrag<MusArtPre> implements MusArtVie, MusArtAdapter.Callback {
    private final ContentObserver contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.mus.tab.MusArtTabFrag.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (MusArtTabFrag.this.mPresenter != null) {
                ((MusArtPre) MusArtTabFrag.this.mPresenter).openArtistTab();
            }
        }
    };
    private FrameLayout frameAds;
    private ProgressBar loading;
    private MusArtAdapter mAdapter;
    private Context mContext;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvTotal;

    private void loadNativeAds() {
    }

    @Override // com.videoplayer.videox.vie.mus.MusArtVie
    public void onUpdateMusicOfArtist(List<MusicInfo> list) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static MusArtTabFrag newInstance() {
        return new MusArtTabFrag();
    }

    @Override // com.videoplayer.videox.fragment.BasFrag
    public MusArtPre createPresenter() {
        Context context = this.mContext;
        return new MusArtPre(context, this, new MusicDataRepository(context));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_music_artist, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_content_tab);
        this.refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        this.loading = (ProgressBar) inflate.findViewById(R.id.loading);
        this.tvTotal = (TextView) inflate.findViewById(R.id.tv_total);
        this.mAdapter = new MusArtAdapter(this.mContext, new ArrayList(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        recyclerView.setAdapter(this.mAdapter);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.fragment.mus.tab.MusArtTabFrag$$ExternalSyntheticLambda0
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                MusArtTabFrag.this.lambda$onCreateView$0$MusicArtistTabFragment();
            }
        });
        return inflate;
    }

    public void lambda$onCreateView$0$MusicArtistTabFragment() {
        if (this.mPresenter != null) {
            ((MusArtPre) this.mPresenter).openArtistTab();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mPresenter != null) {
            ((MusArtPre) this.mPresenter).openArtistTab();
        }
        requireActivity().getContentResolver().registerContentObserver(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, true, this.contentObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getContentResolver().unregisterContentObserver(this.contentObserver);
    }

    @Override // com.videoplayer.videox.vie.mus.MusArtVie
    public void onOpenArtist(List<MusicArtist> list) {
        this.loading.setVisibility(View.GONE);
        this.refreshLayout.setVisibility(View.VISIBLE);
        this.refreshLayout.setRefreshing(false);
        this.tvTotal.setText(getString(R.string.all_artist, Integer.valueOf(list.size())));
        MusArtAdapter musArtAdapter = this.mAdapter;
        if (musArtAdapter != null) {
            musArtAdapter.updateMusicDataList(list);
        }
    }

    @Override // com.videoplayer.videox.adapter.mus.MusArtAdapter.Callback
    public void onArtistSelected(MusicArtist musicArtist) {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        MusPlylistDialFrag.newInstance(3, musicArtist).show(getChildFragmentManager().beginTransaction(), "dialog_artist_music");
    }
}
