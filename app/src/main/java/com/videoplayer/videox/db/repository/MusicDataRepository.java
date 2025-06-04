package com.videoplayer.videox.db.repository;

import android.content.Context;
import android.util.Log;

import com.videoplayer.videox.db.datasource.MusicDatabaseDataSource;
import com.videoplayer.videox.db.datasource.MusicStorageDataSource;
import com.videoplayer.videox.db.entity.music.MusicAlbum;
import com.videoplayer.videox.db.entity.music.MusicArtist;
import com.videoplayer.videox.db.entity.music.MusicHistory;
import com.videoplayer.videox.db.entity.music.MusicInfo;
import com.videoplayer.videox.db.entity.music.MusicPlaylist;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MusicDataRepository {
    private static final int TIME_DELAY = 300;
    private final MusicDatabaseDataSource musicDatabaseDataSource;
    private final MusicStorageDataSource musicStorageDataSource;

    public MusicDataRepository(Context context) {
        this.musicStorageDataSource = new MusicStorageDataSource(context);
        this.musicDatabaseDataSource = new MusicDatabaseDataSource(context);
    }

    public void getAllMusics(final ILoaderRepository.LoadDataListener<MusicInfo> loadDataListener) {
        Single.defer((Callable) () -> MusicDataRepository.this.getAllMusics_MusicDataRepository()).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicInfo>>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<MusicInfo> list) {
                loadDataListener.onSuccess(list);
            }

            @Override
            public void onError(Throwable th) {
                loadDataListener.onError();
            }
        });
    }

    public SingleSource getAllMusics_MusicDataRepository() {
        return Single.just(this.musicStorageDataSource.getAllMusicFromStorage());
    }

    public SingleSource updatePlaylistName_MusicDataRepository(MusicPlaylist musicPlaylist, String str) {
        return Single.just(this.musicDatabaseDataSource.updatePlaylistName(musicPlaylist, str));
    }

    public void updatePlaylistName(final ILoaderRepository.InsertDataListener insertDataListener, final MusicPlaylist musicPlaylist, final String str) {
        Single.defer((Callable) () -> MusicDataRepository.this.updatePlaylistName_MusicDataRepository(musicPlaylist, str)).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(Boolean bool) {
                if (bool) {
                    insertDataListener.onSuccess();
                } else {
                    insertDataListener.onError();
                }
            }
        });
    }

    public void duplicateMusicPlaylist(final ILoaderRepository.InsertDataListener insertDataListener, final MusicPlaylist musicPlaylist) {
        Single.defer((Callable) () -> MusicDataRepository.this.duplicateMusicPlaylist_MusicDataRepository(musicPlaylist)).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(Boolean bool) {
                if (bool) {
                    insertDataListener.onSuccess();
                } else {
                    insertDataListener.onError();
                }
            }
        });
    }

    public SingleSource duplicateMusicPlaylist_MusicDataRepository(MusicPlaylist musicPlaylist) {
        return Single.just(this.musicDatabaseDataSource.duplicateMusicPlaylist(musicPlaylist));
    }

    public void getAllFavoriteMusic(final ILoaderRepository.LoadDataListener<MusicInfo> loadDataListener) {
        Single.defer((Callable) () -> MusicDataRepository.this.getAllFavoriteMusic_MusicDataRepository()).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicInfo>>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<MusicInfo> list) {
                Log.d("binhnk08", "musics = " + list.size());
                loadDataListener.onSuccess(list);
            }
        });
    }

    public SingleSource getAllFavoriteMusic_MusicDataRepository() {
        return Single.just(this.musicDatabaseDataSource.getAllFavoriteMusic());
    }

    public void getAllPlaylistMusics(final ILoaderRepository.LoadDataListener<MusicPlaylist> loadDataListener) {
        Single.defer((Callable) () -> MusicDataRepository.this.getAllPlaylistMusics_MusicDataRepository()).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicPlaylist>>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<MusicPlaylist> list) {
                loadDataListener.onSuccess(list);
            }

            @Override
            public void onError(Throwable th) {
                loadDataListener.onError();
            }
        });
    }

    public SingleSource getAllPlaylistMusics_MusicDataRepository() {
        return Single.just(this.musicDatabaseDataSource.getAllPlaylistMusics());
    }

    public void getHistoryMusics(final ILoaderRepository.LoadDataListener<MusicHistory> loadDataListener) {
        Single.defer((Callable) () -> MusicDataRepository.this.getHistoryMusics_MusicDataRepository()).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicHistory>>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<MusicHistory> list) {
                Log.d("binhnk08", "musics = " + list.size());
                loadDataListener.onSuccess(list);
            }
        });
    }

    public SingleSource getHistoryMusics_MusicDataRepository() {
        return Single.just(this.musicDatabaseDataSource.getHistoryMusics());
    }

    public void createMusicPlaylist(final ILoaderRepository.InsertDataListener insertDataListener, final MusicPlaylist musicPlaylist) {
        Single.defer((Callable) () -> MusicDataRepository.this.createMusicPlaylist_MusicDataRepository(musicPlaylist)).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(Boolean bool) {
                if (bool) {
                    insertDataListener.onSuccess();
                } else {
                    insertDataListener.onError();
                }
            }
        });
    }

    public SingleSource createMusicPlaylist_MusicDataRepository(MusicPlaylist musicPlaylist) {
        return Single.just(this.musicDatabaseDataSource.createMusicPlaylist(musicPlaylist));
    }

    public void updateMusicHistoryData(MusicInfo musicInfo) {
        this.musicDatabaseDataSource.updateMusicHistoryData(musicInfo);
    }

    public void updateMusicListForPlaylist(MusicPlaylist musicPlaylist, List<MusicInfo> list) {
        this.musicDatabaseDataSource.updateMusicListForPlaylist(musicPlaylist, list);
    }

    public void deletePlaylist(MusicPlaylist musicPlaylist) {
        this.musicDatabaseDataSource.deletePlaylist(musicPlaylist);
    }

    public void getAllMusicAlbum(final ILoaderRepository.LoadDataListener<MusicAlbum> loadDataListener) {
        Single.just(this.musicStorageDataSource.getAllMusicAlbum()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicAlbum>>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<MusicAlbum> list) {
                loadDataListener.onSuccess(list);
            }
        });
    }

    public void getAllMusicArtist(final ILoaderRepository.LoadDataListener<MusicArtist> loadDataListener) {
        Single.just(this.musicStorageDataSource.getAllMusicArtist()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicArtist>>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<MusicArtist> list) {
                loadDataListener.onSuccess(list);
            }
        });
    }

    public void getAllAlbumOfArtist(final ILoaderRepository.LoadDataListener<MusicAlbum> loadDataListener, MusicArtist musicArtist) {
        Single.just(this.musicStorageDataSource.getAllAlbumOfArtist(musicArtist)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicAlbum>>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<MusicAlbum> list) {
                loadDataListener.onSuccess(list);
            }
        });
    }

    public void getAllMusicOfAlbum(final ILoaderRepository.LoadDataListener<MusicInfo> loadDataListener, MusicAlbum musicAlbum) {
        Single.just(this.musicStorageDataSource.getAllMusicFromAlbum(musicAlbum.getAlbumId())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicInfo>>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<MusicInfo> list) {
                loadDataListener.onSuccess(list);
            }
        });
    }

    public void getAllSongsOfArtist(final ILoaderRepository.LoadDataListener<MusicInfo> loadDataListener, MusicArtist musicArtist) {
        Single.just(this.musicStorageDataSource.getAllSongsOfArtist(musicArtist.getArtistId())).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicInfo>>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<MusicInfo> list) {
                loadDataListener.onSuccess(list);
            }
        });
    }

    public void deleteAHistoryMusicById(long j) {
        this.musicDatabaseDataSource.deleteMusicHistoryById(j);
    }

    public SingleSource searchMusicByMusicName_MusicDataRepository(String str) {
        return Single.just(this.musicDatabaseDataSource.searchMusicByMusicName(str));
    }

    public void searchMusicByMusicName(final ILoaderRepository.LoadDataListener<MusicInfo> loadDataListener, final String str) {
        Single.defer((Callable) () -> MusicDataRepository.this.searchMusicByMusicName_MusicDataRepository(str)).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<MusicInfo>>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<MusicInfo> list) {
                loadDataListener.onSuccess(list);
            }
        });
    }

    public void deleteAllMusicHistory() {
        this.musicDatabaseDataSource.deleteAllMusicHistory();
    }
}
