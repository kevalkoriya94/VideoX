package com.videoplayer.videox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;

import java.util.List;


public class HistSeWebAdapter extends RecyclerView.Adapter<HistSeWebAdapter.ViewHolder> {
    private final Callback mCallback;
    private final List<String> mHistories;

    public interface Callback {
        void onClick(String str);
    }

    public HistSeWebAdapter(List<String> list, Callback callback) {
        this.mHistories = list;
        this.mCallback = callback;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_history_search_web, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final String str = this.mHistories.get(i);
        viewHolder.tvHistory.setText(str);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.HistSeWebAdapter$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                HistSeWebAdapter.this.m553x4f189912(str, view);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-HistSeWebAdapter */
    void m553x4f189912(String str, View view) {
        this.mCallback.onClick(str);
    }

    public void clearData() {
        this.mHistories.clear();
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mHistories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvHistory;

        public ViewHolder(View view) {
            super(view);
            this.tvHistory = (TextView) view.findViewById(R.id.tv_history);
        }
    }
}
