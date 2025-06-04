package com.videoplayer.videox.dialog;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.videoplayer.videox.R;
import com.videoplayer.videox.adapter.BotMenAdapter;


public class BtmMenDialBuil {
    final BottomSheetDialog mDialog;
    final RecyclerView mRvContent;

    public BtmMenDialBuil(Context context, BotMenAdapter bottomMenuAdapter) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.CustomDialog);
        this.mDialog = bottomSheetDialog;
        bottomSheetDialog.setContentView(R.layout.custom_bottom_dialog);
        RecyclerView recyclerView = (RecyclerView) bottomSheetDialog.findViewById(R.id.rv_content_dialog);
        this.mRvContent = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(bottomMenuAdapter);
    }

    public BottomSheetDialog build() {
        return this.mDialog;
    }
}
