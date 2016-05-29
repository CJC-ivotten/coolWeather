package ivo_chuanzhi.coolweather.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by chenjiacheng on 2016/5/29.
 */

public class MyApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}
