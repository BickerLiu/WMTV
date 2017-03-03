package com.bluemobi.wanmen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.activity.CourseDetailsActivity1;
import com.bluemobi.wanmen.activity.MainActivity;
import com.bluemobi.wanmen.bean.CourseCatalogBean;
import com.bluemobi.wanmen.bean.CourseInfoBean;
import com.bluemobi.wanmen.bean.NavigationBean;
import com.bluemobi.wanmen.finals.Url_Base;
import com.bluemobi.wanmen.listener.callBackListener;
import com.bluemobi.wanmen.utils.GsonUtils;
import com.bluemobi.wanmen.utils.NetwoekUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 课程简介
 */
public class CoursedescriptionFragment extends BaseFragment {
    private View view;
    /**
     * 课程分类二级目录
     */
    private NavigationBean bean;
    /**
     * 课程分类三级目录
     */
    private List<CourseCatalogBean> courseCatalogBeans = new ArrayList<CourseCatalogBean>();
    /**
     * 课程详情
     */
    private CourseInfoBean courseInfoBean;
    private TextView tvteacher;
    private ImageView ivteacher;
    private TextView tv_teacher_name;
    private TextView tv_video_count;
    private TextView tvvideo;
    private ImageView ivvideo;
    private LinearLayout lllist;
    private LinearLayout ll_teacher;

    public void setBean(NavigationBean bean) {
//        Log.i("NetWork", bean.name + "setBean");
        this.bean = bean;
        if (view != null) {
            for (int i = 0; i < courseCatalogBeans.size(); i++) {
                if (courseCatalogBeans.get(i).msg.equals(bean.name)) {
                    setData(courseCatalogBeans.get(i));
                    return;
                }
            }
            getListData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        view = inflater.inflate(R.layout.fg_coursedescription, null);
        initView(view);
        getListData();
//        Log.i("NetWork", bean.name + "onCreateView");
        return view;
    }


    private void initView(View view) {
        tvteacher = (TextView) view.findViewById(R.id.tv_teacher);//教师简介
        ivteacher = (ImageView) view.findViewById(R.id.iv_teacher);//教师图片
        tv_teacher_name = (TextView) view.findViewById(R.id.tv_teacher_name);//教师姓名
        tv_video_count = (TextView) view.findViewById(R.id.tv_video_count);//第几讲
        tvvideo = (TextView) view.findViewById(R.id.tv_video);//视频简介
        ivvideo = (ImageView) view.findViewById(R.id.iv_video);//视频图片
        lllist = (LinearLayout) view.findViewById(R.id.ll_list);
        view.findViewById(R.id.ll_teacher).setLayoutParams(new LinearLayout.LayoutParams(getWidht() * 30 / 117 - 30, ViewGroup.LayoutParams.MATCH_PARENT));
        view.findViewById(R.id.ll_listgroup).setLayoutParams(new LinearLayout.LayoutParams(getWidht() * 15 / 117, ViewGroup.LayoutParams.MATCH_PARENT));
        view.findViewById(R.id.fl_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CourseDetailsActivity1.class);
                Bundle bundle = new Bundle();
//                bundle.putString("tag", MyCollectionBean.tag);

                bundle.putInt("course_id", courseInfoBean.getCourse().getId());
//                bundle.putSerializable(MyCollectionBean.tag, mList.get(index));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

    }

    /**
     * 获取三级目录
     */
    private void getListData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("major_id", bean.id);
        new NetwoekUtil().setIsShowDialog(true).HttpPost(getActivity(), Url_Base.appUrl + Url_Base.course_coiurses, params, new callBackListener() {
            @Override
            public void isSucess(String data) {
                if (getActivity() == null) {
                    return;
                }
                CourseCatalogBean courseCatalogBean = GsonUtils.json2bean(data, CourseCatalogBean.class);
                courseCatalogBean.msg = bean.name;
                courseCatalogBeans.add(courseCatalogBean);
                setData(courseCatalogBean);
//                Log.i("NetWork", courseCatalogBean.toString());
            }

            @Override
            public void isError(String error) {

            }
        });

    }

    /**
     * 填充三级目录
     *
     * @param data
     */
    private void setData(CourseCatalogBean data) {
        if (getActivity() == null) {
            return;
        }
        lllist.removeAllViews();
        int hight = 0;
        for (int i = 0; i < data.courses.size(); i++) {
            View view_item = getListView(data, i);
            lllist.addView(view_item);
            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3));
            tv.setBackgroundResource(R.mipmap.fengexian);
            lllist.addView(tv);
            hight += getTargetHeight(view_item);
            hight += 3;
        }
        View iv_all = view.findViewById(R.id.iv_all);
        int grouphight = view.getHeight() - view.getPaddingBottom();
        if (hight > grouphight) {
            iv_all.setVisibility(View.VISIBLE);
        } else {
            iv_all.setVisibility(View.GONE);
        }
