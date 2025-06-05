package com.videoplayer.videox.adapter.vid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VidTimAdapter extends RecyclerView.Adapter<VidTimAdapter.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private int mCurrentSelect = 0;
    private final List<String> mTimes;

    public interface Callback {
        void onTimerSelect(int i);
    }

    public VidTimAdapter(Context context, Callback callback) {
        this.mTimes = new ArrayList(Arrays.asList(context.getString(R.string.turn_off), "10 min", "20 min", "30 min", "40 min", "50 min", "60 min", context.getString(R.string.custom)));
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sound_preset, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        return this.mTimes.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.tvName.setText(this.mTimes.get(i));
        if (this.mCurrentSelect == i) {
            viewHolder.tvName.setTextColor(ContextCompat.getColor(this.mContext, R.color.main_orange));
            viewHolder.itemView.setBackgroundResource(R.drawable.bg_video_timer_selected);
        } else {
            viewHolder.tvName.setTextColor(ContextCompat.getColor(this.mContext, R.color.black));
            viewHolder.itemView.setBackgroundResource(R.drawable.bg_video_timer_unselected);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidTimAdapter$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                VidTimAdapter.this.m604x1c90b6a9(i, view);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-VidTimAdapter */
    void m604x1c90b6a9(int i, View view) {
        this.mCurrentSelect = i;
        notifyDataSetChanged();
        this.mCallback.onTimerSelect(i);
    }

    public int getNumberSelected() {
        return this.mCurrentSelect;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;

        public ViewHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_preset);
        }
    }
}
