package com.smartbracelet.sunny.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.het.comres.view.layout.ItemLinearLayout;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.model.event.BaseEvent;
import com.smartbracelet.sunny.ui.favorite.MyFavoriteActivity;
import com.smartbracelet.sunny.ui.help.HelpActivity;
import com.smartbracelet.sunny.ui.report.StageReportActivity;
import com.smartbracelet.sunny.ui.set.SettingActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by sunny on 15-11-5 下午11:43
 * 侧边栏
 */
public class SlidingFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    @InjectView(R.id.sliding_setting)
    ItemLinearLayout mLayoutSetting;
    @InjectView(R.id.sliding_help)
    ItemLinearLayout mLayoutHelp;
    @InjectView(R.id.sliding_stage_report)
    ItemLinearLayout mLayoutReport;
    @InjectView(R.id.sliding_my_favorite)
    ItemLinearLayout mLayoutFavorite;
    @InjectView(R.id.cb_bloor_pressure)
    CheckBox mCBBloorPressure;
    @InjectView(R.id.cb_breath_reate)
    CheckBox mCBBreathRate;
    @InjectView(R.id.cb_heart_pressure)
    CheckBox mCBHeartPressure;
    @InjectView(R.id.cb_emotion)
    CheckBox mCBEmotion;
    @InjectView(R.id.cb_tired)
    CheckBox mCBTired;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View slidingView = inflater.inflate(R.layout.fragment_sliding, null);
        ButterKnife.inject(this, slidingView);
        return slidingView;
    }

    @OnClick({R.id.sliding_setting, R.id.sliding_help, R.id.sliding_my_favorite, R.id.sliding_stage_report})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sliding_setting:
                SettingActivity.startSettingActivity(mContext);
                break;
            case R.id.sliding_help:
                HelpActivity.startHelpActivity(mContext);
                break;
            case R.id.sliding_my_favorite:
                MyFavoriteActivity.startSettingActivity(mContext);
                break;
            case R.id.sliding_stage_report:
                StageReportActivity.startStageReportActivity(mContext);
                break;
        }
    }

    @OnCheckedChanged({R.id.cb_bloor_pressure, R.id.cb_tired,
            R.id.cb_emotion, R.id.cb_heart_pressure, R.id.cb_breath_reate})
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.cb_bloor_pressure:
                postEvent(BaseEvent.EventType.BLOOTH_PRESSURE_GONE,
                        BaseEvent.EventType.BLOOTH_PRESSURE_VISIBLE, isChecked);
                break;
            case R.id.cb_tired:
                postEvent(BaseEvent.EventType.TIRED_GONE,
                        BaseEvent.EventType.TIRED_VISIBLE, isChecked);
                break;
            case R.id.cb_emotion:
                postEvent(BaseEvent.EventType.MOOD_GONE,
                        BaseEvent.EventType.MOOD_VISIBLE, isChecked);
                break;
            case R.id.cb_heart_pressure:
                postEvent(BaseEvent.EventType.HEART_RATE_GONE,
                        BaseEvent.EventType.HEART_RATE_VISIBLE, isChecked);
                break;
            case R.id.cb_breath_reate:
                postEvent(BaseEvent.EventType.BREATH_PRESSURE_GONE,
                        BaseEvent.EventType.BREATH_PRESSURE_VISIBLE, isChecked);
                break;
        }
    }

    private void postEvent(BaseEvent.EventType eventTypeGone, BaseEvent.EventType eventTypeVisible,
                           boolean isChecked) {
        BaseEvent baseEvent = new BaseEvent();
        if (!isChecked) {
            baseEvent.setEventType(eventTypeGone);
        } else {
            baseEvent.setEventType(eventTypeVisible);
        }
        EventBus.getDefault().post(baseEvent);
    }
}
