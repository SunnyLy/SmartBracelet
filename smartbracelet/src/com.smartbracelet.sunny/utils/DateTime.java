package com.smartbracelet.sunny.utils;

import android.text.format.DateUtils;
import android.text.format.Time;

import com.het.common.constant.TimeConsts;
import com.het.common.utils.TimeUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * 类名: DateTime
 * 描述: 日期处理工具
 *
 * @author ~若相惜
 * @version V1.0
 * @date 2014-7-6 上午12:13:31
 * @since JDK 1.6
 */
public class DateTime {
    public static final String DEFYMD = "yyyy-MM-dd";
    public static final String DEFYMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFHMS = "HH:mm:ss";

    public static Date getDateByFormat(String strDate, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = mSimpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 描述：Date类型转化为String类型.
     *
     * @param date   the date
     * @param format the format
     * @return String String类型日期时间
     */
    public static String getStringByFormat(Date date, String format) {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format  输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    public static String getStringByFormat(String strDate, String format) {
        return getStringByFormat(strDate, DEFYMD, format);
    }

    public static String getStringByFormat(String strDate, String oldformat, String newformat) {
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(oldformat);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(newformat);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * 获得指定格式的日期
     *
     * @param pattern 格式模板，为null，则为默认的格式
     * @param date    毫秒数
     * @return 符合格式的日期字符串
     */
    public static String getDate(String pattern, long date) {
        pattern = pattern == null ? "yyyy/MM/dd/HH/mm/ss" : pattern;
        return new SimpleDateFormat(pattern).format(new Date(date));
    }

    /**
     * 将一个double型的数四舍五入,以百分比的形式返回
     *
     * @param num       要格式化的 double的数
     * @param strFormat 格式模板 如果strFormat为null，默认为“#0.00%”，否则为传入的样式模板
     * @return 反回百分比的字符串
     */
    public static String getDouble4Format(double num, String strFormat) {
        strFormat = strFormat == null ? "#0.00%" : strFormat;
        DecimalFormat dFormat = new DecimalFormat(strFormat);
        String num2str = dFormat.format(num);
        return num2str;
    }


    /**
     * <p><B>方法:</B><br/> getTime </p><br/>
     * <p><B>描述:</B><br/> 获取运行时间</p>
     *
     * @param startTime
     * @return String    返回类型
     */
    public static String getTime(long startTime) {
        long a = System.currentTimeMillis() - startTime;
        Date date = new Date(a);
        return "00:" + new SimpleDateFormat("mm:ss").format(date);
    }

    public static String turnTime(long timemills) {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = sf.parse("00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("parse date err!");
        }
        date.setTime(date.getTime() + timemills);
        return sf.format(date);
    }

    /**
     * <p><B>方法:</B><br/> fileNameFromDate </p><br/>
     * <p><B>描述:</B><br/> 给文件命名</p>
     *
     * @return String    返回类型
     */
    public static String fileNameFromDate() {
        return getDate("yyyy-MM-dd-HH-mm-ss-SSS", System.currentTimeMillis());
    }


    /**
     * <p><B>方法:</B><br/> fileNameFromDate </p><br/>
     * <p><B>描述:</B><br/> 给文件命名</p>
     *
     * @return String    返回类型
     */
    public static String leaveTimeFromDate() {
        return getDate("yyyy-MM-dd HH:mm", System.currentTimeMillis());
    }

    public static long getStartTime() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        return currentDate.getTime().getTime();
    }

    public static long getEndTime() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return currentDate.getTime().getTime();
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay, int num) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(DEFYMD).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - num);

