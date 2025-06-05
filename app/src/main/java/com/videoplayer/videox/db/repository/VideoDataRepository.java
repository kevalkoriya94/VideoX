package com.videoplayer.videox.db.repository;

import android.content.Context;
import android.util.Log;

import com.videoplayer.videox.db.datasource.VideoDatabaseDataSource;
import com.videoplayer.videox.db.datasource.VideoStorageDataSource;
import com.videoplayer.videox.db.entity.video.VideoFolder;
import com.videoplayer.videox.db.entity.video.VideoHistory;
import com.videoplayer.videox.db.entity.video.VideoInfo;
import com.videoplayer.videox.db.entity.video.VideoPlaylist;
import com.videoplayer.videox.db.entity.video.VideoSubtitle;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class VideoDataRepository {
    private final VideoDatabaseDataSource videoDatabaseDataSource;
    private final VideoStorageDataSource videoStorageDataSource;

    public VideoDataRepository(Context context) {
        this.videoStorageDataSource = new VideoStorageDataSource(context);
        this.videoDatabaseDataSource = new VideoDatabaseDataSource(context);
    }

    public void getAllVideos(final ILoaderRepository.LoadDataListener<VideoInfo> loadDataListener) {
        Single.defer((Callable) () -> Single.just(videoStorageDataSource.getAllVideoFromStorage())).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<VideoInfo>>() { 
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<VideoInfo> list) {
                loadDataListener.onSuccess(list);
            }

            @Override
            public void onError(Throwable th) {
                Log.e("TAG", "updateVideoList: "+th.getMessage());
                loadDataListener.onError();
            }
        });
    }

    public void getAllFolders(final ILoaderRepository.LoadDataListener<VideoFolder> loadDataListener) {
        Single.defer(new Callable() {
            @Override
            public Object call() {
                return Single.just(videoStorageDataSource.getAllVideoOfFolder());
            }
        }).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<VideoFolder>>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<VideoFolder> list) {
                loadDataListener.onSuccess(list);
            }

            @Override
            public void onError(Throwable th) {
                loadDataListener.onError();
            }
        });
    }

    public void getAllPlaylistVideos(final ILoaderRepository.LoadDataListener<VideoPlaylist> loadDataListener) {
        Single.defer(new Callable() {
            @Override
            public Object call() {
                return Single.just(videoDatabaseDataSource.getAllPlaylistVideos());
            }
        }).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<VideoPlaylist>>() {             @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<VideoPlaylist> list) {
                loadDataListener.onSuccess(list);
            }

            @Override
            public void onError(Throwable th) {
                loadDataListener.onError();
            }
        });
    }

    public void getHistoryVideos(final ILoaderRepository.LoadDataListener<VideoHistory> loadDataListener) {
        Single.defer((Callable) () -> VideoDataRepository.this.getHistoryVideosOVideoDataRepository()).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<VideoHistory>>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<VideoHistory> list) {
                loadDataListener.onSuccess(list);
            }

            @Override
            public void onError(Throwable th) {
                loadDataListener.onError();
            }
        });
    }

    public SingleSource getHistoryVideosOVideoDataRepository() {
        return Single.just(this.videoDatabaseDataSource.getHistoryVideos());
    }

    public void getUnWatchedVideos(final ILoaderRepository.LoadDataListener<VideoInfo> loadDataListener) {
        Single.defer(new Callable() {
            @Override
            public Object call() {
                return Single.just(videoStorageDataSource.getUnwatchedVideoFromStorage());
            }
        }).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<VideoInfo>>() {             @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<VideoInfo> list) {
                loadDataListener.onSuccess(list);
            }

            @Override
            public void onError(Throwable th) {
                loadDataListener.onError();
            }
        });
    }


    public void createVideoPlaylist(final ILoaderRepository.InsertDataListener insertDataListener, final VideoPlaylist videoPlaylist) {
        Single.defer(new Callable() {
            @Override
            public Object call() {
                return Single.just(videoDatabaseDataSource.createVideoPlaylist(videoPlaylist));
            }
        }).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() {             @Override
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

    public void duplicateVideoPlaylist(final ILoaderRepository.InsertDataListener insertDataListener, final VideoPlaylist videoPlaylist) {
        Single.defer(new Callable() {
            @Override
            public Object call() {
                return Single.just(videoDatabaseDataSource.duplicateVideoPlaylist(videoPlaylist));
            }
        }).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() {             @Override
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

    public void updateVideoListForPlaylist(VideoPlaylist videoPlaylist, List<VideoInfo> list) {
        this.videoDatabaseDataSource.updateVideoListForPlaylist(videoPlaylist, list);
    }

    public void updatePlaylistName(final ILoaderRepository.InsertDataListener insertDataListener, final VideoPlaylist videoPlaylist, final String str) {
        Single.defer(new Callable() {
            @Override
            public Object call() {
                return Single.just(videoDatabaseDataSource.updatePlaylistName(videoPlaylist, str));
            }
        }).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Boolean>() {             @Override
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

    public void deletePlaylist(VideoPlaylist videoPlaylist) {
        this.videoDatabaseDataSource.deletePlaylist(videoPlaylist);
    }

    public void deleteAHistoryVideoById(long j) {
        this.videoDatabaseDataSource.deleteVideoHistoryById(j);
    }

    public void deleteAllHistoryVideo() {
        this.videoDatabaseDataSource.deleteAllVideoHistory();
    }

    public void updateVideoHistoryData(VideoInfo videoInfo) {
        this.videoDatabaseDataSource.updateVideoHistoryData(videoInfo);
    }

    public void updateVideoTimeData(long j, long j2) {
        this.videoDatabaseDataSource.updateVideoTimeData(j, j2);
    }

    public int getAVideoTimeData(long j) {
        return this.videoDatabaseDataSource.getAVideoTimeData(j);
    }

    public void getAllFavoriteVideo(final ILoaderRepository.LoadDataListener<VideoInfo> loadDataListener) {
        Single.defer((Callable) () -> Single.just(videoDatabaseDataSource.getAllFavoriteVideo())).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<VideoInfo>>() {             @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<VideoInfo> list) {
                loadDataListener.onSuccess(list);
            }
        });
    }

    public void searchVideoByVideoName(final ILoaderRepository.LoadDataListener<VideoInfo> loadDataListener, final String str) {
        Single.defer(new Callable() {
            @Override
            public Object call() {
                return Single.just(videoDatabaseDataSource.searchVideoByVideoName(str));
            }
        }).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<VideoInfo>>() {             @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<VideoInfo> list) {
                loadDataListener.onSuccess(list);
            }
        });
    }

    public void getAllSubtitleFile(final ILoaderRepository.LoadDataListener<VideoSubtitle> loadDataListener) {
        Single.defer(new Callable() {
            @Override
            public Object call() {
                return Single.just(videoStorageDataSource.getAllSubFile());
            }
        }).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<VideoSubtitle>>() {
            @Override
            public void onError(Throwable th) {
            }

            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<VideoSubtitle> list) {
                loadDataListener.onSuccess(list);
            }
        });
    }

    public void getAllVideoHidden(final ILoaderRepository.LoadDataListener<VideoInfo> loadDataListener) {
        Single.defer(new Callable() {
            @Override
            public Object call() {
                return Single.just(videoDatabaseDataSource.getAllVideoHidden());
            }
        }).delay(300L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<VideoInfo>>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            public void onSuccess(List<VideoInfo> list) {
                loadDataListener.onSuccess(list);
            }

            @Override
            public void onError(Throwable th) {
                loadDataListener.onError();
            }
        });
    }
}
