package com.bluemobi.wanmen.bean;

import java.util.List;

/**
 * Created by xujm on 2015/7/31.
 * 首页推荐实体类
 */
public class RecommendBean {

    private List<Course> newCourses;
    private List<Course> hotCourses;
    private String msg;
    private int status;

    public List<Course> getNewCourses() {
        return newCourses;
    }

    public void setNewCourses(List<Course> newCourses) {
        this.newCourses = newCourses;
    }

    public List<Course> getHotCourses() {
        return hotCourses;
    }

    public void setHotCourses(List<Course> hotCourses) {
        this.hotCourses = hotCourses;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class Course {
        private int id;
        private String name;
        private String teacher_name;
        private String description;
        private String code;
        private String big_img;
        private String small_img;

        public boolean isShow = false;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getBig_img() {
            return big_img;
        }

        public void setBig_img(String big_img) {
            this.big_img = big_img;
        }

        public String getSmall_img() {
            return small_img;
        }

        public void setSmall_img(String small_img) {
            this.small_img = small_img;
        }

        @Override
        public String toString() {
            return "Course{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", teacher_name='" + teacher_name + '\'' +
                    ", description='" + description + '\'' +
                    ", code='" + code + '\'' +
                    ", big_img='" + big_img + '\'' +
                    ", small_img='" + small_img + '\'' +
                    '}';
        }
    }
}
