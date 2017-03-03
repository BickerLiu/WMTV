package com.bluemobi.wanmen.bean;

import java.util.List;

/**
 * Created by xujm on 2015/8/4.
 */
public class CourseListItem {
    private int id;
    private String code;
    private String name;
    private List<PartsBean.Part> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PartsBean.Part> getList() {
        return list;
    }

    public void setList(List<PartsBean.Part> list) {
        this.list = list;
    }
}
