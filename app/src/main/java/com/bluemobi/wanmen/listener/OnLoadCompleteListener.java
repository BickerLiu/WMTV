package com.bluemobi.wanmen.listener;

import com.bluemobi.wanmen.bean.CourseListItem;

import java.util.List;

/**
 * Created by xujm on 2015/8/10.
 */
public interface OnLoadCompleteListener {
    public void loadComplete(List<CourseListItem> list);
}
