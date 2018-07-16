package com.example.wuchangi.hinderball.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by WuchangI on 2018/6/17.
 */

//滑块view类
class SlidingBlockView extends View
{
    //设置画笔
    private Paint paint = new Paint();

    //滑块左上角x坐标
    private int slidingBlockX;

    //滑块左上角y坐标
    private int slidingBlockY;

    //滑块宽度
    private int slidingBlockWidth;

    //滑块高度
    private int slidingBlockHeight;

    //滑块颜色
    private int slidingBlockColor;


    public SlidingBlockView(Context context, int slidingBlockXLocation, int slidingBlockYLocation,
                            int slidingBlockWidth, int slidingBlockHeight, int slidingBlockColor)
    {
        super(context);
        setFocusable(true);

        this.slidingBlockX = slidingBlockXLocation;
        this.slidingBlockY = slidingBlockYLocation;
        this.slidingBlockWidth = slidingBlockWidth;
        this.slidingBlockHeight = slidingBlockHeight;
        this.slidingBlockColor = slidingBlockColor;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        //设置填充样式画笔
        paint.setStyle(Paint.Style.FILL);

        //去锯齿
        paint.setAntiAlias(true);

        //设置画笔颜色
        paint.setColor(slidingBlockColor);

        //绘制矩形滑块
        canvas.drawRect(slidingBlockX, slidingBlockY, slidingBlockX + slidingBlockWidth, slidingBlockY + slidingBlockHeight, paint);
    }

    public void setSlidingBlockX(int slidingBlockX)
    {
        this.slidingBlockX = slidingBlockX;
    }

}
