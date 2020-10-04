package com.example.chapter06.bean;

//购物车信息
public class CartInfo {
    public long rowid; // 行号
    public int xuhao; // 序号
    public long goods_id; // 商品编号
    public int count; // 商品数量
    public String update_time; // 更新时间

    public CartInfo() {
        rowid = 0L;
        xuhao = 0;
        goods_id = 0L;
        count = 0;
        update_time = "";
    }
}
