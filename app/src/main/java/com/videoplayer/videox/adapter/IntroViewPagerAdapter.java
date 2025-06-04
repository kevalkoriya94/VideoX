package com.videoplayer.videox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.videoplayer.videox.R;
import com.videoplayer.videox.cv.ScreenItem;

import java.util.List;


public class IntroViewPagerAdapter extends PagerAdapter {
    Context mContext;
    List<ScreenItem> mListScreen;

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    public IntroViewPagerAdapter(Context mCOntext, List<ScreenItem> mListScreen) {
        this.mContext = mCOntext;
        this.mListScreen = mListScreen;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup container, int position) {
        View inflate = ((LayoutInflater) this.mContext.getSystemService("layout_inflater")).inflate(R.layout.layout_screen_design, (ViewGroup) null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.intro_img);
        TextView textView = (TextView) inflate.findViewById(R.id.intro_title);
        TextView textView2 = (TextView) inflate.findViewById(R.id.intro_description);
        textView.setText(this.mListScreen.get(position).getTitle());
        textView2.setText(this.mListScreen.get(position).getDescription());
        imageView.setImageResource(this.mListScreen.get(position).getScreenImg());
        container.addView(inflate);
        return inflate;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.mListScreen.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
