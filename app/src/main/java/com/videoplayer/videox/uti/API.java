package com.videoplayer.videox.uti;

import android.app.Activity;
import android.util.Base64;


public class API {
    public API(Activity activity) {
    }

    public static String toBase64(String input) {
        return new String(Base64.encode(input.getBytes(), 0));
    }
}
