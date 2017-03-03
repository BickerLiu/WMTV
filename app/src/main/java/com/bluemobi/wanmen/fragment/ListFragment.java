package com.bluemobi.wanmen.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.utils.Tools;

import java.util.List;



/**
 * 主界面左侧导航栏
 * 作者： sunll 日期： 2015/7/23 14:20
 */
@SuppressLint("ValidFragment")
public class ListFragment extends BaseFragment {

    private View view;
    private List<String> str;
    private LinearLayout ll_group;

    public ListFragment(List<String> str) {
        super();
        this.str = str;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_list, null);
        ll_group = (LinearLayout) view.findViewById(R.id.ll_group);
        ll_group.removeAllViews();
        setStrList(str);
        return view;
    }

    private TextView getNewTextView(String str) {
        TextView tv = new TextView(getActivity());
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setBackgroundResource(R.drawable.list_background);
        tv.setFocusable(true);
        tv.setGravity(Gravity.CENTER);
        tv.setText(str);
        tv.setTextColor(0XFFFFFFFF);
        tv.setTextSize(Tools.px2sp(getActivity(), getResources().getDimension(R.dimen.list1_text_size)));
        tv.setPadding(0, Tools.dip2px(getActivity(), 20), 0, Tools.dip2px(getActivity(), 20));
        if (TitleFragment.id == R.id.tv2) {
            selectTop(tv, str);
        }
        return tv;
    }

    public void selectTop(View v, String str) {
        if (str.equals("热门课程")) {
            v.setNextFocusRightId(PopularcoursesFragment1.BUTTOM_START_ID);
        } else if (str.equals("最新课程")) {
            v.setNextFocusRightId(PopularcoursesFragment1.BUTTOM_START_ID);
        } else if (str.equals("万门简介")) {
        } else if (str.equals("App二维码")) {
        } else if (str.equals("联系我们")) {
        } else if (str.equals("软件升级")) {
            v.setNextFocusRightId(R.id.textView_update);
        }

    }


    public static int id = 10100;

    public void setStrList(List<String> str) {
        this.str = str;
        ll_group = (LinearLayout) view.findViewById(R.id.ll_group);
        ll_group.removeAllViews();
        for (int i = 0; i < str.size(); i++) {
            TextView tv = getNewTextView(str.get(i));
            tv.setId(10100 + i);
            if (i == 0) {
                tv.setNextFocusUpId(TitleFragment.id);
                id = tv.getId();
            }
            tv.setOnFocusChangeListener(this);
//            tv.setOnClickListener(this);
            ll_group.addView(tv);
            View textView = new View(getActivity());
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
            textView.setBackgroundResource(R.mipmap.fengexian);
            ll_group.addView(textView);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        super.onFocusChange(v, hasFocus);
        if (hasFocus) {
            for (int i = 0; i < ll_group.getChildCount(); i++) {
                if (ll_group.getChildAt(i) instanceof TextView)
                    ll_group.getChildAt(i).setBackgroundResource(R.drawable.list_background);
            }
        } else {
            v.setBackgroundResource(R.drawable.list_backgroup_select);
        }
    }
}
