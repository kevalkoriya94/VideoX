package com.videoplayer.videox.adapter.mus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.BotMenAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.InpDialBui;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.music.MusicPlaylist;
import com.videoplayer.videox.db.utils.MusicFavoriteUtil;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MusPlalisAdapter extends RecyclerView.Adapter<MusPlalisAdapter.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private List<MusicPlaylist> mPlaylists;
    private int mViewMode = 1;

    public interface Callback {
        void createPlaylist(String str);

        void onPlaylistClick(int i, MusicPlaylist musicPlaylist);

        void onPlaylistOptionSelect(MusicPlaylist musicPlaylist, int i, int i2);
    }

    public MusPlalisAdapter(Context context, List<MusicPlaylist> list, Callback callback) {
        this.mContext = context;
        this.mPlaylists = list;
        this.mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater from;
        int i2;
        if (i == 1) {
            from = LayoutInflater.from(viewGroup.getContext());
            i2 = R.layout.item_folder_list;
        } else {
            from = LayoutInflater.from(viewGroup.getContext());
            i2 = R.layout.item_folder_grid;
        }
        return new ViewHolder(from.inflate(i2, viewGroup, false));
    }

    @Override
    public int getItemCount() {
        List<MusicPlaylist> list = this.mPlaylists;
        if (list == null) {
            return 2;
        }
        return list.size() + 2;
    }

    @Override
    public int getItemViewType(int i) {
        return this.mViewMode;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        if (i == 0) {
            viewHolder.tvFolderName.setTextColor(ContextCompat.getColor(this.mContext, R.color.black));
            viewHolder.tvNumber.setVisibility(View.GONE);
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.tvFolderName.setText(R.string.create_playlist);
            viewHolder.ivFolder.setImageResource(R.drawable.ic_create_playlist);
            viewHolder.ivFolder.setBackgroundColor(this.mContext.getResources().getColor(R.color.main_orange));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusPlalisAdapter.1
                @Override 
                public void onClick(View view) {
                    AdmobAdsHelper.ShowFullAds(MusPlalisAdapter.this.mContext, false);
                    MusPlalisAdapter.this.onBindViewHolderMMMMusicPlaylistAdapter(view);
                }
            });
            return;
        }
        viewHolder.tvFolderName.setTextColor(ContextCompat.getColor(this.mContext, R.color.black));
        viewHolder.tvNumber.setTextColor(ContextCompat.getColor(this.mContext, R.color.black));
        viewHolder.tvNumber.setVisibility(View.VISIBLE);
        if (i == 1) {
            viewHolder.ivFolder.setImageResource(R.drawable.ic_favorite_playlist);
            viewHolder.ivFolder.setBackgroundColor(this.mContext.getResources().getColor(R.color.main_orange));
            viewHolder.tvFolderName.setText(R.string.favorite);
            viewHolder.ivMore.setVisibility(View.GONE);
            int size = MusicFavoriteUtil.getAllFavoriteMusicId(this.mContext).size();
            viewHolder.tvNumber.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_music, size, Integer.valueOf(size)));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusPlalisAdapter.2
                @Override 
                public void onClick(View view) {
                    MusPlalisAdapter.this.mCallback.onPlaylistClick(1, null);
                }
            });
            return;
        }
        viewHolder.ivFolder.setImageResource(R.drawable.ic_play_playlist);
        viewHolder.ivFolder.setBackgroundColor(this.mContext.getResources().getColor(R.color.main_orange));
        viewHolder.ivMore.setVisibility(View.VISIBLE);
        final MusicPlaylist musicPlaylist = this.mPlaylists.get(i - 2);
        viewHolder.tvFolderName.setText(musicPlaylist.getPlaylistName());
        int size2 = musicPlaylist.getMusicList().size();
        viewHolder.tvNumber.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_music, size2, Integer.valueOf(size2)));
        viewHolder.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusPlalisAdapter.3
            @Override 
            public void onClick(View view) {
                BtmMenDialCont.getInstance().showMoreDialogPlaylist(MusPlalisAdapter.this.mContext, new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.adapter.mus.MusPlalisAdapter.3.1
                    @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
                    public void onClick(int i2) {
                        MusPlalisAdapter.this.mCallback.onPlaylistOptionSelect(musicPlaylist, i2, i);
                    }
                });
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusPlalisAdapter.4
            @Override 
            public void onClick(View view) {
                MusPlalisAdapter.this.mCallback.onPlaylistClick(i, (MusicPlaylist) MusPlalisAdapter.this.mPlaylists.get(i - 2));
            }
        });
    }

    public void onBindViewHolderMMMMusicPlaylistAdapter(View view) {
        new InpDialBui(this.mContext, new InpDialBui.OkButtonClickListener() { // from class: com.videoplayer.videox.adapter.mus.MusPlalisAdapter.5
            @Override 
            public void onClick(String str) {
                MusPlalisAdapter.this.mCallback.createPlaylist(str);
            }
        }, "").setTitle(R.string.create_new_playlist, ContextCompat.getColor(this.mContext, R.color.main_orange)).build().show();
    }

    public void sortPlaylist(int i) {
        if (i == 0) {
            Collections.sort(this.mPlaylists, new Comparator<MusicPlaylist>() { // from class: com.videoplayer.videox.adapter.mus.MusPlalisAdapter.6
                @Override // java.util.Comparator
                public int compare(MusicPlaylist obj, MusicPlaylist obj2) {
                    return obj.getPlaylistName().compareToIgnoreCase(obj2.getPlaylistName());
                }
            });
        } else if (i == 1) {
            Collections.sort(this.mPlaylists, new Comparator<MusicPlaylist>() { // from class: com.videoplayer.videox.adapter.mus.MusPlalisAdapter.7
                @Override // java.util.Comparator
                public int compare(MusicPlaylist obj, MusicPlaylist obj2) {
                    return obj2.getPlaylistName().compareToIgnoreCase(obj.getPlaylistName());
                }
            });
        } else if (i == 2) {
            Collections.sort(this.mPlaylists, new Comparator<MusicPlaylist>() { // from class: com.videoplayer.videox.adapter.mus.MusPlalisAdapter.8
                @Override // java.util.Comparator
                public int compare(MusicPlaylist o1, MusicPlaylist o2) {
                    return Long.compare(o1.getDateAdded(), o2.getDateAdded());
                }
            });
        } else if (i == 3) {
            Collections.sort(this.mPlaylists, new Comparator<MusicPlaylist>() { // from class: com.videoplayer.videox.adapter.mus.MusPlalisAdapter.9
                @Override // java.util.Comparator
                public int compare(MusicPlaylist obj, MusicPlaylist obj2) {
                    return Long.compare(obj2.getDateAdded(), obj.getDateAdded());
                }
            });
        }
        notifyDataSetChanged();
    }

    public void setViewMode(int i) {
        if (this.mViewMode != i) {
            this.mViewMode = i;
            notifyDataSetChanged();
        }
    }

    public void updateTotalFavoriteMusic() {
        notifyItemChanged(1);
    }

    public void updateItemMusicPosition(int i, List<MusicInfo> list) {
        this.mPlaylists.get(i - 2).setMusicList(list);
        notifyItemChanged(i);
    }

    public void updateItemNamePosition(int i, String str) {
        this.mPlaylists.get(i - 2).setPlaylistName(str);
        notifyItemChanged(i);
    }

    public void removeItemPosition(int i) {
        this.mPlaylists.remove(i - 2);
        notifyDataSetChanged();
    }

    public void addItemPosition(MusicPlaylist musicPlaylist) {
        this.mPlaylists.add(musicPlaylist);
        notifyDataSetChanged();
    }

    public void updatePlaylist(List<MusicPlaylist> list) {
        this.mPlaylists = new ArrayList(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFolder;
        ImageView ivMore;
        TextView tvFolderName;
        TextView tvNumber;

        public ViewHolder(View view) {
            super(view);
            this.ivFolder = (ImageView) view.findViewById(R.id.iv_folder);
            this.tvFolderName = (TextView) view.findViewById(R.id.tv_folder_name);
            this.tvNumber = (TextView) view.findViewById(R.id.tv_number);
            this.ivMore = (ImageView) view.findViewById(R.id.iv_more);
        }
    }
}
