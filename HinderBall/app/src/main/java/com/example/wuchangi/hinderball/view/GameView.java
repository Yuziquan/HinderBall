package com.example.wuchangi.hinderball.view;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import com.example.wuchangi.hinderball.R;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by WuchangI on 2018/6/17.
 */


//游戏界面类
public class GameView extends View
{
    //自动控制的滑块
    private SlidingBlockView autoSlidingBlock;

    //玩家控制的滑块
    private SlidingBlockView playerSlidingBlock;

    //每个小球
    private HinderBallView[] hinderBalls;

    //游戏界面宽度
    private int gameViewWidth;

    //游戏界面高度
    private int gameViewHeight;

    //自动滑块的左上角x坐标
    private int autoSlidingBlockX;

    //自动滑块的左上角y坐标
    private int autoSlidingBlockY;

    //玩家滑块的左上角x坐标
    private int playerSlidingBlockX;

    //玩家滑块的左上角y坐标
    private int playerSlidingBlockY;

    //滑块的宽度
    private int slidingBlockWidth;

    //滑块的高度
    private int slidingBlockHeight;

    //每个小球的半径大小
    private int hinderBallRadius;

    //可供选择的每个小球的初始速度（其实是初始移动方向）
    private final int[] velocity = {-1, 1};

    //每个小球水平方向的速度
    private int[] hinderBallXVelocity;

    //小球垂直方向的速度
    private int[] hinderBallYVelocity;

    //每个小球球心x坐标
    private int[] hinderBallX;

    //每个小球球心y坐标
    private int[] hinderBallY;

    //设置画笔
    private Paint paint = new Paint();

    //保存一个上下文副本
    private Context context;

    //设置定时器（控制小球运动速度）
    private Timer timerForBalls;

    //设置定时器(控制自动滑块运动速度)
    private Timer timerForAutoSlidingBlock;

    //标志每个小球是否存在(存在：1， 不存在：0)
    private int[] hinderBallsIsExisted;

    //标志每个小球是否被激活(已激活：1， 未激活：0)
    private int[] hinderBallIsActivated;

    //玩家胜局数
    private int playerVictoryRounds;

    //电脑胜局数
    private int autoVictoryRounds;

    //玩家是否胜利
    private boolean isPlayerWinTheGame = false;

    //游戏是否结束
    private boolean isGameOver = false;

    //消息处理器（用于向主线程发送刷新界面信息）
    private Handler handler;

    //随机数产生器
    private Random random = new Random();

    //为小球设置的待选颜色
    private int[] hinderBallColorToBeSelected = {Color.BLUE, Color.RED, Color.GRAY, Color.GREEN, Color.WHITE, Color.YELLOW};

    //游戏赛制
    private String gameRule;

    //双方中某方的胜局数达到这个数目时，比赛结果已知，比赛结束
    private int maxVictoryRounds;

    //小球个数
    private int hinderBallNum;

    //自动滑块每隔一个时间段移动一次，该值越小，自动滑块移动速度显得越快，越灵敏
    private int autoSlidingBlockMovePeriod;

    //小球每隔一个时间段移动一次，该值越小，小球移动速度显得越快
    private int hinderBallMovePeriod;

    //振动器
    private Vibrator vibrator;

    //定时器执行的次数
    private int loopTimes = 1;

    //记录当前已被激活的小球下标
    private int currentHinderBallWhichIsActivated;

    //两个提示对话框
    private AlertDialog alertDialogOfWin;
    private AlertDialog alertDialogOfLose;

    public GameView(Context context, int gameViewWidth, int gameViewHeight, String gameRule, String gameLevel, Handler handler)
    {
        super(context);

        setFocusable(true);

        this.context = context;
        this.gameViewWidth = gameViewWidth;
        this.gameViewHeight = gameViewHeight;
        this.gameRule = gameRule;
        this.handler = handler;


        //根据游戏赛制和难度等级初始化相关参数
        initGameSettings(gameRule, gameLevel);

        //初始化相关界面参数
        initComponents();
    }

