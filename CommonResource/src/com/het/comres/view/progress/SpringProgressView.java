package com.het.comres.view.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class SpringProgressView extends View {
    /**
     * 进度条最大值
     */
    private float maxCount = 100;
    /**
     * 进度条当前值
     */
    private float currentCount;
    /**
     * 画笔
     */
    private Paint mPaint;
    private int mWidth, mHeight;
    private int currentColor;
    private int bgColor;

    public SpringProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    public SpringProgressView(Context context) {
        super(context);
        mPaint = new Paint();
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAntiAlias(true);
        int round = mHeight / 2;

        mPaint.setColor(Color.rgb(71, 76, 80));
        RectF rectBg = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectBg, round, round, mPaint);
        // 底部颜色值
        mPaint.setColor(bgColor);
        RectF rectBlackBg = new RectF(2, 2, mWidth - 2, mHeight - 2);// - 2
        canvas.drawRoundRect(rectBlackBg, round, round, mPaint);

        float section = currentCount / maxCount;
        // mPaint.setColor(currentColor);
        RectF rectProgressBg = new RectF(3, 3, (mWidth - 3) * section,
                mHeight - 3);// -3

        LinearGradient shader = new LinearGradient(3, 3,
                (mWidth - 3) * section, mHeight - 3, new int[]{bgColor,
                bgColor}, null, Shader.TileMode.MIRROR);
        mPaint.setShader(shader);
        canvas.drawRoundRect(rectProgressBg, round, round, mPaint);
    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /***
     * 设置最大的进度值
     *
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    public float getMaxCount() {
        return maxCount;
    }

    /***
     * 设置当前的进度值
     *
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        invalidate();
    }

    public float getCurrentCount() {
        return currentCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY
                || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(15);
        } else {
            mHeight = heightSpecSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

}
