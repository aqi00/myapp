package com.example.chapter14.bean;

public class PackageInfo {
    public String app_name; // 应用名称
    public String package_name; // 应用包名
    public String download_url; // 下载地址
    public String new_version; // 新版本号

    public PackageInfo() {
        app_name = "";
        package_name = "";
        download_url = "";
        new_version = "";
    }

    public PackageInfo(String package_name) {
        this.package_name = package_name;
    }

}
