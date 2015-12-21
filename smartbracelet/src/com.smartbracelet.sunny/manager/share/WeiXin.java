package com.smartbracelet.sunny.manager.share;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.smartbracelet.sunny.AppConstant;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.utils.ImageDownloader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WeiXin {

    private Context context;
    private IWXAPI api;
    private static final int THUMB_SIZE = 120;

    public WeiXin(Context context) {
        super();
        this.context = context;
        api = WXAPIFactory.createWXAPI(context, AppConstant.WeiXinAppID);
        api.registerApp(AppConstant.WeiXinAppID);
    }

//	/*
//	 * 分享信息给朋友
//	 */
//	public void sharePicTofriend(String targetUrl, String title,
//			String description, String imgUrl) {
//		final WXMediaMessage msg = new WXMediaMessage();
//		msg.description = description;
//		msg.title = description;
//
////		final String imageUrl = "http://avatar.csdn.net/6/6/D/1_lfdfhl.jpg";
//		final ImageCache imageCache = new ImageCache() {
//			@Override
//			public void putBitmap(String key, Bitmap value) {
//				lruCache.put(key, value);
//			}
//
//			@Override
//			public Bitmap getBitmap(String key) {
//				Bitmap bm = lruCache.get(key);
//				if (bm != null) {
//					WXImageObject imgObj = new WXImageObject(bitmap);
//					// imgObj.imageUrl = imgUrl;//只能是10kb
//					msg.mediaObject = imgObj;
//					Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap,
//							THUMB_SIZE, THUMB_SIZE, true);
//					// msg.thumbData = Util.bmpToByteArray(thumbBmp, true); //
//					// 设置缩略图
//					msg.thumbData = BitmapUtils.createThumbBitmap(thumbBmp,
//							true);
//
//				}
//				SendMessageToWX.Req req = new SendMessageToWX.Req();
//				req.transaction = buildTransaction("img");
//				req.message = msg;
//				req.scene = SendMessageToWX.Req.WXSceneSession;
//				api.sendReq(req);
//				return bm;
//			}
//		};
//
//	}

//	/*
//	 * 分享图片信息到朋友圈
//	 */
//	public void sharePicToFriendCircle(String targetUrl, String title,
//			String description, String imgUrl) {
//		// api = WXAPIFactory.createWXAPI(context, AppConstant.WeiXinAppID);
//		final WXMediaMessage msg = new WXMediaMessage();
//		msg.title = description;
//		msg.description = description;
//
//		final ImageCache imageCache = new ImageCache() {
//			@Override
//			public void putBitmap(String key, Bitmap value) {
//				lruCache.put(key, value);
//			}
//
//			@Override
//			public Bitmap getBitmap(String key) {
//				Bitmap bm = lruCache.get(key);
//				if (bm != null) {
//					WXImageObject imgObj = new WXImageObject(bm);
//					// imgObj.imageUrl = imgUrl;
//					msg.mediaObject = imgObj;
//					Bitmap thumbBmp = Bitmap.createScaledBitmap(bm, THUMB_SIZE,
//							THUMB_SIZE, true);
//					msg.thumbData = BitmapUtils.createThumbBitmap(thumbBmp,
//							true);
//
//				}
//				SendMessageToWX.Req req = new SendMessageToWX.Req();
//				req.transaction = buildTransaction("img");
//				req.message = msg;
//				req.scene = SendMessageToWX.Req.WXSceneTimeline;
//				api.sendReq(req);
//				return bm;
//			}
//		};
//		initImageLoader(imgUrl, imageCache);
//	}

	/*
     * 分享web网页给朋友
	 * 
	 * 特别注意（微信对bitmap有严格的要求，不能超过一定尺寸，否则无法分享这条记录）；
	 */

    public void shareWebToFriend(String url, String title, String des,
                                 String imgUrl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = des;
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.scene = SendMessageToWX.Req.WXSceneSession;
        downloadImgAndPost(imgUrl, msg, api, req);
    }

	/*
	 * 分享web网页到朋友圈
	 * 
	 * 特别注意（微信对bitmap有严格的要求，不能超过一定尺寸，否则无法分享这条记录）；
	 */

    public void shareWebToFriendCircle(String url, String title, String des, String imgUrl) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = des;
        msg.description = des;
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        downloadImgAndPost(imgUrl, msg, api, req);
    }

    /**
     * 下载图片并发送给微信
     *
     * @param imgUrl
     * @param msg
     * @param api
     * @param req
     */
    private void downloadImgAndPost(String imgUrl, final WXMediaMessage msg, final IWXAPI api, final SendMessageToWX.Req req) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        ImageDownloader.getInstance(context).download(imgUrl, new ImageDownloader.OnDownloadCompleteListener() {
            @Override
            public void onDownloadSuccess(Bitmap bmp) {
                dialog.dismiss();
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//					msg.thumbData = BitmapUtils.createThumbBitmap(thumbBmp, true);
                msg.setThumbImage(thumbBmp);
                req.message = msg;
                api.sendReq(req);
            }

            @Override
            public void onDownloadFail() {
                dialog.dismiss();
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//					msg.thumbData = BitmapUtils.createThumbBitmap(thumbBmp, true);
                msg.setThumbImage(thumbBmp);
                req.message = msg;
                api.sendReq(req);
            }
        });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }
}