    //界面控件初始化
    public void initComponents()
    {
        //初始双方胜局数各自为0
        playerVictoryRounds = autoVictoryRounds = 0;

        //滑块的宽高
        slidingBlockHeight = gameViewHeight / 25;
        slidingBlockWidth = gameViewWidth / 4;

        //小球半径
        hinderBallRadius = slidingBlockHeight / 3;

        //设置两个滑块的初始位置
        autoSlidingBlockX = playerSlidingBlockX = gameViewWidth / 2 - slidingBlockWidth / 2;
        autoSlidingBlockY = 150;
        playerSlidingBlockY = gameViewHeight - 220;

        //创建滑块
        autoSlidingBlock = new SlidingBlockView(context, autoSlidingBlockX, autoSlidingBlockY, slidingBlockWidth, slidingBlockHeight, Color.parseColor("#D98719"));
        playerSlidingBlock = new SlidingBlockView(context, playerSlidingBlockX, playerSlidingBlockY, slidingBlockWidth, slidingBlockHeight, Color.parseColor("#38B0DE"));


        //设置每个小球的初始位置坐标
        for (int i = 0; i < hinderBallNum; i++)
        {
            hinderBallX[i] = 10 + random.nextInt(gameViewWidth);
            hinderBallY[i] = gameViewHeight / 2 + (random.nextInt(20) - 10);
        }

        //创建小球
        for (int i = 0; i < hinderBallNum; i++)
        {
            hinderBalls[i] = new HinderBallView(context, hinderBallX[i], hinderBallY[i], hinderBallRadius, hinderBallColorToBeSelected[random.nextInt(6)]);
        }

        //设置每个小球的初始方向和速度(随机的)
        for (int i = 0; i < hinderBallNum; i++)
        {
            hinderBallXVelocity[i] = velocity[random.nextInt(2)];
            hinderBallYVelocity[i] = velocity[random.nextInt(2)];
        }

        currentHinderBallWhichIsActivated = 0;

        //设置定时器(控制小球运动速度)
        timerForBalls = new Timer();

        //设置定时任务
        timerForBalls.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < currentHinderBallWhichIsActivated; i++)
                {
                    if (hinderBallsIsExisted[i] == 1)
                    {
                        //如果球碰到左右边界,则反向
                        if (hinderBallX[i] - hinderBallRadius <= 0 || hinderBallX[i] + hinderBallRadius >= gameViewWidth)
                        {
                            hinderBallXVelocity[i] = -hinderBallXVelocity[i];
                        }

                        //如果球碰到了上下边界
                        if (hinderBallY[i] - hinderBallRadius <= 0 || hinderBallY[i] + hinderBallRadius >= gameViewHeight)
                        {
                            hinderBallYVelocity[i] = -hinderBallYVelocity[i];
                        }


                        //如果玩家滑块接到球
                        if ((hinderBallY[i] + hinderBallRadius >= playerSlidingBlockY) && (hinderBallX[i] + hinderBallRadius >= playerSlidingBlockX && hinderBallX[i] - hinderBallRadius <= playerSlidingBlockX + slidingBlockWidth))
                        {
                            hinderBallYVelocity[i] = -hinderBallYVelocity[i];

                            //小球撞击滑块反弹时的音效
                            handler.sendEmptyMessage(0x126);
                        }
                        //如果玩家滑块没有接到球
                        else if (hinderBallY[i] + hinderBallRadius > playerSlidingBlockY + 20 && (hinderBallX[i] + hinderBallRadius < playerSlidingBlockX || hinderBallX[i] - hinderBallRadius > playerSlidingBlockX + slidingBlockWidth))
                        {
                            //不显示该小球
                            hinderBallsIsExisted[i] = 0;

                            autoVictoryRounds++;

                            //产生手机振动
                            vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
                            //振动持续0.2秒
                            vibrator.vibrate(200);

                        }

                        //如果自动滑块接到球
                        if ((hinderBallY[i] - hinderBallRadius <= autoSlidingBlockY + slidingBlockHeight) && (hinderBallX[i] + hinderBallRadius >= autoSlidingBlockX && hinderBallX[i] - hinderBallRadius <= autoSlidingBlockX + slidingBlockWidth))
                        {
                            hinderBallYVelocity[i] = -hinderBallYVelocity[i];

                            //小球撞击滑块反弹时的音效
                            handler.sendEmptyMessage(0x126);
                        }
                        //如果自动滑块没有接到球
                        else if (hinderBallY[i] - hinderBallRadius < autoSlidingBlockY + slidingBlockHeight - 20 && (hinderBallX[i] + hinderBallRadius < autoSlidingBlockX || hinderBallX[i] - hinderBallRadius > autoSlidingBlockX + slidingBlockWidth))
                        {
                            //不显示该小球
                            hinderBallsIsExisted[i] = 0;

                            playerVictoryRounds++;

                            //产生手机振动
                            vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
                            //振动持续0.2秒
                            vibrator.vibrate(200);
                        }

                        //玩家胜
                        if (playerVictoryRounds == maxVictoryRounds)
                        {
                            timerForBalls.cancel();
                            isPlayerWinTheGame = true;
                            isGameOver = true;
                        }
                        //电脑胜
                        else if (autoVictoryRounds == maxVictoryRounds)
                        {
                            timerForBalls.cancel();
                            isPlayerWinTheGame = false;
                            isGameOver = true;
                        }

                        //小球继续运动
                        hinderBallX[i] += hinderBallXVelocity[i];
                        hinderBallY[i] += hinderBallYVelocity[i];
                    }
                }

