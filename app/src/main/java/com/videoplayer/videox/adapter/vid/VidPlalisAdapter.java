package com.videoplayer.videox.adapter.vid;

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
import com.videoplayer.videox.db.datasource.VideoDatabaseControl;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoPlaylist;
import com.videoplayer.videox.db.utils.VideoFavoriteUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class VidPlalisAdapter extends RecyclerView.Adapter<VidPlalisAdapter.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private List<VideoPlaylist> mPlaylists;
    private int mViewMode = 1;

    public interface Callback {
        void createPlaylist(String str);

        void onPlaylistClick(int i, VideoPlaylist videoPlaylist);

        void onPlaylistOptionSelect(VideoPlaylist videoPlaylist, int i, int i2);
    }

    public VidPlalisAdapter(Context context, List<VideoPlaylist> list, Callback callback) {
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
    public int getItemViewType(int i) {
        return this.mViewMode;
    }

    @Override
    public int getItemCount() {
        List<VideoPlaylist> list = this.mPlaylists;
        if (list == null) {
            return 3;
        }
        return list.size() + 3;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        int size;
        int size2;
        int size3;
        if (i == 0) {
            viewHolder.tvNumber.setVisibility(View.GONE);
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.tvFolderName.setText(R.string.create_playlist);
            viewHolder.tvFolderName.setTextColor(ContextCompat.getColor(this.mContext, R.color.black));
            viewHolder.ivFolder.setImageResource(R.drawable.ic_create_playlist);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda8
                @Override 
                public final void onClick(View view) {
                    VidPlalisAdapter.this.m596x85bae4ad(view);
                }
            });
            return;
        }
        viewHolder.tvFolderName.setTextColor(ContextCompat.getColor(this.mContext, R.color.black));
        viewHolder.tvNumber.setTextColor(ContextCompat.getColor(this.mContext, R.color.black));
        viewHolder.tvNumber.setVisibility(View.VISIBLE);
        if (i == 1) {
            if (VideoDatabaseControl.getInstance().getAllRecentlyVideo().size() == 0) {
                size3 = VideoDatabaseControl.getInstance().getAllRecentlyVideo().size();
            } else {
                size3 = VideoDatabaseControl.getInstance().getAllRecentlyVideo().size() - 1;
            }
            viewHolder.ivFolder.setImageResource(R.drawable.ic_recently);
            viewHolder.tvFolderName.setText(R.string.recently_added);
            viewHolder.tvNumber.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_video, size3, Integer.valueOf(size3)));
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda9
                @Override 
                public final void onClick(View view) {
                    VidPlalisAdapter.this.m597x12f5962e(view);
                }
            });
            return;
        }
        if (i == 2) {
            if (VideoFavoriteUtil.getAllFavoriteVideoId(this.mContext).size() == 0) {
                size2 = VideoFavoriteUtil.getAllFavoriteVideoId(this.mContext).size();
            } else {
                size2 = VideoFavoriteUtil.getAllFavoriteVideoId(this.mContext).size() - 1;
            }
            viewHolder.ivFolder.setImageResource(R.drawable.ic_favorite_playlist);
            viewHolder.tvFolderName.setText(R.string.favorite);
            viewHolder.ivMore.setVisibility(View.GONE);
            viewHolder.tvNumber.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_video, size2, Integer.valueOf(size2)));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda10
                @Override 
                public final void onClick(View view) {
                    VidPlalisAdapter.this.m598xa03047af(view);
                }
            });
            return;
        }
        viewHolder.ivFolder.setImageResource(R.drawable.ic_play_playlist);
        viewHolder.ivMore.setVisibility(View.VISIBLE);
        final VideoPlaylist videoPlaylist = this.mPlaylists.get(i - 3);
        viewHolder.tvFolderName.setText(videoPlaylist.getPlaylistName());
        if (videoPlaylist.getVideoList().size() == 0) {
            size = videoPlaylist.getVideoList().size();
        } else {
            size = videoPlaylist.getVideoList().size() - 1;
        }
        viewHolder.tvNumber.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_video, size, Integer.valueOf(size)));
        viewHolder.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda11
            @Override 
            public final void onClick(View view) {
                VidPlalisAdapter.this.m600xbaa5aab1(videoPlaylist, i, view);
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda1
            @Override 
            public final void onClick(View view) {
                VidPlalisAdapter.this.m601x47e05c32(i, view);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$1$com-videoplayer-videox-adapter-vid-VidPlalisAdapter */
    void m596x85bae4ad(View view) {
        new InpDialBui(this.mContext, new InpDialBui.OkButtonClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda2
            @Override 
            public final void onClick(String str) {
                VidPlalisAdapter.this.m595xf880332c(str);
            }
        }, "").setTitle(R.string.create_new_playlist, ContextCompat.getColor(this.mContext, R.color.main_orange)).build().show();
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-VidPlalisAdapter */
    void m595xf880332c(String str) {
        this.mCallback.createPlaylist(str);
    }

    /* renamed from: lambda$onBindViewHolder$2$com-videoplayer-videox-adapter-vid-VidPlalisAdapter */
    void m597x12f5962e(View view) {
        this.mCallback.onPlaylistClick(1, null);
    }

    /* renamed from: lambda$onBindViewHolder$3$com-videoplayer-videox-adapter-vid-VidPlalisAdapter */
    void m598xa03047af(View view) {
        this.mCallback.onPlaylistClick(2, null);
    }

    /* renamed from: lambda$onBindViewHolder$5$com-videoplayer-videox-adapter-vid-VidPlalisAdapter */
    void m600xbaa5aab1(final VideoPlaylist videoPlaylist, final int i, View view) {
        BtmMenDialCont.getInstance().showMoreDialogPlaylist(this.mContext, new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda6
            @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
            public final void onClick(int i2) {
                VidPlalisAdapter.this.m599x2d6af930(videoPlaylist, i, i2);
            }
        });
    }

    /* renamed from: lambda$onBindViewHolder$4$com-videoplayer-videox-adapter-vid-VidPlalisAdapter */
    void m599x2d6af930(VideoPlaylist videoPlaylist, int i, int i2) {
        this.mCallback.onPlaylistOptionSelect(videoPlaylist, i2, i);
    }

    /* renamed from: lambda$onBindViewHolder$6$com-videoplayer-videox-adapter-vid-VidPlalisAdapter */
    void m601x47e05c32(int i, View view) {
        this.mCallback.onPlaylistClick(i, this.mPlaylists.get(i - 3));
    }

    public void sortPlaylist(int i) {
        if (i == 0) {
            this.mPlaylists.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compareToIgnoreCase;
                    compareToIgnoreCase = ((VideoPlaylist) obj).getPlaylistName().compareToIgnoreCase(((VideoPlaylist) obj2).getPlaylistName());
                    return compareToIgnoreCase;
                }
            });
        } else if (i == 1) {
            this.mPlaylists.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda3
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compareToIgnoreCase;
                    compareToIgnoreCase = ((VideoPlaylist) obj2).getPlaylistName().compareToIgnoreCase(((VideoPlaylist) obj).getPlaylistName());
                    return compareToIgnoreCase;
                }
            });
        } else if (i == 2) {
            this.mPlaylists.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda4
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compare;
                    compare = Long.compare(((VideoPlaylist) obj).getDateAdded(), ((VideoPlaylist) obj2).getDateAdded());
                    return compare;
                }
            });
        } else if (i == 3) {
            this.mPlaylists.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda5
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compare;
                    compare = Long.compare(((VideoPlaylist) obj2).getDateAdded(), ((VideoPlaylist) obj).getDateAdded());
                    return compare;
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

    public void updateTotalFavoriteVideo() {
        notifyItemChanged(2);
    }

    public void updateItemVideoPosition(int i, List<VideoInfo> list) {
        this.mPlaylists.get(i - 3).setVideoList(list);
        notifyItemChanged(i);
    }

    public void updateItemNamePosition(int i, String str) {
        this.mPlaylists.get(i - 3).setPlaylistName(str);
        notifyItemChanged(i);
    }

    public void removeItemPosition(int i) {
        this.mPlaylists.remove(i - 3);
        notifyDataSetChanged();
    }

    public void addItemPosition(VideoPlaylist videoPlaylist) {
        this.mPlaylists.add(videoPlaylist);
        notifyDataSetChanged();
    }

    public void updatePlaylist(List<VideoPlaylist> list) {
        ArrayList arrayList = new ArrayList(list);
        this.mPlaylists = arrayList;
        arrayList.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidPlalisAdapter$$ExternalSyntheticLambda7
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compareToIgnoreCase;
                compareToIgnoreCase = ((VideoPlaylist) obj).getPlaylistName().compareToIgnoreCase(((VideoPlaylist) obj2).getPlaylistName());
                return compareToIgnoreCase;
            }
        });
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivFolder;
        final ImageView ivMore;
        final TextView tvFolderName;
        final TextView tvNumber;

        public ViewHolder(View view) {
            super(view);
            this.ivFolder = (ImageView) view.findViewById(R.id.iv_folder);
            this.tvFolderName = (TextView) view.findViewById(R.id.tv_folder_name);
            this.tvNumber = (TextView) view.findViewById(R.id.tv_number);
            this.ivMore = (ImageView) view.findViewById(R.id.iv_more);
        }
    }
}
