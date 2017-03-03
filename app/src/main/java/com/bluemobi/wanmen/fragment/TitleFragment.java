package com.bluemobi.wanmen.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
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
 * 头部导航栏
 * 作者： sunll 日期： 2015/7/23 14:20
 */
@SuppressLint("ValidFragment")
public class TitleFragment extends BaseFragment {
    private View view;
    private List<String> str;
    private TextView tv1, tv2, tv6, tv7;
    private LinearLayout ll_group,ll_tv2,ll_tv6,ll_tv7;

    public TitleFragment(List<String> str) {
        super();
        this.str = str;
    }

    public static int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_title, null);

        tv7 = (TextView) view.findViewById(R.id.tv7);
        tv6 = (TextView) view.findViewById(R.id.tv6);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        view.findViewById(R.id.iv_logo).setLayoutParams(new LinearLayout.LayoutParams(getWidht() / 8 + 26, ViewGroup.LayoutParams.MATCH_PARENT));
        view.findViewById(R.id.ll_tv2).setLayoutParams(new LinearLayout.LayoutParams(getWidht() * 15 / 117, ViewGroup.LayoutParams.MATCH_PARENT));
        view.findViewById(R.id.ll_tv7).setLayoutParams(new LinearLayout.LayoutParams(getWidht() * 15 / 117, ViewGroup.LayoutParams.MATCH_PARENT));
        view.findViewById(R.id.ll_tv6).setLayoutParams(new LinearLayout.LayoutParams(getWidht() * 15 / 117, ViewGroup.LayoutParams.MATCH_PARENT));
        ll_group = (LinearLayout) view.findViewById(R.id.ll_group);
        ll_tv2= (LinearLayout) view.findViewById(R.id.ll_tv2);
        ll_tv6= (LinearLayout) view.findViewById(R.id.ll_tv6);
        ll_tv7= (LinearLayout) view.findViewById(R.id.ll_tv7);
        tv1.setOnFocusChangeListener(this);
        tv2.setOnFocusChangeListener(this);
        tv6.setOnFocusChangeListener(this);
        tv7.setOnFocusChangeListener(this);
//        tv1.setOnClickListener(this);
//        tv2.setOnClickListener(this);
//        tv6.setOnClickListener(this);
//        tv7.setOnClickListener(this);
        for (int i = 0; i < str.size(); i++) {
            TextView tv = getNewTextView(str.get(i));
            tv.setOnFocusChangeListener(this);
//            tv.setOnClickListener(this);
            tv.setTextColor(0XFFFFFFFF);
            tv.setId(100000 + i);
            ll_group.addView(tv);
            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(new LinearLayout.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setBackgroundResource(R.mipmap.fengexian_course);
            ll_group.addView(textView);
        }


        return view;
    }

    /**
     * 初始化所有的title背景色
     */
    private void setAllTitleBack() {
        ll_tv2.setBackgroundResource(R.drawable.title_backgroup_noselect);
        ll_tv6.setBackgroundResource(R.drawable.title_background);
        ll_tv7.setBackgroundResource(R.drawable.title_background);
        for (int i = 0; i < ll_group.getChildCount(); i++) {
            ll_group.getChildAt(i).setBackgroundResource(R.drawable.title_background);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        super.onFocusChange(v, hasFocus);
        if (getActivity() == null && getActivity().isDestroyed()) {
            return;
        }
        setAllTitleBack();
        TextView tv = (TextView) v;
        Drawable drawable = null;
        int drawID = 0;
        switch (v.getId()) {
            case R.id.tv2:
                if (hasFocus) {
                    drawID = R.mipmap.title_home_select;
                    ll_tv2.setBackgroundResource(R.drawable.title_backgroup_select);
                } else {
                    drawID = R.mipmap.title_home_noselect;
                    ll_tv2.setBackgroundResource(R.drawable.title_backgroup_select);
                }
                drawable = getResources().getDrawable(drawID);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv.setCompoundDrawables(drawable, null, null, null);
                break;
            case R.id.tv6:
                if (hasFocus) {
                    drawID = R.mipmap.title_pay_select;
                    ll_tv6.setBackgroundResource(R.drawable.title_backgroup_select);
                } else {
                    drawID = R.mipmap.title_pay_noselect;
                    ll_tv6.setBackgroundResource(R.drawable.title_backgroup_select);
                }
                drawable = getResources().getDrawable(drawID);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv.setCompoundDrawables(drawable, null, null, null);
                break;
            case R.id.tv7:
                if (hasFocus) {
                    drawID = R.mipmap.title_collection_select;
                    ll_tv7.setBackgroundResource(R.drawable.title_backgroup_select);
                } else {
                    drawID = R.mipmap.title_collection_noselect;
                    ll_tv7.setBackgroundResource(R.drawable.title_backgroup_select);
                }
                drawable = getResources().getDrawable(drawID);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv.setCompoundDrawables(drawable, null, null, null);
                break;
            default:
                if (hasFocus) {
                   v.setBackgroundResource(R.drawable.title_background);
                } else {
                    v.setBackgroundResource(R.drawable.title_backgroup_select);
                }
                break;
        }
    }

    /**
     * @param str
     * @return
     */
    private TextView getNewTextView(String str) {
        TextView tv = new TextView(getActivity());
        tv.setBackgroundResource(R.drawable.title_background);
        tv.setFocusable(true);
        tv.setText(str);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(Tools.px2sp(getActivity(), getResources().getDimension(R.dimen.title_text_size)));
        tv.setLayoutParams(new LinearLayout.LayoutParams(getWidht() * 15 / 117, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setNextFocusDownId(ListFragment.id);
        return tv;
    }


}
