package com.bluemobi.wanmen.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.fragment.ContactusFragment;
import com.bluemobi.wanmen.fragment.CoursedescriptionFragment;
import com.bluemobi.wanmen.fragment.IntroductionFragment;
import com.bluemobi.wanmen.fragment.ListFragment;
import com.bluemobi.wanmen.fragment.MycollectionFragment;
import com.bluemobi.wanmen.fragment.PlayingrecordFragment;
import com.bluemobi.wanmen.fragment.PopularcoursesFragment1;
import com.bluemobi.wanmen.fragment.QrCodeFragment;
import com.bluemobi.wanmen.fragment.TitleFragment;
import com.bluemobi.wanmen.fragment.UpDataFragment;
import com.bluemobi.wanmen.listener.OnFocusable;
import com.bluemobi.wanmen.listener.OnOkListener;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.xiaomi.mistatistic.sdk.MiStatInterface;


import java.util.ArrayList;
import java.util.List;



/**
 * 作者： sunll 日期： 2015/7/23 14:20
 */
public class MainActivity extends FragmentActivity {


    private FrameLayout fltitle;
    private FrameLayout fllist;
    private FrameLayout flframe;
    private FragmentManager fragmentManager = getFragmentManager();
    private TitleFragment titleFragment;
    private ListFragment listFragment;
    /**
     * 联系我们
     */
    private ContactusFragment contactusFragment;
    /**
     * 课程简介
     */
    private CoursedescriptionFragment coursedescriptionFragment;
    /**
     * 万门简介
     */
    private IntroductionFragment introductionFragment;
    /**
     * 收藏课程
     */
    private MycollectionFragment mycollectionFragment;
    /**
     * 播放记录
     */
    private PlayingrecordFragment playingrecordFragment;
    /**
     * 热门课程
     */
    private PopularcoursesFragment1 popularcoursesFragment;
    /**
     * 二维码
     */
    private QrCodeFragment qrCodeFragment;
    /**
     * 软件升级
     */
    private UpDataFragment upDataFragment;


