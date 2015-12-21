package com.smartbracelet.sunny.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.text.TextUtils;

import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.model.BaseAvg;
import com.smartbracelet.sunny.model.LineAvg;
import com.smartbracelet.sunny.model.LinePoint;
import com.smartbracelet.sunny.model.SkinCheckResultModelForQuery;
import com.smartbracelet.sunny.model.WaterOilResult;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.NoData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 描述：绘制图表帮助类
 *
 * @Author sunny
 * @Date 2015/12/02
 */
public class SunnyChartHelp {
    private Context mContext;
    protected List<Integer> curvecolor = new ArrayList<Integer>();

    public SunnyChartHelp(Context context) {
        super();
        this.mContext = context;
        curvecolor.add(mContext.getResources().getColor(R.color.cb_test_analysis_chart2));
        curvecolor.add(mContext.getResources().getColor(R.color.cb_test_analysis_chart4));
        curvecolor.add(mContext.getResources().getColor(R.color.cb_test_analysis_chart10));
    }

    /**
     * <p>描述:画一条默认的看不见的线</p>
     *
     * @param range
     * @return Line
     */
    public Line defLine(int range) {
        List<PointValue> values = new ArrayList<PointValue>();
        for (int j = 0; j < range; ++j) {
            values.add(new PointValue(j, 40));
        }
        Line line = new Line(values);
        line.setColor(Color.TRANSPARENT);
        line.setShape(ValueShape.CIRCLE);
        line.setAreaTransparency(0);//area透明度
        line.setCubic(true);
        line.setFilled(true);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setStrokeWidth(2);
        line.setHasPoints(true);
        return line;
    }

    /**
     * <p>描述:默认画虚线的配置</p>
     *
     * @return Line
     */
    public Line defDottedLine() {
        Line line = new Line();
        line.setFilled(false);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setStrokeWidth(2);
        line.setHasPoints(false);
        PathEffect effects = new DashPathEffect(new float[]{10, 10, 10, 10}, 1);
        line.setPathEffect(effects);
        return line;
    }

    /**
     * <p>描述:默认画光滑曲线的配置</p>
     *
     * @return Line
     */
    public Line defCurveLine() {
        Line line = new Line();
        line.setShape(ValueShape.RING);
        line.setCubic(true);
        line.setFilled(true);
        line.setHasLabelsOnlyForSelected(false);
        line.setHasLines(true);
        line.setStrokeWidth(2);
        line.setHasPoints(true);
        return line;
    }

    /**
     * <p>描述:配置没有数据时显示</p>
     *
     * @param isVisible true 显示  false不显示
     */
    public NoData defNoData(boolean isVisible) {
        return new NoData().setContent(mContext.getString(R.string.cb_chart_nodata)).setTextColor(Color.RED)
                .setTextSize(18).setStrokeWidth(2).setVisible(isVisible);
    }

    /**
     * <p>描述:水分&油分&弹性平均值</p>
     *
     * @param avg
     * @return 设定文件
     */
    private List<LineAvg> getAvgLineData(BaseAvg avg) {
        List<LineAvg> avgList = new ArrayList<LineAvg>();
        if (!TextUtils.isEmpty(avg.getAvgWater())) {
            float water_value = Float.parseFloat(avg.getAvgWater());
            if (water_value >= 0.1f)
                avgList.add(new LineAvg(water_value, mContext.getResources().getColor(R.color.cb_test_analysis_chart1)));
        }
        if (!TextUtils.isEmpty(avg.getAvgOil())) {
            float oil_value = Float.parseFloat(avg.getAvgOil());
            if (oil_value >= 0.1f)
                avgList.add(new LineAvg(oil_value, mContext.getResources().getColor(R.color.cb_test_analysis_chart3)));
        }
        if (!TextUtils.isEmpty(avg.getAvgElasticity())) {
            float elast_value = Float.parseFloat(avg.getAvgElasticity());
            if (elast_value >= 0.1f)
                avgList.add(new LineAvg(elast_value, mContext.getResources().getColor(R.color.cb_test_analysis_chart9)));
        }
        return avgList;
    }

