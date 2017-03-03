package com.bluemobi.wanmen.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.bean.CourseInfoBean;
import com.bluemobi.wanmen.bean.CourseListItem;
import com.bluemobi.wanmen.bean.MyCollectionBean;
import com.bluemobi.wanmen.bean.PlayingRecordBean;
import com.bluemobi.wanmen.db.MyCollectionDao;
import com.bluemobi.wanmen.finals.Url_Base;
import com.bluemobi.wanmen.fragment.PlayListFragment;
import com.bluemobi.wanmen.fragment.TeacherInfoFragment;
import com.bluemobi.wanmen.listener.OnLoadCompleteListener;
import com.bluemobi.wanmen.listener.OnPlayListClickListener;
import com.bluemobi.wanmen.listener.callBackListener;
import com.bluemobi.wanmen.utils.GsonUtils;
import com.bluemobi.wanmen.utils.ImportJCplayer;
import com.bluemobi.wanmen.utils.NetwoekUtil;
import com.bokecc.sdk.mobile.exception.ErrorCode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class CourseDetailsActivity1 extends AppCompatActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnBufferingUpdateListener, OnPlayListClickListener, OnLoadCompleteListener, MediaPlayer.OnPreparedListener {

    /*播放列表fragment*/
    private PlayListFragment playListFragment;
    /*老师介绍fragment*/
    private TeacherInfoFragment teacherInfoFragment;
    private TextView tv_back, tv_collect, tv_directory, tv_teacher,  tv_title, tv_description, tv_number;
    private RelativeLayout relativeLayout;
    private int course_id;

    private ImportJCplayer player;

    /*用于存储屏幕宽高和通知栏宽高*/
    int[] location=new int[2];
    int h=0;
    int normalweight,normalheight;
    private boolean isfullscreen=false;


    private String videoId;
    private Handler handler;


    private Handler playerHandler;
    private Timer timer = new Timer();
    private TimerTask timerTask;

    private Boolean isPlaying;
    // 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
    private boolean isFreeze = false;
    private boolean isSurfaceDestroy = false;
    private boolean isPrepared;
    private int currentPosition;
    private Dialog dialog;
    private CourseInfoBean courseInfoBean;
    /**
     * 收藏ＤＢ
     */
    private MyCollectionDao myCollectionDao;

    private int discourse;
    private int part;
    private Bundle bundle;
   private ImageView backimg;
  private  TextView fullscreen;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_course_details1);
        //初始化数据
        init();
        //初始化布局
        initView();

    }


    public void init() {

        myCollectionDao = new MyCollectionDao(this);
        bundle = getIntent().getExtras();
     //   course_id = bundle.getInt("course_id");
        course_id=123;



    }

//    public int selectId;

    /*初始化*/
    public void initView() {

        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout_coursedetail);
        player= (ImportJCplayer) findViewById(R.id.jiecaoplayer_course);
        fullscreen= (TextView) findViewById(R.id.tofullscreen);
        relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                fullscreen.requestFocus();
                player.setBackgroundResource(R.drawable.fullscreencheck_shape);
            }
        });


        player.setUp("http://media.wanmen.org/250a0e03ec17570fd6056e981576a9f1_pc_high.m3u8", JCVideoPlayer.SCREEN_LAYOUT_LIST,"");
        backimg= (ImageView) findViewById(R.id.back_img_course);

        backimg.post(new Runnable() {
            @Override
            public void run() {

                backimg.getLocationInWindow(location);
                h=getStatusBarHeight(CourseDetailsActivity1.this);
                normalweight=backimg.getWidth();
                normalheight=backimg.getHeight();
                FrameLayout.LayoutParams layoutParam = (FrameLayout.LayoutParams) player.getLayoutParams();
                layoutParam.width=normalweight;
                layoutParam.height=normalheight;
                if (h>0)
                {
                    layoutParam.setMargins(location[0],location[1]-h,0,0);
                }
               else
                {
                    layoutParam.setMargins(location[0],location[1],0,0);
                }
                player.setLayoutParams(layoutParam);
                fullscreen.setLayoutParams(layoutParam);
                player.setVisibility(View.VISIBLE);
                fullscreen.setVisibility(View.VISIBLE);
                player.fullscreenButton.setVisibility(View.GONE);
                player.startButton.setVisibility(View.GONE);
                player.startButton.performClick();


            }
        });
        tv_number = (TextView) findViewById(R.id.textView_course_number);
        tv_back = (TextView) findViewById(R.id.textView_course_back);
        tv_back.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_back.setTextColor(Color.rgb(35, 155, 240));
                    tv_back.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.back_course_blue, 0, 0, 0);
                } else {
                    tv_back.setTextColor(Color.WHITE);
                    tv_back.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.back_course, 0, 0, 0);
                }
            }
        });
        tv_collect = (TextView) findViewById(R.id.textView_course_collection);
        tv_collect.setNextFocusRightId(R.id.textView_course_directory);

        tv_collect.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    tv_collect.setTextColor(Color.rgb(35, 155, 240));
                    tv_collect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.collection_course_blue, 0, 0, 0);
                } else {
                    tv_collect.setTextColor(Color.WHITE);
                    tv_collect.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.collection_course, 0, 0, 0);
                }
            }
        });
        if (myCollectionDao.selectMyCollection(course_id + "") != null) {
            tv_collect.setText("取消收藏");
        } else {
            tv_collect.setText("收藏");
        }
        tv_directory = (TextView) findViewById(R.id.textView_course_directory);
