package com.videoplayer.videox.adapter.vid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.dialog.VidPlayMorDialBuil;

import java.util.List;


public class VidPlayMorAdapter extends RecyclerView.Adapter<VidPlayMorAdapter.ViewHolder> {
    private final VidPlayMorDialBuil.Callback mCallback;
    private final List<Integer> mIcons;
    private boolean mIsFavorite;
    private boolean mIsHWD;
    private boolean mIsMirror;
    private boolean mIsNightMode;
    private int mRepeatMode = 0;
    private final List<Integer> mSelections;

    public VidPlayMorAdapter(List<Integer> list, List<Integer> list2, VidPlayMorDialBuil.Callback callback) {
        this.mIcons = list;
        this.mSelections = list2;
        this.mCallback = callback;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_player_more, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.tvName.setText(this.mSelections.get(i).intValue());
        if (i == 1) {
            viewHolder.ivIcon.setImageResource(this.mIsFavorite ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);
        } else if (i == 3) {
            int i2 = this.mRepeatMode;
            if (i2 == 0) {
                viewHolder.ivIcon.setImageResource(R.drawable.ic_video_player_no_loop);
                viewHolder.tvName.setText(R.string.video_more_no_repeat);
            } else if (i2 == 1) {
                viewHolder.ivIcon.setImageResource(R.drawable.baseline_repeat_one_24px);
                viewHolder.tvName.setText(R.string.video_more_repeat_one);
            } else {
                viewHolder.ivIcon.setImageResource(R.drawable.baseline_repeat_24px);
                viewHolder.tvName.setText(R.string.video_more_repeat_all);
            }
        } else if (i == 4) {
            viewHolder.ivIcon.setImageResource(this.mIsNightMode ? R.drawable.baseline_mode_night_true : R.drawable.baseline_mode_night_24);
        } else if (i == 5) {
            viewHolder.ivIcon.setImageResource(this.mIsMirror ? R.drawable.baseline_flip_true : R.drawable.baseline_flip_24);
        } else if (i == 8) {
            viewHolder.ivIcon.setImageResource(this.mIsHWD ? R.drawable.ic_video_player_hw : R.drawable.ic_video_player_hw_);
        } else {
            viewHolder.ivIcon.setImageResource(this.mIcons.get(i).intValue());
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidPlayMorAdapter$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                VidPlayMorAdapter.this.m602x48f23685(i, view);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-VidPlayMorAdapter */
    void m602x48f23685(int i, View view) {
        this.mCallback.onMoreClick(i, false);
    }

    public void setHWD(boolean z) {
        this.mIsHWD = z;
        notifyItemChanged(8);
    }

    public void setFavorite(boolean z) {
        this.mIsFavorite = z;
        notifyItemChanged(1);
    }

    public void setRepeatMode(int i) {
        this.mRepeatMode = i;
        notifyItemChanged(3);
    }

    public void setNightMode(boolean z) {
        this.mIsNightMode = z;
        notifyItemChanged(4);
    }

    public void setMirror(boolean z) {
        this.mIsMirror = z;
        notifyItemChanged(5);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mSelections.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivIcon;
        final TextView tvName;

        public ViewHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.name_function);
            this.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }
}