    /**
     * <p>描述:肤质 水油&弹性 分别每条线所有点组成的集合</p>
     *
     * @param skinModel
     * @return
     */
    public List<List<LinePoint>> lineSkinTestPointsListNew(SkinCheckResultModelForQuery skinModel) {
        //解析数据
        if (skinModel == null) return new ArrayList<List<LinePoint>>();
        List<SkinCheckResultModelForQuery.SkinTestRecord> records = skinModel.getSkinMeasureRec();
        List<List<LinePoint>> listlines = new ArrayList<List<LinePoint>>();
        if (records != null && records.size() > 0) {
            List<LinePoint> waterPoints = new ArrayList<LinePoint>();//水分点集合
            List<LinePoint> oilPoints = new ArrayList<LinePoint>();//油分点集合
            List<LinePoint> elasticityPoints = new ArrayList<LinePoint>();//弹性点集合
            for (int j = 0; j < records.size(); j++) {
                SkinCheckResultModelForQuery.SkinTestRecord checkRecord = records.get(j);
                if (TextUtils.isEmpty(checkRecord.getMeasureTime())) continue;
                //utc时间转为成本地时间
                String utc2Local = DateTime.utc2Local(checkRecord.getMeasureTime(), DateTime.DEFYMDHMS, DateTime.DEFYMDHMS);
                String localtime = DateTime.getStringByFormat(utc2Local, DateTime.DEFYMDHMS, DateTime.DEFHMS);
                if (TextUtils.isEmpty(localtime)) continue;

                if (!TextUtils.isEmpty(checkRecord.getWater()))
                    waterPoints.add(new LinePoint(localtime,
                            Float.parseFloat(checkRecord.getWater()), utc2Local));
                if (!TextUtils.isEmpty(checkRecord.getOil()))
                    oilPoints.add(new LinePoint(localtime, Float.parseFloat(checkRecord.getOil()), utc2Local));
                if (!TextUtils.isEmpty(checkRecord.getElasticity()))
                    elasticityPoints.add(new LinePoint(localtime,
                            Float.parseFloat(checkRecord.getElasticity()), utc2Local));
            }
            listlines.add(waterPoints);
            listlines.add(oilPoints);
            listlines.add(elasticityPoints);
        }
        return listlines;
    }

    /**
     * <p>描述:水油&弹性 分别每条线所有点组成的集合</p>
     *
     * @param mRecord
     * @return List<List<LinePoint>>
     */
    public List<List<LinePoint>> lineWaterOilPointsListNew(WaterOilResult mRecord) {
        //解析数据
        if (mRecord == null) return new ArrayList<List<LinePoint>>();
        List<WaterOilResult.WaterOilCheckRecord> checkRecords = mRecord.getMeasureRec();
        List<List<LinePoint>> listlines = new ArrayList<List<LinePoint>>();
        if (checkRecords != null && checkRecords.size() > 0) {
            List<LinePoint> waterPoints = new ArrayList<LinePoint>();//水分点集合
            List<LinePoint> oilPoints = new ArrayList<LinePoint>();//油分点集合
            List<LinePoint> elasticityPoints = new ArrayList<LinePoint>();//弹性点集合
            for (int j = 0; j < checkRecords.size(); j++) {
                WaterOilResult.WaterOilCheckRecord checkRecord = checkRecords.get(j);
                //utc时间转为成本地时间
                String utc2Local = DateTime.utc2Local(checkRecord.getMeasureTime(), DateTime.DEFYMDHMS, DateTime.DEFYMDHMS);
                String localtime = DateTime.getStringByFormat(utc2Local, DateTime.DEFYMDHMS, DateTime.DEFHMS);
                if (TextUtils.isEmpty(localtime)) continue;

                if (!TextUtils.isEmpty(checkRecord.getOil()))
                    waterPoints.add(new LinePoint(localtime, Float.parseFloat(checkRecord.getWater()), utc2Local));
                if (!TextUtils.isEmpty(checkRecord.getWater()))
                    oilPoints.add(new LinePoint(localtime, Float.parseFloat(checkRecord.getOil()), utc2Local));
                if (!TextUtils.isEmpty(checkRecord.getElasticity()))
                    elasticityPoints.add(new LinePoint(localtime, Float.parseFloat(checkRecord.getElasticity()), utc2Local));
            }
            listlines.add(waterPoints);
            listlines.add(oilPoints);
            listlines.add(elasticityPoints);
        }
        return listlines;
    }

    /**
     * <p>描述:绘制 日  线图</p>
     *
     * @param mLineChartView
     * @param avg            chat控件
     * @param listlines
     */
    public void generateDayData(LineChartView mLineChartView, BaseAvg avg, List<List<LinePoint>> listlines) {
        int xtempoRange = 26;
        int ytempoRange = 125;
        boolean isVisible = true;
        final List<Line> lines = new ArrayList<Line>();
        lines.add(defLine(xtempoRange));
        if (avg != null) {//封装数据
            List<LineAvg> avgValue = this.getAvgLineData(avg);
            //画肌肤水分&油分&弹性平均线
            for (int i = 0; i < avgValue.size(); i++) {
                final LineAvg lineAvg = avgValue.get(i);
                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j < xtempoRange; ++j) {
                    values.add(new PointValue(j, lineAvg.avg));
                }
                Line line = this.defDottedLine();
                line.setColor(lineAvg.color);
                line.setValues(values);
                lines.add(line);
                isVisible = false;
            }

            //画曲线
            for (int i = 0; i < listlines.size(); i++) {
                List<PointValue> values = new ArrayList<PointValue>();
                List<LinePoint> ponitlines = listlines.get(i);
                for (int j = 0; j < ponitlines.size(); ++j) {
                    values.add(new PointValue(parseTime(ponitlines.get(j).pointX), ponitlines.get(j).pointY));
                }
                Line line = this.defCurveLine();
                line.setColor(this.curvecolor.get(i));
                line.setValues(values);
                lines.add(line);
                isVisible = false;
            }
        }

