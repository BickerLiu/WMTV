package com.bluemobi.wanmen.bean;

import java.util.List;

/**
 * 作者： sunll 日期： 2015/7/24 15:47
 * 用于存储侧边栏列表信息
 */
public class NavigationlistBean {
    public NavigationBean genres;
    public List<NavigationBean> list;

    @Override
    public String toString() {
        return "NavigationlistBean{" +
                "genres=" + genres +
                ", list=" + list +
                '}';
    }
}
