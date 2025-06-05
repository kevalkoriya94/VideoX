package com.videoplayer.videox.adapter.vid;

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
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.uti.ads.Utility;

import java.util.List;


public class NexVidPlaAdapter extends RecyclerView.Adapter<NexVidPlaAdapter.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private int mCurrentVideo;
    private final List<VideoInfo> mVideos;

    public interface Callback {
        void onVideoPlay(int i);
    }

    public NexVidPlaAdapter(Context context, int i, List<VideoInfo> list, Callback callback) {
        this.mContext = context;
        this.mCurrentVideo = i;
        this.mVideos = list;
        this.mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_info_mini, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        VideoInfo videoInfo = this.mVideos.get(i);
        Glide.with(this.mContext).load(videoInfo.getPath()).centerCrop().error(R.drawable.placeholder_video).into(viewHolder.ivThumbnail);
        if (this.mCurrentVideo == i) {
            viewHolder.tvVideoName.setTextColor(ContextCompat.getColor(this.mContext, R.color.color_FF6666));
        } else {
            viewHolder.tvVideoName.setTextColor(ContextCompat.getColor(this.mContext, R.color.white));
        }
        viewHolder.tvTotalTime.setText(Utility.convertLongToDuration(videoInfo.getDuration()));
        viewHolder.tvVideoName.setText(videoInfo.getDisplayName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public final void onClick(View view) {
                NexVidPlaAdapter.this.m561x1f7270b9(i, view);
            }
        });
    }

    void m561x1f7270b9(int i, View view) {
        this.mCallback.onVideoPlay(i);
    }

    @Override
    public int getItemCount() {
        List<VideoInfo> list = this.mVideos;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void updateCurrentPosition(int i) {
        this.mCurrentVideo = i;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivThumbnail;
        private final TextView tvTotalTime;
        private final TextView tvVideoName;

        public ViewHolder(View view) {
            super(view);
            this.ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
            this.tvVideoName = (TextView) view.findViewById(R.id.tv_video_name);
            this.tvTotalTime = (TextView) view.findViewById(R.id.tv_total_time);
        }
    }
}
