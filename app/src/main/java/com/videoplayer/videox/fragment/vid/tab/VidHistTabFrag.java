package com.videoplayer.videox.fragment.vid.tab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.VidHist2Adapter;
import com.videoplayer.videox.dialog.MedInfDialBuil;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.db.datasource.VideoDatabaseControl;
import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.db.utils.VideoFavoriteUtil;
import com.videoplayer.videox.pre.vid.VidHisPre;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.thre.ThrExe;
import com.videoplayer.videox.vie.vid.VidHistVie;

import java.util.ArrayList;
import java.util.List;


public class VidHistTabFrag extends BasFrag<VidHisPre> implements VidHistVie, VidHist2Adapter.Callback {
    private ProgressBar loading;
    private VidHist2Adapter mAdapter;
    private Context mContext;
    private SwipeRefreshLayout refreshLayout;

    private void loadNativeAds() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static VidHistTabFrag newInstance() {
        return new VidHistTabFrag();
    }

    @Override // com.videoplayer.videox.fragment.BasFrag
    public VidHisPre createPresenter() {
        Context context = this.mContext;
        return new VidHisPre(context, this, new VideoDataRepository(context));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video_history, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_content_tab);
        this.refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        this.loading = (ProgressBar) inflate.findViewById(R.id.loading);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 1, false));
        VidHist2Adapter vidHist2Adapter = new VidHist2Adapter(this.mContext, new ArrayList(), this);
        this.mAdapter = vidHist2Adapter;
        recyclerView.setAdapter(vidHist2Adapter);
        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VidHistTabFrag$$ExternalSyntheticLambda0
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                VidHistTabFrag.this.lambda$onCreateView$0$VideoHistoryTabFragment();
            }
        });
        return inflate;
    }

    public void lambda$onCreateView$0$VideoHistoryTabFragment() {
        if (this.mPresenter != null) {
            ((VidHisPre) this.mPresenter).openHistoryTab();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mPresenter != null) {
            ((VidHisPre) this.mPresenter).openHistoryTab();
        }
    }

    @Override
    public void updateHistoryVideos(List<VideoHistory> list) {
        this.loading.setVisibility(View.GONE);
        this.refreshLayout.setVisibility(View.VISIBLE);
        this.refreshLayout.setRefreshing(false);
        VidHist2Adapter vidHist2Adapter = this.mAdapter;
        if (vidHist2Adapter != null) {
            vidHist2Adapter.updateHistory(list);
        }
    }

    class AnonymousClass3 implements QueDiaBuil.OkButtonClickListener {
        final int val$position;
        final VideoHistory val$videoHistory;

        @Override 
        public void onCancelClick() {
        }

        AnonymousClass3(int i, VideoHistory videoHistory) {
            this.val$position = i;
            this.val$videoHistory = videoHistory;
        }

        @Override 
        public void onOkClick() {
            if (VidHistTabFrag.this.mAdapter != null) {
                VidHistTabFrag.this.mAdapter.removeItemPosition(this.val$position);
            }
            if (VidHistTabFrag.this.mPresenter != null) {
                ((VidHisPre) VidHistTabFrag.this.mPresenter).deleteAHistoryVideoById(this.val$videoHistory.getId());
            }
            ThrExe.runOnDatabaseThread(new Runnable() { // from class: com.videoplayer.videox.fragment.vid.tab.VidHistTabFrag$AnonymousClass3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AnonymousClass3.this.m664x1295a5fe();
                }
            });
        }

        /* renamed from: lambda$onOkClick$0$com-videoplayer-videox-fragment-vid-tab-VidHistTabFrag$AnonymousClass3 */
        void m664x1295a5fe() {
            lambda$onOkClick$0$VideoHistoryTabFragment$3(this.val$videoHistory);
        }

        public void lambda$onOkClick$0$VideoHistoryTabFragment$3(VideoHistory videoHistory) {
            VideoDatabaseControl.getInstance().removeVideoById(videoHistory.getId());
            VideoFavoriteUtil.addFavoriteVideoId(VidHistTabFrag.this.mContext, videoHistory.getId(), false);
            Utility.deleteAVideo(VidHistTabFrag.this.mContext, videoHistory.getVideo());
        }
    }

    @Override // com.videoplayer.videox.adapter.vid.VidHist2Adapter.Callback
    public void onHistoryOptionSelect(final VideoHistory videoHistory, int i, final int i2) {
        if (i == 0) {
            new QueDiaBuil(this.mContext, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VidHistTabFrag.1
                @Override 
                public void onCancelClick() {
                }

                @Override 
                public void onOkClick() {
                    if (VidHistTabFrag.this.mPresenter != null) {
                        ((VidHisPre) VidHistTabFrag.this.mPresenter).deleteAHistoryVideoById(videoHistory.getId());
                    }
                    if (VidHistTabFrag.this.mAdapter != null) {
                        VidHistTabFrag.this.mAdapter.removeItemPosition(i2);
                    }
                }
            }).setTitle(R.string.delete, ContextCompat.getColor(this.mContext, R.color.color_FF6666)).setQuestion(R.string.question_remove_a_history_video).build().show();
        } else if (i == 1) {
            new QueDiaBuil(this.mContext, new AnonymousClass3(i2, videoHistory)).setTitle(R.string.delete, ContextCompat.getColor(this.mContext, R.color.color_FF6666)).setQuestion(R.string.question_remove_file).build().show();
        } else if (i == 2) {
            new MedInfDialBuil(this.mContext, videoHistory.getVideo()).build().show();
        }
    }

    public void deleteAllHistory() {
        VidHist2Adapter vidHist2Adapter = this.mAdapter;
        if (vidHist2Adapter != null) {
            vidHist2Adapter.removeAllItem();
        }
    }
}
