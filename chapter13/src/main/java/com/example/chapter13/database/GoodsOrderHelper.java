package com.example.chapter13.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.chapter13.bean.GoodsOrder;

import java.util.ArrayList;
import java.util.List;

public class GoodsOrderHelper extends DbHelper {
    private static String table_name = "goods_order";

    public GoodsOrderHelper(Context context) {
        super(context, db_name, null, 1);
        mTableName = table_name;
        mSelectSQL = String.format("select rowid,_id,goods_name,price,evaluate_status,create_time from %s where "
                , mTableName);
    }

    private static GoodsOrderHelper mHelper = null; // 数据库帮助器的实例
    // 利用单例模式获取数据库帮助器的唯一实例
    public static GoodsOrderHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new GoodsOrderHelper(context);
        }
        return mHelper;
    }

    // 往该表添加一条记录
    public long insert(GoodsOrder info) {
        List<GoodsOrder> infoList = new ArrayList<GoodsOrder>();
        infoList.add(info);
        return insert(infoList);
    }

    // 往该表添加多条记录
    public long insert(List<GoodsOrder> infoList) {
        long result = -1;
        for (int i = 0; i < infoList.size(); i++) {
            GoodsOrder info = infoList.get(i);
            List<GoodsOrder> tempList = new ArrayList<GoodsOrder>();
            // 不存在唯一性重复的记录，则插入新记录
            ContentValues cv = new ContentValues();
            cv.put("goods_name", info.goods_name);
            cv.put("price", info.price);
            cv.put("evaluate_status", info.evaluate_status);
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
    public List<GoodsOrder> query(String condition) {
        String sql = mSelectSQL + condition;
        Log.d(TAG, "query sql: " + sql);
        List<GoodsOrder> infoList = new ArrayList<GoodsOrder>();
        // 执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mReadDB.rawQuery(sql, null);
        // 循环取出游标指向的每条记录
        while (cursor.moveToNext()) {
            GoodsOrder info = new GoodsOrder();
            info.rowid = cursor.getLong(0); // 取出长整型数
            info.xuhao = cursor.getInt(1); // 取出整型数
            info.goods_name = cursor.getString(2); // 取出字符串
            info.price = cursor.getDouble(3); // 取出双精度数
            info.evaluate_status = cursor.getInt(4); // 取出整型数
            info.create_time = cursor.getString(5); // 取出字符串
            infoList.add(info);
        }
        cursor.close(); // 查询完毕，关闭数据库游标
        Log.d(TAG, "infoList.size="+infoList.size());
        return infoList;
    }

    public int updateStatus(GoodsOrder order) {
        Log.d(TAG, "order_id="+order.rowid+", evaluate_status="+order.evaluate_status);
        ContentValues cv = new ContentValues();
        cv.put("evaluate_status", order.evaluate_status);
        // 执行更新记录动作，该语句返回更新的记录数量
        return mWriteDB.update(mTableName, cv, "rowid=" + order.rowid, null);
    }

}
