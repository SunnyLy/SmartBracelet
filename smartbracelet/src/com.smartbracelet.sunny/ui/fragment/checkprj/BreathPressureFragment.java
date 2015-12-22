package com.smartbracelet.sunny.ui.fragment.checkprj;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.het.common.callback.ICallback;
import com.het.common.utils.LogUtils;
import com.het.comres.view.dialog.CommonToast;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.biz.api.BloodPressureApi;
import com.smartbracelet.sunny.biz.api.BreathRateApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.TimeBloodPressure;
import com.smartbracelet.sunny.model.event.BaseEvent;
import com.smartbracelet.sunny.model.event.BreathPressureEvent;
import com.smartbracelet.sunny.ui.widget.view.DivisionCircle;
import com.smartbracelet.sunny.utils.Json2Model;
import com.smartbracelet.sunny.utils.SunnyChartHelp;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by sunny on 2015/11/28.
 */
public class BreathPressureFragment extends BaseFragment {

    @InjectView(R.id.my_breath_icon)
    ImageView mBreathState;
    @InjectView(R.id.rl_item_test_result)
    RelativeLayout mItemTestResult;
    @InjectView(R.id.breath_division_circle)
    DivisionCircle mBreathDivisionCircle;

    private String mTestValue;
    private String mTestTime;
    private String mUserId;
    private UserManager mUserManager;

    //下面是图表参数========================
    @InjectView(R.id.blooth_line_chart_data)
    LineChartView mLineChartView;
    private LineChartData data;
    private int numberOfLines = 2;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 24;
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
    private List<AxisValue> xAxisValues = new ArrayList<>();
    private String[] xAxisLables = new String[]{"00:00", "01:00", "02:00",
            "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
            "21:00", "22:00", "23:00",};
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.RING;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private int[] lineColors = new int[]{0xFFB44A5A, 0xFF7EE9BE};

    private SunnyChartHelp mChartHelp;
    //图表参数结束================================


    public void setmTestValue(String mTestValue) {
        this.mTestValue = mTestValue;
    }

    public void setmTestTime(String mTestTime) {
        this.mTestTime = mTestTime;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_breath, null);
        ButterKnife.inject(this, view);
        EventBus.getDefault().register(this);
        initParams();
        return view;
    }

    private void initParams() {
        mItemTestResult.setVisibility(View.INVISIBLE);
        mBreathState.setImageResource(R.mipmap.icon_my_bloothpressure_step);
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
        mBreathDivisionCircle.setCircleX(300);
        mBreathDivisionCircle.setCircleY(300);
        mBreathDivisionCircle.setmRadius(200);
        mBreathDivisionCircle.setmTime(mTestTime);
        mBreathDivisionCircle.setmValue(TextUtils.isEmpty(mTestValue) ? 20 : Integer.valueOf(mTestValue));
        mBreathDivisionCircle.invalidate();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.e("BreathPressureFragment,onHiddenChanged========");
    }


    public void onEventMainThread(BreathPressureEvent event) {
        Object object = event.object;
        parseJsonObject(object);

    }

    public void onEventMainThread(BaseEvent event) {
        BaseEvent.EventType type = event.getEventType();
        if (type == BaseEvent.EventType.BREATH) {
        }
    }

    private void parseJsonObject(Object o) {
        if (o == null) {
            setEmptyView();
        } else {
            List<TimeBloodPressure> timeBloodPressureList = Json2Model.parseJsonToList((String) o, "bloodPressuress", TimeBloodPressure.class);
            getData(timeBloodPressureList);
        }
    }

    private void setEmptyView() {
        data.setValueEmpty(mChartHelp.defNoData(true));
    }

    private void getData(List<TimeBloodPressure> data) {

        if (data != null && data.size() > 0) {
            numberOfPoints = data.size();
            // Generate some randome values.
            generateValues();
            generateData();
            // Disable viewpirt recalculations, see toggleCubic() method for more info.
            mLineChartView.setViewportCalculationEnabled(false);
            resetViewport();
        } else {
            setEmptyView();
        }


    }

    private void generateValues() {
        xAxisValues.clear();
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }

        for (int j = 0; j < numberOfPoints; ++j) {
            //X轴坐标值
            xAxisValues.add(new AxisValue(j).setLabel(xAxisLables[j]));
        }
    }

    private void generateData() {
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            line.setColor(lineColors[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis().setHasLines(true)
                    .setMaxLabelChars(0)//来控制X轴上文字显示
                    .setHasTiltedLabels(true)
                    .setValues(xAxisValues);
            Axis axisY = new Axis().setHasLines(false);
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

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        mLineChartView.setLineChartData(data);

    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(mLineChartView.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        mLineChartView.setMaximumViewport(v);
        mLineChartView.setCurrentViewport(v);
    }
}
