package com.het.comres.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.het.comres.R;

import java.security.InvalidParameterException;

/**
 * 自定义布局
 *
 * @author sunny
 * @date 2015年3月19日 上午11:24:33
 */
public class ItemLinearLayout extends LinearLayout {
    private Drawable mDrawableLeft;
    private Drawable mDrawableRight;

    private String mStringLeft;
    private String mStringRight;

    private ImageView mIViewLeft, mIViewRight, iv_common_arrow;
    private TextView mTViewLeft, mTViewRight;

    private TextView mTViewDiliver;

    private RelativeLayout mRLayoutItem;

    private int bgColor;
    private boolean showDiliver;
    private boolean isShowRight;
    private int diliverCcolor;
    private int text_color;

    public int getDiliverCcolor() {
        return diliverCcolor;
    }

    public void setDiliverCcolor(int diliverCcolor) {
        this.diliverCcolor = diliverCcolor;
    }

    public boolean isShowRight() {
        return isShowRight;
    }

    public void setShowRight(boolean isShowRight) {
        this.isShowRight = isShowRight;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        if (mRLayoutItem != null) {
            mRLayoutItem.setBackgroundColor(bgColor);
        }
    }

    public void setmDrawableRight(Drawable mDrawableRight) {
        this.mDrawableRight = mDrawableRight;
        iv_common_arrow.setImageDrawable(mDrawableRight);
    }

    public ItemLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public ItemLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.activity_common_item, null);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ItemLinearLayout);
        mDrawableLeft = typedArray
                .getDrawable(R.styleable.ItemLinearLayout_item_leftImageSrc);
        mDrawableRight = typedArray
                .getDrawable(R.styleable.ItemLinearLayout_item_rightImageSrc);
        mStringLeft = typedArray
                .getString(R.styleable.ItemLinearLayout_item_funcName);
        mStringRight = typedArray
                .getString(R.styleable.ItemLinearLayout_item_rightText);

        isShowRight = typedArray.getBoolean(
                R.styleable.ItemLinearLayout_item_isShowRight, true);

        showDiliver = typedArray.getBoolean(
                R.styleable.ItemLinearLayout_item_showDiliver, true);

        diliverCcolor = typedArray.getInt(
                R.styleable.ItemLinearLayout_item_diliver_color,
                Color.parseColor("#CBCCCE"));

        text_color = typedArray.getInt(
                R.styleable.ItemLinearLayout_item_text_color,
                Color.parseColor("#232323"));

        mRLayoutItem = (RelativeLayout) view.findViewById(R.id.common_item_bg);

        mIViewLeft = (ImageView) view.findViewById(R.id.iv_common_left);
        mIViewRight = (ImageView) view.findViewById(R.id.iv_commom_right);
        mTViewLeft = (TextView) view.findViewById(R.id.tv_common_left);
        mTViewRight = (TextView) view.findViewById(R.id.tv_common_right);
        mTViewDiliver = (TextView) view.findViewById(R.id.tv_common_diliver);

        iv_common_arrow = (ImageView) view.findViewById(R.id.iv_common_arrow);

        if (isShowRight) {
            iv_common_arrow.setVisibility(View.VISIBLE);
        } else {
            iv_common_arrow.setVisibility(View.INVISIBLE);
        }

        if (!showDiliver) {
            mTViewDiliver.setVisibility(View.INVISIBLE);
        } else {
            mTViewDiliver.setVisibility(View.VISIBLE);
            mTViewDiliver.setBackgroundColor(diliverCcolor);
        }

        // 左边图片
        if (mDrawableLeft != null) {
            mIViewLeft.setVisibility(View.VISIBLE);
            mIViewLeft.setImageDrawable(mDrawableLeft);
        } else {
            mIViewLeft.setVisibility(View.GONE);
        }

        // 右边图片
        if (mDrawableRight != null) {
            mIViewRight.setImageDrawable(mDrawableRight);
        }

        // 左边显示功能名称
        if (TextUtils.isEmpty(mStringLeft)) {
            throw new InvalidParameterException(
                    "dear,you must offer a funcName");
        } else {
            mTViewLeft.setText(mStringLeft);
            mTViewLeft.setTextColor(text_color);

        }
        // 右边文字
        if (!TextUtils.isEmpty(mStringRight)) {
            mTViewRight.setText(mStringRight);
            mTViewRight.setTextColor(text_color);
        }

        typedArray.recycle();

        addView(view);
    }

    public void showRightText(String rightText) {
        mStringRight = rightText;
        mTViewRight.setText(mStringRight);
    }

    public ItemLinearLayout(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    public void setRightText(String text) {
        mTViewRight.setText(text);
    }

    public String getRightText() {
        return mTViewRight.getText().toString();
    }

    public void setLeftText(String text) {
        mTViewLeft.setText(text);
    }

    public String getLeftText() {
        return mTViewLeft.getText().toString();
    }

}
