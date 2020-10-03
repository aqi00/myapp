package com.example.chapter14.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.chapter14.util.Utils;

@SuppressLint("DrawAllocation")
public class TextProgressCircle extends View {
    private Context mContext; // 声明一个上下文对象
    private Paint mPaintBack = new Paint(); // 声明一个背景画笔对象
    private Paint mPaintFore = new Paint(); // 声明一个前景画笔对象
    private Paint mPaintText = new Paint(); // 声明一个文本画笔对象
    private int mLineWidth; // 线条的宽度
    private int mProgress = 0; // 进度值

    public TextProgressCircle(Context context) {
        this(context, null);
    }

    public TextProgressCircle(Context context, AttributeSet attr) {
        super(context, attr);
        mContext = context;
        initPaint(); // 初始化画笔
    }

    // 初始化画笔
    private void initPaint() {
        mLineWidth = Utils.dip2px(mContext, 10);
        // 以下初始化背景画笔
        mPaintBack.setColor(Color.LTGRAY);
        mPaintBack.setStrokeWidth(mLineWidth);
        mPaintBack.setStyle(Style.STROKE);
        // 以下初始化前景画笔
        mPaintFore.setColor(Color.GREEN);
        mPaintFore.setStrokeWidth(mLineWidth);
        mPaintFore.setStyle(Style.STROKE);
        // 以下初始化文本画笔
        mPaintText.setColor(Color.BLUE);
        mPaintText.setTextSize(Utils.dip2px(mContext, 50));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth(); // 获得视图测量后的宽度
        int height = getMeasuredHeight(); // 获得视图测量后的高度
        int diameter = Math.min(width, height); // 宽度和高度取较小的那个作为进度圆圈的直径
        RectF rectF = new RectF((width - diameter) / 2 + mLineWidth, (height - diameter) / 2 + mLineWidth,
                (width + diameter) / 2 - mLineWidth, (height + diameter) / 2 - mLineWidth);
        // 在画布上绘制完整的背景圆圈
        canvas.drawArc(rectF, 0, 360, false, mPaintBack);
        // 在画布上绘制规定进度的前景圆弧
        canvas.drawArc(rectF, 0, mProgress * 360 / 100, false, mPaintFore);
        String text = mProgress + "%";
        Rect rect = new Rect();
        mPaintText.getTextBounds(text, 0, text.length(), rect); // 获得进度文字的矩形边界
        int x = (width / 2) - rect.centerX(); // 计算进度文字左上角的横坐标
        int y = (height / 2) - rect.centerY(); // 计算进度文字左上角的纵坐标
        canvas.drawText(text, x, y, mPaintText); // 在画布上绘制进度文字
    }

    // 设置进度值
    public void setProgress(int progress) {
        mProgress = progress;
        invalidate(); // 立刻刷新视图
    }

}
