package com.smartbracelet.sunny.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by sunny on 2015/12/23.
 * 练手，写一个罗盘的自定义控件
 */
public class CompassView extends View {

    private Paint marketPaint;//用于做标记
    private Paint textPaint;//用于画文字
    private Paint circlePaint;//画圆

    private String northString = "北";//北
    private String southString = "南";//南
    private String westString = "西";//西
    private String eastString = "东";//东

    //用来显示当前方向
    private int bearing;

    private int textHeight;
    private int textWidth;

    public int getBearing() {
        return bearing;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
    }

    public CompassView(Context context) {
        this(context, null);
    }

    public CompassView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCompassView();
    }

    /**
     * 初始化罗盘控件的一些参数
     */
    private void initCompassView() {
        setFocusable(true);


        marketPaint = new Paint();
        marketPaint.setAntiAlias(true);
        marketPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        marketPaint.setColor(0xFFFF1122);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFFFFFFFF);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.parseColor("#FFEE2288"));
        //测量指定字的宽度
        textHeight = (int) textPaint.measureText("W");
        textWidth = (int) textPaint.measureText("yY");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = measure(widthMeasureSpec);
        int measureHeight = measure(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int measureWidth = getMeasuredWidth();
        int measureHeight = getMeasuredHeight();

        int px = measureWidth / 2;
        int py = measureHeight / 2;

        int radius = Math.min(px, py);

        //画圆
        canvas.drawCircle(px, py, radius, circlePaint);
        canvas.save();

        //通过旋转画布来显示当前方向
        canvas.rotate(-bearing, px, py);

        //下面准备画标记了，
        int cardinalX = px - textWidth / 2;
        int cardinalY = py - radius + textHeight;
        //360度分成24份，每15度画一刻度，每45度画一文本
        for (int i = 0; i < 24; i++) {
            canvas.drawLine(px, py - radius, px, py - radius + 10, marketPaint);
            canvas.save();
            canvas.translate(0, textHeight);

            //先画东南西北
            if (i % 6 == 0) {
                String direcString = "";
                switch (i) {
                    case 0:
                        direcString = northString;
                        int arrowY = 2 * textHeight;
                        canvas.drawLine(px, arrowY, px - 5, 3 * textHeight, marketPaint);
                        canvas.drawLine(px, arrowY, px + 5, 3 * textHeight, marketPaint);
                        break;
                    case 6:
                        direcString = eastString;
                        break;
                    case 12:
                        direcString = southString;
                        break;
                    case 18:
                        direcString = westString;
                        break;
                }
                canvas.drawText(direcString, cardinalX, cardinalY, textPaint);
            } else if (i % 3 == 0) {
                //每45度画一个角度
                String angle = String.valueOf(i * 15);
                float angleTextWidth = textPaint.measureText(angle);
                int angleTextX = (int) (px - angleTextWidth / 2);
                int angleTextY = py - radius + textHeight;
                canvas.drawText(angle, angleTextX, angleTextY, textPaint);
            }
            canvas.restore();
            canvas.rotate(15, px, py);
        }

        canvas.restore();

    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.dispatchPopulateAccessibilityEvent(event);
        if (!isShown()) {
            return false;
        } else {
            String bearingStr = String.valueOf(bearing);
            if (bearingStr.length() > AccessibilityEvent.MAX_TEXT_LENGTH) {
                bearingStr = bearingStr.substring(0, AccessibilityEvent.MAX_TEXT_LENGTH);
            }
            event.getText().add(bearingStr);
            return true;
        }
    }

    private int measure(int measureSpec) {
        int result = 0;
        //对测量说明进行解码
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;//指明大小
        } else if (specMode == MeasureSpec.AT_MOST) {
            //充分利用ViewGroup可用的空间
            result = specSize;
        } else if (specSize == MeasureSpec.UNSPECIFIED) {
            //未指定大小时，给一个默认值
            result = 300;
        }
        return result;
    }
}
