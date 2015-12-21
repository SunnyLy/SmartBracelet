package com.smartbracelet.sunny.manager.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import com.het.comres.view.dialog.PromptUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class ShareManager {
    private IWXAPI api;
    private Context context;
    private WeiXin weixin;
    private String path;
    private String description;

    private String mShareTitle;
    private String mShareImagePath;
    private String mShareSummary;

    private Bitmap bitmap;

    /**
     * 微博分享的接口实例
     */
    private IWeiboShareAPI mWeiboShareAPI;
    private boolean isInstalledWeibo;
    private int supportApiLevel;

    public String getmShareTitle() {
        return mShareTitle;
    }

    public void setmShareTitle(String mShareTitle) {
        this.mShareTitle = mShareTitle;
    }

    public String getmShareSummary() {
        return mShareSummary;
    }

    public void setmShareSummary(String mShareSummary) {
        this.mShareSummary = mShareSummary;
    }


    public String getmShareImagePath() {
        return mShareImagePath;
    }

    public void setmShareImagePath(String mShareImagePath) {
        this.mShareImagePath = mShareImagePath;
    }

    public ShareManager(Context context) {
        super();
        this.context = context;
        // weixin
        initWeixin(context);
        // 新浪微博
        initSinaWeibo();
    }

    private void initWeixin(Context context) {
        api = WXAPIFactory.createWXAPI(context, AppConstant.WeiXinAppID, false);
        api.registerApp(AppConstant.WeiXinAppID);
        weixin = new WeiXin(context);
    }

    private void initSinaWeibo() {
        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context,
                AppConstant.SINA_APP_KEY);
        // 注册应用
        mWeiboShareAPI.registerApp();

        // 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
        isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
        supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI();

    }

    /**
     * 分享至微信
     *
     * @param targetUrl 分享内容的链接地址
     * @param title     标题
     * @param content   内容摘要
     * @param shareType 分享类别：1，分享至微信 2，分享至朋友圈
     */
    public void shareToWeixin(String targetUrl, String title, String imgUrl, String content,
                              int shareType) {
        if (api.isWXAppInstalled()) {
//			Bitmap bitmap = BitmapFactory.decodeResource(
//					context.getResources(), R.drawable.ic_logo);
            if (shareType == 1) {
                //weixin.sharePicTofriend(targetUrl, title, content, imgUrl);
                new WeiXin(context).shareWebToFriend(targetUrl, title, content, imgUrl);
            } else if (shareType == 2) {
                //weixin.sharePicToFriendCircle(targetUrl, title, content, imgUrl);
                new WeiXin(context).shareWebToFriendCircle(targetUrl, title, content, imgUrl);
            }
        } else {
            PromptUtil.showToast(context, "您还没有安装微信，请您先安装微信~");
        }
    }

    /**
     * 分享至新浪微博客户端
     */
    public void shareToSinaClient() {
        if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
            sendMultiMessage();
        } else {
            PromptUtil.showToast(context,
                    R.string.share_sina_not_installed);
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。 注意：当
     * {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     *
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     * @param hasVoice   分享的内容是否有声音
     */
    private void sendMultiMessage() {

        // 1. 初始化微博的分享消息
        TextObject textObj = new TextObject();
        textObj.title = getmShareTitle();
        textObj.text = getmShareSummary();
        // imageObject
        ImageObject imageObj = new ImageObject();
        imageObj.imagePath = getmShareImagePath();
        // WebpageObject
        WebpageObject webObj = new WebpageObject();

        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = textObj;

        weiboMessage.imageObject = imageObj;

        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        weiboMessage.mediaObject = webObj;

        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboShareAPI.sendRequest((Activity) context, request);

    }

}
