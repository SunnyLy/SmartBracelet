package com.smartbracelet.sunny.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.smartbracelet.sunny.adapter.comm.CommonAdapter;
import com.smartbracelet.sunny.adapter.comm.CommonViewHoler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunny on 2015/11/10.
 * viewpager 适配器
 */
public class HelpViewAdapter extends PagerAdapter {
    private List<View> fragmentList = new ArrayList<>();

    public HelpViewAdapter(Context context, List<View> list) {
        if (list != null && list.size() > 0) {
            fragmentList.clear();
            fragmentList.addAll(list);
        }
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(fragmentList.get(position));
        return fragmentList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(fragmentList.get(position));
    }
}
