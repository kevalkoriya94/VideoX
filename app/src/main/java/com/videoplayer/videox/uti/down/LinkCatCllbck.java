package com.videoplayer.videox.uti.down;


public interface LinkCatCllbck<T> {
    void loadFailed();

    void loadSuccess(T t, String str);

    void needLogin();
}
