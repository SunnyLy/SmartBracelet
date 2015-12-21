package com.smartbracelet.sunny.adapter.comm;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sunny on 2015/11/7.
 * ViewHolder基类
 */
public class CommonViewHoler {
    private final SparseArray<View> mViews = new SparseArray<>();
    private View mConvertView;
    private int mPosition;

    private CommonViewHoler(Context context, ViewGroup parent, int layoutId, int postion) {
        this.mPosition = postion;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 获取ViewHolder
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static CommonViewHoler getViewHolder(Context context, View convertView,
                                                ViewGroup parent, int layoutId, int position) {
        CommonViewHoler viewHoler = null;
        if (viewHoler == null) {
            viewHoler = new CommonViewHoler(context, parent, layoutId, position);
        } else {
            viewHoler = (CommonViewHoler) convertView.getTag();
            viewHoler.mPosition = position;
        }

        return viewHoler;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 根据指定的id获取控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 对textView设置文字
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonViewHoler setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public CommonViewHoler setText(int viewId, String text, int color) {
        TextView textView = getView(viewId);
        textView.setText(text);
        textView.setTextColor(color);
        return this;
    }

    public CommonViewHoler setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    public CommonViewHoler setImageBitmap(int viewId, Bitmap bm) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bm);
        return this;
    }

    public CommonViewHoler setVisibility(int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }

    public int getPostion() {
        return mPosition;
    }
}
