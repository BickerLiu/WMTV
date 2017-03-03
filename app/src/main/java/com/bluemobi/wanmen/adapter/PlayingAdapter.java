package com.bluemobi.wanmen.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.activity.BeginActivity;

import com.bluemobi.wanmen.activity.CourseDetailsActivity1;
import com.bluemobi.wanmen.activity.MainActivity;
import com.bluemobi.wanmen.bean.PlayingRecordBean;
import com.bluemobi.wanmen.fragment.TitleFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;



public class PlayingAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<PlayingRecordBean> mList;


    public PlayingAdapter(Context context, List<PlayingRecordBean> list) {
        mList = list;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivvideo;
        public TextView tvname;
        public TextView tvtime;
        public TextView tvReplay;
        public TextView tvContinued;
        public TextView tvNext;
        public View view;
        public FrameLayout fl_video;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.tvNext = (TextView) view.findViewById(R.id.tv_Next);
            this.tvContinued = (TextView) view.findViewById(R.id.tv_Continued);
            this.tvReplay = (TextView) view.findViewById(R.id.tv_Replay);
            this.tvtime = (TextView) view.findViewById(R.id.tv_time);
            this.tvname = (TextView) view.findViewById(R.id.tv_name);
            this.ivvideo = (ImageView) view.findViewById(R.id.iv_video);
            this.fl_video = (FrameLayout) view.findViewById(R.id.fl_video);
            ivvideo.setLayoutParams(new FrameLayout.LayoutParams(BeginActivity.getWidht() * 14 / 117, BeginActivity.getWidht() * 14 / 117));
        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_playingrecord, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder vh = (ViewHolder) viewHolder;
        if (i < 3) {
            vh.fl_video.setNextFocusUpId(TitleFragment.id);
            vh.tvContinued.setNextFocusUpId(TitleFragment.id);
            vh.tvNext.setNextFocusUpId(TitleFragment.id);
            vh.tvReplay.setNextFocusUpId(TitleFragment.id);
        } else {
            vh.fl_video.setNextFocusUpId(-1);
            vh.tvContinued.setNextFocusUpId(-1);
            vh.tvNext.setNextFocusUpId(-1);
            vh.tvReplay.setNextFocusUpId(-1);
        }
        ImageLoader
                .getInstance()
                .displayImage(
                        mList.get(i).image_url,
                        vh.ivvideo,
                        ((MainActivity) mContext).getDisplayImageOptions());
        vh.tvname.setText(mList.get(i).video_name);
        int time = 0;
        try {
            time = Integer.parseInt(mList.get(i).time) / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        vh.tvtime.setText("上次看到\n第" + mList.get(i).discourse + "讲 " + mList.get(i).part + "节 " + time / 60 + "分" + time % 60 + "秒");
        final int index = i;
        View.OnClickListener OnClick = new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_Replay:
                        mList.get(index).flag = 0;
                        break;
                    case R.id.tv_Continued:
                        mList.get(index).flag = 1;
                        break;
                    case R.id.fl_video:
                        mList.get(index).flag = 1;
                        break;
                    case R.id.tv_Next:
                        mList.get(index).flag = 2;
                        break;
                }
                Intent intent = new Intent(mContext, CourseDetailsActivity1.class);
                Bundle bundle = new Bundle();
                bundle.putString("tag", PlayingRecordBean.tag);
                bundle.putSerializable(PlayingRecordBean.tag, mList.get(index));
                bundle.putInt("course_id", Integer.parseInt(mList.get(index).video_id));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        };
        vh.tvNext.setOnClickListener(OnClick);
        vh.tvContinued.setOnClickListener(OnClick);
        vh.tvReplay.setOnClickListener(OnClick);
        vh.view.findViewById(R.id.fl_video).setOnClickListener(OnClick);
        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView tv = (TextView) v;
                if (hasFocus) {
                    tv.setTextColor(v.getContext().getResources().getColor(R.color.selectcolor));
                } else {
                    tv.setTextColor(v.getContext().getResources().getColor(R.color.noselectcolor));
                }
                if (oldSelectIndex -3>= index) {
                    if (index < 3) {
                        recyclerView.smoothScrollToPosition(0);
                    } else {
                        recyclerView.scrollBy(0,-BeginActivity.getHeight()/8);
                        for(int i=0;i<BeginActivity.getHeight()/8;i+=10)
                        {
                            handler.postDelayed(runnable,i);
                        }
                    }
                }
                oldSelectIndex = index;

            }
        };
        vh.tvContinued.setOnFocusChangeListener(focusChangeListener);
        vh.tvNext.setOnFocusChangeListener(focusChangeListener);
        vh.tvReplay.setOnFocusChangeListener(focusChangeListener);
    }
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(recyclerView!=null)
            recyclerView.scrollBy(0,-10);
        }
    };
    @Override
    public int getItemCount() {
        return mList.size();
    }

    private RecyclerView recyclerView;

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    private int oldSelectIndex;
}
