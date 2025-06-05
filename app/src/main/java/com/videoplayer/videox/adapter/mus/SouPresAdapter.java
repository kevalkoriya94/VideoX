package com.videoplayer.videox.adapter.mus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;

import java.util.ArrayList;
import java.util.List;


public class SouPresAdapter extends RecyclerView.Adapter<SouPresAdapter.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private int mCurrentSelect;
    private final List<String> mPresets;

    public interface Callback {
        void onSelect(int i);
    }

    public SouPresAdapter(Context context, List<String> list, Callback callback, int i) {
        this.mCurrentSelect = 0;
        this.mContext = context;
        this.mCallback = callback;
        ArrayList arrayList = new ArrayList();
        this.mPresets = arrayList;
        arrayList.add(context.getString(R.string.custom));
        arrayList.addAll(list);
        this.mCurrentSelect = i;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sound_preset, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.tvName.setText(this.mPresets.get(i));
        if (this.mCurrentSelect == i) {
            viewHolder.tvName.setTextColor(ContextCompat.getColor(this.mContext, R.color.black));
            viewHolder.itemView.setBackgroundResource(R.drawable.bg_music_preset_selected);
        } else {
            viewHolder.tvName.setTextColor(ContextCompat.getColor(this.mContext, R.color.main_orange));
            viewHolder.itemView.setBackgroundResource(R.drawable.bg_music_preset_unselected);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.SouPresAdapter$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                SouPresAdapter.this.m560xb3d548ab(i, view);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-mus-SouPresAdapter */
    void m560xb3d548ab(int i, View view) {
        if (this.mCurrentSelect != i) {
            this.mCallback.onSelect(i);
            this.mCurrentSelect = i;
            notifyDataSetChanged();
        }
    }

    public void setCurrentSelect(int i) {
        this.mCurrentSelect = i;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        List<String> list = this.mPresets;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;

        public ViewHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tv_preset);
        }
    }
}
