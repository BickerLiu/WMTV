package com.bluemobi.wanmen.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.activity.BeginActivity;

import com.bluemobi.wanmen.activity.CourseDetailsActivity1;
import com.bluemobi.wanmen.activity.MainActivity;
import com.bluemobi.wanmen.bean.MyCollectionBean;
import com.bluemobi.wanmen.fragment.TitleFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;



/**
 * 作者： sunll 日期： 2015/7/28 10:56
 */
public class MycollectionAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<MyCollectionBean> mList;


    public MycollectionAdapter(Context context, List<MyCollectionBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder vh = (ViewHolder) viewHolder;
        if (i < 6) {
            vh.llvideo.setNextFocusUpId(TitleFragment.id);
            if (i == mList.size() - 1) {
                vh.llvideo.setNextFocusRightId(TitleFragment.id);
            } else {
                vh.llvideo.setNextFocusRightId(-1);
            }
        } else {
            vh.llvideo.setNextFocusUpId(-1);
        }

        ImageLoader
                .getInstance()
                .displayImage(
                        mList.get(i).image_url,
                        vh.ivvideo,
                        ((MainActivity) mContext).getDisplayImageOptions());
        vh.tv_name.setText(mList.get(i).video_name);
        final int index = i;
        vh.llvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CourseDetailsActivity1.class);
                Bundle bundle = new Bundle();
//                bundle.putString("tag", MyCollectionBean.tag);
                bundle.putInt("course_id", Integer.parseInt(mList.get(index).video_id));
//                bundle.putSerializable(MyCollectionBean.tag, mList.get(index));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_mycollection, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llvideo;
        private ImageView ivvideo;
        private TextView tv_name;

        public ViewHolder(View v) {
            super(v);
            llvideo = (LinearLayout) v.findViewById(R.id.ll_video);
            ivvideo = (ImageView) v.findViewById(R.id.iv_video);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            ivvideo.setLayoutParams(new LinearLayout.LayoutParams(BeginActivity.getWidht() * 14 / 117, BeginActivity.getWidht() * 14 / 117));
        }
    }
}
