/**
 * Copyright (C) 2015 孙思远
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bluemobi.wanmen.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.bluemobi.wanmen.bean.MyCollectionBean;
import com.bluemobi.wanmen.bean.PlayingRecordBean;

/**
 * 数据库辅助类
 */
public class DBHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "record.db";
    public static final int DB_VERSION = 1;


    public DBHelper(Context context, String name, CursorFactory factory
    ) {
        super(context, name, factory, DB_VERSION);
    }

    public DBHelper(Context context) {
        this(context, DB_NAME, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_calendar = "create table "
                + "TabName"
                + "("
                + "calendar_id integer primary key autoincrement,calendar_name varchar(255),calendar_intro varchar(255))";
        db.execSQL(create_calendar);
        String create_playingrecord = "create table "
                + PlayingRecordBean.tag
                + "("
                + "video_id varchar(255),image_url varchar(255),"
                + "video_name varchar(255),discourse varchar(255),"
                + "part varchar(255),time varchar(255))";
        db.execSQL(create_playingrecord);
        String create_mycollection = "create table "
                + MyCollectionBean.tag
                + "("
                + "video_id varchar(255),image_url varchar(255),"
                + "video_name varchar(255))";
        db.execSQL(create_mycollection);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
