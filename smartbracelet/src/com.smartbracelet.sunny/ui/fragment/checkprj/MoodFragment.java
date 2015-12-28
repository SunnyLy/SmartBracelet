package com.smartbracelet.sunny.ui.fragment.checkprj;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.het.common.callback.ICallback;
import com.het.common.utils.LogUtils;
import com.het.comres.view.dialog.CommonToast;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.biz.api.MoodApi;
import com.smartbracelet.sunny.biz.api.StepApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.MoodModel;
import com.smartbracelet.sunny.model.StepModel;
import com.smartbracelet.sunny.model.TimeStepModel;
import com.smartbracelet.sunny.model.event.BaseEvent;
import com.smartbracelet.sunny.model.event.MoodEvent;
import com.smartbracelet.sunny.model.event.StepEvent;
import com.smartbracelet.sunny.ui.widget.EmotionStateLayout;
import com.smartbracelet.sunny.utils.Json2Model;
import com.smartbracelet.sunny.utils.SunnyChartHelp;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by sunny on 2015/11/28.
 * 心情界面
 */
public class MoodFragment extends BaseFragment {

    private String mTestValue;
    private String mTestTime;
    private UserManager mUserManger;
    private String mUserId;

    @InjectView(R.id.my_emotion_layout)
    EmotionStateLayout mEmotionStateLayout;
    @InjectView(R.id.my_emotion_state)
    TextView mEmotionResult;
    @InjectView(R.id.emotion_state_result)
    TextView mEmotionInfoTitle;
    @InjectView(R.id.emotion_state_anlyze)
    TextView mEmotionInfoResult;
    @InjectView(R.id.emotion_state_idicate_info)
    TextView mEmotionTips;


    public void setmTestValue(String mTestValue) {
        this.mTestValue = mTestValue;
    }

    public void setmTestTime(String mTestTime) {
        this.mTestTime = mTestTime;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_emotion, null);
        ButterKnife.inject(this, view);
        EventBus.getDefault().register(this);
        initParams();
        return view;
    }

    private void initParams() {
        mUserManger = UserManager.getInstance();
        mUserId = mUserManger.getUserModel() == null ? "1" : mUserManger.getUserModel().getUserID();
        mEmotionStateLayout.setEmotionState(EmotionStateLayout.EmotionState.HAPPY);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void getMood() {

        /**
         * 获取情绪信息
         */
        new MoodApi().getMood(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {
                hideDialog();
                parseJson(o);
            }

            @Override
            public void onFailure(int code, String msg, int id) {
                handleFailure(code, msg);
            }
        }, mUserId);
    }

    private void handleFailure(int code, String msg) {
        hideDialog();
        CommonToast.showToast(mContext, msg);
    }

    /**
     * 刷新界面
     * attentions:
     * 1，这个接口是有问题的，情绪最好是返回一个int，然后对号入座
     * 2，这个文案不全，最好是由产品经理给出，
     * 3，由于时间的关系，我这边就没有去跟这个问题，希望公司ios开发能提出
     *
     * @param moodModel
     */
    private void freshUI(MoodModel moodModel) {

        String mood = moodModel.getMood();
        if(TextUtils.isEmpty(mood)){
            mood = getString(R.string.mood_normal);
        }
        mEmotionResult.setText(mood);
        if(mood.contains(getString(R.string.mood_normal))){
            mEmotionInfoTitle.setText(getString(R.string.mood_normal));
        }


    }

    private void parseJson(Object o) {
        if (o != null) {

            MoodModel moodModel = Json2Model.parseJson((String) o, MoodModel.class);
            freshUI(moodModel);
        }
    }


    public void onEventMainThread(MoodEvent event) {
        Object object = event.object;
        parseJson(object);

    }

    public void onEventMainThread(BaseEvent event) {
        BaseEvent.EventType type = event.getEventType();
        if (type == BaseEvent.EventType.MOOD) {
            //当fragment重新show时，请求网络
            getMood();
        }
    }

}
