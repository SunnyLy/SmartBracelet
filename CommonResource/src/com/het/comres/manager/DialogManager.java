package com.het.comres.manager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.het.comres.view.dialog.CommPrompDialog;

public class DialogManager {

    private Dialog mDialog;

    private CommPrompDialog.Builder mBuilder;
    private Context mContext;

    public DialogManager(Context context) {
        this.mContext = context;
        if (mBuilder == null) {
            this.mBuilder = new CommPrompDialog.Builder(context);
        }

        if (mDialog == null) {
            this.mDialog = mBuilder.create();
        }
    }


    public void showDialog(String title,
                           String msg, String positive, String negative, DialogInterface.OnClickListener postiveListener) {
        mBuilder.setTitle(title);
        mBuilder.setMessage(msg);
        mBuilder.setPositiveButton(positive, postiveListener);
        mBuilder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if (mDialog != null)
            mDialog.show();
    }

    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

}
