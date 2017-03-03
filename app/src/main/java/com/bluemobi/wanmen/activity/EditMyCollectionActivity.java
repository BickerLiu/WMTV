package com.bluemobi.wanmen.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.adapter.EditMycollectionAdapter;
import com.bluemobi.wanmen.bean.MyCollectionBean;
import com.bluemobi.wanmen.db.MyCollectionDao;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.List;



/**
 * 作者： sunll 日期： 2015/7/23 14:20
 */

public class EditMyCollectionActivity extends Activity implements View.OnClickListener {

    private Context context = this;
    private TextView tvback;
    private TextView tvcount;
    private TextView tvdelect;
    private TextView tvselectall;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EditMycollectionAdapter mAdapter;
    private List<MyCollectionBean> list;
    private MyCollectionDao dbDao;
    private int count;
    /**
     * 显示图片的选项
     */
    private DisplayImageOptions headOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editmycollection);
        init();
    }

    private void init() {
        initUI();
        initListener();
        initImageLoader(this);
    }

    private void initUI() {
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerView__fragment_popular);
        this.tvselectall = (TextView) findViewById(R.id.tv_selectall);
        this.tvdelect = (TextView) findViewById(R.id.tv_delect);
        this.tvcount = (TextView) findViewById(R.id.tv_count);
        this.tvback = (TextView) findViewById(R.id.tv_back);
        recyclerView.setHasFixedSize(true);
//        layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        layoutManager = new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        dbDao = new MyCollectionDao(this);
        list = dbDao.selectAllMyCollection();
//        if (list.size() == 0)
//            for (int i = 0; i < 15; i++) {
//                MyCollectionBean myCollectionBean = new MyCollectionBean();
//                myCollectionBean.image_url = "image_url" + i;
//                myCollectionBean.video_id = "video_id" + i;
//                myCollectionBean.video_name = "video_name" + i;
//                list.add(myCollectionBean);
//                dbDao.insertPlayingRecord(myCollectionBean);
//            }
        mAdapter = new EditMycollectionAdapter(this, list);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        tvselectall.setOnFocusChangeListener(textOnFocusListener);
        tvdelect.setOnFocusChangeListener(textOnFocusListener);
        tvback.setOnFocusChangeListener(textOnFocusListener);
        tvselectall.setOnClickListener(this);
        tvdelect.setOnClickListener(this);
        tvback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_selectall:
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isCheck == false) {
                        for (int j = 0; j < list.size(); j++) {
                            list.get(j).isCheck = true;
                        }
                        setSelectCount(list.size());
                        tvselectall.setText("取消");
                        mAdapter.notifyDataSetChanged();
                        return;
                    }
                }
                for (int j = 0; j < list.size(); j++) {
                    list.get(j).isCheck = false;
                }
                setSelectCount(0);
                tvselectall.setText("全选");
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_delect:
                delectCollection();
                break;
            default:
                break;
        }
    }

    /**
     * 删除选中的收藏
     */
    private void delectCollection() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck) {
                dbDao.delectMyCollection(list.get(i).video_id);
            }
        }
        list.clear();
        list.addAll(dbDao.selectAllMyCollection());
        mAdapter.notifyDataSetChanged();
        setSelectCount(0);
    }

    private View.OnFocusChangeListener textOnFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            TextView tv = (TextView) v;
            if (hasFocus) {
                tv.setTextColor(getResources().getColor(R.color.selectcolor));
                if (v.getId() == R.id.tv_back) {
                    tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.back_course_blue, 0, 0, 0);
                }
            } else {
                tv.setTextColor(getResources().getColor(R.color.noselectcolor));
                if (v.getId() == R.id.tv_back) {
                    tv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.back_course, 0, 0, 0);
                }
            }
        }
    };

    /**
     * ImageLoader 设置项
     *
     * @return
     */
    public DisplayImageOptions getDisplayImageOptions() {
        return headOptions;
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     */
    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                        // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        headOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.background)
                .showImageForEmptyUri(R.drawable.background)
                .showImageOnFail(R.drawable.background).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                        // .displayer(new RoundedVignetteBitmapDisplayer(10, 6))
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public void finish() {
        super.finish();
        dbDao.close();
    }

    /**
     * 设置选中的个数
     *
     * @param count
     */
    public void setSelectCount(int count) {
        this.count = count;
        if (count == list.size()) {
            tvselectall.setText("取消");
        } else {
            tvselectall.setText("全选");
        }
        tvcount.setText(count + "");
    }

    public int getSelectCount() {
        return count;
    }
}
