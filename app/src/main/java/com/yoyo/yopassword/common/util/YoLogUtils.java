package com.yoyo.yopassword.common.util;

import android.util.Log;

import com.yoyo.yopassword.common.config.AppConfig;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/7 17:12
 * 修改人：yoyo
 * 修改时间：2016/5/7 17:12
 * 修改备注：
 */
public class YoLogUtils {
    public static void i(String msg){
        if(!AppConfig.isDebug){
            return;
        }
        Log.i(AppConfig.logTag,msg);
    }
    public static void e(String msg){
        if(!AppConfig.isDebug){
            return;
        }
        Log.e(AppConfig.logTag,msg);
    }
    public static void d(String msg){
        if(!AppConfig.isDebug){
            return;
        }
        Log.d(AppConfig.logTag,msg);
    }

    public static void d(String tag,String msg){
        if(!AppConfig.isDebug){
            return;
        }
        Log.d(AppConfig.logTag+tag,msg);
    }
    public static void v(String msg){
        if(!AppConfig.isDebug){
            return;
        }
        Log.v(AppConfig.logTag,msg);
    }
}
