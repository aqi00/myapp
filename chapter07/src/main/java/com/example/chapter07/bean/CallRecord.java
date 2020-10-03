package com.example.chapter07.bean;

public class CallRecord {
    public String name; // 姓名
    public String phone; // 手机号
    public int type; // 类型
    public String date; // 通话日期
    public long duration; // 通话时长
    public int _new; // 是否接听

    public CallRecord() {
        name = "";
        phone = "";
        type = 0;
        date = "";
        duration = 0;
        _new = 0;
    }

}
