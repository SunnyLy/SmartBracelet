package com.smartbracelet.sunny.ui.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.het.common.callback.ICallback;
import com.het.comres.view.dialog.CommonToast;
import com.het.comres.widget.CommonTopBar;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseActivity;
import com.smartbracelet.sunny.biz.api.UserApi;
import com.smartbracelet.sunny.manager.UserManager;
import com.smartbracelet.sunny.model.FavoriteModel;
import com.smartbracelet.sunny.model.UserModel;
import com.smartbracelet.sunny.utils.Json2Model;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by sunny on 2015/11/6.
 * Annotion:系统设定
 */
public class MyFavoriteActivity extends BaseActivity {
    public static final String TAG = MyFavoriteActivity.class.getSimpleName();
    private static final String RESULT_KEY = "collects";

    @InjectView(R.id.common_top_bar)
    CommonTopBar commonTopBar;
    @InjectView(R.id.report_empty_view)
    LinearLayout mLayoutEmptyView;
    @InjectView(R.id.empty_info)
    TextView mEmptyInfo;

    private UserManager mUserManger;
    private UserModel mUserModel;
    private String mUserId;

    private List<FavoriteModel> favoriteModels = new ArrayList<>();

    public static void startSettingActivity(Context context) {
        Intent targetIntent = new Intent(context, MyFavoriteActivity.class);
        context.startActivity(targetIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        getFavoriteData();
    }

    @Override
    public void initParams() {
        mUserManger = UserManager.getInstance();
        mUserModel = mUserManger.getUserModel();
        mUserId = mUserModel == null ? AppConstant.User.USE_ID_DEF : mUserModel.getUserID();
    }

    private void getFavoriteData() {
        favoriteModels.clear();
        showDialog();
        new UserApi().queryFavorite(new ICallback() {
            @Override
            public void onSuccess(Object o, int id) {

                hideDialog();
                List<FavoriteModel> list = Json2Model.parseJsonToList((String) o, RESULT_KEY, FavoriteModel.class);
                if (list != null && list.size() > 0) {
                    favoriteModels.addAll(list);
                }

                freshUI(favoriteModels);

            }

            @Override
            public void onFailure(int code, String msg, int id) {

                hideDialog();
                CommonToast.showToast(mContext, msg);
                showEmptyView("暂无收藏数据");
            }
        }, mUserId);
    }

    /**
     * 刷新界面。
     * 注：这个收藏界面，目前还没有UI,不知道他们是要怎么展示的，
     * 由于时间关系，由ios开发人员跟设计或产品经理去提这事。
     * @param favoriteModels
     */
    private void freshUI(List<FavoriteModel> favoriteModels) {

        if(favoriteModels.size() == 0){
            showEmptyView("暂无收藏数据");
        }else{
            hideEmptyView();
        }
    }

    private void showEmptyView(String emptyInfo) {

        mLayoutEmptyView.setVisibility(View.VISIBLE);
        mEmptyInfo.setText(emptyInfo);
    }

    private void hideEmptyView(){
        mLayoutEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void initTitleBar() {
        commonTopBar.setUpNavigateMode();
        commonTopBar.setTitle(R.string.my_favorite);
    }
}
