package com.bluemobi.wanmen.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bluemobi.wanmen.bean.PlayingRecordBean;

import java.util.ArrayList;
import java.util.List;

public class PlayingRecordDao {

    DBHelper helper;
    Context context;
    SQLiteDatabase db;


    public PlayingRecordDao(Context context) {
        this.context = context;
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 插入或更新播放记录
     *
     * @param bean
     */
    public void insertPlayingRecord(PlayingRecordBean bean) {
        if (selectPlayRecord(bean.video_id) != null) {
//            upDataPlayRecord(bean);
            delectPlayRecord(bean.video_id);
//            return;
        }
        String sql = "insert into " + bean.tag + "(time, part, discourse, video_name, image_url, video_id) values(?,?,?,?,?,?)";
        db.execSQL(sql, new Object[]{bean.time, bean.part, bean.discourse, bean.video_name, bean.image_url, bean.video_id});
    }

    /**
     * 查看所有的播放记录
     *
     * @return
     */
    public List<PlayingRecordBean> selectAllPlayRecord() {
        List<PlayingRecordBean> data = new ArrayList<PlayingRecordBean>();
        String sql = "SELECT * FROM " + PlayingRecordBean.tag;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PlayingRecordBean bean = new PlayingRecordBean();
            bean.time = cursor.getString(cursor.getColumnIndex("time"));
            bean.part = cursor.getString(cursor.getColumnIndex("part"));
            bean.discourse = cursor.getString(cursor.getColumnIndex("discourse"));
            bean.video_name = cursor.getString(cursor.getColumnIndex("video_name"));
            bean.image_url = cursor.getString(cursor.getColumnIndex("image_url"));
            bean.video_id = cursor.getString(cursor.getColumnIndex("video_id"));
            data.add(0,bean);
        }
        return data;
    }

    /**
     * 根据视频ID查看播放记录
     *
     * @param video_id
     * @return
     */
    public PlayingRecordBean selectPlayRecord(String video_id) {
        String sql = "SELECT * FROM " + PlayingRecordBean.tag + " where video_id='" + video_id + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            PlayingRecordBean bean = new PlayingRecordBean();
            bean.time = cursor.getString(cursor.getColumnIndex("time"));
            bean.part = cursor.getString(cursor.getColumnIndex("part"));
            bean.discourse = cursor.getString(cursor.getColumnIndex("discourse"));
            bean.video_name = cursor.getString(cursor.getColumnIndex("video_name"));
            bean.image_url = cursor.getString(cursor.getColumnIndex("image_url"));
            bean.video_id = cursor.getString(cursor.getColumnIndex("video_id"));
            return bean;
        }
        return null;
    }

    /**
     * 更新播放记录，此方法不公开，如果进行更新请调用insertPlayingRecord(PlayingRecordBean bean)
     *
     * @param bean
     */
    private void upDataPlayRecord(PlayingRecordBean bean) {
        String sql = "UPDATE " + PlayingRecordBean.tag + " set time=?, part=?, discourse=?, video_name=?, image_url=?  where video_id=?";
        db.execSQL(
                sql,
                new Object[]{bean.time, bean.part, bean.discourse, bean.video_name, bean.image_url, bean.video_id});

    }

    /**
     * 根据视频ID删除播放记录
     *
     * @param video_id
     */
    public void delectPlayRecord(String video_id) {
        db.beginTransaction();
        String sql1 = "DELETE FROM " + PlayingRecordBean.tag + " WHERE video_id=?";
        db.execSQL(sql1, new Object[]{video_id});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void close() {
        db.close();
    }
}
