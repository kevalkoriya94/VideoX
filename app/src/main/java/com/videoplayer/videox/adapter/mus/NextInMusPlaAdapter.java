package com.videoplayer.videox.adapter.mus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.videoplayer.videox.R;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.mus.MusPlUti;

import java.util.List;


public class NextInMusPlaAdapter extends RecyclerView.Adapter<NextInMusPlaAdapter.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private int mCurrentPosition;
    private final List<MusicInfo> mMusics;

    public interface Callback {
        void onMusicPlay(int i);
    }

    public NextInMusPlaAdapter(Context context, List<MusicInfo> list, int i, Callback callback) {
        this.mContext = context;
        this.mMusics = list;
        this.mCurrentPosition = i;
        this.mCallback = callback;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music_info2, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if (this.mCurrentPosition == i) {
            int color = ContextCompat.getColor(this.mContext, R.color.main_orange);
            viewHolder.tvSong.setTextColor(color);
            viewHolder.tvArtist.setTextColor(color);
            viewHolder.tvDuration.setTextColor(color);
        } else {
            viewHolder.tvSong.setTextColor(ContextCompat.getColor(this.mContext, R.color.black));
            viewHolder.tvArtist.setTextColor(ContextCompat.getColor(this.mContext, R.color.color_B0B0B0));
            viewHolder.tvDuration.setTextColor(ContextCompat.getColor(this.mContext, R.color.color_B0B0B0));
        }
        MusicInfo musicInfo = this.mMusics.get(i);
        viewHolder.tvSong.setText(musicInfo.getDisplayName());
        viewHolder.tvArtist.setText(musicInfo.getArtist());
        viewHolder.tvDuration.setText(Utility.convertLongToDuration(musicInfo.getDuration()));
        Glide.with(this.mContext).load(MusPlUti.getThumbnailOfSong(this.mContext, musicInfo.getPath(), 60)).placeholder(R.drawable.disc_icn).centerCrop().error(R.drawable.disc_icn).into(viewHolder.ivThumbnail);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.NextInMusPlaAdapter$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                NextInMusPlaAdapter.this.m559xece7558(i, view);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-mus-NextInMusPlaAdapter */
    void m559xece7558(int i, View view) {
        this.mCallback.onMusicPlay(i);
    }

    public void updateCurrentPosition(int i) {
        this.mCurrentPosition = i;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<MusicInfo> list = this.mMusics;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivThumbnail;
        private final TextView tvArtist;
        private final TextView tvDuration;
        private final TextView tvSong;

        public ViewHolder(View view) {
            super(view);
            this.ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
            this.tvSong = (TextView) view.findViewById(R.id.tv_song);
            this.tvArtist = (TextView) view.findViewById(R.id.tv_artist);
            this.tvDuration = (TextView) view.findViewById(R.id.tv_duration);
            ((ImageView) view.findViewById(R.id.iv_more)).setVisibility(View.GONE);
        }
    }
}
