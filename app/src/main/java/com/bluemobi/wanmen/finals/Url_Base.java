package com.bluemobi.wanmen.finals;


/**
 * Created by xujm on 2015/7/29.
 * 接口地址类
 */

public class Url_Base {

    /*参数*/
    /*数量*/
    public static final String count = "count";
    /*一级课程目录id*/
    public static final String genre_id = "genre_id";
    /*二级课程目录id*/
    public static final String major_id = "major_id";
    /*三级课程目录id*/
    public static final String course_id = "course_id";
    /*主题id*/
    public static final String topic_id = "topic_id";
    /*视频id*/
    public static final String part_id = "part_id";


    /*服务器地址*/
//    public static final String appUrl = "http://112.124.50.148";
    public static final String appUrl = "http://ww2.wanmen.org";

    /*首页接口*/
    public static final String top = "/api/v1/courses/queryRecommend.json";
    /*一级课程目录*/
    public static final String course_genres = "/api/v1/genres/queryFreeGenres.json";
    /*二级课程目录*/
    public static final String course_majors = "/api/v1/majors/queryMajors.json";
    /*三级课程列表*/
    public static final String course_coiurses = "/api/v1/courses/queryCourses.json";
    /*课程详情*/
    public static final String course_info = "/api/v1/courses/findCourse.json";
    /*课程播放列表*/
    public static final String course_list = "/api/v1/topics/queryTopics.json";
    /*视频列表*/
    public static final String course_parts = "/api/v1/parts/queryParts.json";
    /*单个视频信息*/
    public static final String course_part = "/api/v1/parts/findPart.json";

}
