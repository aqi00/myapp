package com.example.chapter13.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
    // 获取当前的日期时间
    public static String getNowDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    // 获取当前的时间
    public static String getNowTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    // 获取当前的时间（精确到毫秒）
    public static String getNowTimeDetail() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(new Date());
    }

    // 重新格式化日期字符串
    public static String convertDateString(String strTime) {
        String newTime = strTime;
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = oldFormat.parse(strTime);
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            newTime = newFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newTime;
    }

}
