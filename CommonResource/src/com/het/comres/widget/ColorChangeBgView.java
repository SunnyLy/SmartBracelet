package com.het.comres.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by galislsm on 2015/4/8.
 */
public class ColorChangeBgView extends View {

    private int mRadius;//圆的半径
    private int mLineLength;//直线的距离
    private Paint mPaint;
    private Path mPath;
    private RectF mLeftCircleRectF;//左边半圆
    private RectF mRightCircleRectF;//右边半圆
    private int mBgWith;
    private boolean mIsFirstMeasure = true;


    //颜色变量
    private int mStartColor;
    private int mStopColor;
    private int mCurBgColor;

    private int mStartColorRed;
    private int mStartColorGreen;
    private int mStartColorBlue;

    private int mStopColorRed;
    private int mStopColorGreen;
    private int mStopColorBlue;

    private int mDeltaColorRed;
    private int mDeltaColorGreen;
    private int mDeltaColorBlue;


    public ColorChangeBgView(Context context) {
        super(context);
        init();

    }

    public ColorChangeBgView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPath = new Path();


        mLeftCircleRectF = new RectF(0, 0, 0, 0);
        mRightCircleRectF = new RectF(0, 0, 0, 0);
    }

    public void initColor(int startColor, int stopColor) {
        mStartColor = startColor;
        mStopColor = stopColor;
        mCurBgColor = mStartColor;

        mStartColorRed = (mStartColor >> 16) & 0xff;
        mStartColorGreen = (mStartColor >> 8) & 0xff;
        mStartColorBlue = mStartColor & 0xff;

        mStopColorRed = (mStopColor >> 16) & 0xff;
        mStopColorGreen = (mStopColor >> 8) & 0xff;
        mStopColorBlue = mStopColor & 0xff;

        mDeltaColorRed = mStopColorRed - mStartColorRed;
        mDeltaColorGreen = mStopColorGreen - mStartColorGreen;
        mDeltaColorBlue = mStopColorBlue - mStartColorBlue;

        mPaint.setColor(mStartColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mIsFirstMeasure) {
            mIsFirstMeasure = false;
            mBgWith = MeasureSpec.getSize(widthMeasureSpec);
            mRadius = (int) (MeasureSpec.getSize(heightMeasureSpec) / 2.0f);
            mLineLength = mBgWith - 2 * mRadius;

            mLeftCircleRectF.set(0, 0, 2 * mRadius, 2 * mRadius);
            mRightCircleRectF.set(mBgWith - 2 * mRadius, 0, mBgWith, 2 * mRadius);

            mPath.reset();
            mPath.moveTo(mRadius, 0);
            mPath.lineTo(mRadius + mLineLength, 0);
            mPath.arcTo(mRightCircleRectF, 270, 180);
            mPath.lineTo(mRadius, 2 * mRadius);
            mPath.arcTo(mLeftCircleRectF, 90, 180);
        }
        Log.e("Measure", mBgWith + "");
        Log.e("mRadius", mRadius + "");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    public void setColorFlactor(float f) {
        int curRed = (int) (mStartColorRed + mDeltaColorRed * f);
        int curGreen = (int) (mStartColorGreen + mDeltaColorGreen * f);
        int curBlue = (int) (mStartColorBlue + mDeltaColorBlue * f);
        mCurBgColor = Color.argb(0xFF, curRed, curGreen, curBlue);
        mPaint.setColor(mCurBgColor);
        invalidate();

    }

}
