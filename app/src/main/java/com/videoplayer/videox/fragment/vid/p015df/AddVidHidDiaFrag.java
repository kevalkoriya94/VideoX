package com.videoplayer.videox.fragment.vid.p015df;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.VidInfAdapter;
import com.videoplayer.videox.databinding.DialogFragmentPlaylistVideoBinding;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.BasDialFrag;
import com.videoplayer.videox.cv.NpaGridLayManager;
import com.videoplayer.videox.db.datasource.VideoDatabaseControl;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.db.utils.VideoFavoriteUtil;
import com.videoplayer.videox.pre.vid.VidInfoPre;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.vie.vid.VidInfVie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AddVidHidDiaFrag extends BasDialFrag<VidInfoPre> implements VidInfVie {
    DialogFragmentPlaylistVideoBinding binding;
    private VidInfAdapter mAdapter;
    private final Callback mCallback;
    private Context mContext;
    private int mCurrentViewMode = 1;
    private NpaGridLayManager mGridLayoutManager;

    public interface Callback {
        void onDoneClick();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public AddVidHidDiaFrag(Callback callback) {
        this.mCallback = callback;
    }

    public static AddVidHidDiaFrag newInstance(Callback callback) {
        return new AddVidHidDiaFrag(callback);
    }

    @Override // com.videoplayer.videox.fragment.BasDialFrag
    public VidInfoPre createPresenter() {
        Context context = this.mContext;
        return new VidInfoPre(context, this, new VideoDataRepository(context));
    }

    @Override // com.videoplayer.videox.fragment.BasDialFrag, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(0, AppCon.Themes.THEMES_STYLE[new SettingPrefUtils(this.mContext).getThemes()]);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        Window window = onCreateDialog.getWindow();
        Objects.requireNonNull(window);
        window.requestFeature(1);
        return onCreateDialog;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = DialogFragmentPlaylistVideoBinding.inflate(getLayoutInflater());
        VidInfAdapter vidInfAdapter = new VidInfAdapter(this.mContext, new ArrayList(), false, true, null, new ArrayList());
        this.mAdapter = vidInfAdapter;
        this.binding.rvVideoDialog.setAdapter(vidInfAdapter);
        NpaGridLayManager npaGridLayManager = new NpaGridLayManager(this.mContext, 1);
        this.mGridLayoutManager = npaGridLayManager;
        this.binding.rvVideoDialog.setLayoutManager(npaGridLayManager);
        this.binding.ivDone.setVisibility(View.VISIBLE);
        this.binding.ivAddVideo.setVisibility(View.GONE);
        this.binding.ivSearch.setVisibility(View.INVISIBLE);
        this.binding.loading.setVisibility(View.VISIBLE);
        this.binding.rvVideoDialog.setVisibility(View.GONE);
        ((VidInfoPre) this.mPresenter).openVideosTab();
        this.binding.ivSort.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidHidDiaFrag.1
            @Override 
            public void onClick(View v) {
                AddVidHidDiaFrag.this.onSortClick();
            }
        });
        this.binding.ivViewMode.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidHidDiaFrag.2
            @Override 
            public void onClick(View v) {
                AddVidHidDiaFrag.this.onViewModeClick();
            }
        });
        this.binding.ivBack.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidHidDiaFrag.3
            @Override 
            public void onClick(View v) {
                AddVidHidDiaFrag.this.onBackClick();
            }
        });
        this.binding.ivDone.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidHidDiaFrag.4
            @Override 
            public void onClick(View v) {
                AddVidHidDiaFrag.this.onDoneClick();
            }
        });
        return this.binding.getRoot();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override // com.videoplayer.videox.vie.vid.VidInfVie
    public void updateVideoList(List<VideoInfo> list) {
        this.binding.loading.setVisibility(View.GONE);
        this.binding.rvVideoDialog.setVisibility(View.VISIBLE);
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.updateVideoDataList(list);
        }
    }

    public void onSortClick() {
        BtmMenDialCont.getInstance().showSortDialogForVideo(this.mContext, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidHidDiaFrag$$ExternalSyntheticLambda1
            @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
            public final void onClick(int i, boolean z) {
                AddVidHidDiaFrag.this.m653x9a7dce16(i, z);
            }
        });
    }

    /* renamed from: lambda$onSortClick$0$com-videoplayer-videox-fragment-vid-df-AddVidHidDiaFrag */
    void m653x9a7dce16(int i, boolean z) {
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.sortVideoList(i, z);
        }
    }

    public void onViewModeClick() {
        if (this.mCurrentViewMode == 1) {
            this.mCurrentViewMode = 2;
            this.binding.ivViewMode.setImageResource(R.drawable.baseline_grid_view_24);
            this.mGridLayoutManager.setSpanCount(2);
        } else {
            this.mCurrentViewMode = 1;
            this.binding.ivViewMode.setImageResource(R.drawable.baseline_view_list_24);
            this.mGridLayoutManager.setSpanCount(1);
        }
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.setViewMode(this.mCurrentViewMode);
        }
    }

    public void onBackClick() {
        dismiss();
    }

    public void onDoneClick() {
        VidInfAdapter vidInfAdapter;
        if (this.mCallback == null || (vidInfAdapter = this.mAdapter) == null) {
            return;
        }
        final List<VideoInfo> videoSelected = vidInfAdapter.getVideoSelected();
        final Dialog dialog = new Dialog(this.mContext);
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        dialog.show();
        Single.defer(new Callable() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidHidDiaFrag$$ExternalSyntheticLambda0
            @Override 
            public final Object call() {
                return AddVidHidDiaFrag.this.m652x410e7cb3(videoSelected);
            }
        }).delay(500L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidHidDiaFrag.5
            @Override // io.reactivex.SingleObserver
            public void onSubscribe(Disposable disposable) {
            }

            @Override // io.reactivex.SingleObserver
            public void onSuccess(Boolean bool) {
                if (AddVidHidDiaFrag.this.mCallback != null) {
                    AddVidHidDiaFrag.this.mCallback.onDoneClick();
                }
                dialog.dismiss();
                AddVidHidDiaFrag.this.dismiss();
            }

            @Override // io.reactivex.SingleObserver
            public void onError(Throwable th) {
                if (AddVidHidDiaFrag.this.mCallback != null) {
                    AddVidHidDiaFrag.this.mCallback.onDoneClick();
                }
                dialog.dismiss();
                AddVidHidDiaFrag.this.dismiss();
            }
        });
    }

    /* renamed from: onDoneList, reason: merged with bridge method [inline-methods] */
    public SingleSource m652x410e7cb3(List list) {
        return Single.just(Boolean.valueOf(hiddenVideo(list)));
    }

    private boolean hiddenVideo(List<VideoInfo> list) {
        for (VideoInfo videoInfo : list) {
            VideoDatabaseControl.getInstance().removeVideoById(videoInfo.getVideoId());
            VideoFavoriteUtil.addFavoriteVideoId(this.mContext, videoInfo.getVideoId(), false);
            Utility.setHiddenAVideo(this.mContext, videoInfo);
        }
        return true;
    }
}
