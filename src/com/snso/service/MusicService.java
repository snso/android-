package com.snso.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.snso.heart.R;

public class MusicService extends Service {  
    //Ϊ��־�������ñ�ǩ  
    private static String TAG = "MusicService";  
    //�������ֲ���������  
    private MediaPlayer mPlayer;  
      
    //�÷��񲻴�����Ҫ������ʱ�����ã�����startService()����bindService()��������ʱ���ø÷���  
    @Override  
    public void onCreate() {  
        Log.e(TAG, "MusicSerice onCreate()");  
          
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.happy);  
        //���ÿ����ظ�����  
        mPlayer.setLooping(false);  
        super.onCreate();  
    }  
      
    @Override  
    public void onStart(Intent intent, int startId) {  
        Log.e(TAG, "MusicSerice onStart()");  
        mPlayer.start();  
    }  
    @Override  
    public void onDestroy() {  
        Log.e(TAG, "MusicSerice onDestroy()");  
          
        mPlayer.stop();  
          
        super.onDestroy();  
    }  
    //��������ͨ��bindService ����֪ͨ��Serviceʱ�÷���������  
    @Override  
    public IBinder onBind(Intent intent) {  
        Log.e(TAG, "MusicSerice onBind()");  
        mPlayer.start();  
        return null;  
    }  
    //��������ͨ��unbindService����֪ͨ��Serviceʱ�÷���������  
    @Override  
    public boolean onUnbind(Intent intent) {  
        Log.e(TAG, "MusicSerice onUnbind()");  
        mPlayer.stop();  
        return super.onUnbind(intent);  
    }  
      
}  
