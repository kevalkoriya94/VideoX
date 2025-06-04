package com.videoplayer.videox.adapter.vid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.BotMenAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.db.datasource.VideoDatabaseControl;
import com.videoplayer.videox.db.entity.video.VideoFolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class VidFolAudAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private final boolean mIsSelectMode;
    private List<VideoFolder> mVideoFolders;
    private int mViewMode = 1;

    public interface Callback {
        void onFolderClick(VideoFolder videoFolder, int i);

        void onFolderOptionSelect(VideoFolder videoFolder, int i, int i2);
    }

    public VidFolAudAdapter(Context context, List<VideoFolder> list, boolean z, Callback callback) {
        this.mContext = context;
        this.mVideoFolders = list;
        this.mIsSelectMode = z;
        this.mCallback = callback;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
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

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        int size;
        try {
            ViewHolder viewHolder = (ViewHolder) holder;
            if (i == 0) {
                int size2 = VideoDatabaseControl.getInstance().getAllRecentlyVideo().size();
                viewHolder.ivFolder.setImageResource(R.drawable.ic_recently);
                viewHolder.ivMore.setVisibility(View.GONE);
                viewHolder.tvFolderName.setText(R.string.recently_added);
                viewHolder.tvNumber.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_video, size2, Integer.valueOf(size2)));
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidFolAudAdapter$$ExternalSyntheticLambda4
                    @Override 
                    public final void onClick(View view) {
                        VidFolAudAdapter.this.m564xf8f82350(i, view);
                    }
                });
                return;
            }
            final VideoFolder videoFolder = this.mVideoFolders.get(i - 1);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidFolAudAdapter$$ExternalSyntheticLambda5
                @Override 
                public final void onClick(View view) {
                    VidFolAudAdapter.this.m565x8632d4d1(videoFolder, i, view);
                }
            });
            viewHolder.ivFolder.setImageResource(R.drawable.baseline_folder_24);
            if (this.mIsSelectMode) {
                viewHolder.ivMore.setVisibility(View.GONE);
            } else {
                viewHolder.ivMore.setVisibility(View.VISIBLE);
            }
            if (videoFolder.getVideoList().size() == 0) {
                size = videoFolder.getVideoList().size();
            } else {
                size = videoFolder.getVideoList().size() - 1;
            }
            viewHolder.tvFolderName.setText(videoFolder.getFolderName());
            viewHolder.tvNumber.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_video, size, Integer.valueOf(videoFolder.getVideoList().size())));
            viewHolder.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidFolAudAdapter$$ExternalSyntheticLambda6
                @Override 
                public final void onClick(View view) {
                    VidFolAudAdapter.this.m567xa0a837d3(videoFolder, i, view);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-VidFolAudAdapter */
    void m564xf8f82350(int i, View view) {
        this.mCallback.onFolderClick(null, i);
    }

    /* renamed from: lambda$onBindViewHolder$1$com-videoplayer-videox-adapter-vid-VidFolAudAdapter */
    void m565x8632d4d1(VideoFolder videoFolder, int i, View view) {
        this.mCallback.onFolderClick(videoFolder, i);
    }

    /* renamed from: lambda$onBindViewHolder$2$com-videoplayer-videox-adapter-vid-VidFolAudAdapter */
    void m566x136d8652(VideoFolder videoFolder, int i, int i2) {
        this.mCallback.onFolderOptionSelect(videoFolder, i, i2);
    }

    /* renamed from: lambda$onBindViewHolder$3$com-videoplayer-videox-adapter-vid-VidFolAudAdapter */
    void m567xa0a837d3(final VideoFolder videoFolder, final int i, View view) {
        BtmMenDialCont.getInstance().showMoreDialogVideoFolder(this.mContext, new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.adapter.vid.VidFolAudAdapter$$ExternalSyntheticLambda3
            @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
            public final void onClick(int i2) {
                VidFolAudAdapter.this.m566x136d8652(videoFolder, i, i2);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return this.mViewMode;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mVideoFolders.size() + 1;
    }

    public void sortFolderList(int i) {
        if (i == 0) {
            this.mVideoFolders.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidFolAudAdapter$$ExternalSyntheticLambda0
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compareToIgnoreCase;
                    compareToIgnoreCase = ((VideoFolder) obj).getFolderName().compareToIgnoreCase(((VideoFolder) obj2).getFolderName());
                    return compareToIgnoreCase;
                }
            });
        } else if (i == 1) {
            this.mVideoFolders.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidFolAudAdapter$$ExternalSyntheticLambda1
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compareToIgnoreCase;
                    compareToIgnoreCase = ((VideoFolder) obj2).getFolderName().compareToIgnoreCase(((VideoFolder) obj).getFolderName());
                    return compareToIgnoreCase;
                }
            });
        }
        notifyDataSetChanged();
    }

    public void removeItemPosition(int i) {
        int i2 = i - 1;
        if (i2 < 0 || i2 >= this.mVideoFolders.size()) {
            return;
        }
        this.mVideoFolders.remove(i2);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, getItemCount());
    }

    public void updateRecently() {
        notifyItemChanged(0);
    }

    public void updateVideoFolders(List<VideoFolder> list) {
        ArrayList arrayList = new ArrayList(list);
        this.mVideoFolders = arrayList;
        arrayList.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidFolAudAdapter$$ExternalSyntheticLambda2
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compareToIgnoreCase;
                compareToIgnoreCase = ((VideoFolder) obj).getFolderName().compareToIgnoreCase(((VideoFolder) obj2).getFolderName());
                return compareToIgnoreCase;
            }
        });
        notifyDataSetChanged();
    }

    public void setViewMode(int i) {
        if (this.mViewMode != i) {
            this.mViewMode = i;
            notifyDataSetChanged();
        }
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