        //画x坐标轴
        List<AxisValue> xaxisValues = new ArrayList<AxisValue>();
        for (float i = 0; i < xtempoRange; i += 4) {
            xaxisValues.add(new AxisValue(i).setLabel(formatMinutes(i)));
        }
        //画y坐标轴
        List<AxisValue> yaxisValues = new ArrayList<AxisValue>();
        for (float i = 0; i < ytempoRange; i += 25) {
            yaxisValues.add(new AxisValue(i));
        }

        LineChartData data = new LineChartData(lines);
        data.setValueEmpty(this.defNoData(isVisible));
        Axis axisX = new Axis(xaxisValues).setHasLines(true).setTextColor(Color.parseColor("#999999"));//颜色
        Axis axisY = new Axis(yaxisValues).setHasLines(true).setMaxLabelChars(4).setTextColor(Color.parseColor("#999999"));
        axisY.setFormatter(new SimpleAxisValueFormatter().setAppendedText("%".toCharArray()));
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        mLineChartView.setLineChartData(data);

        mLineChartView.setViewportCalculationEnabled(false);

        Viewport v = mLineChartView.getMaximumViewport();
        v.set(v.left, ytempoRange - 25, v.right, 0);
        mLineChartView.setMaximumViewport(v);
        mLineChartView.setCurrentViewport(v);
        mLineChartView.setZoomType(ZoomType.HORIZONTAL);
        mLineChartView.startDataAnimation();
    }

    /**
     * <p>描述:绘制 月  线图</p>
     *
     * @param mLineChartView chat控件
     * @param avg            平均线
     * @param startTime      起始时间
     * @param listlines      曲线集合
     */
    public void generateMonthData(LineChartView mLineChartView, BaseAvg avg, String startTime, List<List<LinePoint>> listlines) {
        final String monthlastday = DateTime.getDate(DateTime.DEFYMD, DateTime.getLastDayOfMonth(DateTime.getDateByFormat(startTime, DateTime.DEFYMD), 0).getTime());
        final String day = DateTime.getStringByFormat(DateTime.getDateByFormat(monthlastday, DateTime.DEFYMD), "dd");
        final int xtempoRange = Integer.parseInt(day);
        final int ytempoRange = 125;
        boolean isVisible = true;
        final List<Line> lines = new ArrayList<Line>();
        lines.add(defLine(xtempoRange + 2));
        if (avg != null) {//封装数据
            List<LineAvg> avgValue = this.getAvgLineData(avg);
            //画肌肤水分&油分&弹性平均线
            for (int i = 0; i < avgValue.size(); i++) {
                final LineAvg lineAvg = avgValue.get(i);
                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 1; j < xtempoRange + 2; ++j) {
                    values.add(new PointValue(j, lineAvg.avg));
                }
                Line line = this.defDottedLine();
                line.setColor(lineAvg.color);
                line.setValues(values);
                lines.add(line);
                isVisible = false;
            }

            for (int i = 0; i < listlines.size(); i++) {
                List<PointValue> values = new ArrayList<PointValue>();
                List<LinePoint> ponitlines = listlines.get(i);
                for (int j = 0; j < ponitlines.size(); ++j) {
                    //int index = DateTime.dayForMonth(ponitlines.get(j).pointX);
                    int index = DateTime.dayForMonth(ponitlines.get(j).measureTime);
                    values.add(new PointValue(index, ponitlines.get(j).pointY));
                }
                Line line = this.defCurveLine();
                line.setColor(this.curvecolor.get(i));
                line.setValues(values);
                lines.add(line);
                isVisible = false;
            }
        }

        List<AxisValue> xaxisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < 26; i += 5) {//日
            xaxisValues.add(new AxisValue(i));
        }
        xaxisValues.add(new AxisValue(xtempoRange));//结束日期

        List<AxisValue> yaxisValues = new ArrayList<AxisValue>();
        for (float i = 0; i < ytempoRange; i += 25) {//百分比
            yaxisValues.add(new AxisValue(i)/*.setLabel("")*/);
        }

        LineChartData data = new LineChartData(lines);
        data.setValueEmpty(this.defNoData(isVisible));
        Axis axisX = new Axis(xaxisValues).setHasLines(true).setTextColor(Color.parseColor("#999999"));//颜色
        Axis axisY = new Axis(yaxisValues).setHasLines(true).setMaxLabelChars(4).setTextColor(Color.parseColor("#999999"));
        axisY.setFormatter(new SimpleAxisValueFormatter().setAppendedText("%".toCharArray()));
        axisY.setInside(false);
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        mLineChartView.setLineChartData(data);

        mLineChartView.setViewportCalculationEnabled(false);

        Viewport v = mLineChartView.getMaximumViewport();
        v.set(v.left, ytempoRange - 25, v.right, 0);
        mLineChartView.setMaximumViewport(v);
        mLineChartView.setCurrentViewport(v);
        mLineChartView.setZoomType(ZoomType.HORIZONTAL);
        mLineChartView.startDataAnimation();
    }

    /**
     * <p>描述:绘制 周  线图</p>
     *
     * @param mLineChartView chat控件
     * @param avg
     * @param startTime      起始时间
     * @param listlines      曲线集合
     */
    public void generateWeekData(LineChartView mLineChartView, BaseAvg avg, String startTime, List<List<LinePoint>> listlines) {
        final List<String> weeks = DateTime.dateToWeek(startTime, "MM-dd");
        final int xRange = weeks.size();
        final int ytempoRange = 125;
        boolean isVisible = true;
        final List<Line> lines = new ArrayList<Line>();
        lines.add(defLine(xRange));
        if (avg != null) {//封装数据
            List<LineAvg> avgValue = this.getAvgLineData(avg);
            //画肌肤水分&油分&弹性平均线
            for (int i = 0; i < avgValue.size(); i++) {
                final LineAvg lineAvg = avgValue.get(i);
                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j < xRange; ++j) {
                    values.add(new PointValue(j, lineAvg.avg));
                }
                Line line = this.defDottedLine();
                line.setColor(lineAvg.color);
                line.setValues(values);
                lines.add(line);
                isVisible = false;
            }


            //获取解析数据
            for (int i = 0; i < listlines.size(); i++) {
                List<PointValue> values = new ArrayList<PointValue>();
                List<LinePoint> ponitlines = listlines.get(i);
                for (int j = 0; j < ponitlines.size(); ++j) {
                    //int index = DateTime.dayForWeek(ponitlines.get(j).pointX);
                    int index = DateTime.dayForWeek(ponitlines.get(j).measureTime);
                    values.add(new PointValue(index - 1, ponitlines.get(j).pointY));
                }
                Line line = this.defCurveLine();
                line.setColor(curvecolor.get(i));
                line.setValues(values);
                lines.add(line);
                isVisible = false;
            }
        }

        List<AxisValue> xaxisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < xRange; i++) {
            xaxisValues.add(new AxisValue(i).setLabel(weeks.get(i) + " "));
        }

        List<AxisValue> yaxisValues = new ArrayList<AxisValue>();
        for (float i = 0; i < ytempoRange; i += 25) {
            yaxisValues.add(new AxisValue(i));
        }

        LineChartData data = new LineChartData(lines);
        data.setValueEmpty(this.defNoData(isVisible));
        Axis axisX = new Axis(xaxisValues).setHasLines(true).setTextColor(Color.parseColor("#999999"));//颜色
        axisX.setInside(false);
        //axisX.setHasTiltedLabels(true);//下标倾斜
        Axis axisY = new Axis(yaxisValues).setHasLines(true).setMaxLabelChars(4).setTextColor(Color.parseColor("#999999"));
        axisY.setFormatter(new SimpleAxisValueFormatter().setAppendedText("%".toCharArray()));
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        data.setLabelXY(10);
        mLineChartView.setLineChartData(data);

        mLineChartView.setViewportCalculationEnabled(false);

        Viewport v = mLineChartView.getMaximumViewport();
        v.set(v.left, ytempoRange - 25, v.right, 0);
        mLineChartView.setMaximumViewport(v);
        mLineChartView.setCurrentViewport(v);
        mLineChartView.setZoomType(ZoomType.HORIZONTAL);
        mLineChartView.startDataAnimation();
    }

    private String formatMinutes(float value) {
        StringBuilder sb = new StringBuilder();
        // translate value to seconds, for example
        int valueInSeconds = (int) (value * 60);
        int minutes = (int) Math.floor(valueInSeconds / 60);
        int seconds = valueInSeconds % 60;

        sb.append(String.valueOf(minutes)).append(':');
        if (seconds < 10) {
            sb.append('0');
        }
        sb.append(String.valueOf(seconds));
        return sb.toString();
    }

    private float parseTime(String timeStr) {
        try {
            String time[] = timeStr.split(":");
            if (time.length > 0) {
                float decimal = Float.parseFloat(time[1]) * (100 / 60) / 100;//分钟转换成小数
                float value = Float.parseFloat(time[0]) + decimal;
                return value;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0f;
    }
}
