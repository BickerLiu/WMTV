package com.bluemobi.wanmen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluemobi.wanmen.R;


/**
 * 二维码
 */
public class QrCodeFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_qrcode, null);
        return view;
    }
}
