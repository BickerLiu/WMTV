package com.bluemobi.wanmen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bluemobi.wanmen.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MaxScreenActivity extends AppCompatActivity {

    private int position;
    private long pos;
    private JCVideoPlayerStandard jcPlayerStandard;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_max_screen);
        bundle=getIntent().getBundleExtra("bundle");
        init();
    }

    private void init() {
        position=getIntent().getIntExtra("position",0);
        jcPlayerStandard= (JCVideoPlayerStandard) findViewById(R.id.max_screen_jcplayer);
        jcPlayerStandard.fullscreenButton.setVisibility(View.GONE);
        jcPlayerStandard.setUp(bundle.getString("url"), JCVideoPlayer.SCREEN_LAYOUT_LIST,"");
        jcPlayerStandard.seekToInAdvance=bundle.getInt("position");
        jcPlayerStandard.startButton.performClick();


    }
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                    finish();
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                jcPlayerStandard.performClick();
                pos=jcPlayerStandard.getCurrentPositionWhenPlaying();
                 if (pos-3000<=0)
                 {
                     jcPlayerStandard.seekToInAdvance=0;
                     jcPlayerStandard.performClick();
                 }
                else
                 {
                     jcPlayerStandard.seekToInAdvance= (int) (pos-3000);
                     jcPlayerStandard.performClick();

                 }

                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                jcPlayerStandard.performClick();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                jcPlayerStandard.performClick();
                pos=jcPlayerStandard.getCurrentPositionWhenPlaying();
                if (pos-3000<=0)
                {
                    jcPlayerStandard.seekToInAdvance=0;
                    jcPlayerStandard.performClick();
                }
                else
                {
                    jcPlayerStandard.seekToInAdvance= (int) (pos-3000);
                    jcPlayerStandard.performClick();

                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


}
