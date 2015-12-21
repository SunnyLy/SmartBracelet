package com.smartbracelet.sunny.ui.fragment.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;

/**
 * Created by sunny on 2015/11/10.
 * 了解
 */
public class HelpLearnFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_help_learn, null);
        return content;
    }
}
