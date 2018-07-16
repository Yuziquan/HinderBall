package com.example.wuchangi.hinderball.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by WuchangI on 2018/6/17.
 */

//拦阻球view类
class HinderBallView extends View
{
    //设置画笔
    private Paint paint = new Paint();

    //拦阻球球心x坐标
    private int hinderBallX;

    //拦阻球球心y坐标
    private int hinderBallY;

    //拦阻球半径
    private int hinderBallRadius;

    //拦阻球颜色
    private int hinderBallColor;

    public HinderBallView(Context context, int hinderBallX, int hinderBallY, int hinderBallRadius, int hinderBallColor)
    {
        super(context);
        this.hinderBallX = hinderBallX;
        this.hinderBallY = hinderBallY;
        this.hinderBallRadius = hinderBallRadius;
        this.hinderBallColor = hinderBallColor;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //设置填充样式画笔
        paint.setStyle(Paint.Style.FILL);

        //去锯齿
        paint.setAntiAlias(true);

        //设置画笔颜色
        paint.setColor(hinderBallColor);

        //绘制小球
        canvas.drawCircle(hinderBallX, hinderBallY, hinderBallRadius, paint);
    }

    public void setHinderBallX(int hinderBallX)
    {
        this.hinderBallX = hinderBallX;
    }

    public void setHinderBallY(int hinderBallY)
    {
        this.hinderBallY = hinderBallY;
    }
}
