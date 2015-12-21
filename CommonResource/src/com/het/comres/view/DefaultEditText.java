/**
 *
 */
package com.het.comres.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.EditText;

import com.het.comres.R;


/**
 * @author galis
 */
public class DefaultEditText extends EditText {

    private Context mContext;
    private Drawable mDelDrawable;
    private boolean mIsShow;
    private int mPaddingRight;

    public DefaultEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mDelDrawable = getResources().getDrawable(R.drawable.icon_delete);
        mIsShow = false;
        mPaddingRight = (int) (20 * getResources().getDisplayMetrics().density);
        setPadding(getPaddingLeft(), getPaddingTop(), mPaddingRight, getPaddingBottom());
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            if (getText().toString().length() > 0) {
                show();
            }
        } else {
            hide();
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (text.length() > 0 && !mIsShow) {
            show();
        } else {
            if (text.length() == 0) {
                hide();
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getX() > getWidth() - mDelDrawable.getIntrinsicWidth() - mPaddingRight) {
            setText("");
        }

        return super.dispatchTouchEvent(event);
    }

    public void show() {
        setCompoundDrawablesWithIntrinsicBounds(null, null, mDelDrawable, null);
        mIsShow = true;
    }

    public void hide() {
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        mIsShow = false;
    }

    /**
     * 将光标移到最后
     */
    public void moveCursorToLast() {
        String str = getText().toString() + "";
        setSelection(str.length());
    }

    /**
     * 展示键盘
     */
    public void showKeyBoard(final Dialog dialog) {
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }
}
