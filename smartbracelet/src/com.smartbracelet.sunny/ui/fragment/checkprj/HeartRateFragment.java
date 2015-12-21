package com.smartbracelet.sunny.ui.fragment.checkprj;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.het.common.callback.ICallback;
import com.het.common.utils.LogUtils;
import com.het.comres.view.dialog.CommonToast;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.biz.api.BloodPressureApi;
import com.smartbracelet.sunny.biz.api.HeartRateApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.TimeBloodPressure;
import com.smartbracelet.sunny.model.event.BaseEvent;
import com.smartbracelet.sunny.model.event.HeartPressureEvent;
import com.smartbracelet.sunny.ui.widget.view.DivisionCircle2;
import com.smartbracelet.sunny.utils.Json2Model;
import com.smartbracelet.sunny.utils.SunnyChartHelp;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
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
public class HeartRateFragment extends BaseFragment {

    @InjectView(R.id.my_breath_icon)
    ImageView mState;
    @InjectView(R.id.my_breath_result_icon)
    ImageView mTestResult;
    @InjectView(R.id.heart_rate_value)
    DivisionCircle2 mHeartRateValue;

    @InjectView(R.id.layout_result)
    LinearLayout mLayoutResult;
    @InjectView(R.id.indicate_arrow)
    ImageView mArrow;
    @InjectView(R.id.test_result_show)
    LinearLayout mResultShow;
    private boolean isUp = true;

    private String mTestValue;
    private String mTestTime;
    private String mUserId;
    private UserManager mUserManager;


    @InjectView(R.id.heart_rate_chart_data)
    ColumnChartView mColumnChartView;


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


    public String getTestValue() {
        return mTestValue;
    }

    public void setTestValue(String mTestValue) {
        this.mTestValue = mTestValue;
    }

    public String getTestTime() {
        return mTestTime;
    }

    public void setTestTime(String mTestTime) {
        this.mTestTime = mTestTime;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_heart_rate, null);
        ButterKnife.inject(this, view);
        EventBus.getDefault().register(this);
        initParams();
        return view;
    }

    private void initParams() {
        mState.setImageResource(R.mipmap.icon_my_heartrate_state);
        mTestResult.setImageResource(R.mipmap.icon_my_heartrate_result);
        mUserManager = UserManager.getInstance();
        mUserId = mUserManager.getUserModel() == null ? "1" : mUserManager.getUserModel().getUserID();

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
        LogUtils.e("HeartRateFragment,onResume====");
        mHeartRateValue.setCurrentValue(TextUtils.isEmpty(getTestValue()) ? 0 : Integer.valueOf(getTestValue()));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.e("HeartRateFragment,onHiddenChanged========");
    }

    @OnClick({R.id.layout_result})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_result:

                if (isUp) {
                    mArrow.setImageResource(R.mipmap.icon_indicator_arrow_down);
                    mResultShow.setVisibility(View.VISIBLE);
                    isUp = false;
                } else {
                    mArrow.setImageResource(R.mipmap.icon_indicator_arrow_up);
                    mResultShow.setVisibility(View.GONE);
                    isUp = true;
                }

                break;
        }
    }


    public void onEventMainThread(HeartPressureEvent event) {
        Object object = event.object;
        parseJsonObject(object);

    }

    public void onEventMainThread(BaseEvent event) {
        BaseEvent.EventType type = event.getEventType();
        if (type == BaseEvent.EventType.HEART_RATE) {
        }
    }


    private void parseJsonObject(Object o) {
        if (o == null) {
            setEmptyView();
        } else {
            List<TimeBloodPressure> timeBloodPressureList = Json2Model.parseJsonToList((String) o,
                    "heardPressure", TimeBloodPressure.class);
            generateDefaultData(timeBloodPressureList);
        }
    }

    private void setEmptyView() {
        data.setValueEmpty(mChartHelp.defNoData(true));
    }

    /**
     * 生成测试数据
     */
    private void generateDefaultData(List<TimeBloodPressure> dataes) {
        int numSubcolumns = 1;
        int numColumns = 0;
        if (dataes == null || dataes.size() == 0) {
            setEmptyView();
        } else {
            numColumns = dataes.size();
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
            SunnyChartHelp chartHelp = new SunnyChartHelp(mContext);
            data.setValueEmpty(chartHelp.defNoData(true));

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("时间");
                    axisY.setName("心率");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            mColumnChartView.setColumnChartData(data);
        }
    }
}
