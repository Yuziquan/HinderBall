package com.example.wuchangi.hinderball.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.wuchangi.hinderball.R;
import com.example.wuchangi.hinderball.service.GameBGMService;

/**
 * Created by WuchangI on 2018/6/17.
 */

//游戏设置界面类
public class GameSettingsActivity extends AppCompatActivity
{
    private Toolbar gameSettingToolbar;

    private RadioGroup competitionRulesRadioGroup;
    private RadioGroup gameLevelRadioGroup;
    private RadioGroup musicOnOffRadioGroup;

    private RadioButton rule1RadioButton;
    private RadioButton rule2RadioButton;

    private RadioButton level1RadioButton;
    private RadioButton level2RadioButton;
    private RadioButton level3RadioButton;
    private RadioButton level4RadioButton;

    private RadioButton musicOnRadioButton;
    private RadioButton musicOffRadioButton;

    private SeekBar musicVolumeSeekBar;
    private TextView maxVolumeTextView;
    private TextView curVolumeTextView;


    //音频管理器
    private AudioManager audioManager;
    //最大音量
    private int maxVolume;
    //当前音量
    private int currentVolume;

    //用来保存界面所有单选按钮被选中的状态
    private SharedPreferences radioButtonsState = null;
    private SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_settings_activity);

        //初始化界面并绑定监听器
        initAndBindingListeners();
    }


    //初始化界面，获取界面上的控件，并绑定监听器
    public void initAndBindingListeners()
    {
        //沉浸式状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //获取界面控件
        gameSettingToolbar = (Toolbar) findViewById(R.id.game_settings);

        setSupportActionBar(gameSettingToolbar);

        //左侧添加一个默认的返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("游戏设置");

        competitionRulesRadioGroup = (RadioGroup)findViewById(R.id.game_rules);
        gameLevelRadioGroup = (RadioGroup)findViewById(R.id.game_level);
        musicOnOffRadioGroup = (RadioGroup)findViewById(R.id.music_on_off);

        rule1RadioButton = (RadioButton)findViewById(R.id.rule1);
        rule2RadioButton = (RadioButton)findViewById(R.id.rule2);

        level1RadioButton = (RadioButton)findViewById(R.id.level1);
        level2RadioButton = (RadioButton)findViewById(R.id.level2);
        level3RadioButton = (RadioButton)findViewById(R.id.level3);
        level4RadioButton = (RadioButton)findViewById(R.id.level4);

        musicOnRadioButton = (RadioButton)findViewById(R.id.music_on);
        musicOffRadioButton = (RadioButton)findViewById(R.id.music_off);

        musicVolumeSeekBar = (SeekBar)findViewById(R.id.music_volume);
        curVolumeTextView = (TextView)findViewById(R.id.current_volume);
        maxVolumeTextView = (TextView)findViewById(R.id.max_volume);

        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        maxVolumeTextView.setText("最大音量: " + maxVolume);

        //音量控制进度条的最大值设置为系统音量最大值
        musicVolumeSeekBar.setMax(maxVolume);

        //获取当前系统音量
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        //设置音量控制进度条当前值为系统音量当前值
        musicVolumeSeekBar.setProgress(currentVolume);
        curVolumeTextView.setText("当前音量: " + currentVolume);


        //保存界面中的单选按钮被选中的状态
        radioButtonsState = getSharedPreferences("radioButtonsState", MODE_PRIVATE);
        editor = radioButtonsState.edit();

        //设置单选按钮被选中的状态
        if(radioButtonsState.getString("ruleState", "rule1").equals("rule1"))
        {
            rule1RadioButton.setChecked(true);
        }
        else
        {
            rule2RadioButton.setChecked(true);
        }

        switch(radioButtonsState.getString("levelState", "level1"))
        {
            case "level1":
                level1RadioButton.setChecked(true);
                break;
            case "level2":
                level2RadioButton.setChecked(true);
                break;
            case "level3":
                level3RadioButton.setChecked(true);
                break;
            case "level4":
                level4RadioButton.setChecked(true);
                break;
        }


        if(radioButtonsState.getString("musicState", "musicOn").equals("musicOn"))
        {
            musicOnRadioButton.setChecked(true);
        }
        else
        {
            musicOffRadioButton.setChecked(true);
        }


        //绑定监听器

        //点击返回图标返回主菜单
        gameSettingToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //返回主菜单
                finish();
            }
        });


        //游戏规则设置
        competitionRulesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch(i)
                {
                    case R.id.rule1:

                        //保存被选中状态
                        editor.putString("ruleState", "rule1");
                        break;

                    case R.id.rule2:

                        //保存被选中状态
                        editor.putString("ruleState", "rule2");
                        break;
                }

                //提交存入的数据
                editor.commit();
            }
        });

        //游戏难度级别设置
        gameLevelRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch(i)
                {
                    case R.id.level1:

                        //保存被选中状态
                        editor.putString("levelState", "level1");
                        break;

                    case R.id.level2:

                        //保存被选中状态
                        editor.putString("levelState", "level2");
                        break;

                    case R.id.level3:

                        //保存被选中状态
                        editor.putString("levelState", "level3");
                        break;

                    case R.id.level4:

                        //保存被选中状态
                        editor.putString("levelState", "level4");
                        break;
                }

                //提交存入的数据
                editor.commit();
            }
        });


        //背景音乐开关
        musicOnOffRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                Intent gameBGMIntent = new Intent(GameSettingsActivity.this,
                        GameBGMService.class);

                switch(i)
                {
                    case R.id.music_on:

                        //开启背景音乐
                        startService(gameBGMIntent);

                        //保存被选中状态
                        editor.putString("musicState", "musicOn");

                        break;

                    case R.id.music_off:

                        //关闭背景音乐
                        stopService(gameBGMIntent);

                        //保存被选中状态
                        editor.putString("musicState", "musicOff");

                        break;
                }

                //提交存入的数据
                editor.commit();
            }
        });

        //音量设置
        musicVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            //数值的改变
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                //如果是用户拖动
                if(fromUser)
                {
                    int seekPosition = musicVolumeSeekBar.getProgress();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekPosition, 0);
                }

                curVolumeTextView.setText("当前音量: " + progress);

            }

            //开始拖动
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                // TODO Auto-generated method stub
            }

            //停止拖动
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                // TODO Auto-generated method stub
            }
        });

    }


    //当玩家按手机返回键时，返回主菜单
    @Override
    public void onBackPressed()
    {
        //返回主菜单
        finish();
    }
}
