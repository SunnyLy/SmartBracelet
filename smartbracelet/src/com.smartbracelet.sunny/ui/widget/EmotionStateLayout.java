package com.smartbracelet.sunny.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartbracelet.sunny.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunny on 2015/12/20.
 * 心情状态显示的布局
 * 根据UI需求，意在封装成一个只需外部传入一个表示心情状态的控件。
 */
public class EmotionStateLayout extends LinearLayout {
    public static final String TAG = EmotionStateLayout.class.getSimpleName();

    @InjectView(R.id.emotion_state_happy)
    TextView mEmotionHappy;
    @InjectView(R.id.icon_emotion_happy)
    ImageView mIconEmotionHappy;

    @InjectView(R.id.emotion_state_fraid)
    TextView mEmotionFraid;
    @InjectView(R.id.icon_emotion_fraid)
    ImageView mIconEmotionFraid;

    @InjectView(R.id.emotion_state_bother)
    TextView mEmotonBother;
    @InjectView(R.id.icon_emotion_bother)
    ImageView mIconEmotionBother;

    @InjectView(R.id.emotion_state_angry)
    TextView mEmotionAngry;
    @InjectView(R.id.icon_emotion_angry)
    ImageView mIconEmotionAngry;

    @InjectView(R.id.emotion_state_hurt)
    TextView mEmotionHurt;
    @InjectView(R.id.icon_emotion_hurt)
    ImageView mIconEmotionHurt;

    private TextView[] mStatesInfos;

    private Paint mLinePaint;
    private DisplayMetrics mDm;

    private EmotionState emotionState;

    /**
     * 设置心情状态
     *
     * @param emotionState
     */
    public void setEmotionState(EmotionState emotionState) {
        this.emotionState = emotionState;
        freshStateLayout(emotionState);
    }

    private void freshStateLayout(EmotionState emotionState) {
        if (emotionState == null) {
            emotionState = EmotionState.HAPPY;
        }

        switch (emotionState) {
            case HAPPY:
                changeView(0);
                break;
            case BOTHER:
                break;
            case FRAID:
                break;
            case ANGRY:
                break;
            case HURT:
                break;
        }
    }

    public enum EmotionState {
        HAPPY, BOTHER, FRAID, ANGRY, HURT
    }

    public EmotionStateLayout(Context context) {
        this(context, null);
    }

    public EmotionStateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
        initLayout(context);
    }

    private void initPaint(Context context) {
        mLinePaint = new Paint();
        //   mLinePaint.setColor(0xFFF39D2B);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeWidth(2);

        mDm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDm);

    }

    /**
     * 初始化布局
     *
     * @param context
     */
    private void initLayout(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.item_emotion_state_show, null);
        ButterKnife.inject(this, contentView);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        contentView.setLayoutParams(layoutParams);
        addView(contentView);
        mStatesInfos = new TextView[]{mEmotionHappy, mEmotionAngry, mEmotionFraid, mEmotionHurt, mEmotonBother};
        invalidate();
    }

    private void changeView(int position) {
        for (int i = 0; i < mStatesInfos.length; i++) {
            if (i == position) {
                mStatesInfos[position].setTextColor(Color.WHITE);
            } else {
                mStatesInfos[i].setTextColor(0xFFD17913);
            }
        }
        switch (position) {
            case 0:
                mIconEmotionHappy.setImageResource(R.mipmap.icon_emotion_happy_checked);
                mIconEmotionAngry.setImageResource(R.mipmap.icon_emotion_angry_normal);
                mIconEmotionBother.setImageResource(R.mipmap.icon_emotion_angry_normal);
                mIconEmotionFraid.setImageResource(R.mipmap.icon_emotion_fraid_normal);
                mIconEmotionHurt.setImageResource(R.mipmap.icon_emotion_hurt_normal);
                break;
            case 1:
                mIconEmotionHappy.setImageResource(R.mipmap.icon_emotion_happy_normal);
                mIconEmotionBother.setImageResource(R.mipmap.icon_emotion_angry_checked);
                mIconEmotionAngry.setImageResource(R.mipmap.icon_emotion_angry_normal);
                mIconEmotionFraid.setImageResource(R.mipmap.icon_emotion_fraid_normal);
                mIconEmotionHurt.setImageResource(R.mipmap.icon_emotion_hurt_normal);
                break;
            case 2:
                mIconEmotionHappy.setImageResource(R.mipmap.icon_emotion_happy_checked);
                mIconEmotionAngry.setImageResource(R.mipmap.icon_emotion_angry_normal);
                mIconEmotionFraid.setImageResource(R.mipmap.icon_emotion_fraid_checked);
                mIconEmotionBother.setImageResource(R.mipmap.icon_emotion_angry_normal);
                mIconEmotionHurt.setImageResource(R.mipmap.icon_emotion_hurt_normal);
                break;
            case 3:
                mIconEmotionHappy.setImageResource(R.mipmap.icon_emotion_happy_checked);
                mIconEmotionBother.setImageResource(R.mipmap.icon_emotion_angry_normal);
                mIconEmotionFraid.setImageResource(R.mipmap.icon_emotion_fraid_normal);
                mIconEmotionAngry.setImageResource(R.mipmap.icon_emotion_angry_checked);
                mIconEmotionHurt.setImageResource(R.mipmap.icon_emotion_hurt_normal);
                break;
            case 4:
                mIconEmotionHappy.setImageResource(R.mipmap.icon_emotion_happy_checked);
                mIconEmotionAngry.setImageResource(R.mipmap.icon_emotion_angry_normal);
                mIconEmotionBother.setImageResource(R.mipmap.icon_emotion_angry_normal);
                mIconEmotionFraid.setImageResource(R.mipmap.icon_emotion_fraid_normal);
                mIconEmotionHurt.setImageResource(R.mipmap.icon_emotion_hurt_checked);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText("Emotion", (getWidth() - getPaddingLeft()) / 2, getHeight() - getPaddingTop(), mLinePaint);
        canvas.drawLine(10, getHeight() - getPaddingTop(), 20 * mDm.density, 10 - getPaddingTop(), mLinePaint);
        canvas.drawLine(getPaddingLeft() + 20 * mDm.density, getHeight() - getPaddingTop(), 20 * mDm.density, getHeight() - getPaddingTop(), mLinePaint);
        canvas.drawLine(getPaddingLeft() + 40 * mDm.density, getHeight() - getPaddingTop(), 20 * mDm.density, getHeight() - getPaddingTop(), mLinePaint);
        canvas.drawLine(getPaddingLeft() + 60 * mDm.density, getHeight() - getPaddingTop(), 20 * mDm.density, getHeight() - getPaddingTop(), mLinePaint);
        canvas.drawLine(getPaddingLeft() + 80 * mDm.density, getHeight() - getPaddingTop(), 20 * mDm.density, getHeight() - getPaddingTop(), mLinePaint);
    }
}
