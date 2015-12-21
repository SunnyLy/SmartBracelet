package com.smartbracelet.sunny.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.smartbracelet.sunny.AppApplication;
import com.smartbracelet.sunny.AppConstant;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 处理微信回调
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, AppConstant.WeiXinAppID, true);
        api.registerApp(AppConstant.WeiXinAppID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    public void onReq(BaseReq arg0) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof SendAuth.Resp) {
            //ThirdLoginManager.getWeiXinLogin(AppApplication.getInstance()).onResp(this, (SendAuth.Resp)resp);
            return;
        }
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                break;
            default:
                result = "发送返回";
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        this.finish();
    }

}
