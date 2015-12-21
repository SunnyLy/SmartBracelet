package com.smartbracelet.sunny.ui.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.het.common.callback.ICallback;
import com.het.common.utils.CommSharePreferencesUtil;
import com.het.common.utils.LogUtils;
import com.het.common.utils.NetworkStateUtil;
import com.het.comres.view.dialog.CommPrompDialog;
import com.het.comres.view.dialog.CommonToast;
import com.het.comres.view.dialog.PromptUtil;
import com.het.comres.view.layout.ItemLinearLayout;
import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.biz.api.UserApi;
import com.smartbracelet.sunny.manager.DateManager;
import com.smartbracelet.sunny.manager.ScreenManager;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.UserModel;
import com.smartbracelet.sunny.ui.MainActivity;
import com.smartbracelet.sunny.ui.widget.AbstractBaseDialog;
import com.smartbracelet.sunny.ui.widget.HeightDialog;
import com.smartbracelet.sunny.ui.widget.SimpleChooseDialogManager;
import com.smartbracelet.sunny.ui.widget.WeightDialog;
import com.smartbracelet.sunny.utils.Json2Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 15-11-8 上午12:12
 * 个人信息
 */
public class UserInfoActivity extends BaseActivity implements SimpleChooseDialogManager.IOnSimpleChooseDialogCallback {
    public static final String TAG = UserInfoActivity.class.getSimpleName();

    @InjectView(R.id.common_top_bar)
    CommonTopBar mCommonTopBar;
    @InjectView(R.id.item_userinfo_birthday)
    ItemLinearLayout mLayoutBirthday;
    @InjectView(R.id.item_userinfo_weight)
    ItemLinearLayout mLayoutWeight;
    @InjectView(R.id.item_userinfo_height)
    ItemLinearLayout mLayoutHeight;
    @InjectView(R.id.item_userinfo_mobile)
    ItemLinearLayout mLayoutMobile;
    @InjectView(R.id.item_sex)
    RelativeLayout mItemSex;
    @InjectView(R.id.item_hands)
    RelativeLayout mItemHands;
    @InjectView(R.id.item_sex_man)
    TextView mCheckboxMan;
    @InjectView(R.id.item_sex_woman)
    TextView mCheckboxWoman;
    @InjectView(R.id.item_hands_right)
    TextView mRightHand;
    @InjectView(R.id.item_hands_left)
    TextView mLeftHand;

    private TextView mTextViewBirthday;
    private TextView mTextViewWeight;
    private TextView mTextViewHeight;
    private TextView mTextViewMobile;

    @InjectView(R.id.user_info_commit)
    Button mBtnCommit;

    private EditText mEdittextMobile;
    private EditText mEdittextCode;
    private Button mBtnGetCode;

    private int mCheckedItem = 1;

    private volatile String currentHand = "1";
    private volatile String currentSex = "1";
    private volatile int dialogType = 1;


    //身高体重
    private List<String> mHeights = new ArrayList<String>();
    private List<String> mWeights = new ArrayList<String>();
    private static final int HEIGHT_MIN = 50;
    private static final int HEIGHT_MAX = 250;
    private static final int WEIGHT_MIN = 20;
    private static final int WEIGHT_MAX = 200;

    //出生日期
    private Calendar mCalendar;
    private int mYear;
    private int mMonth;
    private int mDay;

    private DateManager mDateManager;
    private SimpleChooseDialogManager mSimpleChooseDialogManager;

    private UserModel mUserModel;
    private UserManager mUserManager;
    private String mUserId;


    public static void startUserInfoActivity(Context context) {
        Intent targetIntent = new Intent(context, UserInfoActivity.class);
        context.startActivity(targetIntent);
    }

    /**
     * 修改监听
     */
    private AbstractBaseDialog.OnSaveListener onSimpleSaveListener = new AbstractBaseDialog.OnSaveListener() {
        @Override
        public void onSave(String data) {

            switch (dialogType) {
                case 1:
                    mUserModel.setWeight(data);
                    mTextViewWeight.setText(Integer.valueOf(data) / 1000 + "");
                    break;
                case 2:
                    mUserModel.setHeight(data);
                    mTextViewHeight.setText(data);
                    break;
            }

        }
    };

