package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.het.common.utils.LogUtils;
import com.het.comres.R;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * CLife自定义刷新
 * Created by Galis on 2015/6/29.
 */
public class CLifeHeaderLoadingLayout extends LoadingLayout {

    private GifImageView mGifView;
    private GifDrawable mGidDrawable;
    private int mCurFrameIndex = 0;
    private TextView mTextView;
    private LinearLayout mLinearLayout;

    public CLifeHeaderLoadingLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context);
        mMode = mode;
        mScrollDirection = scrollDirection;

        switch (scrollDirection) {
            case HORIZONTAL:
                LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_vertical_clife, this);
                break;
            case VERTICAL:
            default:
                LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_vertical_clife, this);
                break;
        }

        mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
        mLinearLayout = (LinearLayout) findViewById(R.id.fl_linearlayout);

        mGifView = new GifImageView(getContext());
        int width = (int) (50 * context.getResources().getDisplayMetrics().density);
        LayoutParams layoutParams = new LayoutParams(width, width);
        mLinearLayout.addView(mGifView, layoutParams);

        mTextView = new TextView(context);
        mTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTextView.setTextColor(Color.GRAY);
        mTextView.setTextSize(15);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) mTextView.getLayoutParams();
        layoutParams1.setMargins((int) (5 * context.getResources().getDisplayMetrics().density), 0, 0, 0);

        mLinearLayout.addView(mTextView);
        mGifView.setImageResource(R.drawable.clife_loading);
        mGidDrawable = (GifDrawable) mGifView.getDrawable();

        LayoutParams lp = (LayoutParams) mInnerLayout.getLayoutParams();

        switch (mode) {
            case PULL_FROM_END:
                lp.gravity = scrollDirection == PullToRefreshBase.Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;
                break;

            case PULL_FROM_START:
            default:
                lp.gravity = scrollDirection == PullToRefreshBase.Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;
                break;
        }

        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
            Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
            if (null != background) {
                ViewCompat.setBackground(this, background);
            }
        }

    }


    @Override
    public void hideAllViews() {
    }

    @Override
    public void onPull(float scaleOfLayout) {
        mGidDrawable.seekToFrame(0);
        mGidDrawable.pause();
        LogUtils.d("GifView", "OnPull:  " + scaleOfLayout);

    }

    @Override
    public void pullToRefresh() {
        mGidDrawable.seekToFrame(1);
        mGidDrawable.pause();
        mTextView.setText("下拉加载更多..");
        LogUtils.d("GifView", " pullToRefresh");
    }

    @Override
    public void refreshing() {
        mGidDrawable.start();
        mTextView.setText("努力加载中..  ");
        LogUtils.d("GifView", " refreshing");
    }

    @Override
    public void releaseToRefresh() {
        mTextView.setText("松手马上加载..");
        LogUtils.d("GifView", " releaseToRefresh");
    }


    @Override
    public void setLastUpdatedLabel(CharSequence label) {
    }


    @Override
    public void setPullLabel(CharSequence pullLabel) {
    }

    @Override
    public void setRefreshingLabel(CharSequence refreshingLabel) {
    }

    @Override
    public void setReleaseLabel(CharSequence releaseLabel) {
    }

    @Override
    public void setTextTypeface(Typeface tf) {
    }

    @Override
    protected int getDefaultDrawableResId() {
        return 0;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {

    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {

    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {

    }

    @Override
    public void reset() {
    }
}
