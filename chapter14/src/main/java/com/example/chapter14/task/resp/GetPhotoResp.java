package com.example.chapter14.task.resp;

import com.example.chapter14.bean.PhotoInfo;

import java.util.List;

public class GetPhotoResp {
    private List<PhotoInfo> photo_list; // 照片列表

    public void setPhotoList(List<PhotoInfo> photo_list) {
        this.photo_list = photo_list;
    }

    public List<PhotoInfo> getPhotoList() {
        return this.photo_list;
    }

}
