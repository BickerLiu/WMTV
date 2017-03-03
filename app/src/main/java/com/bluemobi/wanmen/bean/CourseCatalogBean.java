package com.bluemobi.wanmen.bean;

import java.util.List;

/**
 * 作者： sunll 日期： 2015/8/3 13:56
 * 课程目录
 */
public class CourseCatalogBean {

    public List<CourseBean> courses;
    public String msg;
    public String status;

    public class CourseBean {
        public String id;
        public String name;
        public String description;
        public String structure;
        public String code;
        public String logo;

        @Override
        public String toString() {
            return "CourseBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", structure='" + structure + '\'' +
                    ", code='" + code + '\'' +
                    ", logo='" + logo + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CourseCatalogBean{" +
                "courses=" + courses +
                ", msg='" + msg + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
