package com.videoplayer.videox.fragment.mus.tab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.MusHisAdapter;
import com.videoplayer.videox.dialog.MedInfDialBuil;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.db.datasource.MusicDatabaseControl;
import com.videoplayer.videox.db.entity.music.MusicHistory;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.db.utils.MusicFavoriteUtil;
import com.videoplayer.videox.pre.mus.MusHisPre;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.thre.ThrExe;
import com.videoplayer.videox.vie.mus.MusHistVie;

import java.util.ArrayList;
import java.util.List;


public class MusHistTabFrag extends BasFrag<MusHisPre> implements MusHistVie, MusHisAdapter.Callback {
    private ProgressBar loading;
    private MusHisAdapter mAdapter;
    private Context mContext;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvTotal;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static MusHistTabFrag newInstance() {
        return new MusHistTabFrag();
    }

    @Override // com.videoplayer.videox.fragment.BasFrag
    public MusHisPre createPresenter() {
        Context context = this.mContext;
        return new MusHisPre(context, this, new MusicDataRepository(context));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_music_info_tab, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_content_tab);
        this.tvTotal = (TextView) inflate.findViewById(R.id.tv_total);
        this.refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        this.loading = (ProgressBar) inflate.findViewById(R.id.loading);
        this.mAdapter = new MusHisAdapter(this.mContext, new ArrayList(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        recyclerView.setAdapter(this.mAdapter);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.fragment.mus.tab.MusHistTabFrag.1
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                MusHistTabFrag.this.lambda$onCreateView$0$MusicHistoryTabFragment();
            }
        });
        return inflate;
    }

    public void lambda$onCreateView$0$MusicHistoryTabFragment() {
        if (this.mPresenter != null) {
            ((MusHisPre) this.mPresenter).openMusicHistoryTab();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mPresenter != null) {
            ((MusHisPre) this.mPresenter).openMusicHistoryTab();
        }
    }

    @Override // com.videoplayer.videox.vie.mus.MusHistVie
    public void updateMusicHistoryList(List<MusicHistory> list) {
        this.loading.setVisibility(View.GONE);
        this.refreshLayout.setVisibility(View.VISIBLE);
        this.refreshLayout.setRefreshing(false);
        this.tvTotal.setText(getString(R.string.all_history, Integer.valueOf(list.size())));
        MusHisAdapter musHisAdapter = this.mAdapter;
        if (musHisAdapter != null) {
            musHisAdapter.updateMusicHistory(list);
        }
    }

    class AnonymousClass3 implements QueDiaBuil.OkButtonClickListener {
        final MusicHistory val$musicHistory;
        final int val$position;

        @Override 
        public void onCancelClick() {
        }

        AnonymousClass3(int i, MusicHistory musicHistory) {
            this.val$position = i;
            this.val$musicHistory = musicHistory;
        }

        @Override 
        public void onOkClick() {
            if (MusHistTabFrag.this.mAdapter != null) {
                MusHistTabFrag.this.mAdapter.removeItemPosition(this.val$position);
            }
            if (MusHistTabFrag.this.mPresenter != null) {
                ((MusHisPre) MusHistTabFrag.this.mPresenter).deleteAHistoryMusicById(this.val$musicHistory.getId());
            }
            final MusicHistory musicHistory = this.val$musicHistory;
            ThrExe.runOnDatabaseThread(new Runnable() { // from class: com.videoplayer.videox.fragment.mus.tab.MusHistTabFrag.AnonymousClass3.1
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass3.this.lambda$onOkClick$0$MusicHistoryTabFragment$3(musicHistory);
                }
            });
        }

        public void lambda$onOkClick$0$MusicHistoryTabFragment$3(MusicHistory musicHistory) {
            MusicDatabaseControl.getInstance().removeMusicById(musicHistory.getId());
            MusicFavoriteUtil.addFavoriteMusicId(MusHistTabFrag.this.mContext, musicHistory.getId(), false);
            Utility.deleteAMusic(MusHistTabFrag.this.mContext, musicHistory.getMusics());
        }
    }

    @Override // com.videoplayer.videox.adapter.mus.MusHisAdapter.Callback
    public void onMusicOptionSelect(final MusicHistory musicHistory, int i, final int i2) {
        if (i == 0) {
            AdmobAdsHelper.showAdsNumberCount++;
            new QueDiaBuil(this.mContext, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.mus.tab.MusHistTabFrag.2
                @Override 
                public void onCancelClick() {
                }

                @Override 
                public void onOkClick() {
                    if (MusHistTabFrag.this.mPresenter != null) {
                        ((MusHisPre) MusHistTabFrag.this.mPresenter).deleteAHistoryMusicById(musicHistory.getId());
                    }
                    if (MusHistTabFrag.this.mAdapter != null) {
                        MusHistTabFrag.this.mAdapter.removeItemPosition(i2);
                    }
                }
            }).setTitle(R.string.delete, this.mContext.getResources().getColor(R.color.color_FF6666)).setQuestion(R.string.question_remove_a_history_video).build().show();
        } else if (i == 1) {
            AdmobAdsHelper.showAdsNumberCount++;
            new QueDiaBuil(this.mContext, new AnonymousClass3(i2, musicHistory)).setTitle(R.string.delete, this.mContext.getResources().getColor(R.color.color_FF6666)).setQuestion(R.string.question_remove_file).build().show();
        } else if (i == 2) {
            AdmobAdsHelper.showAdsNumberCount++;
            new MedInfDialBuil(this.mContext, musicHistory.getMusics()).build().show();
        }
    }

    public void deleteAllHistory() {
        MusHisAdapter musHisAdapter = this.mAdapter;
        if (musHisAdapter != null) {
            musHisAdapter.removeAllItem();
        }
    }
}