//                Log.i("NetWork", "hight=" + hight);
//                Log.i("NetWork", " lllist.getMeasuredHeight()=" + grouphight);
        if (data.courses.size() > 0) {
            getCourseData(data.courses.get(0).id);
        }
    }

    private int getTargetHeight(View v) {

        try {
            Method m = v.getClass().getDeclaredMethod("onMeasure", int.class,
                    int.class);
            m.setAccessible(true);
            m.invoke(v, View.MeasureSpec.makeMeasureSpec(
                    ((View) v.getParent()).getMeasuredWidth(),
                    View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED));
        } catch (Exception e) {

        }
        return v.getMeasuredHeight();
    }


    private View getListView(final CourseCatalogBean data, int index) {
        String str = data.courses.get(index).name;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_text_imageview, null);
        final ViewHodler hodler = new ViewHodler();
        hodler.ivimage = (ImageView) view.findViewById(R.id.iv_image);
        hodler.tvtext = (TextView) view.findViewById(R.id.tv_text);
        hodler.tvtext.setText(str);
        hodler.view = view;
        hodler.view.setBackgroundResource(R.drawable.title_backgroup_noselect);
        view.setTag(hodler);
        hodler.tvtext.setNextFocusLeftId(ListFragment.id);
        hodler.tvtext.setId(10200 + index);
        if (index == 0) {
            hodler.tvtext.setNextFocusUpId(TitleFragment.id);
        }
        hodler.tvtext.setNextFocusRightId(TitleFragment.id);
        if (index == data.courses.size() - 1) {
            hodler.tvtext.setNextFocusDownId(hodler.tvtext.getId());
        }
//        view.setFocusable(true);
        hodler.tvtext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hodler.view.setBackgroundResource(R.drawable.title_backgroup_select);
                    hodler.tvtext.setTextColor(getActivity().getResources().getColor(R.color.selectcolor));
                    hodler.tvtext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.pay_video, 0);
                    for (int i = 0; i < data.courses.size(); i++) {
                        if (data.courses.get(i).name.equals(hodler.tvtext.getText().toString())) {
                            getCourseData(data.courses.get(i).id);
                        }
                    }
                } else {
                    hodler.tvtext.setTextColor(getActivity().getResources().getColor(R.color.noselectcolor));
                    hodler.tvtext.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    hodler.view.setBackgroundResource(R.drawable.title_backgroup_noselect);
                }
            }
        });
        hodler.tvtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TextView tv = (TextView) v.findViewById(R.id.tv_text);
//                for (int i = 0; i < courseCatalogBean.courses.size(); i++) {
//                    if (courseCatalogBean.courses.get(i).name.equals(tv.getText().toString())) {
//                        getCourseData(courseCatalogBean.courses.get(i).id);
//                    }
//                }
                Intent intent = new Intent(getActivity(), CourseDetailsActivity1.class);
                Bundle bundle = new Bundle();
//                bundle.putString("tag", MyCollectionBean.tag);

                bundle.putInt("course_id", courseInfoBean.getCourse().getId());
//                bundle.putSerializable(MyCollectionBean.tag, mList.get(index));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 获取课程详情
     *
     * @param id
     */
    private void getCourseData(String id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("course_id", id);
        new NetwoekUtil().setIsShowDialog(false).HttpPost(getActivity(), Url_Base.appUrl + Url_Base.course_info, params, new callBackListener() {
            @Override
            public void isSucess(String data) {
                courseInfoBean = GsonUtils.json2bean(data, CourseInfoBean.class);
                setCourseData();
            }

            @Override
            public void isError(String error) {

            }
        });
        new NetwoekUtil().setIsShowDialog(false).HttpPost(getActivity(), Url_Base.appUrl + Url_Base.course_list, params, new callBackListener() {
            @Override
            public void isSucess(String data) {
                try {
                    JSONArray ja = new JSONObject(data).getJSONArray("topics");
                    tv_video_count.setText("共" + ja.length() + "讲");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void isError(String error) {

            }
        });
    }

    private void setCourseData() {
        if (getActivity() == null) {
            return;
        }
        ImageLoader
                .getInstance()
                .displayImage(
                        courseInfoBean.getCourse().getBanner(),
                        ivvideo,
                        ((MainActivity) getActivity()).getDisplayImageOptions());
        ImageLoader
                .getInstance()
                .displayImage(
                        courseInfoBean.getCourse().getTeacher_avatar(),
                        ivteacher,
                        ((MainActivity) getActivity()).getDisplayImageOptions());
        tvvideo.setText(courseInfoBean.getCourse().getDescription());
        tv_teacher_name.setText("主讲人：" + courseInfoBean.getCourse().getTeacher_name());
        tvteacher.setText(Html.fromHtml(courseInfoBean.getCourse().getTeacher_description()));
    }

    public class ViewHodler {
        ImageView ivimage;
        TextView tvtext;
        View view;
    }
}
