package com.bluemobi.wanmen.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bluemobi.wanmen.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class JieCaoPlayActivity extends AppCompatActivity {
    JCVideoPlayerStandard JCplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jie_cao_play);
        JCplayer= (JCVideoPlayerStandard) findViewById(R.id.jiecaoplayer);
        JCplayer.setUp("http://media.wanmen.org/87e5e30a-2aa4-48f4-a19b-de635493b803_mobile_low.m3u8",JCVideoPlayerStandard.SCREEN_LAYOUT_LIST," ");
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
}
