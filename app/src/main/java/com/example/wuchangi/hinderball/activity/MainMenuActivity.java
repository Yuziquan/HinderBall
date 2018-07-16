package com.example.wuchangi.hinderball.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.example.wuchangi.hinderball.R;
import com.example.wuchangi.hinderball.custom.MyButton;
import com.example.wuchangi.hinderball.service.GameBGMService;


/**
 * Created by WuchangI on 2018/6/17.
 */

//主菜单界面类
public class MainMenuActivity extends AppCompatActivity
{
    private MyButton startGameButton;
    private MyButton setGameRulesButton;
    private MyButton exitGameButton;

    private SharedPreferences radioButtonsState = null;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init();

        setContentView(R.layout.main_menu_activity);

        //为界面上的所有按钮绑定监听器
        bindingListeners();

    }


    //界面初始化
    public void init()
    {
        //去掉窗口标题
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //沉浸式状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        radioButtonsState = getSharedPreferences("radioButtonsState", MODE_PRIVATE);

        //第一次开启应用时
        if(!radioButtonsState.contains("ruleState") && !radioButtonsState.contains("levelState")
                && !radioButtonsState.contains("musicState"))
        {
            //将"设置界面"中的单选按钮默认被选中的状态保存起来
            editor = radioButtonsState.edit();

            //默认赛制为rule1
            editor.putString("ruleState", "rule1");
            //默认难度级别为level1
            editor.putString("levelState", "level1");
            //默认背景音乐为开启状态
            editor.putString("musicState", "musicOn");
        }



        if(radioButtonsState.getString("musicState", "musicOn").equals("musicOn"))
        {
            //开启背景音乐
            Intent startMusicIntent = new Intent(this, GameBGMService.class);
            startService(startMusicIntent);
        }

    }



    //获取界面上的控件，并绑定监听器
    public void bindingListeners()
    {
        //获取控件
        startGameButton = (MyButton) findViewById(R.id.startGame);
        setGameRulesButton = (MyButton) findViewById(R.id.setGameRules);
        exitGameButton = (MyButton) findViewById(R.id.exitGame);

        //绑定监听器

        //开始游戏
        startGameButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent startGameViewIntent = new Intent(MainMenuActivity.this, GameViewActivity.class);
                startActivity(startGameViewIntent);
            }
        });


        //游戏设置
        setGameRulesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent startGameRulesIntent = new Intent(MainMenuActivity.this, GameSettingsActivity.class);
                startActivity(startGameRulesIntent);
            }
        });


        //退出游戏
        exitGameButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (radioButtonsState.getString("musicState", "musicOn").equals("musicOn"))
                {
                    //结束背景音乐的播放
                    Intent gameBGMIntent = new Intent(MainMenuActivity.this,
                            GameBGMService.class);


                    stopService(gameBGMIntent);
                }

                //退出主菜单
                finish();
            }
        });

    }

    //当玩家按手机返回键时，退出主菜单
    @Override
    public void onBackPressed()
    {
        if(radioButtonsState.getString("musicState", "musicOn").equals("musicOn"))
        {
            //结束背景音乐的播放
            Intent gameBGMIntent = new Intent(MainMenuActivity.this,
                    GameBGMService.class);


            stopService(gameBGMIntent);
        }

        //退出主菜单
        finish();
    }

}
