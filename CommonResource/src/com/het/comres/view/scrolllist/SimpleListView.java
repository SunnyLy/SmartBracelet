package com.het.comres.view.scrolllist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class SimpleListView extends ListView {

    public SimpleListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SimpleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleListView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
