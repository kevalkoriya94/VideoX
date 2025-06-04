package com.videoplayer.videox.fragment.vid.p015df;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.VidInfAdapter;
import com.videoplayer.videox.fragment.BasDialFrag;
import com.videoplayer.videox.cv.NpaGridLayManager;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.vid.VidPre;
import com.videoplayer.videox.vie.VidVie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SeaVidDiaFrag extends BasDialFrag<VidPre> implements VidVie, VidInfAdapter.Callback {
    private VidInfAdapter mAdapter;
    private Context mContext;
    RecyclerView rvSearchResult;
    SearchView searchView;

    @Override // com.videoplayer.videox.adapter.vid.VidInfAdapter.Callback
    public void onBottomNaviUpdate() {
    }

    @Override // com.videoplayer.videox.adapter.vid.VidInfAdapter.Callback
    public void onFavoriteUpdate(int i, boolean z) {
    }

    @Override // com.videoplayer.videox.vie.VidVie
    public void openFavoriteVideo(List<VideoInfo> list) {
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static SeaVidDiaFrag newInstance() {
        return new SeaVidDiaFrag();
    }

    @Override // com.videoplayer.videox.fragment.BasDialFrag
    public VidPre createPresenter() {
        Context context = this.mContext;
        return new VidPre(context, this, new VideoDataRepository(context));
    }

    @Override // com.videoplayer.videox.fragment.BasDialFrag, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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
        View inflate = layoutInflater.inflate(R.layout.dialog_fragment_search, viewGroup, false);
        this.rvSearchResult = (RecyclerView) inflate.findViewById(R.id.rv_search_result);
        this.searchView = (SearchView) inflate.findViewById(R.id.search_view);
        inflate.findViewById(R.id.tv_cancel_search).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.SeaVidDiaFrag$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                SeaVidDiaFrag.this.m656x32956a82(view);
            }
        });
        VidInfAdapter vidInfAdapter = new VidInfAdapter(this.mContext, new ArrayList(), false, false, this, new ArrayList());
        this.mAdapter = vidInfAdapter;
        this.rvSearchResult.setAdapter(vidInfAdapter);
        this.rvSearchResult.setLayoutManager(new NpaGridLayManager(this.mContext, 1));
        this.searchView.setFocusable(true);
        this.searchView.setIconified(false);
        this.searchView.requestFocusFromTouch();
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.videoplayer.videox.fragment.vid.df.SeaVidDiaFrag.1
            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextSubmit(String str) {
                SeaVidDiaFrag.this.searchView.clearFocus();
                return false;
            }

            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextChange(String str) {
                if (SeaVidDiaFrag.this.mPresenter == null) {
                    return false;
                }
                ((VidPre) SeaVidDiaFrag.this.mPresenter).searchVideoByVideoName(str);
                return false;
            }
        });
        if (this.mPresenter != null) {
            ((VidPre) this.mPresenter).searchVideoByVideoName("");
        }
        return inflate;
    }

    /* renamed from: lambda$onCreateView$0$com-videoplayer-videox-fragment-vid-df-SeaVidDiaFrag */
    void m656x32956a82(View view) {
        onSearchCancelClick();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onSearchCancelClick() {
        dismiss();
    }

    @Override // com.videoplayer.videox.vie.VidVie
    public void onSearchVideo(List<VideoInfo> list) {
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.updateVideoDataList(list);
        }
    }
}
