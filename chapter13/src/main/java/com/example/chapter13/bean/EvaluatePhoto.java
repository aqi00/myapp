package com.example.chapter13.bean;

public class EvaluatePhoto {
    public long rowid; // 行号
    public int xuhao; // 序号
    public long evaluate_id; // 评价编号
    public String image_path; // 图片路径

    public EvaluatePhoto() {
        rowid = 0L;
        xuhao = 0;
        evaluate_id = -1;
        image_path = "";
    }
}
