package com.het.comres.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.het.comres.R;

/**
 * 公共的TopBar Created by Galis on 2015/5/25.
 */
public class CommonTopBar extends FrameLayout implements View.OnClickListener {

    public static final int NORMAL_MODE = 0;
    public static final int NAVIGATION_MODE = 1;
    private static final int DEFAULT_DRAWABLE_PADDING = 5;

    private Context mContext;
    private boolean mIsAnimate = false;

    // Normal
    private View mTopBar;
    private View mRedDot;
    private View mLeftClick;
    private ImageView mLeftImage;
    private TextView mTitleView;
    private TextView mRightText;
    private ImageView mRightImgOne;
    private ImageView mRightImgTwo;

    // 搜索模式的View
    private View mTopBarOverlay;
    private View mCancelView;
    private View mCloseView;
    private View mSearchView;
    private EditText mEditText;

    private OnClickListener mLeftClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }
        }
    };

    @Override
    public void onClick(View v) {

    }

    private int mMode = NORMAL_MODE;

    public CommonTopBar(Context context) {
        this(context, null);
    }

    public CommonTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public View getmSearchView() {
        return mSearchView;
    }

    public void setmSearchView(View mSearchView) {
        this.mSearchView = mSearchView;
    }

    private void init() {
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);

        mTopBar = mLayoutInflater.inflate(R.layout.common_topbar_help, this,
                false);

        mRedDot = mTopBar.findViewById(R.id.red_dot);
        mLeftImage = (ImageView) mTopBar.findViewById(R.id.left_image);
        mLeftClick = mTopBar.findViewById(R.id.left_click);
        mTitleView = (TextView) mTopBar.findViewById(R.id.title);

        mRightText = (TextView) mTopBar.findViewById(R.id.right_text);
        mRightImgOne = (ImageView) mTopBar.findViewById(R.id.right_img_one);
        mRightImgTwo = (ImageView) mTopBar.findViewById(R.id.right_img_two);
        mRightText.setOnClickListener(this);
        mRightImgOne.setOnClickListener(this);
        mRightImgTwo.setOnClickListener(this);

        addView(mTopBar);

        mTopBarOverlay = mLayoutInflater.inflate(
                R.layout.common_topbar_overlay, this, false);
        mTopBarOverlay.setVisibility(View.GONE);
        mCancelView = mTopBarOverlay.findViewById(R.id.overlay_cancel);
        mCloseView = mTopBarOverlay.findViewById(R.id.overlay_close);
        mSearchView = mTopBarOverlay.findViewById(R.id.overlay_search);
        mEditText = (EditText) mTopBarOverlay.findViewById(R.id.overlay_edit);
        addView(mTopBarOverlay);
    }

    public void setLeftClick(OnClickListener leftListener) {
        mLeftImage.setOnClickListener(leftListener);
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    public void setTitle(int res) {
        mTitleView.setText(res);
    }

    public void setLeftRedDot(boolean isVisible) {
        mRedDot.setVisibility(isVisible ? VISIBLE : INVISIBLE);
    }

    public void setBackground(int color) {
        mTopBar.setBackgroundColor(color);
    }

    @Deprecated
    public void setMode(int mode) {
        mMode = mode;
        setUpMode();
    }

    private void setUpMode() {
        switch (mMode) {
            case NORMAL_MODE:
                break;
            case NAVIGATION_MODE:
                setUpNavigateMode();
                break;
        }
    }

    public void setUpNavigate(int res, OnClickListener onClickListener) {
        mLeftImage.setImageResource(res);
        mLeftClick.setOnClickListener(onClickListener);
    }

    public void setUpNavigate(Drawable drawable, OnClickListener onClickListener) {
        mLeftImage.setImageDrawable(drawable);
        mLeftClick.setOnClickListener(onClickListener);
    }

    public void setUpNavigateMode() {
        mLeftImage.setImageResource(R.drawable.arrow_back);
        mLeftClick.setOnClickListener(mLeftClickListener);
    }

    /**
     * setup rightOne
     *
     * @param res
     * @param tag
     */
    public void setUpTextOption(int res, String tag,
                                OnClickListener onClickListener) {
        mRightText.setVisibility(View.VISIBLE);
        if (res != -1) {
            Drawable drawable = getResources().getDrawable(res);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            mRightText.setCompoundDrawables(drawable, null, null, null);
            mRightText
                    .setCompoundDrawablePadding((int) (DEFAULT_DRAWABLE_PADDING * getResources()
                            .getDisplayMetrics().density));
        }
        if (!TextUtils.isEmpty(tag)) {
            mRightText.setText(tag);
        }
        if (onClickListener != null) {
            mRightText.setOnClickListener(onClickListener);
        }
    }

    /**
     * setup rightOne
     *
     * @param res
     * @param tag
     */
    public void setUpTextOption(int res, String tag) {
        setUpTextOption(res, tag, null);
    }

    /**
     * setup rightOne
     */
    public void setUpTextOption(String tag, OnClickListener onClickListener) {
        setUpTextOption(-1, tag, onClickListener);
    }

    /**
     * setup rightImgOne
     *
     * @param res1
     */
    public void setUpImgOption(int res1) {
        mRightImgOne.setVisibility(VISIBLE);
        mRightImgOne.setImageResource(res1);
    }

    /**
     * setup rightImgOne
     *
     * @param res1
     */
    public void setUpImgOption(int res1, OnClickListener onClickListener) {
        setUpImgOption(res1);
        mRightImgOne.setOnClickListener(onClickListener);
    }

    /**
     * setup rightImgTwo
     *
     * @param res1
     * @param res2
     */
    public void setUpImgOption(int res1, OnClickListener onClickListener1, int res2, OnClickListener onClickListener2) {
        mRightImgTwo.setVisibility(VISIBLE);
        mRightImgTwo.setImageResource(res2);
        mRightImgTwo.setOnClickListener(onClickListener2);
        setUpImgOption(res1, onClickListener1);
    }

    public final void setUpSearchMode() {
        setUpImgOption(R.drawable.main_icon_search);
        mRightImgOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animAlpha(mTopBarOverlay, mTopBar);
            }
        });
        mCancelView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                animAlpha(mTopBar, mTopBarOverlay);
            }
        });
    }

    private void animAlpha(final View show, final View hide) {
        if (mIsAnimate) {
            return;
        }
        mIsAnimate = true;
        ValueAnimator alpahAnimator = ValueAnimator.ofFloat(0, 1);
        alpahAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float flactor = (Float) animation.getAnimatedValue();
                        show.setAlpha(flactor);
                        hide.setAlpha(1 - flactor);
                    }
                });
        alpahAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                show.setVisibility(View.VISIBLE);
                hide.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hide.setVisibility(View.GONE);
                mIsAnimate = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        alpahAnimator.setDuration(500);
        alpahAnimator.start();
    }

}