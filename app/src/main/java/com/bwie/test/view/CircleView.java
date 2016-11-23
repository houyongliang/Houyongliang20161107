package com.bwie.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Boolean.FALSE;

/**
 * Created by mis on 2016/11/7. 自定义 CircleView
 */

public class CircleView extends View {

    private Paint cPaint;
    private Paint tPaint;
    private int width;
    private int height;
    //设置监听器的接口 定义一个接口对象listerner
    private OnItemSelectListener listener;
    private int lenth;
    private String text;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //定义圆形画笔
        cPaint = new Paint();
        cPaint.setStyle(Paint.Style.STROKE);//设置空的
        cPaint.setColor(Color.RED);//设置颜色
        cPaint.setAntiAlias(true);//去锯齿

        //定义字体画笔
        tPaint = new Paint();
        tPaint.setStyle(Paint.Style.FILL);//设置实心
        tPaint.setColor(Color.GREEN);/*设置颜色*/
        tPaint.setTextSize(50);/*设置字体大小*/

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /*获取宽高*/
        width = getWidth();
        height = getHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*绘制 圆和 字体*/
        lenth = Math.min(width, height);
        text = "下一步";
        /*画圆 时 定义 原点和半径及其 样式*/
        canvas.drawCircle(lenth / 2, lenth / 2, lenth / 3, cPaint);
        /*画文本的时候 设置文本 字体的大小 起始位置 和画笔 */
        canvas.drawText(text, 0, text.length(), lenth / 2 - 60, lenth / 2 + 10, tPaint);
        super.onDraw(canvas);
    }

    /*定义监听器*/
    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.listener = listener;
    }
   //定义设置监听 接口
    public interface  OnItemSelectListener{
        public void onItemSelect(int index, String indexString);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                float x=event.getX();
                float y = event.getY();
                /*设置 在 园内进行点击 ，点击后进行跳转*/
                if( (x-lenth / 2)*(x-lenth / 2)+(y-lenth / 2)*(y-lenth / 2)<lenth / 3*lenth / 3){

                    //此处有增加，当屏幕被点击后，将参数传入。
                    if(listener!=null){
                        listener.onItemSelect(0,null);
                    }
                    invalidate();
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:

                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }
}
