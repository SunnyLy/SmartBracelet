package com.het.comres.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by galislsm on 2015/4/8.
 */
public class AnimBall extends View {

    private static final float HEIGHT_WIDTH_RADIO = 1.4f;
    private Paint mPaint;
    private RectF mDrawRectF;
    private int mWidth;
    private int mRadius;
    private int mDeltaOfHeightWidth;

    public AnimBall(Context context) {
        super(context);
        init();
    }


    public AnimBall(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mDrawRectF = new RectF(0, 0, 0, 0);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setBallRadius(int radius) {
        mRadius = radius;
        mDeltaOfHeightWidth = (int) (radius * (HEIGHT_WIDTH_RADIO - 1)) * 2;
        mDrawRectF.set(0, 0, 2 * mRadius, 2 * mRadius);
        mWidth = 2 * mRadius;
    }

    public void setAnimFactor(float factorOfScrollX) {
        caculateWidth(getActualFactor(factorOfScrollX));
        refreshRectFOfDraw();
    }

    public void setBallColor(int color) {
        mPaint.setColor(color);
    }

    private void refreshRectFOfDraw() {
        mDrawRectF.set(0, 0, mWidth, 2 * mRadius);
    }

    private void caculateWidth(float curF) {
        mWidth = (int) (2 * mRadius + mDeltaOfHeightWidth * curF);
    }

    private float getActualFactor(float f) {
        return f < 0.5f ? 2 * f : 1 - 2 * (f - 0.5f);
    }

    public int getActualHeight() {
        return 2 * mRadius;
    }

    public int getActualWidth() {
        return mWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(mDrawRectF, mPaint);
    }
}
