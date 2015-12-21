package com.smartbracelet.sunny.manager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;

import com.smartbracelet.sunny.R;

import java.util.Calendar;

public class DateManager {
    private static Context mContext;
    private static DateManager mInstance;

    private DatePickerDialog mDateDialog;
    private static int mYear;
    private static int mMonth;
    private static int mDay;

    public interface DatePickerLister {
        void onDatePicker(int year, int month, int day);
    }

    public DateManager(Context context) {

    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

        }
    };

    public static DateManager getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            synchronized (DateManager.class) {
                if (mInstance == null) {
                    mInstance = new DateManager(context);
                }
            }
        }

        return mInstance;
    }

    public void showDatePickerDialog(int year, int month, int day, final DatePickerLister datePickerLister) {
        mYear = year;
        mMonth = month;
        mDay = day;
        if (mDateDialog == null) {
            mDateDialog = new DatePickerDialog(mContext, onDateSetListener, mYear,
                    mMonth, mDay);
        }
        DatePicker dp = mDateDialog.getDatePicker();
        dp.setMaxDate(Calendar.getInstance().getTimeInMillis());// 设置最大日期
        mDateDialog.setTitle(R.string.userinfo_select_birthday);
        mDateDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mContext
                        .getResources().getString(R.string.common_cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mDateDialog = null;
                    }
                });
        mDateDialog.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getResources().getString(R.string.common_sure), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mYear = mDateDialog.getDatePicker().getYear();
                mMonth = mDateDialog.getDatePicker().getMonth();
                mDay = mDateDialog.getDatePicker().getDayOfMonth();
                datePickerLister.onDatePicker(mYear, mMonth, mDay);
                dialog.dismiss();
                mDateDialog = null;
            }
        });
        mDateDialog.show();
    }
}
