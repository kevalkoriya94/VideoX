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
import com.videoplayer.videox.fragment.mus.MusFilFrag;
import com.videoplayer.videox.fragment.vid.VidFilFrag;
import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.vid.VidPre;
import com.videoplayer.videox.vie.vid.VidHistVie;

import java.util.ArrayList;
import java.util.List;


public class MedFilFrag extends BasFrag<VidPre> implements VidHistAdapter.Callback, VidHistVie {
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

    public static MedFilFrag newInstance() {
        return new MedFilFrag();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_history, viewGroup, false);
        ArrayList arrayList = new ArrayList();
        arrayList.add(VidFilFrag.newInstance());
        arrayList.add(MusFilFrag.newInstance());
        this.tabLayoutVideo = (TabLayout) inflate.findViewById(R.id.tab_layout_video);
        this.viewPager = (ViewPager2) inflate.findViewById(R.id.view_pager);
        this.viewPager.setAdapter(new HistoryTabPagerAdapter(requireActivity(), arrayList));
        this.viewPager.setOffscreenPageLimit(2);
        new TabLayoutMediator(this.tabLayoutVideo, this.viewPager, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.videoplayer.videox.fragment.MedFilFrag$$ExternalSyntheticLambda0
            @Override
            public final void onConfigureTab(TabLayout.Tab tab, int i) {
                MedFilFrag.lambda$onCreateView$0(tab, i);
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