                loopTimes++;

                if (currentHinderBallWhichIsActivated < hinderBallNum && loopTimes % 200 == 0)
                {
                    currentHinderBallWhichIsActivated++;
                }

                //通知主线程重绘gameView(所有组件)
                handler.sendEmptyMessage(0x123);
            }
        }, 0, hinderBallMovePeriod);


        //设置定时器（控制自动滑块运动速度）
        timerForAutoSlidingBlock = new Timer();

        //设置定时任务
        timerForAutoSlidingBlock.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                //自动滑块进行水平方向随机运动
                autoSlidingBlockX = random.nextInt(gameViewWidth);

                //通知主线程重绘gameView(所有组件)
                handler.sendEmptyMessage(0x123);
            }
        }, 0, autoSlidingBlockMovePeriod);

    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //设置填充样式画笔
        paint.setStyle(Paint.Style.FILL);

        //去锯齿
        paint.setAntiAlias(true);

        //显示玩家和电脑的比赛胜局数信息
        paint.setTextSize(50);

        paint.setColor(Color.YELLOW);
        canvas.drawText("电脑当前胜局数：" + autoVictoryRounds, gameViewWidth / 2 - 150, 100, paint);
        paint.setColor(Color.GREEN);
        canvas.drawText("玩家当前胜局数：" + playerVictoryRounds, gameViewWidth / 2 - 150, gameViewHeight - 100, paint);

        //如果游戏结束
        if (isGameOver)
        {
            timerForAutoSlidingBlock.cancel();

            if (isPlayerWinTheGame)
            {
                //播放游戏成功时的音效
                handler.sendEmptyMessage(0x127);

                alertDialogOfWin = new AlertDialog.Builder(context)
                        .setTitle("恭喜你!挑战成功!")
                        .setIcon(R.mipmap.win)
                        .setMessage("\t\t\t\t\t\t\t是否重来一局?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        handler.sendEmptyMessage(0x125);
                    }
                }).setNegativeButton("否, 返回主菜单", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        handler.sendEmptyMessage(0x124);
                    }
                }).create();

                alertDialogOfWin.show();

            }
            else
            {
                //播放游戏失败时的音效
                handler.sendEmptyMessage(0x128);

                alertDialogOfLose = new AlertDialog.Builder(context)
                        .setTitle("很遗憾~~挑战失败~~")
                        .setIcon(R.mipmap.lose)
                        .setMessage("\t\t\t\t\t\t\t是否重来一局?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        handler.sendEmptyMessage(0x125);
                    }
                }).setNegativeButton("否, 返回主菜单", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        handler.sendEmptyMessage(0x124);
                    }
                }).create();

                alertDialogOfLose.show();
            }

        }

        //游戏还未结束
        else
        {
            autoSlidingBlock.setSlidingBlockX(autoSlidingBlockX);
            playerSlidingBlock.setSlidingBlockX(playerSlidingBlockX);

            for (int i = 0; i < hinderBallNum; i++)
            {
                if (hinderBallsIsExisted[i] == 1)
                {
                    hinderBalls[i].setHinderBallX(hinderBallX[i]);
                    hinderBalls[i].setHinderBallY(hinderBallY[i]);
                }
            }

            //绘制两个滑块
            autoSlidingBlock.draw(canvas);
            playerSlidingBlock.draw(canvas);

            //绘制三个小球
            for (int i = 0; i < hinderBallNum; i++)
            {
                if (hinderBallsIsExisted[i] == 1)
                {
                    hinderBalls[i].draw(canvas);
                }
            }
        }

    }

    //根据游戏赛制和难度等级初始化相关参数
    public void initGameSettings(String gameRule, String gameLevel)
    {
        //根据赛制设置maxVictoryRound的值
        if (gameRule.equals("rule1"))
        {
            maxVictoryRounds = 3;
        }
        else
        {
            maxVictoryRounds = 4;
        }

        //根据比赛难度等级设置相关参数
        switch (gameLevel)
        {
            //入门级难度
            case "level1":

                hinderBallNum = 7;
                autoSlidingBlockMovePeriod = 13;
                hinderBallMovePeriod = 5;
                break;

            //中级难度
            case "level2":
                hinderBallNum = 8;
                autoSlidingBlockMovePeriod = 9;
                hinderBallMovePeriod = 4;
                break;

            //高级难度
            case "level3":
                hinderBallNum = 9;
                autoSlidingBlockMovePeriod = 5;
                hinderBallMovePeriod = 3;
                break;

            //大师级难度
            case "level4":
                hinderBallNum = 10;
                autoSlidingBlockMovePeriod = 1;
                hinderBallMovePeriod = 2;
                break;
        }

        hinderBalls = new HinderBallView[hinderBallNum];

        hinderBallX = new int[hinderBallNum];
        hinderBallY = new int[hinderBallNum];

        hinderBallXVelocity = new int[hinderBallNum];
        hinderBallYVelocity = new int[hinderBallNum];

        hinderBallsIsExisted = new int[hinderBallNum];
        //开始时所有小球均存在
        Arrays.fill(hinderBallsIsExisted, 0, hinderBallNum, 1);

        hinderBallIsActivated = new int[hinderBallNum];
        //开始时所有小球均未激活
        Arrays.fill(hinderBallIsActivated, 0, hinderBallNum, 0);
    }



    //销毁gameView中残余的东西
    public void destroyGameView()
    {
        //销毁所有定时器
        timerForBalls.cancel();
        timerForAutoSlidingBlock.cancel();
    }


    public int getPlayerSlidingBlockY()
    {
        return playerSlidingBlockY;
    }


    public void setPlayerSlidingBlockX(int playerSlidingBlockX)
    {
        this.playerSlidingBlockX = playerSlidingBlockX;
    }

    public int getSlidingBlockWidth()
    {
        return slidingBlockWidth;
    }


    public int getSlidingBlockHeight()
    {
        return slidingBlockHeight;
    }


}

