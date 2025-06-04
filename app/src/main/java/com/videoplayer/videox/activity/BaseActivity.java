package com.videoplayer.videox.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.videoplayer.videox.pre.BasePre;


public abstract class BaseActivity<Presenter extends BasePre> extends AppCompatActivity {
    protected Presenter mPresenter;

    protected abstract Presenter createPresenter();

    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPresenter = createPresenter();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        Presenter presenter = this.mPresenter;
        if (presenter != null) {
            presenter.onDetach();
        }
    }
}
