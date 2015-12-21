package com.het.comres.view.sortlistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.het.comres.R;

public class SideBar extends View {

    public static String[] LETTER_CHARTS = {"#", "☆", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private Paint mPaint;
    private int mTextColorUnChoose;
    private int mTextColorChoose;
    private float mTextSize;

    private int mChoose;// 选中
    private Drawable mChooseBgRes;
    private TextView mTextDialog;
    private OnTouchingLetterChangedListener mOnTouchingLetterChangedListener;

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SideBar);
        mTextColorChoose = typedArray.getColor(R.styleable.SideBar_ChooseTextColor, Color.BLUE);
        mTextColorUnChoose = typedArray.getColor(R.styleable.SideBar_UnChooseTextColor, Color.BLUE);
        mTextSize = typedArray.getDimension(R.styleable.SideBar_TextSize, 40.0f);
        mChooseBgRes = typedArray.getDrawable(R.styleable.SideBar_ChooseBgDrawable);
        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / LETTER_CHARTS.length;// 获取每一个字母的高度

        for (int i = 0; i < LETTER_CHARTS.length; i++) {
            mPaint.setColor(mTextColorUnChoose);
            mPaint.setTypeface(Typeface.DEFAULT);
            mPaint.setTextSize(mTextSize);
            mPaint.setAntiAlias(true);
            // 选中的状态
            if (i == mChoose) {
                mPaint.setColor(mTextColorChoose);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - mPaint.measureText(LETTER_CHARTS[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(LETTER_CHARTS[i], xPos, yPos, mPaint);
            mPaint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = mChoose;
        final OnTouchingLetterChangedListener listener = mOnTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * LETTER_CHARTS.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                mChoose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                setBackgroundDrawable(mChooseBgRes);
                if (oldChoose != c) {
                    if (c >= 0 && c < LETTER_CHARTS.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(LETTER_CHARTS[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(LETTER_CHARTS[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        mChoose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setIndicatorDialog(TextView dialog) {
        mTextDialog = dialog;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        mOnTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

}