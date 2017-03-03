package com.bluemobi.wanmen.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.finals.Config;
import com.bluemobi.wanmen.utils.ParamsUtil;
import com.bokecc.sdk.mobile.exception.ErrorCode;
import com.bokecc.sdk.mobile.play.DWMediaPlayer;

import java.util.Timer;
import java.util.TimerTask;



public class PlayingActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {
    private TextView tv_time;
    private LinearLayout linearLayout;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private SeekBar seekBar;


    private DWMediaPlayer player;
    private String videoId;

    private Handler playerHandler;
    private Handler handler;
    private Runnable runnable;
    private Timer timer = new Timer();
    private TimerTask timerTask;

    private Boolean isPlaying;
    // 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
    private boolean isFreeze = false;
    private boolean isSurfaceDestroy = false;
    private boolean isPrepared;
    private int currentPosition;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private ImageView imageView;
    private RelativeLayout iv_pause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_playing);

        //初始化数据
        init();
        //初始化布局
        initView();

        //初始化播放hander
        initPlayHander();
        //初始化播放信息
        initPlayInfo(videoId);

    }

    //初始化数据
    public void init() {
        Bundle bundle = getIntent().getExtras();
        videoId = bundle.getString("videoId");
        currentPosition = bundle.getInt("currentPosition", currentPosition);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                linearLayout.setVisibility(View.GONE);
            }
        };
    }

    /*初始化布局*/
    public void initView() {
        tv_time = (TextView) findViewById(R.id.textView_playing_time);
         iv_pause= (RelativeLayout) findViewById(R.id.iv_pause);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_playing);
        /*image_fastback = (ImageView) findViewById(R.id.imageView_playing_fastback);
        image_fastback.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    image_fastback.setImageResource(R.mipmap.faseback_course_blue);
                } else {
                    image_fastback.setImageResource(R.mipmap.faseback_course);
                }
            }
        });
        image_start = (ImageView) findViewById(R.id.imageView_playing_start);
        image_start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (player.isPlaying()) {
                        image_start.setImageResource(R.mipmap.pause_blue);
                    } else {
                        image_start.setImageResource(R.mipmap.start_blue);
                    }
                } else {
                    if (player.isPlaying()) {
                        image_start.setImageResource(R.mipmap.pause);
                    } else {
                        image_start.setImageResource(R.mipmap.start);
                    }
                }
            }
        });
        image_forward = (ImageView) findViewById(R.id.imageView_playing_forward);
        image_forward.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    image_forward.setImageResource(R.mipmap.fastforward_course_blue);
                } else {
                    image_forward.setImageResource(R.mipmap.fastforward_course);
                }
            }
        });*/
        imageView = (ImageView) findViewById(R.id.imageView_playing);

        imageView.setVisibility(View.VISIBLE);
        seekBar = (SeekBar) findViewById(R.id.skbProgress_playing);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView_playing);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //2.3及以下使用，不然出现只有声音没有图像的问题
        surfaceHolder.addCallback(this);


    }


    //初始化播放hander
    private void initPlayHander() {
        playerHandler = new Handler() {
            public void handleMessage(Message msg) {

                if (player == null) {
                    return;
                }

                // 更新播放进度
                int position = player.getCurrentPosition();
                int duration = player.getDuration();

                if (duration > 0) {
                    int pos = seekBar.getMax() * position / duration;
                    tv_time.setText(ParamsUtil.millsecondsToStr(player
                            .getCurrentPosition()) + "/" + ParamsUtil.millsecondsToStr(player.getDuration()));
                    seekBar.setProgress(pos);
                    if (pos >= seekBar.getMax()) {
                        finish();
                    }
                }
            }
        };

        // 通过定时器和Handler来更新进度
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (!isPrepared) {
                    return;
                }

                playerHandler.sendEmptyMessage(0);
            }
        };

    }

    //初始化播放信息
    private void initPlayInfo(String id) {
        timer.schedule(timerTask, 0, 1000);
        isPrepared = false;
        player = new DWMediaPlayer();
        player.reset();
        player.setOnErrorListener(this);
        player.setOnVideoSizeChangedListener(this);

        try {

            player.setVideoPlayInfo(id, Config.USERID, Config.API_KEY, this);
            player.setDefaultDefinition(DWMediaPlayer.HIGH_DEFINITION);

            player.prepareAsync();

        } catch (IllegalArgumentException e) {
            Log.e("player error", e.getMessage());
        } catch (SecurityException e) {
            Log.e("player error", e.getMessage());
        } catch (IllegalStateException e) {
            Log.e("player error", e + "");
        }


    }


    @Override
    public void onPause() {
        if (isPrepared) {
            // 如果播放器prepare完成，则对播放器进行暂停操作，并记录状态
            if (player.isPlaying()) {
                isPlaying = true;
            } else {
                isPlaying = false;
            }
            player.pause();
        } else {
            // 如果播放器没有prepare完成，则设置isFreeze为true
            isFreeze = true;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
        alertHandler.removeCallbacksAndMessages(null);
        alertHandler = null;

        if (player != null) {
            player.release();
            player = null;
        }

        if (dialog != null) {
            dialog.dismiss();
        }

        super.onDestroy();
    }

    /*点击事件*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("currentPosition", player.getCurrentPosition());
                intent.putExtras(bundle);
                setResult(2, intent);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                linearLayout.setVisibility(View.VISIBLE);
                handler.removeCallbacks(runnable);
                fastBack();
                if (linearLayout.isShown()) {
                    handler.postDelayed(runnable, 4000);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                linearLayout.setVisibility(View.VISIBLE);
                handler.removeCallbacks(runnable);
                if (!isPrepared) {
                    break;
                }
                //改变播放暂停状态
                changePlayStatus();
                if (linearLayout.isShown()) {
                    handler.postDelayed(runnable, 4000);
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                linearLayout.setVisibility(View.VISIBLE);
                handler.removeCallbacks(runnable);
                fastForward();
                if (linearLayout.isShown()) {
                    handler.postDelayed(runnable, 4000);
                }
                break;
            case KeyEvent.KEYCODE_POWER:
                finish();
                break;
            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }


//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.imageView_playing_forward:
//                fastForward();
//                break;
//            case R.id.imageView_playing_start:
//                if (!isPrepared) {
//                    return;
//                }
//                //改变播放暂停状态
//                changePlayStatus();
//                break;
//            case R.id.imageView_playing_fastback:
//                fastBack();
//                break;
//            default:
//                break;
//        }
//    }

    /*快进*/
    public void fastForward() {
        int duration = player.getDuration();
        currentPosition = player.getCurrentPosition() + 5000;
        player.seekTo(currentPosition);
        int pos = seekBar.getMax() * currentPosition / duration;
        seekBar.setProgress(pos);
    }

    /*快退*/
    public void fastBack() {
        int duration;
        int pos;
        duration = player.getDuration();
        if (currentPosition >= 5000) {
            currentPosition = player.getCurrentPosition() - 5000;
        } else {
            currentPosition = 0;
        }
        player.seekTo(currentPosition);
        pos = seekBar.getMax() * currentPosition / duration;
        seekBar.setProgress(pos);
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
                builder = new AlertDialog.Builder(PlayingActivity.this);
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


    /*以下三个方法是实现surfaceView接口*/
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnBufferingUpdateListener(this);
            player.setOnPreparedListener(this);
            player.setDisplay(holder);
            if (isSurfaceDestroy) {
                player.prepareAsync();
            }
        } catch (Exception e) {
            Log.e("videoPlayer", "error", e);
        }
        Log.i("videoPlayer", "surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        holder.setFixedSize(width, height);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player == null) {
            return;
        }
        if (isPrepared) {
            currentPosition = player.getCurrentPosition();
        }

        isPrepared = false;
        isSurfaceDestroy = true;

        player.stop();
        player.reset();

    }


    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        if (!isFreeze) {
            if (isPlaying == null) {
//                progressDialog.dismiss();
                player.start();
//                image_start.setImageResource(R.mipmap.pause);
            }
        }

        if (currentPosition > 0) {
            player.seekTo(currentPosition);
        }

        if (player.getDuration() > currentPosition) {
            if (imageView.isShown()) {
                imageView.setVisibility(View.GONE);
            }
        }
        tv_time.setText(ParamsUtil.millsecondsToStr(currentPosition) + "/" + ParamsUtil.millsecondsToStr(player.getDuration()));
    }

    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
    }


    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }

    private void changePlayStatus() {
        if (player.isPlaying()) {
            player.pause();
//            image_start.setImageResource(R.mipmap.start);
            iv_pause.setVisibility(View.VISIBLE);
        } else {
            player.start();
//            image_start.setImageResource(R.mipmap.pause);
            iv_pause.setVisibility(View.INVISIBLE);
        }
    }


}
