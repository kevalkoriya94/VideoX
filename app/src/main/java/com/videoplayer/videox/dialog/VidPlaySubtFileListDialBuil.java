package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.vid.VidSubAdapter;
import com.videoplayer.videox.db.entity.video.VideoSubtitle;
import com.videoplayer.videox.db.repository.VideoDataRepository;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.pre.vid.VidSubPre;
import com.videoplayer.videox.uti.cons.AppCon;
import com.videoplayer.videox.vie.vid.SubttlVie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class VidPlaySubtFileListDialBuil implements SubttlVie {
    private final ProgressBar loading;
    private final VidSubAdapter mAdapter;
    private final Dialog mDialog;
    private final RecyclerView mRvContent;
    private List<VideoSubtitle> mSubtitles;
    private final SearchView searchView;

    public VidPlaySubtFileListDialBuil(Context context, VidSubAdapter.Callback callback) {
        int themes = new SettingPrefUtils(context).getThemes();
        Dialog dialog = new Dialog(context, AppCon.Themes.THEMES_STYLE[themes]);
        this.mDialog = dialog;
        Window window = dialog.getWindow();
        if (window != null && themes == 1) {
            Window window2 = dialog.getWindow();
            Objects.requireNonNull(window2);
            window2.setBackgroundDrawable(new ColorDrawable(-1));
            window.setStatusBarColor(ContextCompat.getColor(context, R.color.ui_controls_background));
            window.setNavigationBarColor(ContextCompat.getColor(context, R.color.ui_controls_background));
        }
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_video_player_subtitle_file_list);
        dialog.findViewById(R.id.tv_cancel_search).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlaySubtFileListDialBuil$$ExternalSyntheticLambda0
            @Override 
            public final void onClick(View view) {
                VidPlaySubtFileListDialBuil.this.m638xe3626b2(view);
            }
        });
        this.loading = (ProgressBar) dialog.findViewById(R.id.loading);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.rv_content);
        this.mRvContent = recyclerView;
        VidSubAdapter vidSubAdapter = new VidSubAdapter(context, callback);
        this.mAdapter = vidSubAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(vidSubAdapter);
        SearchView searchView = (SearchView) dialog.findViewById(R.id.search_view);
        this.searchView = searchView;
        this.mSubtitles = new ArrayList();
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() { // from class: com.videoplayer.videox.dialog.VidPlaySubtFileListDialBuil.1
            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextSubmit(String str) {
                VidPlaySubtFileListDialBuil.this.searchView.clearFocus();
                return false;
            }

            @Override // androidx.appcompat.widget.SearchView.OnQueryTextListener
            public boolean onQueryTextChange(String str) {
                List<VideoSubtitle> searchSubtitle = VidPlaySubtFileListDialBuil.this.searchSubtitle(str);
                if (VidPlaySubtFileListDialBuil.this.mAdapter == null) {
                    return false;
                }
                VidPlaySubtFileListDialBuil.this.mAdapter.updateSubtitle(searchSubtitle);
                return false;
            }
        });
        new VidSubPre(this, new VideoDataRepository(context)).getAllSubtitleFile();
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-VidPlaySubtFileListDialBuil */
    void m638xe3626b2(View view) {
        this.mDialog.dismiss();
    }

    public Dialog build() {
        return this.mDialog;
    }

    public boolean isShowing() {
        Dialog dialog = this.mDialog;
        if (dialog == null) {
            return false;
        }
        return dialog.isShowing();
    }

    public void dismiss() {
        Dialog dialog = this.mDialog;
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        this.mDialog.dismiss();
    }

    @Override // com.videoplayer.videox.vie.vid.SubttlVie
    public void updateSubtitle(List<VideoSubtitle> list) {
        this.mSubtitles = new ArrayList(list);
        Log.d("binhnk08", " subtitles.size = " + list.size());
        if (this.mDialog.isShowing()) {
            VidSubAdapter vidSubAdapter = this.mAdapter;
            if (vidSubAdapter != null) {
                vidSubAdapter.updateSubtitle(list);
            }
            this.mRvContent.setVisibility(View.VISIBLE);
            this.loading.setVisibility(View.GONE);
        }
    }

    public List<VideoSubtitle> searchSubtitle(String str) {
        if (TextUtils.isEmpty(str.trim())) {
            return this.mSubtitles;
        }
        ArrayList arrayList = new ArrayList();
        for (VideoSubtitle videoSubtitle : this.mSubtitles) {
            if (videoSubtitle.getName().toLowerCase().contains(str.trim().toLowerCase())) {
                arrayList.add(videoSubtitle);
            }
        }
        return arrayList;
    }
}
