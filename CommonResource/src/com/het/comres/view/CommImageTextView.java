package com.het.comres.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.het.comres.R;

import java.security.InvalidParameterException;

/**
 * 自定义图片+文字控件
 *
 * @author sunny
 * @date 2015年3月19日 上午11:24:33
 */
public class CommImageTextView extends LinearLayout {

    private ImageView mIViewTop;
    private TextView mTViewDown;

    private Drawable mDrawTop;

    private String mStrDown;

    private boolean visible;

    public CommImageTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CommImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.widget_image_text, null);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CommImageTextView);

        mIViewTop = (ImageView) view.findViewById(R.id.ivShareIcon);
        mTViewDown = (TextView) view.findViewById(R.id.tvShareText);

        mDrawTop = typedArray.getDrawable(R.styleable.CommImageTextView_comm_topImageSrc);

        mStrDown = typedArray.getString(R.styleable.CommImageTextView_comm_itemName);
        // 左边图片
        if (mDrawTop != null) {
            mIViewTop.setImageDrawable(mDrawTop);
        }


        // 左边显示功能名称
        if (TextUtils.isEmpty(mStrDown)) {
            throw new InvalidParameterException("dear,you must offer a funcName");
        } else {
            mTViewDown.setText(mStrDown);
        }

        typedArray.recycle();

        addView(view);
    }

    public CommImageTextView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

}
