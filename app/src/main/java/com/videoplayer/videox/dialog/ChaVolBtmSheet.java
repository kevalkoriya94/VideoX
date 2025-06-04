package com.videoplayer.videox.dialog;

import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.videoplayer.videox.R;


public class ChaVolBtmSheet extends BottomSheetDialogFragment {
    private AudioManager audioManager;

    @Override // androidx.fragment.app.DialogFragment
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.change_volume_bottom_sheet, viewGroup, false);
        this.audioManager = (AudioManager) getContext().getSystemService("audio");
        SeekBar seekBar = (SeekBar) inflate.findViewById(R.id.seekBar_volume);
        seekBar.setMax(this.audioManager.getStreamMaxVolume(3));
        seekBar.setProgress(this.audioManager.getStreamVolume(3));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: com.videoplayer.videox.dialog.ChaVolBtmSheet.1
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar2) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar2, int i, boolean z) {
                ChaVolBtmSheet.this.audioManager.setStreamVolume(3, i, 0);
            }
        });
        return inflate;
    }
}
