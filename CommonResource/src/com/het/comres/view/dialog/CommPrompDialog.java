package com.het.comres.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.het.comres.R;

/**
 * common promt dialog
 *
 * @author sunny
 * @date 2015年5月6日 上午11:07:46
 */
public class CommPrompDialog extends BaseDialog {
    public CommPrompDialog(Context context) {
        super(context);
    }

    public CommPrompDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener,
                negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示对话框消息
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 用资源文件来设置提示对话框消息
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * 设置对话框标题
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * 设置对话框标题（字符串）
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * 为对话框设置一个视图
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * 设置确认键
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * 设置确认键
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * 取消键
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * 取消键
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * 创建对话框
         */
        public CommPrompDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CommPrompDialog dialog = new CommPrompDialog(context,
                    R.style.common_dialog_style);
            View layout = inflater.inflate(R.layout.common_layout_dialog, null);
            // dialog.addContentView(layout, new LayoutParams(
            // 100, LayoutParams.WRAP_CONTENT));
            dialog.addContentView(layout, new LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            // set the dialog title
            LinearLayout ll_title = (LinearLayout) layout
                    .findViewById(R.id.ll_comm_dialog_title);
            TextView tv = (TextView) layout.findViewById(R.id.tv_comm_dialog_title);
            if (TextUtils.isEmpty(title)) {
                ll_title.setVisibility(View.GONE);
            } else {
                ll_title.setVisibility(View.VISIBLE);
                tv.setText(title);
            }
            // set the confirm button
            if (!TextUtils.isEmpty(positiveButtonText)) {
                Button btn_positive = (Button) layout
                        .findViewById(R.id.btn_comm_dialog_positive);
                btn_positive.setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    layout.findViewById(R.id.btn_comm_dialog_positive)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_comm_dialog_positive).setVisibility(View.GONE);
            }
            // set the cancel button
            if (!TextUtils.isEmpty(negativeButtonText)) {
                ((Button) layout.findViewById(R.id.btn_comm_dialog_negative))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    layout.findViewById(R.id.btn_comm_dialog_negative)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_comm_dialog_negative).setVisibility(View.GONE);
            }
            // set the content message
            if (!TextUtils.isEmpty(message)) {
                ((TextView) layout.findViewById(R.id.tv_comm_dialog_message))
                        .setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(
                        contentView, new LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);

            // ///显示的位置
            Window dialogWindow = dialog.getWindow();
            // //窗口的布局参数LayoutParams
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            // lp.alpha=0.7f;////透明度
            // lp.width=300;////宽度
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;// //宽度
            lp.height = LayoutParams.WRAP_CONTENT;// /高度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL
                    | Gravity.CENTER_VERTICAL);
            return dialog;
        }

    }

}
