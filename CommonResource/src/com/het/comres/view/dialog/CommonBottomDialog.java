package com.het.comres.view.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.het.comres.R;

/**
 * 由底部弹出的对话框
 * eg.相片选择
 *
 * @author sunny
 * @date 2015年5月21日 下午3:47:16
 */
public class CommonBottomDialog extends BaseDialog {

    public static final String TAG = "CommonBottomDialog";

    private Context mContext;
    private LinearLayout mContainer;

    public CommonBottomDialog(Context context) {
        super(context, R.style.common_dialog_style);
        this.mContext = context;
        initUI();
    }

    private void initUI() {
        View dlgView = LayoutInflater.from(mContext).inflate(R.layout.common_vertical_dialog_layout, null);
        mContainer = (LinearLayout) dlgView.findViewById(R.id.common_dialog_content_container);
        setContentView(dlgView);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        window.setWindowAnimations(R.style.AnimBottom); // 设置显示动画
        window.setGravity(Gravity.BOTTOM); // 设置显示位置
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); // 设置布局大小

    }

    // 设置显示内容
    public void setViewContent(View view) {
        mContainer.addView(view, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dismiss();
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

}
