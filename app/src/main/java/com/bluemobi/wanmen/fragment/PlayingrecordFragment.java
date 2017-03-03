package com.bluemobi.wanmen.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.adapter.PlayingAdapter;
import com.bluemobi.wanmen.bean.PlayingRecordBean;
import com.bluemobi.wanmen.db.PlayingRecordDao;

import java.util.List;



/**
 * 播放记录
 */
public class PlayingrecordFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PlayingAdapter mAdapter;
    private List<PlayingRecordBean> list;
    private PlayingRecordDao dbDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fg_playingrecord, null);
        initView();
        return recyclerView;
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
//        layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        layoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        dbDao = new PlayingRecordDao(getActivity());

    }

    @Override
    public void onStart() {
        super.onStart();
        list = dbDao.selectAllPlayRecord();
        if (list.size() > 15) {
            for (int i = 15; i<list.size(); i++) {
                dbDao.delectPlayRecord(list.get(i).video_id);
            }
            list = dbDao.selectAllPlayRecord();
        }
        mAdapter = new PlayingAdapter(getActivity(), list);
        mAdapter.setRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbDao.close();
    }
}
