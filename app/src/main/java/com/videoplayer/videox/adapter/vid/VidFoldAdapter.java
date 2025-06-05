package com.videoplayer.videox.adapter.vid;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.NativeAdLayout;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.BotMenAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.db.datasource.VideoDatabaseControl;
import com.videoplayer.videox.db.entity.video.VideoFolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class VidFoldAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Callback mCallback;
    private final Context mContext;
    private final boolean mIsSelectMode;
    private final boolean mIsShowNativeAds;
    private List<VideoFolder> mVideoFolders;
    private int mViewMode = 1;
    View view;

    public interface Callback {
        void onFolderClick(VideoFolder videoFolder, int i);

        void onFolderOptionSelect(VideoFolder videoFolder, int i, int i2);
    }

    public VidFoldAdapter(Context context, List<VideoFolder> list, boolean z, Callback callback) {
        this.mContext = context;
        this.mVideoFolders = list;
        this.mIsSelectMode = z;
        this.mCallback = callback;
        this.mIsShowNativeAds = !z;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            this.view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder_list, viewGroup, false);
        } else if (i == 2) {
            this.view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder_grid, viewGroup, false);
        } else if (i == 3) {
            return new AdsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_info_ads, viewGroup, false));
        }
        return new ViewHolder(this.view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {
        try {
            if (holder.getItemViewType() != 3) {
                ViewHolder viewHolder = (ViewHolder) holder;
                if (i == 0) {
                    int size = VideoDatabaseControl.getInstance().getAllRecentlyVideo().size();
                    viewHolder.ivFolder.setImageResource(R.drawable.ic_recently);
                    viewHolder.ivMore.setVisibility(View.GONE);
                    viewHolder.tvFolderName.setText(R.string.recently_added);
                    viewHolder.tvNumber.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_video, size, Integer.valueOf(size)));
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidFoldAdapter$$ExternalSyntheticLambda3
                        @Override 
                        public final void onClick(View view) {
                            VidFoldAdapter.this.m568x7311273c(i, view);
                        }
                    });
                    return;
                }
                final VideoFolder videoFolder = this.mVideoFolders.get(i - 1);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidFoldAdapter$$ExternalSyntheticLambda4
                    @Override 
                    public final void onClick(View view) {
                        VidFoldAdapter.this.m569xb69c44fd(videoFolder, i, view);
                    }
                });
                viewHolder.ivFolder.setImageResource(R.drawable.baseline_folder_24);
                if (this.mIsSelectMode) {
                    viewHolder.ivMore.setVisibility(View.GONE);
                } else {
                    viewHolder.ivMore.setVisibility(View.VISIBLE);
                }
                viewHolder.tvFolderName.setText(videoFolder.getFolderName());
                int size2 = videoFolder.getVideoList().size();
                Log.e("TAG", "onBindViewHolder: " + size2);
                viewHolder.tvNumber.setText(this.mContext.getResources().getQuantityString(R.plurals.value_of_video, size2, Integer.valueOf(size2)));
                viewHolder.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidFoldAdapter$$ExternalSyntheticLambda5
                    @Override 
                    public final void onClick(View view) {
                        VidFoldAdapter.this.m571x3db2807f(videoFolder, i, view);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-VidFoldAdapter */
    void m568x7311273c(int i, View view) {
        this.mCallback.onFolderClick(null, i);
    }

    /* renamed from: lambda$onBindViewHolder$1$com-videoplayer-videox-adapter-vid-VidFoldAdapter */
    void m569xb69c44fd(VideoFolder videoFolder, int i, View view) {
        this.mCallback.onFolderClick(videoFolder, i);
    }

    /* renamed from: lambda$onBindViewHolder$2$com-videoplayer-videox-adapter-vid-VidFoldAdapter */
    void m570xfa2762be(VideoFolder videoFolder, int i, int i2) {
        this.mCallback.onFolderOptionSelect(videoFolder, i, i2);
    }

    /* renamed from: lambda$onBindViewHolder$3$com-videoplayer-videox-adapter-vid-VidFoldAdapter */
    void m571x3db2807f(final VideoFolder videoFolder, final int i, View view) {
        BtmMenDialCont.getInstance().showMoreDialogVideoFolder(this.mContext, new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.adapter.vid.VidFoldAdapter$$ExternalSyntheticLambda6
            @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
            public final void onClick(int i2) {
                VidFoldAdapter.this.m570xfa2762be(videoFolder, i, i2);
            }
        });
    }

    @Override
    public int getItemViewType(int i) {
        return this.mViewMode;
    }

    @Override
    public int getItemCount() {
        return this.mVideoFolders.size() + 1;
    }

    public void sortFolderList(int i) {
        if (i == 0) {
            this.mVideoFolders.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidFoldAdapter$$ExternalSyntheticLambda1
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compareToIgnoreCase;
                    compareToIgnoreCase = ((VideoFolder) obj).getFolderName().compareToIgnoreCase(((VideoFolder) obj2).getFolderName());
                    return compareToIgnoreCase;
                }
            });
        } else if (i == 1) {
            this.mVideoFolders.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidFoldAdapter$$ExternalSyntheticLambda2
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
        arrayList.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidFoldAdapter$$ExternalSyntheticLambda0
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

    public static class AdsViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout frameAds;
        final NativeAdLayout nativeAdLayout;

        public AdsViewHolder(View view) {
            super(view);
            this.frameAds = (FrameLayout) view.findViewById(R.id.frame_ads);
            this.nativeAdLayout = (NativeAdLayout) view.findViewById(R.id.native_banner_ad_container);
        }
    }
}
