package com.videoplayer.videox.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.videoplayer.videox.pre.BasePre;


public abstract class BasFrag<Presenter extends BasePre> extends Fragment {
    protected Presenter mPresenter;

    protected abstract Presenter createPresenter();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPresenter = createPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Presenter presenter = this.mPresenter;
        if (presenter != null) {
            presenter.onDetach();
        }
    }
}
