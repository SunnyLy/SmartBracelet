package com.het.comres.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.het.comres.R;

/**
 * 提示信息工具类
 *
 * @author gary
 * @date 2014-4-14
 */
public class PromptUtil {

    public static void showToast(Context context, String str) {
        CommonToast.showToast(context, str);
    }

    public static void showToast(Context context, int strRes) {
        CommonToast.showToast(context, context.getResources().getString(strRes));
    }

    public static void showShortToast(Context context, String prompt) {
        showToast(context, prompt);

    }

    public static void showLongToast(Context context, String prompt) {
        showToast(context, prompt);
    }

    public static void noNetWorkToast(Context context) {
        showToast(context, R.string.common_no_network);
    }

    public static void showPromptDialog(final Context mContext, String title,
                                        String msg, String positvieBtn, DialogInterface.OnClickListener onPositiveListener
    ) {
        Dialog d = null;
        CommPrompDialog.Builder b = new CommPrompDialog.Builder(mContext);
        if (!TextUtils.isEmpty(title))
            b.setTitle(title);
        b.setMessage(msg);
        b.setPositiveButton(
                TextUtils.isEmpty(positvieBtn) ? mContext.getResources().getString(R.string.common_sure) : positvieBtn,
                onPositiveListener);
        b.setNegativeButton(
                mContext.getResources().getString(R.string.common_cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        d = b.create();
        d.show();
    }


}
