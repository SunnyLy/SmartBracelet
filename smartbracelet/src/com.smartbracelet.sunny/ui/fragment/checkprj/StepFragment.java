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
import com.het.common.utils.StringUtils;
import com.het.comres.view.dialog.CommonToast;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.biz.api.StepApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.StepModel;
import com.smartbracelet.sunny.model.TimeBloodPressure;
import com.smartbracelet.sunny.model.TimeStepModel;
import com.smartbracelet.sunny.model.event.BaseEvent;
import com.smartbracelet.sunny.model.event.BloothPressureEvent;
import com.smartbracelet.sunny.model.event.StepEvent;
import com.smartbracelet.sunny.utils.Json2Model;
import com.smartbracelet.sunny.utils.SunnyChartHelp;

import org.json.JSONException;
import org.json.JSONObject;

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
 */
public class StepFragment extends BaseFragment {

    private String mTestValue;
    private String mTestTime;
    private UserManager mUserManger;
    private String mUserId;

    @InjectView(R.id.motion_step_bargrah)
    ColumnChartView mColumnChartView;

    @InjectView(R.id.step_result)
    TextView mStepResult;
    @InjectView(R.id.step_kilometers_result)
    TextView mKiloMeterResult;
    @InjectView(R.id.step_energy_result)
    TextView mExpandResult;
    @InjectView(R.id.motion_step_grade)
    TextView mScore;
    @InjectView(R.id.motion_step_time)
    TextView mTime;


    //柱形图属性=================================================
    private static final int DEFAULT_DATA = 0;
    private static final int SUBCOLUMNS_DATA = 1;
    private static final int STACKED_DATA = 2;
    private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
    private static final int NEGATIVE_STACKED_DATA = 4;
    private ColumnChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLabels = false;
    private boolean hasLabelForSelected = false;
    private int dataType = DEFAULT_DATA;

    private SunnyChartHelp mChartHelp;
    //柱形图属性结束==============================================

    public void setmTestValue(String mTestValue) {
        this.mTestValue = mTestValue;
    }

    public void setmTestTime(String mTestTime) {
        this.mTestTime = mTestTime;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_motion_step, null);
        ButterKnife.inject(this, view);
        EventBus.getDefault().register(this);
        initParams();
        return view;
    }

    private void initParams() {
        mUserManger = UserManager.getInstance();
        mUserId = mUserManger.getUserModel() == null ? "1" : mUserManger.getUserModel().getUserID();
        mChartHelp = new SunnyChartHelp(mContext);
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


        mStepResult.setText(step);
        mKiloMeterResult.setText(kilometers);
        mExpandResult.setText(expand);
        mTime.setText(time);
        mScore.setText(score);

    }

    private void parseJson(Object o) {
        if (o == null) {
            setEmptyView();
        } else {
            List<TimeStepModel> timeBloodPressureList = Json2Model.parseJsonToList((String) o, "bloodPressuress", TimeStepModel.class);
            generateDefaultData(timeBloodPressureList);
        }
    }

    private void setEmptyView() {
        data.setValueEmpty(mChartHelp.defNoData(true));
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


    /**
     * 生成测试数据
     */
    private void generateDefaultData(List<TimeStepModel> datas) {
        int numSubcolumns = 1;
        int numColumns = 0;
        if (datas != null && datas.size() == 0) {
            numColumns = datas.size();
            // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
                }

                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }

            data = new ColumnChartData(columns);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Axis X");
                    axisY.setName("Axis Y");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            mColumnChartView.setColumnChartData(data);

        } else {
            setEmptyView();
        }

    }
}
