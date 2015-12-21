package com.smartbracelet.sunny.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.het.common.utils.NetworkStateUtil;
import com.smartbracelet.sunny.adapter.WheelViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-09-10.
 */
public class HeightDialog extends AbstractBaseDialog {
    private List<String> heights;
    private WheelViewAdapter mWheelViewAdapter;
    private Context mContext;

    public HeightDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void initData(Context context) {
        wheelViewFirst.setVisibility(View.INVISIBLE);
        heights = new ArrayList<>();
        showUnit("cm");
    }


    public void setData(List<String> data) {
        heights.clear();
        heights.addAll(data);
        mWheelViewAdapter = new WheelViewAdapter(mContext, heights);
        wheelViewSecond.setViewAdapter(mWheelViewAdapter);
    }

    public void setCurrentItem(String resData) {

        if (!TextUtils.isEmpty(resData)) {
            for (int i = 0; i < heights.size(); i++) {
                if (resData.equals(heights.get(i))) {
                    wheelViewSecond.setCurrentItem(i);
                }
            }
        }
    }


    @Override
    public void onSave(OnSaveListener listener) {
        super.onSave(listener);
    }

    @Override
    public void onClick(View v) {
        String data = heights.get(wheelViewSecond.getCurrentItem());
        if (getUserModel() != null && NetworkStateUtil.isNetworkAvailable(mContext))
            getUserModel().setHeight(data);
        mOnSaveListener.onSave(data);
        dismiss();
    }
}
