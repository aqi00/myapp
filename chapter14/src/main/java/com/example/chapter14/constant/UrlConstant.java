package com.example.chapter14.constant;

public class UrlConstant {
    // 以下是HTTP接口调用的服务地址，根据实际情况修改IP和端口
    public static final String REQUEST_URL = "http://192.168.1.7:8080/NetServer";
    //public static final String REQUEST_URL = "http://192.168.1.2:6001/NetServer";
    // 检查应用更新的服务地址
    public static final String CHECK_UPDATE_URL = REQUEST_URL + "/checkUpdate";
    // 上传文件的服务地址
    public static final String UPLOAD_URL = REQUEST_URL + "/uploadServlet";
    // 获取旅游图片的服务地址
    public static final String GET_PHOTO_URL = REQUEST_URL + "/getPhoto";
    // 根据经纬度查询详细地址的网址（天地图）
    public static final String GET_ADDRESS_URL = "https://api.tianditu.gov.cn/geocoder?postStr={'lon':%f,'lat':%f,'ver':1}&type=geocode&tk=145897399844a50e3de2309513c8df4b";
    // 请求图片验证码的网址
    public static final String IMAGE_CODE_URL = "http://yx12.fjjcjy.com/Public/Control/GetValidateCode?time=";
}
