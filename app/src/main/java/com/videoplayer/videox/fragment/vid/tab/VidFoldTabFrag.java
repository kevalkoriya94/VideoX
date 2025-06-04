package com.videoplayer.videox.fragment.vid.tab;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.exoplayer2.C;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.VidPlayActivity;
import com.videoplayer.videox.adapter.vid.VidFoldAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.FoldInfDialBuil;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.fragment.vid.p015df.VidPlylistDiaFrag;
import com.videoplayer.videox.cv.NpaGridLayManager;
import com.videoplayer.videox.db.datasource.VideoDatabaseControl;
import com.videoplayer.videox.db.entity.video.VideoFolder;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoPlaylist;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.db.utils.VideoFavoriteUtil;
import com.videoplayer.videox.pre.vid.VidFolPre;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.vie.vid.VidFoldVie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class VidFoldTabFrag extends BasFrag<VidFolPre> implements VidFoldVie, VidFoldAdapter.Callback, VidPlylistDiaFrag.FolderCallback {
    private final ContentObserver contentObserver;
    ImageView ivViewMode;
    private ProgressBar loading;
    private VidFoldAdapter mAdapter;
    private Callback mCallback;
    private Context mContext;
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsSelectMode;
    private final VideoPlaylist mPlaylist;
    private final int[] mViewMode;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvTotalFolder;

    public interface Callback {
        void onDone(List<VideoInfo> list);
    }

    public VidFoldTabFrag() {
        this.mViewMode = new int[]{1, 1, 1};
        this.contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                if (VidFoldTabFrag.this.mPresenter != null) {
                    ((VidFolPre) VidFoldTabFrag.this.mPresenter).openFoldersTab();
                }
            }
        };
        this.mPlaylist = new VideoPlaylist();
    }

    public VidFoldTabFrag(VideoPlaylist videoPlaylist, Callback callback) {
        this.mViewMode = new int[]{1, 1, 1};
        this.contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                if (VidFoldTabFrag.this.mPresenter != null) {
                    ((VidFolPre) VidFoldTabFrag.this.mPresenter).openFoldersTab();
                }
            }
        };
        this.mPlaylist = videoPlaylist;
        this.mIsSelectMode = true;
        this.mCallback = callback;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static VidFoldTabFrag newInstance() {
        return new VidFoldTabFrag();
    }

    public static VidFoldTabFrag newInstance(VideoPlaylist videoPlaylist, Callback callback) {
        return new VidFoldTabFrag(videoPlaylist, callback);
    }

    @Override // com.videoplayer.videox.fragment.BasFrag
    public VidFolPre createPresenter() {
        Context context = this.mContext;
        return new VidFolPre(context, this, new VideoDataRepository(context));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video_folder_tab, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.rv_content_tab);
        this.refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        this.loading = (ProgressBar) inflate.findViewById(R.id.loading);
        this.tvTotalFolder = (TextView) inflate.findViewById(R.id.tv_total);
        NpaGridLayManager npaGridLayManager = new NpaGridLayManager(this.mContext, 1);
        this.mGridLayoutManager = npaGridLayManager;
        npaGridLayManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag.3
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                return VidFoldTabFrag.this.mGridLayoutManager.getSpanCount() == 2 ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(this.mGridLayoutManager);
        VidFoldAdapter vidFoldAdapter = new VidFoldAdapter(this.mContext, new ArrayList(), this.mIsSelectMode, this);
        this.mAdapter = vidFoldAdapter;
        recyclerView.setAdapter(vidFoldAdapter);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag$$ExternalSyntheticLambda0
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                VidFoldTabFrag.this.m659x5dc667ee();
            }
        });
        return inflate;
    }

    /* renamed from: lambda$onCreateView$0$com-videoplayer-videox-fragment-vid-tab-VidFoldTabFrag */
    void m659x5dc667ee() {
        if (this.mPresenter != null) {
            ((VidFolPre) this.mPresenter).openFoldersTab();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_view_mode);
        this.ivViewMode = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag$$ExternalSyntheticLambda1
            @Override 
            public final void onClick(View view2) {
                VidFoldTabFrag.this.m660xe739d5c3(view2);
            }
        });
        view.findViewById(R.id.iv_sort).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag$$ExternalSyntheticLambda2
            @Override 
            public final void onClick(View view2) {
                VidFoldTabFrag.this.m662xe9a67b81(view2);
            }
        });
    }

    /* renamed from: lambda$onViewCreated$1$com-videoplayer-videox-fragment-vid-tab-VidFoldTabFrag */
    void m660xe739d5c3(View view) {
        try {
            AdmobAdsHelper.ShowFullAds(requireActivity(), false);
            int[] iArr = this.mViewMode;
            if (iArr[0] == 1) {
                iArr[0] = 2;
            } else {
                iArr[0] = 1;
            }
            setViewModeIcon(iArr[0]);
            setViewMode(this.mViewMode[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: lambda$onViewCreated$3$com-videoplayer-videox-fragment-vid-tab-VidFoldTabFrag */
    void m662xe9a67b81(View view) {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        BtmMenDialCont.getInstance().showSortDialogForVideo(requireActivity(), new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag$$ExternalSyntheticLambda3
            @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
            public final void onClick(int i, boolean z) {
                VidFoldTabFrag.this.m661xe87028a2(i, z);
            }
        });
    }

    /* renamed from: lambda$onViewCreated$2$com-videoplayer-videox-fragment-vid-tab-VidFoldTabFrag */
    void m661xe87028a2(int i, boolean z) {
        sortFolderList(i);
    }

    public void setViewModeIcon(int i) {
        if (i == 1) {
            this.ivViewMode.setImageResource(R.drawable.baseline_view_list_24);
        } else {
            this.ivViewMode.setImageResource(R.drawable.baseline_grid_view_24);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mPresenter != null) {
            ((VidFolPre) this.mPresenter).openFoldersTab();
        }
        requireActivity().getContentResolver().registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, this.contentObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getContentResolver().unregisterContentObserver(this.contentObserver);
    }

    public void setViewMode(int i) {
        GridLayoutManager gridLayoutManager = this.mGridLayoutManager;
        if (gridLayoutManager != null) {
            if (i == 1) {
                gridLayoutManager.setSpanCount(1);
            } else {
                gridLayoutManager.setSpanCount(3);
            }
        }
        VidFoldAdapter vidFoldAdapter = this.mAdapter;
        if (vidFoldAdapter != null) {
            vidFoldAdapter.setViewMode(i);
        }
    }

    @Override // com.videoplayer.videox.vie.vid.VidFoldVie
    public void updateFolderList(List<VideoFolder> list) {
        this.loading.setVisibility(View.GONE);
        this.refreshLayout.setVisibility(View.VISIBLE);
        this.tvTotalFolder.setText(getString(R.string.all_folder, Integer.valueOf(list.size() + 1)));
        this.refreshLayout.setRefreshing(false);
        VidFoldAdapter vidFoldAdapter = this.mAdapter;
        if (vidFoldAdapter != null) {
            vidFoldAdapter.updateVideoFolders(list);
        }
    }

    public void sortFolderList(int i) {
        this.mAdapter.sortFolderList(i);
    }

    public class AnonymousClass3 implements QueDiaBuil.OkButtonClickListener {
        final int val$position;
        final VideoFolder val$videoFolder;

        @Override 
        public void onCancelClick() {
        }

        AnonymousClass3(VideoFolder videoFolder, int i) {
            this.val$videoFolder = videoFolder;
            this.val$position = i;
        }

        @Override 
        public void onOkClick() {
            final Dialog dialog = new Dialog(VidFoldTabFrag.this.mContext);
            dialog.getWindow().requestFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.dialog_loading);
            dialog.setCancelable(false);
            dialog.show();
            Single.defer(new Callable() { // from class: com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag$AnonymousClass3$$ExternalSyntheticLambda0
                @Override 
                public final Object call() {
                    return Single.just(Boolean.valueOf(VidFoldTabFrag.this.deleteFolder(val$videoFolder)));
                }
            }).delay(500L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() { // from class: com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag.AnonymousClass3.1
                @Override // io.reactivex.SingleObserver
                public void onSubscribe(Disposable disposable) {
                }

                @Override // io.reactivex.SingleObserver
                public void onSuccess(Boolean bool) {
                    if (VidFoldTabFrag.this.mAdapter != null) {
                        VidFoldTabFrag.this.mAdapter.removeItemPosition(AnonymousClass3.this.val$position);
                        VidFoldTabFrag.this.mAdapter.updateRecently();
                    }
                    dialog.dismiss();
                }

                @Override // io.reactivex.SingleObserver
                public void onError(Throwable th) {
                    dialog.dismiss();
                }
            });
        }


        public SingleSource lambda$onOkClick$0$VideoFolderTabFragment$3(VideoFolder videoFolder) {
            return Single.just(Boolean.valueOf(VidFoldTabFrag.this.deleteFolder(videoFolder)));
        }
    }

    @Override // com.videoplayer.videox.adapter.vid.VidFoldAdapter.Callback
    public void onFolderOptionSelect(VideoFolder videoFolder, int i, int i2) {
        if (i2 == 0) {
            Intent intent = new Intent(this.mContext, (Class<?>) VidPlayActivity.class);
            intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, 0);
            intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_AUDIO_MODE, true);
            intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_ARRAY, (ArrayList) getAllVideoId(videoFolder));
            intent.addFlags(C.ENCODING_PCM_32BIT);
            intent.addFlags(65536);
            AdmobAdsHelper.ShowFullAdsIntent(this.mContext, intent);
            return;
        }
        if (i2 == 1) {
            AdmobAdsHelper.showAdsNumberCount++;
            new QueDiaBuil(this.mContext, new AnonymousClass3(videoFolder, i)).setTitle(R.string.delete, ContextCompat.getColor(this.mContext, R.color.color_FF6666)).setQuestion(R.string.question_remove_file).build().show();
        } else if (i2 == 2) {
            AdmobAdsHelper.showAdsNumberCount++;
            new FoldInfDialBuil(this.mContext, videoFolder).build().show();
        }
    }

    private List<Long> getAllVideoId(VideoFolder videoFolder) {
        if (videoFolder == null) {
            return new ArrayList();
        }
        List<VideoInfo> videoList = videoFolder.getVideoList();
        ArrayList arrayList = new ArrayList();
        Iterator<VideoInfo> it = videoList.iterator();
        while (it.hasNext()) {
            arrayList.add(Long.valueOf(it.next().getVideoId()));
        }
        return arrayList;
    }

    public boolean deleteFolder(VideoFolder videoFolder) {
        for (VideoInfo videoInfo : videoFolder.getVideoList()) {
            VideoDatabaseControl.getInstance().removeVideoById(videoInfo.getVideoId());
            VideoFavoriteUtil.addFavoriteVideoId(this.mContext, videoInfo.getVideoId(), false);
            Utility.deleteAVideo(this.mContext, videoInfo);
        }
        return true;
    }

    @Override // com.videoplayer.videox.adapter.vid.VidFoldAdapter.Callback
    public void onFolderClick(VideoFolder videoFolder, int i) {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        if (i == 0) {
            VidPlylistDiaFrag.newInstance(4, this.mIsSelectMode, this.mPlaylist, this).show(getChildFragmentManager().beginTransaction(), "dialog_recently_video");
        } else if (videoFolder != null) {
            VidPlylistDiaFrag.newInstance(1, videoFolder, this.mIsSelectMode, this.mPlaylist, this).show(getChildFragmentManager().beginTransaction(), "dialog_folder_video");
        }
    }

    @Override // com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.FolderCallback
    public void onAddVideoClick(List<VideoInfo> list) {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onDone(list);
        }
    }
}
