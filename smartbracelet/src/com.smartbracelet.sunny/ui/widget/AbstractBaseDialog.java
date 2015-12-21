package com.smartbracelet.sunny.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.het.comres.view.dialog.CommonBottomDialog;
import com.het.comres.view.wheelview.WheelView;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.model.UserModel;

/**
 * Created by Administrator on 2015-09-10.
 */
public abstract class AbstractBaseDialog extends CommonBottomDialog implements View.OnClickListener {
    private View mLayoutView;
    private Context mContext;

    private TextView mTitle;
    public TextView mUnit;
    private ImageView mSave;

    public WheelView wheelViewFirst;
    public WheelView wheelViewSecond;
    public WheelView wheelViewThree;
    public UserModel mUserModel;

    public OnSaveListener mOnSaveListener;

    public AbstractBaseDialog(Context context) {
        super(context);
        mContext = context;
        initView();
        initData(context);
    }

    public void setUserModel(UserModel mUserModel) {
        this.mUserModel = mUserModel;
    }

    public UserModel getUserModel() {
        return mUserModel;
    }

    public abstract void initData(Context context);

    private void initView() {
        mLayoutView = LayoutInflater.from(mContext).inflate(R.layout.dialog_wheelview, null);
        setContentView(mLayoutView);
        mTitle = (TextView) mLayoutView.findViewById(R.id.wheelview_title);
        mUnit = (TextView) mLayoutView.findViewById(R.id.wheelview_unit);
        mSave = (ImageView) mLayoutView.findViewById(R.id.wheelview_save);
        wheelViewFirst = (WheelView) mLayoutView.findViewById(R.id.wheelview_first);
        wheelViewSecond = (WheelView) mLayoutView.findViewById(R.id.wheelview_second);
        wheelViewThree = (WheelView) mLayoutView.findViewById(R.id.wheelview_three);

        show();
    }


    public void showTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    public void showUnit(String unit) {
        if (mUnit != null) {
            mUnit.setText(unit);
        }
    }

    public void onSave(OnSaveListener listener) {
        if (mSave != null) {
            mOnSaveListener = listener;
            mSave.setOnClickListener(this);
        }
    }

    public interface OnSaveListener {
        void onSave(String data);
    }

    @Override
    public void onClick(View v) {

    }
}
