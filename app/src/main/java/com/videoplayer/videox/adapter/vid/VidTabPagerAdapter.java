package com.videoplayer.videox.adapter.vid;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.videoplayer.videox.fragment.vid.tab.VidFoldTabFrag;
import com.videoplayer.videox.fragment.vid.tab.VidInfTabFrag;
import com.videoplayer.videox.fragment.vid.tab.VidPlylistTabFrag;
import com.videoplayer.videox.db.entity.video.VideoInfo;

import java.util.ArrayList;
import java.util.List;


public class VidTabPagerAdapter extends FragmentStateAdapter {
    final List<Fragment> mVideoTabFragmentList;

    public VidTabPagerAdapter(FragmentActivity fragmentActivity, List<Fragment> list) {
        super(fragmentActivity);
        this.mVideoTabFragmentList = list;
    }

    public void sortData(int i, int i2, boolean z) {
        if (i == 0) {
            if (this.mVideoTabFragmentList.get(0) instanceof VidInfTabFrag) {
                ((VidInfTabFrag) this.mVideoTabFragmentList.get(0)).sortVideoList(i2, z);
            }
        } else {
            if (i != 1) {
                if (i == 2 && (this.mVideoTabFragmentList.get(2) instanceof VidPlylistTabFrag)) {
                    ((VidPlylistTabFrag) this.mVideoTabFragmentList.get(2)).sortPlaylist(i2);
                    return;
                }
                return;
            }
            if (this.mVideoTabFragmentList.get(1) instanceof VidFoldTabFrag) {
                ((VidFoldTabFrag) this.mVideoTabFragmentList.get(1)).sortFolderList(i2);
            }
        }
    }

    public void setViewMode(int i, int i2) {
        try {
            Log.e("TAG", "setViewMode: " + i2);
            if (i != 0) {
                if (i != 1) {
                    if (i == 2 && (this.mVideoTabFragmentList.get(2) instanceof VidPlylistTabFrag)) {
                        ((VidPlylistTabFrag) this.mVideoTabFragmentList.get(2)).setViewMode(i2);
                    }
                } else if (this.mVideoTabFragmentList.get(1) instanceof VidFoldTabFrag) {
                    ((VidFoldTabFrag) this.mVideoTabFragmentList.get(1)).setViewMode(i2);
                }
            } else if (this.mVideoTabFragmentList.get(0) instanceof VidInfTabFrag) {
                ((VidInfTabFrag) this.mVideoTabFragmentList.get(0)).setViewMode(i2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<VideoInfo> getVideoSelected() {
        if (this.mVideoTabFragmentList.get(0) instanceof VidInfTabFrag) {
            return ((VidInfTabFrag) this.mVideoTabFragmentList.get(0)).getVideoSelected();
        }
        return new ArrayList();
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
