package com.smartbracelet.sunny.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.het.comres.view.dialog.CommonLoadingDialog;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

/**
 * Created by sunny on 15-11-5.
 */
public class BaseFragment extends Fragment implements View.OnClickListener {

    public Context mContext;
    public Resources mResources;
    public LayoutInflater mInflater;
    public CommonLoadingDialog mCommonLoadingDialog;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mResources = mContext.getResources();
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
    }

    public void showDialog() {
        if (mCommonLoadingDialog == null) {
            mCommonLoadingDialog = new CommonLoadingDialog(mContext);
        }
        mCommonLoadingDialog.show();
    }

    public void showDialog(String title) {
        if (mCommonLoadingDialog == null) {
            mCommonLoadingDialog = new CommonLoadingDialog(mContext);
            mCommonLoadingDialog.setText(title);
        }
        mCommonLoadingDialog.show();
    }

    public void hideDialog() {
        if (mCommonLoadingDialog != null && mCommonLoadingDialog.isShowing()) {
            mCommonLoadingDialog.dismiss();
            mCommonLoadingDialog = null;
        }
    }

    public boolean isDialogShow() {
        return mCommonLoadingDialog != null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
