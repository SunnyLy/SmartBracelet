package com.smartbracelet.sunny.ui.fragment.help;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.het.comres.view.dialog.CommPrompDialog;
import com.het.comres.view.layout.ItemLinearLayout;
import com.smartbracelet.sunny.R;
import com.smartbracelet.sunny.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sunny on 2015/11/10.
 * 故障排除
 */
public class HelpSolveFragment extends BaseFragment {

    @InjectView(R.id.help_solve_connect_failure)
    ItemLinearLayout mLayoutConnectFailure;
    @InjectView(R.id.help_solve_ble_disconnected)
    ItemLinearLayout mLayoutDisconnected;
    @InjectView(R.id.help_solve_no_data)
    ItemLinearLayout mLayoutNodata;
    @InjectView(R.id.help_solve_how_updated)
    ItemLinearLayout mLayoutUpdate;
    @InjectView(R.id.help_solve_how_charged)
    ItemLinearLayout mLayoutCharged;

    private CommPrompDialog prompDialog;
    private CommPrompDialog.Builder mBuilder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_help_solve, null);
        ButterKnife.inject(this, content);

        initParams();
        return content;
    }

    private void initParams() {
        mBuilder = new CommPrompDialog.Builder(mContext);
    }


    @OnClick({R.id.help_solve_how_charged, R.id.help_solve_how_updated,
            R.id.help_solve_no_data, R.id.help_solve_ble_disconnected,
            R.id.help_solve_connect_failure})
    @Override
    public void onClick(View v) {
        String dialogInfo = "";
        switch (v.getId()) {
            case R.id.help_solve_how_charged:
                dialogInfo = getString(R.string.help_solve_how_charged);
                break;
            case R.id.help_solve_how_updated:
                dialogInfo = getString(R.string.help_solve_how_update);
                break;
            case R.id.help_solve_no_data:
                dialogInfo = getString(R.string.help_solve_no_data);
                break;
            case R.id.help_solve_ble_disconnected:
                dialogInfo = getString(R.string.help_solve_ble_disconnect);
                break;
            case R.id.help_solve_connect_failure:
                dialogInfo = getString(R.string.help_solve_connect_failure);
                break;
        }

        mBuilder.setMessage(dialogInfo);
        mBuilder.setPositiveButton(getString(R.string.common_sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mBuilder.setNegativeButton(getString(R.string.common_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        prompDialog = mBuilder.create();
        prompDialog.show();

    }
}
