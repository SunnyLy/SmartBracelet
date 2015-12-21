package com.smartbracelet.sunny.adapter;

import android.content.Context;

import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.adapter.comm.CommonAdapter;
import com.smartbracelet.sunny.adapter.comm.CommonViewHoler;
import com.smartbracelet.sunny.model.CheckProjModel;

import java.util.List;

/**
 * Created by sunny on 2015/11/7.
 * Annotion:首页适配器
 */
public class HomeAdapter extends CommonAdapter<CheckProjModel> {
    public HomeAdapter(Context context, List<CheckProjModel> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(CommonViewHoler holer, CheckProjModel itemObject) {

        String time = itemObject.getTime();
        String name = itemObject.getType();
        String result = itemObject.getValue();

        holer.setText(R.id.item_time, time);
        holer.setText(R.id.item_proj_name, name);
        holer.setText(R.id.item_proj_value, result);
    }
}
