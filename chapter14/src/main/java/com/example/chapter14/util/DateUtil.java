package com.example.chapter14.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

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

    // 获取指定格式的日期时间
    public static String getNowDateTime(String formatStr) {
        String format = formatStr;
        if (TextUtils.isEmpty(format)) {
            format = "yyyyMMddHHmmss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String formatDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
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
