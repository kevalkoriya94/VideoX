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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.MusAlbAdapter;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.fragment.mus.p014df.MusAlbDialFrag;
import com.videoplayer.videox.db.entity.music.MusicAlbum;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.mus.MusAlbPre;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.vie.mus.MusAlbVie;

import java.util.ArrayList;
import java.util.List;


public class MusAlbTabFrag extends BasFrag<MusAlbPre> implements MusAlbVie, MusAlbAdapter.Callback {
    private final ContentObserver contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.mus.tab.MusAlbTabFrag.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (MusAlbTabFrag.this.mPresenter != null) {
                ((MusAlbPre) MusAlbTabFrag.this.mPresenter).openAlbumTab();
            }
        }
    };
    private ProgressBar loading;
    private MusAlbAdapter mAdapter;
    private Context mContext;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvTotal;

    private void loadNativeAds() {
    }

    @Override // com.videoplayer.videox.adapter.mus.MusAlbAdapter.Callback
    public void onAlbumOptionSelect(MusicAlbum musicAlbum, int i, int i2) {
    }

    @Override // com.videoplayer.videox.vie.mus.MusAlbVie
    public void onUpdateMusicList(List<MusicInfo> list) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static MusAlbTabFrag newInstance() {
        return new MusAlbTabFrag();
    }

    @Override // com.videoplayer.videox.fragment.BasFrag
    public MusAlbPre createPresenter() {
        Context context = this.mContext;
        return new MusAlbPre(context, this, new MusicDataRepository(context));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_music_info_tab, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_content_tab);
        this.refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        this.tvTotal = (TextView) inflate.findViewById(R.id.tv_total);
        this.loading = (ProgressBar) inflate.findViewById(R.id.loading);
        this.mAdapter = new MusAlbAdapter(this.mContext, new ArrayList(), this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mContext, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.videoplayer.videox.fragment.mus.tab.MusAlbTabFrag.2
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                return (MusAlbTabFrag.this.mAdapter != null && MusAlbTabFrag.this.mAdapter.isEmptyData() && i == 0) ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(this.mAdapter);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.fragment.mus.tab.MusAlbTabFrag.3
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                MusAlbTabFrag.this.lambda$onCreateView$0$MusicAlbumTabFragment();
            }
        });
        return inflate;
    }

    public void lambda$onCreateView$0$MusicAlbumTabFragment() {
        if (this.mPresenter != null) {
            ((MusAlbPre) this.mPresenter).openAlbumTab();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mPresenter != null) {
            ((MusAlbPre) this.mPresenter).openAlbumTab();
        }
        requireActivity().getContentResolver().registerContentObserver(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, true, this.contentObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getContentResolver().unregisterContentObserver(this.contentObserver);
    }

    @Override // com.videoplayer.videox.adapter.mus.MusAlbAdapter.Callback
    public void onAlbumClick(int i, MusicAlbum musicAlbum) {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        MusAlbDialFrag.newInstance(musicAlbum, new MusAlbDialFrag.Callback() { // from class: com.videoplayer.videox.fragment.mus.tab.MusAlbTabFrag.4
        }).show(getChildFragmentManager().beginTransaction(), "dialog_music_album");
    }

    @Override // com.videoplayer.videox.vie.mus.MusAlbVie
    public void onUpdateAlbum(List<MusicAlbum> list) {
        this.loading.setVisibility(View.GONE);
        this.refreshLayout.setVisibility(View.VISIBLE);
        this.refreshLayout.setRefreshing(false);
        this.tvTotal.setText(getString(R.string.all_album, Integer.valueOf(list.size())));
        MusAlbAdapter musAlbAdapter = this.mAdapter;
        if (musAlbAdapter != null) {
            musAlbAdapter.updateMusicAlbum(list);
        }
    }
}
