package com.videoplayer.videox.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.facebook.ads.NativeAdLayout;
import com.google.android.material.tabs.TabLayout;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.IntroViewPagerAdapter;
import com.videoplayer.videox.cv.ScreenItem;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;

import java.util.ArrayList;


public class IntroActivity extends AppCompatActivity {
    Button btnGetStarted;
    Button btnSkip;
    IntroViewPagerAdapter introViewPagerAdapter;
    private ViewPager screenPager;
    TabLayout tabIndicator;

    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_intro);
//        if (restorePrefData()) {
//            startActivity(new Intent(getApplicationContext(), StartPolicyActivity.class));
//            finish();
//        }
        setContentView(R.layout.activity_intro);
        this.btnSkip = findViewById(R.id.btn_skip);
        this.btnGetStarted = findViewById(R.id.btn_get_started);
        this.tabIndicator = findViewById(R.id.tab_indicator);
        final ArrayList arrayList = new ArrayList();
        arrayList.add(new ScreenItem("Video Player", "All format videos & all format Audio supported", R.drawable.img1));
        arrayList.add(new ScreenItem("Picture In Picture", "Allowing users to minimize the video player into a small screen while continuing to watch their video.", R.drawable.img2));
        arrayList.add(new ScreenItem("Music Player", "Best Music Player included in this app", R.drawable.img3));
        this.screenPager = findViewById(R.id.screen_viewpager);
        IntroViewPagerAdapter introViewPagerAdapter = new IntroViewPagerAdapter(this, arrayList);
        this.introViewPagerAdapter = introViewPagerAdapter;
        this.screenPager.setAdapter(introViewPagerAdapter);
        this.tabIndicator.setupWithViewPager(this.screenPager);
        this.btnSkip.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.IntroActivity.1
            @Override
            public void onClick(View view) {
                IntroActivity.this.screenPager.setCurrentItem(arrayList.size());
            }
        });
        this.tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() { // from class: com.videoplayer.videox.activity.IntroActivity.2
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == arrayList.size() - 1) {
                    IntroActivity.this.loadLastScreen();
                    return;
                }
                IntroActivity.this.btnSkip.setVisibility(View.VISIBLE);
                IntroActivity.this.btnGetStarted.setVisibility(View.GONE);
                IntroActivity.this.tabIndicator.setVisibility(View.VISIBLE);
            }
        });
        AdmobAdsHelper.smallnativeAds(this, findViewById(R.id.ad_layout), findViewById(R.id.adspace), findViewById(R.id.native_banner_ad_container), 1);
        this.btnGetStarted.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.IntroActivity.3
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this.getApplicationContext(), StartPolicyActivity.class);
                IntroActivity.this.savePrefsData();
                IntroActivity.this.startActivity(intent);
                AdmobAdsHelper.ShowFullAds(IntroActivity.this, false);
                IntroActivity.this.finish();
            }
        });
    }

    private boolean restorePrefData() {
        return getApplicationContext().getSharedPreferences("myPrefs", 0).getBoolean("isIntroOpened", false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void savePrefsData() {
        SharedPreferences.Editor edit = getApplicationContext().getSharedPreferences("myPrefs", 0).edit();
        edit.putBoolean("isIntroOpened", true);
        edit.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadLastScreen() {
        this.btnSkip.setVisibility(View.INVISIBLE);
        this.btnGetStarted.setVisibility(View.VISIBLE);
        this.tabIndicator.setVisibility(View.INVISIBLE);
    }
}
