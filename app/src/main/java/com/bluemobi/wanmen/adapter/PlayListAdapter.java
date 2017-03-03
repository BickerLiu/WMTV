package com.bluemobi.wanmen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.bean.CourseListItem;

import java.util.List;



/**
 * Created by xujm on 2015/7/29.
 * 播放列表适配器
 */
public class PlayListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<CourseListItem> mList;


    public PlayListAdapter(Context context, List<CourseListItem> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mList.get(groupPosition).getList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (groupViewHolder == null) {
            groupViewHolder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_expandlistviewgroup, null);
            groupViewHolder.textView_Group = (TextView) convertView.findViewById(R.id.textView_group);
            groupViewHolder.imageView_Group = (ImageView) convertView.findViewById(R.id.imageView_group);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (isExpanded) {
            groupViewHolder.imageView_Group.setImageResource(R.mipmap.packup);
            groupViewHolder.textView_Group.setTextColor(Color.rgb(35, 155, 240));
        } else {
            groupViewHolder.imageView_Group.setImageResource(R.mipmap.dropdown);
            groupViewHolder.textView_Group.setTextColor(Color.WHITE);
        }
//        int selectId=((CourseDetailsActivity1)mContext).selectId;
        groupViewHolder.textView_Group.setText(mList.get(groupPosition).getCode() + "  " + mList.get(groupPosition).getName());
        convertView.setId(groupPosition * 1000);
        return convertView;
    }

    protected static class GroupViewHolder {
        private TextView textView_Group;
        private ImageView imageView_Group;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (childViewHolder == null) {
            childViewHolder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_expandlistviewchild, null);
            childViewHolder.textView_Child = (TextView) convertView.findViewById(R.id.textView_child);
            childViewHolder.imageView_Child = (ImageView) convertView.findViewById(R.id.imageView_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.textView_Child.setText((childPosition + 1) + "节 " + mList.get(groupPosition).getList().get(childPosition).getName());

        if (groupPosition == groupNum && childPosition == childNum) {
            childViewHolder.textView_Child.setTextColor(Color.rgb(35, 155, 240));
            childViewHolder.imageView_Child.setImageResource(R.mipmap.playing);
        } else {
            childViewHolder.textView_Child.setTextColor(Color.WHITE);
            childViewHolder.imageView_Child.setImageResource(R.mipmap.play);
        }

        return convertView;
    }

    private int groupNum;
    private int childNum;

    public void setSelect(int groupNum, int childNum) {
        this.childNum = childNum;
        this.groupNum = groupNum;
        notifyDataSetChanged();
    }


    public static class ChildViewHolder {
        private TextView textView_Child;
        private ImageView imageView_Child;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
