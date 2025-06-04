package com.videoplayer.videox.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.audiofx.Equalizer;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.Toast;

import com.videoplayer.videox.R;
import com.videoplayer.videox.db.utils.SettingPrefUtils;
import com.videoplayer.videox.uti.cons.AppCon;

import java.util.Objects;


public class VidPlayEquDiaBuil {
    public static boolean isFirstLoad = false;
    public static final int[] seekbarPos = new int[5];
    private final Dialog dialogEqualizer;
    private Equalizer mEqualizer;
    private float[] points;

    public VidPlayEquDiaBuil(Context context, int i) {
        Dialog dialog = new Dialog(context, AppCon.Themes.THEMES_STYLE[new SettingPrefUtils(context).getThemes()]);
        this.dialogEqualizer = dialog;
        dialog.requestWindowFeature(1);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window);
        window.setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.dialog_video_player_equalizer);
        try {
            this.mEqualizer = new Equalizer(0, i);
            dialog.findViewById(R.id.view_click).setOnClickListener(new View.OnClickListener() { // from class: com.videoplayer.videox.dialog.VidPlayEquDiaBuil$$ExternalSyntheticLambda0
                @Override 
                public void onClick(View view) {
                    VidPlayEquDiaBuil.this.m902lambda$new$0$comvideoplayervideoxdialogVidPlayEquDiaBuil(view);
                }
            });
            this.mEqualizer.setEnabled(true);
            this.points = new float[5];
            final short s = this.mEqualizer.getBandLevelRange()[0];
            short s2 = this.mEqualizer.getBandLevelRange()[1];
            for (short s3 = 0; s3 < 5; s3 = (short) (s3 + 1)) {
                SeekBar seekBar = new SeekBar(context);
                if (s3 == 0) {
                    seekBar = this.dialogEqualizer.findViewById(R.id.seekBar1);
                } else if (s3 == 1) {
                    seekBar = this.dialogEqualizer.findViewById(R.id.seekBar2);
                } else if (s3 == 2) {
                    seekBar = this.dialogEqualizer.findViewById(R.id.seekBar3);
                } else if (s3 == 3) {
                    seekBar = this.dialogEqualizer.findViewById(R.id.seekBar4);
                } else if (s3 == 4) {
                    seekBar = this.dialogEqualizer.findViewById(R.id.seekBar5);
                }
                SeekBar[] seekBarFinal = new SeekBar[5];
                seekBarFinal[s3] = seekBar;
                seekBar.setId(s3);
                seekBar.setMax(s2 - s);
                if (isFirstLoad) {
                    this.points[s3] = this.mEqualizer.getBandLevel(s3) - s;
                    seekBar.setProgress(this.mEqualizer.getBandLevel(s3) - s);
                    seekbarPos[s3] = this.mEqualizer.getBandLevel(s3);
                    isFirstLoad = false;
                } else {
                    float[] fArr = this.points;
                    int i2 = seekbarPos[s3];
                    fArr[s3] = i2 - s;
                    seekBar.setProgress(i2 - s);
                }
                short finalS = s3;
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar2) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar2) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar2, int i2, boolean z) {
                        mEqualizer.setBandLevel(finalS, (short) (s + i2));
                        points[seekBar2.getId()] = mEqualizer.getBandLevel(finalS) - s;
                        seekbarPos[seekBar2.getId()] = i2 + s;
                    }
                });
            }
        } catch (Exception unused) {
            Toast.makeText(context, R.string.device_not_supported, Toast.LENGTH_SHORT).show();
            this.dialogEqualizer.dismiss();
        }
    }

    /* renamed from: lambda$new$0$com-videoplayer-videox-dialog-VidPlayEquDiaBuil, reason: not valid java name */
    void m902lambda$new$0$comvideoplayervideoxdialogVidPlayEquDiaBuil(View view) {
        this.dialogEqualizer.dismiss();
    }

    public Dialog build() {
        return this.dialogEqualizer;
    }
}
