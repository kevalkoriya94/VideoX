package com.videoplayer.videox.adapter.mus;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.videoplayer.videox.fragment.mus.tab.MusHistTabFrag;
import com.videoplayer.videox.fragment.mus.tab.MusInfTabFrag;
import com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag;
import com.videoplayer.videox.db.entity.music.MusicInfo;

import java.util.ArrayList;
import java.util.List;


public class MusTabPaAdapter extends FragmentStateAdapter {
    final List<Fragment> mMusicTabFragmentList;

    public MusTabPaAdapter(FragmentActivity fragmentActivity, List<Fragment> list) {
        super(fragmentActivity);
        this.mMusicTabFragmentList = list;
    }

    public void sortData(int i, int i2, boolean z) {
        if (i != 0) {
            if (i == 1 && (this.mMusicTabFragmentList.get(1) instanceof MusPlylistTabFrag)) {
                ((MusPlylistTabFrag) this.mMusicTabFragmentList.get(1)).sortPlaylist(i2);
                return;
            }
            return;
        }
        if (this.mMusicTabFragmentList.get(0) instanceof MusInfTabFrag) {
            ((MusInfTabFrag) this.mMusicTabFragmentList.get(0)).sortMusicList(i2, z);
        }
    }

    public void setViewMode(int i) {
        if (this.mMusicTabFragmentList.get(1) instanceof MusPlylistTabFrag) {
            ((MusPlylistTabFrag) this.mMusicTabFragmentList.get(1)).setViewMode(i);
        }
    }

    public void deleteAllMusicHistory() {
        if (this.mMusicTabFragmentList.get(4) instanceof MusHistTabFrag) {
            ((MusHistTabFrag) this.mMusicTabFragmentList.get(4)).deleteAllHistory();
        }
    }

    public List<MusicInfo> getMusicSelected() {
        if (this.mMusicTabFragmentList.get(0) instanceof MusInfTabFrag) {
            return ((MusInfTabFrag) this.mMusicTabFragmentList.get(0)).getMusicSelected();
        }
        return new ArrayList();
    }

    @Override // androidx.viewpager2.adapter.FragmentStateAdapter
    public Fragment createFragment(int i) {
        return this.mMusicTabFragmentList.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mMusicTabFragmentList.size();
    }
}
