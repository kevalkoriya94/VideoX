package com.videoplayer.videox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;

import java.util.List;


public class BotMenAdapter extends RecyclerView.Adapter<BotMenAdapter.ViewHolder> {
    private final Callback mCallback;
    private final List<Integer> mIcons;
    private final List<Integer> mSelections;

    public interface Callback {
        void onClick(int i);
    }

    public BotMenAdapter(List<Integer> list, List<Integer> list2, Callback callback) {
        this.mSelections = list;
        this.mCallback = callback;
        this.mIcons = list2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_option_bottom, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.tvSelectionName.setText(this.mSelections.get(i).intValue());
        viewHolder.ivIcon.setImageResource(this.mIcons.get(i).intValue());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.BotMenAdapter$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                BotMenAdapter.this.m552x366c591d(i, view);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-BotMenAdapter */
    void m552x366c591d(int i, View view) {
        this.mCallback.onClick(i);
    }

    @Override
    public int getItemCount() {
        return this.mSelections.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivIcon;
        final TextView tvSelectionName;

        public ViewHolder(View view) {
            super(view);
            this.tvSelectionName = (TextView) view.findViewById(R.id.tv_selection_name);
            this.ivIcon = (ImageView) view.findViewById(R.id.iv_icon_option);
        }
    }
}