    private DateManager.DatePickerLister mDatePickerLister = new DateManager.DatePickerLister() {

        @Override
        public void onDatePicker(int year, int month, int day) {
            handleDate(year, month, day);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        getUserInfo();
    }

    private void initView() {
        mTextViewBirthday = (TextView) ((RelativeLayout) (mLayoutBirthday
                .getChildAt(0))).getChildAt(3);
        mTextViewHeight = (TextView) ((RelativeLayout) (mLayoutHeight.getChildAt(0)))
                .getChildAt(3);
        mTextViewWeight = (TextView) ((RelativeLayout) (mLayoutWeight.getChildAt(0)))
                .getChildAt(3);
        mTextViewMobile = (TextView) ((RelativeLayout) (mLayoutMobile.getChildAt(0)))
                .getChildAt(3);
    }

    private void getUserInfo() {
        mUserId = mUserModel == null ? AppConstant.User.USE_ID_DEF : mUserModel.getUserID();
        new UserApi().getPersonInfo(new ICallback<String>() {
            @Override
            public void onSuccess(String o, int id) {
                UserModel model = Json2Model.parseJson(o, UserModel.class);
                freshUI(model);
            }

            @Override
            public void onFailure(int code, String msg, int id) {
                CommonToast.showToast(mContext, msg);
            }
        }, mUserId);
    }

    /**
     * 刷新界面
     *
     * @param model
     */
    private void freshUI(UserModel model) {

        String sex = model.getSex();
        String birthday = model.getBirthday();
        String height = model.getHeight();
        String weight = model.getWeight();
        String adornHand = model.getAdornHand();
        String mobile = model.getMobile();

        setSex(Integer.valueOf(sex));
        setHands(Integer.valueOf(adornHand));
        mTextViewBirthday.setText(birthday);
        mTextViewHeight.setText(height);
        mTextViewWeight.setText(weight);
        mTextViewMobile.setText(mobile);


    }

    @Override
    public void initParams() {
        initData();
        initCaledar();
        mDateManager = DateManager.getInstance(UserInfoActivity.this);
        mUserManager = UserManager.getInstance();
        UserModel newUserModel = new UserModel();
        newUserModel.setUserID(AppConstant.User.USE_ID_DEF);
        mUserModel = mUserManager.getUserModel() == null ? newUserModel : mUserManager.getUserModel();
    }

    private void initData() {
        mWeights.clear();
        mHeights.clear();
        for (int i = WEIGHT_MIN; i < WEIGHT_MAX; i++) {
            mWeights.add(i + "");
        }

        for (int j = HEIGHT_MIN; j < HEIGHT_MAX; j++) {
            mHeights.add(j + "");
        }
    }

    private void initCaledar() {
        mCalendar = Calendar.getInstance(Locale.CHINA);
        Date mDate = new Date();
        mCalendar.setTime(mDate);

        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public void initTitleBar() {
        mCommonTopBar.setUpNavigateMode();
        mCommonTopBar.setTitle(R.string.set_personal);
    }

    @OnClick({R.id.item_userinfo_weight, R.id.item_userinfo_height,
            R.id.item_userinfo_birthday, R.id.item_userinfo_mobile,
            R.id.item_sex, R.id.item_hands,
            R.id.user_info_commit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_sex:
                new SimpleChooseDialogManager(mContext).setParams(getString(R.string.userinfo_sex),
                        SimpleChooseDialogManager.DialogType.SEX, mCheckedItem, this).showDialog();
                break;
            case R.id.item_hands:
                new SimpleChooseDialogManager(mContext).setParams(getString(R.string.userinfo_hands),
                        SimpleChooseDialogManager.DialogType.HANDS, mCheckedItem, this).showDialog();
                break;
            case R.id.item_userinfo_weight:
                dialogType = 1;
                WeightDialog weightDialog = new WeightDialog(UserInfoActivity.this);
                weightDialog.setUserModel(mUserModel);
                weightDialog.showTitle(getResources().getString(R.string.userinfo_weight));
                weightDialog.setData(mWeights);
                weightDialog.setCurrentItem(Integer.valueOf(mTextViewWeight.getText().toString()) * 1000 + "");
                weightDialog.onSave(onSimpleSaveListener);
                break;

            case R.id.item_userinfo_height:
                dialogType = 2;
                HeightDialog heightDialog = new HeightDialog(UserInfoActivity.this);
                heightDialog.setUserModel(mUserModel);
                heightDialog.showTitle(getResources().getString(R.string.userinfo_height));
                heightDialog.setData(mHeights);
                heightDialog.setCurrentItem(mTextViewHeight.getText().toString());
                heightDialog.onSave(onSimpleSaveListener);
                break;

            case R.id.item_userinfo_birthday:
                mDateManager.showDatePickerDialog(mYear, mMonth, mDay, mDatePickerLister);
                break;
            case R.id.item_userinfo_mobile:
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.widget_bind_mobile, null);
                mEdittextMobile = (EditText) contentView.findViewById(R.id.input_mobile);
                mEdittextCode = (EditText) contentView.findViewById(R.id.input_code);
                mBtnGetCode = (Button) contentView.findViewById(R.id.send_code);
                mBtnGetCode.setOnClickListener(this);
                CommPrompDialog dialog = new CommPrompDialog.Builder(mContext).setContentView(contentView).
                        setNegativeButton(getResources().getString(R.string.base_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        setPositiveButton(R.string.base_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String mobile = mEdittextMobile.getText().toString();
                                String code = mEdittextCode.getText().toString();
                                if (TextUtils.isEmpty(mobile)) {
                                    CommonToast.showToast(mContext, getString(R.string.dialog_input_mobile));
                                    return;
                                }
                                if (TextUtils.isEmpty(code)) {
                                    CommonToast.showToast(mContext, getString(R.string.dialog_input_code));
                                    return;
                                }

                                startToChangeMobile(mobile, code);
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                break;
            case R.id.user_info_commit:
                commitUserInfo();
                break;

            case R.id.send_code:
                getVeryCode();
                break;
        }
    }

    private void getVeryCode() {

        String mobile = mEdittextMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            CommonToast.showToast(mContext, getString(R.string.dialog_input_mobile));
            return;
        }
        //// TODO: 2015/12/3 下面调获取验证码接口
    }

    /**
     * 开始修改手机号
     * 这个接口是有问题的，
     *
     * @param mobile
     * @param code
     */
    private void startToChangeMobile(String mobile, String code) {

        new UserApi().setMobile(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {

            }

            @Override
            public void onFailure(int code, String msg, int id) {

            }
        }, mUserModel.getUserID(), mobile);
    }

    /**
     * 修改个人信息
     */
    private void commitUserInfo() {
        if (mUserModel == null) {
            LogUtils.e(TAG, "the UserModel cannot be null");
        }

        mUserModel.setSex(currentSex);
        mUserModel.setWeight(mTextViewWeight.getText().toString());
        mUserModel.setAdornHand(currentHand);
        mUserModel.setBirthday(mTextViewBirthday.getText().toString());
        mUserModel.setHeight(mTextViewHeight.getText().toString());
        mUserModel.setMobile(mTextViewMobile.getText().toString());
        mUserModel.setUserID(mUserId);

        showDialog();
        new UserApi().modifyPersonalInfo(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {

                hideDialog();
                CommonToast.showToast(mContext, "修改成功");
                boolean notFirstStart = CommSharePreferencesUtil.getBoolean(mContext, "notFirstStart");
                if (!notFirstStart) {
                    CommSharePreferencesUtil.putBoolean(mContext, "notFirstStart", true);
                    MainActivity.startMainActivity(mContext);
                    ScreenManager.getScreenManager().popAllActivity();

                }
                finish();

            }

            @Override
            public void onFailure(int code, String msg, int id) {
                hideDialog();
                CommonToast.showToast(mContext, msg);
            }
        }, mUserModel);

    }


    protected void handleDate(int year, int month, int day) {
        String mon = "";
        String strDay = "";
        if ((month + 1) < 10) {
            mon = "0" + (month + 1);
        } else {
            mon = (month + 1) + "";
        }

        if (day < 10) {
            strDay = "0" + day;
        } else {
            strDay = day + "";
        }
        String birth = year + "-" + mon + "-" + strDay;
        if (!NetworkStateUtil.isNetworkAvailable(mContext)) {
            PromptUtil.showToast(mContext, R.string.common_no_network);
            return;
        }
        mTextViewBirthday.setText(birth);
        mUserModel.setBirthday(birth);
    }

    @Override
    public void onSure(DialogInterface dialog, String item, int which, SimpleChooseDialogManager.DialogType type) {
        dialog.dismiss();
        mCheckedItem = which;
        switch (type) {
            case SEX:
                setSex(which);
                break;
            case HANDS:
                setHands(which);
                break;
        }
    }

    private void setHands(int which) {
        if (which == 1) {
            Drawable leftDrawable = getResources().getDrawable(R.mipmap.icon_left_hand_checked);
            leftDrawable.setBounds(0, 0, 32, 32);
            mLeftHand.setCompoundDrawables(leftDrawable, null, null, null);
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.icon_right_hand_def);
            rightDrawable.setBounds(0, 0, 32, 32);
            mRightHand.setCompoundDrawables(rightDrawable, null, null, null);
            currentHand = "1";
        } else {
            Drawable leftDrawable = getResources().getDrawable(R.mipmap.icon_left_hand_def);
            leftDrawable.setBounds(0, 0, 32, 32);
            mLeftHand.setCompoundDrawables(leftDrawable, null, null, null);
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.icon_right_hand_checked);
            rightDrawable.setBounds(0, 0, 32, 32);
            mRightHand.setCompoundDrawables(rightDrawable, null, null, null);
            currentHand = "2";
        }
    }

    /**
     * 设置性别
     *
     * @param who
     */
    private void setSex(int who) {
        if (who == 1) {
            Drawable manDrawable = getResources().getDrawable(R.mipmap.sex_man_press);
            manDrawable.setBounds(0, 0, 60, 60);
            mCheckboxMan.setCompoundDrawables(manDrawable, null, null, null);
            Drawable womanDrawable = getResources().getDrawable(R.mipmap.sex_woman_norman);
            womanDrawable.setBounds(0, 0, 60, 60);
            mCheckboxWoman.setCompoundDrawables(womanDrawable, null, null, null);
            currentSex = "1";
        } else {
            Drawable manDrawable = getResources().getDrawable(R.mipmap.sex_man_norman);
            manDrawable.setBounds(0, 0, 60, 60);
            mCheckboxMan.setCompoundDrawables(manDrawable, null, null, null);
            Drawable womanDrawable = getResources().getDrawable(R.mipmap.sex_woman_press);
            womanDrawable.setBounds(0, 0, 60, 60);
            mCheckboxWoman.setCompoundDrawables(womanDrawable, null, null, null);
            currentSex = "2";
        }
    }

    @Override
    public void onCancel() {


    }
}
