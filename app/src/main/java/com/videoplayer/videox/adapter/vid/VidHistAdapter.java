package com.videoplayer.videox.adapter.vid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.VidPlayActivity;
import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class VidHistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private final List<VideoHistory> mHistories;
    private final List<Long> mVideoIds = new ArrayList();

    public interface Callback {
        void onHistoryOptionSelect(VideoHistory videoHistory, int i, int i2);
    }

    public VidHistAdapter(Context context, List<VideoHistory> list, Callback callback) {
        this.mContext = context;
        this.mHistories = list;
        Iterator<VideoHistory> it = list.iterator();
        while (it.hasNext()) {
            this.mVideoIds.add(Long.valueOf(it.next().getId()));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == -1) {
            return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_data, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_history, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder.getItemViewType() != -1) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            VideoInfo video = this.mHistories.get(i).getVideo();
            Glide.with(this.mContext).load(video.getPath()).placeholder(R.drawable.placeholder_video).centerCrop().error(R.drawable.placeholder_video).into(viewHolder2.ivThumbnail);
            viewHolder2.tvTotalTime.setText(Utility.convertLongToDuration(video.getDuration()));
            viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidHistAdapter$$ExternalSyntheticLambda0
                @Override 
                public final void onClick(View view) {
                    VidHistAdapter.this.m578x6f13bcfb(i, view);
                }
            });
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-VidHistAdapter */
    void m578x6f13bcfb(int i, View view) {
        Intent intent = new Intent(this.mContext, (Class<?>) VidPlayActivity.class);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, i);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_ARRAY, (ArrayList) this.mVideoIds);
        intent.addFlags(C.ENCODING_PCM_32BIT);
        intent.addFlags(65536);
        AdmobAdsHelper.ShowFullAdsIntent(this.mContext, intent);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        List<VideoHistory> list = this.mHistories;
        return (list == null || list.isEmpty()) ? -1 : 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<VideoHistory> list = this.mHistories;
        if (list == null || list.isEmpty()) {
            return 1;
        }
        return this.mHistories.size();
    }

    public void removeItemPosition(int i) {
        this.mHistories.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, getItemCount());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivThumbnail;
        private final TextView tvTotalTime;

        public ViewHolder(View view) {
            super(view);
            this.ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
            this.tvTotalTime = (TextView) view.findViewById(R.id.tv_total_time);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
}
