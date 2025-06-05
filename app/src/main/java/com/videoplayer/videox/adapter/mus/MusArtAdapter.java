package com.videoplayer.videox.adapter.mus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.db.entity.music.MusicArtist;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MusArtAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private List<MusicArtist> mMusicArtists;

    public interface Callback {
        void onArtistSelected(MusicArtist musicArtist);
    }

    public MusArtAdapter(Context context, List<MusicArtist> list, Callback callback) {
        this.mContext = context;
        this.mMusicArtists = list;
        this.mCallback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != -1) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music_artist, viewGroup, false));
        }
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_data, viewGroup, false);
        ((TextView) inflate.findViewById(R.id.tv_history)).setText(R.string.no_artists);
        return new EmptyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() != -1) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            final MusicArtist musicArtist = this.mMusicArtists.get(i);
            viewHolder2.tvArtist.setText(musicArtist.getArtistName());
            int size = musicArtist.getMusicList().size();
            viewHolder2.tvNumberOfTrack.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_track, size, Integer.valueOf(size)));
            viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusArtAdapter.1
                @Override 
                public void onClick(View view) {
                    MusArtAdapter.this.mCallback.onArtistSelected(musicArtist);
                }
            });
        }
    }

    public void sortMusicList(int i) {
        notifyDataSetChanged();
    }

    public void updateMusicDataList(List<MusicArtist> list) {
        this.mMusicArtists = list;
        Collections.sort(list, new Comparator<MusicArtist>() { // from class: com.videoplayer.videox.adapter.mus.MusArtAdapter.2
            @Override // java.util.Comparator
            public int compare(MusicArtist obj, MusicArtist obj2) {
                return obj.getArtistName().compareToIgnoreCase(obj2.getArtistName());
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int i) {
        List<MusicArtist> list = this.mMusicArtists;
        return (list == null || list.isEmpty()) ? -1 : 0;
    }

    @Override
    public int getItemCount() {
        List<MusicArtist> list = this.mMusicArtists;
        if (list == null || list.isEmpty()) {
            return 1;
        }
        return this.mMusicArtists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvArtist;
        private final TextView tvNumberOfTrack;

        public ViewHolder(View view) {
            super(view);
            this.tvArtist = (TextView) view.findViewById(R.id.tv_artist);
            this.tvNumberOfTrack = (TextView) view.findViewById(R.id.tv_number_song);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
}
