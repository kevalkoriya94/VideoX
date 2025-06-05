package com.videoplayer.videox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.videoplayer.videox.adapter.vid.VidHistAdapter;
import com.videoplayer.videox.databinding.FragmentHistoryBinding;
import com.videoplayer.videox.fragment.mus.tab.MusHistTabFrag;
import com.videoplayer.videox.fragment.vid.tab.VidHistTabFrag;
import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.vid.VidPre;
import com.videoplayer.videox.vie.vid.VidHistVie;

import java.util.ArrayList;
import java.util.List;


public class HistFrag extends BasFrag<VidPre> implements VidHistAdapter.Callback, VidHistVie {
    FragmentHistoryBinding binding;
    private Context mContext;

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

    public static HistFrag newInstance() {
        return new HistFrag();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = FragmentHistoryBinding.inflate(getLayoutInflater());
        ArrayList arrayList = new ArrayList();
        arrayList.add(VidHistTabFrag.newInstance());
        arrayList.add(MusHistTabFrag.newInstance());
        this.binding.viewPager.setAdapter(new HistoryTabPagerAdapter(requireActivity(), arrayList));
        this.binding.viewPager.setOffscreenPageLimit(2);
        new TabLayoutMediator(this.binding.tabLayoutVideo, this.binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.videoplayer.videox.fragment.HistFrag$$ExternalSyntheticLambda0
            @Override
            public final void onConfigureTab(TabLayout.Tab tab, int i) {
                HistFrag.lambda$onCreateView$0(tab, i);
            }
        }).attach();
        return this.binding.getRoot();
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
