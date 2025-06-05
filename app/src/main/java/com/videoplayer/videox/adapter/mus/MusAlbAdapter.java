package com.videoplayer.videox.adapter.mus;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.videoplayer.videox.R;
import com.videoplayer.videox.db.entity.music.MusicAlbum;
import com.videoplayer.videox.db.entity.music.MusicPlaylist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MusAlbAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private final Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
    private List<MusicAlbum> mMusicAlbums = new ArrayList();

    public interface Callback {
        void onAlbumClick(int i, MusicAlbum musicAlbum);

        void onAlbumOptionSelect(MusicAlbum musicAlbum, int i, int i2);
    }

    public MusAlbAdapter(Context context, List<MusicPlaylist> list, Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != -1) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_music_album, viewGroup, false));
        }
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_data, viewGroup, false);
        ((TextView) inflate.findViewById(R.id.tv_history)).setText(R.string.no_albums);
        return new EmptyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder.getItemViewType() != -1) {
            ViewHolder viewHolder2 = (ViewHolder) viewHolder;
            final MusicAlbum musicAlbum = this.mMusicAlbums.get(i);
            viewHolder2.tvAlbumName.setText(musicAlbum.getAlbumName());
            viewHolder2.tvArtist.setText(musicAlbum.getArtistName());
            Glide.with(this.mContext).load(ContentUris.withAppendedId(this.artworkUri, musicAlbum.getAlbumId())).placeholder(R.drawable.bg_music_thumbnail_default).centerCrop().error(R.drawable.bg_music_thumbnail_default).into(viewHolder2.ivAlbumArt);
            viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusAlbAdapter$$ExternalSyntheticLambda3
                @Override 
                public final void onClick(View view) {
                    MusAlbAdapter.this.m554xf515823e(i, musicAlbum, view);
                }
            });
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-mus-MusAlbAdapter */
    void m554xf515823e(int i, MusicAlbum musicAlbum, View view) {
        this.mCallback.onAlbumClick(i, musicAlbum);
    }

    public void sortPlaylist(int i) {
        if (i == 0) {
            Collections.sort(this.mMusicAlbums, new Comparator() { // from class: com.videoplayer.videox.adapter.mus.MusAlbAdapter$$ExternalSyntheticLambda1
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compareToIgnoreCase;
                    compareToIgnoreCase = ((MusicAlbum) obj).getAlbumName().compareToIgnoreCase(((MusicAlbum) obj2).getAlbumName());
                    return compareToIgnoreCase;
                }
            });
        } else if (i == 1) {
            Collections.sort(this.mMusicAlbums, new Comparator() { // from class: com.videoplayer.videox.adapter.mus.MusAlbAdapter$$ExternalSyntheticLambda2
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compareToIgnoreCase;
                    compareToIgnoreCase = ((MusicAlbum) obj2).getAlbumName().compareToIgnoreCase(((MusicAlbum) obj).getAlbumName());
                    return compareToIgnoreCase;
                }
            });
        }
        notifyDataSetChanged();
    }

    public void updateMusicAlbum(List<MusicAlbum> list) {
        this.mMusicAlbums = list;
        Collections.sort(list, new Comparator() { // from class: com.videoplayer.videox.adapter.mus.MusAlbAdapter$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compareToIgnoreCase;
                compareToIgnoreCase = ((MusicAlbum) obj).getAlbumName().compareToIgnoreCase(((MusicAlbum) obj2).getAlbumName());
                return compareToIgnoreCase;
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int i) {
        List<MusicAlbum> list = this.mMusicAlbums;
        return (list == null || list.isEmpty()) ? -1 : 0;
    }

    @Override
    public int getItemCount() {
        List<MusicAlbum> list = this.mMusicAlbums;
        if (list == null || list.isEmpty()) {
            return 1;
        }
        return this.mMusicAlbums.size();
    }

    public boolean isEmptyData() {
        List<MusicAlbum> list = this.mMusicAlbums;
        return list == null || list.isEmpty();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAlbumArt;
        TextView tvAlbumName;
        TextView tvArtist;

        public ViewHolder(View view) {
            super(view);
            this.ivAlbumArt = (ImageView) view.findViewById(R.id.iv_album_art);
            this.tvAlbumName = (TextView) view.findViewById(R.id.tv_album_name);
            this.tvArtist = (TextView) view.findViewById(R.id.tv_artist);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }
}
