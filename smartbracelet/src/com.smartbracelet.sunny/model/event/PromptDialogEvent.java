package com.smartbracelet.sunny.model.event;

import android.content.DialogInterface;

/**
 * 描述：提示对话框
 * 作者： Sunny
 * 日期： 2015-10-19 17:26
 * 版本： v1.0
 */
public class PromptDialogEvent extends BaseEvent {

    private String title;
    private String msg;
    private String positiveInfo;
    private DialogInterface.OnClickListener onPositiveListener;
    private DialogInterface.OnClickListener onCancelListener;

    public PromptDialogEvent() {
    }

    public PromptDialogEvent(String positiveInfo) {
        this.positiveInfo = positiveInfo;
    }

    public String getPositiveInfo() {
        return positiveInfo;
    }

    public void setPositiveInfo(String positiveInfo) {
        this.positiveInfo = positiveInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DialogInterface.OnClickListener getOnPositiveListener() {
        return onPositiveListener;
    }

    public void setOnPositiveListener(DialogInterface.OnClickListener onPositiveListener) {
        this.onPositiveListener = onPositiveListener;
    }

    public DialogInterface.OnClickListener getOnCancelListener() {
        return onCancelListener;
    }

    public void setOnCancelListener(DialogInterface.OnClickListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }
}
