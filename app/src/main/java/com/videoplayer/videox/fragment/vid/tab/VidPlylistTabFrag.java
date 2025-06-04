package com.videoplayer.videox.fragment.vid.tab;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.VidPlalisAdapter;
import com.videoplayer.videox.dialog.InpDialBui;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.fragment.vid.p015df.VidPlylistDiaFrag;
import com.videoplayer.videox.cv.NpaGridLayManager;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoPlaylist;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.pre.vid.VidPlaylistPre;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.vie.vid.VidPlylistVie;

import java.util.ArrayList;
import java.util.List;


public class VidPlylistTabFrag extends BasFrag<VidPlaylistPre> implements VidPlylistVie, VidPlalisAdapter.Callback {
    private final ContentObserver contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.vid.tab.VidPlylistTabFrag.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (VidPlylistTabFrag.this.mPresenter != null) {
                ((VidPlaylistPre) VidPlylistTabFrag.this.mPresenter).openPlaylistTab();
            }
        }
    };
    private ProgressBar loading;
    private VidPlalisAdapter mAdapter;
    private Context mContext;
    private NpaGridLayManager mGridLayoutManager;
    private RecyclerView mRvVideoTabContent;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvTotalPlaylist;

    private void loadNativeAds() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static VidPlylistTabFrag newInstance() {
        return new VidPlylistTabFrag();
    }

    @Override // com.videoplayer.videox.fragment.BasFrag
    public VidPlaylistPre createPresenter() {
        Context context = this.mContext;
        return new VidPlaylistPre(context, this, new VideoDataRepository(context));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video_playlist_tab, viewGroup, false);
        this.tvTotalPlaylist = (TextView) inflate.findViewById(R.id.tv_total);
        this.mRvVideoTabContent = (RecyclerView) inflate.findViewById(R.id.rv_content_tab);
        this.refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        this.loading = (ProgressBar) inflate.findViewById(R.id.loading);
        this.mAdapter = new VidPlalisAdapter(this.mContext, new ArrayList(), this);
        NpaGridLayManager npaGridLayManager = new NpaGridLayManager(this.mContext, 1);
        this.mGridLayoutManager = npaGridLayManager;
        this.mRvVideoTabContent.setLayoutManager(npaGridLayManager);
        this.mRvVideoTabContent.setAdapter(this.mAdapter);
        RecyclerView.ItemAnimator itemAnimator = this.mRvVideoTabContent.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VidPlylistTabFrag$$ExternalSyntheticLambda0
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                VidPlylistTabFrag.this.m667x52451984();
            }
        });
        Log.e("TAG", "onCreateView:getItemCount " + this.mAdapter.getItemCount());
        return inflate;
    }

    /* renamed from: lambda$onCreateView$0$com-videoplayer-videox-fragment-vid-tab-VidPlylistTabFrag */
    void m667x52451984() {
        if (this.mPresenter != null) {
            ((VidPlaylistPre) this.mPresenter).openPlaylistTab();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mPresenter != null) {
            ((VidPlaylistPre) this.mPresenter).openPlaylistTab();
        }
        updateNumberFavorite();
        requireActivity().getContentResolver().registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, this.contentObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getContentResolver().unregisterContentObserver(this.contentObserver);
    }

    public void setViewMode(int i) {
        try {
            if (i == 1) {
                this.mGridLayoutManager.setSpanCount(1);
            } else {
                this.mGridLayoutManager.setSpanCount(3);
            }
            VidPlalisAdapter vidPlalisAdapter = this.mAdapter;
            if (vidPlalisAdapter != null) {
                vidPlalisAdapter.setViewMode(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.videoplayer.videox.vie.vid.VidPlylistVie
    public void updateVideoPlaylist(List<VideoPlaylist> list) {
        this.loading.setVisibility(View.GONE);
        this.refreshLayout.setVisibility(View.VISIBLE);
        this.refreshLayout.setRefreshing(false);
        this.tvTotalPlaylist.setText(getString(R.string.all_playlist, Integer.valueOf(list.size())));
        VidPlalisAdapter vidPlalisAdapter = this.mAdapter;
        if (vidPlalisAdapter != null) {
            vidPlalisAdapter.updatePlaylist(list);
        }
    }

    @Override // com.videoplayer.videox.vie.vid.VidPlylistVie
    public void onCreatePlaylist(boolean z, VideoPlaylist videoPlaylist) {
        if (z) {
            VidPlylistDiaFrag.newInstance(2, videoPlaylist, new VidPlylistDiaFrag.PlaylistCallback() { // from class: com.videoplayer.videox.fragment.vid.tab.VidPlylistTabFrag.2
                @Override // com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.PlaylistCallback
                public void onUpdateVideoPlaylist(List<VideoInfo> list) {
                }

                @Override // com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.PlaylistCallback
                public void onDialogDismiss() {
                    if (VidPlylistTabFrag.this.mPresenter != null) {
                        ((VidPlaylistPre) VidPlylistTabFrag.this.mPresenter).openPlaylistTab();
                    } else {
                        VidPlylistTabFrag.this.updateNumberFavorite();
                    }
                }
            }).show(getChildFragmentManager().beginTransaction(), "dialog_playlist_video");
        } else {
            Toast.makeText(this.mContext, R.string.duplicate_name, Toast.LENGTH_SHORT).show();
        }
    }

    public void sortPlaylist(int i) {
        VidPlalisAdapter vidPlalisAdapter = this.mAdapter;
        if (vidPlalisAdapter != null) {
            vidPlalisAdapter.sortPlaylist(i);
        }
    }

    public void updateNumberFavorite() {
        VidPlalisAdapter vidPlalisAdapter = this.mAdapter;
        if (vidPlalisAdapter != null) {
            vidPlalisAdapter.updateTotalFavoriteVideo();
        }
    }

    /* renamed from: lambda$onPlaylistOptionSelect$1$VideoPlaylistTabFragment, reason: merged with bridge method [inline-methods] */
    public void m668x4aa01347(VideoPlaylist videoPlaylist, int i, String str) {
        String trim = str.trim();
        if (TextUtils.isEmpty(str.trim())) {
            Toast.makeText(this.mContext, R.string.empty_playlist_name, Toast.LENGTH_SHORT).show();
        } else {
            if (trim.equals(videoPlaylist.getPlaylistName())) {
                return;
            }
            ((VidPlaylistPre) this.mPresenter).updatePlaylistName(videoPlaylist, str.trim(), i);
        }
    }

    /* renamed from: lambda$onPlaylistOptionSelect$2$VideoPlaylistTabFragment, reason: merged with bridge method [inline-methods] */
    public void m669x5b55e008(VideoPlaylist videoPlaylist, String str) {
        str.trim();
        if (!TextUtils.isEmpty(str.trim())) {
            ((VidPlaylistPre) this.mPresenter).duplicateVideoPlaylist(str.trim(), videoPlaylist);
        } else {
            Toast.makeText(this.mContext, R.string.empty_playlist_name, Toast.LENGTH_SHORT).show();
        }
    }

    @Override // com.videoplayer.videox.adapter.vid.VidPlalisAdapter.Callback
    public void onPlaylistOptionSelect(final VideoPlaylist videoPlaylist, int i, final int i2) {
        if (i == 0) {
            AdmobAdsHelper.showAdsNumberCount++;
            new InpDialBui(this.mContext, new InpDialBui.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VidPlylistTabFrag$$ExternalSyntheticLambda1
                @Override 
                public final void onClick(String str) {
                    VidPlylistTabFrag.this.m668x4aa01347(videoPlaylist, i2, str);
                }
            }, videoPlaylist.getPlaylistName()).setTitle(R.string.create_new_playlist, ContextCompat.getColor(this.mContext, R.color.main_orange)).build().show();
        } else if (i == 1) {
            AdmobAdsHelper.showAdsNumberCount++;
            new InpDialBui(this.mContext, new InpDialBui.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VidPlylistTabFrag$$ExternalSyntheticLambda2
                @Override 
                public final void onClick(String str) {
                    VidPlylistTabFrag.this.m669x5b55e008(videoPlaylist, str);
                }
            }, "").setTitle(R.string.name_the_playlist, ContextCompat.getColor(this.mContext, R.color.main_orange)).build().show();
        } else if (i == 2) {
            AdmobAdsHelper.showAdsNumberCount++;
            new QueDiaBuil(this.mContext, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.tab.VidPlylistTabFrag.3
                @Override 
                public void onCancelClick() {
                }

                @Override 
                public void onOkClick() {
                    if (VidPlylistTabFrag.this.mAdapter != null) {
                        VidPlylistTabFrag.this.mAdapter.removeItemPosition(i2);
                    }
                    ((VidPlaylistPre) VidPlylistTabFrag.this.mPresenter).deletePlaylist(videoPlaylist);
                }
            }).setTitle(R.string.delete, ContextCompat.getColor(this.mContext, R.color.color_FF6666)).setQuestion(R.string.question_remove_playlist).build().show();
        }
    }

    @Override // com.videoplayer.videox.adapter.vid.VidPlalisAdapter.Callback
    public void createPlaylist(String str) {
        if (!TextUtils.isEmpty(str.trim())) {
            ((VidPlaylistPre) this.mPresenter).createVideoPlaylist(str.trim());
        } else {
            Toast.makeText(this.mContext, R.string.empty_playlist_name, Toast.LENGTH_SHORT).show();
        }
    }

    @Override // com.videoplayer.videox.adapter.vid.VidPlalisAdapter.Callback
    public void onPlaylistClick(final int i, VideoPlaylist videoPlaylist) {
        if (i == 1) {
            VidPlylistDiaFrag.newInstance(4, new VidPlylistDiaFrag.PlaylistCallback() { // from class: com.videoplayer.videox.fragment.vid.tab.VidPlylistTabFrag.4
                @Override // com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.PlaylistCallback
                public void onUpdateVideoPlaylist(List<VideoInfo> list) {
                }

                @Override // com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.PlaylistCallback
                public void onDialogDismiss() {
                    if (VidPlylistTabFrag.this.mPresenter != null) {
                        ((VidPlaylistPre) VidPlylistTabFrag.this.mPresenter).openPlaylistTab();
                    } else {
                        VidPlylistTabFrag.this.updateNumberFavorite();
                    }
                }
            }).show(getChildFragmentManager().beginTransaction(), "dialog_recently_video");
        } else if (i == 2) {
            VidPlylistDiaFrag.newInstance(3, new VidPlylistDiaFrag.PlaylistCallback() { // from class: com.videoplayer.videox.fragment.vid.tab.VidPlylistTabFrag.5
                @Override // com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.PlaylistCallback
                public void onUpdateVideoPlaylist(List<VideoInfo> list) {
                }

                @Override // com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.PlaylistCallback
                public void onDialogDismiss() {
                    if (VidPlylistTabFrag.this.mPresenter != null) {
                        ((VidPlaylistPre) VidPlylistTabFrag.this.mPresenter).openPlaylistTab();
                    } else {
                        VidPlylistTabFrag.this.updateNumberFavorite();
                    }
                }
            }).show(getChildFragmentManager().beginTransaction(), "dialog_favorite_video");
        } else {
            VidPlylistDiaFrag.newInstance(2, videoPlaylist, new VidPlylistDiaFrag.PlaylistCallback() { // from class: com.videoplayer.videox.fragment.vid.tab.VidPlylistTabFrag.6
                @Override // com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.PlaylistCallback
                public void onUpdateVideoPlaylist(List<VideoInfo> list) {
                    if (VidPlylistTabFrag.this.mAdapter != null) {
                        VidPlylistTabFrag.this.mAdapter.updateItemVideoPosition(i, list);
                    }
                }

                @Override // com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.PlaylistCallback
                public void onDialogDismiss() {
                    if (VidPlylistTabFrag.this.mPresenter != null) {
                        ((VidPlaylistPre) VidPlylistTabFrag.this.mPresenter).openPlaylistTab();
                    } else {
                        VidPlylistTabFrag.this.updateNumberFavorite();
                    }
                }
            }).show(getChildFragmentManager().beginTransaction(), "dialog_playlist_video");
        }
    }

    @Override // com.videoplayer.videox.vie.vid.VidPlylistVie
    public void onUpdatePlaylistName(int i, String str, boolean z) {
        if (z) {
            Toast.makeText(this.mContext, R.string.successfully, Toast.LENGTH_SHORT).show();
            VidPlalisAdapter vidPlalisAdapter = this.mAdapter;
            if (vidPlalisAdapter != null) {
                vidPlalisAdapter.updateItemNamePosition(i, str);
                return;
            }
            return;
        }
        Toast.makeText(this.mContext, R.string.duplicate_name, Toast.LENGTH_SHORT).show();
    }

    @Override // com.videoplayer.videox.vie.vid.VidPlylistVie
    public void onDuplicationPlaylist(VideoPlaylist videoPlaylist) {
        if (videoPlaylist == null) {
            Toast.makeText(this.mContext, R.string.duplicate_name, Toast.LENGTH_SHORT).show();
            return;
        }
        VidPlalisAdapter vidPlalisAdapter = this.mAdapter;
        if (vidPlalisAdapter != null) {
            vidPlalisAdapter.addItemPosition(videoPlaylist);
            this.mRvVideoTabContent.smoothScrollToPosition(this.mAdapter.getItemCount() - 1);
        }
    }
}
