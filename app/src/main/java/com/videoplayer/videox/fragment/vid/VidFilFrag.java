package com.videoplayer.videox.fragment.vid;

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

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.HomPageActivity;
import com.videoplayer.videox.adapter.vid.VidInfAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.cv.NpaGridLayManager;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.vid.VidInfoPre;
import com.videoplayer.videox.vie.vid.VidInfVie;

import java.util.ArrayList;
import java.util.List;


public class VidFilFrag extends BasFrag<VidInfoPre> implements VidInfVie, VidInfAdapter.Callback {
    private final ContentObserver contentObserver;
    private ProgressBar loading;
    private VidInfAdapter mAdapter;
    private Context mContext;
    private NpaGridLayManager mGridLayoutManager;
    private boolean mIsSelectMode;
    private final List<VideoInfo> mVideoSelected;
    private SwipeRefreshLayout refreshLayout;

    private void loadNativeAds() {
    }

    @Override // com.videoplayer.videox.adapter.vid.VidInfAdapter.Callback
    public void onFavoriteUpdate(int i, boolean z) {
    }

    public VidFilFrag() {
        this.contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.vid.VidFilFrag.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                if (VidFilFrag.this.mPresenter != null) {
                    ((VidInfoPre) VidFilFrag.this.mPresenter).openVideosTab();
                }
            }
        };
        this.mVideoSelected = new ArrayList();
    }

    public VidFilFrag(List<VideoInfo> list) {
        this.contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.vid.VidFilFrag.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                if (VidFilFrag.this.mPresenter != null) {
                    ((VidInfoPre) VidFilFrag.this.mPresenter).openVideosTab();
                }
            }
        };
        this.mVideoSelected = new ArrayList(list);
        this.mIsSelectMode = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static VidFilFrag newInstance() {
        return new VidFilFrag();
    }

    public static VidFilFrag newInstance(List<VideoInfo> list) {
        return new VidFilFrag(list);
    }

    @Override // com.videoplayer.videox.fragment.BasFrag
    public VidInfoPre createPresenter() {
        Context context = this.mContext;
        return new VidInfoPre(context, this, new VideoDataRepository(context));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video_files, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_content_tab);
        this.loading = (ProgressBar) inflate.findViewById(R.id.loading);
        this.refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        Context context = this.mContext;
        ArrayList arrayList = new ArrayList();
        boolean z = this.mIsSelectMode;
        this.mAdapter = new VidInfAdapter(context, arrayList, !z, z, this, this.mVideoSelected);
        NpaGridLayManager npaGridLayManager = new NpaGridLayManager(this.mContext, 1);
        this.mGridLayoutManager = npaGridLayManager;
        npaGridLayManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.videoplayer.videox.fragment.vid.VidFilFrag.3
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                return VidFilFrag.this.mGridLayoutManager.getSpanCount() == 2 ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(this.mGridLayoutManager);
        recyclerView.setAdapter(this.mAdapter);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.fragment.vid.VidFilFrag$$ExternalSyntheticLambda1
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                VidFilFrag.this.m645x44d080eb();
            }
        });
        inflate.findViewById(R.id.iv_sort).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.VidFilFrag$$ExternalSyntheticLambda2
            @Override 
            public final void onClick(View view) {
                VidFilFrag.this.m646x7e9b22ca(view);
            }
        });
        return inflate;
    }

    /* renamed from: lambda$onCreateView$0$com-videoplayer-videox-fragment-vid-VidFilFrag */
    void m645x44d080eb() {
        if (this.mPresenter != null) {
            ((VidInfoPre) this.mPresenter).openVideosTab();
        }
    }

    /* renamed from: lambda$onCreateView$1$com-videoplayer-videox-fragment-vid-VidFilFrag */
    void m646x7e9b22ca(View view) {
        onSortClick();
    }

    /* renamed from: lambda$onSortClick$2$com-videoplayer-videox-fragment-vid-VidFilFrag */
    void m647x393c2578(int i, boolean z) {
        this.mAdapter.sortVideoList(i, z);
    }

    public void onSortClick() {
        BtmMenDialCont.getInstance().showSortDialogForVideo(this.mContext, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.VidFilFrag$$ExternalSyntheticLambda0
            @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
            public final void onClick(int i, boolean z) {
                VidFilFrag.this.m647x393c2578(i, z);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mPresenter != null) {
            ((VidInfoPre) this.mPresenter).openVideosTab();
        }
        requireActivity().getContentResolver().registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, this.contentObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getContentResolver().unregisterContentObserver(this.contentObserver);
    }

    @Override // com.videoplayer.videox.vie.vid.VidInfVie
    public void updateVideoList(List<VideoInfo> list) {
        this.loading.setVisibility(View.GONE);
        this.refreshLayout.setVisibility(View.VISIBLE);
        this.refreshLayout.setRefreshing(false);
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.updateVideoDataList(list);
        }
    }

    public void setViewMode(int i) {
        if (i == 1) {
            NpaGridLayManager npaGridLayManager = this.mGridLayoutManager;
            if (npaGridLayManager != null) {
                npaGridLayManager.setSpanCount(1);
            }
        } else {
            NpaGridLayManager npaGridLayManager2 = this.mGridLayoutManager;
            if (npaGridLayManager2 != null) {
                npaGridLayManager2.setSpanCount(2);
            }
        }
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.setViewMode(i);
        }
    }

    public void sortVideoList(int i, boolean z) {
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.sortVideoList(i, z);
        }
    }

    public List<VideoInfo> getVideoSelected() {
        VidInfAdapter vidInfAdapter = this.mAdapter;
        return vidInfAdapter != null ? vidInfAdapter.getVideoSelected() : new ArrayList();
    }

    @Override // com.videoplayer.videox.adapter.vid.VidInfAdapter.Callback
    public void onBottomNaviUpdate() {
        FragmentActivity activity = getActivity();
        if (activity instanceof HomPageActivity) {
            ((HomPageActivity) activity).updateBottomNavigation();
        }
    }
}
