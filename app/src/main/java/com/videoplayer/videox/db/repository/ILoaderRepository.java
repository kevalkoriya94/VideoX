package com.videoplayer.videox.db.repository;

import java.util.List;


public interface ILoaderRepository<T> {

    
    interface InsertDataListener {
        void onError();

        void onSuccess();
    }

    
    interface LoadDataListener<T> {
        void onError();

        void onSuccess(List<T> list);
    }
}
