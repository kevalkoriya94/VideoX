package com.videoplayer.videox.fragment.mus.p014df;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.MusTabPaAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.mus.tab.MusInfTabFrag;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.music.MusicPlaylist;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddMusPlylistDialFrag extends DialogFragment implements View.OnClickListener {
    ImageView ivViewMode;
    ImageView iv_back;
    ImageView iv_done;
    ImageView iv_search;
    ImageView iv_sort;
    private final Callback mCallback;
    private Context mContext;
    private MusTabPaAdapter mMusicTabPagerAdapter;
    private final MusicPlaylist mPlaylist;
    TabLayout tabLayoutMusic;
    TextView tvTitle;
    ViewPager2 viewPager;

    public interface Callback {
        void onAddMusic(List<MusicInfo> list);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public AddMusPlylistDialFrag(MusicPlaylist musicPlaylist, Callback callback) {
        this.mPlaylist = musicPlaylist;
        this.mCallback = callback;
    }

    public static AddMusPlylistDialFrag newInstance(MusicPlaylist musicPlaylist, Callback callback) {
        return new AddMusPlylistDialFrag(musicPlaylist, callback);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
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
        View inflate = layoutInflater.inflate(R.layout.dialog_fragment_add_video, viewGroup, false);
        this.tvTitle = (TextView) inflate.findViewById(R.id.tv_title);
        this.tabLayoutMusic = (TabLayout) inflate.findViewById(R.id.tab_layout_video);
        this.ivViewMode = (ImageView) inflate.findViewById(R.id.iv_view_mode);
        this.viewPager = (ViewPager2) inflate.findViewById(R.id.view_pager);
        this.iv_back = (ImageView) inflate.findViewById(R.id.iv_back);
        this.iv_done = (ImageView) inflate.findViewById(R.id.iv_done);
        this.iv_sort = (ImageView) inflate.findViewById(R.id.iv_sort);
        this.iv_search = (ImageView) inflate.findViewById(R.id.iv_search);
        this.tvTitle.setText(R.string.add_new_song);
        this.ivViewMode.setVisibility(View.GONE);
        ArrayList arrayList = new ArrayList();
        arrayList.add(MusInfTabFrag.newInstance(this.mPlaylist.getMusicList()));
        MusTabPaAdapter musTabPaAdapter = new MusTabPaAdapter(requireActivity(), arrayList);
        this.mMusicTabPagerAdapter = musTabPaAdapter;
        this.viewPager.setAdapter(musTabPaAdapter);
        this.viewPager.setOffscreenPageLimit(1);
        new TabLayoutMediator(this.tabLayoutMusic, this.viewPager, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.videoplayer.videox.fragment.mus.df.AddMusPlylistDialFrag$$ExternalSyntheticLambda0
            @Override
            public final void onConfigureTab(TabLayout.Tab tab, int i) {
                AddMusPlylistDialFrag.onCreateView$0(tab, i);
            }
        }).attach();
        this.iv_back.setOnClickListener(this);
        this.iv_done.setOnClickListener(this);
        this.iv_sort.setOnClickListener(this);
        this.iv_search.setOnClickListener(this);
        return inflate;
    }

    @Override 
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            onBackClick();
            return;
        }
        if (v.getId() == R.id.iv_done) {
            onDoneClick();
        } else if (v.getId() == R.id.iv_sort) {
            onSortClick();
        } else if (v.getId() == R.id.iv_search) {
            onSearchClick();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static void onCreateView$0(TabLayout.Tab tab, int i) {
        if (i == 0) {
            tab.setText(R.string.songs);
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onSearchClick() {
        SeaMusDiaFrag.newInstance().show(getChildFragmentManager().beginTransaction(), "dialog_search");
    }

    public void onSortClick() {
        if (this.viewPager.getCurrentItem() == 0) {
            BtmMenDialCont.getInstance().showSortDialogForMusic(this.mContext, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.mus.df.AddMusPlylistDialFrag$$ExternalSyntheticLambda1
                @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
                public final void onClick(int i, boolean z) {
                    AddMusPlylistDialFrag.this.m641x194af93d(i, z);
                }
            });
        }
    }

    /* renamed from: lambda$onSortClick$1$com-videoplayer-videox-fragment-mus-df-AddMusPlylistDialFrag */
    void m641x194af93d(int i, boolean z) {
        this.mMusicTabPagerAdapter.sortData(0, i, z);
    }

    public void onDoneClick() {
        AdmobAdsHelper.ShowFullAds(requireActivity(), false);
        if (this.viewPager.getCurrentItem() == 0) {
            Callback callback = this.mCallback;
            if (callback != null) {
                callback.onAddMusic(this.mMusicTabPagerAdapter.getMusicSelected());
            }
            dismiss();
        }
    }

    public void onBackClick() {
        dismiss();
    }
}
