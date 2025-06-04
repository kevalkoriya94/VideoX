package com.videoplayer.videox.fragment.vid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.HistActivity;
import com.videoplayer.videox.adapter.vid.VidHistAdapter;
import com.videoplayer.videox.databinding.FragmentVideoManagerBinding;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.fragment.vid.p015df.SeaVidDiaFrag;
import com.videoplayer.videox.fragment.vid.tab.VidInfTabFrag;
import com.videoplayer.videox.db.database.MyDatabase;
import com.videoplayer.videox.db.datasource.VideoDatabaseDataSource;
import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.vid.VidPre;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.vie.vid.VidHistVie;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class VidManFrag extends BasFrag<VidPre> implements VidHistAdapter.Callback, VidHistVie {
    FragmentVideoManagerBinding binding;
    private VidHistAdapter mAdapter;
    private Context mContext;
    private VideoDatabaseDataSource videoDatabaseDataSource;

    @Override
    public void onHistoryOptionSelect(VideoHistory videoHistory, int i, int i2) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static VidManFrag newInstance() {
        return new VidManFrag();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = FragmentVideoManagerBinding.inflate(getLayoutInflater());
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.frameview, VidInfTabFrag.newInstance(), "video").commit();
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.binding.ivSearch.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.VidManFrag$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view2) {
                VidManFrag.this.m649x517bfab2(view2);
            }
        });
        this.binding.ivDelete.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.VidManFrag$$ExternalSyntheticLambda1
            @Override 
            public final void onClick(View view2) {
                VidManFrag.this.m650x8b469c91(view2);
            }
        });
        this.binding.ivHistory.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.VidManFrag$$ExternalSyntheticLambda2
            @Override 
            public final void onClick(View view2) {
                VidManFrag.this.m651xc5113e70(view2);
            }
        });
    }

    /* renamed from: lambda$onViewCreated$0$com-videoplayer-videox-fragment-vid-VidManFrag */
    void m649x517bfab2(View view) {
        onSearchClick();
    }

    /* renamed from: lambda$onViewCreated$1$com-videoplayer-videox-fragment-vid-VidManFrag */
    void m650x8b469c91(View view) {
        onDeleteClick();
    }

    /* renamed from: lambda$onViewCreated$2$com-videoplayer-videox-fragment-vid-VidManFrag */
    void m651xc5113e70(View view) {
        onFolderHistoryClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getHistory() {
        try {
            this.binding.rvHomeHistory.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
            this.videoDatabaseDataSource = new VideoDatabaseDataSource(this.mContext);
            historyVideos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void historyVideos() {
        Single.defer((Callable) () -> Single.just(this.videoDatabaseDataSource.getHistoryVideos())).delay(300L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<VideoHistory>>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<VideoHistory> list) {
                try {
                    mAdapter = new VidHistAdapter(mContext, list, (videoHistory, i, i2) -> {});
                    if(binding.rvHomeHistory!=null){
                        binding.rvHomeHistory.setAdapter(mAdapter);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable th) {
            }
        });
    }

    /* renamed from: lambda$historyVideos$3$com-videoplayer-videox-fragment-vid-VidManFrag */
    Object m648x61805167() throws Exception {
        return Single.just(this.videoDatabaseDataSource.getHistoryVideos());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override // com.videoplayer.videox.fragment.BasFrag
    public VidPre createPresenter() {
        Context context = this.mContext;
        return new VidPre(context, null, new VideoDataRepository(context));
    }

    public void onSearchClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        SeaVidDiaFrag.newInstance().show(getChildFragmentManager().beginTransaction(), "dialog_search");
    }

    public void onSortClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        BtmMenDialCont.getInstance().showSortDialogForVideo(this.mContext, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.VidManFrag$$ExternalSyntheticLambda4
            @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
            public final void onClick(int i, boolean z) {
                VidInfTabFrag.newInstance().sortVideoList(i, z);
            }
        });
    }

    public void onDeleteClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        new QueDiaBuil(this.mContext, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.VidManFrag.2
            @Override 
            public void onCancelClick() {
            }

            @Override 
            public void onOkClick() {
                MyDatabase.getInstance(VidManFrag.this.requireActivity()).videoHistoryDAO().deleteAllVideoHistory();
                VidManFrag.this.getHistory();
            }
        }).setTitle(R.string.delete_all, ContextCompat.getColor(this.mContext, R.color.color_FF6666)).setQuestion(R.string.question_remove_all_history_video).build().show();
    }

    public void onFolderHistoryClick() {
        Intent intent = new Intent(getContext(), (Class<?>) HistActivity.class);
        intent.putExtra("TYPE", "no");
        intent.addFlags(65536);
        startActivity(intent);
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getHistory();
    }

    @Override
    public void updateHistoryVideos(List<VideoHistory> list) {
        try {
            if (list.isEmpty()) {
                this.binding.llHistory.setVisibility(View.GONE);
            } else {
                this.binding.llHistory.setVisibility(View.VISIBLE);
            }
            this.mAdapter = new VidHistAdapter(this.mContext, list, this);
            this.binding.rvHomeHistory.setAdapter(this.mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
