package com.videoplayer.videox.db.datasource;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.videoplayer.videox.db.database.MyDatabase;
import com.videoplayer.videox.db.entity.music.MusicHistory;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.music.MusicPlaylist;
import com.videoplayer.videox.db.utils.MusicFavoriteUtil;
import com.videoplayer.videox.uti.thre.ThrExe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MusicDatabaseDataSource {
    final Context mContext;

    public MusicDatabaseDataSource(Context context) {
        this.mContext = context;
    }

    public List<MusicPlaylist> getAllPlaylistMusics() {
        List<MusicPlaylist> allPlaylist = MyDatabase.getInstance(this.mContext).musicPlaylistDAO().getAllPlaylist();
        for (MusicPlaylist musicPlaylist : allPlaylist) {
            musicPlaylist.setMusicList(removeMusicNotExisted(musicPlaylist.getMusicList()));
        }
        return allPlaylist;
    }

    private List<MusicInfo> removeMusicNotExisted(List<MusicInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (MusicInfo musicInfo : list) {
            if (MusicDatabaseControl.getInstance().getMusicById(musicInfo.getId()) != null) {
                arrayList.add(musicInfo);
            }
        }
        return arrayList;
    }

    public boolean createMusicPlaylist(final MusicPlaylist musicPlaylist) {
        if (checkPlaylistNameExisted(musicPlaylist.getPlaylistName())) {
            return false;
        }
        ThrExe.runOnDatabaseThread(() -> MyDatabase.getInstance(this.mContext).musicPlaylistDAO().insertNewPlaylist(musicPlaylist));
        return true;
    }

    public void updateMusicListForPlaylist(MusicPlaylist musicPlaylist, List<MusicInfo> list) {
        MyDatabase.getInstance(this.mContext).musicPlaylistDAO().updateMusicListForPlaylist(musicPlaylist.getDateAdded(), list);
    }

    public List<MusicInfo> getAllFavoriteMusic() {
        ArrayList arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        for (Long l : MusicFavoriteUtil.getAllFavoriteMusicId(this.mContext)) {
            MusicInfo musicById = MusicDatabaseControl.getInstance().getMusicById(l);
            if (musicById != null) {
                arrayList.add(musicById);
                hashSet.add(l);
            }
        }
        MusicFavoriteUtil.setFavoriteMusicId(this.mContext, hashSet);
        return arrayList;
    }

    public List<MusicHistory> getHistoryMusics() {
        ArrayList arrayList = new ArrayList();
        for (MusicHistory musicHistory : MyDatabase.getInstance(this.mContext).musicHistoryDAO().getAllHistoryMusic()) {
            MusicInfo musicById = MusicDatabaseControl.getInstance().getMusicById(musicHistory.getId());
            if (musicById != null) {
                musicHistory.setMusic(musicById);
                arrayList.add(musicHistory);
            } else {
//                MyDatabase.getInstance(this.mContext).musicHistoryDAO().deleteMusicHistoryById(musicHistory.getId());
            }
        }
        return arrayList;
    }

    public void deleteMusicHistoryById(final long j) {
        ThrExe.runOnDatabaseThread(() -> MusicDatabaseDataSource.this.deleteMusicHistoryById$1$MusicDatabaseDataSource(j));
    }

    public void deleteMusicHistoryById$1$MusicDatabaseDataSource(long j) {
//        MyDatabase.getInstance(this.mContext).musicHistoryDAO().deleteMusicHistoryById(j);
    }

    public void deleteAllMusicHistory() {
        ThrExe.runOnDatabaseThread(() -> MusicDatabaseDataSource.this.deleteAllMusicHistory_MusicDatabaseDataSource());
    }

    public void deleteAllMusicHistory_MusicDatabaseDataSource() {
//        MyDatabase.getInstance(this.mContext).musicHistoryDAO().deleteAllMusicHistory();
    }

    public void updateMusicHistoryData(final MusicInfo musicInfo) {
        ThrExe.runOnDatabaseThread(() -> MusicDatabaseDataSource.this.updateMusicHistoryData_MusicDatabaseDataSource(musicInfo));
    }

    public void updateMusicHistoryData_MusicDatabaseDataSource(MusicInfo musicInfo) {
        MusicHistory musicHistory = new MusicHistory();
        musicHistory.setId(musicInfo.getId());
        musicHistory.setMusic(musicInfo);
        musicHistory.setDateAdded(System.currentTimeMillis());
        Log.e("TAG", musicInfo+" test: "+musicInfo.getId());
        MyDatabase.getInstance(this.mContext).musicHistoryDAO().insertNewHistoryMusic(musicHistory);
    }

    private boolean checkPlaylistNameExisted(String str) {
        String playlistContainingSpecificName = MyDatabase.getInstance(this.mContext).musicPlaylistDAO().getPlaylistContainingSpecificName(str);
        return !TextUtils.isEmpty(playlistContainingSpecificName) && playlistContainingSpecificName.equals(str);
    }

    public boolean updatePlaylistName(final MusicPlaylist musicPlaylist, final String str) {
        if (checkPlaylistNameExisted(str)) {
            return false;
        }
        ThrExe.runOnDatabaseThread(() -> MyDatabase.getInstance(this.mContext).musicPlaylistDAO().updatePlaylistName(musicPlaylist.getDateAdded(), str));
        return true;
    }

    public boolean duplicateMusicPlaylist(final MusicPlaylist musicPlaylist) {
        if (checkPlaylistNameExisted(musicPlaylist.getPlaylistName())) {
            return false;
        }
        ThrExe.runOnDatabaseThread(() -> MyDatabase.getInstance(this.mContext).musicPlaylistDAO().insertNewPlaylist(musicPlaylist));
        return true;
    }

    public void deletePlaylist(final MusicPlaylist musicPlaylist) {
        ThrExe.runOnDatabaseThread(() -> MyDatabase.getInstance(this.mContext).musicPlaylistDAO().deletePlaylist(musicPlaylist.getDateAdded()));
    }

    public List<MusicInfo> searchMusicByMusicName(String str) {
        if (TextUtils.isEmpty(str.trim())) {
            return new ArrayList(MusicDatabaseControl.getInstance().getAllMusics());
        }
        return MusicDatabaseControl.getInstance().searchMusicByMusicName(str);
    }
}
