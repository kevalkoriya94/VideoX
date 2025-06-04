package com.videoplayer.videox.fragment.mus.p014df;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.MusPlayActivity;
import com.videoplayer.videox.adapter.mus.MusInfAdapter;
import com.videoplayer.videox.databinding.DialogFragmentMusicAlbumBinding;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.BasDialFrag;
import com.videoplayer.videox.db.entity.music.MusicAlbum;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.pre.mus.MusAlbPre;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.vie.mus.MusAlbVie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class MusAlbDialFrag extends BasDialFrag<MusAlbPre> implements MusAlbVie, MusInfAdapter.Callback {
    DialogFragmentMusicAlbumBinding binding;
    private MusInfAdapter mAdapter;
    private Context mContext;
    private MusicAlbum mMusicAlbum;
    private final Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
    private List<MusicInfo> mMusics = new ArrayList();

    public interface Callback {
    }

    @Override // com.videoplayer.videox.adapter.mus.MusInfAdapter.Callback
    public void onFavoriteUpdate(int i, boolean z) {
    }

    @Override // com.videoplayer.videox.vie.mus.MusAlbVie
    public void onUpdateAlbum(List<MusicAlbum> list) {
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public MusAlbDialFrag() {
    }

    public MusAlbDialFrag(MusicAlbum musicAlbum, Callback callback) {
        this.mMusicAlbum = musicAlbum;
    }

    public static MusAlbDialFrag newInstance(MusicAlbum musicAlbum, Callback callback) {
        return new MusAlbDialFrag(musicAlbum, callback);
    }

    @Override // com.videoplayer.videox.fragment.BasDialFrag
    public MusAlbPre createPresenter() {
        Context context = this.mContext;
        return new MusAlbPre(context, this, new MusicDataRepository(context));
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
        DialogFragmentMusicAlbumBinding inflate = DialogFragmentMusicAlbumBinding.inflate(getLayoutInflater());
        this.binding = inflate;
        MusicAlbum musicAlbum = this.mMusicAlbum;
        if (musicAlbum == null) {
            dismiss();
        } else {
            inflate.tvTitle.setText(musicAlbum.getAlbumName());
            this.binding.tvTitle2.setText(this.mMusicAlbum.getAlbumName());
            this.binding.tvArtist.setText(this.mMusicAlbum.getArtistName());
            Glide.with(this.mContext).load(ContentUris.withAppendedId(this.artworkUri, this.mMusicAlbum.getAlbumId())).placeholder(R.drawable.disc_icn).centerCrop().error(R.drawable.disc_icn).into(this.binding.ivAlbumArt);
            this.mAdapter = new MusInfAdapter(this.mContext, new ArrayList(), false, this, new ArrayList());
            this.binding.rvContent.setLayoutManager(new LinearLayoutManager(this.mContext));
            this.binding.rvContent.setAdapter(this.mAdapter);
            ((MusAlbPre) this.mPresenter).getAllMusicOfAlbum(this.mMusicAlbum);
            this.binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusAlbDialFrag.1
                @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
                public boolean onQueryTextSubmit(String str) {
                    MusAlbDialFrag.this.binding.searchView.clearFocus();
                    return false;
                }

                @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
                public boolean onQueryTextChange(String str) {
                    List<MusicInfo> searchMusicByMusicName = Utility.searchMusicByMusicName(MusAlbDialFrag.this.mMusics, str);
                    if (MusAlbDialFrag.this.mAdapter == null) {
                        return false;
                    }
                    MusAlbDialFrag.this.mAdapter.updateMusicDataList(searchMusicByMusicName);
                    return false;
                }
            });
        }
        this.binding.ivSearch.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusAlbDialFrag.2
            @Override 
            public void onClick(View v) {
                MusAlbDialFrag.this.onSearchClick();
            }
        });
        this.binding.tvCancelSearch.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusAlbDialFrag.3
            @Override 
            public void onClick(View v) {
                MusAlbDialFrag.this.onCancelSearchClick();
            }
        });
        this.binding.ivSort.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusAlbDialFrag.4
            @Override 
            public void onClick(View v) {
                MusAlbDialFrag.this.onSortClick();
            }
        });
        this.binding.llPlay.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusAlbDialFrag.5
            @Override 
            public void onClick(View v) {
                MusAlbDialFrag.this.onPlayClick();
            }
        });
        this.binding.ivBack.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusAlbDialFrag.6
            @Override 
            public void onClick(View v) {
                MusAlbDialFrag.this.onBackClick();
            }
        });
        return this.binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onSearchClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        this.binding.rlSearchView.setVisibility(View.VISIBLE);
        this.binding.rlTitle.setVisibility(View.INVISIBLE);
        this.binding.searchView.setFocusable(true);
        this.binding.searchView.setIconified(false);
        this.binding.searchView.requestFocusFromTouch();
    }

    public void onCancelSearchClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        this.binding.rlTitle.setVisibility(View.VISIBLE);
        this.binding.rlSearchView.setVisibility(View.INVISIBLE);
        MusInfAdapter musInfAdapter = this.mAdapter;
        if (musInfAdapter != null) {
            musInfAdapter.updateMusicDataList(this.mMusics);
        }
    }

    @Override // com.videoplayer.videox.vie.mus.MusAlbVie
    public void onUpdateMusicList(List<MusicInfo> list) {
        if (this.mMusicAlbum != null) {
            this.mMusics = list;
            MusInfAdapter musInfAdapter = this.mAdapter;
            if (musInfAdapter != null) {
                musInfAdapter.updateMusicDataList(list);
            }
            Iterator<MusicInfo> it = list.iterator();
            long j = 0;
            while (it.hasNext()) {
                j += it.next().getDuration();
            }
            long j2 = j / 60000;
            long lastYear = this.mMusicAlbum.getLastYear();
            Log.d("binhnk08 ", "last year = " + lastYear + ", totalTime = " + j2);
            if (lastYear > 0) {
                this.binding.tvInfoAlbum.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_album_info, (int) this.mMusicAlbum.getNumberOfSongs(), Long.valueOf(this.mMusicAlbum.getLastYear()), Long.valueOf(this.mMusicAlbum.getNumberOfSongs()), Long.valueOf(j2)));
            } else {
                this.binding.tvInfoAlbum.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_album_info_2, (int) this.mMusicAlbum.getNumberOfSongs(), Long.valueOf(this.mMusicAlbum.getNumberOfSongs()), Long.valueOf(j2)));
            }
        }
    }

    public void onSortClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        BtmMenDialCont.getInstance().showSortDialogForMusic(this.mContext, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.MusAlbDialFrag$$ExternalSyntheticLambda0
            @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
            public final void onClick(int i, boolean z) {
                MusAlbDialFrag.this.m642xb0a9ca23(i, z);
            }
        });
    }

    /* renamed from: lambda$onSortClick$0$com-videoplayer-videox-fragment-mus-df-MusAlbDialFrag */
    void m642xb0a9ca23(int i, boolean z) {
        this.mAdapter.sortMusicList(i, z);
    }

    public void onPlayClick() {
        List<MusicInfo> list = this.mMusics;
        if (list == null || list.isEmpty()) {
            return;
        }
        Intent intent = new Intent(this.mContext, (Class<?>) MusPlayActivity.class);
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_NUMBER, 0);
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_ARRAY, (ArrayList) getAllMusicId());
        intent.addFlags(C.ENCODING_PCM_32BIT);
        intent.addFlags(65536);
        this.mContext.startActivity(intent);
        AdmobAdsHelper.ShowFullAds(this.mContext, false);
    }

    private List<Long> getAllMusicId() {
        ArrayList arrayList = new ArrayList();
        List<MusicInfo> list = this.mMusics;
        if (list == null) {
            return new ArrayList();
        }
        Iterator<MusicInfo> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(Long.valueOf(it.next().getId()));
        }
        return arrayList;
    }

    public void onBackClick() {
        dismiss();
    }
}
