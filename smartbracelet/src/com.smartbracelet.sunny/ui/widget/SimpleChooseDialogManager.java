package com.smartbracelet.sunny.ui.widget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.smartbracelet.sunny.R;

/**
 * Created by sunny on 2015/11/17.
 * 简单的选择对话框
 */
public class SimpleChooseDialogManager {
    private static Context mContext;

    private static SimpleChooseDialogManager mInstance;

    private IOnSimpleChooseDialogCallback chooseDialogCallback;

    private AlertDialog mAlertDialog;
    private AlertDialog.Builder mBuilder;

    private String mTitle;
    private String mNegativeChar;
    private String mPositiveChar;
    private String mMsg;
    private String mChecked;
    private int mCheckedItem;
    private int layoutId;
    private DialogType mDialogType;

    private Button mBtnSubmit;
    private TextView mTextViewFirst;
    private TextView mTextViewSecond;
    private TextView mTextViewTitle;
    private CheckBox mCheckboxFirst;
    private CheckBox mCheckboxSecond;

    public enum DialogType {
        SEX, HANDS
    }


    public void setChooseDialogCallback(IOnSimpleChooseDialogCallback chooseDialogCallback) {
        this.chooseDialogCallback = chooseDialogCallback;
    }

    public SimpleChooseDialogManager(Context context) {
        mContext = context;
        initDialog();
    }


    /**
     * 设置参数
     *
     * @param title    标题
     * @param callback 回调
     * @param curItem  当前选中项
     * @return
     */
    public SimpleChooseDialogManager setParams(String title, DialogType type, int curItem, final IOnSimpleChooseDialogCallback callback) {

        if (mBuilder != null) {
            mDialogType = type;
            mCheckedItem = curItem;
            this.chooseDialogCallback = callback;
            mTitle = title;
            mBuilder.setTitle(title);
           /* mBuilder.setNegativeButton(negativeChar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    callback.onCancel();
                }
            });
            mBuilder.setPositiveButton(positiveChar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callback.onSure(dialog, "", which);
                }
            });*/
        }
        return this;
    }

    private void initDialog() {
        mBuilder = new AlertDialog.Builder(mContext);

    }

    /**
     * 弹出Dialog
     */
    public void showDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = mBuilder.create();
            mAlertDialog.setCanceledOnTouchOutside(false);
        }

        if (!mAlertDialog.isShowing())
            mAlertDialog.show();
        Window mWindow = mAlertDialog.getWindow();
        mWindow.setContentView(R.layout.dialog_sex);
        mBtnSubmit = (Button) mWindow.findViewById(R.id.dialog_submmit);
        mTextViewTitle = (TextView) mWindow.findViewById(R.id.dialog_title);
        mTextViewFirst = (TextView) mWindow.findViewById(R.id.dialog_item_first);
        mTextViewSecond = (TextView) mWindow.findViewById(R.id.dialog_item_second);
        mCheckboxFirst = (CheckBox) mWindow.findViewById(R.id.cb_item_first);
        mCheckboxSecond = (CheckBox) mWindow.findViewById(R.id.cb_item_second);
        mTextViewTitle.setText(mTitle);
        if (mDialogType == DialogType.HANDS) {
            mTextViewFirst.setText("左");
            mTextViewSecond.setText("右");
        } else if (mDialogType == DialogType.SEX) {
            mTextViewFirst.setText("男");
            mTextViewSecond.setText("女");
        }
        checkCheckbox(mCheckedItem);
        mCheckboxFirst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    mCheckboxFirst.setChecked(false);
                    mCheckboxSecond.setChecked(true);
                    mChecked = "2";
                    mCheckedItem = 2;
                } else {
                    mCheckboxSecond.setChecked(false);
                    mChecked = "1";
                    mCheckedItem = 1;
                }

                chooseDialogCallback.onSure(mAlertDialog, mChecked, mCheckedItem, mDialogType);
            }
        });
        mCheckboxSecond.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    mCheckboxFirst.setChecked(true);
                    mCheckboxSecond.setChecked(false);
                    mChecked = "1";
                    mCheckedItem = 1;
                } else {
                    mCheckboxFirst.setChecked(false);
                    mChecked = "2";
                    mCheckedItem = 2;
                }

                chooseDialogCallback.onSure(mAlertDialog, mChecked, mCheckedItem, mDialogType);
            }
        });
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDialogCallback.onSure(mAlertDialog, mChecked, mCheckedItem, mDialogType);
            }
        });
    }

    private void checkCheckbox(int mCheckedItem) {
        if (mCheckedItem == 1) {
            mCheckboxFirst.setChecked(true);
            mCheckboxSecond.setChecked(false);
        } else {
            mCheckboxSecond.setChecked(true);
            mCheckboxFirst.setChecked(false);
        }
    }

    /**
     * 隐藏Dialog
     */
    public void hideDialog() {

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }

    public interface IOnSimpleChooseDialogCallback {
        void onSure(DialogInterface dialog, String item, int which, DialogType type);

        void onCancel();
    }
}
