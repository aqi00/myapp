package com.example.chapter13.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.chapter13.bean.EvaluatePhoto;

import java.util.ArrayList;
import java.util.List;

public class EvaluatePhotoHelper extends DbHelper {
    private static String table_name = "evaluate_photo";

    public EvaluatePhotoHelper(Context context) {
        super(context, db_name, null, 1);
        mTableName = table_name;
        mSelectSQL = String.format("select rowid,_id,evaluate_id,image_path from %s where "
                , mTableName);
    }

    private static EvaluatePhotoHelper mHelper = null; // 数据库帮助器的实例
    // 利用单例模式获取数据库帮助器的唯一实例
    public static EvaluatePhotoHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new EvaluatePhotoHelper(context);
        }
        return mHelper;
    }

    // 往该表添加一条记录
    public long insert(EvaluatePhoto info) {
        List<EvaluatePhoto> infoList = new ArrayList<EvaluatePhoto>();
        infoList.add(info);
        return insert(infoList);
    }

    // 往该表添加多条记录
    public long insert(List<EvaluatePhoto> infoList) {
        long result = -1;
        for (int i = 0; i < infoList.size(); i++) {
            EvaluatePhoto info = infoList.get(i);
            List<EvaluatePhoto> tempList = new ArrayList<EvaluatePhoto>();
            // 不存在唯一性重复的记录，则插入新记录
            ContentValues cv = new ContentValues();
            cv.put("evaluate_id", info.evaluate_id);
            cv.put("image_path", info.image_path);
            // 执行插入记录动作，该语句返回插入记录的行号
            result = mWriteDB.insert(mTableName, "", cv);
            if (result == -1) { // 添加成功则返回行号，添加失败则返回-1
                return result;
            }
        }
        Log.d(TAG, "result="+result);
        return result;
    }

    // 根据指定条件查询记录，并返回结果数据列表
    @Override
    public List<EvaluatePhoto> query(String condition) {
        String sql = mSelectSQL + condition;
        Log.d(TAG, "query sql: " + sql);
        List<EvaluatePhoto> infoList = new ArrayList<EvaluatePhoto>();
        // 执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mReadDB.rawQuery(sql, null);
        // 循环取出游标指向的每条记录
        while (cursor.moveToNext()) {
            EvaluatePhoto info = new EvaluatePhoto();
            info.rowid = cursor.getLong(0); // 取出长整型数
            info.xuhao = cursor.getInt(1); // 取出整型数
            info.evaluate_id = cursor.getLong(2); // 取出字符串
            info.image_path = cursor.getString(3); // 取出字符串
            infoList.add(info);
        }
        cursor.close(); // 查询完毕，关闭数据库游标
        Log.d(TAG, "infoList.size="+infoList.size());
        return infoList;
    }

    public List<EvaluatePhoto> queryByEvaluateId(long evaluate_id) {
        String sql = " evaluate_id=" + evaluate_id + ";";
        return query(sql);
    }

    public void deleteByEvaluateId(long evaluate_id) {
        String delete_sql = String.format("delete from %s where evaluate_id=%d;", mTableName, evaluate_id);
        Log.d(TAG, "delete sql="+delete_sql);
        mWriteDB.execSQL(delete_sql);
    }

}
