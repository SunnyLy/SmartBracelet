package com.smartbracelet.sunny.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.smartbracelet.sunny.R;

/**
 * Created by sunny on 2015/11/12.
 * ViewPager下面的圆点显示
 */
public class ViewPagerIndicator extends RelativeLayout {

    private static final int mChooseColorDef = Color.parseColor("#FFEC1B23");
    private static final int mUnChooseColorDef = Color.parseColor("#FFAAAAAA");
    private static final int ITEM_WIDTH_DP = 6;
    private static final int ITEM_MARGIN_DP = 10;

    private int mDensity;
    private int mItemChooseColor;
    private int mItemUnChooseColor;
    private int mItemWidth;
    private int mItemMargin;

    private Context mContext;
    private Resources mResources;

    private ImageView mIndicator;
    private PagerAdapter mPagerAdapter;
    private ShapeDrawable mChooseShapeDrawable;
    private ShapeDrawable mUnChooseShapeDrawable;

    private LinearLayout mLinearLayout;

    public ViewPagerIndicator(Context context) {
        super(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mResources = context.getResources();
        mDensity = (int) mResources.getDisplayMetrics().density;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mItemChooseColor = typedArray.getColor(R.styleable.ViewPagerIndicator_item_choose_color, mChooseColorDef);
        mItemUnChooseColor = typedArray.getColor(R.styleable.ViewPagerIndicator_item_unchoose_color, mUnChooseColorDef);
        mItemWidth = (int) typedArray.getDimension(R.styleable.ViewPagerIndicator_item_width, ITEM_WIDTH_DP * mDensity);
        mItemMargin = (int) typedArray.getDimension(R.styleable.ViewPagerIndicator_item_margin, ITEM_MARGIN_DP * mDensity);

        mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mItemWidth));
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mLinearLayout);

        mChooseShapeDrawable = generateShapeDrawable(mItemChooseColor);
        mUnChooseShapeDrawable = generateShapeDrawable(mItemUnChooseColor);

        mIndicator = generateIndicator();
        addView(mIndicator);

        typedArray.recycle();

    }

    /**
     * public 方法，供外部调
     *
     * @return
     */
    public void setViewPager(ViewPager viewPager) {
        mPagerAdapter = viewPager.getAdapter();
        if (mPagerAdapter == null) {
            throw new NullPointerException("please to set the Adapter before invoke setViewPager()");
        }
        redrawChilds();
        mPagerAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                redrawChilds();
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float offset, int i1) {

                View view = mLinearLayout.getChildAt(position);
                if (view != null) {
                    mIndicator.setTranslationX(view.getLeft() + position + offset * (mItemWidth + mItemMargin));
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * 根据viewPager的页数来生成圆点数
     */
    private void redrawChilds() {
        int pageCount = mPagerAdapter.getCount();
        int pointCount = mLinearLayout.getChildCount();
        int delta = -pageCount + pointCount;
        if (delta < 0) {
            for (int i = 0; i < Math.abs(delta); i++) {
                mLinearLayout.addView(generateChild());
            }
        } else if (delta > 0) {
            for (int i = 0; i < Math.abs(delta); i++) {
                mLinearLayout.removeViewAt(pageCount - i - 1);
            }
        }

        if (pageCount <= 1) {
            setVisibility(INVISIBLE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    private View generateChild() {
        ImageView child = new ImageViewShape(mContext, mUnChooseShapeDrawable);
        child.setLayoutParams(generateIndicatorLayoutParams());
        return child;
    }

    private ImageView generateIndicator() {
        ImageView indicator = new ImageViewShape(mContext, mChooseShapeDrawable);
        indicator.setLayoutParams(generateIndicatorLayoutParams());
        return indicator;
    }

    /**
     * 生成选中圆点的LayoutParams
     *
     * @return
     */
    private LinearLayout.LayoutParams generateIndicatorLayoutParams() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mItemWidth, mItemWidth);
        layoutParams.rightMargin = mItemMargin;
        return layoutParams;
    }

    /**
     * 动态画选中的圆点
     *
     * @return
     */
    private ShapeDrawable generateShapeDrawable(int color) {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.setBounds(0, 0, mItemWidth, mItemWidth);
        return shapeDrawable;
    }


    private class ImageViewShape extends ImageView {

        private ShapeDrawable mShapeDrawable;

        public ImageViewShape(Context context, ShapeDrawable shapeDrawable) {
            super(context);
            this.mShapeDrawable = shapeDrawable;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //  super.onDraw(canvas);
            mShapeDrawable.draw(canvas);
        }
    }
}
