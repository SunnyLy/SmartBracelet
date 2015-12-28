package com.smartbracelet.sunny.ui.fragment.checkprj;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.het.common.callback.ICallback;
import com.het.common.utils.LogUtils;
import com.het.common.utils.TimeUtils;
import com.het.comres.view.dialog.CommonToast;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;
import com.smartbracelet.sunny.biz.api.BloodPressureApi;
import com.smartbracelet.sunny.biz.api.TiredApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.TiredModel;
import com.smartbracelet.sunny.model.TiredModel2;
import com.smartbracelet.sunny.model.event.BaseEvent;
import com.smartbracelet.sunny.utils.Json2Model;

import java.text.ParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by sunny on 2015/11/28.
 */
public class TiredFragment extends BaseFragment {

    private String mTestValue;
    private String mTestTime;
    private String mUserId;
    private UserManager mUserManager;

    @InjectView(R.id.measure_time)
    TextView mTimeShow;
    @InjectView(R.id.measure_tired_grade)
    TextView mTiredGrade;
    @InjectView(R.id.measure_tired_tips)
    TextView mTiredTips;
    @InjectView(R.id.tired_et)
    EditText mContent;
    @InjectView(R.id.tired_cancel)
    Button mBtnCancel;
    @InjectView(R.id.tired_save)
    Button mBtnSave;

    public void setmTestValue(String mTestValue) {
        this.mTestValue = mTestValue;
    }

    public void setmTestTime(String mTestTime) {
        this.mTestTime = mTestTime;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tired, null);
        ButterKnife.inject(this, view);
        EventBus.getDefault().register(this);
        initParams();
        return view;
    }

    private void initParams() {
        mUserManager = UserManager.getInstance();
        mUserId = mUserManager.getUserModel() == null ? "1" : mUserManager.getUserModel().getUserID();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("TiredFragment,onResume====");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.e("TiredFragment,onHiddenChanged========");
    }

    @OnClick({R.id.tired_save,R.id.tired_cancel})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tired_cancel:
                mContent.setText("");
                break;
            case R.id.tired_save:
                submitt2server();
                break;
        }
    }

    private void submitt2server() {
        String content = mContent.getText().toString();
        if(TextUtils.isEmpty(content)){
            CommonToast.showToast(mContext,getString(R.string.measure_result_hint));
            return;
        }

        showDialog();
        //这里的请求参数是有问题的，后台没有指定哪种类型对应哪种int值，
        //说公司ios都做完了，我想这个应该有发现出来吧，
        TiredModel tiredModel = new TiredModel();
        tiredModel.setDepressed("1");
        tiredModel.setGentle("1");
        tiredModel.setMiddleTired("1");
        tiredModel.setNormal("1");
        tiredModel.setTired("1");
        new TiredApi().setTiredPressure(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {

                hideDialog();
                CommonToast.showToast(mContext,"保存成功");

            }

            @Override
            public void onFailure(int code, String msg, int id) {

                hideDialog();
                CommonToast.showToast(mContext,msg);
            }
        },mUserId,tiredModel);
    }

    public void onEventMainThread(BaseEvent event) {
        BaseEvent.EventType type = event.getEventType();
        if (type == BaseEvent.EventType.TIRED) {
            getTiredData();
        }
    }

    private void getTiredData() {
        showDialog();
        new TiredApi().getTired(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {
                hideDialog();
                parseJsonObject(o);
            }

            @Override
            public void onFailure(int code, String msg, int id) {

                hideDialog();
                CommonToast.showToast(mContext, msg);
            }
        }, mUserId);
    }

    private void parseJsonObject(Object o) {
        if (o != null) {
            TiredModel2 tiredModel = Json2Model.parseJson((String) o, TiredModel2.class);

            freshUI(tiredModel);
        }

    }

    /**
     * 刷新界面
     *
     * @param tiredModel
     */
    private void freshUI(TiredModel2 tiredModel) {

        String tired = tiredModel.getTired();
        String time = tiredModel.getTime();
        StringBuilder stringBuilder = new StringBuilder();
        String tiredShow = "";
        String tiredTips = "";

        try {
            //今天，昨天，前天，
            String timeShow = TimeUtils.getComparedDateStringCN(time, "yyyy-MM-dd");
            //时间
            String date = TimeUtils.format(time, "yyyy-MM-dd", "HH:mm");
            stringBuilder.append(timeShow).append(" ").append(date);
            mTimeShow.setText(stringBuilder.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int tiredInt = Integer.valueOf(TextUtils.isEmpty(tired) ? "3" : tired);
        //由于这里产品没有给出一个相对统一规范的文案，
        //下面这些都是由ios提供，暂且这样，以后重构时，记得改成统一
        switch (tiredInt) {
            case 1:
                tiredShow = getString(R.string.tired_light_tired);
                tiredTips = getString(R.string.tired_light_tired_info);
                break;
            case 2:
                tiredShow = "平和";
                tiredTips = getString(R.string.tired_normal);
                break;
            case 3:
                tiredShow = "正常";
                tiredTips = getString(R.string.tired_normal);
                break;
            case 4:
                tiredShow = getString(R.string.tired_middle_tired);
                tiredTips = getString(R.string.tired_middle_tired_info);
                break;
            case 5:
                tiredShow = getString(R.string.tired_high_tired);
                tiredTips = getString(R.string.tired_high_tired_info);
                break;
        }

        mTiredGrade.setText(tiredShow);
        mTiredTips.setText(tiredTips);
    }
}
