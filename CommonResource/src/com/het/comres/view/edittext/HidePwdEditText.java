package com.het.comres.view.edittext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.het.comres.R;

public class HidePwdEditText extends EditText {

    private Drawable mClearDrawable;

    private boolean hasHide = true;

    public HidePwdEditText(Context context) {
        this(context, null);
    }

    public HidePwdEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public HidePwdEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private final int base = 10;

    private void init() {

        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(
                    R.drawable.btn_mimakejian_shurukuang);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth()
                + base, mClearDrawable.getIntrinsicHeight() + base);
        setClearIconVisible(true);
        this.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    private void changeIcon() {
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(
                    R.drawable.btn_mimakejian_shurukuang_press);
        }
        if (hasHide) {
            mClearDrawable = getResources().getDrawable(
                    R.drawable.btn_mimakejian_shurukuang_press);
            this.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
            hasHide = false;
        } else {
            mClearDrawable = getResources().getDrawable(
                    R.drawable.btn_mimakejian_shurukuang);
            this.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
            hasHide = true;
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth()
                + base, mClearDrawable.getIntrinsicHeight() + base);
        setClearIconVisible(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));
                if (touchable) {
                    changeIcon();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

}
