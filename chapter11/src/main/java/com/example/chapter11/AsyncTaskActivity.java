package com.example.chapter11;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter11.task.BookLoadTask;
import com.example.chapter11.task.BookLoadTask.OnProgressListener;

@SuppressLint(value={"SetTextI18n","DefaultLocale"})
public class AsyncTaskActivity extends AppCompatActivity implements OnProgressListener {
    private TextView tv_async;
    private ProgressBar pb_async; // 声明一个进度条对象
    private ProgressDialog dialog; // 声明一个进度对话框对象
    public int mShowStyle; // 显示风格
    public int BAR_HORIZONTAL = 1; // 水平条
    public int DIALOG_CIRCLE = 2; // 圆圈对话框
    public int DIALOG_HORIZONTAL = 3; // 水平对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        tv_async = findViewById(R.id.tv_async);
        // 从布局文件中获取名叫pb_async的进度条
        pb_async = findViewById(R.id.pb_async);
        initBookSpinner(); // 初始化书籍选择下拉框
    }

    // 初始化书籍选择下拉框
    private void initBookSpinner() {
        ArrayAdapter<String> styleAdapter = new ArrayAdapter<String>(this,
                R.layout.item_select, bookArray);
        Spinner sp_style = findViewById(R.id.sp_style);
        sp_style.setPrompt("请选择要加载的小说");
        sp_style.setAdapter(styleAdapter);
        sp_style.setOnItemSelectedListener(new StyleSelectedListener());
        sp_style.setSelection(0);
    }

    private String[] bookArray = {"三国演义", "西游记", "红楼梦"};
    private int[] styleArray = {BAR_HORIZONTAL, DIALOG_CIRCLE, DIALOG_HORIZONTAL};
    class StyleSelectedListener implements OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            startTask(styleArray[arg2], bookArray[arg2]); // 启动书籍加载线程
        }

        public void onNothingSelected(AdapterView<?> arg0) {}
    }

    // 启动书籍加载线程
    private void startTask(int style, String book) {
        mShowStyle = style;
        BookLoadTask task = new BookLoadTask(book); // 创建一个书籍加载线程
        task.setOnProgressListener(this); // 设置书籍加载监听器
        task.execute(book); // 把书籍加载线程加入到处理列表
    }

    // 关闭对话框
    private void closeDialog() {
        if (dialog != null && dialog.isShowing()) { // 对话框仍在显示
            dialog.dismiss(); // 关闭对话框
        }
    }

    // 在线程处理结束时触发
    public void onFinish(String book) {
        String desc = String.format("您要阅读的《%s》已经加载完毕", book);
        tv_async.setText(desc);
        closeDialog(); // 关闭对话框
    }

    // 在线程处理取消时触发
    public void onCancel(String result) {}

    // 在线程处理过程中更新进度时触发
    public void onUpdate(String book, int progress) {
        String desc = String.format("%s当前加载进度为%d%%", book, progress);
        tv_async.setText(desc); // 设置加载进度的文本内容
        if (mShowStyle == BAR_HORIZONTAL) { // 水平条
            pb_async.setProgress(progress); // 设置水平进度条的当前进度
        } else if (mShowStyle == DIALOG_HORIZONTAL) { // 水平对话框
            dialog.setProgress(progress); // 设置水平进度对话框的当前进度
        }
    }

    // 在线程处理开始时触发
    public void onBegin(String book) {
        tv_async.setText(book + "开始加载");
        if (dialog == null || !dialog.isShowing()) {  // 进度框未弹出
            if (mShowStyle == DIALOG_CIRCLE) { // 圆圈对话框
                // 弹出带有提示文字的圆圈进度对话框
                dialog = ProgressDialog.show(this, "稍等", book + "页面加载中……");
            } else if (mShowStyle == DIALOG_HORIZONTAL) { // 水平对话框
                dialog = new ProgressDialog(this); // 创建一个进度对话框
                dialog.setTitle("稍等"); // 设置进度对话框的标题文本
                dialog.setMessage(book + "页面加载中……"); // 设置进度对话框的内容文本
                dialog.setIcon(R.drawable.ic_search); // 设置进度对话框的图标
                dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); // 设置进度样式
                dialog.show(); // 显示进度对话框
            }
        }
    }

}
