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


public class VidHist2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private List<VideoHistory> mHistories;
    private final List<Long> mVideoIds = new ArrayList();

    public interface Callback {
        void onHistoryOptionSelect(VideoHistory videoHistory, int i, int i2);
    }

    public VidHist2Adapter(Context context, List<VideoHistory> list, Callback callback) {
        this.mContext = context;
        this.mHistories = list;
        this.mCallback = callback;
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
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_info_list, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder.getItemViewType() != -1) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            final VideoHistory videoHistory = this.mHistories.get(i);
            VideoInfo video = videoHistory.getVideo();
            Glide.with(this.mContext).load(video.getPath()).placeholder(R.drawable.placeholder_video).centerCrop().error(R.drawable.placeholder_video).into(viewHolder2.ivThumbnail);
            viewHolder2.tvVideoName.setText(video.getDisplayName());
            viewHolder2.tvCreatedDay.setText(Utility.convertLongToTime(videoHistory.getDateAdded(), "yyyy-MM-dd"));
            viewHolder2.tvTotalTime.setText(Utility.convertLongToDuration(video.getDuration()));
            viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidHist2Adapter$$ExternalSyntheticLambda1
                @Override 
                public final void onClick(View view) {
                    VidHist2Adapter.this.m575x2601b671(i, view);
                }
            });
            viewHolder2.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidHist2Adapter$$ExternalSyntheticLambda2
                @Override 
                public final void onClick(View view) {
                    VidHist2Adapter.this.m577x81b2eb2f(videoHistory, i, view);
                }
            });
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-VidHist2Adapter */
    void m575x2601b671(int i, View view) {
        Intent intent = new Intent(this.mContext, (Class<?>) VidPlayActivity.class);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, i);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_ARRAY, (ArrayList) this.mVideoIds);
        intent.addFlags(C.ENCODING_PCM_32BIT);
        intent.addFlags(65536);
        AdmobAdsHelper.ShowFullAdsIntent(this.mContext, intent);
    }

    /* renamed from: lambda$onBindViewHolder$2$com-videoplayer-videox-adapter-vid-VidHist2Adapter */
    void m577x81b2eb2f(final VideoHistory videoHistory, final int i, View view) {
        BtmMenDialCont.getInstance().showMoreDialogHistory(this.mContext, new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.adapter.vid.VidHist2Adapter$$ExternalSyntheticLambda0
            @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
            public final void onClick(int i2) {
                VidHist2Adapter.this.m576x53da50d0(videoHistory, i, i2);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$1$com-videoplayer-videox-adapter-vid-VidHist2Adapter */
    void m576x53da50d0(VideoHistory videoHistory, int i, int i2) {
        this.mCallback.onHistoryOptionSelect(videoHistory, i2, i);
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

    public void removeAllItem() {
        this.mHistories = new ArrayList();
        notifyDataSetChanged();
    }

    public void updateHistory(List<VideoHistory> list) {
        this.mHistories = list;
        notifyDataSetChanged();
        this.mVideoIds.clear();
        Iterator<VideoHistory> it = this.mHistories.iterator();
        while (it.hasNext()) {
            this.mVideoIds.add(Long.valueOf(it.next().getId()));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMore;
        private final ImageView ivThumbnail;
        private final TextView tvCreatedDay;
        private final TextView tvTotalTime;
        private final TextView tvVideoName;

        public ViewHolder(View view) {
            super(view);
            this.ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
            this.tvVideoName = (TextView) view.findViewById(R.id.tv_video_name);
            this.tvCreatedDay = (TextView) view.findViewById(R.id.tv_created_day);
            this.tvTotalTime = (TextView) view.findViewById(R.id.tv_total_time);
            this.ivMore = (ImageView) view.findViewById(R.id.iv_more);
            view.findViewById(R.id.tv_resolution_size).setVisibility(View.GONE);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
}
