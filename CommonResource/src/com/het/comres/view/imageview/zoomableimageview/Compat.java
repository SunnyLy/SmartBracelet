package com.het.comres.view.imageview.zoomableimageview;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;

@TargetApi(VERSION_CODES.JELLY_BEAN)
public class Compat {

    private static final int SIXTY_FPS_INTERVAL = 1000 / 60;

    public static void postOnAnimation(View view, Runnable runnable) {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            SDK16.postOnAnimation(view, runnable);
        } else {
            view.postDelayed(runnable, SIXTY_FPS_INTERVAL);
        }
    }

    public static class SDK16 {

        public static void postOnAnimation(View view, Runnable r) {
            view.postOnAnimation(r);
        }

    }

}
