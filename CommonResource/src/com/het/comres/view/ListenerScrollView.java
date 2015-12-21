package com.het.comres.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

@SuppressLint("NewApi")
public class ListenerScrollView extends ScrollView {
    private View mTrackedChild;

    private ScrollViewListener mScrollViewListener = null;

    public ScrollViewListener getmScrollViewListener() {
        return mScrollViewListener;
    }

    public void setScrollListener(ScrollViewListener mScrollViewListener) {
        this.mScrollViewListener = mScrollViewListener;
    }


    public ListenerScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ListenerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
            this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }

    public ListenerScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mScrollViewListener != null) {
            mScrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public interface ScrollViewListener {
        void onScrollChanged(ListenerScrollView scrollView, int x, int y, int oldx, int oldy);
    }

}
