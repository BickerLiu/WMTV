package com.bluemobi.wanmen.bean;

import java.io.Serializable;

/**
 * 作者： sunll 日期： 2015/8/4 10:07
 */
public class PlayingRecordBean implements Serializable {
    public static final String tag = "PlayingRecord";
    public String video_id;
    public String image_url;
    public String video_name;
    /**
     * 讲
     */
    public String discourse;
    /**
     * 节
     */
    public String part;
    /**
     * 时间
     */
    public String time;
    /**
     * 0 重播
     * 1 续播
     * 2 下集
     */
    public int flag;

}
