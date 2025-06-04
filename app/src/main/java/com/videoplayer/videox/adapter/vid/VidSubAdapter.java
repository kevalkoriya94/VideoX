package com.videoplayer.videox.adapter.vid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.db.entity.video.VideoSubtitle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class VidSubAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private final int mCurrentSelect = -1;
    private List<VideoSubtitle> mSubtitles = new ArrayList();

    public interface Callback {
        void onSubtitleSelect(VideoSubtitle videoSubtitle);
    }

    public VidSubAdapter(Context context, Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != -1) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_subtitle, viewGroup, false));
        }
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_data, viewGroup, false);
        ((TextView) inflate.findViewById(R.id.tv_history)).setText(R.string.no_subtitles);
        return new EmptyViewHolder(inflate);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() != -1) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            final VideoSubtitle videoSubtitle = this.mSubtitles.get(i);
            viewHolder2.tvName.setText(videoSubtitle.getName());
            Objects.requireNonNull(this);
            if (-1 == i) {
                viewHolder2.tvName.setTextColor(ContextCompat.getColor(this.mContext, R.color.main_orange));
            } else {
                viewHolder2.tvName.setTextColor(ContextCompat.getColor(this.mContext, R.color.black));
            }
            viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidSubAdapter$$ExternalSyntheticLambda0
                @Override 
                public final void onClick(View view) {
                    VidSubAdapter.this.m603xa4cf8c01(videoSubtitle, view);
                }
            });
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-VidSubAdapter */
    void m603xa4cf8c01(VideoSubtitle videoSubtitle, View view) {
        this.mCallback.onSubtitleSelect(videoSubtitle);
    }

    public int getNumberSelected() {
        Objects.requireNonNull(this);
        return -1;
    }

    public void updateSubtitle(List<VideoSubtitle> list) {
        this.mSubtitles = list;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        List<VideoSubtitle> list = this.mSubtitles;
        return (list == null || list.isEmpty()) ? -1 : 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<VideoSubtitle> list = this.mSubtitles;
        if (list == null || list.isEmpty()) {
            return 1;
        }
        return this.mSubtitles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;

        public ViewHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_subtitle_name);
        }
    }

    static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
}
