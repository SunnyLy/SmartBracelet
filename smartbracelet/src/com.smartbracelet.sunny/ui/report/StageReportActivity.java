package com.smartbracelet.sunny.ui.report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.ui.fragment.report.ReportBloothPressureFragment;
import com.smartbracelet.sunny.ui.fragment.report.ReportHeartRateFragment;
import com.smartbracelet.sunny.ui.fragment.report.ReportStepFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 2015/11/6.
 * Annotion:阶段报告
 */
public class StageReportActivity extends BaseActivity {
    public static final String TAG = StageReportActivity.class.getSimpleName();

    @InjectView(R.id.common_top_bar)
    CommonTopBar commonTopBar;

    @InjectView(R.id.report_heart_rate)
    Button btnHeartRate;
    @InjectView(R.id.report_blooth_pressure)
    Button btnBloothPressure;
    @InjectView(R.id.report_step)
    Button btnStep;
    private ReportHeartRateFragment reportHeartRateFragment;
    private ReportBloothPressureFragment reportBloothPressureFragment;
    private ReportStepFragment reportStepFragment;
    private Button[] btns;

    public static void startStageReportActivity(Context context) {
        Intent targetIntent = new Intent(context, StageReportActivity.class);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stage_report);
    }

    @Override
    public void initParams() {
        btns = new Button[]{btnHeartRate, btnBloothPressure, btnStep};
        initFragment();
    }

    private void initFragment() {
        reportHeartRateFragment = new ReportHeartRateFragment();
        reportBloothPressureFragment = new ReportBloothPressureFragment();
        reportStepFragment = new ReportStepFragment();
        changeView(0);
    }

    @OnClick({R.id.report_heart_rate, R.id.report_blooth_pressure, R.id.report_step})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.report_heart_rate:
                changeView(0);
                break;
            case R.id.report_blooth_pressure:
                changeView(1);
                break;

            case R.id.report_step:
                changeView(2);
                break;

        }
    }

    private void changeView(int position) {
        try {
            FragmentTransaction mFragmentTranscation = getSupportFragmentManager().beginTransaction();
            mFragmentTranscation.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            for (int i = 0; i < btns.length; i++) {
                int bgColor;
                int textColor = R.color.textcolor_9B;
                if (i == position) {
                    bgColor = R.drawable.help_tab_bg_pressed;
                    textColor = R.color.white;
                } else {
                    bgColor = R.drawable.help_tab_bg_normal;
                    textColor = R.color.textcolor_9B;
                }
                btns[i].setBackgroundResource(bgColor);
                btns[i].setTextColor(getResources().getColor(textColor));
            }

            switch (position) {
                case 0:
                    mFragmentTranscation.replace(R.id.report_content, reportHeartRateFragment);
                    break;
                case 1:
                    mFragmentTranscation.replace(R.id.report_content, reportBloothPressureFragment);
                    break;
                case 2:
                    mFragmentTranscation.replace(R.id.report_content, reportStepFragment);
                    break;
            }
            mFragmentTranscation.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initTitleBar() {
        commonTopBar.setUpNavigateMode();
        commonTopBar.setTitle(R.string.stage_report);
    }
}