//        selectId = tv_directory.getId();
        tv_directory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_directory.setTextColor(Color.rgb(35, 155, 240));
                    tv_teacher.setBackgroundResource(R.drawable.title_background);
                    initFragment(courseInfoBean, 0);
                } else {
                    tv_directory.setTextColor(Color.WHITE);
                    tv_directory.setBackgroundResource(R.drawable.title_backgroup_select);
                }
            }
        });
        tv_teacher = (TextView) findViewById(R.id.textView_course_teacher);
        tv_teacher.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_teacher.setTextColor(Color.rgb(35, 155, 240));
                    tv_directory.setBackgroundResource(R.drawable.title_background);
                    initFragment(courseInfoBean, 1);
                } else {
                    tv_teacher.setTextColor(Color.WHITE);
                    tv_teacher.setBackgroundResource(R.drawable.title_backgroup_select);
                }
            }
        });

        tv_title = (TextView) findViewById(R.id.textView_course_name);
        tv_description = (TextView) findViewById(R.id.textView_course_description);
   }

    /*点击事件*/
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_course_back:
                Toast.makeText(CourseDetailsActivity1.this,"backclick",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.textView_course_collection:
                isCollect();
                break;
            case R.id.textView_course_teacher:
                if (courseInfoBean != null) {
                    initFragment(courseInfoBean, 1);
                }
                break;
            case R.id.textView_course_directory:
                if (courseInfoBean != null) {
                    initFragment(courseInfoBean, 0);
                }
                break;


          case R.id.tofullscreen:

              if (isfullscreen!=true)
              {
                  DisplayMetrics metric = new DisplayMetrics();
                  getWindowManager().getDefaultDisplay().getMetrics(metric);
                  int width = metric.widthPixels;     // 屏幕宽度（像素）
                  int height = metric.heightPixels;
                  FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) player.getLayoutParams();
                  if (h>=0)
                  {
                      layoutParams.width=width;
                      layoutParams.height=height-h;
                      layoutParams.setMargins(0,0,0,0);
                  }
                  player.setLayoutParams(layoutParams);

                  isfullscreen=true;



              }break;

            default:
                break;
        }
    }



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
//
                if (isfullscreen==true)
                {
                    FrameLayout.LayoutParams layoutParam = (FrameLayout.LayoutParams) player.getLayoutParams();
                    layoutParam.width=normalweight;
                    layoutParam.height=normalheight;
                    if (h>0)
                    {
                        layoutParam.setMargins(location[0],location[1]-h,0,0);
                    }
                    else
                    {
                        layoutParam.setMargins(location[0],location[1],0,0);
                    }
                    player.setLayoutParams(layoutParam);
                    isfullscreen=false;
                }
                return true;


            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (isfullscreen=true)
                {  if (JCMediaManager.instance().mediaPlayer.getCurrentPosition()<=4000)
                {
                    JCMediaManager.instance().mediaPlayer.seekTo(0);
                }
                else
                {
                    JCMediaManager.instance().mediaPlayer.seekTo(JCMediaManager.instance().mediaPlayer.getCurrentPosition()-4000);
                }}
                break;
            case KeyEvent.KEYCODE_ENTER :

                if (isfullscreen==true)
                {
                    if (JCMediaManager.instance().mediaPlayer.isPlaying())
                    {
                        JCMediaManager.instance().mediaPlayer.pause();
                    }else
                    {
                        JCMediaManager.instance().mediaPlayer.start();
                    }
                }

                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (isfullscreen==true)
                {
                    if (JCMediaManager.instance().mediaPlayer.getCurrentPosition()>=JCMediaManager.instance().mediaPlayer.getDuration()-4000)
                {
                    JCMediaManager.instance().mediaPlayer.seekTo(JCMediaManager.instance().mediaPlayer.getDuration());
                }
                else
                {
                    JCMediaManager.instance().mediaPlayer.seekTo(JCMediaManager.instance().mediaPlayer.getCurrentPosition()+4000);

                }}
                break;
        }
        return super.onKeyDown(keyCode, event);
    }





    /*初始化fragment*/
    public void initFragment(CourseInfoBean courseInfoBean, int i) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (i) {
            case 0:
                if (playListFragment == null) {
                    playListFragment = new PlayListFragment();
                    PlayingRecordBean playingRecordBean = (PlayingRecordBean) getIntent().getExtras().getSerializable(PlayingRecordBean.tag);
                    Bundle bundle = new Bundle();
                    bundle.putInt("course_id", course_id);
                    if (playingRecordBean != null) {
                        if (playingRecordBean.flag == 2) {
                            bundle.putInt("part", Integer.parseInt(playingRecordBean.part));
                        } else {
                            bundle.putInt("part", Integer.parseInt(playingRecordBean.part) - 1);
                        }
                        bundle.putInt("num", Integer.parseInt(playingRecordBean.discourse) - 1);

                    }
                    playListFragment.setArguments(bundle);
                    transaction.add(R.id.frameLayout_content, playListFragment);
                }
                if (teacherInfoFragment != null) {
                    transaction.hide(teacherInfoFragment);
                }
                if (playListFragment != null) {
                    transaction.hide(playListFragment);
                }
                transaction.show(playListFragment);
                break;
            case 1:
                if (null == teacherInfoFragment) {
                    teacherInfoFragment = new TeacherInfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("teacher_name", courseInfoBean.getCourse().getTeacher_name());
                    bundle.putString("teacher_info", courseInfoBean.getCourse().getTeacher_description());
                    bundle.putString("teacher_img", courseInfoBean.getCourse().getTeacher_avatar());
                    teacherInfoFragment.setArguments(bundle);
                    transaction.add(R.id.frameLayout_content, teacherInfoFragment);
                }
                if (teacherInfoFragment != null) {
                    transaction.hide(teacherInfoFragment);
                }
                if (playListFragment != null) {
                    transaction.hide(playListFragment);
                }
                transaction.show(teacherInfoFragment);
                break;
            default:
                break;
        }

        transaction.commit();
    }


    //点击播放列表回调函数
    @Override
    public void onPlayListClick(String str, int i, int j) {
        if (i == discourse && j == part) {
            return;
        }
        currentPosition = 0;
        discourse = i;
        part = j;
//        if (player != null) {
//            player.release();
//        }
        if (str != null) {
        //    progressBar.setVisibility(View.VISIBLE);
            videoId = str;



//            callback.surfaceCreated(surfaceHolder);

        } else {
            Toast.makeText(this, "暂无此视频", Toast.LENGTH_SHORT).show();
        }
    }


    //Fragment加载完毕播放第一条
    @Override
    public void loadComplete(List<CourseListItem> list) {
        if (list != null) {
            tv_number.setText("共" + list.size() + "讲");
//            if (player != null) {
//                player.release();
//            }
            PlayingRecordBean playingRecordBean = (PlayingRecordBean) bundle.getSerializable(PlayingRecordBean.tag);
            if (playingRecordBean != null) {
                if (playingRecordBean.discourse == null) {
                    return;
                }
                if (playingRecordBean.part == null) {
                    return;
                }
                discourse = Integer.parseInt(playingRecordBean.discourse) - 1;
                part = Integer.parseInt(playingRecordBean.part) - 1;
                switch (playingRecordBean.flag) {
                    case 0:
                        currentPosition = 0;
                        videoId = list.get(discourse).getList().get(part).getCc_video_link();
                        break;
                    case 1:
                        currentPosition = Integer.parseInt(playingRecordBean.time);
                        videoId = list.get(discourse).getList().get(part).getCc_video_link();
                        break;
                    case 2:
                        currentPosition = 0;
                        if (part < list.get(discourse).getList().size() - 1) {
                            part += 1;
                            videoId = list.get(discourse).getList().get(part).getCc_video_link();
                        } else {
                            if (discourse < list.size() - 1) {
                                discourse += 1;
                                if (list.get(discourse).getList().size() > 0) {
                                    part = 0;
                                    videoId = list.get(discourse).getList().get(0).getCc_video_link();
                                }
                            } else {
//                                Toast.makeText(CourseDetailsActivity1.this, "已是最后一集", Toast.LENGTH_SHORT).show();
                                discourse = list.size() - 1;
                                if (list.get(discourse).getList().size() > 0) {
                                    part = 0;
                                    videoId = list.get(discourse).getList().get(0).getCc_video_link();
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            } else {
                currentPosition = 0;
                videoId = list.get(0).getList().get(0).getCc_video_link();
            }

        } else {
            Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            Bundle bundle = data.getExtras();
            currentPosition = bundle.getInt("currentPosition");
        }
    }


    //准备视频
    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        if (!isFreeze) {
//            if (isPlaying == null || isPlaying.booleanValue()) {
//                player.setDisplay(surfaceHolder);
//                if (progressBar.isShown()) {
//                    progressBar.setVisibility(View.GONE);
//                }
//                player.start();
//                image_start.setImageResource(R.mipmap.pause);
//            }

        }

        if (currentPosition > 0) {
//            player.seekTo(currentPosition);
        }


//        tv_time.setText("00:00:00/" + ParamsUtil.millsecondsToStr(player.getDuration()));
//        tv_timefull.setText("00:00:00/" + ParamsUtil.millsecondsToStr(player.getDuration()));
    }

    //缓冲视频
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
      //  seekBar.setSecondaryProgress(percent);
      //  seekBarFull.setSecondaryProgress(percent);
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }


    //视频播放出错提示
    private Handler alertHandler = new Handler() {

        AlertDialog.Builder builder;
        AlertDialog.OnClickListener onClickListener = new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        };

        @Override
        public void handleMessage(Message msg) {

            String message = "";
            boolean isSystemError = false;
            if (ErrorCode.INVALID_REQUEST.Value() == msg.what) {
                message = "无法播放此视频，请检查视频状态";
            } else if (ErrorCode.NETWORK_ERROR.Value() == msg.what) {
                message = "无法播放此视频，请检查网络状态";
            } else if (ErrorCode.PROCESS_FAIL.Value() == msg.what) {
                message = "无法播放此视频，请检查帐户信息";
            } else {
                isSystemError = true;
            }

            if (!isSystemError) {
                builder = new AlertDialog.Builder(CourseDetailsActivity1.this);
                dialog = builder.setTitle("提示").setMessage(message)
                        .setPositiveButton("OK", onClickListener)
                        .setCancelable(false).show();
            }

            super.handleMessage(msg);
        }

    };

    //视频播放出错监听
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Message msg = new Message();
        msg.what = what;
        if (alertHandler != null) {
            alertHandler.sendMessage(msg);
        }
        return false;
    }


    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    @Override
    public void onPause() {
//        if (isPrepared) {
////            // 如果播放器prepare完成，则对播放器进行暂停操作，并记录状态
////            if (player.isPlaying()) {
////                isPlaying = true;
////            } else {
////                isPlaying = false;
////            }
////            player.pause();
//        } else {
//            // 如果播放器没有prepare完成，则设置isFreeze为true
//            isFreeze = true;
//        }
       ImportJCplayer.releaseAllVideos();
       // JCMediaManager.instance().mediaPlayer.pause();
        super.onPause();
    }
    @Override
    public void onBackPressed() {
        if (ImportJCplayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onDestroy() {
        if (timerTask != null) {
            timerTask.cancel();
        }
//        alertHandler.removeCallbacksAndMessages(null);
//        alertHandler = null;

//        if (player != null) {
//            player.release();
//            player = null;
//        }

        if (dialog != null) {
            dialog.dismiss();
        }


        super.onDestroy();
    }



    //退出时
    @Override
    public void finish() {

        myCollectionDao.close();

        super.finish();
    }

    //是否收藏
    public void isCollect() {
        if (courseInfoBean == null) {
            return;
        }
        if (myCollectionDao.selectMyCollection(course_id + "") != null) {//已收藏
            myCollectionDao.delectMyCollection(course_id + "");
            tv_collect.setText("收藏");
        } else {
            MyCollectionBean bean = new MyCollectionBean();
            bean.image_url = courseInfoBean.getCourse().getTeacher_avatar();
            bean.video_name = courseInfoBean.getCourse().getName();
            bean.video_id = course_id + "";
            myCollectionDao.insertPlayingRecord(bean);
            tv_collect.setText("取消收藏");
        }
    }


    //加载数据
    public void initData() {
        String url = Url_Base.appUrl + Url_Base.course_info;
        Map<String, String> params = new HashMap();
        params.put(Url_Base.course_id, course_id + "");
        new NetwoekUtil().HttpPost(CourseDetailsActivity1.this, url, params, new callBackListener() {
            @Override
            public void isSucess(String data) {
                NetwoekUtil.NetworkLog(data);
                if (data != null) {
                    bindData(data);
                } else {
                    Toast.makeText(CourseDetailsActivity1.this, "数据加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void isError(String error) {
                Toast.makeText(CourseDetailsActivity1.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //绑定数据
    public void bindData(String data) {
        courseInfoBean = GsonUtils.json2bean(data, CourseInfoBean.class);
        tv_title.setText(courseInfoBean.getCourse().getName());
        tv_description.setText(courseInfoBean.getCourse().getDescription());
        //默认显示播放列表
        initFragment(courseInfoBean, 0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    //获取通知栏高度
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

}
