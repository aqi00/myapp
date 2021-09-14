package com.example.chapter14.task;

import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.chapter14.constant.UrlConstant;
import com.example.chapter14.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// 根据经纬度获取详细地址的异步任务
public class GetAddressTask extends AsyncTask<Location, Void, String> {
    private final static String TAG = "GetAddressTask";
    private String mQueryUrl = "https://api.tianditu.gov.cn/geocoder?postStr=%s&type=geocode&tk=253b3bd69713d4bdfdc116255f379841";

    // 线程正在后台处理
    protected String doInBackground(Location... params) {
        Location location = params[0];
        //String url = String.format(mQueryUrl, mLocation.getLongitude(), mLocation.getLatitude());
        String param = String.format("{'lon':%f,'lat':%f,'ver':1}", location.getLongitude(), location.getLatitude());
        try {
            param = URLEncoder.encode(param, "utf8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(mQueryUrl, param);
//        // 把经度和纬度代入到URL地址。天地图的地址查询url在UrlConstant.java中定义
//        String url = String.format(UrlConstant.GET_ADDRESS_URL,
//                location.getLongitude(), location.getLatitude());
        Log.d(TAG, "url = " + url);
        String resp = HttpUtil.get(url, null); // 发送HTTP请求信息，并获得HTTP应答内容
        Log.d(TAG, "resp = " + resp);
        String address = "未知";
        // 下面从JSON串中解析formatted_address字段获得详细地址描述
        if (!TextUtils.isEmpty(resp)) {
            try {
                JSONObject obj = new JSONObject(resp);
                JSONObject result = obj.getJSONObject("result");
                address = result.getString("formatted_address");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "address = " + address);
        return address; // 返回HTTP应答内容中的详细地址
    }

    // 线程已经完成处理
    protected void onPostExecute(String address) {
        mListener.onFindAddress(address); // HTTP调用完毕，触发监听器的找到地址事件
    }

    private OnAddressListener mListener; // 声明一个查询详细地址的监听器对象
    // 设置查询详细地址的监听器
    public void setOnAddressListener(OnAddressListener listener) {
        mListener = listener;
    }

    // 定义一个查询详细地址的监听器接口
    public interface OnAddressListener {
        void onFindAddress(String address);
    }

}
