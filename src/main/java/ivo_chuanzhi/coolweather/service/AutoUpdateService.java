package ivo_chuanzhi.coolweather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
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



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Notification notification = new Notification(R.drawable.karen,"Karen’s home weather",System.currentTimeMillis());
   /*     NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getContext());
        builder.setSmallIcon(R.drawable.karen);
        Notification notification = builder.build();
        //自定义通知布局
        RemoteViews contentView = new RemoteViews(MyApplication.getContext().getPackageName(),R.layout.notification_layout);
        contentView.setImageViewResource(R.id.notification_image,R.drawable.karen);
        contentView.setTextViewText(R.id.notification_temp1,"22度");
        contentView.setTextViewText(R.id.notification_temp2,"32度");
        contentView.setTextViewText(R.id.notification_publish_text,"今天8:00发布");
        notification.contentView = contentView;


        Intent notificationIntent = new Intent(this, WeatherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        startForeground(1,notification);*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
                System.out.println("后台更新天气！！！");
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 4 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
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
