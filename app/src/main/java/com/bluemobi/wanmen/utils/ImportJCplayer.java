package com.bluemobi.wanmen.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.activity.CourseDetailsActivity1;


import fm.jiecao.jcvideoplayer_lib.JCBuriedPointStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by lbq on 17/2/28.
 */

public class ImportJCplayer extends JCVideoPlayer {
    private Bundle bundle=new Bundle();
    public ImportJCplayer(Context context) {
        super(context);
    }

    public ImportJCplayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return fm.jiecao.jcvideoplayer_lib.R.layout.jc_layout_base;
    }

    @Override
    public boolean setUp(String url, int screen, Object... objects) {
        if (super.setUp(url, screen, objects)) {
            fullscreenButton.setVisibility(View.GONE);
            super.onClick(startButton);
            startButton.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
    @Override
    public void setUiWitStateAndScreen(int state) {
        super.setUiWitStateAndScreen(state);
        topContainer.setVisibility(View.GONE);
        switch (currentState) {
            case CURRENT_STATE_NORMAL:

                //startButton.setVisibility(View.VISIBLE);
                break;
            case CURRENT_STATE_PREPAREING:
               // startButton.setVisibility(View.INVISIBLE);
                break;
            case CURRENT_STATE_PLAYING:
               // startButton.setVisibility(View.VISIBLE);
                break;
            case CURRENT_STATE_PAUSE:
                break;
            case CURRENT_STATE_ERROR:
                break;
        }
        updateStartImage();
    }

    private void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setImageResource(fm.jiecao.jcvideoplayer_lib.R.drawable.jc_click_pause_selector);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setImageResource(fm.jiecao.jcvideoplayer_lib.R.drawable.jc_click_error_selector);
        } else {
            startButton.setImageResource(fm.jiecao.jcvideoplayer_lib.R.drawable.jc_click_play_selector);
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (currentState == CURRENT_STATE_NORMAL) {
                Toast.makeText(getContext(), "Play video first", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        super.onProgressChanged(seekBar, progress, fromUser);
    }

    @Override
    public boolean goToOtherListener() {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return super.onTouch(v, event);
    }
}
