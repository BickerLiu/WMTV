package com.bluemobi.wanmen.fragment;

import android.app.Fragment;
import android.view.View;

import com.bluemobi.wanmen.activity.BeginActivity;
import com.bluemobi.wanmen.listener.OnFocusable;
import com.bluemobi.wanmen.listener.OnOkListener;

/**
 * 作者： sunll 日期： 2015/7/23 14:20
 */
public class BaseFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {
    private OnFocusable OnFocusableChangeListener;
    private OnOkListener onOkListener;

    public BaseFragment() {

    }


    public void setOnFocusableChangeListener(OnFocusable OnFocusableChangeListener) {
        this.OnFocusableChangeListener = OnFocusableChangeListener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (OnFocusableChangeListener != null) {
            OnFocusableChangeListener.isFocusable(hasFocus, v);
        }
    }

    @Override

    public void onClick(View v) {
        if (onOkListener != null) {
            onOkListener.OK(v);
        }

    }

    public void setOnOkListener(OnOkListener onOkListener) {
        this.onOkListener = onOkListener;
    }
    public  int getHeight() {
        return BeginActivity.getHeight();
    }

    public  int getWidht() {
        return BeginActivity.getWidht();
    }
}
