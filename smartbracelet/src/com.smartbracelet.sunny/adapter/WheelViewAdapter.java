package com.smartbracelet.sunny.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.het.comres.view.wheelview.AbstractWheelTextAdapter;
import com.smartbracelet.sunny.R;

import java.util.ArrayList;
import java.util.List;

public class WheelViewAdapter extends AbstractWheelTextAdapter {
    private List<String> mData = new ArrayList<String>();

    public WheelViewAdapter(Context context, List<String> list) {
        super(context, R.layout.widget_wheelview_item, R.id.tempValue);
        this.mData.clear();
        this.mData.addAll(list);
        setItemTextResource(R.id.tempValue);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return mData.size();
    }

    @Override
    protected CharSequence getItemText(int index) {
        return mData.get(index) + "";
    }
}
