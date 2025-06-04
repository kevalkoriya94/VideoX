package com.videoplayer.videox.fragment.mus;

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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.MusInfAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.mus.MusInfPre;
import com.videoplayer.videox.vie.mus.MusInfVie;

import java.util.ArrayList;
import java.util.List;


public class MusFilFrag extends BasFrag<MusInfPre> implements MusInfVie, MusInfAdapter.Callback {
    private final ContentObserver contentObserver;
    private ProgressBar loading;
    private MusInfAdapter mAdapter;
    private Context mContext;
    private boolean mIsSelectMode;
    private final List<MusicInfo> mMusicSelected;
    private SwipeRefreshLayout refreshLayout;

    private void loadNativeAds() {
    }

    @Override // com.videoplayer.videox.adapter.mus.MusInfAdapter.Callback
    public void onFavoriteUpdate(int i, boolean z) {
    }

    public MusFilFrag() {
        this.contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.mus.MusFilFrag.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                if (MusFilFrag.this.mPresenter != null) {
                    ((MusInfPre) MusFilFrag.this.mPresenter).openMusicsTab();
                }
            }
        };
        this.mMusicSelected = new ArrayList();
    }

    public MusFilFrag(List<MusicInfo> list) {
        this.contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.mus.MusFilFrag.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                if (MusFilFrag.this.mPresenter != null) {
                    ((MusInfPre) MusFilFrag.this.mPresenter).openMusicsTab();
                }
            }
        };
        this.mMusicSelected = list;
        this.mIsSelectMode = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static MusFilFrag newInstance() {
        return new MusFilFrag();
    }

    public static MusFilFrag newInstance(List<MusicInfo> list) {
        return new MusFilFrag(list);
    }

    @Override // com.videoplayer.videox.fragment.BasFrag
    public MusInfPre createPresenter() {
        Context context = this.mContext;
        return new MusInfPre(context, this, new MusicDataRepository(context));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_music_file, viewGroup, false);
    }

    @Override
    public void onViewCreated(View inflate, Bundle savedInstanceState) {
        super.onViewCreated(inflate, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_content_tab);
        this.refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        this.loading = (ProgressBar) inflate.findViewById(R.id.loading);
        this.mAdapter = new MusInfAdapter(this.mContext, new ArrayList(), this.mIsSelectMode, this, this.mMusicSelected);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        recyclerView.setAdapter(this.mAdapter);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.fragment.mus.MusFilFrag$$ExternalSyntheticLambda0
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                MusFilFrag.this.m639x104db4ed();
            }
        });
        inflate.findViewById(R.id.iv_sort).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusFilFrag.3
            @Override 
            public void onClick(View v) {
                MusFilFrag.this.onSortClick();
            }
        });
    }

    /* renamed from: lambda$onCreateView$0$MusicInfoTabFragment, reason: merged with bridge method [inline-methods] */
    public void m639x104db4ed() {
        if (this.mPresenter != null) {
            ((MusInfPre) this.mPresenter).openMusicsTab();
        }
    }

    public void onSortClick() {
        BtmMenDialCont.getInstance().showSortDialogForMusic(this.mContext, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.mus.MusFilFrag.4
            @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
            public void onClick(int i, boolean z) {
                MusFilFrag.this.mAdapter.sortMusicList(i, z);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mPresenter != null) {
            ((MusInfPre) this.mPresenter).openMusicsTab();
        }
        requireActivity().getContentResolver().registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, this.contentObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getContentResolver().unregisterContentObserver(this.contentObserver);
    }

    public void sortMusicList(int i, boolean z) {
        MusInfAdapter musInfAdapter = this.mAdapter;
        if (musInfAdapter != null) {
            musInfAdapter.sortMusicList(i, z);
        }
    }

    @Override // com.videoplayer.videox.vie.mus.MusInfVie
    public void updateMusicList(List<MusicInfo> list) {
        this.loading.setVisibility(View.GONE);
        this.refreshLayout.setVisibility(View.VISIBLE);
        this.refreshLayout.setRefreshing(false);
        MusInfAdapter musInfAdapter = this.mAdapter;
        if (musInfAdapter != null) {
            musInfAdapter.updateMusicDataList(list);
        }
    }

    public List<MusicInfo> getMusicSelected() {
        MusInfAdapter musInfAdapter = this.mAdapter;
        return musInfAdapter == null ? new ArrayList() : musInfAdapter.getMusicSelected();
    }
}
