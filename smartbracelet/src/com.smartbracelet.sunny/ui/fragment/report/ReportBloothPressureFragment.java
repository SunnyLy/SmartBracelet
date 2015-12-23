package com.smartbracelet.sunny.ui.fragment.report;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.het.common.callback.ICallback;
import com.het.comres.view.dialog.CommonToast;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.biz.api.HeartRateApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.TimeBloodPressure;
import com.smartbracelet.sunny.model.UserModel;
import com.smartbracelet.sunny.model.event.HeartPressureEvent;
import com.smartbracelet.sunny.utils.DateTime;
import com.smartbracelet.sunny.utils.Json2Model;
import com.smartbracelet.sunny.utils.SunnyChartHelp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * Created by sunny on 2015/11/12.
 * 每周报告
 */
public class ReportBloothPressureFragment extends BaseFragment {

    private UserManager mUserManager;
    private UserModel mUserModel;
    private String mUserId;
    private static final String QUERY_TYPE = "day";

    private long mStartTime;
    private long mEndTime;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_blooth_pressure, null);

        initParams();
        getBloothPressureData();
        return view;
    }

    private void initParams() {
        mUserManager = UserManager.getInstance();
        mUserModel = mUserManager.getUserModel();
        mUserId = mUserModel==null?"1":mUserModel.getUserID();

        long currentDate = System.currentTimeMillis();
        mStartTime = DateTime.getFirstDayOfWeek(new Date(currentDate));
        mEndTime = DateTime.getLastDayOfWeek(new Date(currentDate));
    }

    private void getBloothPressureData() {
        showDialog();
        new HeartRateApi().getHeartPressureByTime(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {
                hideDialog();
               freshUI(o);
            }

            @Override
            public void onFailure(int code, String msg, int id) {
                handleFailure(code, msg);
            }
        }, mUserId, QUERY_TYPE, String.valueOf(mStartTime), String.valueOf(mEndTime));
    }

    /**
     * 刷新界面
     * @param o
     */
    private void freshUI(Object o) {
        if (o == null) {
            setEmptyView();
        } else {
            List<TimeBloodPressure> timeBloodPressureList = Json2Model.parseJsonToList((String) o, "bloodPressuress", TimeBloodPressure.class);
            getData(timeBloodPressureList);
        }
    }

    /**
     * 解析数据
     * @param data
     */
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

    /**
     * 重置
     */
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

    private void setEmptyView() {
        data.setValueEmpty(mChartHelp.defNoData(true));
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void handleFailure(int code, String msg) {
        hideDialog();
        CommonToast.showToast(mContext, msg);
    }

}
