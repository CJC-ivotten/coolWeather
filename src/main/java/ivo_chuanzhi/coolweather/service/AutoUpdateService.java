package ivo_chuanzhi.coolweather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;

import ivo_chuanzhi.coolweather.R;
import ivo_chuanzhi.coolweather.activity.WeatherActivity;
import ivo_chuanzhi.coolweather.receiver.AutoUpdateReceiver;
import ivo_chuanzhi.coolweather.utils.HttpCallbackListener;
import ivo_chuanzhi.coolweather.utils.HttpUtil;
import ivo_chuanzhi.coolweather.utils.MyApplication;
import ivo_chuanzhi.coolweather.utils.Utility;

/**
 * Created by chenjiacheng on 2016/5/29.
 */

public class AutoUpdateService extends Service{

    Notification notification;
    RemoteViews contentView;
    NotificationCompat.Builder builder;
    PendingIntent pendingIntent;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        builder = new NotificationCompat.Builder(MyApplication.getContext());

        //自定义通知布局  这里并没有使用自定义的通知布局，没有用到contentView
        contentView = new RemoteViews(MyApplication.getContext().getPackageName(),R.layout.notification_layout);
       // contentView.setImageViewResource(R.id.notification_image,R.drawable.karen);

        contentView.setTextViewText(R.id.notification_text,"河源 今天 晴 22度~33度");


        Intent notificationIntent = new Intent(this, WeatherActivity.class);
        pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        notification = builder
                .setContentText("河源 今天 晴 22度~33度")
              //  .setContent(contentView)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.karen)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.karen))
                .build();

      //  manager.notify(1, notification);
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
                System.out.println("后台更新天气！！！");
                try {
                    //做一个延时
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("更新前台");
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
                String cityName = preferences.getString("city_name","");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("今天"); stringBuilder.append(" ");
                stringBuilder.append(preferences.getString("weather_desp","")); stringBuilder.append(" ");
                stringBuilder.append(preferences.getString("temp2","")); stringBuilder.append(" ");
                stringBuilder.append("~");
                stringBuilder.append(preferences.getString("temp1","")); stringBuilder.append(" ");



             //   contentView.setTextViewText(R.id.notification_text,stringBuilder.toString());
                notification = builder
                        .setContentTitle(cityName)
                        .setContentText(stringBuilder.toString())
                    //    .setContent(contentView)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .setTicker("老家的天气")
                        .setSmallIcon(R.drawable.karen)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.karen))
                        .build();
                startForeground(1, notification);
                System.out.println(stringBuilder.toString());
            }
        }).start();



        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 4 * 60 * 60 * 1000;
//        int anHour = 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        stopForeground(false);
        super.onDestroy();
    }

    /**
     * 更新天气信息
     */
    private void updateWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode = preferences.getString("weather_code","");
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(AutoUpdateService.this,response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }
}
