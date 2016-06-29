package com.yoyo.yopassword.common.util;

import android.content.Context;

import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;

/**
 * 项目名称：YoAppManage
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/28 12:20
 * 修改人：yoyo
 * 修改时间：2016/6/28 12:20
 * 修改备注：
 */
public class BmobUtils {
    public static void callEndpoint(Context context,String cloudcodeName,MyCloudCodeListener myCloudCodeListener){
        callEndpoint( context, cloudcodeName,null, myCloudCodeListener);
    }
    public static void callEndpoint(Context context,String cloudcodeName, JSONObject params,MyCloudCodeListener myCloudCodeListener){
        AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
        if(params==null){
            ace.callEndpoint(context,cloudcodeName,myCloudCodeListener);
        }else{
            ace.callEndpoint(context,cloudcodeName, params, myCloudCodeListener);
        }
    }
}
