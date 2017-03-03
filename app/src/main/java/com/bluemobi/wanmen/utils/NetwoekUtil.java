package com.bluemobi.wanmen.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ProgressBar;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.listener.callBackListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;



/**
 * 作者： sunll 日期： 2015/7/31 16:16
 */
public class NetwoekUtil {
    private callBackListener listener;
    private Context context;
    private AlertDialog dialog;
    private ProgressBar progressBar1;
    private boolean isShowDialog = true;

    /* 请求示例
      Map<String, String> params = new HashMap<String, String>();
        params.put("genre_id", "2");
        new NetwoekUtil().HttpPost(this, Url_Base.appUrl + Url_Base.course_majors, params, new callBackListener() {
            @Override
            public void isSucess(String data) {

            }

            @Override
            public void isError(String error) {

            }
        });

     */

    /**
     * @param context  上下文环境
     * @param uri      请求地址
     * @param params   请求参数
     * @param listener 回调方法
     */
    public void HttpPost(Context context, final String uri, final Map<String, String> params, callBackListener listener) {
        this.listener = listener;
        this.context = context;
        if (!isNetworkConnected(context)) {
            listener.isError("请检查网络！");
            return;
        }
        showDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = Post(uri, params);
                try {
                    NetworkLog(data + "");
//                    JSONObject jo = new JSONObject(data);
                } catch (Exception e) {
                    strError = "解析失败";
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.obj = data;
                msg.what = 1;
                handler.sendMessage(msg);

            }
        }).start();

    }

    public void HttpGet(Context context, final String uri, final Map<String, String> params, callBackListener listener) {
        this.listener = listener;
        this.context = context;
        if (!isNetworkConnected(context)) {
            listener.isError("请检查网络！");
            return;
        }
        showDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = Get(uri, params);
                try {
                    NetworkLog(data + "");
//                    JSONObject jo = new JSONObject(data);
                } catch (Exception e) {
                    strError = "解析失败";
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.obj = data;
                msg.what = 1;
                handler.sendMessage(msg);

            }
        }).start();

    }


    /**
     * 网络请求时弹窗
     */
    private void showDialog() {
        try {
            if (!isShowDialog)
                return;
            if (dialog == null) {
                dialog = new AlertDialog.Builder(context).create();
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode,
                                         KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            return true;
                        } else {
                            return false; // 默认返回 false
                        }
                    }
                });
            }
            Window win = dialog.getWindow();
            win.setBackgroundDrawable(new ColorDrawable(0X00000000));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            win.setContentView(R.layout.dg_progress);
            progressBar1 = (ProgressBar) win.findViewById(R.id.progressBar1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭弹窗
     */
    private void dissMissDialog() {
        try {
            if (!isShowDialog)
                return;
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String strError = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                dissMissDialog();
                if (msg.obj == null) {
                    listener.isError(strError);
                    NetworkLog(strError);
                } else {
                    listener.isSucess((String) msg.obj);
                    NetworkLog("Success");
                }
            }
        }
    };

    public String Post(String uri, Map<String, String> params) {
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(5 * 1000);
            connection.connect();
            PrintWriter pw = new PrintWriter(connection.getOutputStream());
            StringBuffer paramStr = new StringBuffer();
            Iterator it = params.entrySet().iterator();
            boolean isFirst = true;
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (isFirst) {
                    isFirst = false;
                } else {
                    paramStr.append("&");
                }
                paramStr.append(key).append("=").append(value);
            }
            if (isFirst) {
                NetworkLog(uri);
            } else {
                NetworkLog(uri + "?" + paramStr.toString());
            }
            pw.print(paramStr.toString());
            pw.flush();
            pw.close();
            InputStream inStream = connection.getInputStream();
            String data = new String(readInputStream(inStream));
            return data;
        } catch (Exception e) {
            strError = "请求失败！";
            e.printStackTrace();
            return null;
        }
    }

    public String Get(String uri, Map<String, String> params) {
        HttpURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(5 * 1000);
            int responseCode = connection.getResponseCode();
            Log.i("responseCode",responseCode + "");
            InputStream inStream = connection.getInputStream();
            String data = getStringFromInputStream(inStream);
            return data;
        } catch (Exception e) {
            strError = "请求失败！";
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据流返回一个字符串信息         *
     * @param is
     * @return
     * @throws IOException
     */
    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 模板代码 必须熟练
        byte[] buffer = new byte[1024];
        int len = -1;
        // 一定要写len=is.read(buffer)
        // 如果while((is.read(buffer))!=-1)则无法将数据写入buffer中
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        os.close();
        return state;
    }

    /**
     * 判断是否连接网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 从输入流中读取数据
     *
     * @param inStream
     * @throws Exception
     */
    public byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }

    /**
     * 设置网络请求时是否弹出进度条
     *
     * @param isShowDialog
     */
    public NetwoekUtil setIsShowDialog(boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
        return this;
    }

    /**
     * 打印log
     *
     * @param str
     */
    public static void NetworkLog(String str) {
//        Log.i("NetWork", str);
    }
}
