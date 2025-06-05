package com.videoplayer.videox.adapter.vid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.VidPlayActivity;
import com.videoplayer.videox.adapter.BotMenAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.ArrayList;
import java.util.List;


public class VidHidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private List<VideoInfo> mVideos;

    public VidHidAdapter(Context context, List<VideoInfo> list) {
        this.mContext = context;
        this.mVideos = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != -1) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_hiden, viewGroup, false));
        }
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_data, viewGroup, false);
        ((TextView) inflate.findViewById(R.id.tv_history)).setText(R.string.no_hidden_videos);
        return new EmptyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder.getItemViewType() != -1) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            final VideoInfo videoInfo = this.mVideos.get(i);
            Glide.with(this.mContext).load(videoInfo.getPath()).centerCrop().placeholder(R.drawable.bg_video_thumbnail_hidden).error(R.drawable.bg_video_thumbnail_hidden).into(viewHolder2.ivThumbnail);
            viewHolder2.tvVideoName.setText(videoInfo.getDisplayName());
            viewHolder2.tvTotalTime.setText(Utility.convertLongToDuration(videoInfo.getDuration()));
            viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidHidAdapter$$ExternalSyntheticLambda0
                @Override 
                public final void onClick(View view) {
                    VidHidAdapter.this.m572x234d55e(i, view);
                }
            });
            viewHolder2.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidHidAdapter$$ExternalSyntheticLambda1
                @Override 
                public final void onClick(View view) {
                    VidHidAdapter.this.m574xe588219c(videoInfo, i, view);
                }
            });
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-VidHidAdapter */
    void m572x234d55e(int i, View view) {
        Intent intent = new Intent(this.mContext, (Class<?>) VidPlayActivity.class);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, i);
        intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_HIDDEN, (ArrayList) this.mVideos);
        intent.addFlags(C.ENCODING_PCM_32BIT);
        intent.addFlags(65536);
        AdmobAdsHelper.ShowFullAdsIntent(this.mContext, intent);
    }

    /* renamed from: lambda$onBindViewHolder$2$com-videoplayer-videox-adapter-vid-VidHidAdapter */
    void m574xe588219c(final VideoInfo videoInfo, final int i, View view) {
        BtmMenDialCont.getInstance().showMoreDialogHiddenVideo(this.mContext, new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.adapter.vid.VidHidAdapter$$ExternalSyntheticLambda2
            @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
            public final void onClick(int i2) {
                VidHidAdapter.this.m573xf3de7b7d(videoInfo, i, i2);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$1$com-videoplayer-videox-adapter-vid-VidHidAdapter */
    void m573xf3de7b7d(VideoInfo videoInfo, int i, int i2) {
        if (i2 == 0) {
            boolean unhiddenAVideo = Utility.setUnhiddenAVideo(this.mContext, videoInfo);
            removeItemPosition(i);
            if (unhiddenAVideo) {
                return;
            }
            Toast.makeText(this.mContext, R.string.video_has_been_modified, Toast.LENGTH_SHORT).show();
        }
    }

    public void removeItemPosition(int i) {
        this.mVideos.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, getItemCount());
    }

    public void updateData(List<VideoInfo> list) {
        this.mVideos = new ArrayList(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int i) {
        List<VideoInfo> list = this.mVideos;
        return (list == null || list.isEmpty()) ? -1 : 0;
    }

    @Override
    public int getItemCount() {
        List<VideoInfo> list = this.mVideos;
        if (list == null || list.isEmpty()) {
            return 1;
        }
        return this.mVideos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivMore;
        private final ImageView ivThumbnail;
        private final TextView tvTotalTime;
        private final TextView tvVideoName;

        public ViewHolder(View view) {
            super(view);
            this.ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
            this.tvVideoName = (TextView) view.findViewById(R.id.tv_video_name);
            this.tvTotalTime = (TextView) view.findViewById(R.id.tv_total_time);
            this.ivMore = (ImageView) view.findViewById(R.id.iv_more);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
}
