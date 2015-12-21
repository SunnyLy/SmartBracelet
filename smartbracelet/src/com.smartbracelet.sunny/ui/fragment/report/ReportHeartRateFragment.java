package com.smartbracelet.sunny.ui.fragment.report;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;

/**
 * Created by sunny on 2015/11/12.
 * 每周报告
 */
public class ReportHeartRateFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_heart_rate, null);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
