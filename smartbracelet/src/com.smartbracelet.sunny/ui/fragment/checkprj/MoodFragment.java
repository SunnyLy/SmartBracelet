package com.smartbracelet.sunny.ui.fragment.checkprj;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.het.common.callback.ICallback;
import com.het.common.utils.LogUtils;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.biz.api.StepApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.StepModel;
import com.smartbracelet.sunny.model.TimeStepModel;
import com.smartbracelet.sunny.model.event.BaseEvent;
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
    public void onResume() {
        super.onResume();
        LogUtils.e("StepFragment,onResume====");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.e("StepFragment,onHiddenChanged=======");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.e("StepFragment,onStart=======");
    }

    private void getStepData() {

        new StepApi().getStep(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {

                if (o != null) {
                    StepModel stepModel = Json2Model.parseJson((String) o, StepModel.class);
                    freshUI(stepModel);

                }
            }

            @Override
            public void onFailure(int code, String msg, int id) {

            }
        }, mUserId);
    }

    /**
     * 刷新界面
     *
     * @param stepModel
     */
    private void freshUI(StepModel stepModel) {

        String step = stepModel.getStep();
        String kilometers = stepModel.getKilometer();
        String expand = stepModel.getExpand();
        String time = stepModel.getTime();
        String score = stepModel.getScore();

    }

    private void parseJson(Object o) {
        if (o == null) {
        } else {
            List<TimeStepModel> timeBloodPressureList = Json2Model.parseJsonToList((String) o, "bloodPressuress", TimeStepModel.class);
        }
    }


    public void onEventMainThread(StepEvent event) {
        Object object = event.object;
        parseJson(object);

    }

    public void onEventMainThread(BaseEvent event) {
        BaseEvent.EventType type = event.getEventType();
        if (type == BaseEvent.EventType.STEP) {
            //当fragment重新show时，请求网络
            getStepData();
        }
    }

}
