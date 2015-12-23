package com.smartbracelet.sunny.ui.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.het.common.utils.LogUtils;
import com.smartbracelet.sunny.R;

/**
 * Created by sunny on 2015/11/16.
 * Annotion:刻度盘2
 */
public class DivisionCircle2 extends View {

    private Context mContext;
    private static final int BG_COLOR_DEF = 0xFFFF6F3B;
    private static final int RADIUS_DEF = 150;

    private int mHorizonPadding = 50;
    private int mVerticalPadding = 30;
    private int mViewWidth;
    private int mViewHeight;


    private int radius;
    private int divided = 90;
    private int agle = 2;
    private int circleX = 200;
    private int circleY = 200;

    private int mCurrentTimes = 0;//当前绘制次数
    private int mTotalTimes = 10;//总共绘制次数
    private int mUnitScale = 10;//刻度之间的间距

    private int mCurrentValue;//值
    private int mMinValue;//最小值
    private int mMaxValue;//最大值
    private int mBgColor;
    private String time;//测量时间

    private Paint mValuePaint;
    private Paint mTagPaint;
    private Paint mBigLineBottomPaint;
    private Paint mBigLineTopPaint;
    private Paint mScaleBigPaint;
    private Paint mScaleSmallPaint;
    private Paint mNumPaint;
    private Paint mBigTextPaint;

    public void setCurrentValue(int mValue) {
        this.mCurrentValue = mValue;
        invalidate();
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setMinValue(int mMinValue) {
        this.mMinValue = mMinValue;
    }

    public void setMaxValue(int mMaxValue) {
        this.mMaxValue = mMaxValue;
    }

    public DivisionCircle2(Context context) {
        this(context, null);
    }

    public DivisionCircle2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DivisionCircle2);
        mMinValue = typedArray.getInt(R.styleable.DivisionCircle2_min_value, 0);
        mMaxValue = typedArray.getInt(R.styleable.DivisionCircle2_max_value, 150);
        mBgColor = typedArray.getColor(R.styleable.DivisionCircle2_bg_color, BG_COLOR_DEF);
        radius = typedArray.getInt(R.styleable.DivisionCircle2_radius, RADIUS_DEF);
        typedArray.recycle();

        mHorizonPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mHorizonPadding,getResources().getDisplayMetrics());
        mVerticalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mVerticalPadding,getResources().getDisplayMetrics());

        initView();

    }

    private void initView() {

        mValuePaint = new Paint();
        mValuePaint.setColor(Color.WHITE);
        mValuePaint.setStyle(Paint.Style.STROKE);
        mValuePaint.setStrokeWidth(2);
        mValuePaint.setAntiAlias(true);

        mTagPaint = new Paint();
        mTagPaint.setStyle(Paint.Style.FILL);
        mTagPaint.setColor(Color.RED);
        mTagPaint.setTextSize(30);

        mBigLineBottomPaint = new Paint();
        mBigLineBottomPaint.setStyle(Paint.Style.STROKE);
        mBigLineBottomPaint.setStrokeWidth(20);
        mBigLineBottomPaint.setColor(0xFFE95726);
        mBigLineBottomPaint.setAntiAlias(true);

        mBigLineTopPaint = new Paint();
        mBigLineTopPaint.setStyle(Paint.Style.STROKE);
        mBigLineTopPaint.setStrokeWidth(20);
        mBigLineTopPaint.setColor(Color.WHITE);
        mBigLineTopPaint.setAntiAlias(true);

        mScaleBigPaint = new Paint();
        mScaleBigPaint.setStyle(Paint.Style.FILL);
        mScaleBigPaint.setColor(Color.WHITE);
        mScaleBigPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mScaleBigPaint.setAntiAlias(true);
        mScaleBigPaint.setTextSize(30);

        mScaleSmallPaint = new Paint();
        mScaleSmallPaint.setStyle(Paint.Style.FILL);
        mScaleSmallPaint.setColor(Color.WHITE);
        mScaleSmallPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mScaleSmallPaint.setAntiAlias(true);
        mScaleSmallPaint.setTextSize(15);

        mNumPaint = new Paint();
        mNumPaint.setStyle(Paint.Style.FILL);
        mNumPaint.setColor(Color.WHITE);
        mNumPaint.setAntiAlias(true);
        mNumPaint.setTextSize(30);

        mBigTextPaint = new Paint();
        mBigTextPaint.setStyle(Paint.Style.FILL);
        mBigTextPaint.setColor(Color.WHITE);
        mBigTextPaint.setAntiAlias(true);
        mBigTextPaint.setTextSize(50);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCurrentTimes++;
        canvas.drawColor(0xFFFF6F3B);
        // canvas.drawText("刻度盘2", 10, 100, mTagPaint);

        //控件宽度
        mViewWidth = getWidth() - 2*mHorizonPadding;
        mViewHeight = getHeight()+mVerticalPadding;

        int px = getWidth()/2;
        int py = (mVerticalPadding+mViewWidth/2+20+50);



        //画最外的半圆
        RectF rectF = new RectF(mHorizonPadding, mVerticalPadding, mHorizonPadding+mViewWidth, mVerticalPadding+mViewHeight);
        canvas.drawArc(rectF, 180, 180, false, mValuePaint);

        int blodCircleHorizonPadding = (int) (rectF.left + 30);
        int blodCircleVerticalPadding = (int) (rectF.top + 30);
        int boldCircleWith = getWidth()-2*blodCircleHorizonPadding;
        int boldCircleHeight = getHeight() +blodCircleHorizonPadding;

        //画底部粗线条的半圆
        //最后要减去画笔的宽度
        RectF bigRecFBottom = new RectF(blodCircleHorizonPadding, blodCircleVerticalPadding,
                blodCircleHorizonPadding+boldCircleWith,rectF.bottom-20);
        canvas.drawArc(bigRecFBottom, 180, 180, false, mBigLineBottomPaint);

        //画上面的白色粗线条半圆
        RectF bigRecFTop = new RectF(blodCircleHorizonPadding, blodCircleVerticalPadding,
                blodCircleHorizonPadding+boldCircleWith,
                rectF.bottom-20);
        //把扫射的角度按100次来平分，即得出每次绘制所要扫射的角度
        canvas.drawArc(bigRecFTop, 180, (((mCurrentValue * 180) / mMaxValue) / mTotalTimes * mCurrentTimes), false, mBigLineTopPaint);

        if (mCurrentTimes < mTotalTimes) {
            postInvalidate();//只要当前绘制次数少于总共次数，则重绘
        }

        canvas.save();
        //再画刻度
        int indicateLeft = (int) (bigRecFTop.left + 60);
        int indicateTop = (int) (bigRecFTop.top + 60);
        int indicateWidth = getWidth() - 2*indicateLeft;
        RectF scaleRectF = new RectF(indicateLeft,
                indicateTop,
                indicateLeft+indicateWidth,
                rectF.bottom-40);
        // canvas.drawArc(scaleRectF, 180, 180, false, mValuePaint);
        //弧长s=(n/180)*3.14*r
        int scaleLength = (int) ((Math.PI * ((indicateWidth/ 2))));
        int devided = scaleLength / mUnitScale+scaleLength%mUnitScale;
        Path path = new Path();
        path.addArc(scaleRectF, 180, 180);
        int startX = 0;
        int vOffset = 0;
        for (int i = 1; i < devided + 1; i++) {
            if (i % 2 == 0) {
                canvas.drawTextOnPath("|", path, startX, 0, mScaleSmallPaint);
            } else {
                canvas.drawTextOnPath("|", path, startX, 0, mScaleBigPaint);
            }
            startX += mUnitScale;
        }
        //画最后一个
        canvas.drawTextOnPath("|", path, startX, 0, mScaleSmallPaint);
        canvas.save();

        //画左右2个坐标值
        canvas.drawText(mMinValue + "", mHorizonPadding, mVerticalPadding+mViewWidth/2+20+50, mNumPaint);
        canvas.drawText(mMaxValue + "", mHorizonPadding+boldCircleWith,  mVerticalPadding+mViewWidth/2+20+50, mNumPaint);
        canvas.save();

        //再画文字
        canvas.drawText(TextUtils.isEmpty(time) ? "时间:" : "时间：" + time, px-(mViewWidth/8), py, mNumPaint);
        canvas.drawText("次/分", px-(mViewWidth/8), py-50, mBigTextPaint);
        canvas.drawText(mCurrentValue / mTotalTimes * mCurrentTimes + "", px-20, py-100, mBigTextPaint);


    }
}
