package com.smartbracelet.sunny.ui.help;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.adapter.HelpViewAdapter;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.ui.fragment.help.HelpLearnFragment;
import com.smartbracelet.sunny.ui.fragment.help.HelpNewerFragment;
import com.smartbracelet.sunny.ui.fragment.help.HelpSolveFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 2015/11/6.
 * Annotion:用户帮助
 */
public class HelpActivity extends BaseActivity {
    public static final String TAG = HelpActivity.class.getSimpleName();

    @InjectView(R.id.common_top_bar)
    CommonTopBar mCommonTop;
    @InjectView(R.id.help_content)
    //   ViewPager mViewPager;
            View mFragment;
    @InjectView(R.id.help_learn)
    Button mBtnLearn;
    @InjectView(R.id.help_newer)
    Button mBtnNewer;
    @InjectView(R.id.help_solve_problem)
    Button mBtnSolve;

    private Button[] btns;
    private Fragment[] fragments;

    //fragment
    private HelpLearnFragment helpLearnFragment;
    private HelpNewerFragment helpNewerFragment;
    private HelpSolveFragment helpSolveFragment;

    public static void startHelpActivity(Context context) {
        Intent targetIntent = new Intent(context, HelpActivity.class);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    @Override
    public void onContentChanged() {
        mFragment = findViewById(R.id.help_content);
    }

    @Override
    public void initParams() {
        btns = new Button[]{mBtnLearn, mBtnNewer, mBtnSolve};
        initFragment();
    }


    private void initFragment() {
        helpLearnFragment = new HelpLearnFragment();
        helpNewerFragment = new HelpNewerFragment();
        helpSolveFragment = new HelpSolveFragment();
        fragments = new Fragment[]{helpLearnFragment, helpNewerFragment, helpSolveFragment};
        changeView(0);
    }

    @Override
    public void initTitleBar() {
        mCommonTop.setTitle(R.string.help);
        mCommonTop.setUpNavigateMode();
    }

    @OnClick({R.id.help_learn, R.id.help_solve_problem, R.id.help_newer})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.help_learn:
                changeView(0);
                break;
            case R.id.help_newer:
                changeView(1);
                break;
            case R.id.help_solve_problem:
                changeView(2);
                break;
        }
    }

    private void changeView(int position) {
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
                mFragmentTranscation.replace(R.id.help_content, helpLearnFragment);
                break;
            case 1:
                mFragmentTranscation.replace(R.id.help_content, helpNewerFragment);
                break;
            case 2:
                mFragmentTranscation.replace(R.id.help_content, helpSolveFragment);
                break;
        }
        mFragmentTranscation.commit();

    }
}
