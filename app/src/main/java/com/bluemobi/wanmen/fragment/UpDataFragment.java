package com.bluemobi.wanmen.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bluemobi.wanmen.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;



/**
 * 软件升级
 */
public class UpDataFragment extends BaseFragment {
    private TextView textView;
    private View progressBar;
    private Context mContext;
    private View updata_logo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_updata, null);
        textView = (TextView) view.findViewById(R.id.textView_update);
        updata_logo = view.findViewById(R.id.updata_logo);
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textView.setBackgroundResource(R.mipmap.auto_update_focus);
                } else {
                    textView.setBackgroundResource(R.mipmap.auto_update);
                }
            }
        });
        textView.setNextFocusUpId(TitleFragment.id);
        textView.setNextFocusLeftId(ListFragment.id);
        textView.setNextFocusRightId(TitleFragment.id);
        textView.setNextFocusDownId(ListFragment.id);

        progressBar = view.findViewById(R.id.progressbar_updata);
        progressBar.setVisibility(View.GONE);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                updata_logo.setVisibility(View.GONE);
                /*友盟更新调用接口*/
                UmengUpdateAgent.setUpdateAutoPopup(false);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                        switch (updateStatus) {
                            case UpdateStatus.Yes: // has update
                                UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
                                break;
                            case UpdateStatus.No: // has no update
                                Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.NoneWifi: // none wifi
                                Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.Timeout: // time out
                                Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        progressBar.setVisibility(View.GONE);
                        updata_logo.setVisibility(View.VISIBLE);
                    }
                });
                UmengUpdateAgent.forceUpdate(mContext);
            }
        });
        return view;
    }
}
