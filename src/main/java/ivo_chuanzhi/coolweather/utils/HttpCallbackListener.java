package ivo_chuanzhi.coolweather.utils;

/**
 * Created by chenjiacheng on 2016/5/28.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
