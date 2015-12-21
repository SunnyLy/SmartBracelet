package com.het.comres.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.het.comres.R;


/**
 * Created by galislsm on 2015/4/8.
 */
public class ColorToggleButton extends RelativeLayout {
    public static final int OPEN = 1;
    public static final int CLOSE = -1;
    public static final int RUN = 0;
    private static final ClipData EMPTY_CLIP_DATA = ClipData.newPlainText("Toggle", "Toggle");
    private static final String TOGGLE_BUTTON = "COLORBUTTON";
    private static final long DURATION = 300;

    private ColorChangeBgView mBgView;
    private AnimBall mAnimBall;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private int mState = -1;//-1:关 0:运动 1:开


    private int mMeasureWidth;
    private int mMeasureHeight;
    private int mBallWidth;
    private int mBallColor;
    private int mBgSelColor;
    private int mBgUnSelColor;

    private int mBallStartLeft;//
    private int mBallStopLeft;//
    private int mBallAnimStartLeft;
    private int mBallAnimStopLeft;
    private int mBallScrollMaxX;

    private float mPaddingFactor = 0.1f;
    private int mFirstScreenX;
    private int[] mScreenLocation = new int[2];


    public ColorToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorToggleButton);
        mBallColor = typedArray.getColor(R.styleable.ColorToggleButton_toggole_ball_color, Color.WHITE);
        mBgSelColor = typedArray.getColor(R.styleable.ColorToggleButton_toggole_bg_color_sel, Color.parseColor("#FF2ED785"));
        mBgUnSelColor = typedArray.getColor(R.styleable.ColorToggleButton_toggole_bg_color_unsel, Color.GRAY);
        typedArray.recycle();


        mBgView = new ColorChangeBgView(context);
        mBgView.initColor(mBgUnSelColor, mBgSelColor);
        mBgView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mBgView);

        mAnimBall = new AnimBall(context);
        mAnimBall.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mAnimBall.setColor(mBallColor);
        addView(mAnimBall);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mState == RUN) {
                    return;
                }
                if (mState == OPEN) {
                    close();
                } else {
                    open();
                }
            }
        });


        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.startDrag(EMPTY_CLIP_DATA, new DragShadowBuilder(), TOGGLE_BUTTON, 0);
                return false;
            }
        });

    }

    public void initState(int state) {
        mState = state;
        mBgView.setColorFlactor(mState == OPEN ? 1 : 0);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMs = MeasureSpec.makeMeasureSpec(getLayoutParams().width, MeasureSpec.EXACTLY);
        int heightMs = MeasureSpec.makeMeasureSpec(getLayoutParams().height, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMs, heightMs);
        }
        setMeasuredDimension(widthMs, heightMs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mMeasureWidth = getWidth();
        mMeasureHeight = getHeight();
        mBgView.layout(0, 0, mMeasureWidth, mMeasureHeight);

        mAnimBall.setBallRadius((int) (mMeasureHeight * (1 - 2 * mPaddingFactor) / 2));
        mBallWidth = mAnimBall.getActualWidth();

        int padding = (mMeasureHeight - mAnimBall.getActualHeight()) / 2;
        mBallStartLeft = padding;
        mBallStopLeft = mMeasureWidth - padding - mBallWidth;
        mBallScrollMaxX = mMeasureWidth - 2 * padding - mBallWidth;
        if (mState == OPEN) {
            mAnimBall.layout(mBallStopLeft, padding, mBallStopLeft + mBallWidth, padding + mAnimBall.getActualHeight());
        } else if (mState == CLOSE) {
            mAnimBall.layout(mBallStartLeft, padding, mBallStartLeft + mBallWidth, padding + mAnimBall.getActualHeight());
        }


        Log.e("mMeasureWidth ", mMeasureWidth + "");
        Log.e("mMeasureHeight ", mMeasureHeight + "");
        Log.e("mBallWidth", mBallWidth + "");
        Log.e(" mAnimBall", mAnimBall + "");

    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    private void slideButton() {
        final int lastState = mState;
        Log.e("tag", "lastState:" + mState);
        ValueAnimator openAnimator = ValueAnimator.ofInt(mBallAnimStartLeft, mBallAnimStopLeft);
        openAnimator.setDuration(DURATION);
        openAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int left = (Integer) valueAnimator.getAnimatedValue();
                moveBall(left);
            }
        });
        openAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        openAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mState = 0;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                int state = mBallAnimStopLeft == mBallStartLeft ? -1 : 1;
                mState = state;
                if (lastState != mState) {
                    if (mOnCheckedChangeListener != null) {
                        mOnCheckedChangeListener.onCheckedChange(ColorToggleButton.this, mState == 1);
                    }
                    Log.e("tag", "state:" + state);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        openAnimator.start();
    }


    private void moveBall(int curLeft) {

        if (curLeft > mBallStopLeft || curLeft < mBallStartLeft) {
            return;
        }

        float f = (curLeft - mBallStartLeft) * 1.0f / mBallScrollMaxX;
        mAnimBall.setAnimFactor(f);
        mAnimBall.layout(curLeft, mAnimBall.getTop(), curLeft + mAnimBall.getActualWidth(), mAnimBall.getBottom());
        mBgView.setColorFlactor(f);
    }


    @Override
    public boolean onDragEvent(DragEvent event) {

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (!TOGGLE_BUTTON.equals(event.getLocalState())) {
                    return false;
                }
                mFirstScreenX = (int) event.getX();
                getLocationOnScreen(mScreenLocation);
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                int curScreenX = (int) (mScreenLocation[0] + event.getX());
                int deltaX = curScreenX - mFirstScreenX;
                moveBall(mAnimBall.getLeft() + deltaX);
                mFirstScreenX = curScreenX;
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                autoOpenOrClose();
                break;
        }
        return true;
    }

    private void autoOpenOrClose() {
        if (isNeedToClose()) {
            close();
        } else {
            open();
        }
    }

    private boolean isNeedToClose() {
        return mAnimBall.getLeft() - mBallStartLeft < mBallScrollMaxX * 1.0f / 2;
    }


    public void open() {
        mBallAnimStartLeft = mAnimBall.getLeft();
        mBallAnimStopLeft = mBallStopLeft;
        slideButton();
    }

    public void close() {
        mBallAnimStartLeft = mAnimBall.getLeft();
        mBallAnimStopLeft = mBallStartLeft;
        slideButton();
    }

    public boolean isOpen() {
        return mState == OPEN;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(ColorToggleButton button, boolean isChecked);
    }

}
