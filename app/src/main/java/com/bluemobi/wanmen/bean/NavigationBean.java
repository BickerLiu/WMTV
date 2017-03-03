package com.bluemobi.wanmen.bean;

/**
 * 作者： sunll 日期： 2015/7/24 15:48
 */
public class NavigationBean {
    public String id;
    public String name;
    public String description;
    public String logo;

    @Override
    public String toString() {
        return "NavigationBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
