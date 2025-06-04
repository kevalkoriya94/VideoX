package com.videoplayer.videox.fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.videoplayer.videox.pre.BasePre;


public abstract class BasDialFrag<Presenter extends BasePre> extends DialogFragment {
    protected Presenter mPresenter;

    protected abstract Presenter createPresenter();

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
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
