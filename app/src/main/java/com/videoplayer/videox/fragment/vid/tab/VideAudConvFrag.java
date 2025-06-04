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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.exoplayer2.C;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.VidPlayActivity;
import com.videoplayer.videox.adapter.vid.VidFolAudAdapter;
import com.videoplayer.videox.dialog.FoldInfDialBuil;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.fragment.vid.p015df.VidPlylist2DiagFrag;
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


public class VideAudConvFrag extends BasFrag<VidFolPre> implements VidFoldVie, VidFolAudAdapter.Callback, VidPlylist2DiagFrag.FolderCallback {
    private final ContentObserver contentObserver;
    private ProgressBar loading;
    private VidFolAudAdapter mAdapter;
    private final Callback mCallback;
    private Context mContext;
    private boolean mIsSelectMode;
    private final VideoPlaylist mPlaylist;
    private SwipeRefreshLayout refreshLayout;

    public interface Callback {
        void onDone(List<VideoInfo> list);
    }

    public VideAudConvFrag(Callback callback) {
        this.mCallback = callback;
        this.contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.vid.tab.VideAudConvFrag.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                if (VideAudConvFrag.this.mPresenter != null) {
                    ((VidFolPre) VideAudConvFrag.this.mPresenter).openFoldersTab();
                }
            }
        };
        this.mPlaylist = new VideoPlaylist();
    }

    public VideAudConvFrag(VideoPlaylist videoPlaylist, Callback callback) {
        this.contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.vid.tab.VideAudConvFrag.2
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                if (VideAudConvFrag.this.mPresenter != null) {
                    ((VidFolPre) VideAudConvFrag.this.mPresenter).openFoldersTab();
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

    public static VideAudConvFrag newInstance(Callback callback) {
        return new VideAudConvFrag(callback);
    }

    public static VideAudConvFrag newInstance(VideoPlaylist videoPlaylist, Callback callback) {
        return new VideAudConvFrag(videoPlaylist, callback);
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
        recyclerView.setLayoutManager(new GridLayoutManager(this.mContext, 3));
        VidFolAudAdapter vidFolAudAdapter = new VidFolAudAdapter(this.mContext, new ArrayList(), this.mIsSelectMode, this);
        this.mAdapter = vidFolAudAdapter;
        recyclerView.setAdapter(vidFolAudAdapter);
        vidFolAudAdapter.setViewMode(2);
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VideAudConvFrag$$ExternalSyntheticLambda0
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                VideAudConvFrag.this.m670x34b48949();
            }
        });
        return inflate;
    }

    /* renamed from: lambda$onCreateView$0$com-videoplayer-videox-fragment-vid-tab-VideAudConvFrag */
    void m670x34b48949() {
        if (this.mPresenter != null) {
            ((VidFolPre) this.mPresenter).openFoldersTab();
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

    @Override // com.videoplayer.videox.vie.vid.VidFoldVie
    public void updateFolderList(List<VideoFolder> list) {
        this.loading.setVisibility(View.GONE);
        this.refreshLayout.setVisibility(View.VISIBLE);
        this.refreshLayout.setRefreshing(false);
        VidFolAudAdapter vidFolAudAdapter = this.mAdapter;
        if (vidFolAudAdapter != null) {
            vidFolAudAdapter.updateVideoFolders(list);
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
            final Dialog dialog = new Dialog(VideAudConvFrag.this.mContext);
            dialog.getWindow().requestFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.dialog_loading);
            dialog.setCancelable(false);
            dialog.show();
            Single.defer((Callable) () -> AnonymousClass3.this.lambda$onOkClick$0$VideoFolderTabFragment$3(AnonymousClass3.this.val$videoFolder)).delay(500L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() {
                @Override
                public void onSubscribe(Disposable disposable) {
                }

                public void onSuccess(Boolean bool) {
                    if (mAdapter != null) {
                        mAdapter.removeItemPosition(AnonymousClass3.this.val$position);
                        mAdapter.updateRecently();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onError(Throwable th) {
                    dialog.dismiss();
                }
            });
        }

        public SingleSource lambda$onOkClick$0$VideoFolderTabFragment$3(VideoFolder videoFolder) {
            return Single.just(Boolean.valueOf(VideAudConvFrag.this.deleteFolder(videoFolder)));
        }
    }

    @Override // com.videoplayer.videox.adapter.vid.VidFolAudAdapter.Callback
    public void onFolderOptionSelect(VideoFolder videoFolder, int i, int i2) {
        if (i2 != 0) {
            if (i2 == 1) {
                new QueDiaBuil(this.mContext, new AnonymousClass3(videoFolder, i)).setTitle(R.string.delete, ContextCompat.getColor(this.mContext, R.color.color_FF6666)).setQuestion(R.string.question_remove_file).build().show();
                return;
            } else {
                if (i2 == 2) {
                    new FoldInfDialBuil(this.mContext, videoFolder).build().show();
                    return;
                }
                return;
            }
        }
        Intent intent = new Intent(this.mContext, (Class<?>) VidPlayActivity.class);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, 0);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_AUDIO_MODE, true);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_ARRAY, (ArrayList) getAllVideoId(videoFolder));
        intent.addFlags(C.ENCODING_PCM_32BIT);
        intent.addFlags(65536);
        AdmobAdsHelper.ShowFullAdsIntent(this.mContext, intent);
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

    @Override // com.videoplayer.videox.adapter.vid.VidFolAudAdapter.Callback
    public void onFolderClick(VideoFolder videoFolder, int i) {
        Log.e("TAG", "onFolderClick: " + i);
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        if (i == 0) {
            VidPlylist2DiagFrag.newInstance(4, this.mIsSelectMode, this.mPlaylist, this).show(getChildFragmentManager().beginTransaction(), "dialog_recently_video");
        } else if (videoFolder != null) {
            VidPlylist2DiagFrag.newInstance(1, videoFolder, this.mIsSelectMode, this.mPlaylist, this).show(getChildFragmentManager().beginTransaction(), "dialog_folder_video");
        }
    }

    @Override // com.videoplayer.videox.fragment.vid.df.VidPlylist2DiagFrag.FolderCallback
    public void onAddVideoClick(List<VideoInfo> list) {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onDone(list);
        }
    }
}
