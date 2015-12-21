package com.het.comres.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * CustomGriView 在ScrollView中也能自动测量自己的高度
 * Created by Galis on 2015/7/10.
 */
public class CustomGridView extends GridView {
    public CustomGridView(Context context) {
        super(context);
    }

    public CustomGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
