package com.bluemobi.wanmen.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bluemobi.wanmen.bean.MyCollectionBean;

import java.util.ArrayList;
import java.util.List;

public class MyCollectionDao {

    DBHelper helper;
    Context context;
    SQLiteDatabase db;


    public MyCollectionDao(Context context) {
        this.context = context;
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 插入或更新我的收藏
     *
     * @param bean
     */
    public void insertPlayingRecord(MyCollectionBean bean) {
        if (selectMyCollection(bean.video_id) != null) {
            upDataMyCollection(bean);
            return;
        }
        String sql = "insert into " + bean.tag + "(video_name, image_url, video_id) values(?,?,?)";
        db.execSQL(sql, new Object[]{bean.video_name, bean.image_url, bean.video_id});
    }

    /**
     * 查看所有的收藏
     *
     * @return
     */
    public List<MyCollectionBean> selectAllMyCollection() {
        List<MyCollectionBean> data = new ArrayList<MyCollectionBean>();
        String sql = "SELECT * FROM " + MyCollectionBean.tag;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyCollectionBean bean = new MyCollectionBean();
            bean.video_name = cursor.getString(cursor.getColumnIndex("video_name"));
            bean.image_url = cursor.getString(cursor.getColumnIndex("image_url"));
            bean.video_id = cursor.getString(cursor.getColumnIndex("video_id"));
            data.add(bean);
        }
        return data;
    }

    /**
     * 根据视频ID查看我的收藏
     *
     * @param video_id
     * @return
     */
    public MyCollectionBean selectMyCollection(String video_id) {
        String sql = "SELECT * FROM " + MyCollectionBean.tag + " where video_id='" + video_id + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            MyCollectionBean bean = new MyCollectionBean();
            bean.video_name = cursor.getString(cursor.getColumnIndex("video_name"));
            bean.image_url = cursor.getString(cursor.getColumnIndex("image_url"));
            bean.video_id = cursor.getString(cursor.getColumnIndex("video_id"));
            return bean;
        }
        return null;
    }

    /**
     * 更新我的收藏，此方法不公开，如果进行更新请调用insertMyCollection(MyCollectionBean bean)
     *
     * @param bean
     */
    private void upDataMyCollection(MyCollectionBean bean) {
        String sql = "UPDATE " + MyCollectionBean.tag + " set  video_name=?, image_url=?  where video_id=?";
        db.execSQL(
                sql,
                new Object[]{bean.video_name, bean.image_url, bean.video_id});

    }

    /**
     * 根据视频ID删除对应的收藏
     *
     * @param video_id
     */
    public void delectMyCollection(String video_id) {
        db.beginTransaction();
        String sql1 = "DELETE FROM " + MyCollectionBean.tag + " WHERE video_id=?";
        db.execSQL(sql1, new Object[]{video_id});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void close() {
        db.close();
    }
}