        String dayBefore = new SimpleDateFormat(DEFYMD).format(c.getTime());
        return dayBefore;
    }


    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(DEFYMD).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat(DEFYMD).format(c.getTime());
        return dayAfter;
    }

    /**
     * <p>描述:当前日期一周中第几天</p>
     *
     * @param pTime 例如："2015-08-12"
     * @return 第几天
     */
    public static int dayForWeek(String pTime) {
        int dayForWeek = 1;
        try {
            SimpleDateFormat format = new SimpleDateFormat(DEFYMD);
            Calendar c = Calendar.getInstance();
            c.setTime(format.parse(pTime));
            dayForWeek = 0;
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                dayForWeek = 7;
            } else {
                dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayForWeek;
    }

    /**
     * <p>描述:当前日期一月中第几天</p>
     *
     * @param pTime 例如："2015-08-12"
     * @return 第几天
     */
    public static int dayForMonth(String pTime) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(DEFYMD);
            Calendar c = Calendar.getInstance();
            c.setTime(format.parse(pTime));
            int days = c.get(Calendar.DAY_OF_MONTH);
            return days;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 取得当前日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static long getFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Sunday
        return calendar.getTime().getTime();
    }

    /**
     * 取得当前日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static long getLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Saturday
        return calendar.getTime().getTime();
    }

    public static int weeks = 0;

    // 获得当前日期与本周一相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    public static String getPreviousMonday() {
        weeks--;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        String preMonday = getStringByFormat(monday, DEFYMD);
        return preMonday;
    }

    /**
     * <p>描述:获得上周星期一的日期</p>
     *
     * @return 设定文件
     */
    public static String getNextMonday() {
        weeks++;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime();
        String preMonday = getStringByFormat(monday, DEFYMD);
        return preMonday;
    }

    /**
     * <p>描述:获得相应周的周日的日期</p>
     *
     * @return String
     */
    public static String getSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime();
        String preMonday = getStringByFormat(monday, DEFYMD);
        return preMonday;
    }

    /**
     * <p>描述:求当前日期的上num个月的第一天</p>
     *
     * @param date 当前日期
     * @param num  0：表示当前月  num必须大于0  例：num:2 表示上两个月
     * @return 日期
     */
    public static Date getFirstDayOfMonth(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - num, 1);
        return calendar.getTime();
    }

    /**
     * <p>描述:求当前日期的下num个月的最后一天</p>
     *
     * @param date 当前日期
     * @param num  0：表示当前月  num必须大于0  例：num:2 表示上两个月
     * @return 日期
     */
    public static Date getLastDayOfMonth(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + num, 1);
        calendar.roll(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * <p>描述:获取一周内所有的日期</p>
     *
     * @param startdate 一周的起始日期
     * @param parent    "MM-dd"
     * @return List<String>
     */
    public static List<String> dateToWeek(String startdate, String parent) {
        List<String> list = new ArrayList<String>();
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(DEFYMD);
            Date mdate = mSimpleDateFormat.parse(startdate);
            int b = mdate.getDay() - 1;
            Long fTime = mdate.getTime() - b * 24 * 3600000;
            for (int a = 0; a < 7; a++) {
                list.add(DateTime.getDate(parent, fTime + (a * 24 * 3600000)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将UTC时间转换为本地时间
     *
     * @param utcTime
     * @param utcTimePatten
     * @param localTimePatten
     * @return 本地时间
     */
    public static String utc2Local(String utcTime, String utcTimePatten, String localTimePatten) {
        SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
        utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = utcFormater.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
        localFormater.setTimeZone(TimeZone.getDefault());
        String localTime = localFormater.format(gpsUTCDate.getTime());
        return localTime;
    }


    /**
     * 将本地时间转换为UTC
     *
     * @param localTime
     * @param localTimePatten
     * @param utcTimePatten
     * @return UTC时间
     */
    public static String local2utc(String localTime, String localTimePatten, String utcTimePatten) {
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateTime.getDateByFormat(localTime, localTimePatten));
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        int year = cal.get(Calendar.YEAR);
        //int month = cal.get(Calendar.MONTH) + 1;
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        cal.set(year, month, day, hour, minute);
        return DateTime.getStringByFormat(cal.getTime(), utcTimePatten);
    }

    // 获得当天0点时间
    public static long getTimesmorning(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    // 获得当天24点时间
    public static long getTimesnight(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 转换本地指定的时间到utc时间
     *
     * @param mStartTime 本地起始时间
     * @param mEndTime   本地结束时间
     * @return utc时间数组
     */
    public static String[] local2utcArray(String mStartTime, String mEndTime) {
        final Date startDate = getDateByFormat(mStartTime, DEFYMD);
        final Date endDate = getDateByFormat(mEndTime, DEFYMD);
        final String start = getStringByFormat(new Date(getTimesmorning(startDate)), DEFYMDHMS);
        final String end = getStringByFormat(new Date(getTimesnight(endDate)), DEFYMDHMS);
        final String utcStartTime = local2utc(start, DEFYMDHMS, DEFYMDHMS);
        final String utcEndTime = local2utc(end, DEFYMDHMS, DEFYMDHMS);
        return new String[]{utcStartTime, utcEndTime};
    }

    /**
     * 获取utc时间与本地时区相差时间小时
     *
     * @return
     */
    public static int getTimeZoneRawOffset() {
        int time = TimeZone.getTimeZone(TimeZone.getDefault().getID()).getRawOffset();
        return time / (3600 * 1000);
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
                DateTime.DEFYMD);
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return mSimpleDateFormat.format(c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
                DateTime.DEFYMD);
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return mSimpleDateFormat.format(c.getTime());
    }

    /**
     * 获取月份的中文描述.
     *
     * @param iMonth 一年的第几月，范围从0到11
     * @return 月份的中文描述
     */
    public static String getMonthStringCN(final int iMonth) {
        String strWhatMonth = null;
        switch (iMonth) {
            case Calendar.JANUARY:
                strWhatMonth = TimeConsts.CN_JANUARY;
                break;
            case Calendar.FEBRUARY:
                strWhatMonth = TimeConsts.CN_FEBRUARY;
                break;
            case Calendar.MARCH:
                strWhatMonth = TimeConsts.CN_MARCH;
                break;
            case Calendar.APRIL:
                strWhatMonth = TimeConsts.CN_APRIL;
                break;
            case Calendar.MAY:
                strWhatMonth = TimeConsts.CN_MAY;
                break;
            case Calendar.JUNE:
                strWhatMonth = TimeConsts.CN_JUNE;
                break;
            case Calendar.JULY:
                strWhatMonth = TimeConsts.CN_JULY;
                break;
            case Calendar.AUGUST:
                strWhatMonth = TimeConsts.CN_AUGUST;
                break;
            case Calendar.SEPTEMBER:
                strWhatMonth = TimeConsts.CN_SEPTEMBER;
                break;
            case Calendar.OCTOBER:
                strWhatMonth = TimeConsts.CN_OCTOBER;
                break;
            case Calendar.NOVEMBER:
                strWhatMonth = TimeConsts.CN_NOVEMBER;
                break;
            case Calendar.DECEMBER:
                strWhatMonth = TimeConsts.CN_DECEMBER;
                break;
            default:
                strWhatMonth = TimeConsts.CN_JANUARY;
                break;
        }
        return strWhatMonth;
    }

    /**
     * 当天日期与参数日期进行比较，根据时间差返回对应的字符串.
     *
     * @param strDate  比较的日期
     * @param strInFmt 日期的格式
     * @return 时间差为0返回“今天”，时间差(0,1]天返回“昨天“，时间差(1,2]天返回“前天”，否则返回星期几
     * @throws ParseException 日期格式转换异常
     */
    public static String getComparedDateStringCN(final String strDate,
                                                 final String strInFmt) throws ParseException {
        long lTodayMillis = TimeUtils.remainMillis(System.currentTimeMillis()); // 当天零点时间距离1970-01-01的毫秒数
        long lComparedMillis = TimeUtils.parseMillis(strDate, strInFmt); // 需要比较的日期距离1970-01-01的毫秒数
        long lOffset = lTodayMillis - lComparedMillis; // 时间差
        if (lOffset == 0) {
            return TimeConsts.CN_TODAY; // 今天
        } else if (lOffset > 0 && lOffset <= DateUtils.DAY_IN_MILLIS) {
            return TimeConsts.CN_YESTERDAY; // 昨天
        } else if (lOffset > DateUtils.DAY_IN_MILLIS
                && lOffset <= TimeConsts.TWO_DAYS_IN_MILLIS) {
            return TimeConsts.CN_BEFORE_YESTERDAY; // 前天
        } else {
            Time time = new Time();
            time.set(lComparedMillis);
            int iWhatDay = time.weekDay;
            return TimeUtils.getDayOfWeekStringCN(iWhatDay); // 星期几
        }
    }

    /**
     * 获取日期的中文描述，日期为一号时返回月份名，否则返回日数.
     *
     * @param strDate  日期参数
     * @param strInFmt 日期格式
     * @return 日期的中文描述
     * @throws ParseException 日期格式转换异常
     */
    public static String getDateStringForDayCN(final String strDate,
                                               final String strInFmt) throws ParseException {
        long lMillis = TimeUtils.parseMillis(strDate, strInFmt);
        Time time = new Time();
        time.set(lMillis);
        int iDay = time.monthDay;
        if (iDay == 1) {
            /* 日期为一号返回月份信息 */
            int iMonth = time.month;
            return TimeUtils.getMonthStringCN(iMonth);
        } else {
                  /* 日期非一号返回日信息 */
            return String.valueOf(iDay);
        }
    }

    /**
     * 获取日期的中文描述，日期所在周为当前月份的第一周时返回月份名，否则返回日数.
     *
     * @param strDate  日期参数
     * @param strInFmt 日期格式
     * @return 日期的中文描述
     * @throws ParseException 日期格式转换异常
     */
    public static String getDateStringForWeekCN(final String strDate,
                                                final String strInFmt) throws ParseException {
        long lMillis = TimeUtils.parseMillis(strDate, strInFmt);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeInMillis(lMillis);
        int iWeek = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        if (iWeek == 1) {
            /* 所在周为当前月份第一周时返回月份名 */
            int iMonth = calendar.get(Calendar.MONTH);
            return TimeUtils.getMonthStringCN(iMonth);
        } else {
            /* 日期非一号返回日信息 */
            int iDay = calendar.get(Calendar.DAY_OF_MONTH);
            return String.valueOf(iDay);
        }
    }

    /**
     * 根据出生日期计算岁数
     *
     * @param dateOfBirth
     * @return
     */
    public static int getAge(Date dateOfBirth) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException(
                        "Can't be born in the future");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
                age -= 1;
            }
        }
        return age;
    }
}
