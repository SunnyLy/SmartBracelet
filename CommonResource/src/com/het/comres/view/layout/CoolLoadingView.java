package com.het.comres.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.het.comres.R;

/**
 * Created by galislsm on 2015/4/28.
 */
public class CoolLoadingView extends RelativeLayout {

    private static final long ANIM_DURATION = 600;
    private static final long PER_DELAY = 180;
    private static final int NUM = 3;
    private static final int SHAPE_BALL_RADIUS_DP = 10;
    private static final int LEFT_MARGIN_DP = 10;


    private float mDensity;
    private int mRadius;
    private int mLeftMargin;
    private LinearLayout mContainer;

    public CoolLoadingView(Context context) {
        super(context);
        init();
    }

    public CoolLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mDensity = getResources().getDisplayMetrics().density;
        mRadius = (int) (SHAPE_BALL_RADIUS_DP * mDensity);
        mLeftMargin = (int) (LEFT_MARGIN_DP * mDensity);

        generateContainer();
        addView(mContainer);
        anim();
    }

    private LinearLayout generateContainer() {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT);

        mContainer = new LinearLayout(getContext());
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setLayoutParams(layoutParams);

        mContainer.addView(generateChild(0));

        for (int i = 0; i < NUM - 1; i++) {
            mContainer.addView(generateChild(mLeftMargin));
        }
        return mContainer;
    }

    private View generateChild(int margin) {
        ImageView child = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mRadius * 2, mRadius * 2);
        layoutParams.leftMargin = margin;
        child.setLayoutParams(layoutParams);
        child.setBackgroundResource(R.drawable.circle_ball);
        child.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        child.setVisibility(View.INVISIBLE);
        return child;
    }

    private void anim() {
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            final View child = mContainer.getChildAt(i);
            final Animation animation = generateScaleReverseAnimation();
            child.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    child.startAnimation(animation);
                }
            }, i * PER_DELAY);
        }

    }

    private Animation generateScaleReverseAnimation() {
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, mRadius, mRadius);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(ANIM_DURATION);
        return animation;
    }
}
