package com.smartbracelet.sunny.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.het.common.utils.TimeUtils;

/**
 * Created by sunny on 2015/11/13.
 * Annotion:刻度盘
 */
public class DivisionCircle extends View {
    private Context mContext;

    private static final int RADIUS_DEF = 200;
    private static final int CIRCLE_X = 300;
    private static final int CIRCLE_Y = 300;
    private static final int UNIT_ANGLE_DEF = 9;

    private Paint mTextPaint;
    private Paint mTextPaint2;
    private Paint mSmallPaint;
    private Paint mCirclePaint;//画圆
    private Paint mSmallCirclePaint;//圆中的小圆
    private Paint mTagPaint;//当前值记录画笔
    private Paint mBigTextPaint;//大数字画笔
    private Paint mSmallTextPaint;//小数字画笔

    private int mRadius = RADIUS_DEF;
    private int circleX = CIRCLE_X;
    private int circleY = CIRCLE_Y;
    private int unitAngle = UNIT_ANGLE_DEF;
    private int mAgle;
    private int mValue;
    private String mTime;

    public void setmValue(int mValue) {
        this.mValue = mValue;
        this.mAgle = mValue / 3 == 0 ? ((mValue / 3) * unitAngle) : ((mValue / 3) * unitAngle + (mValue % 3) * (unitAngle / 3));
    }

    public void setmRadius(int mRadius) {
        this.mRadius = mRadius;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public void setCircleX(int circleX) {
        this.circleX = circleX;
    }

    public void setCircleY(int circleY) {
        this.circleY = circleY;
    }

    public void setUnitAngle(int unitAngle) {
        this.unitAngle = unitAngle;
    }

    public DivisionCircle(Context context) {
        this(context, null);
    }

    public DivisionCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;


        initPaint();
    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.TRANSPARENT);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setStrokeWidth(10);
        mTextPaint.setAntiAlias(true);

        mTextPaint2 = new Paint();
        mTextPaint2.setColor(Color.WHITE);
        mTextPaint2.setAntiAlias(true);
        mTextPaint2.setStyle(Paint.Style.FILL);
        mTextPaint2.setTextSize(30);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(0xFF57E4AB);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mSmallCirclePaint = new Paint();
        mSmallCirclePaint.setColor(0xFF88FFCF);
        mSmallCirclePaint.setAntiAlias(true);
        mSmallCirclePaint.setStyle(Paint.Style.FILL);

        mSmallPaint = new Paint();
        mSmallPaint.setColor(Color.WHITE);
        mSmallPaint.setAntiAlias(true);
        mSmallPaint.setStyle(Paint.Style.FILL);
        mSmallPaint.setTextSize(10);

        mTagPaint = new Paint();
        mTagPaint.setColor(Color.WHITE);
        mTagPaint.setAntiAlias(true);
        mTagPaint.setStyle(Paint.Style.FILL);
        mTagPaint.setTextSize(30);

        mBigTextPaint = new Paint();
        mBigTextPaint.setTextSize(20);
        mBigTextPaint.setColor(Color.WHITE);
        mBigTextPaint.setStyle(Paint.Style.FILL);

        mSmallTextPaint = new Paint();
        mSmallTextPaint.setTextSize(15);
        mSmallTextPaint.setColor(Color.WHITE);
        mSmallTextPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            width = widthMeasureSpec;
        } else if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        }
        return width;
    }

    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = heightMeasureSpec;
        }
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFA9475E);

        RectF rectF = new RectF();
        rectF.left = 100;
        rectF.top = 100;
        rectF.right = rectF.left + 200 * 2;
        rectF.bottom = rectF.top + 200 * 2;
        /**
         * 画扇形，startAngle:扇形起始点角度
         *  sweepAngle:扇形扫过的角度
         *  注：是以水平为0度，开始，顺时针画
         */
        canvas.drawArc(rectF, 135, 225, false, mTextPaint);
        Path path = new Path();
        path.addArc(rectF, 135, 225);
        //弧长
        int s = (int) (225 * Math.PI * 250 / 180);
        int space = (s / 24);
        int smallSpace = space / 3;//每一份又分成三小份
        int start = 10;
        int smallStart = 0;//每一小份的开始
        canvas.save();
        canvas.restore();
        for (int i = 0; i < 24; i++) {
            //画刻度值
            //hOffset:画文字的起始位置
            //vOffset:<0,在路径上方，>0在路径下方
            if (i + 10 > 29) {
                break;
            }
            if (((i + 1) % 2) != 0) {
                canvas.drawTextOnPath((i + 10) + "", path, start, 30, mBigTextPaint);
            } else {
                canvas.drawTextOnPath((i + 10) + "", path, start, 30, mSmallTextPaint);
            }


            start += space;
            //画刻度
            // canvas.drawTextOnPath("|", path, start, 0, mTextPaint2);
            for (int j = 0; j < 3; j++) {
                //画小刻度
                if (j == 2) {
                    canvas.drawTextOnPath("|", path, smallStart, 0, mTextPaint2);
                } else if ((i + 10) > 10) {
                    canvas.drawTextOnPath("|", path, smallStart, 0, mSmallPaint);
                }
                smallStart += smallSpace;
            }

        }


        //canvas.restore();//restore()就是保存已经画好的，重新开始一块新的画布
        //再画刻度盘中间的圆
        canvas.drawCircle(300, 300, 100, mCirclePaint);

        //再画圆中的小圆
        canvas.drawCircle(300, 300, 20, mSmallCirclePaint);
        //画指针
        Point pointStart = getPoint(mAgle, unitAngle, mRadius, circleX, circleY);
        canvas.drawLine(pointStart.x, pointStart.y, 300, 300, mSmallCirclePaint);

        //画文字
        canvas.drawText(mValue + "次/分", 270, 450, mTagPaint);
        canvas.drawText(TextUtils.isEmpty(mTime) ? "" : mTime, 270, 480, mTagPaint);

    }


    /**
     * 圆上任意一点坐标计算
     * <p/>
     * 因为圆弧所扫过的角度是已知的，
     * 最小值，最大值都是已知的，
     * 则可以得知圆弧每一大份所扫过的角度，
     * 从而可以根据这个角度，来计算这个点在圆上的坐标
     *
     * @param unitAngle    每一大份所占的角度
     * @param angle        测量值在圆上所占的角度
     * @param circleRadius 圆半径
     * @param circleX      圆心X坐标
     * @param circleY      圆心Y坐标
     */
    private Point getPoint(float angle, int unitAngle, int circleRadius, int circleX, int circleY) {
        Point point = new Point();
        //1:如果值所扫过的角度在0~45度间，则在第三象限
        //2:如果所扫过的角度在45-90度间，则在第二象限
        //3:如果所扫过的角度在90-180度间，则在第一象限
        if (angle >= 0 && angle <= 45) {
            point.x = (int) (circleX - Math.abs(circleRadius * Math.cos(45 - angle)));
            point.y = (int) (circleY + Math.abs(circleRadius * Math.sin(45 - angle)));
        } else if (angle > 45 && angle <= 90) {
            point.x = (int) (circleX - Math.abs(circleRadius * Math.cos((angle - 45))));
            point.y = (int) (circleY - Math.abs(circleRadius * Math.sin((angle - 45))));
        } else if (angle > 90 && angle <= 180) {
            point.x = (int) (circleX + Math.abs(circleRadius * Math.sin((angle - 135))));
            point.y = (int) (circleY - Math.abs(circleRadius * Math.cos((angle - 135))));
        }
        return point;
    }
}
