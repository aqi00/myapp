package com.example.chapter13.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.chapter13.bean.EvaluateInfo;

import java.util.ArrayList;
import java.util.List;

public class EvaluateInfoHelper extends DbHelper {
    private static String table_name = "evaluate_info";

    public EvaluateInfoHelper(Context context) {
        super(context, db_name, null, 1);
        mTableName = table_name;
        mSelectSQL = String.format("select rowid,_id,order_id,goods_name,evaluate_star,evaluate_content,create_time from %s where "
                , mTableName);
    }

    private static EvaluateInfoHelper mHelper = null; // 数据库帮助器的实例
    // 利用单例模式获取数据库帮助器的唯一实例
    public static EvaluateInfoHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new EvaluateInfoHelper(context);
        }
        return mHelper;
    }

    // 往该表添加一条记录
    public long insert(EvaluateInfo info) {
        List<EvaluateInfo> infoList = new ArrayList<EvaluateInfo>();
        infoList.add(info);
        return insert(infoList);
    }

    // 往该表添加多条记录
    public long insert(List<EvaluateInfo> infoList) {
        long result = -1;
        for (int i = 0; i < infoList.size(); i++) {
            EvaluateInfo info = infoList.get(i);
            List<EvaluateInfo> tempList = new ArrayList<EvaluateInfo>();
            // 不存在唯一性重复的记录，则插入新记录
            ContentValues cv = new ContentValues();
            cv.put("order_id", info.order_id);
            cv.put("goods_name", info.goods_name);
            cv.put("evaluate_star", info.evaluate_star);
            cv.put("evaluate_content", info.evaluate_content);
            cv.put("create_time", info.create_time);
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
    public List<EvaluateInfo> query(String condition) {
        String sql = mSelectSQL + condition;
        Log.d(TAG, "query sql: " + sql);
        List<EvaluateInfo> infoList = new ArrayList<EvaluateInfo>();
        // 执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mReadDB.rawQuery(sql, null);
        // 循环取出游标指向的每条记录
        while (cursor.moveToNext()) {
            EvaluateInfo info = new EvaluateInfo();
            info.rowid = cursor.getLong(0); // 取出长整型数
            info.xuhao = cursor.getInt(1); // 取出整型数
            info.order_id = cursor.getLong(2); // 取出长整型数
            info.goods_name = cursor.getString(3); // 取出字符串
            info.evaluate_star = cursor.getInt(4); // 取出整型数
            info.evaluate_content = cursor.getString(5); // 取出字符串
            info.create_time = cursor.getString(6); // 取出字符串
            infoList.add(info);
        }
        cursor.close(); // 查询完毕，关闭数据库游标
        Log.d(TAG, "infoList.size="+infoList.size());
        return infoList;
    }

    public EvaluateInfo queryByOrderId(long order_id) {
        List<EvaluateInfo> evaluateList = query("order_id="+order_id);
        return evaluateList.get(0);
    }

}
