package com.bluemobi.wanmen.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.adapter.PlayListAdapter;
import com.bluemobi.wanmen.finals.Url_Base;
import com.bluemobi.wanmen.listener.callBackListener;
import com.bluemobi.wanmen.utils.GsonUtils;
import com.bluemobi.wanmen.utils.NetwoekUtil;
import com.bluemobi.wanmen.bean.CourseListItem;
import com.bluemobi.wanmen.bean.PartsBean;
import com.bluemobi.wanmen.bean.TopicsBean;
import com.bluemobi.wanmen.listener.OnLoadCompleteListener;
import com.bluemobi.wanmen.listener.OnPlayListClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by xujm on 2015/7/29.
 * 播放列表fragment
 */
public class PlayListFragment extends Fragment {
    public static int itemId;
    private ExpandableListView expandableListView;
    private PlayListAdapter adapter;
    private int course_id;
    private TopicsBean topicsBean;
    private List<PartsBean> partList;
    public List<CourseListItem> mList;
    private int num;
    private int part;
    private OnPlayListClickListener onPlayListClickListener;
    private OnLoadCompleteListener onLoadCompleteListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        course_id = getArguments().getInt("course_id");
        num = getArguments().getInt("num");
        part = getArguments().getInt("part");
        partList = new ArrayList<PartsBean>();
        mList = new ArrayList<CourseListItem>();
        onPlayListClickListener = (OnPlayListClickListener) getActivity();
        onLoadCompleteListener = (OnLoadCompleteListener) getActivity();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_playlist, null);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView_playlist);
        expandableListView.setNextFocusUpId(R.id.textView_course_directory);
        expandableListView.setDivider(getResources().getDrawable(R.mipmap.bg_playlist));
        expandableListView.setDividerHeight(2);
        expandableListView.setChildDivider(getResources().getDrawable(R.mipmap.bg_playlist));
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                for (int i = 0; i < topicsBean.getTopics().size(); i++) {
                    if (i != groupPosition) {
                        expandableListView.collapseGroup(i);
                    }
                }
                expandableListView.expandGroup(groupPosition, false);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                onPlayListClickListener.onPlayListClick(mList.get(groupPosition).getList().get(childPosition).getCc_video_link(), groupPosition, childPosition);

//                for (int i = 0; i < mList.size(); i++) {
//                    boolean isLast = false;
//                    for (int j = 0; j < mList.get(i).getList().size(); j++) {
//                        if (j == mList.size() - 1) {
//                            isLast = true;
//                        }
//                        View childView = adapter.getChildView(i, j, isLast, null, null);
//
//                        TextView tv = (TextView) childView.findViewById(R.id.textView_child);
//                        ImageView iv = (ImageView) childView.findViewById(R.id.imageView_child);
//                        tv.setTextColor(Color.WHITE);
//                        iv.setImageResource(R.mipmap.play);
//                    }
//                }

//                TextView textView = (TextView) v.findViewById(R.id.textView_child);
//                ImageView imageView = (ImageView) v.findViewById(R.id.imageView_child);
//                textView.setTextColor(Color.rgb(35, 155, 240));
//                imageView.setImageResource(R.mipmap.playing);

                adapter.setSelect(groupPosition, childPosition);

                return true;
            }
        });


        expandableListView.setGroupIndicator(null);
        adapter = new PlayListAdapter(getActivity(), mList);
        expandableListView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onResume() {
        netWorkTopics();
        super.onResume();
    }

    private String int2string(int i) {
        String count = "";
        switch (i) {
            case 0:
                count = "";
                break;
            case 1:
                count = "一";
                break;
            case 2:
                count = "二";
                break;
            case 3:
                count = "三";
                break;
            case 4:
                count = "四";
                break;
            case 5:
                count = "五";
                break;
            case 6:
                count = "六";
                break;
            case 7:
                count = "七";
                break;
            case 8:
                count = "八";
                break;
            case 9:
                count = "九";
                break;
            case 10:
                count = "十";
                break;
            case 11:
                count = "十一";
                break;
            case 12:
                count = "十二";
                break;
        }
        return count;
    }

    public String getTopics(int i) {
        if (i < 10) {
            return "第" + int2string(i) + "讲";
        } else if (i < 20) {
            return "第十" + int2string(i % 10) + "讲";
        } else if (i < 100) {
            return "第" + int2string(i / 10) + "十" + int2string(i % 10) + "讲";
        } else if (i < 1000) {
            return "第" + int2string(i / 100) + "百" + int2string(i / 10) + "十" + int2string(i % 10) + "讲";
        }
        return "";
    }

    public void reload(List<PartsBean> partList) {
        mList.clear();
        for (int i = 0; i < topicsBean.getTopics().size(); i++) {
            CourseListItem courseListItem = new CourseListItem();
            courseListItem.setId(topicsBean.getTopics().get(i).getId());
            courseListItem.setName(topicsBean.getTopics().get(i).getName());
            courseListItem.setCode(getTopics(i + 1));

            courseListItem.setList(partList.get(i).getParts());
            mList.add(courseListItem);
        }
        if (part >= mList.get(num).getList().size()) {
            if (num == mList.size() - 1) {
                part -= 1;
            } else {
                num++;
                part = 0;
            }
        }
//        Toast.makeText(getActivity(),"num="+num+ ";part="+part,Toast.LENGTH_LONG).show();
        adapter.setSelect(num, part);
        adapter.notifyDataSetChanged();
        expandableListView.expandGroup(num);
        onLoadCompleteListener.loadComplete(mList);
        expandableListView.requestFocus();
//        expandableListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){
//                    expandableListView.getChildAt(num).setSelected(true);
//                }else {
//
//                }
//            }
//        });
    }
public void requestFocus()
{
    expandableListView.requestFocus();
}

    /*加载视频一级列表*/
    public void netWorkTopics() {
        String url = Url_Base.appUrl + Url_Base.course_list;
        Map<String, String> params = new HashMap();
        params.put(Url_Base.course_id, course_id + "");
        new NetwoekUtil().HttpPost(getActivity(), url, params, new callBackListener() {
            @Override
            public void isSucess(String data) {
                NetwoekUtil.NetworkLog(data);
                if (data != null) {
                    NetwoekUtil.NetworkLog(data);
                    topicsBean = GsonUtils.json2bean(data, TopicsBean.class);
                    if (topicsBean != null) {
                        for (int i = 0; i < topicsBean.getTopics().size(); i++) {
                            netWorkParts(topicsBean.getTopics().get(i).getId());
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void isError(String error) {
                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*加载视频二级列表*/
    public void netWorkParts(int topic_id) {
        String url = Url_Base.appUrl + Url_Base.course_parts;
        Map<String, String> params = new HashMap();
        params.put(Url_Base.topic_id, topic_id + "");
        new NetwoekUtil().HttpPost(getActivity(), url, params, new callBackListener() {
            @Override
            public void isSucess(String data) {
                NetwoekUtil.NetworkLog(data);
                if (data != null) {
                    NetwoekUtil.NetworkLog(data);
                    PartsBean partsBean = GsonUtils.json2bean(data, PartsBean.class);
                    partList.add(partsBean);
                    if (partList.size() == topicsBean.getTopics().size()) {
                        reload(partList);
                    }
                } else {
                    Toast.makeText(getActivity(), "数据加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void isError(String error) {
                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
