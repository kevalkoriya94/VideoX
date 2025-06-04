package com.videoplayer.videox.cv;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;


public class NpaGridLayManager extends GridLayoutManager {
    @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public NpaGridLayManager(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public NpaGridLayManager(Context context, int i) {
        super(context, i);
    }

    public NpaGridLayManager(Context context, int i, int i2, boolean z) {
        super(context, i, i2, z);
    }
}
