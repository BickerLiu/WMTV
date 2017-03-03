package com.bluemobi.wanmen.bean;

import java.util.List;

/**
 * Created by xujm on 2015/7/31.
 * <p/>
 * 视频列表信息
 */
public class PartsBean {
    private List<Part> parts;
    private String msg;
    private int status;

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
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

    public class Part {
        private int id;
        private String code;
        private String name;
        private String cc_video_link;

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

        public String getCc_video_link() {
            return cc_video_link;
        }

        public void setCc_video_link(String cc_video_link) {
            this.cc_video_link = cc_video_link;
        }
    }
}
