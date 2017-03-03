package com.bluemobi.wanmen.utils;

import com.google.gson.Gson;

/**
 * Created by xujm on 2015/7/30.
 * Gson工具类
 */

public class GsonUtils {

    public static <T> T json2bean(String result, Class<T> clazz) {
        Gson gson = new Gson();
        T t = gson.fromJson(result, clazz);
        return t;
    }

}
