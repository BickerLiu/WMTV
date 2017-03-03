package com.bluemobi.wanmen.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.activity.CourseDetailsActivity1;
import com.bluemobi.wanmen.adapter.RecyclerViewAdapter;
import com.bluemobi.wanmen.bean.RecommendBean;
import com.bluemobi.wanmen.finals.Url_Base;
import com.bluemobi.wanmen.listener.OnItemClickListener;
import com.bluemobi.wanmen.listener.OnItemFocusListener;
import com.bluemobi.wanmen.listener.callBackListener;
import com.bluemobi.wanmen.utils.GsonUtils;
import com.bluemobi.wanmen.utils.NetwoekUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 热门课程
 */
public class PopularcoursesFragment extends BaseFragment {
    private ImageView imageView;
    private TextView tv_title, tv_content, tv_author;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter mAdapter;
    private ArrayList<RecommendBean.Course> list;
    private int course_id;
    private Context mContext;
    private int type;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        mContext = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_pupular, null);
        view.findViewById(R.id.linearLayout_fragment_popular).setLayoutParams(new LinearLayout.LayoutParams(getWidht() * 30 / 117 - 30, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView = (ImageView) view.findViewById(R.id.imageView_fragment_popular);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), CourseDetailsActivity1.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("course_id", course_id);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

        tv_title = (TextView) view.findViewById(R.id.textView_fragment_popular_title);
        tv_content = (TextView) view.findViewById(R.id.textView_fragment_popular_content);
        tv_author = (TextView) view.findViewById(R.id.textView_fragment_popular_author);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView__fragment_popular);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<RecommendBean.Course>();
        mAdapter = new RecyclerViewAdapter(getActivity(), list);
        mAdapter.setOnItemFocusListener(new OnItemFocusListener() {
            @Override
            public void onViewFocus(int position) {
                tv_title.setText(list.get(position).getName());
                tv_content.setText(list.get(position).getDescription());
                tv_author.setText("主讲人："+ list.get(position).getTeacher_name());
                course_id = list.get(position).getId();
                ImageLoader.getInstance().displayImage(list.get(position).getBig_img(), imageView);
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onViewClick() {
                Intent intent = new Intent(getActivity(), CourseDetailsActivity1.class);
                Bundle bundle = new Bundle();
                bundle.putInt("course_id", course_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        initData(type);
        super.onResume();
    }

    public void initData(final int i) {
        String url = Url_Base.appUrl + Url_Base.top;
        Map<String, String> params = new HashMap();
        params.put(Url_Base.count, "20");
        new NetwoekUtil().setIsShowDialog(true).HttpPost(mContext, url, params, new callBackListener() {
            @Override
            public void isSucess(String data) {
                NetwoekUtil.NetworkLog(data);
                if (data != null) {
                    RecommendBean recommendBean = GsonUtils.json2bean(data, RecommendBean.class);
                    reloadData(recommendBean, i);
                } else {
                    Toast.makeText(mContext, "数据加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void isError(String error) {
                Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void reloadData(RecommendBean recommendBean, int i) {
        if (recommendBean != null) {
            list.clear();
            if (i == 1) {
                list.addAll((List<RecommendBean.Course>) recommendBean.getHotCourses());
            } else {
                list.addAll((List<RecommendBean.Course>) recommendBean.getNewCourses());
            }
            mAdapter.notifyDataSetChanged();
            tv_title.setText(list.get(0).getName());
            tv_content.setText(list.get(0).getDescription());
            tv_author.setText("主讲人："+list.get(0).getTeacher_name());
            course_id = list.get(0).getId();
            ImageLoader.getInstance().displayImage(list.get(0).getBig_img(), imageView);
        }
    }
}

