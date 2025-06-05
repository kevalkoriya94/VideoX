package com.videoplayer.videox.activity;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewException;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import com.videoplayer.videox.R;
import com.videoplayer.videox.dialog.ExiDialBuil;
import com.videoplayer.videox.dialog.QueDiaBuil;
import com.videoplayer.videox.fragment.SetFrag;
import com.videoplayer.videox.fragment.mus.MusManFrag;
import com.videoplayer.videox.fragment.vid.VidManFrag;
import com.videoplayer.videox.uti.CoUti;
import com.videoplayer.videox.uti.ads.AdmobAdsHelper;


public class HomPageActivity extends AppCompatActivity implements View.OnTouchListener {
    private BottomNavigationView bottomNavigationView;
    float dRawX;
    float dRawY;

    /* renamed from: dX */
    float f579dX;

    /* renamed from: dY */
    float f580dY;
    private FrameLayout layoutParent;
    private Dialog mRatingDialog;
    private int mScreenHeight;
    private int mScreenWidth;
    float newX;
    float newY;
    float screenBottomY;
    float screenCenterX;
    float screenCenterY;
    float screenLeftX;
    float screenRightX;
    float screenSuperBottomY;
    float screenSuperTopY;
    float screenTopY;
    long touchingTime;
    private final Fragment[] mFragment = new Fragment[3];
    private int current = 0;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), (ActivityResultCallback) obj -> {
    });
    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    String[] permissions33 = {"android.permission.READ_MEDIA_VIDEO", "android.permission.READ_MEDIA_AUDIO"};

    private AppUpdateManager appUpdateManager;


    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_home_page);
        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(new NotificationChannel(getString(R.string.default_notification_channel_id), getString(R.string.default_notification_channel_name), 2));
        }
        getFirebaseToken();
        askNotificationPermission();
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        this.mFragment[0] = VidManFrag.newInstance();
        this.mFragment[1] = MusManFrag.newInstance();
        this.mFragment[2] = SetFrag.newInstance();
        supportFragmentManager.beginTransaction().add(R.id.frame, this.mFragment[0], "video").commit();
        this.current = 0;
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        this.bottomNavigationView = bottomNavigationView;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { // from class: com.videoplayer.videox.activity.HomPageActivity$$ExternalSyntheticLambda1
            @Override // com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
            public final boolean onNavigationItemSelected(MenuItem menuItem) {
                return HomPageActivity.this.m517xd9ebee9a(supportFragmentManager, menuItem);
            }
        });
        this.bottomNavigationView.setSelectedItemId(R.id.nav_video);
        if (Build.VERSION.SDK_INT >= 33) {
            if (!StartPolicyActivity.hasStoragePermissions33(this)) {
                ActivityCompat.requestPermissions(this, this.permissions33, 1);
            }
        } else if (Build.VERSION.SDK_INT >= 26 && !StartPolicyActivity.hasStoragePermissions(this)) {
            ActivityCompat.requestPermissions(this, this.permissions, 1);
        }
        this.layoutParent = (FrameLayout) findViewById(R.id.layout_parent);
        initDefaultLocationForDragDrop();
        if (CoUti.checkTimeShowFloatingButtonGift(this)) {
            CoUti.putTimeShowFloatingButtonGift(this);
        }
        AdmobAdsHelper.smallnativeAds(this, (ViewGroup) findViewById(R.id.layout_ads), (TextView) findViewById(R.id.adspace), (NativeAdLayout) findViewById(R.id.native_banner_ad_container), 1);

        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        inAppUpdate();
    }

    private void inAppUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, 124);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    public void showSnackBar(String message) {
        Snackbar.make(findViewById(R.id.layout_parent), message, Snackbar.LENGTH_SHORT).show();
    }

    boolean m517xd9ebee9a(FragmentManager fragmentManager, MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.nav_video) {
            AdmobAdsHelper.showAdsNumberCount++;
            fragmentManager.beginTransaction().replace(R.id.frame, this.mFragment[0], "video").commit();
            fragmentManager.beginTransaction().hide(this.mFragment[this.current]).show(this.mFragment[0]).commit();
            this.current = 0;
            return true;
        }
        if (itemId == R.id.nav_music) {
            AdmobAdsHelper.showAdsNumberCount++;
            fragmentManager.beginTransaction().replace(R.id.frame, this.mFragment[1], "music").commit();
            fragmentManager.beginTransaction().hide(this.mFragment[this.current]).show(this.mFragment[1]).commit();
            this.current = 1;
            return true;
        }
        if (itemId != R.id.nav_setting) {
            return false;
        }
        AdmobAdsHelper.showAdsNumberCount++;
        fragmentManager.beginTransaction().replace(R.id.frame, this.mFragment[2], "setting").commit();
        fragmentManager.beginTransaction().hide(this.mFragment[this.current]).show(this.mFragment[2]).commit();
        this.current = 2;
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ReviewManager manager = ReviewManagerFactory.create(this);
            Task<ReviewInfo> request = manager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    ReviewInfo reviewInfo = task.getResult();
                    manager.launchReviewFlow(this, reviewInfo).addOnFailureListener(e -> {
                    }).addOnCompleteListener(complete -> {
                                Log.d("TAG", "Success");
                            }
                    ).addOnFailureListener(failure -> {
                        Log.d("TAG", "Rating Failed");
                    });
                }
            }).addOnFailureListener(failure -> Log.d("TAG", "In-App Request Failed " + failure));
            Log.d("TAG", "in app token complete, show in app review if available");
        } catch (Exception unused) {
        }
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") == 0) {
            return;
        }
        this.requestPermissionLauncher.launch("android.permission.POST_NOTIFICATIONS");
    }

    private void getFirebaseToken() {
    }


    @Override
    public void onBackPressed() {
        AdmobAdsHelper.showAdsNumberCount++;
        new ExiDialBuil(this, new ExiDialBuil.Callback() {
            @Override
            public final void onDialogDismiss() {
            }
        }).build().show();
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (Build.VERSION.SDK_INT >= 33) {
            if (StartPolicyActivity.hasStoragePermissions33(this)) {
                return;
            }
            new QueDiaBuil(this, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.activity.HomPageActivity.1
                @Override 
                public void onOkClick() {
                    AdmobAdsHelper.showAdsNumberCount++;
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.parse("package:" + HomPageActivity.this.getPackageName()));
                    HomPageActivity.this.startActivityForResult(intent, 2);
                }

                @Override 
                public void onCancelClick() {
                    AdmobAdsHelper.showAdsNumberCount++;
                    HomPageActivity.this.finish();
                }
            }).setTitle(R.string.notice, ContextCompat.getColor(this, R.color.main_orange)).setQuestion(R.string.question_request_permission).build().show();
        } else {
            if (Build.VERSION.SDK_INT < 26 || StartPolicyActivity.hasStoragePermissions(this)) {
                return;
            }
            new QueDiaBuil(this, new QueDiaBuil.OkButtonClickListener() { // from class: com.videoplayer.videox.activity.HomPageActivity.2
                @Override 
                public void onOkClick() {
                    AdmobAdsHelper.showAdsNumberCount++;
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.parse("package:" + HomPageActivity.this.getPackageName()));
                    HomPageActivity.this.startActivityForResult(intent, 2);
                }

                @Override 
                public void onCancelClick() {
                    AdmobAdsHelper.showAdsNumberCount++;
                    HomPageActivity.this.finish();
                }
            }).setTitle(R.string.notice, ContextCompat.getColor(this, R.color.main_orange)).setQuestion(R.string.question_request_permission).build().show();
        }
    }

    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 124) {
            if (i2 == RESULT_CANCELED) {
                showSnackBar(getString(R.string.msg_cancel_update));
            } else if (i2 == RESULT_OK) {
                showSnackBar(getString(R.string.msg_success_update));
            } else {
                showSnackBar(getString(R.string.msg_failed_update));
                inAppUpdate();
            }
        }
        if (i != 2) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 33) {
            if (StartPolicyActivity.hasStoragePermissions33(this)) {
                return;
            }
            ActivityCompat.requestPermissions(this, this.permissions33, 1);
        } else {
            if (Build.VERSION.SDK_INT < 26 || StartPolicyActivity.hasStoragePermissions(this)) {
                return;
            }
            ActivityCompat.requestPermissions(this, this.permissions, 1);
        }
    }

    public void updateBottomNavigation() {
        this.bottomNavigationView.setSelectedItemId(R.id.nav_music);
        Log.d("binhnk08", "updateBottomNavigation");
    }

    private void initDefaultLocationForDragDrop() {
        this.layoutParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.videoplayer.videox.activity.HomPageActivity.3
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                HomPageActivity.this.layoutParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                HomPageActivity homPageActivity = HomPageActivity.this;
                homPageActivity.mScreenHeight = homPageActivity.layoutParent.getHeight();
                HomPageActivity homPageActivity2 = HomPageActivity.this;
                homPageActivity2.mScreenWidth = homPageActivity2.layoutParent.getWidth();
                HomPageActivity homPageActivity3 = HomPageActivity.this;
                homPageActivity3.screenCenterX = homPageActivity3.layoutParent.getX() + (HomPageActivity.this.mScreenWidth / 2.0f);
                HomPageActivity homPageActivity4 = HomPageActivity.this;
                homPageActivity4.screenCenterY = homPageActivity4.layoutParent.getY() + (HomPageActivity.this.mScreenHeight / 2.0f);
                HomPageActivity homPageActivity5 = HomPageActivity.this;
                homPageActivity5.screenLeftX = homPageActivity5.layoutParent.getX() + (HomPageActivity.this.mScreenWidth / 4.0f);
                HomPageActivity homPageActivity6 = HomPageActivity.this;
                homPageActivity6.screenRightX = homPageActivity6.layoutParent.getX() + ((HomPageActivity.this.mScreenWidth / 4.0f) * 3.0f);
                HomPageActivity homPageActivity7 = HomPageActivity.this;
                homPageActivity7.screenTopY = homPageActivity7.layoutParent.getY() + ((HomPageActivity.this.mScreenHeight / 6.0f) * 2.0f);
                HomPageActivity homPageActivity8 = HomPageActivity.this;
                homPageActivity8.screenSuperTopY = homPageActivity8.layoutParent.getY() + (HomPageActivity.this.mScreenHeight / 6.0f);
                HomPageActivity homPageActivity9 = HomPageActivity.this;
                homPageActivity9.screenBottomY = homPageActivity9.layoutParent.getY() + ((HomPageActivity.this.mScreenHeight / 6.0f) * 4.0f);
                HomPageActivity homPageActivity10 = HomPageActivity.this;
                homPageActivity10.screenSuperBottomY = homPageActivity10.layoutParent.getY() + ((HomPageActivity.this.mScreenHeight / 6.0f) * 5.0f);
            }
        });
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.touchingTime = System.currentTimeMillis();
            Log.d("binhnk08", "ACTION_DOWN " + this.touchingTime);
            this.dRawX = this.f579dX - motionEvent.getRawX();
            this.dRawY = this.f580dY - motionEvent.getRawY();
        } else if (action == 1) {
            Log.d("binhnk08", "ACTION_UP " + this.touchingTime + " " + System.currentTimeMillis());
            System.currentTimeMillis();
        } else {
            if (action != 2) {
                return false;
            }
            Log.d("binhnk08", "ACTION_MOVE " + this.touchingTime);
            this.newX = motionEvent.getRawX() + this.dRawX;
            this.newY = motionEvent.getRawY() + this.dRawY;
        }
        return true;
    }

    public void translationView(View view, float f, float f2) {
        view.animate().x(f).y(f2).setDuration(0L).start();
    }
}
