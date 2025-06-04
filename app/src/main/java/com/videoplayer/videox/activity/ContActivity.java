package com.videoplayer.videox.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.videoplayer.videox.R;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;


public class ContActivity extends AppCompatActivity {
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        final EditText editText = (EditText) findViewById(R.id.TextTitle);
        final EditText editText2 = (EditText) findViewById(R.id.TextDescription);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.ContActivity$$ExternalSyntheticLambda0
            @Override
            public final void onClick(View view) {
                ContActivity.this.m855lambda$onCreate$0$comvideoplayervideoxactivityContActivity(editText, editText2, view);
            }
        });
        findViewById(R.id.exitEditMode).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.activity.ContActivity$$ExternalSyntheticLambda1
            @Override
            public final void onClick(View view) {
                ContActivity.this.m856lambda$onCreate$1$comvideoplayervideoxactivityContActivity(view);
            }
        });
        AdmobAdsHelper.bannerAds(this, (ViewGroup) findViewById(R.id.layout_ads), findViewById(R.id.adspace));
    }

    /* renamed from: lambda$onCreate$0$com-videoplayer-videox-activity-ContActivity, reason: not valid java name */
    void m855lambda$onCreate$0$comvideoplayervideoxactivityContActivity(EditText editText, EditText editText2, View view) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError("Please enter subject");
            return;
        }
        if (TextUtils.isEmpty(editText2.getText().toString())) {
            editText2.setError("Please enter description");
            return;
        }
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("message/rfc822");
        intent.putExtra("android.intent.extra.EMAIL", new String[]{"lunaiapps52@gmail.com"});
        intent.putExtra("android.intent.extra.SUBJECT", editText.getText().toString());
        intent.putExtra("android.intent.extra.TEXT", editText2.getText().toString());
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    /* renamed from: lambda$onCreate$1$com-videoplayer-videox-activity-ContActivity, reason: not valid java name */
    void m856lambda$onCreate$1$comvideoplayervideoxactivityContActivity(View view) {
        onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        AdmobAdsHelper.ShowFullAds(this, true);
        finish();
    }
}
