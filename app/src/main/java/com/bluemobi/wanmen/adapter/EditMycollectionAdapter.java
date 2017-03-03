package com.bluemobi.wanmen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.activity.BeginActivity;
import com.bluemobi.wanmen.activity.EditMyCollectionActivity;
import com.bluemobi.wanmen.bean.MyCollectionBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;



/**
 * 作者： sunll 日期： 2015/7/28 10:56
 */
public class EditMycollectionAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<MyCollectionBean> mList;


    public EditMycollectionAdapter(Context context, List<MyCollectionBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final ViewHolder vh = (ViewHolder) viewHolder;
        ImageLoader
                .getInstance()
                .displayImage(
                        mList.get(i).image_url,
                        vh.iv_video,
                        ((EditMyCollectionActivity) mContext).getDisplayImageOptions());
        if (mList.get(i).isCheck) {
            vh.iv_select.setBackgroundResource(R.mipmap.collect_edit_select_ok);
        } else {
            vh.iv_select.setBackgroundResource(R.mipmap.collect_edit_noselect);
        }
        vh.tv_video.setText(mList.get(i).video_name);
        final int position = i;
        vh.llvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList.get(position).isCheck = !mList.get(position).isCheck;
                if (mList.get(position).isCheck) {
                    vh.iv_select.setBackgroundResource(R.mipmap.collect_edit_select_ok);
                    ((EditMyCollectionActivity) mContext).setSelectCount(((EditMyCollectionActivity) mContext).getSelectCount() + 1);
                } else {
                    vh.iv_select.setBackgroundResource(R.mipmap.collect_edit_noselect);
                    ((EditMyCollectionActivity) mContext).setSelectCount(((EditMyCollectionActivity) mContext).getSelectCount() - 1);
                }
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_editmycollection, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout llvideo;
        private ImageView iv_select;
        private ImageView iv_video;
        private TextView tv_video;

        public ViewHolder(View v) {
            super(v);
            llvideo = (LinearLayout) v.findViewById(R.id.ll_video);
            iv_select = (ImageView) v.findViewById(R.id.iv_select);
            iv_video = (ImageView) v.findViewById(R.id.iv_video);
            tv_video = (TextView) v.findViewById(R.id.tv_video);
            iv_video.setLayoutParams(new LinearLayout.LayoutParams(BeginActivity.getWidht()*14/117,BeginActivity.getWidht()*14/117));
        }
    }
}
