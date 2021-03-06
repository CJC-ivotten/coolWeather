package ivo_chuanzhi.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ivo_chuanzhi.coolweather.utils.LogUtil;

/**
 * Created by chenjiacheng on 2016/5/28.
 */

public class CoolweatherOpenHelper extends SQLiteOpenHelper{

    /**
     * Provicne表的建表语句
     */
    public static final String CREATE_PROVINCE = "create table Province ("
            + "id integer primary key autoincrement, "
            + "province_name text, "
            + "province_code text)";

    /**
     * City表的建表语句
     */
    public static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "city_name text, "
            + "city_code text, "
            + "province_id integer)";

    /**
     * County表的建表语句
     */
    public static final String CREATE_COUNTY = "create table County ("
            + "id integer primary key autoincrement, "
            + "county_name text, "
            + "county_code text, "
            + "city_id integer)";



    public CoolweatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE); // 省
        LogUtil.d("coolweather","创建ProVince表成功");

        db.execSQL(CREATE_CITY); // 市
        LogUtil.d("coolweather","创建City表成功");

        db.execSQL(CREATE_COUNTY); // 县
        LogUtil.d("coolweather","创建County表成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
