package com.videoplayer.videox.fragment.mus.p014df;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.MusInfAdapter;
import com.videoplayer.videox.databinding.DialogFragmentPlaylistVideoBinding;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.BasDialFrag;
import com.videoplayer.videox.db.entity.music.MusicArtist;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.music.MusicPlaylist;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.pre.mus.MusicPresenter;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.vie.MusVie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MusPlylistDialFrag extends BasDialFrag<MusicPresenter> implements MusVie, MusInfAdapter.Callback, AddMusPlylistDialFrag.Callback {
    DialogFragmentPlaylistVideoBinding binding;
    private MusInfAdapter mAdapter;
    private Callback mCallback;
    private Context mContext;
    private MusicArtist mMusicArtist;
    private List<MusicInfo> mMusics;
    private MusicPlaylist mPlaylist;
    private int mType;

    public interface Callback {
        void onDialogDismiss();

        void onUpdateMusicPlaylist(List<MusicInfo> list);
    }

    @Override // com.videoplayer.videox.vie.MusVie
    public void onSearchMusic(List<MusicInfo> list) {
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public MusPlylistDialFrag() {
    }

    public MusPlylistDialFrag(int i, MusicPlaylist musicPlaylist, Callback callback) {
        this.mType = i;
        this.mPlaylist = musicPlaylist;
        this.mMusics = musicPlaylist.getMusicList();
        this.mCallback = callback;
    }

    public MusPlylistDialFrag(int i, Callback callback) {
        this.mType = i;
        this.mCallback = callback;
        this.mMusics = new ArrayList();
    }

    public MusPlylistDialFrag(int i, MusicArtist musicArtist) {
        this.mType = i;
        this.mMusicArtist = musicArtist;
        this.mMusics = musicArtist.getMusicList();
    }

    public static MusPlylistDialFrag newInstance(int i, MusicPlaylist musicPlaylist, Callback callback) {
        return new MusPlylistDialFrag(i, musicPlaylist, callback);
    }

    public static MusPlylistDialFrag newInstance(int i, Callback callback) {
        return new MusPlylistDialFrag(i, callback);
    }

    public static MusPlylistDialFrag newInstance(int i, MusicArtist musicArtist) {
        return new MusPlylistDialFrag(i, musicArtist);
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

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onDialogDismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        DialogFragmentPlaylistVideoBinding inflate = DialogFragmentPlaylistVideoBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        inflate.tvNoVideo.setText(R.string.no_songs_in_the_playlist);
        this.binding.tvAddVideo.setText(R.string.add_song);
        if (this.mMusics == null) {
            this.mMusics = new ArrayList();
        }
        if (this.mPlaylist == null) {
            this.mPlaylist = new MusicPlaylist();
        }
        if (this.mMusicArtist == null) {
            this.mMusicArtist = new MusicArtist();
        }
        Context context = this.mContext;
        List<MusicInfo> list = this.mMusics;
        MusInfAdapter musInfAdapter = new MusInfAdapter(context, list, false, this, list);
        this.mAdapter = musInfAdapter;
        this.binding.rvVideoDialog.setAdapter(musInfAdapter);
        this.binding.rvVideoDialog.setLayoutManager(new LinearLayoutManager(this.mContext));
        int i = this.mType;
        if (i == 1) {
            if (this.mMusics.isEmpty()) {
                setViewDataEmpty(true);
                this.binding.ivAddVideo.setVisibility(View.GONE);
            } else {
                setViewDataEmpty(false);
                this.binding.ivAddVideo.setVisibility(View.VISIBLE);
            }
            this.binding.tvFolderName.setText(this.mPlaylist.getPlaylistName());
        } else if (i == 2) {
            this.binding.ivAddVideo.setVisibility(View.GONE);
            this.binding.rlAddVideo.setVisibility(View.GONE);
            setViewDataEmpty(true);
            this.binding.tvFolderName.setText(R.string.favorite);
            this.binding.loading.setVisibility(View.VISIBLE);
            ((MusicPresenter) this.mPresenter).getAllFavoriteMusic();
            this.binding.rlNoVideo.setVisibility(View.GONE);
        } else if (i == 3) {
            this.binding.ivAddVideo.setVisibility(View.GONE);
            this.binding.rlAddVideo.setVisibility(View.GONE);
            setViewDataEmpty(this.mMusics.isEmpty());
            this.binding.tvFolderName.setText(this.mMusicArtist.getArtistName());
            this.binding.rlNoVideo.setVisibility(View.GONE);
        }
        this.binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.1
            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextSubmit(String str) {
                MusPlylistDialFrag.this.binding.searchView.clearFocus();
                return false;
            }

            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextChange(String str) {
                List<MusicInfo> searchMusicByMusicName = Utility.searchMusicByMusicName(MusPlylistDialFrag.this.mMusics, str);
                if (MusPlylistDialFrag.this.mAdapter == null) {
                    return false;
                }
                MusPlylistDialFrag.this.mAdapter.updateMusicDataList(searchMusicByMusicName);
                return false;
            }
        });
        this.binding.ivSort.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.2
            @Override 
            public void onClick(View v) {
                MusPlylistDialFrag.this.onSortClick();
            }
        });
        this.binding.rlAddVideo.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.3
            @Override 
            public void onClick(View v) {
                MusPlylistDialFrag.this.onAddVideoClick();
            }
        });
        this.binding.ivAddVideo.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.4
            @Override 
            public void onClick(View v) {
                MusPlylistDialFrag.this.onAddVideoIconClick();
            }
        });
        this.binding.ivSearch.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.5
            @Override 
            public void onClick(View v) {
                MusPlylistDialFrag.this.onSearchClick();
            }
        });
        this.binding.ivBack.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.6
            @Override 
            public void onClick(View v) {
                MusPlylistDialFrag.this.onBackClick();
            }
        });
        this.binding.tvCancelSearch.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.7
            @Override 
            public void onClick(View v) {
                MusPlylistDialFrag.this.onSearchCancelClick();
            }
        });
        return this.binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mType == 2) {
            ((MusicPresenter) this.mPresenter).getAllFavoriteMusic();
        }
    }

    private void setViewDataEmpty(boolean z) {
        if (z) {
            this.binding.ivSearch.setVisibility(View.GONE);
            this.binding.ivSort.setVisibility(View.GONE);
            this.binding.ivViewMode.setVisibility(View.GONE);
            this.binding.rvVideoDialog.setVisibility(View.GONE);
            this.binding.rlNoVideo.setVisibility(View.VISIBLE);
            return;
        }
        this.binding.rlNoVideo.setVisibility(View.GONE);
        this.binding.rvVideoDialog.setVisibility(View.VISIBLE);
        this.binding.ivSort.setVisibility(View.VISIBLE);
        this.binding.ivViewMode.setVisibility(View.GONE);
        this.binding.ivSearch.setVisibility(View.VISIBLE);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onSearchCancelClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        this.binding.rlSearchView.setVisibility(View.INVISIBLE);
        this.binding.rlTitle.setVisibility(View.VISIBLE);
        MusInfAdapter musInfAdapter = this.mAdapter;
        if (musInfAdapter != null) {
            musInfAdapter.updateMusicDataList(this.mMusics);
        }
    }

    public void onSearchClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        this.binding.rlSearchView.setVisibility(View.VISIBLE);
        this.binding.rlTitle.setVisibility(View.INVISIBLE);
        this.binding.searchView.setFocusable(true);
        this.binding.searchView.setIconified(false);
        this.binding.searchView.requestFocusFromTouch();
    }

    public void onSortClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        BtmMenDialCont.getInstance().showSortDialogForMusic(this.mContext, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag$$ExternalSyntheticLambda0
            @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
            public final void onClick(int i, boolean z) {
                MusPlylistDialFrag.this.m643x5ddfa3a7(i, z);
            }
        });
    }

    /* renamed from: lambda$onSortClick$0$com-videoplayer-videox-fragment-mus-df-MusPlylistDialFrag */
    void m643x5ddfa3a7(int i, boolean z) {
        MusInfAdapter musInfAdapter = this.mAdapter;
        if (musInfAdapter != null) {
            musInfAdapter.sortMusicList(i, z);
        }
    }

    public void onAddVideoClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        AddMusPlylistDialFrag.newInstance(this.mPlaylist, this).show(getChildFragmentManager().beginTransaction(), "dialog_playlist_add_music");
    }

    public void onAddVideoIconClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        onAddVideoClick();
    }

    public void onBackClick() {
        dismiss();
    }

    @Override // com.videoplayer.videox.adapter.mus.MusInfAdapter.Callback
    public void onFavoriteUpdate(int i, boolean z) {
        MusInfAdapter musInfAdapter;
        if (this.mType != 2 || z || (musInfAdapter = this.mAdapter) == null) {
            return;
        }
        musInfAdapter.removeItemPosition(i);
        if (this.mAdapter.isEmptyData()) {
            setViewDataEmpty(true);
        }
    }

    @Override // com.videoplayer.videox.vie.MusVie
    public void openFavoriteMusic(List<MusicInfo> list) {
        MusInfAdapter musInfAdapter = this.mAdapter;
        if (musInfAdapter != null) {
            musInfAdapter.updateMusicDataList(list);
        }
        this.mMusics = list;
        this.binding.loading.setVisibility(View.GONE);
        setViewDataEmpty(this.mMusics.isEmpty());
    }

    @Override // com.videoplayer.videox.fragment.mus.df.AddMusPlylistDialFrag.Callback
    public void onAddMusic(List<MusicInfo> list) {
        this.mMusics = list;
        if (list.isEmpty()) {
            this.binding.ivAddVideo.setVisibility(View.GONE);
        } else {
            this.binding.ivAddVideo.setVisibility(View.VISIBLE);
        }
        this.mPlaylist.setMusicList(list);
        setViewDataEmpty(this.mMusics.isEmpty());
        MusInfAdapter musInfAdapter = this.mAdapter;
        if (musInfAdapter != null) {
            musInfAdapter.updateMusicDataList(list);
        }
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onUpdateMusicPlaylist(list);
        }
        ((MusicPresenter) this.mPresenter).addMusicToPlaylist(this.mPlaylist, list);
    }
}