    /**
     * 显示图片的选项
     */
    private DisplayImageOptions headOptions;

    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initImageLoader(this);
        getData();
        initUI();
        initData();
//        upDate();
    }

    private void upDate() {
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
//                        Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
//                        Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
//                        Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(mContext);
    }

    private void getData() {
        for (int i = 0; i < BeginActivity.titleList.size(); i++) {
            str_title.add(BeginActivity.titleList.get(i).genres.name);
        }
    }

    private List<String> str_title = new ArrayList<>();
    //    private String[] str_list = {"热门课程", "最新课程", "万门简介", "App二维码", "联系我们", "软件升级"};
    private String tag_title = "首页";
    private String tag_list = "热门课程";
    List<String> left_main = new ArrayList<String>();

    private void initUI() {
        this.flframe = (FrameLayout) findViewById(R.id.fl_frame);
        this.fllist = (FrameLayout) findViewById(R.id.fl_list);
        this.fltitle = (FrameLayout) findViewById(R.id.fl_title);
        titleFragment = new TitleFragment(str_title);
        left_main.add("热门课程");
        left_main.add("最新课程");
        left_main.add("万门简介");
//        left_main.add("App二维码");
        left_main.add("联系我们");
//        left_main.add("软件升级");
        listFragment = new ListFragment(left_main);
        addView(titleFragment, R.id.fl_title);
        addView(listFragment, R.id.fl_list);
        popularcoursesFragment = new PopularcoursesFragment1();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        popularcoursesFragment.setArguments(bundle);
        addView(popularcoursesFragment, R.id.fl_frame);
    }

    private boolean isOk;

    @Override
    protected void onStart() {
        isOk = true;
        super.onStart();
//        Toast.makeText(this, "主界面", Toast.LENGTH_SHORT).show();
//        Log.d("MiStatInterface", "MiStatInterface");
//        MiStatInterface.recordCountEvent("Button_Click", "Button_OK_click");
//        MiStatInterface.recordPageStart(this, "主界面");
    }

    @Override
    protected void onResume() {
        isOk = true;
        super.onResume();
        MiStatInterface.recordPageStart(this, "主界面");
        MiStatInterface.recordCountEvent("主界面", "主界面");
    }

    @Override
    protected void onPause() {
        isOk = false;
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

    /**
     * 对Fragment进行替换
     *
     * @param fg
     * @param fm
     */
    private void replaceView(Fragment fg, int fm) {
        if (!isOk) {
            return;
        }
        FragmentTransaction trans = fragmentManager.beginTransaction();
        try {
            trans.replace(fm, fg);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第一次添加Fragment
     *
     * @param fg
     * @param fm
     */
    private void addView(Fragment fg, int fm) {
        FragmentTransaction trans = fragmentManager.beginTransaction();
        trans.replace(fm, fg);
        trans.commit();
    }

    /**
     * 对主界面Frame位置进行Fragment切换
     *
     * @param title
     * @param list
     */
    private void replaceFrame(String title, String list) {
        Fragment fm = null;
        if (list == null) {
            list = getListFirst(title);
        }
        if (title.equals("首页")) {
            if (list.equals("热门课程")) {
                if (popularcoursesFragment == null) {
                    popularcoursesFragment = new PopularcoursesFragment1();
                }
                popularcoursesFragment.initData(1);
                fm = popularcoursesFragment;
            } else if (list.equals("最新课程")) {
                if (popularcoursesFragment == null) {
                    popularcoursesFragment = new PopularcoursesFragment1();
                }
                popularcoursesFragment.initData(2);
                fm = popularcoursesFragment;
            } else if (list.equals("万门简介")) {
                if (introductionFragment == null) {
                    introductionFragment = new IntroductionFragment();
                }
                fm = introductionFragment;
            } else if (list.equals("App二维码")) {
                if (qrCodeFragment == null) {
                    qrCodeFragment = new QrCodeFragment();
                }
                fm = qrCodeFragment;
            } else if (list.equals("联系我们")) {
                if (contactusFragment == null) {
                    contactusFragment = new ContactusFragment();
                }
                fm = contactusFragment;
            } else if (list.equals("软件升级")) {
                if (upDataFragment == null) {
                    upDataFragment = new UpDataFragment();
                }
                fm = upDataFragment;
            }
        } else if (title.equals("播放记录")) {
            if (playingrecordFragment == null) {
                playingrecordFragment = new PlayingrecordFragment();
            }
            fm = playingrecordFragment;
        } else if (title.equals("收藏课程")) {
            if (mycollectionFragment == null) {
                mycollectionFragment = new MycollectionFragment();
            }
            fm = mycollectionFragment;
        } else {
            if (coursedescriptionFragment == null) {
                coursedescriptionFragment = new CoursedescriptionFragment();
            }
            for (int i = 0; i < BeginActivity.titleList.size(); i++) {
                if (title.equals(BeginActivity.titleList.get(i).genres.name)) {
                    for (int j = 0; j < BeginActivity.titleList.get(i).list.size(); j++) {
                        if (BeginActivity.titleList.get(i).list.get(j).name.equals(list)) {
                            coursedescriptionFragment.setBean(BeginActivity.titleList.get(i).list.get(j));
                            break;
                        }
                    }
                    break;
                }
            }
            fm = coursedescriptionFragment;
        }
        replaceView(fm, R.id.fl_frame);
    }

    /**
     * 根据title的值，返回左侧栏第一个分类的类型
     */
    private String getListFirst(String title) {
        if (title.equals("首页")) {
            return "热门课程";
        }
        for (int i = 0; i < BeginActivity.titleList.size(); i++) {
            if (BeginActivity.titleList.get(i).genres.name.equals(title)) {
                return BeginActivity.titleList.get(i).list.get(0).name;
            }
        }
        return "";
    }

    private void initData() {
        titleFragment.setOnFocusableChangeListener(new OnFocusable() {
            public void isFocusable(boolean isFocusable, View v) {
                TextView tv = (TextView) v;
                if (isFocusable) {
                    TitleFragment.id = v.getId();
                    titleChange(v);
                    tv.setTextColor(getResources().getColor(R.color.selectcolor));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.noselectcolor));
                }
            }
        });
        titleFragment.setOnOkListener(new OnOkListener() {
            @Override
            public void OK(View v) {
//                titleChange(v);
            }
        });
        listFragment.setOnFocusableChangeListener(new OnFocusable() {
            @Override
            public void isFocusable(boolean isFocusable, View v) {
                TextView tv = (TextView) v;
                if (isFocusable) {
                    tv.setTextColor(getResources().getColor(R.color.selectcolor));
                    String str = ((TextView) v).getText().toString();
                    ListFragment.id = v.getId();
                    tag_list = str;
                    replaceFrame(tag_title, str);
                } else {
                    tv.setTextColor(getResources().getColor(R.color.noselectcolor));
                }
            }
        });

        listFragment.setOnOkListener(new OnOkListener() {
            @Override
            public void OK(View v) {
//                String str = ((TextView) v).getText().toString();
//                ListFragment.id=v.getId();
//                tag_list = str;
//                replaceFrame(tag_title, str);
//                Toast.makeText(context, str, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Context context = this;


    /**
     * 处理title的更改选择的事件
     *
     * @param v
     */
    private void titleChange(View v) {
        String str = ((TextView) v).getText().toString();
        tag_title = str;
        replaceFrame(str, null);

//        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        if (str.equals("播放记录")) {
            fllist.setVisibility(View.GONE);
        } else if (str.equals("收藏课程")) {
            fllist.setVisibility(View.GONE);
        } else if (str.equals("首页")) {
            fllist.setVisibility(View.VISIBLE);
            String[] str_t = {"热门课程", "最新课程", "万门简介", "App二维码", "联系我们", "软件升级"};
            listFragment.setStrList(left_main);
        } else {
            fllist.setVisibility(View.VISIBLE);
            for (int i = 0; i < str_title.size(); i++) {
                if (str.equals(BeginActivity.titleList.get(i).genres.name)) {
                    List<String> str_left = new ArrayList<String>();
                    for (int j = 0; j < BeginActivity.titleList.get(i).list.size(); j++) {
                        str_left.add(BeginActivity.titleList.get(i).list.get(j).name);
                    }
                    listFragment.setStrList(str_left);
                }
            }
//
//
//            if (str.equals("中学")) {
//                String[] str_t = {"高中英语", "高中生物", "高中物理", "高中化学", "高中数学", "高中语文", "高中生物", "高中物理", "高中化学", "高中数学", "高中语文", "高中生物", "高中物理", "高中化学", "高中数学", "高中语文"};
//                listFragment.setStrList(str_t);
//            } else if (str.equals("语言")) {
//                String[] str_t = {"四六级", "法语", "日语", "德语", "韩语"};
//                listFragment.setStrList(str_t);
//            } else if (str.equals("大学")) {
//                String[] str_t = {"万有引力", "万门留学", "万门通识", "万门讲座", "人文社会学院"};
//                listFragment.setStrList(str_t);
//            }
        }
    }

    /**
     * ImageLoader 设置项
     *
     * @return
     */
    public DisplayImageOptions getDisplayImageOptions() {
        return headOptions;
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                        // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        headOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.image_loading)
                .showImageForEmptyUri(R.mipmap.image_loading)
                .showImageOnFail(R.mipmap.image_loading).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                        // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("是否退出？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
                break;
            default:
                break;

        }
        return super.onKeyDown(keyCode, event);
    }


}




