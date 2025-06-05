package com.videoplayer.videox.adapter.mus;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.MusPlayActivity;
import com.videoplayer.videox.adapter.BotMenAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.db.entity.music.MusicHistory;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MusHisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private List<MusicHistory> mMusicHistories;
    private final List<Long> mMusicIds = new ArrayList();

    public interface Callback {
        void onMusicOptionSelect(MusicHistory musicHistory, int i, int i2);
    }

    public MusHisAdapter(Context context, List<MusicHistory> list, Callback callback) {
        this.mContext = context;
        this.mMusicHistories = list;
        this.mCallback = callback;
        Iterator<MusicHistory> it = list.iterator();
        while (it.hasNext()) {
            this.mMusicIds.add(Long.valueOf(it.next().getId()));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != -1) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music_info, viewGroup, false));
        }
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_data, viewGroup, false);
        ((TextView) inflate.findViewById(R.id.tv_history)).setText(R.string.no_song_listened);
        return new EmptyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder.getItemViewType() != -1) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            final MusicHistory musicHistory = this.mMusicHistories.get(i);
            MusicInfo musics = musicHistory.getMusics();
            viewHolder2.tvSong.setText(musics.getDisplayName());
            viewHolder2.tvArtist.setText(musics.getArtist());
            viewHolder2.tvDuration.setText(Utility.convertLongToDuration(musics.getDuration()));
            viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusHisAdapter$$ExternalSyntheticLambda0
                @Override 
                public final void onClick(View view) {
                    MusHisAdapter.this.m555xcc3e47e3(i, view);
                }
            });
            viewHolder2.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusHisAdapter$$ExternalSyntheticLambda1
                @Override 
                public final void onClick(View view) {
                    MusHisAdapter.this.m557xaf919421(musicHistory, i, view);
                }
            });
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-mus-MusHisAdapter */
    void m555xcc3e47e3(int i, View view) {
        Log.e("TAG", "lambda$onBindViewHolder$0$MusicHistoryAdapter: ");
        Intent intent = new Intent(view.getContext(), (Class<?>) MusPlayActivity.class);
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_NUMBER, i);
        intent.putExtra(AppCon.IntentExtra.EXTRA_MUSIC_ARRAY, (ArrayList) this.mMusicIds);
        intent.addFlags(C.ENCODING_PCM_32BIT);
        intent.addFlags(65536);
        this.mContext.startActivity(intent);
        AdmobAdsHelper.ShowFullAds(this.mContext, false);
    }

    /* renamed from: lambda$onBindViewHolder$1$com-videoplayer-videox-adapter-mus-MusHisAdapter */
    void m556xbde7ee02(MusicHistory musicHistory, int i, int i2) {
        this.mCallback.onMusicOptionSelect(musicHistory, i2, i);
    }

    /* renamed from: lambda$onBindViewHolder$2$com-videoplayer-videox-adapter-mus-MusHisAdapter */
    void m557xaf919421(final MusicHistory musicHistory, final int i, View view) {
        BtmMenDialCont.getInstance().showMoreDialogHistory(this.mContext, new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.adapter.mus.MusHisAdapter$$ExternalSyntheticLambda2
            @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
            public final void onClick(int i2) {
                MusHisAdapter.this.m556xbde7ee02(musicHistory, i, i2);
            }
        });
    }

    @Override
    public int getItemViewType(int i) {
        List<MusicHistory> list = this.mMusicHistories;
        return (list == null || list.isEmpty()) ? -1 : 0;
    }

    @Override
    public int getItemCount() {
        List<MusicHistory> list = this.mMusicHistories;
        if (list == null || list.isEmpty()) {
            return 1;
        }
        return this.mMusicHistories.size();
    }

    public void removeItemPosition(int i) {
        this.mMusicHistories.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, getItemCount());
    }

    public void removeAllItem() {
        this.mMusicHistories = new ArrayList();
        notifyDataSetChanged();
    }

    public void updateMusicHistory(List<MusicHistory> list) {
        this.mMusicHistories = list;
        notifyDataSetChanged();
        this.mMusicIds.clear();
        Iterator<MusicHistory> it = this.mMusicHistories.iterator();
        while (it.hasNext()) {
            this.mMusicIds.add(Long.valueOf(it.next().getId()));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
}
