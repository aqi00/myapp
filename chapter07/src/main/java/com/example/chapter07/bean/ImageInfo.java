package com.example.chapter07.bean;

public class ImageInfo {
    private long id; // 图片编号
    private String name; // 图片标题
    private long size; // 文件大小
    private String path; // 文件路径

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
