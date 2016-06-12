package com.yoyo.yopassword.common.util;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.util.entity.ACacheEntity;
import com.yoyo.yopassword.common.util.safe.BPCodeUtil;
import com.yoyo.yopassword.common.util.safe.DesUtils;

import java.io.Serializable;

/**
 * Created by yoyo on 2015/4/22.
 */
public class ACacheUtils {
    static ACache mCacheInstance;
    final static String ACACHE_INFO = "ACACHE_INFO";
    static ACacheEntity mACacheEntityInstance;

    private synchronized static ACache getACacheInstance(Context mContext) {
        if (mCacheInstance == null) {
            mCacheInstance = ACache.get(mContext);
        }
        return mCacheInstance;
    }

    private synchronized static ACacheEntity getACacheEntityInstance(Context mContext) {
        if (mACacheEntityInstance == null) {
            Gson gson = new Gson();
            String acacheInfo = getACacheInstance(mContext).getAsString(ACACHE_INFO);
            if (!TextUtils.isEmpty(acacheInfo)) {
                try {
                    String jsonTextDecrypt = DesUtils.decryptMode(AppConfig.APP_KEY_NUM, AppConfig.APP_KEY,acacheInfo);
                    ACacheEntity mACacheEntity = gson.fromJson(jsonTextDecrypt, ACacheEntity.class);
                    mACacheEntityInstance = mACacheEntity;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (mACacheEntityInstance == null) {
                mACacheEntityInstance = new ACacheEntity();
            }
        }
        return mACacheEntityInstance;
    }

    public static void setACacheEntity(Context mContext, ACacheEntity mACacheEntity) {
        String randomCode= BPCodeUtil.getInstance().createCode();
        mACacheEntity.setRandomCode(randomCode);
        Gson gson = new Gson();
        String jsonText = gson.toJson(mACacheEntity);
        try {
            String jsonTextEncrypt = DesUtils.encryptMode(AppConfig.APP_KEY_NUM, AppConfig.APP_KEY,jsonText);
            put(mContext, ACACHE_INFO, jsonTextEncrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setCheckPassword(Context mContext, String checkPassword) {
        ACacheEntity mACacheEntity = getACacheEntityInstance(mContext);
        mACacheEntity.setCheckPassword(checkPassword);
        setACacheEntity(mContext, mACacheEntity);
    }

    public static String getCheckPassword(Context mContext) {
        ACacheEntity mACacheEntity = getACacheEntityInstance(mContext);
        Object mCheckPassword = mACacheEntity.getCheckPassword();
        return mCheckPassword == null ? null : mCheckPassword.toString();
    }

    public static boolean isLogin(Context mContext) {
        ACacheEntity mACacheEntity = getACacheEntityInstance(mContext);
        Object mCheckPassword = mACacheEntity.getCheckPassword();
        Object mLoginStatus = mACacheEntity.getLoginStatus();
        return (mLoginStatus != null && mLoginStatus.equals(1)) && mCheckPassword != null && !TextUtils.isEmpty(mCheckPassword.toString());
    }

    public static void loginIn(Context mContext, String openId) {
        ACacheEntity mACacheEntity = getACacheEntityInstance(mContext);
        mACacheEntity.setLoginStatus(1);
        mACacheEntity.setOpenId(openId);
        setACacheEntity(mContext, mACacheEntity);
    }

    public static void signOut(Context mContext) {
        ACacheEntity mACacheEntity = getACacheEntityInstance(mContext);
        mACacheEntity.setLoginStatus(0);
        setACacheEntity(mContext, mACacheEntity);
    }

  /*  public static Object getAsObject(Context mContext,String key){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        return mCache.getAsObject(key);
    }*/

    public static String getOpenId(Context mContext) {
        ACacheEntity mACacheEntity = getACacheEntityInstance(mContext);
        return mACacheEntity.getOpenId();
    }

    /*  public static Object getAsObjectDefault(Context mContext,String key,Object defaultObj){
          if(mCache==null){
              mCache = ACache.get(mContext);
          }
          if(mCache.getAsObject(key)==null){
              return defaultObj;
          }
          return mCache.getAsObject(key);
      }*/
    public static void put(Context mContext, String key, Serializable value) {
        getACacheInstance(mContext).put(key, value);
    }
   /* public static void putTimeS(Context mContext,String key, Serializable value,int s){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        mCache.put(key, value,s);
    }
    public static void putTimeDay(Context mContext,String key, Serializable value,int day){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        mCache.put(key, value,day* ACache.TIME_DAY);
    }
    public static void putTime3Day(Context mContext,String key, Serializable value){
        int Day=3;
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        mCache.put(key, value,Day* ACache.TIME_DAY);
    }*/
}