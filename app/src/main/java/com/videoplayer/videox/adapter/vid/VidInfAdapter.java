package com.videoplayer.videox.adapter.vid;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.ads.NativeAdLayout;
import com.google.android.exoplayer2.C;
import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.VidPlayActivity;
import com.videoplayer.videox.adapter.BotMenAdapter;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.InpDialBui;
import com.videoplayer.videox.dialog.MedInfDialBuil;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.db.database.MyDatabase;
import com.videoplayer.videox.db.datasource.VideoDatabaseControl;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.utils.VideoFavoriteUtil;
import com.videoplayer.videox.ser.MusServ;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.uti.thre.ThrExe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class VidInfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean ascending;
    private final Callback mCallback;
    private final Context mContext;
    private final boolean mIsSelectVideoMode;
    private List<VideoInfo> mVideos;
    private List<VideoInfo> mVideosSelected;
    private int mViewMode = 1;
    private int sortMode;

    public interface Callback {
        void onBottomNaviUpdate();

        void onFavoriteUpdate(int i, boolean z);
    }

    private int convertPositionToIndexList(int i) {
        return i;
    }

    public VidInfAdapter(Context context, List<VideoInfo> list, boolean z, boolean z2, Callback callback, List<VideoInfo> list2) {
        this.mContext = context;
        this.mVideos = list;
        this.mIsSelectVideoMode = z2;
        this.mVideosSelected = list2;
        this.mCallback = callback;
        this.sortMode = Utility.getVideoSortMode(context);
        boolean videoSortAscending = Utility.getVideoSortAscending(context);
        this.ascending = videoSortAscending;
        sortVideoList(this.mVideos, this.sortMode, videoSortAscending);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate;
        if (i == 1) {
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_info_list, viewGroup, false);
        } else {
            if (i != 2) {
                if (i == 3) {
                    return new AdsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_info_ads, viewGroup, false));
                }
                View inflate2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_empty_data, viewGroup, false);
                ((TextView) inflate2.findViewById(R.id.tv_history)).setText(R.string.no_videos);
                return new EmptyViewHolder(inflate2);
            }
            inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_info_grid, viewGroup, false);
        }
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        if (viewHolder.getItemViewType() == 0 || viewHolder.getItemViewType() == 3) {
            return;
        }
        final int convertPositionToIndexList = convertPositionToIndexList(i);
        final ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        final VideoInfo videoInfo = this.mVideos.get(convertPositionToIndexList);
        Glide.with(this.mContext).load(videoInfo.getPath()).centerCrop().placeholder(R.drawable.placeholder_video).error(R.drawable.placeholder_video).into(viewHolder2.ivThumbnail);
        viewHolder2.tvVideoName.setText(videoInfo.getDisplayName());
        viewHolder2.tv_folder_name.setText(new File(new File(videoInfo.getPath()).getParent()).getName());
        viewHolder2.tvCreatedDay.setText(Utility.convertSize(videoInfo.getSize()) + " | " + Utility.convertLongToTime(videoInfo.getDate(), "yyyy-MM-dd"));
        viewHolder2.tvTotalTime.setText(Utility.convertLongToDuration(videoInfo.getDuration()));
        if (getItemViewType(i) == 2) {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) viewHolder2.itemView.getLayoutParams();
            if (convertPositionToIndexList % 2 == 0) {
                layoutParams.setMarginEnd((int) this.mContext.getResources().getDimension(R.dimen._5sdp));
                layoutParams.setMarginStart((int) this.mContext.getResources().getDimension(R.dimen._12sdp));
            } else {
                layoutParams.setMarginEnd((int) this.mContext.getResources().getDimension(R.dimen._12sdp));
                layoutParams.setMarginStart((int) this.mContext.getResources().getDimension(R.dimen._5sdp));
            }
            viewHolder2.itemView.setLayoutParams(layoutParams);
        }
        viewHolder2.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda11
            @Override 
            public final void onClick(View view) {
                VidInfAdapter.this.m587xab96cb40(viewHolder2, videoInfo, convertPositionToIndexList, view);
            }
        });
        if (this.mIsSelectVideoMode) {
            viewHolder2.ivMore.setVisibility(View.GONE);
            viewHolder2.ivChecked.setVisibility(View.VISIBLE);
            viewHolder2.ivChecked.setActivated(this.mVideosSelected.contains(videoInfo));
        } else {
            viewHolder2.ivChecked.setVisibility(View.GONE);
            viewHolder2.ivMore.setVisibility(View.VISIBLE);
            viewHolder2.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda1
                @Override 
                public final void onClick(View view) {
                    VidInfAdapter.this.m588x9d40715f(videoInfo, convertPositionToIndexList, i, view);
                }
            });
        }
    }

    /* renamed from: lambda$onBindViewHolder$0$com-videoplayer-videox-adapter-vid-VidInfAdapter */
    void m587xab96cb40(ViewHolder viewHolder, VideoInfo videoInfo, int i, View view) {
        Log.e("TAG", "onClick: ");
        onBindViewHolder0VideoInfoAdapter(viewHolder, videoInfo, i, view);
    }

    public void onBindViewHolder0VideoInfoAdapter(ViewHolder viewHolder, VideoInfo videoInfo, int i, View view) {
        Log.e("TAG", "onBindViewHolder0VideoInfoAdapter: " + this.mIsSelectVideoMode);
        if (!this.mIsSelectVideoMode) {
            Log.e("TAG", "onBindViewHolder0VideoInfoAdapter: 1 " + i);
            Intent intent = new Intent(this.mContext, (Class<?>) VidPlayActivity.class);
            intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, i);
            intent.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_ARRAY, (ArrayList) getAllVideoId());
            AdmobAdsHelper.ShowFullAdsIntent(this.mContext, intent);
            return;
        }
        viewHolder.ivChecked.setActivated(!viewHolder.ivChecked.isActivated());
        if (viewHolder.ivChecked.isActivated()) {
            this.mVideosSelected.add(videoInfo);
        } else {
            this.mVideosSelected.remove(videoInfo);
        }
    }

    /* renamed from: onBindViewHolder5VideoInfoAdapter, reason: merged with bridge method [inline-methods] */
    public void m588x9d40715f(final VideoInfo videoInfo, final int i, final int i2, View view) {
        final boolean checkFavoriteVideoIdExisted = VideoFavoriteUtil.checkFavoriteVideoIdExisted(this.mContext, videoInfo.getVideoId());
        BtmMenDialCont.getInstance().showMoreDialogVideo(this.mContext, checkFavoriteVideoIdExisted, new BotMenAdapter.Callback() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda10
            @Override // com.videoplayer.videox.adapter.BotMenAdapter.Callback
            public final void onClick(int i3) {
                VidInfAdapter.this.m592xcb7680d7(videoInfo, i, i2, checkFavoriteVideoIdExisted, i3);
            }
        });
    }

    /* renamed from: onBindViewHolder3VideoInfoAdapter, reason: merged with bridge method [inline-methods] */
    public void m591x40576d5(String str, final VideoInfo videoInfo, final String str2, final int i, String str3) {
        final String trim = str3.trim();
        if (TextUtils.isEmpty(str3)) {
            Toast.makeText(this.mContext, R.string.empty_video_name, Toast.LENGTH_SHORT).show();
        } else {
            if (trim.equals(str)) {
                return;
            }
            Utility.renameAVideo(this.mContext, videoInfo, trim + str2, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda0
                @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                public final void onScanCompleted(String str4, Uri uri) {
                    VidInfAdapter.this.m590x84442e38(videoInfo, trim, str2, i, str4, uri);
                }
            });
        }
    }

    /* renamed from: onBindViewHolder2VideoInfoAdapter, reason: merged with bridge method [inline-methods] */
    public void m590x84442e38(VideoInfo videoInfo, String str, String str2, final int i, String str3, Uri uri) {
        long j;
        if (uri != null) {
            videoInfo.setDisplayName(str + str2);
            videoInfo.setPath(str3);
            videoInfo.setUri(uri.toString());
            try {
                j = ContentUris.parseId(uri);
            } catch (Exception unused) {
                j = 0;
            }
            VideoDatabaseControl.getInstance().removeVideoById(videoInfo.getVideoId());
            MyDatabase.getInstance(this.mContext).videoHistoryDAO().deleteVideoHistoryById(videoInfo.getVideoId());
            videoInfo.setVideoId(j);
            VideoDatabaseControl.getInstance().addVideo(videoInfo);
            Log.d("binhnk08", " renameAVideo = " + videoInfo);
            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    VidInfAdapter.this.m589xd97fd7f8(i);
                }
            });
        }
    }

    /* renamed from: lambda$onBindViewHolder2VideoInfoAdapter$4$com-videoplayer-videox-adapter-vid-VidInfAdapter */
    void m589xd97fd7f8(int i) {
        notifyItemChanged(i);
    }

    public class AnonymousClass3 implements QueDiaBuil.OkButtonClickListener {
        final int val$position;
        final VideoInfo val$video;

        @Override 
        public void onCancelClick() {
        }

        AnonymousClass3(int i, VideoInfo videoInfo) {
            this.val$position = i;
            this.val$video = videoInfo;
        }

        @Override 
        public void onOkClick() {
            VidInfAdapter.this.removeItemPosition(this.val$position);
            ThrExe.runOnDatabaseThread(new Runnable() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$AnonymousClass3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AnonymousClass3.this.m593xc8eb577e();
                }
            });
        }

        /* renamed from: lambda$onOkClick$0$com-videoplayer-videox-adapter-vid-VidInfAdapter$AnonymousClass3 */
        void m593xc8eb577e() {
            VideoDatabaseControl.getInstance().removeVideoById(this.val$video.getVideoId());
            VideoFavoriteUtil.addFavoriteVideoId(VidInfAdapter.this.mContext, this.val$video.getVideoId(), false);
            Utility.deleteAVideo(VidInfAdapter.this.mContext, this.val$video);
        }
    }

    /* renamed from: onBindViewHolder4VideoInfoAdapter, reason: merged with bridge method [inline-methods] */
    public void m592xcb7680d7(final VideoInfo videoInfo, int i, final int i2, boolean z, int i3) {
        String str;
        Log.e("TAG", "onBindViewHolder4VideoInfoAdapter: " + i3);
        switch (i3) {
            case 0:
                final MusicInfo musicInfo = new MusicInfo();
                musicInfo.setId(-1L);
                musicInfo.setUri(videoInfo.getUri());
                musicInfo.setPath(videoInfo.getPath());
                musicInfo.setDisplayName(videoInfo.getDisplayName());
                Intent intent = new Intent(this.mContext, (Class<?>) MusServ.class);
                this.mContext.startService(intent);
                this.mContext.bindService(intent, new ServiceConnection() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter.1
                    @Override // android.content.ServiceConnection
                    public void onServiceDisconnected(ComponentName componentName) {
                    }

                    @Override // android.content.ServiceConnection
                    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                        MusServ service = ((MusServ.MyBinder) iBinder).getService();
                        if (service != null) {
                            service.setMusicInfoList(new ArrayList(Collections.singletonList(musicInfo)));
                            service.setIndex(0);
                            service.setRepeatState(0);
                            service.playMusic();
                        }
                        VidInfAdapter.this.mContext.unbindService(this);
                    }
                }, 1);
                Callback callback = this.mCallback;
                if (callback != null) {
                    callback.onBottomNaviUpdate();
                    break;
                }
                break;
            case 1:
                Intent intent2 = new Intent(this.mContext, (Class<?>) VidPlayActivity.class);
                intent2.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_NUMBER, i);
                intent2.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_AUDIO_MODE, true);
                intent2.putExtra(AppCon.IntentExtra.EXTRA_VIDEO_ARRAY, (ArrayList) getAllVideoId());
                intent2.addFlags(C.ENCODING_PCM_32BIT);
                intent2.addFlags(65536);
                AdmobAdsHelper.ShowFullAdsIntent(this.mContext, intent2);
                break;
            case 2:
                Callback callback2 = this.mCallback;
                if (callback2 != null) {
                    callback2.onFavoriteUpdate(i2, !z);
                }
                VideoFavoriteUtil.addFavoriteVideoId(this.mContext, videoInfo.getVideoId(), !z);
                break;
            case 3:
                Utility.convertToMp3(this.mContext, videoInfo.getPath(), this.mCallback);
                break;
            case 4:
                String displayName = videoInfo.getDisplayName();
                if (displayName.indexOf(".") > 0) {
                    str = displayName.substring(displayName.lastIndexOf("."));
                    displayName = displayName.substring(0, displayName.lastIndexOf("."));
                } else {
                    str = "";
                }
                final String str2 = str;
                final String str3 = displayName;
                new InpDialBui(this.mContext, new InpDialBui.OkButtonClickListener() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda3
                    @Override 
                    public final void onClick(String str4) {
                        VidInfAdapter.this.m591x40576d5(str3, videoInfo, str2, i2, str4);
                    }
                }, displayName).setTitle(R.string.rename, ContextCompat.getColor(this.mContext, R.color.main_orange)).build().show();
                break;
            case 5:
                Utility.shareVideo(this.mContext, videoInfo);
                break;
            case 6:
                new MedInfDialBuil(this.mContext, videoInfo).build().show();
                break;
            case 7:
                new QueDiaBuil(this.mContext, new AnonymousClass3(i2, videoInfo)).setTitle(R.string.delete, ContextCompat.getColor(this.mContext, R.color.color_FF6666)).setQuestion(R.string.question_remove_file).build().show();
                break;
        }
    }

    private List<Long> getAllVideoId() {
        ArrayList arrayList = new ArrayList();
        List<VideoInfo> list = this.mVideos;
        if (list == null) {
            return new ArrayList();
        }
        Iterator<VideoInfo> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(Long.valueOf(it.next().getVideoId()));
        }
        return arrayList;
    }

    @Override
    public int getItemViewType(int i) {
        List<VideoInfo> list = this.mVideos;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return this.mViewMode;
    }

    public static int sortVideoListtt(VideoInfo videoInfo, VideoInfo videoInfo2) {
        int compare = Long.compare(videoInfo.getDate(), videoInfo2.getDate());
        if (compare == 0) {
            compare = Long.compare(videoInfo.getDuration(), videoInfo2.getDuration());
        }
        return compare == 0 ? videoInfo.getDisplayName().compareToIgnoreCase(videoInfo2.getDisplayName()) : compare;
    }

    public static int sortVideoList7(VideoInfo videoInfo, VideoInfo videoInfo2) {
        int compare = Long.compare(videoInfo2.getDate(), videoInfo.getDate());
        if (compare == 0) {
            compare = Long.compare(videoInfo2.getDuration(), videoInfo.getDuration());
        }
        return compare == 0 ? videoInfo2.getDisplayName().compareToIgnoreCase(videoInfo.getDisplayName()) : compare;
    }

    private void sortVideoList(List<VideoInfo> list, int i, boolean z) {
        if (i == 0) {
            if (z) {
                Collections.sort(list, new Comparator<VideoInfo>() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter.2
                    @Override // java.util.Comparator
                    public int compare(VideoInfo obj, VideoInfo obj2) {
                        return VidInfAdapter.sortVideoListtt(obj, obj2);
                    }
                });
                return;
            } else {
                Collections.sort(list, new Comparator<VideoInfo>() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter.3
                    @Override // java.util.Comparator
                    public int compare(VideoInfo obj, VideoInfo obj2) {
                        return VidInfAdapter.sortVideoList7(obj, obj2);
                    }
                });
                return;
            }
        }
        if (i == 1) {
            if (z) {
                list.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda8
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compareToIgnoreCase;
                        compareToIgnoreCase = ((VideoInfo) obj).getDisplayName().compareToIgnoreCase(((VideoInfo) obj2).getDisplayName());
                        return compareToIgnoreCase;
                    }
                });
                return;
            } else {
                list.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda9
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compareToIgnoreCase;
                        compareToIgnoreCase = ((VideoInfo) obj2).getDisplayName().compareToIgnoreCase(((VideoInfo) obj).getDisplayName());
                        return compareToIgnoreCase;
                    }
                });
                return;
            }
        }
        if (i == 2) {
            if (z) {
                list.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda6
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compare;
                        compare = Long.compare(((VideoInfo) obj).getSize(), ((VideoInfo) obj2).getSize());
                        return compare;
                    }
                });
                return;
            } else {
                list.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda7
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compare;
                        compare = Long.compare(((VideoInfo) obj2).getSize(), ((VideoInfo) obj).getSize());
                        return compare;
                    }
                });
                return;
            }
        }
        if (i == 3) {
            if (z) {
                list.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda4
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compare;
                        compare = Long.compare(((VideoInfo) obj).getDuration(), ((VideoInfo) obj2).getDuration());
                        return compare;
                    }
                });
            } else {
                list.sort(new Comparator() { // from class: com.videoplayer.videox.adapter.vid.VidInfAdapter$$ExternalSyntheticLambda5
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compare;
                        compare = Long.compare(((VideoInfo) obj2).getDuration(), ((VideoInfo) obj).getDuration());
                        return compare;
                    }
                });
            }
        }
    }

    public void sortVideoList(int i, boolean z) {
        if (this.sortMode == i && this.ascending == z) {
            return;
        }
        this.sortMode = i;
        this.ascending = z;
        sortVideoList(this.mVideos, i, z);
        notifyDataSetChanged();
    }

    public void setViewMode(int i) {
        if (this.mViewMode != i) {
            this.mViewMode = i;
            notifyDataSetChanged();
        }
    }

    public List<VideoInfo> getVideoSelected() {
        return this.mVideosSelected;
    }

    public void setVideoSelected(List<VideoInfo> list) {
        this.mVideosSelected = list;
    }

    public void updateVideoDataList(List<VideoInfo> list) {
        sortVideoList(list, this.sortMode, this.ascending);
        this.mVideos = new ArrayList(list);
        notifyDataSetChanged();
    }

    public void removeItemPosition(int i) {
        this.mVideos.remove(convertPositionToIndexList(i));
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, getItemCount());
    }

    public boolean isEmptyData() {
        return this.mVideos.isEmpty();
    }

    @Override
    public int getItemCount() {
        List<VideoInfo> list = this.mVideos;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return this.mVideos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivChecked;
        private final ImageView ivMore;
        private final ImageView ivThumbnail;
        private final TextView tvCreatedDay;
        private final TextView tvTotalTime;
        private final TextView tvVideoName;
        private final TextView tv_folder_name;

        public ViewHolder(View view) {
            super(view);
            this.tv_folder_name = (TextView) view.findViewById(R.id.tv_folder_name);
            this.ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
            TextView textView = (TextView) view.findViewById(R.id.tv_video_name);
            this.tvVideoName = textView;
            textView.setSelected(true);
            this.tvCreatedDay = (TextView) view.findViewById(R.id.tv_created_day);
            this.ivMore = (ImageView) view.findViewById(R.id.iv_more);
            this.ivChecked = (ImageView) view.findViewById(R.id.iv_checked);
            this.tvTotalTime = (TextView) view.findViewById(R.id.tv_total_time);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
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
