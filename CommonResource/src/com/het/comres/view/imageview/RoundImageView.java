package com.het.comres.view.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.het.comres.R;

/**
 * 圆形ImageView
 * Tip:Must set height and width in the xml and height is equals width width!!
 */
public class RoundImageView extends ImageView {

    private Paint mPaint;
    private RectF mRectF_Big;
    private RectF mRectF_Small;
    private Xfermode mXfermode;
    private Bitmap mBitmap;
    private int mBorderWidth;
    private int mBorderColor;

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mRectF_Big = new RectF();
        mRectF_Small = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(false);
        mXfermode = (new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        mBorderWidth = ta.getDimensionPixelSize(R.styleable.RoundImageView_round_border_width, 0);
        mBorderColor = ta.getColor(R.styleable.RoundImageView_round_border_color, Color.WHITE);
        ta.recycle();
    }

    private boolean isHeightWidthInValidate() {
        int height = getLayoutParams().height;
        int width = getLayoutParams().width;
        return height <= 0 || width <= 0 || height != width;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int length = getLayoutParams().height;
        if (isHeightWidthInValidate()) {
            throw new IllegalArgumentException("Height must equals width!");
        }

        mRectF_Big.set(0, 0, length, length);
        mRectF_Small.set(mRectF_Big);
        mRectF_Big.inset(mBorderWidth / 2.0f, mBorderWidth / 2.0f);
        mRectF_Small.inset(mBorderWidth, mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int length = getLayoutParams().height;
        if (mBorderWidth != 0) {
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mBorderColor);
        }
        canvas.drawOval(mRectF_Big, mPaint);
        canvas.saveLayer(0, 0, length, length, null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawOval(mRectF_Small, mPaint);
        mPaint.setXfermode(mXfermode);
        if (getDrawable() != null) {
            mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            canvas.drawBitmap(mBitmap, null, mRectF_Small, mPaint);
            mPaint.setXfermode(null);
        }
    }
}
