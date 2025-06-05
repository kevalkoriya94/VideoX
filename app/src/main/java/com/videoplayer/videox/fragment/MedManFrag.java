package com.videoplayer.videox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.VidHistAdapter;
import com.videoplayer.videox.fragment.mus.tab.MusInfTabFrag;
import com.videoplayer.videox.fragment.vid.tab.VidInfTabFrag;
import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.vid.VidPre;
import com.videoplayer.videox.vie.vid.VidHistVie;

import java.util.ArrayList;
import java.util.List;


public class MedManFrag extends BasFrag<VidPre> implements VidHistAdapter.Callback, VidHistVie {
    static String types;
    private Context mContext;
    TabLayout tabLayoutVideo;
    ViewPager2 viewPager;

    @Override
    public void onHistoryOptionSelect(VideoHistory videoHistory, int i, int i2) {
    }

    @Override
    public void updateHistoryVideos(List<VideoHistory> list) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static MedManFrag newInstance(String type) {
        types = type;
        return new MedManFrag();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_history, viewGroup, false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(VidInfTabFrag.newInstance());
        arrayList.add(MusInfTabFrag.newInstance());
        HistoryTabPagerAdapter historyTabPagerAdapter = new HistoryTabPagerAdapter(requireActivity(), arrayList);
        this.tabLayoutVideo = (TabLayout) inflate.findViewById(R.id.tab_layout_video);
        ViewPager2 viewPager2 = (ViewPager2) inflate.findViewById(R.id.view_pager);
        this.viewPager = viewPager2;
        viewPager2.setAdapter(historyTabPagerAdapter);
        this.viewPager.setOffscreenPageLimit(2);
        try {
            if (types.equals("video")) {
                this.viewPager.setCurrentItem(0);
            } else {
                this.viewPager.setCurrentItem(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        new TabLayoutMediator(this.tabLayoutVideo, this.viewPager, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.videoplayer.videox.fragment.MedManFrag$$ExternalSyntheticLambda0
            @Override
            public final void onConfigureTab(TabLayout.Tab tab, int i) {
                MedManFrag.lambda$onCreateView$0(tab, i);
            }
        }).attach();
        return inflate;
    }

    static void lambda$onCreateView$0(TabLayout.Tab tab, int i) {
        if (i == 0) {
            tab.setText("Video");
        } else if (i == 1) {
            tab.setText("Music");
        }
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

    @Override
    public void onResume() {
        super.onResume();
    }

    public static class HistoryTabPagerAdapter extends FragmentStateAdapter {
        final List<Fragment> mVideoTabFragmentList;

        public HistoryTabPagerAdapter(FragmentActivity fragmentActivity, List<Fragment> list) {
            super(fragmentActivity);
            this.mVideoTabFragmentList = list;
        }

        @Override // androidx.viewpager2.adapter.FragmentStateAdapter
        public Fragment createFragment(int i) {
            return this.mVideoTabFragmentList.get(i);
        }

        @Override
        public int getItemCount() {
            return this.mVideoTabFragmentList.size();
        }
    }
}
