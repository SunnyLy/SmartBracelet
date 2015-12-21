package com.smartbracelet.sunny.ui.fragment.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.adapter.HelpViewAdapter;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.ui.widget.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sunny on 2015/11/10.
 * 新手上路
 */
public class HelpNewerFragment extends BaseFragment {

    @InjectView(R.id.newer_vp)
    ViewPager mViewPager;
    @InjectView(R.id.newer_indicator)
    ViewPagerIndicator mIndicator;

    private HelpViewAdapter mViewAdapter;
    private List<View> viewList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_help_newer, null);
        ButterKnife.inject(this, content);
        initParams();
        return content;
    }

    private void initParams() {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view2 = mInflater.inflate(R.layout.adapter_help_newer2, null);
        View view3 = mInflater.inflate(R.layout.adapter_help_newer3, null);
        View view4 = mInflater.inflate(R.layout.adapter_help_newer4, null);
        View view5 = mInflater.inflate(R.layout.adapter_help_newer5, null);

        viewList.clear();
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        viewList.add(view5);
        mViewAdapter = new HelpViewAdapter(mContext, viewList);
        mViewPager.setAdapter(mViewAdapter);
        mIndicator.setViewPager(mViewPager);
    }
}
