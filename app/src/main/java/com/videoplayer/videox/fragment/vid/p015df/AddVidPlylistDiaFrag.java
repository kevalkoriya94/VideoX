package com.videoplayer.videox.fragment.vid.p015df;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.VidTabPagerAdapter;
import com.videoplayer.videox.databinding.DialogFragmentAddVideoBinding;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag;
import com.videoplayer.videox.fragment.vid.tab.VidInfTabFrag;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoPlaylist;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddVidPlylistDiaFrag extends DialogFragment {
    DialogFragmentAddVideoBinding binding;
    private final Callback mCallback;
    private Context mContext;
    private VideoPlaylist mPlaylist;
    private VidTabPagerAdapter mVideoTabPagerAdapter;
    private final int[] mViewMode = {1, 1};

    public interface Callback {
        void onAddVideo(List<VideoInfo> list);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public AddVidPlylistDiaFrag(VideoPlaylist videoPlaylist, Callback callback) {
        this.mPlaylist = videoPlaylist;
        this.mCallback = callback;
    }

    public static AddVidPlylistDiaFrag newInstance(VideoPlaylist videoPlaylist, Callback callback) {
        return new AddVidPlylistDiaFrag(videoPlaylist, callback);
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
        this.binding = DialogFragmentAddVideoBinding.inflate(getLayoutInflater());
        if (this.mPlaylist == null) {
            this.mPlaylist = new VideoPlaylist();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(VidInfTabFrag.newInstance(this.mPlaylist.getVideoList()));
        arrayList.add(VidFoldTabFrag.newInstance(this.mPlaylist, new VidFoldTabFrag.Callback() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidPlylistDiaFrag$$ExternalSyntheticLambda0
            @Override // com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag.Callback
            public final void onDone(List list) {
                AddVidPlylistDiaFrag.this.m654xf662056d(list);
            }
        }));
        VidTabPagerAdapter vidTabPagerAdapter = new VidTabPagerAdapter(requireActivity(), arrayList);
        this.mVideoTabPagerAdapter = vidTabPagerAdapter;
        this.binding.viewPager.setAdapter(vidTabPagerAdapter);
        this.binding.viewPager.setOffscreenPageLimit(2);
        new TabLayoutMediator(this.binding.tabLayoutVideo, this.binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidPlylistDiaFrag$$ExternalSyntheticLambda1
            @Override
            public final void onConfigureTab(TabLayout.Tab tab, int i) {
                AddVidPlylistDiaFrag.onCreateView$1(tab, i);
            }
        }).attach();
        this.binding.tabLayoutVideo.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidPlylistDiaFrag.1
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    AddVidPlylistDiaFrag addVidPlylistDiaFrag = AddVidPlylistDiaFrag.this;
                    addVidPlylistDiaFrag.setViewModeIcon(addVidPlylistDiaFrag.mViewMode[tab.getPosition()]);
                    AddVidPlylistDiaFrag.this.binding.ivViewMode.setVisibility(View.VISIBLE);
                    AddVidPlylistDiaFrag.this.binding.ivSort.setVisibility(View.VISIBLE);
                    AddVidPlylistDiaFrag.this.binding.ivDone.setVisibility(View.VISIBLE);
                    return;
                }
                if (position == 1) {
                    AddVidPlylistDiaFrag addVidPlylistDiaFrag2 = AddVidPlylistDiaFrag.this;
                    addVidPlylistDiaFrag2.setViewModeIcon(addVidPlylistDiaFrag2.mViewMode[tab.getPosition()]);
                    AddVidPlylistDiaFrag.this.binding.ivViewMode.setVisibility(View.VISIBLE);
                    AddVidPlylistDiaFrag.this.binding.ivSort.setVisibility(View.GONE);
                    AddVidPlylistDiaFrag.this.binding.ivDone.setVisibility(View.GONE);
                }
            }
        });
        this.binding.ivSearch.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidPlylistDiaFrag.2
            @Override 
            public void onClick(View v) {
                AddVidPlylistDiaFrag.this.onSearchClick();
            }
        });
        this.binding.ivSort.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidPlylistDiaFrag.3
            @Override 
            public void onClick(View v) {
                AddVidPlylistDiaFrag.this.onSortClick();
            }
        });
        this.binding.ivViewMode.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidPlylistDiaFrag.4
            @Override 
            public void onClick(View v) {
                AddVidPlylistDiaFrag.this.onViewModeClick();
            }
        });
        this.binding.ivDone.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidPlylistDiaFrag.5
            @Override 
            public void onClick(View v) {
                AddVidPlylistDiaFrag.this.onDoneClick();
            }
        });
        this.binding.ivBack.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidPlylistDiaFrag.6
            @Override 
            public void onClick(View v) {
                AddVidPlylistDiaFrag.this.onBackClick();
            }
        });
        return this.binding.getRoot();
    }

    /* renamed from: lambda$onCreateView$0$com-videoplayer-videox-fragment-vid-df-AddVidPlylistDiaFrag */
    void m654xf662056d(List list) {
        Callback callback = this.mCallback;
        if (callback != null) {
            callback.onAddVideo(list);
        }
        dismiss();
    }

    public static void onCreateView$1(TabLayout.Tab tab, int i) {
        if (i == 0) {
            tab.setText(R.string.video);
        } else if (i == 1) {
            tab.setText(R.string.folders);
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onSearchClick() {
        SeaVidDiaFrag.newInstance().show(getChildFragmentManager().beginTransaction(), "dialog_search");
    }

    public void onSortClick() {
        if (this.binding.tabLayoutVideo.getSelectedTabPosition() == 0) {
            BtmMenDialCont.getInstance().showSortDialogForVideo(this.mContext, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.AddVidPlylistDiaFrag$$ExternalSyntheticLambda2
                @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
                public final void onClick(int i, boolean z) {
                    AddVidPlylistDiaFrag.this.m655x4428ee00(i, z);
                }
            });
        }
    }

    /* renamed from: lambda$onSortClick$2$com-videoplayer-videox-fragment-vid-df-AddVidPlylistDiaFrag */
    void m655x4428ee00(int i, boolean z) {
        this.mVideoTabPagerAdapter.sortData(0, i, z);
    }

    public void onViewModeClick() {
        int selectedTabPosition = this.binding.tabLayoutVideo.getSelectedTabPosition();
        int[] iArr = this.mViewMode;
        if (iArr[selectedTabPosition] == 1) {
            iArr[selectedTabPosition] = 2;
        } else {
            iArr[selectedTabPosition] = 1;
        }
        setViewModeIcon(iArr[selectedTabPosition]);
        this.mVideoTabPagerAdapter.setViewMode(selectedTabPosition, this.mViewMode[selectedTabPosition]);
    }

    public void onDoneClick() {
        if (this.binding.tabLayoutVideo.getSelectedTabPosition() == 0) {
            Callback callback = this.mCallback;
            if (callback != null) {
                callback.onAddVideo(this.mVideoTabPagerAdapter.getVideoSelected());
            }
            dismiss();
        }
    }

    public void setViewModeIcon(int i) {
        if (i == 1) {
            this.binding.ivViewMode.setImageResource(R.drawable.baseline_view_list_24);
        } else {
            this.binding.ivViewMode.setImageResource(R.drawable.baseline_grid_view_24);
        }
    }

    public void onBackClick() {
        dismiss();
    }
}
