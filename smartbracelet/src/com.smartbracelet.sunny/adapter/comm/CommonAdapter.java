package com.smartbracelet.sunny.adapter.comm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunny on 2015/11/7.
 * 基础适配器类
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mDatas = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private int layoutId;

    public CommonAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        if (data == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas.clear();
            mDatas.addAll(data);
        }
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHoler holder = getViewHolder(position, convertView, parent);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    private CommonViewHoler getViewHolder(int postion, View convertView, ViewGroup parent) {
        return CommonViewHoler.getViewHolder(mContext, convertView, parent, layoutId, postion);
    }

    public abstract void convert(CommonViewHoler holer, T itemObject);
}
