package ivo_chuanzhi.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ivo_chuanzhi.coolweather.service.AutoUpdateService;

/**
 * Created by chenjiacheng on 2016/5/29.
 */

public class AutoUpdateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
