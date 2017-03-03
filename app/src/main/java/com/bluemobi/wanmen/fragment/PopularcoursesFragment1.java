package com.bluemobi.wanmen.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.activity.BeginActivity;
import com.bluemobi.wanmen.activity.CourseDetailsActivity1;
import com.bluemobi.wanmen.activity.MainActivity;
import com.bluemobi.wanmen.bean.RecommendBean;
import com.bluemobi.wanmen.finals.Url_Base;
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
public class PopularcoursesFragment1 extends BaseFragment {
    private ImageView imageView;
    private TextView tv_title, tv_content, tv_author;
    private View view;
    //    private RecyclerView recyclerView;
//    private RecyclerView.LayoutManager layoutManager;
//    private RecyclerViewAdapter mAdapter;
    private ArrayList<RecommendBean.Course> list;
    private int course_id;
    private Context mContext;
    private int type = 1;
    private HorizontalScrollView sv_EndGroup;
    private LinearLayout ll_EndGroup;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        mContext = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.remove("android:support:fragments");
        Log.i("OOO", "onSaveInstanceState-->>");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        Log.i("OOO", "onStop-->>");
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.i("OOO", "onPause-->>");
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fg_pupular1, null);
        view.findViewById(R.id.linearLayout_fragment_popular).setLayoutParams(new LinearLayout.LayoutParams(getWidht() * 30 / 117 - 30, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView = (ImageView) view.findViewById(R.id.imageView_fragment_popular);
        tv_title = (TextView) view.findViewById(R.id.textView_fragment_popular_title);
        tv_content = (TextView) view.findViewById(R.id.textView_fragment_popular_content);
        tv_author = (TextView) view.findViewById(R.id.textView_fragment_popular_author);
        sv_EndGroup = (HorizontalScrollView) view.findViewById(R.id.sv_EndGroup);
        ll_EndGroup = (LinearLayout) view.findViewById(R.id.ll_EndGroup);
        sv_EndGroup.setVisibility(View.VISIBLE);
        list = new ArrayList<RecommendBean.Course>();
        if (viewList == null) {
            viewList = new ArrayList<View>();
        }
        initData(1);
        return view;
    }

    @Override
    public void onResume() {
//        initData(type);
        super.onResume();
    }

    RecommendBean recommendBean;

    public void initData(final int i) {
//        type = i;
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", i);
//        setArguments(bundle);
        if (recommendBean != null) {
            reloadData(recommendBean, i);
            return;
        }
        String url = Url_Base.appUrl + Url_Base.top;
        Map<String, String> params = new HashMap();
//        params.put(Url_Base.count, "20");
        new NetwoekUtil().setIsShowDialog(true).HttpPost(mContext, url, params, new callBackListener() {
            @Override
            public void isSucess(String data) {
                NetwoekUtil.NetworkLog(data);
                if (data != null) {
                    recommendBean = GsonUtils.json2bean(data, RecommendBean.class);
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

    public static int BUTTOM_START_ID = 10300;

    public void reloadData(RecommendBean recommendBean, int i) {
        if (recommendBean != null) {
            list.clear();
            if (i == 1) {
                list.addAll((List<RecommendBean.Course>) recommendBean.getHotCourses());
            } else {
                list.addAll((List<RecommendBean.Course>) recommendBean.getNewCourses());
            }
            ll_EndGroup.removeAllViews();
            for (int index = 0; index < list.size(); index++) {
                View view = getItemView(list.get(index));
                RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout_recyclerView);
                view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                relativeLayout.setId(BUTTOM_START_ID + index);
                relativeLayout.setNextFocusUpId(TitleFragment.id);
                if (index == 0) {
                    relativeLayout.setNextFocusLeftId(ListFragment.id);
                } else if (index == list.size() - 1) {
                    relativeLayout.setNextFocusRightId(relativeLayout.getId());
                }
                ll_EndGroup.addView(view);
                if (index < list.size() - 1) {
                    View hintView = new View(mContext);
                    hintView.setLayoutParams(new LinearLayout.LayoutParams(20, 1));
                    ll_EndGroup.addView(hintView);
                }
            }
            tv_title.setText(list.get(0).getName());
            tv_content.setText(Html.fromHtml(list.get(0).getDescription()));
            tv_author.setText("主讲人：" + list.get(0).getTeacher_name());
            course_id = list.get(0).getId();
            ImageLoader
                    .getInstance()
                    .displayImage(
                            list.get(0).getBig_img(),
                            imageView,
                            ((MainActivity) mContext).getDisplayImageOptions());
        }
    }

    private List<View> viewList;

    private View getItemView(final RecommendBean.Course bean) {
        View view = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, null);
        final TextView mTextView = (TextView) view.findViewById(R.id.textView_recyclerView);
        final ImageView mImageView = (ImageView) view.findViewById(R.id.imageView_recyclerView);
        final RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativelayout_recyclerView);
        mImageView.setLayoutParams(new RelativeLayout.LayoutParams(BeginActivity.getWidht() * 14 / 117, BeginActivity.getWidht() * 14 / 117));
        mTextView.setText(bean.getName());
        ImageLoader.getInstance().displayImage(bean.getSmall_img(), mImageView, ((MainActivity) mContext).getDisplayImageOptions());
//        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                ViewGroup.LayoutParams params = relativeLayout.getLayoutParams();
//                params.width = relativeLayout.getHeight();
//                relativeLayout.setLayoutParams(params);
//                if (mImageView.isShown() && !bean.isShow) {
//                    Log.i("IsShow","mImageView.isShown()="+mImageView.isShown()+"bean.isShow="+bean.isShow);
//                    bean.isShow=true;
//                    ImageLoader.getInstance().displayImage(bean.getSmall_img(), mImageView, ((MainActivity) mContext).getDisplayImageOptions());
//                } else if (!mImageView.isShown() && bean.isShow) {
//                    Log.i("IsShow","mImageView.isShown()="+mImageView.isShown()+"bean.isShow="+bean.isShow+"*************");
//                    bean.isShow=false;
//                    mImageView.setImageDrawable(new ColorDrawable(0X00000000));
//                }
//            }
//        });
//            mImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.onViewClick(getPosition());
//                }
//            });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mClickListener.onViewClick();
                Intent intent = new Intent(getActivity(), CourseDetailsActivity1.class);
                Bundle bundle = new Bundle();
                bundle.putInt("course_id", course_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    mFocusListener.onViewFocus(getPosition());
                    mTextView.setTextColor(Color.rgb(35, 155, 240));
                    tv_title.setText(bean.getName());
                    tv_content.setText(bean.getDescription());
                    tv_author.setText("主讲人：" + bean.getTeacher_name());
                    course_id = bean.getId();

                    ImageLoader
                            .getInstance()
                            .displayImage(
                                    bean.getBig_img(),
                                    imageView,
                                    ((MainActivity) getActivity()).getDisplayImageOptions());
                } else {
                    mTextView.setTextColor(Color.WHITE);
                }
            }
        });
        return view;
    }

}

