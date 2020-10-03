package com.example.chapter13.bean;

public class EvaluateInfo {
    public long rowid; // 行号
    public int xuhao; // 序号
    public long order_id; // 订单编号
    public String goods_name; // 商品名称
    public int evaluate_star; // 评价星级
    public String evaluate_content; // 评价内容
    public String create_time; // 评价的创建时间

    public EvaluateInfo() {
        rowid = 0L;
        xuhao = 0;
        order_id = 0L;
        goods_name = "";
        evaluate_star = 0;
        evaluate_content = "";
        create_time = "";
    }
}
