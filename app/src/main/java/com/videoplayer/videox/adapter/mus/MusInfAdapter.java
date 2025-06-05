package com.videoplayer.videox.adapter.mus;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.MusPlayActivity;
import com.videoplayer.videox.adapter.BotMenAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.InpDialBui;
import com.videoplayer.videox.dialog.MedInfDialBuil;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.db.database.MyDatabase;
import com.videoplayer.videox.db.datasource.MusicDatabaseControl;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.utils.MusicFavoriteUtil;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.uti.thre.ThrExe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class MusInfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean ascending;
    private final Callback mCallback;
    private final Context mContext;
    private final boolean mIsSelectMusicMode;
    private List<MusicInfo> mMusics;
    private List<MusicInfo> mMusicsSelected;
    private int sortMode;

    public interface Callback {
        void onFavoriteUpdate(int i, boolean z);
    }

    public MusInfAdapter(Context context, List<MusicInfo> list, boolean z, Callback callback, List<MusicInfo> list2) {
        Log.e("TAG", "onBindViewHolder: ");
        this.mContext = context;
        this.mMusics = list;
        this.mIsSelectMusicMode = z;
        this.mMusicsSelected = list2;
        this.mCallback = callback;
        this.sortMode = Utility.getMusicSortMode(context);
        boolean musicSortAscending = Utility.getMusicSortAscending(context);
        this.ascending = musicSortAscending;
        sortMusicList(this.mMusics, this.sortMode, musicSortAscending);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != -1) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music_info, viewGroup, false));
        }
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_data, viewGroup, false);
        ((TextView) inflate.findViewById(R.id.tv_history)).setText(R.string.no_songs);
        return new EmptyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        try {
            if (viewHolder.getItemViewType() != -1) {
                final ViewHolder viewHolder2 = (ViewHolder) viewHolder;
                final MusicInfo musicInfo = this.mMusics.get(i);
                viewHolder2.tvSong.setText(musicInfo.getDisplayName());
                viewHolder2.tvArtist.setText(musicInfo.getArtist());
                viewHolder2.tvDuration.setText(Utility.convertLongToDuration(musicInfo.getDuration()));
                viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter$$ExternalSyntheticLambda0
                    @Override 
                    public final void onClick(View view) {
                        MusInfAdapter.this.m558x5f6e9cb4(viewHolder2, musicInfo, i, view);
                    }
                });
                if (this.mIsSelectMusicMode) {
                    viewHolder2.ivMore.setVisibility(View.GONE);
                    viewHolder2.ivChecked.setVisibility(View.VISIBLE);
                    viewHolder2.ivChecked.setActivated(this.mMusicsSelected.contains(musicInfo));
                } else {
                    viewHolder2.ivChecked.setVisibility(View.GONE);
                    viewHolder2.ivMore.setVisibility(View.VISIBLE);
                    viewHolder2.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter.1
                        @Override 
                        public void onClick(View view) {
                            final boolean checkFavoriteMusicIdExisted = MusicFavoriteUtil.checkFavoriteMusicIdExisted(MusInfAdapter.this.mContext, musicInfo.getId());
                            BtmMenDialCont.getInstance().showMoreDialogMusic(MusInfAdapter.this.mContext, checkFavoriteMusicIdExisted, new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter.1.1
                                @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
                                public void onClick(int i2) {
                                    MusInfAdapter.this.onBindViewHolderMMusicInfoAdapter(i, checkFavoriteMusicIdExisted, musicInfo, i2);
                                }
                            });
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-mus-MusInfAdapter */
    void m558x5f6e9cb4(ViewHolder viewHolder, MusicInfo musicInfo, int i, View view) {
        if (!this.mIsSelectMusicMode) {
            Intent intent = new Intent(view.getContext(), (Class<?>) MusPlayActivity.class);
            intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_NUMBER, i);
            intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_ARRAY, (ArrayList) getAllMusicId());
            intent.addFlags(C.ENCODING_PCM_32BIT);
            intent.addFlags(65536);
            this.mContext.startActivity(intent);
            AdmobAdsHelper.ShowFullAds(this.mContext, false);
            return;
        }
        viewHolder.ivChecked.setActivated(!viewHolder.ivChecked.isActivated());
        if (viewHolder.ivChecked.isActivated()) {
            this.mMusicsSelected.add(musicInfo);
        } else {
            this.mMusicsSelected.remove(musicInfo);
        }
    }

    public void onBindViewHolder3MusicInfoAdapter(String str, final MusicInfo musicInfo, final String str2, final int i, String str3) {
        final String trim = str3.trim();
        if (TextUtils.isEmpty(str3)) {
            Toast.makeText(this.mContext, R.string.empty_music_name, Toast.LENGTH_SHORT).show();
        } else {
            if (trim.equals(str)) {
                return;
            }
            Utility.renameAMusic(this.mContext, musicInfo, trim + str2, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter.2
                @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                public void onScanCompleted(String str4, Uri uri) {
                    MusInfAdapter.this.lambda$onBindViewHolder$2$MusicInfoAdapter(musicInfo, trim, str2, i, str4, uri);
                }
            });
        }
    }

    public void lambda$onBindViewHolder$2$MusicInfoAdapter(MusicInfo musicInfo, String str, String str2, final int i, String str3, Uri uri) {
        long j;
        if (uri != null) {
            musicInfo.setDisplayName(str + str2);
            musicInfo.setPath(str3);
            musicInfo.setUri(uri.toString());
            try {
                j = ContentUris.parseId(uri);
            } catch (Exception unused) {
                j = 0;
            }
            MusicDatabaseControl.getInstance().removeMusicById(musicInfo.getId());
            MyDatabase.getInstance(this.mContext).musicHistoryDAO().deleteMusicHistoryById(musicInfo.getId());
            musicInfo.setId(j);
            MusicDatabaseControl.getInstance().addMusic(musicInfo);
            Log.d("binhnk08", " renameAMusic = " + musicInfo);
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter.3
                @Override // java.lang.Runnable
                public void run() {
                    MusInfAdapter.this.lambda$onBindViewHolder$1$MusicInfoAdapter(i);
                }
            });
        }
    }

    public void lambda$onBindViewHolder$1$MusicInfoAdapter(int i) {
        notifyItemChanged(i);
    }

    public class AnonymousClass1 implements QueDiaBuil.OkButtonClickListener {
        final MusicInfo val$music;
        final int val$position;

        @Override 
        public void onCancelClick() {
        }

        AnonymousClass1(int i, MusicInfo musicInfo) {
            this.val$position = i;
            this.val$music = musicInfo;
        }

        @Override 
        public void onOkClick() {
            MusInfAdapter.this.removeItemPosition(this.val$position);
            final MusicInfo musicInfo = this.val$music;
            ThrExe.runOnDatabaseThread(new Runnable() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter.AnonymousClass1.1
                @Override // java.lang.Runnable
                public void run() {
                    AnonymousClass1.this.lambda$onOkClick$0$MusicInfoAdapter$1(musicInfo);
                }
            });
        }

        public void lambda$onOkClick$0$MusicInfoAdapter$1(MusicInfo musicInfo) {
            MusicDatabaseControl.getInstance().removeMusicById(musicInfo.getId());
            MusicFavoriteUtil.addFavoriteMusicId(MusInfAdapter.this.mContext, musicInfo.getId(), false);
            Utility.deleteAMusic(MusInfAdapter.this.mContext, musicInfo);
        }
    }

    public void onBindViewHolderMMusicInfoAdapter(final int i, boolean z, final MusicInfo musicInfo, int i2) {
        String str;
        if (i2 == 0) {
            AdmobAdsHelper.showAdsNumberCount++;
            this.mCallback.onFavoriteUpdate(i, !z);
            MusicFavoriteUtil.addFavoriteMusicId(this.mContext, musicInfo.getId(), !z);
            return;
        }
        if (i2 == 1) {
            AdmobAdsHelper.showAdsNumberCount++;
            String displayName = musicInfo.getDisplayName();
            if (displayName.indexOf(".") > 0) {
                str = displayName.substring(displayName.lastIndexOf("."));
                displayName = displayName.substring(0, displayName.lastIndexOf("."));
            } else {
                str = "";
            }
            final String str2 = str;
            final String str3 = displayName;
            new InpDialBui(this.mContext, new InpDialBui.OkButtonClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter.4
                @Override 
                public void onClick(String str22) {
                    MusInfAdapter.this.onBindViewHolder3MusicInfoAdapter(str3, musicInfo, str2, i, str22);
                }
            }, displayName).setTitle(R.string.rename, ContextCompat.getColor(this.mContext, R.color.main_orange)).build().show();
            return;
        }
        if (i2 == 2) {
            AdmobAdsHelper.showAdsNumberCount++;
            Utility.shareMusic(this.mContext, musicInfo);
        } else if (i2 == 3) {
            AdmobAdsHelper.showAdsNumberCount++;
            new MedInfDialBuil(this.mContext, musicInfo).build().show();
        } else if (i2 == 4) {
            AdmobAdsHelper.showAdsNumberCount++;
            new QueDiaBuil(this.mContext, new AnonymousClass1(i, musicInfo)).setTitle(R.string.delete, this.mContext.getResources().getColor(R.color.color_FF6666)).setQuestion(R.string.question_remove_file).build().show();
        }
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

    public static int sortList(MusicInfo musicInfo, MusicInfo musicInfo2) {
        int compare = Long.compare(musicInfo.getDate(), musicInfo2.getDate());
        if (compare == 0) {
            compare = Long.compare(musicInfo.getDuration(), musicInfo2.getDuration());
        }
        return compare == 0 ? musicInfo.getDisplayName().compareToIgnoreCase(musicInfo2.getDisplayName()) : compare;
    }

    public static int sormU(MusicInfo musicInfo, MusicInfo musicInfo2) {
        int compare = Long.compare(musicInfo2.getDate(), musicInfo.getDate());
        if (compare == 0) {
            compare = Long.compare(musicInfo2.getDuration(), musicInfo.getDuration());
        }
        return compare == 0 ? musicInfo2.getDisplayName().compareToIgnoreCase(musicInfo.getDisplayName()) : compare;
    }

    private void sortMusicList(List<MusicInfo> list, int i, boolean z) {
        if (i == 0) {
            if (z) {
                Collections.sort(list, new Comparator<MusicInfo>() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter.5
                    @Override // java.util.Comparator
                    public int compare(MusicInfo obj, MusicInfo obj2) {
                        return MusInfAdapter.sortList(obj, obj2);
                    }
                });
                return;
            } else {
                Collections.sort(list, new Comparator<MusicInfo>() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter.6
                    @Override // java.util.Comparator
                    public int compare(MusicInfo obj, MusicInfo obj2) {
                        return MusInfAdapter.sormU(obj, obj2);
                    }
                });
                return;
            }
        }
        if (i == 1) {
            if (z) {
                Collections.sort(list, new Comparator() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter$$ExternalSyntheticLambda5
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compareToIgnoreCase;
                        compareToIgnoreCase = ((MusicInfo) obj).getDisplayName().compareToIgnoreCase(((MusicInfo) obj2).getDisplayName());
                        return compareToIgnoreCase;
                    }
                });
                return;
            } else {
                Collections.sort(list, new Comparator() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter$$ExternalSyntheticLambda6
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compareToIgnoreCase;
                        compareToIgnoreCase = ((MusicInfo) obj2).getDisplayName().compareToIgnoreCase(((MusicInfo) obj).getDisplayName());
                        return compareToIgnoreCase;
                    }
                });
                return;
            }
        }
        if (i == 2) {
            if (z) {
                Collections.sort(list, new Comparator() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter$$ExternalSyntheticLambda3
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compare;
                        compare = Long.compare(((MusicInfo) obj).getSize(), ((MusicInfo) obj2).getSize());
                        return compare;
                    }
                });
                return;
            } else {
                Collections.sort(list, new Comparator() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter$$ExternalSyntheticLambda4
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compare;
                        compare = Long.compare(((MusicInfo) obj2).getSize(), ((MusicInfo) obj).getSize());
                        return compare;
                    }
                });
                return;
            }
        }
        if (i == 3) {
            if (z) {
                Collections.sort(list, new Comparator() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter$$ExternalSyntheticLambda1
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compare;
                        compare = Long.compare(((MusicInfo) obj).getDuration(), ((MusicInfo) obj2).getDuration());
                        return compare;
                    }
                });
            } else {
                Collections.sort(list, new Comparator() { // from class: com.videoplayer.videox.adapter.mus.MusInfAdapter$$ExternalSyntheticLambda2
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compare;
                        compare = Long.compare(((MusicInfo) obj2).getDuration(), ((MusicInfo) obj).getDuration());
                        return compare;
                    }
                });
            }
        }
    }

    public void sortMusicList(int i, boolean z) {
        if (this.sortMode == i && this.ascending == z) {
            return;
        }
        this.sortMode = i;
        this.ascending = z;
        sortMusicList(this.mMusics, i, z);
        notifyDataSetChanged();
    }

    public List<MusicInfo> getMusicSelected() {
        return this.mMusicsSelected;
    }

    public List<MusicInfo> getMusics() {
        return this.mMusics;
    }

    public void setMusicSelected(List<MusicInfo> list) {
        this.mMusicsSelected = list;
    }

    public boolean isEmptyData() {
        return this.mMusics.isEmpty();
    }

    public void updateMusicDataList(List<MusicInfo> list) {
        sortMusicList(list, this.sortMode, this.ascending);
        this.mMusics = new ArrayList(list);
        notifyDataSetChanged();
    }

    public void removeItemPosition(int i) {
        this.mMusics.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, getItemCount());
    }

    @Override
    public int getItemCount() {
        List<MusicInfo> list = this.mMusics;
        if (list == null || list.isEmpty()) {
            return 1;
        }
        return this.mMusics.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivChecked;
        private final ImageView ivMore;
        private final TextView tvArtist;
        private final TextView tvDuration;
        private final TextView tvSong;

        public ViewHolder(View view) {
            super(view);
            this.tvSong = (TextView) view.findViewById(R.id.tv_song);
            this.tvArtist = (TextView) view.findViewById(R.id.tv_artist);
            this.tvDuration = (TextView) view.findViewById(R.id.tv_duration);
            this.ivMore = (ImageView) view.findViewById(R.id.iv_more);
            this.ivChecked = (ImageView) view.findViewById(R.id.iv_checked);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }

    public static class MusicInfoDiffCallback extends DiffUtil.Callback {
        private final List<MusicInfo> mNewMusicInfoList;
        private final List<MusicInfo> mOldMusicInfoList;

        public MusicInfoDiffCallback(List<MusicInfo> list, List<MusicInfo> list2) {
            this.mOldMusicInfoList = list;
            this.mNewMusicInfoList = list2;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return this.mOldMusicInfoList.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return this.mNewMusicInfoList.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            return this.mOldMusicInfoList.get(i).getId() == this.mNewMusicInfoList.get(i2).getId();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            MusicInfo musicInfo = this.mOldMusicInfoList.get(i);
            MusicInfo musicInfo2 = this.mNewMusicInfoList.get(i2);
            return musicInfo.getDisplayName().equals(musicInfo2.getDisplayName()) && musicInfo.getArtist().equals(musicInfo2.getArtist()) && musicInfo.getDuration() == musicInfo2.getDuration();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public Object getChangePayload(int i, int i2) {
            return super.getChangePayload(i, i2);
        }
    }
}
