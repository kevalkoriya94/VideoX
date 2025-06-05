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
import com.videoplayer.videox.adapter.BotMenAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class UnWatVidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private List<VideoInfo> mHistories;
    private final List<Long> mVideoIds = new ArrayList();

    public interface Callback {
        void onHistoryOptionSelect(VideoHistory videoHistory, int i, int i2);
    }

    public UnWatVidAdapter(Context context, List<VideoInfo> list, Callback callback) {
        this.mContext = context;
        this.mHistories = list;
        this.mCallback = callback;
        Iterator<VideoInfo> it = list.iterator();
        while (it.hasNext()) {
            this.mVideoIds.add(Long.valueOf(it.next().getVideoId()));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == -1) {
            return new EmptyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_data, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_history, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder.getItemViewType() != -1) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            VideoInfo videoInfo = this.mHistories.get(i);
            Glide.with(this.mContext).load(videoInfo.getPath()).placeholder(R.drawable.placeholder_video).centerCrop().error(R.drawable.placeholder_video).into(viewHolder2.ivThumbnail);
            viewHolder2.tvTotalTime.setText(Utility.convertLongToDuration(videoInfo.getDuration()));
            viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.UnWatVidAdapter$$ExternalSyntheticLambda0
                @Override 
                public final void onClick(View view) {
                    UnWatVidAdapter.this.m563x7db8ea50(i, view);
                }
            });
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-UnWatVidAdapter */
    void m563x7db8ea50(int i, View view) {
        Intent intent = new Intent(this.mContext, (Class<?>) VidPlayActivity.class);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, i);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_ARRAY, (ArrayList) this.mVideoIds);
        intent.addFlags(C.ENCODING_PCM_32BIT);
        intent.addFlags(65536);
        AdmobAdsHelper.ShowFullAdsIntent(this.mContext, intent);
    }

    /* renamed from: lambda$onBindViewHolder$1$VideoHistoryAdapter, reason: merged with bridge method [inline-methods] */
    public void m562xf8f29ec(VideoHistory videoHistory, int i, int i2) {
        this.mCallback.onHistoryOptionSelect(videoHistory, i2, i);
    }

    public void lambda$onBindViewHolder$2$VideoHistoryAdapter(final VideoHistory videoHistory, final int i, View view) {
        BtmMenDialCont.getInstance().showMoreDialogHistory(this.mContext, new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.adapter.vid.UnWatVidAdapter$$ExternalSyntheticLambda1
            @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
            public final void onClick(int i2) {
                UnWatVidAdapter.this.m562xf8f29ec(videoHistory, i, i2);
            }
        });
    }

    @Override
    public int getItemViewType(int i) {
        List<VideoInfo> list = this.mHistories;
        return (list == null || list.isEmpty()) ? -1 : 0;
    }

    @Override
    public int getItemCount() {
        List<VideoInfo> list = this.mHistories;
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

    public void removeAllItem() {
        this.mHistories = new ArrayList();
        notifyDataSetChanged();
    }

    public void updateHistory(List<VideoInfo> list) {
        this.mHistories = list;
        notifyDataSetChanged();
        this.mVideoIds.clear();
        Iterator<VideoInfo> it = this.mHistories.iterator();
        while (it.hasNext()) {
            this.mVideoIds.add(Long.valueOf(it.next().getVideoId()));
        }
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
