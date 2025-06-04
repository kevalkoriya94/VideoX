package com.videoplayer.videox.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.videoplayer.videox.R;
import com.videoplayer.videox.uti.cons.AppCon;


public class ThmIteFrag extends Fragment {
    int theme;

    public ThmIteFrag() {
    }

    public ThmIteFrag(int i) {
        this.theme = i;
    }

    public static ThmIteFrag newInstance(int i) {
        return new ThmIteFrag(i);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_item_theme, viewGroup, false);
        Glide.with(this).load(Integer.valueOf(AppCon.Themes.THEMES[this.theme])).into((ImageView) inflate.findViewById(R.id.iv_theme));
        return inflate;
    }
}
