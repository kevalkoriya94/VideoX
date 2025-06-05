package com.videoplayer.videox.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.videoplayer.videox.activity.ContActivity;
import com.videoplayer.videox.activity.PrivPoliActivity;
import com.videoplayer.videox.activity.SetActivity;
import com.videoplayer.videox.databinding.FragmentSettingBinding;
import com.videoplayer.videox.dialog.RatDiaBuil;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;


public class SetFrag extends Fragment {
    FragmentSettingBinding binding;
    private Context mContext;

    static void lambda$onRateClick$4() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public static SetFrag newInstance() {
        return new SetFrag();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.binding = FragmentSettingBinding.inflate(getLayoutInflater());
        initSettingView();
        return this.binding.getRoot();
    }

    public void initSettingView() {
        this.binding.llPrivacy.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.SetFrag$$ExternalSyntheticLambda1
            @Override 
            public void onClick(View view) {
                SetFrag.this.m910lambda$initSettingView$0$comvideoplayervideoxfragmentSetFrag(view);
            }
        });
        this.binding.llSettings.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.SetFrag$$ExternalSyntheticLambda2
            @Override 
            public void onClick(View view) {
                SetFrag.this.m911lambda$initSettingView$1$comvideoplayervideoxfragmentSetFrag(view);
            }
        });
        this.binding.llFeedback.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.SetFrag$$ExternalSyntheticLambda3
            @Override 
            public void onClick(View view) {
                SetFrag.this.m912lambda$initSettingView$2$comvideoplayervideoxfragmentSetFrag(view);
            }
        });
        this.binding.llRate.setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.fragment.SetFrag$$ExternalSyntheticLambda4
            @Override 
            public void onClick(View view) {
                SetFrag.this.m913lambda$initSettingView$3$comvideoplayervideoxfragmentSetFrag(view);
            }
        });
    }

    /* renamed from: lambda$initSettingView$0$com-videoplayer-videox-fragment-SetFrag, reason: not valid java name */
    void m910lambda$initSettingView$0$comvideoplayervideoxfragmentSetFrag(View view) {
        onPrivacyClick();
    }

    /* renamed from: lambda$initSettingView$1$com-videoplayer-videox-fragment-SetFrag, reason: not valid java name */
    void m911lambda$initSettingView$1$comvideoplayervideoxfragmentSetFrag(View view) {
        onSettingClick();
    }

    /* renamed from: lambda$initSettingView$2$com-videoplayer-videox-fragment-SetFrag, reason: not valid java name */
    void m912lambda$initSettingView$2$comvideoplayervideoxfragmentSetFrag(View view) {
        onFeedbackClick();
    }

    /* renamed from: lambda$initSettingView$3$com-videoplayer-videox-fragment-SetFrag, reason: not valid java name */
    void m913lambda$initSettingView$3$comvideoplayervideoxfragmentSetFrag(View view) {
        onRateClick();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onPrivacyClick() {
        Intent intent = new Intent(this.mContext, PrivPoliActivity.class);
        intent.putExtra("ABOUT", false);
        intent.addFlags(65536);
        this.mContext.startActivity(intent);
        AdmobAdsHelper.ShowFullAds(this.mContext, false);
    }

    public void onSettingClick() {
        Intent intent = new Intent(this.mContext, SetActivity.class);
        intent.addFlags(65536);
        this.mContext.startActivity(intent);
        AdmobAdsHelper.ShowFullAds(this.mContext, false);
    }

    public void onFeedbackClick() {
        Intent intent = new Intent(this.mContext, ContActivity.class);
        intent.addFlags(65536);
        this.mContext.startActivity(intent);
        AdmobAdsHelper.ShowFullAds(this.mContext, false);
    }

    public void onRateClick() {
        AdmobAdsHelper.showAdsNumberCount++;
        new RatDiaBuil(requireActivity(), new RatDiaBuil.Callback() { // from class: com.videoplayer.videox.fragment.SetFrag$$ExternalSyntheticLambda0
            @Override // com.videoplayer.videox.dialog.RatDiaBuil.Callback
            public void onDialogDismiss() {
                SetFrag.lambda$onRateClick$4();
            }
        }).build().show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
