package com.yoyo.yopassword.util;

import android.util.Log;

import com.yoyo.yopassword.YoConfig;

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
        if(YoConfig.isDebug){
            return;
        }
        Log.i(YoConfig.logTag,msg);
    }
    public static void e(String msg){
        if(YoConfig.isDebug){
            return;
        }
        Log.e(YoConfig.logTag,msg);
    }
    public static void d(String msg){
        if(YoConfig.isDebug){
            return;
        }
        Log.d(YoConfig.logTag,msg);
    }

    public static void d(String tag,String msg){
        if(YoConfig.isDebug){
            return;
        }
        Log.d(YoConfig.logTag+tag,msg);
    }
    public static void v(String msg){
        if(YoConfig.isDebug){
            return;
        }
        Log.v(YoConfig.logTag,msg);
    }
}
