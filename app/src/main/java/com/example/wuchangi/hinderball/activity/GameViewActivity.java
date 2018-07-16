package com.example.wuchangi.hinderball.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.*;
import com.example.wuchangi.hinderball.R;
import com.example.wuchangi.hinderball.view.GameView;

/**
 * Created by WuchangI on 2018/6/17.
 */

//游戏界面类
public class GameViewActivity extends AppCompatActivity
{

    //手机屏幕宽度
    private int screenWidth;
    //手机屏幕高度
    private int screenHeight;

    //游戏界面
    private GameView gameView;

    //消息处理器（用于向主线程发送刷新界面信息）
    private Handler handler;

    //用来保存界面所有单选按钮被选中的状态
    private SharedPreferences radioButtonsState;

    //使用音效池播放游戏音效（支持多个音乐文件同时播放）
    private SoundPool soundPool;

    //保存每个音效的ID
    private int winSoundID;
    private int loseSoundID;
    private int hitSoundID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //界面初始化
        init();

        //初始化GameView
        initGameView();

        setContentView(gameView);
    }


    //界面初始化
    public void init()
    {
        //去掉窗口标题
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //沉浸式状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //获取窗口管理器
        WindowManager windowManager = getWindowManager();

        //获取手机屏幕的宽度和高度
        Display display = windowManager.getDefaultDisplay();
        final DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        //支持同时播放10个音频文件
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        winSoundID = soundPool.load(this, R.raw.win, 1);
        loseSoundID = soundPool.load(this, R.raw.lose, 1);
        hitSoundID = soundPool.load(this, R.raw.hit, 1);


        //设置消息处理器
        handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case 0x123:
                        //重绘gameView
                        gameView.invalidate();

                        break;

                    case 0x124:
                        //返回主菜单
                        finish();

                        break;

                    case 0x125:
                        //销毁gameView中残余的东西
                        gameView.destroyGameView();
                        //释放音效池
                        soundPool.release();

                        //重新开始游戏(去动画，使之不闪屏)
                        Intent intent = getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                        overridePendingTransition(0, 0);

                        break;

                    case 0x126:
                        //小球撞击滑块反弹时的音效
                        soundPool.play(hitSoundID, 0.8f, 0.8f,1, 0, 1.5f);

                        break;


                    case 0x127:
                        //播放游戏成功时的音效
                        soundPool.play(winSoundID, 0.8f, 0.8f,1, 0, 1.5f);

                        break;

                    case 0x128:
                        //播放游戏失败时的音效
                        soundPool.play(loseSoundID, 0.8f, 0.8f,1, 0, 1.5f);

                        break;
                }

            }

        };
    }


    //初始化GameView
    public void initGameView()
    {
        //获取"设置界面"中的单选按钮被选中的状态
        radioButtonsState = getSharedPreferences("radioButtonsState", MODE_PRIVATE);

        //创建游戏界面
        gameView = new GameView(GameViewActivity.this, screenWidth, screenHeight, radioButtonsState.getString("ruleState", "rule1"), radioButtonsState.getString("levelState", "level1"), handler);

        //为游戏界面设置背景图
        gameView.setBackgroundResource(R.mipmap.game_view_bg);

        //为gameView设置监听器，实质是为了监听gameView中的玩家所控制的滑块的触摸事件
        gameView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if ((int) motionEvent.getY() <= gameView.getPlayerSlidingBlockY() + gameView.getSlidingBlockHeight() + 100 && (int) motionEvent.getY() >= gameView.getPlayerSlidingBlockY() - 100)
                {
                    gameView.setPlayerSlidingBlockX((int) motionEvent.getX() - gameView.getSlidingBlockWidth() / 2);
                    gameView.invalidate();
                }

                return true;
            }
        });
    }

    //当玩家按手机返回键时，结束当前游戏
    @Override
    public void onBackPressed()
    {
        //销毁gameView中残余的东西
        gameView.destroyGameView();
        //释放音效池
        soundPool.release();

        finish();

    }

}

