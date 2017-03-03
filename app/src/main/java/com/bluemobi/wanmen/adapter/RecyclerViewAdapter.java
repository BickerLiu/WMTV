package com.bluemobi.wanmen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.bean.RecommendBean;
import com.bluemobi.wanmen.fragment.ListFragment;
import com.bluemobi.wanmen.fragment.TitleFragment;
import com.bluemobi.wanmen.listener.OnItemClickListener;
import com.bluemobi.wanmen.listener.OnItemFocusListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;



/**
 * Created by xujm on 2015/7/27.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<RecommendBean.Course> mList;


    private static OnItemClickListener mClickListener;
    private static OnItemFocusListener mFocusListener;

    public RecyclerViewAdapter(Context context, ArrayList<RecommendBean.Course> list) {
        mList = list;
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemFocusListener(OnItemFocusListener listener) {
        mFocusListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView mImageView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.textView_recyclerView);
            mImageView = (ImageView) view.findViewById(R.id.imageView_recyclerView);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout_recyclerView);
            relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                boolean isFirst = true;
                @Override
                public void onGlobalLayout() {
                    if (isFirst) {
                        isFirst=false;
                        ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
                        params.width= relativeLayout.getHeight();
                        relativeLayout.setLayoutParams(params);
                    }
                }
            });
//            mImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.onViewClick(getPosition());
//                }
//            });
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onViewClick();
                }
            });
            relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        mFocusListener.onViewFocus(getPosition());
                        mTextView.setTextColor(Color.rgb(35, 155, 240));
                    } else {
                        mTextView.setTextColor(Color.WHITE);
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.mTextView.setText(mList.get(i).getName());
        ImageLoader.getInstance().displayImage(mList.get(i).getSmall_img(), holder.mImageView);
        holder.relativeLayout.setNextFocusUpId(TitleFragment.id);
        if (i == 0) {
            holder.relativeLayout.setNextFocusLeftId(ListFragment.id);
            holder.relativeLayout.setNextFocusRightId(-1);
        } else if (i == mList.size() - 1) {
            holder.relativeLayout.setNextFocusRightId(holder.relativeLayout.getId());
            holder.relativeLayout.setNextFocusLeftId(-1);
        } else {
            holder.relativeLayout.setNextFocusLeftId(-1);
            holder.relativeLayout.setNextFocusRightId(-1);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
