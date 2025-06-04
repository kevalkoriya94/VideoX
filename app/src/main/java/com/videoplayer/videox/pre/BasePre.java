package com.videoplayer.videox.pre;

import com.videoplayer.videox.vie.BasVie;


public class BasePre<View extends BasVie> {
    protected View mView;

    public BasePre(View view) {
        this.mView = view;
    }

    public void onDetach() {
        this.mView = null;
    }
}
