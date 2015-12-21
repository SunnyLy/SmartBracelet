package com.het.comres.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * 带左滑删除的ListView 同时附带下拉的弹性效果
 *
 * @author wu chen
 */

public class LeftSlideListView extends ListView {

    private static final String TAG = "LeftSlideListView";
    private static final boolean DEBUG = false;

    private boolean mFlexible = true;

    public void setFlexible(boolean flexible) {
        mFlexible = flexible;
    }

    public LeftSlideListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    Scroller mScroller;
    private float mOverDraggingBias;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                onTouchDown(ev);
                break;
            }

            case MotionEvent.ACTION_MOVE:

                if (mTouchMode == TOUCH_MODE_DEFAULT) {
                    if (ev.getY() - mActionDownY > 15) {
                        if (getFirstVisiblePosition() == 0
                                && getChildAt(0) != null && getChildAt(0).getTop() == 0) {
                            ev.setAction(MotionEvent.ACTION_CANCEL);
                            if (mFlexible) {
                                mTouchMode = TOUCH_MODE_DOWN_DRAGGING;
                                if (DEBUG) Log.e(TAG, "TOUCH_MODE_DOWN_DRAGGING");
                            }
                            mActionDownX = ev.getX();//
                            mActionDownY = ev.getY();//
                        }
                    } else if (ev.getY() - mActionDownY < -15) {

                        if (getAdapter().getCount() == getChildCount() + getFirstVisiblePosition()
                                && getChildAt(getChildCount() - 1) != null
                                && getChildAt(getChildCount() - 1).getBottom() <= getHeight()) {
                            ev.setAction(MotionEvent.ACTION_CANCEL);

                            if (mFlexible) {
                                mTouchMode = TOUCH_MODE_UP_DRAGGING;
                                if (DEBUG) Log.e(TAG, "TOUCH_MODE_UP_DRAGGING");
                            }

                            mActionDownX = ev.getX();
                            mActionDownY = ev.getY();
                        }
                    } else if (mActionDownX - ev.getX() > 15 && mScroller.getCurrY() == 0) {
                        ev.setAction(MotionEvent.ACTION_CANCEL);

                        mActionDownX = ev.getX();//
                        mActionDownY = ev.getY();//

                        mPosition = pointToPosition((int) mActionDownX, (int) mActionDownY) - getFirstVisiblePosition();
                        // This supposed to be like this:
                        //     mPosition = pointToPosition((int) mActionDownX , (int) mActionDownY );
                        // But in this situation, the adapter is optimized, which means
                        // the return value of getChildAt(mPosition) will no longer be it usually is

                        if (getChildAt(mPosition) != null) {

                            if (DEBUG) Log.e(TAG, "TOUCH_MODE_SLIDING");

                            mTouchMode = TOUCH_MODE_SLIDING;
                            ((ViewGroup) getChildAt(mPosition)).getChildAt(0).setOnTouchListener(onDeleteTouchEvent);
                        }
                    }
                } else if (mTouchMode == TOUCH_MODE_RESET) {
                    return true;
                }

                if (mTouchMode == TOUCH_MODE_DOWN_DRAGGING || mTouchMode == TOUCH_MODE_UP_DRAGGING) {
                    if ((mActionDownY <= ev.getY() && mTouchMode == TOUCH_MODE_DOWN_DRAGGING)
                            || (mActionDownY >= ev.getY() && mTouchMode == TOUCH_MODE_UP_DRAGGING)) {
                        mOverDraggingBias = Math.max(0, Math.min(ev.getY() - mActionDownY, mOverDraggingBias));

                        mScroller.setFinalY((int) (mActionDownY - ev.getY() + mOverDraggingBias) / 2);

                        if (mOnRefreshListener != null)
                            mRefresh = mOnRefreshListener.onDrag(this, mTouchMode, (int) (mActionDownY - ev.getY() + mOverDraggingBias) / 2);
                        invalidate();
                    } else {
                        mTouchMode = TOUCH_MODE_DEFAULT;

                        mActionDownX = ev.getX();//
                        mActionDownY = ev.getY();//

                        ev.setAction(MotionEvent.ACTION_DOWN);
                        super.onTouchEvent(ev);

                        ev.setLocation(ev.getX(), ev.getY() - 30);
                        ev.setAction(MotionEvent.ACTION_MOVE);
                        super.onTouchEvent(ev);

                        ev.setLocation(ev.getX(), ev.getY() + 30);
                    }
                } else if (mTouchMode == TOUCH_MODE_SLIDING) {
                    if (getChildAt(mPosition) != null) {
                        mMaxHorizontalOffset = ((ViewGroup) getChildAt(mPosition)).getChildAt(0).getWidth();

                        mOffsetX = mActionDownX - ev.getX();
                        mOffsetX = Math.max(MIN_HORIZONTAL_OFFSET, mOffsetX);
                        mOffsetX = Math.min(mMaxHorizontalOffset, mOffsetX);

//                        ((ViewGroup)getChildAt(mPosition)).getChildAt(1).scrollTo((int) mOffsetX, 0);//Mo
//                        ((ViewGroup)getChildAt(mPosition)).getChildAt(1).offsetLeftAndRight(-(int) mOffsetX);
                        final int width = getChildAt(mPosition).getWidth();

                        ((ViewGroup) getChildAt(mPosition)).getChildAt(1).setLeft(-(int) mOffsetX);
                        ((ViewGroup) getChildAt(mPosition)).getChildAt(1).setRight(width - (int) mOffsetX);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(ev);
                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (getChildAt(mPosition) != null) {
//                ((ViewGroup)getChildAt(mPosition)).getChildAt(1).scrollTo(mScroller.getCurrX(), 0);//Mo
                int width = getWidth();
                ((ViewGroup) getChildAt(mPosition)).getChildAt(1).setLeft(-mScroller.getCurrX());
                ((ViewGroup) getChildAt(mPosition)).getChildAt(1).setRight(width - mScroller.getCurrX());

                if (mScroller.getCurrX() == 0 && mDeletePosition != INVALID_POSITION) {
                    mOnItemDeleteListener.onItemDelete(getChildAt(mPosition), mDeletePosition);
                    mDeletePosition = INVALID_POSITION;
                }
            }
            scrollTo(0, mScroller.getCurrY());

            postInvalidate();
        }
    }

    float mActionDownX;
    float mActionDownY;

    float mOffsetX;
    private int mMaxHorizontalOffset;
    private final int MIN_HORIZONTAL_OFFSET = -20;

    private final int TOUCH_MODE_DEFAULT = 0;
    private int mTouchMode = TOUCH_MODE_DEFAULT;
    private int mPosition = INVALID_POSITION;
    public static final int TOUCH_MODE_UP_DRAGGING = 1;
    public static final int TOUCH_MODE_DOWN_DRAGGING = 2;
    private final int TOUCH_MODE_SLIDING = 3;
    private final int TOUCH_MODE_RESET = 4;

    public void onTouchDown(MotionEvent ev) {
        mActionDownX = ev.getX();
        mActionDownY = ev.getY();
        mOverDraggingBias = Float.MAX_VALUE;

        if (mTouchMode == TOUCH_MODE_SLIDING && mPosition != INVALID_POSITION) {

            ev.setAction(MotionEvent.ACTION_CANCEL);

            mTouchMode = TOUCH_MODE_RESET;

            if (DEBUG) Log.e(TAG, "TOUCH_MODE_RESET");
            mScroller.startScroll((int) Math.max(MIN_HORIZONTAL_OFFSET, mOffsetX), 0, -(int) Math.max(MIN_HORIZONTAL_OFFSET, mOffsetX), 0, 250);

            invalidate();
        }
    }

    private int mDeletePosition = INVALID_POSITION;

    OnItemDeleteListener mOnItemDeleteListener;

    OnTouchListener onDeleteTouchEvent = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_DOWN
                    && mTouchMode == TOUCH_MODE_SLIDING) {
                if (mOnItemDeleteListener != null) {
                    mDeletePosition = mPosition + getFirstVisiblePosition();
                }
            }
            return false;
        }
    };

    public interface OnItemDeleteListener {
        void onItemDelete(View view, int position);
    }

    public interface OnRefreshListener {
        boolean onDrag(ListView listView, int mode, int offset);

        void onRefresh(ListView listView, int mode);

        View getRefreshView(ListView listView, int mode);
    }

    public void setFinalY(int offset) {
        if (mOnRefreshListener != null && mRefresh) {
            mScroller.setFinalY(offset);
            mTouchMode = TOUCH_MODE_DEFAULT;
            invalidate();
        }
    }

    boolean mRefresh = false;

    OnRefreshListener mOnRefreshListener;

    public void setOnDragListener(OnRefreshListener l) {
        mOnRefreshListener = l;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener l) {
        mOnItemDeleteListener = l;
    }

    public void onTouchUp(MotionEvent ev) {

        if (mTouchMode == TOUCH_MODE_UP_DRAGGING || mTouchMode == TOUCH_MODE_DOWN_DRAGGING) {

            int offset = (int) (mActionDownY - ev.getY() + mOverDraggingBias) / 2;

            if (mOnRefreshListener != null && mRefresh) {
                mOnRefreshListener.onRefresh(this, mTouchMode);
                if (mTouchMode == TOUCH_MODE_DOWN_DRAGGING)
                    mScroller.startScroll(0, offset, 0, -offset - mOnRefreshListener.getRefreshView(this, mTouchMode).getHeight(), 350);
                else
                    mScroller.startScroll(0, offset, 0, -offset + mOnRefreshListener.getRefreshView(this, mTouchMode).getHeight(), 350);
            } else {
                mScroller.startScroll(0, offset, 0, -offset, 350);
            }

            invalidate();

            mTouchMode = TOUCH_MODE_DEFAULT;
        } else if (mTouchMode == TOUCH_MODE_SLIDING) {

            if (mActionDownX - ev.getX() < 30) {
                mTouchMode = TOUCH_MODE_DEFAULT;
                mScroller.startScroll((int) Math.max(MIN_HORIZONTAL_OFFSET, mOffsetX), 0, -(int) Math.max(MIN_HORIZONTAL_OFFSET, mOffsetX), 0, 250);
            } else {
                mMaxHorizontalOffset = ((ViewGroup) getChildAt(mPosition)).getChildAt(0).getWidth();

                mScroller.startScroll((int) Math.max(MIN_HORIZONTAL_OFFSET, mOffsetX), 0, mMaxHorizontalOffset - (int) Math.max(MIN_HORIZONTAL_OFFSET, mOffsetX), 0, 250);
            }
            invalidate();
        } else if (mTouchMode == TOUCH_MODE_RESET) {
            mTouchMode = TOUCH_MODE_DEFAULT;
        }
    }
}