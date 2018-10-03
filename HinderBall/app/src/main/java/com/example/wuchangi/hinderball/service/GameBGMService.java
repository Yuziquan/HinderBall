package com.example.wuchangi.hinderball.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import com.example.wuchangi.hinderball.R;

/**
 * Created by WuchangI on 2018/6/17.
 */

//一个管理背景音乐播放器的后台Service
public class GameBGMService extends Service
{
    //背景音乐播放器
    private MediaPlayer BGMMediaPlayer = null;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }


    @Override
    public void onCreate()
    {
        super.onCreate();

        //播放背景音乐
        startBGM();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        //关闭背景音乐
        stopBGM();
    }

    //开启背景音乐并循环播放
    public void startBGM()
    {
        if(BGMMediaPlayer == null)
        {
            //加载应用的背景音乐资源文件
            BGMMediaPlayer = MediaPlayer.create(this, R.raw.trip);
            BGMMediaPlayer.start();

            //设置背景音乐播放结束监听器
            BGMMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer)
                {
                    BGMMediaPlayer.start();
                    //设置循环播放
                    BGMMediaPlayer.setLooping(true);
                }
            });
        }
    }

    //关闭背景音乐
    public void stopBGM()
    {
        if (BGMMediaPlayer != null && BGMMediaPlayer.isPlaying())
        {
            //停止播放器
            BGMMediaPlayer.stop();

            //主动释放资源
            BGMMediaPlayer.release();

            BGMMediaPlayer = null;
        }
    }

}
