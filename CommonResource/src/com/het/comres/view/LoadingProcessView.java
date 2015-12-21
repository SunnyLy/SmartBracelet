package com.het.comres.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.het.comres.R;

/**
 * 下载进度条
 * Created by galis on 2015/4/16.
 */
public class LoadingProcessView extends FrameLayout {

    private ProgressBar mProgressBar;

    public LoadingProcessView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    public void setTopMargin(int topMargin) {
        LayoutParams params = (LayoutParams) mProgressBar.getLayoutParams();
        params.topMargin = (int) (topMargin - params.height / 2.0f);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mProgressBar = (ProgressBar) findViewById(R.id.pb_progressbar);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setDownloadPercent(int percent) {
        mProgressBar.setProgress(percent);
    }

}
