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
public class WeightDialog extends AbstractBaseDialog {
    private List<String> weights;
    private WheelViewAdapter mWheelViewAdapter;
    private Context mContext;

    public WeightDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void initData(Context context) {
        wheelViewFirst.setVisibility(View.INVISIBLE);
        weights = new ArrayList<>();
        showUnit("kg");
    }


    public void setData(List<String> data) {
        weights.clear();
        weights.addAll(data);
        mWheelViewAdapter = new WheelViewAdapter(mContext, weights);
        wheelViewSecond.setViewAdapter(mWheelViewAdapter);
    }


    public void setCurrentItem(String resData) {

        if (!TextUtils.isEmpty(resData)) {
            resData = Integer.valueOf(resData) / 1000 + "";
            for (int i = 0; i < weights.size(); i++) {
                if (resData.equals(weights.get(i))) {
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
        String data = weights.get(wheelViewSecond.getCurrentItem());
        data = Integer.valueOf(data) * 1000 + "";
        if (getUserModel() != null && NetworkStateUtil.isNetworkAvailable(mContext))
            getUserModel().setWeight(data);
        mOnSaveListener.onSave(data);
        dismiss();
    }
}
