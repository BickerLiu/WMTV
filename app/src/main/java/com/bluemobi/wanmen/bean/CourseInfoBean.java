package com.bluemobi.wanmen.bean;

/**
 * Created by xujm on 2015/7/31.
 * 课程详细信息
 */
public class CourseInfoBean {

    private Course course;
    private String msg;
    private int status;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
        private String description;
        private String course_video_link;
        private String structure;
        private String teacher_name;
        private String teacher_description;
        private String teacher_avatar;
//        private String code;
        private String banner;
        private String logo;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCourse_video_link() {
            return course_video_link;
        }

        public void setCourse_video_link(String course_video_link) {
            this.course_video_link = course_video_link;
        }

        public String getStructure() {
            return structure;
        }

        public void setStructure(String structure) {
            this.structure = structure;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getTeacher_description() {
            return teacher_description;
        }

        public void setTeacher_description(String teacher_description) {
            this.teacher_description = teacher_description;
        }

        public String getTeacher_avatar() {
            return teacher_avatar;
        }

        public void setTeacher_avatar(String teacher_avatar) {
            this.teacher_avatar = teacher_avatar;
        }

//        public String getCode() {
//            return code;
//        }
//
//        public void setCode(String code) {
//            this.code = code;
//        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }
}
