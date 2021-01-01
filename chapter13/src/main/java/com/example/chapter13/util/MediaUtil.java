package com.example.chapter13.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

@SuppressLint("DefaultLocale")
public class MediaUtil {
    private final static String TAG = "MediaUtil";

    // 格式化播放时长（mm:ss）
    public static String formatDuration(int milliseconds) {
        int seconds = milliseconds / 1000;
        int hour = seconds / 3600;
        int minute = seconds / 60;
        int second = seconds % 60;
        String str;
        if (hour > 0) {
            str = String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            str = String.format("%02d:%02d", minute, second);
        }
        return str;
    }

    // 获得音视频文件的缓存路径
    public static String getRecordFilePath(Context context, String dir_name, String extend_name) {
        String path = "";
        File recordDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + dir_name + "/");
        if (!recordDir.exists()) {
            recordDir.mkdirs();
        }
        try {
            File recordFile = File.createTempFile(DateUtil.getNowDateTime(), extend_name, recordDir);
            path = recordFile.getAbsolutePath();
            Log.d(TAG, "dir_name=" + dir_name + ", extend_name=" + extend_name + ", path=" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    // 获取视频文件中的某帧图片
    public static Bitmap getOneFrame(Context ctx, Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(ctx, uri);
        // 获得视频的播放时长，大于1秒的取第1秒处的帧图，不足1秒的取第0秒处的帧图
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        Log.d(TAG, "duration="+duration);
        int pos = (Integer.parseInt(duration)/1000)>1 ? 1 : 0;
        // 获取指定时间的帧图，注意getFrameAtTime方法的时间单位是微秒
        return retriever.getFrameAtTime(pos * 1000 * 1000);
    }
}
