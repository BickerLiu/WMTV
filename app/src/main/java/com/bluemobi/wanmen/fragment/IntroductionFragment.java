package com.bluemobi.wanmen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.activity.PlayingActivity;



/**
 * 万门简介
 */
public class IntroductionFragment extends BaseFragment {
    private View view;
    private LinearLayout ll_video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fg_introduction, null);
            initView();
//            initGif();
        }
        return view;
    }

    private void initGif() {
//        int view_id[] = {R.id.gif1, R.id.gif2, R.id.gif3, R.id.gif4};
//        int mipmap_id[] = {R.mipmap.gif1, R.mipmap.gif2, R.mipmap.gif3, R.mipmap.gif4};
//        for (int i = 0; i < 4; i++) {
//            GifView gf1 = (GifView) view.findViewById(view_id[i]);
//            // 设置Gif图片源
//            gf1.setGifImage(mipmap_id[i]);
//            // 添加监听器
//            gf1.setOnClickListener(this);
//            // 设置显示的大小，拉伸或者压缩
//            gf1.setShowDimension(BeginActivity.getHeight() / 10, BeginActivity.getHeight() / 10);
//            gf1.setLayoutParams(new LinearLayout.LayoutParams(BeginActivity.getHeight() / 10, BeginActivity.getHeight() / 10));
//            // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
//            gf1.setGifImageType(GifView.GifImageType.COVER);
//        }
    }

    private String videoId = "C0DAF6C7808F90F79C33DC5901307461";

    private void initView() {
        ll_video = (LinearLayout) view.findViewById(R.id.ll_video);
        ll_video.setNextFocusDownId(ll_video.getId());
        ll_video.setNextFocusUpId(TitleFragment.id
        );
        ll_video.setNextFocusRightId(ll_video.getId());
        ll_video.setNextFocusLeftId(ListFragment.id);
        ll_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("currentPosition", 0);
                bundle.putString("videoId", videoId);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }

}
