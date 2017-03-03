package com.bluemobi.wanmen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.activity.EditMyCollectionActivity;
import com.bluemobi.wanmen.adapter.MycollectionAdapter;
import com.bluemobi.wanmen.bean.MyCollectionBean;
import com.bluemobi.wanmen.db.MyCollectionDao;

import java.util.ArrayList;
import java.util.List;



/**
 * 收藏的课程
 */
public class MycollectionFragment extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MycollectionAdapter mAdapter;
    private List<MyCollectionBean> list = new ArrayList<MyCollectionBean>();
    ;
    private TextView tv_edit;
    private MyCollectionDao dbDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_mycollection, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView__fragment_popular);
        tv_edit = (TextView) view.findViewById(R.id.tv_edit);
        tv_edit.setNextFocusLeftId(TitleFragment.id);
        tv_edit.setNextFocusRightId(TitleFragment.id);
        initView();
        initListener();
        return view;
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 6, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        dbDao = new MyCollectionDao(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();

        list.clear();
        for (MyCollectionBean bean : dbDao.selectAllMyCollection()) {
            list.add(0, bean);
        }
        ;
        mAdapter = new MycollectionAdapter(getActivity(), list);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        tv_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                TextView tv = (TextView) v;
                if (hasFocus) {
                    tv.setTextColor(v.getContext().getResources().getColor(R.color.selectcolor));
                    if (list.size() == 0) {
                        tv_edit.setNextFocusUpId(TitleFragment.id);
                    } else {
                        tv_edit.setNextFocusUpId(-1);
                    }
                } else {
                    tv.setTextColor(v.getContext().getResources().getColor(R.color.noselectcolor));
                }
            }
        });
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditMyCollectionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbDao.close();
    }
}
