package com.bluemobi.wanmen.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluemobi.wanmen.R;
import com.bluemobi.wanmen.activity.BeginActivity;
import com.nostra13.universalimageloader.core.ImageLoader;



/**
 * Created by xujm on 2015/7/29.
 * 老师介绍fragment
 */
public class TeacherInfoFragment extends Fragment {
    private TextView tv_name, tv_info;
    private ImageView imageView_teacher;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        bundle = getArguments();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_teacherinfo, null);
        tv_name = (TextView) view.findViewById(R.id.textView_teacherinfo_name);
        tv_name.setText("主讲人：" + bundle.getString("teacher_name"));
        tv_info = (TextView) view.findViewById(R.id.textView_teacherinfo_info);
        tv_info.setText(bundle.getString("teacher_info"));
        imageView_teacher = (ImageView) view.findViewById(R.id.imageView_teacherinfo_teacher);
//        imageView_teacher.setLayoutParams(new RelativeLayout.LayoutParams(BeginActivity.getWidht() * 30 / 117 - 30, RelativeLayout.LayoutParams.MATCH_PARENT));
        view.findViewById(R.id.rl_texhergroup).setLayoutParams(new LinearLayout.LayoutParams(BeginActivity.getWidht() * 30 / 117 - 30,BeginActivity.getWidht() * 30 / 117 - 30));
        imageView_teacher.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams params = imageView_teacher.getLayoutParams();
                params.height = imageView_teacher.getWidth();
                imageView_teacher.setLayoutParams(params);
            }
        });
        ImageLoader.getInstance().displayImage(bundle.getString("teacher_img"), imageView_teacher);
        return view;
    }
}
