package com.videoplayer.videox.fragment.vid.p015df;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.VidInfAdapter;
import com.videoplayer.videox.databinding.DialogFragmentPlaylistVideoBinding;
import com.videoplayer.videox.dialog.BtmMenDialCont;
import com.videoplayer.videox.dialog.SorDialBuil;
import com.videoplayer.videox.fragment.BasDialFrag;
import com.videoplayer.videox.db.datasource.VideoDatabaseControl;
import com.videoplayer.videox.db.entity.video.VideoFolder;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoPlaylist;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.pre.vid.VidPre;
import com.videoplayer.videox.uti.ads.Utility;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.vie.VidVie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class VidPlylistDiaFrag extends BasDialFrag<VidPre> implements VidVie, VidInfAdapter.Callback, AddVidPlylistDiaFrag.Callback {
    DialogFragmentPlaylistVideoBinding binding;
    private VidInfAdapter mAdapter;
    private Context mContext;
    private int mCurrentViewMode;
    private FolderCallback mFolderCallback;
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsSelected;
    private VideoPlaylist mPlaylist;
    private PlaylistCallback mPlaylistCallback;
    private int mType;
    private VideoFolder mVideoFolder;
    private List<VideoInfo> mVideos;

    public interface FolderCallback {
        void onAddVideoClick(List<VideoInfo> list);
    }

    public interface PlaylistCallback {
        void onDialogDismiss();

        void onUpdateVideoPlaylist(List<VideoInfo> list);
    }

    @Override // com.videoplayer.videox.adapter.vid.VidInfAdapter.Callback
    public void onBottomNaviUpdate() {
    }

    @Override // com.videoplayer.videox.vie.VidVie
    public void onSearchVideo(List<VideoInfo> list) {
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public VidPlylistDiaFrag() {
        this.mCurrentViewMode = 1;
        this.mIsSelected = false;
    }

    public static VidPlylistDiaFrag newInstance(int i, VideoFolder videoFolder, boolean z, VideoPlaylist videoPlaylist, FolderCallback folderCallback) {
        return new VidPlylistDiaFrag(i, videoFolder, z, videoPlaylist, folderCallback);
    }

    public VidPlylistDiaFrag(int i, VideoFolder videoFolder, boolean z, VideoPlaylist videoPlaylist, FolderCallback folderCallback) {
        this.mCurrentViewMode = 1;
        this.mType = i;
        this.mIsSelected = z;
        this.mPlaylist = videoPlaylist;
        this.mFolderCallback = folderCallback;
        this.mVideoFolder = videoFolder;
        this.mVideos = new ArrayList();
    }

    public static VidPlylistDiaFrag newInstance(int i, boolean z, VideoPlaylist videoPlaylist, FolderCallback folderCallback) {
        return new VidPlylistDiaFrag(i, z, videoPlaylist, folderCallback);
    }

    public VidPlylistDiaFrag(int i, boolean z, VideoPlaylist videoPlaylist, FolderCallback folderCallback) {
        this.mCurrentViewMode = 1;
        this.mType = i;
        this.mIsSelected = z;
        this.mPlaylist = videoPlaylist;
        this.mFolderCallback = folderCallback;
        this.mVideos = new ArrayList();
    }

    public static VidPlylistDiaFrag newInstance(int i, VideoPlaylist videoPlaylist, PlaylistCallback playlistCallback) {
        return new VidPlylistDiaFrag(i, videoPlaylist, playlistCallback);
    }

    public VidPlylistDiaFrag(int i, VideoPlaylist videoPlaylist, PlaylistCallback playlistCallback) {
        this.mCurrentViewMode = 1;
        this.mIsSelected = false;
        this.mType = i;
        this.mPlaylist = videoPlaylist;
        this.mVideos = videoPlaylist.getVideoList();
        this.mPlaylistCallback = playlistCallback;
    }

    public static VidPlylistDiaFrag newInstance(int i, PlaylistCallback playlistCallback) {
        return new VidPlylistDiaFrag(i, playlistCallback);
    }

    public VidPlylistDiaFrag(int i, PlaylistCallback playlistCallback) {
        this.mCurrentViewMode = 1;
        this.mIsSelected = false;
        this.mType = i;
        this.mPlaylistCallback = playlistCallback;
        this.mVideos = new ArrayList();
        this.mPlaylist = new VideoPlaylist();
    }

    @Override // com.videoplayer.videox.fragment.BasDialFrag
    public VidPre createPresenter() {
        Context context = this.mContext;
        return new VidPre(context, this, new VideoDataRepository(context));
    }

    @Override // com.videoplayer.videox.fragment.BasDialFrag, androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(0, AppCon.Themes.THEMES_STYLE[new SettingPrefUtils(this.mContext).getThemes()]);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        Window window = onCreateDialog.getWindow();
        Objects.requireNonNull(window);
        window.requestFeature(1);
        return onCreateDialog;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        PlaylistCallback playlistCallback = this.mPlaylistCallback;
        if (playlistCallback != null) {
            playlistCallback.onDialogDismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = DialogFragmentPlaylistVideoBinding.inflate(getLayoutInflater());
        int i = this.mType;
        if (i == 4) {
            this.mVideos = VideoDatabaseControl.getInstance().getAllRecentlyVideo();
        } else if (i == 1) {
            VideoFolder videoFolder = this.mVideoFolder;
            if (videoFolder == null) {
                this.mVideos = new ArrayList();
            } else {
                this.mVideos = videoFolder.getVideoList();
            }
        }
        if (this.mPlaylist == null) {
            this.mPlaylist = new VideoPlaylist();
        }
        if (this.mVideos == null) {
            this.mVideos = new ArrayList();
        }
        Context context = this.mContext;
        List<VideoInfo> list = this.mVideos;
        boolean z = this.mIsSelected;
        VidInfAdapter vidInfAdapter = new VidInfAdapter(context, list, !z, z, this, new ArrayList(this.mPlaylist.getVideoList()));
        this.mAdapter = vidInfAdapter;
        this.binding.rvVideoDialog.setAdapter(vidInfAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.mContext, 1);
        this.mGridLayoutManager = gridLayoutManager;
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.1
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i2) {
                return (VidPlylistDiaFrag.this.mGridLayoutManager.getSpanCount() == 2 && i2 % 11 == 3 && i2 == 3) ? 2 : 1;
            }
        });
        this.binding.rvVideoDialog.setLayoutManager(this.mGridLayoutManager);
        int i2 = this.mType;
        if (i2 == 1) {
            this.binding.ivAddVideo.setVisibility(View.GONE);
            this.binding.rlAddVideo.setVisibility(View.GONE);
            setViewDataEmpty(this.mVideos.isEmpty());
            if (this.mIsSelected) {
                this.binding.tvFolderName.setText(R.string.add_new_video);
                this.binding.ivSearch.setVisibility(View.INVISIBLE);
                this.binding.ivDone.setVisibility(View.VISIBLE);
            } else {
                TextView textView = this.binding.tvFolderName;
                VideoFolder videoFolder2 = this.mVideoFolder;
                textView.setText(videoFolder2 != null ? videoFolder2.getFolderName() : "Folders");
            }
            this.binding.rlNoVideo.setVisibility(View.GONE);
        } else if (i2 == 2) {
            if (this.mVideos.isEmpty()) {
                setViewDataEmpty(true);
                this.binding.ivAddVideo.setVisibility(View.GONE);
            } else {
                setViewDataEmpty(false);
                this.binding.ivAddVideo.setVisibility(View.VISIBLE);
            }
            this.binding.tvFolderName.setText(this.mPlaylist.getPlaylistName());
        } else if (i2 == 3) {
            this.binding.ivAddVideo.setVisibility(View.GONE);
            this.binding.rlAddVideo.setVisibility(View.GONE);
            setViewDataEmpty(true);
            this.binding.tvFolderName.setText(R.string.favorite);
            this.binding.loading.setVisibility(View.VISIBLE);
            if (this.mPresenter != null) {
                ((VidPre) this.mPresenter).getAllFavoriteVideo();
            }
            this.binding.rlNoVideo.setVisibility(View.GONE);
        } else if (i2 == 4) {
            this.binding.ivAddVideo.setVisibility(View.GONE);
            this.binding.rlAddVideo.setVisibility(View.GONE);
            setViewDataEmpty(this.mVideos.isEmpty());
            if (this.mIsSelected) {
                this.binding.tvFolderName.setText(R.string.add_new_video);
                this.binding.ivSearch.setVisibility(View.INVISIBLE);
                this.binding.ivDone.setVisibility(View.VISIBLE);
            } else {
                this.binding.tvFolderName.setText(R.string.recently_added);
            }
        } else {
            dismiss();
        }
        this.binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.2
            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextSubmit(String str) {
                VidPlylistDiaFrag.this.binding.searchView.clearFocus();
                return false;
            }

            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextChange(String str) {
                List<VideoInfo> searchVideoByVideoName = Utility.searchVideoByVideoName(VidPlylistDiaFrag.this.mVideos, str);
                if (VidPlylistDiaFrag.this.mAdapter == null) {
                    return false;
                }
                VidPlylistDiaFrag.this.mAdapter.updateVideoDataList(searchVideoByVideoName);
                return false;
            }
        });
        this.binding.tvCancelSearch.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.3
            @Override 
            public void onClick(View v) {
                VidPlylistDiaFrag.this.onSearchCancelClick();
            }
        });
        this.binding.ivSearch.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.4
            @Override 
            public void onClick(View v) {
                VidPlylistDiaFrag.this.onSearchClick();
            }
        });
        this.binding.ivSort.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.5
            @Override 
            public void onClick(View v) {
                VidPlylistDiaFrag.this.onSortClick();
            }
        });
        this.binding.ivViewMode.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.6
            @Override 
            public void onClick(View v) {
                VidPlylistDiaFrag.this.onViewModeClick();
            }
        });
        this.binding.rlAddVideo.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.7
            @Override 
            public void onClick(View v) {
                VidPlylistDiaFrag.this.onAddVideoClick();
            }
        });
        this.binding.ivAddVideo.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.8
            @Override 
            public void onClick(View v) {
                VidPlylistDiaFrag.this.onAddVideoIconClick();
            }
        });
        this.binding.ivBack.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.9
            @Override 
            public void onClick(View v) {
                VidPlylistDiaFrag.this.onBackClick();
            }
        });
        this.binding.ivDone.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag.10
            @Override 
            public void onClick(View v) {
                VidPlylistDiaFrag.this.onDoneClick();
            }
        });
        return this.binding.getRoot();
    }

    private void setViewDataEmpty(boolean z) {
        if (z) {
            this.binding.ivSearch.setVisibility(View.GONE);
            this.binding.ivSort.setVisibility(View.GONE);
            this.binding.ivViewMode.setVisibility(View.GONE);
            this.binding.rvVideoDialog.setVisibility(View.GONE);
            this.binding.rlNoVideo.setVisibility(View.VISIBLE);
            return;
        }
        this.binding.rlNoVideo.setVisibility(View.GONE);
        this.binding.rvVideoDialog.setVisibility(View.VISIBLE);
        this.binding.ivSort.setVisibility(View.VISIBLE);
        this.binding.ivViewMode.setVisibility(View.VISIBLE);
        this.binding.ivSearch.setVisibility(View.VISIBLE);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onSearchCancelClick() {
        this.binding.rlSearchView.setVisibility(View.INVISIBLE);
        this.binding.rlTitle.setVisibility(View.VISIBLE);
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.updateVideoDataList(this.mVideos);
        }
    }

    public void onSearchClick() {
        this.binding.rlSearchView.setVisibility(View.VISIBLE);
        this.binding.rlTitle.setVisibility(View.INVISIBLE);
        this.binding.searchView.setFocusable(true);
        this.binding.searchView.setIconified(false);
        this.binding.searchView.requestFocusFromTouch();
    }

    public void onSortClick() {
        BtmMenDialCont.getInstance().showSortDialogForVideo(this.mContext, new SorDialBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.vid.df.VidPlylistDiaFrag$$ExternalSyntheticLambda0
            @Override // com.videoplayer.videox.dialog.SorDialBuil.OkButtonClickListener
            public final void onClick(int i, boolean z) {
                VidPlylistDiaFrag.this.m658x611a6709(i, z);
            }
        });
    }

    /* renamed from: lambda$onSortClick$0$com-videoplayer-videox-fragment-vid-df-VidPlylistDiaFrag */
    void m658x611a6709(int i, boolean z) {
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.sortVideoList(i, z);
        }
    }

    public void onViewModeClick() {
        if (this.mCurrentViewMode == 1) {
            this.mCurrentViewMode = 2;
            this.binding.ivViewMode.setImageResource(R.drawable.baseline_grid_view_24);
            this.mGridLayoutManager.setSpanCount(2);
        } else {
            this.mCurrentViewMode = 1;
            this.binding.ivViewMode.setImageResource(R.drawable.baseline_view_list_24);
            this.mGridLayoutManager.setSpanCount(1);
        }
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.setViewMode(this.mCurrentViewMode);
        }
    }

    public void onAddVideoClick() {
        AddVidPlylistDiaFrag.newInstance(this.mPlaylist, this).show(getChildFragmentManager().beginTransaction(), "dialog_playlist_add_video");
    }

    public void onAddVideoIconClick() {
        onAddVideoClick();
    }

    public void onBackClick() {
        dismiss();
    }

    public void onDoneClick() {
        FolderCallback folderCallback = this.mFolderCallback;
        if (folderCallback != null) {
            VidInfAdapter vidInfAdapter = this.mAdapter;
            folderCallback.onAddVideoClick(vidInfAdapter == null ? new ArrayList<>() : vidInfAdapter.getVideoSelected());
        }
        dismiss();
    }

    @Override // com.videoplayer.videox.vie.VidVie
    public void openFavoriteVideo(List<VideoInfo> list) {
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.updateVideoDataList(list);
        }
        this.mVideos = list;
        this.binding.loading.setVisibility(View.GONE);
        setViewDataEmpty(this.mVideos.isEmpty());
    }

    @Override // com.videoplayer.videox.fragment.vid.df.AddVidPlylistDiaFrag.Callback
    public void onAddVideo(List<VideoInfo> list) {
        this.mVideos = list;
        if (list.isEmpty()) {
            this.binding.ivAddVideo.setVisibility(View.GONE);
        } else {
            this.binding.ivAddVideo.setVisibility(View.VISIBLE);
        }
        this.mPlaylist.setVideoList(list);
        setViewDataEmpty(this.mVideos.isEmpty());
        VidInfAdapter vidInfAdapter = this.mAdapter;
        if (vidInfAdapter != null) {
            vidInfAdapter.updateVideoDataList(list);
        }
        PlaylistCallback playlistCallback = this.mPlaylistCallback;
        if (playlistCallback != null) {
            playlistCallback.onUpdateVideoPlaylist(list);
        }
        ((VidPre) this.mPresenter).addVideoToPlaylist(this.mPlaylist, list);
    }

    @Override // com.videoplayer.videox.adapter.vid.VidInfAdapter.Callback
    public void onFavoriteUpdate(int i, boolean z) {
        VidInfAdapter vidInfAdapter;
        if (this.mType != 3 || z || (vidInfAdapter = this.mAdapter) == null) {
            return;
        }
        vidInfAdapter.removeItemPosition(i);
        if (this.mAdapter.isEmptyData()) {
            setViewDataEmpty(true);
        }
    }
}
