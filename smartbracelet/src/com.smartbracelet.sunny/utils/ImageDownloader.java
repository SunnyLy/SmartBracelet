package com.smartbracelet.sunny.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

/**
 * @author ernst
 * @date 2015/8/6  9:46
 */
public class ImageDownloader {

    private RequestQueue mQueue;

    private static ImageDownloader mInstance;

    private Context mContext;

    private ImageDownloader(Context context) {
        mContext = context;
        mQueue = Volley.newRequestQueue(context);
    }

    public static ImageDownloader getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ImageDownloader.class) {
                if (mInstance == null) {
                    mInstance = new ImageDownloader(context);
                }
            }
        }
        return mInstance;
    }


    public void download(String imgUrl, final OnDownloadCompleteListener listener) {
        int screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        final ImageRequest imgReq = new ImageRequest(imgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response, int id) {
                        if (listener != null) listener.onDownloadSuccess(response);
                    }
                },
                screenWidth, screenHeight, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error, int id) {
                if (listener != null) listener.onDownloadFail();
            }
        }, -1);
        mQueue.add(imgReq);
        mQueue.start();
    }


    public interface OnDownloadCompleteListener {

        void onDownloadSuccess(Bitmap bmp);

        void onDownloadFail();
    }

}
