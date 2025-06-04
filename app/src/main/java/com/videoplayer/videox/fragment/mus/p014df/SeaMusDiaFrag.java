package com.videoplayer.videox.fragment.mus.p014df;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.MusInfAdapter;
import com.videoplayer.videox.fragment.BasDialFrag;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.pre.mus.MusicPresenter;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.vie.MusVie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SeaMusDiaFrag extends BasDialFrag<MusicPresenter> implements MusVie, MusInfAdapter.Callback {
    private MusInfAdapter mAdapter;
    private Context mContext;
    RecyclerView rvSearchResult;
    SearchView searchView;

    @Override // com.videoplayer.videox.adapter.mus.MusInfAdapter.Callback
    public void onFavoriteUpdate(int i, boolean z) {
    }

    @Override // com.videoplayer.videox.vie.MusVie
    public void openFavoriteMusic(List<MusicInfo> list) {
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static SeaMusDiaFrag newInstance() {
        return new SeaMusDiaFrag();
    }

    @Override // com.videoplayer.videox.fragment.BasDialFrag
    public MusicPresenter createPresenter() {
        Context context = this.mContext;
        return new MusicPresenter(context, this, new MusicDataRepository(context));
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
        View inflate = layoutInflater.inflate(R.layout.dialog_fragment_search, viewGroup, false);
        this.rvSearchResult = (RecyclerView) inflate.findViewById(R.id.rv_search_result);
        this.searchView = (SearchView) inflate.findViewById(R.id.search_view);
        MusInfAdapter musInfAdapter = new MusInfAdapter(this.mContext, new ArrayList(), false, this, new ArrayList());
        this.mAdapter = musInfAdapter;
        this.rvSearchResult.setAdapter(musInfAdapter);
        this.rvSearchResult.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.searchView.setFocusable(true);
        this.searchView.setIconified(false);
        this.searchView.requestFocusFromTouch();
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.videoplayer.videox.fragment.mus.df.SeaMusDiaFrag.1
            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextSubmit(String str) {
                SeaMusDiaFrag.this.searchView.clearFocus();
                return false;
            }

            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextChange(String str) {
                ((MusicPresenter) SeaMusDiaFrag.this.mPresenter).searchMusicByMusicName(str);
                return false;
            }
        });
        ((MusicPresenter) this.mPresenter).searchMusicByMusicName("");
        inflate.findViewById(R.id.tv_cancel_search).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.SeaMusDiaFrag.2
            @Override 
            public void onClick(View v) {
                SeaMusDiaFrag.this.onSearchCancelClick();
            }
        });
        return inflate;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onSearchCancelClick() {
        dismiss();
    }

    @Override // com.videoplayer.videox.vie.MusVie
    public void onSearchMusic(List<MusicInfo> list) {
        MusInfAdapter musInfAdapter = this.mAdapter;
        if (musInfAdapter != null) {
            musInfAdapter.updateMusicDataList(list);
        }
    }
}
