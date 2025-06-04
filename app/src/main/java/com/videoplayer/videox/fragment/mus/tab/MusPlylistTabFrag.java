package com.videoplayer.videox.fragment.mus.tab;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.mus.MusInfAdapter;
import com.videoplayer.videox.adapter.mus.MusPlalisAdapter;
import com.videoplayer.videox.dialog.InpDialBui;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.fragment.BasFrag;
import com.videoplayer.videox.fragment.mus.p014df.MusPlylistDialFrag;
import com.videoplayer.videox.cv.NpaGridLayManager;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.music.MusicPlaylist;
import com.videoplayer.videox.db.repository.MusicDataRepository;
import com.videoplayer.videox.pre.mus.MusPlaylistPre;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;
import com.videoplayer.videox.vie.mus.MusPlylistVie;

import java.util.ArrayList;
import java.util.List;


public class MusPlylistTabFrag extends BasFrag<MusPlaylistPre> implements MusPlylistVie, MusInfAdapter.Callback, MusPlalisAdapter.Callback {
    private final ContentObserver contentObserver = new ContentObserver(new Handler()) { // from class: com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            if (MusPlylistTabFrag.this.mPresenter != null) {
                ((MusPlaylistPre) MusPlylistTabFrag.this.mPresenter).openPlaylistTab();
            }
        }
    };
    private ProgressBar loading;
    private MusPlalisAdapter mAdapter;
    private Context mContext;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mRvContent;
    private SwipeRefreshLayout refreshLayout;
    private TextView tvTotal;

    @Override // com.videoplayer.videox.adapter.mus.MusInfAdapter.Callback
    public void onFavoriteUpdate(int i, boolean z) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static MusPlylistTabFrag newInstance() {
        return new MusPlylistTabFrag();
    }

    @Override // com.videoplayer.videox.fragment.BasFrag
    public MusPlaylistPre createPresenter() {
        Context context = this.mContext;
        return new MusPlaylistPre(context, this, new MusicDataRepository(context));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_video_playlist_tab, viewGroup, false);
        this.mRvContent = (RecyclerView) inflate.findViewById(R.id.rv_content_tab);
        this.tvTotal = (TextView) inflate.findViewById(R.id.tv_total);
        this.refreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.swipe_refresh);
        this.loading = (ProgressBar) inflate.findViewById(R.id.loading);
        this.mAdapter = new MusPlalisAdapter(this.mContext, new ArrayList(), this);
        this.mGridLayoutManager = new NpaGridLayManager(this.mContext, 1);
        NpaGridLayManager npaGridLayManager = new NpaGridLayManager(this.mContext, 1);
        this.mGridLayoutManager = npaGridLayManager;
        npaGridLayManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag.2
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                return (MusPlylistTabFrag.this.mGridLayoutManager.getSpanCount() == 2 && i % 11 == 3) ? 2 : 1;
            }
        });
        this.mRvContent.setLayoutManager(this.mGridLayoutManager);
        this.mRvContent.setAdapter(this.mAdapter);
        RecyclerView.ItemAnimator itemAnimator = this.mRvContent.getItemAnimator();
        if (itemAnimator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        this.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag$$ExternalSyntheticLambda0
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public final void onRefresh() {
                MusPlylistTabFrag.this.lambda$onCreateView$0$MusicPlaylistTabFragment();
            }
        });
        return inflate;
    }

    public void lambda$onCreateView$0$MusicPlaylistTabFragment() {
        if (this.mPresenter != null) {
            ((MusPlaylistPre) this.mPresenter).openPlaylistTab();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.mPresenter != null) {
            ((MusPlaylistPre) this.mPresenter).openPlaylistTab();
        }
        updateNumberFavorite();
        requireActivity().getContentResolver().registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, this.contentObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().getContentResolver().unregisterContentObserver(this.contentObserver);
    }

    public void setViewMode(int i) {
        if (i == 1) {
            GridLayoutManager gridLayoutManager = this.mGridLayoutManager;
            if (gridLayoutManager != null) {
                gridLayoutManager.setSpanCount(1);
            }
        } else {
            GridLayoutManager gridLayoutManager2 = this.mGridLayoutManager;
            if (gridLayoutManager2 != null) {
                gridLayoutManager2.setSpanCount(3);
            }
        }
        MusPlalisAdapter musPlalisAdapter = this.mAdapter;
        if (musPlalisAdapter != null) {
            musPlalisAdapter.setViewMode(i);
        }
    }

    @Override // com.videoplayer.videox.vie.mus.MusPlylistVie
    public void updateMusicPlaylist(List<MusicPlaylist> list) {
        this.loading.setVisibility(View.GONE);
        this.refreshLayout.setVisibility(View.VISIBLE);
        this.refreshLayout.setRefreshing(false);
        this.tvTotal.setText(getString(R.string.all_playlist, Integer.valueOf(list.size())));
        MusPlalisAdapter musPlalisAdapter = this.mAdapter;
        if (musPlalisAdapter != null) {
            musPlalisAdapter.updatePlaylist(list);
        }
    }

    @Override // com.videoplayer.videox.vie.mus.MusPlylistVie
    public void onCreatePlaylist(boolean z, MusicPlaylist musicPlaylist) {
        AdmobAdsHelper.showAdsNumberCount++;
        if (z) {
            MusPlylistDialFrag.newInstance(1, musicPlaylist, new MusPlylistDialFrag.Callback() { // from class: com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag.3
                @Override // com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.Callback
                public void onUpdateMusicPlaylist(List<MusicInfo> list) {
                }

                @Override // com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.Callback
                public void onDialogDismiss() {
                    MusPlylistTabFrag.this.updateNumberFavorite();
                    if (MusPlylistTabFrag.this.mPresenter != null) {
                        ((MusPlaylistPre) MusPlylistTabFrag.this.mPresenter).openPlaylistTab();
                    }
                }
            }).show(getChildFragmentManager().beginTransaction(), "dialog_playlist_video");
        } else {
            Toast.makeText(this.mContext, R.string.duplicate_name, Toast.LENGTH_SHORT).show();
        }
    }

    public void sortPlaylist(int i) {
        MusPlalisAdapter musPlalisAdapter = this.mAdapter;
        if (musPlalisAdapter != null) {
            musPlalisAdapter.sortPlaylist(i);
        }
    }

    public void updateNumberFavorite() {
        MusPlalisAdapter musPlalisAdapter = this.mAdapter;
        if (musPlalisAdapter != null) {
            musPlalisAdapter.updateTotalFavoriteMusic();
        }
    }

    public void lambda$onPlaylistOptionSelect$1$MusicPlaylistTabFragment(MusicPlaylist musicPlaylist, int i, String str) {
        String trim = str.trim();
        if (TextUtils.isEmpty(str.trim())) {
            Toast.makeText(this.mContext, R.string.empty_playlist_name, Toast.LENGTH_SHORT).show();
        } else {
            if (trim.equals(musicPlaylist.getPlaylistName()) || this.mPresenter == null) {
                return;
            }
            ((MusPlaylistPre) this.mPresenter).updatePlaylistName(musicPlaylist, str.trim(), i);
        }
    }

    public void lambda$onPlaylistOptionSelect$2$MusicPlaylistTabFragment(MusicPlaylist musicPlaylist, String str) {
        String trim = str.trim();
        if (TextUtils.isEmpty(trim)) {
            Toast.makeText(this.mContext, R.string.empty_playlist_name, Toast.LENGTH_SHORT).show();
        } else if (this.mPresenter != null) {
            ((MusPlaylistPre) this.mPresenter).duplicateMusicPlaylist(trim, musicPlaylist);
        }
    }

    @Override // com.videoplayer.videox.adapter.mus.MusPlalisAdapter.Callback
    public void onPlaylistOptionSelect(final MusicPlaylist musicPlaylist, int i, final int i2) {
        if (i == 0) {
            AdmobAdsHelper.showAdsNumberCount++;
            new InpDialBui(this.mContext, new InpDialBui.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag.4
                @Override 
                public void onClick(String str) {
                    MusPlylistTabFrag.this.lambda$onPlaylistOptionSelect$1$MusicPlaylistTabFragment(musicPlaylist, i2, str);
                }
            }, musicPlaylist.getPlaylistName()).setTitle(R.string.create_new_playlist, ContextCompat.getColor(this.mContext, R.color.main_orange)).build().show();
        } else if (i == 1) {
            AdmobAdsHelper.showAdsNumberCount++;
            new InpDialBui(this.mContext, new InpDialBui.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag.5
                @Override 
                public void onClick(String str) {
                    MusPlylistTabFrag.this.lambda$onPlaylistOptionSelect$2$MusicPlaylistTabFragment(musicPlaylist, str);
                }
            }, "").setTitle(R.string.name_the_playlist, ContextCompat.getColor(this.mContext, R.color.main_orange)).build().show();
        } else if (i == 2) {
            AdmobAdsHelper.showAdsNumberCount++;
            new QueDiaBuil(this.mContext, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag.6
                @Override 
                public void onCancelClick() {
                }

                @Override 
                public void onOkClick() {
                    if (MusPlylistTabFrag.this.mAdapter != null) {
                        MusPlylistTabFrag.this.mAdapter.removeItemPosition(i2);
                    }
                    if (MusPlylistTabFrag.this.mPresenter != null) {
                        ((MusPlaylistPre) MusPlylistTabFrag.this.mPresenter).deletePlaylist(musicPlaylist);
                    }
                }
            }).setTitle(R.string.delete, this.mContext.getResources().getColor(R.color.color_FF6666)).setQuestion(R.string.question_remove_playlist).build().show();
        }
    }

    @Override // com.videoplayer.videox.adapter.mus.MusPlalisAdapter.Callback
    public void createPlaylist(String str) {
        if (TextUtils.isEmpty(str.trim())) {
            Toast.makeText(this.mContext, R.string.empty_playlist_name, Toast.LENGTH_SHORT).show();
        } else if (this.mPresenter != null) {
            ((MusPlaylistPre) this.mPresenter).createMusicPlaylist(str.trim());
        }
    }

    @Override // com.videoplayer.videox.adapter.mus.MusPlalisAdapter.Callback
    public void onPlaylistClick(final int i, MusicPlaylist musicPlaylist) {
        if (i == 1) {
            MusPlylistDialFrag.newInstance(2, new MusPlylistDialFrag.Callback() { // from class: com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag.7
                @Override // com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.Callback
                public void onUpdateMusicPlaylist(List<MusicInfo> list) {
                }

                @Override // com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.Callback
                public void onDialogDismiss() {
                    MusPlylistTabFrag.this.updateNumberFavorite();
                }
            }).show(getChildFragmentManager().beginTransaction(), "dialog_favorite_music");
        } else {
            MusPlylistDialFrag.newInstance(1, musicPlaylist, new MusPlylistDialFrag.Callback() { // from class: com.videoplayer.videox.fragment.mus.tab.MusPlylistTabFrag.8
                @Override // com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.Callback
                public void onUpdateMusicPlaylist(List<MusicInfo> list) {
                    if (MusPlylistTabFrag.this.mAdapter != null) {
                        MusPlylistTabFrag.this.mAdapter.updateItemMusicPosition(i, list);
                    }
                }

                @Override // com.videoplayer.videox.fragment.mus.df.MusPlylistDialFrag.Callback
                public void onDialogDismiss() {
                    MusPlylistTabFrag.this.updateNumberFavorite();
                }
            }).show(getChildFragmentManager().beginTransaction(), "dialog_playlist_music");
        }
    }

    @Override // com.videoplayer.videox.vie.mus.MusPlylistVie
    public void onUpdatePlaylistName(int i, String str, boolean z) {
        if (z) {
            Toast.makeText(this.mContext, R.string.successfully, Toast.LENGTH_SHORT).show();
            MusPlalisAdapter musPlalisAdapter = this.mAdapter;
            if (musPlalisAdapter != null) {
                musPlalisAdapter.updateItemNamePosition(i, str);
                return;
            }
            return;
        }
        Toast.makeText(this.mContext, R.string.duplicate_name, Toast.LENGTH_SHORT).show();
    }

    @Override // com.videoplayer.videox.vie.mus.MusPlylistVie
    public void onDuplicationPlaylist(MusicPlaylist musicPlaylist) {
        if (musicPlaylist == null) {
            Toast.makeText(this.mContext, R.string.duplicate_name, Toast.LENGTH_SHORT).show();
            return;
        }
        MusPlalisAdapter musPlalisAdapter = this.mAdapter;
        if (musPlalisAdapter != null) {
            musPlalisAdapter.addItemPosition(musicPlaylist);
            this.mRvContent.smoothScrollToPosition(this.mAdapter.getItemCount() - 1);
        }
    }
}
