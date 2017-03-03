package com.bluemobi.wanmen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.finals.Url_Base;
import com.bluemobi.wanmen.listener.callBackListener;
import com.bluemobi.wanmen.utils.NetwoekUtil;
import com.bluemobi.wanmen.utils.SharedPreferencesUtils;
import com.bluemobi.wanmen.bean.NavigationBean;
import com.bluemobi.wanmen.bean.NavigationlistBean;
import com.bluemobi.wanmen.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 作者： sunll 日期： 2015/7/23 14:20
 */

/*开启加载界面*/
public class BeginActivity extends Activity {
    private ProgressBar progressBar;
    private static int width;
    private static int height;

    public static int getHeight() {
        return height;
    }

    public static int getWidht() {
        return width;
    }
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            progressBar.setProgress(msg.what);
//            if (msg.what == 100) {
//                boolean isFirst = (boolean) SharedPreferencesUtils.getParam(BeginActivity.this, SharedPreferencesUtils.ISFIRST, true);
//                if (isFirst) {
//                    SharedPreferencesUtils.setParam(BeginActivity.this, SharedPreferencesUtils.ISFIRST, false);
//                    Intent intent = new Intent(BeginActivity.this, HelpActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Intent intent = new Intent(BeginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_begin);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        if (NetwoekUtil.isNetworkConnected(this)) {
            getTitleList();
        } else {
            Toast.makeText(this, "请检查网络后重试！", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    public static List<NavigationlistBean> titleList = new ArrayList<NavigationlistBean>();
    private int count_flag = 0;

    private void getTitleList() {
        Map<String, String> params = new HashMap<String, String>();
        new NetwoekUtil().setIsShowDialog(false).HttpPost(this, Url_Base.appUrl + Url_Base.course_genres, params, new callBackListener() {
            @Override
            public void isSucess(String data) {
                try {
                    JSONObject jo = new JSONObject(data);
                    JSONArray ja = jo.getJSONArray("genres");
                    titleList.clear();
                    count_flag = 0;
                    for (int i = 0; i < ja.length(); i++) {
                        NavigationlistBean bean = new NavigationlistBean();
                        bean.genres = GsonUtils.json2bean(ja.get(i).toString(), NavigationBean.class);
                        titleList.add(bean);
                    }
                    progressBar.setProgress(100 / (1 + titleList.size()));
                    for (int i = 0; i < titleList.size(); i++) {
                        getLeftList(titleList.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void isError(String error) {
                showError();
            }
        });
    }

    /**
     * 获取二级目录
     *
     * @param titleBean
     */
    private void getLeftList(final NavigationlistBean titleBean) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("genre_id", titleBean.genres.id);
        new NetwoekUtil().setIsShowDialog(false).HttpPost(this, Url_Base.appUrl + Url_Base.course_majors, params, new callBackListener() {
            @Override
            public void isSucess(String data) {
                try {
                    JSONObject jo = new JSONObject(data);
                    JSONArray ja = jo.getJSONArray("majors");
                    titleBean.list = new ArrayList<NavigationBean>();
                    for (int i = 0; i < ja.length(); i++) {
                        NavigationBean bean = GsonUtils.json2bean(ja.get(i).toString(), NavigationBean.class);
                        titleBean.list.add(bean);
                    }
                    count_flag++;
                    progressBar.setProgress(100 * (1 + count_flag) / (1 + titleList.size()));
                    if (count_flag == titleList.size()) {
                        boolean isFirst = (boolean) SharedPreferencesUtils.getParam(BeginActivity.this, SharedPreferencesUtils.ISFIRST, true);
                        if (isFirst) {
                            SharedPreferencesUtils.setParam(BeginActivity.this, SharedPreferencesUtils.ISFIRST, false);
                            Intent intent = new Intent(BeginActivity.this, HelpActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(BeginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void isError(String error) {
                showError();
            }
        });
    }

    private void showError() {
        if (isShowDialog)
            return;
        isShowDialog = true;
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    return true;
                } else {
                    return false; // 默认返回 false
                }

            }
        });
        dialog.show();
        Window win = dialog.getWindow();
        win.setBackgroundDrawable(new ColorDrawable(0X00000000));
        dialog.setCanceledOnTouchOutside(false);
        TextView tv = new TextView(this);
        tv.setText("网络异常，请检查网络后重试！");
        tv.setPadding(20, 10, 20, 10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tv.setBackground(new ColorDrawable(0XFFFFFFFF));
        }
        tv.setFocusable(true);
        tv.setFocusableInTouchMode(true);
        tv.requestFocus();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                BeginActivity.this.finish();
            }
        });
        win.setContentView(tv);
    }

    private boolean isShowDialog;
}
