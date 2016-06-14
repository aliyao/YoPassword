package com.yoyo.yopassword.common.util;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.util.entity.ACacheEntity;
import com.yoyo.yopassword.common.util.safe.AESUtils;
import com.yoyo.yopassword.common.util.safe.BPCodeUtil;
import com.yoyo.yopassword.common.util.safe.MD5Util;

/**
 * Created by yoyo on 2015/4/22.
 */
public class ACacheUtils {
    static ACache mCacheInstance;
    final static String ACACHE_INFO_USER = "ACACHE_INFO_USER";
    //final static String ACACHE_INFO = "ACACHE_INFO";
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
            String acacheInfoName= getAcacheInfoName(mContext);
            if(!TextUtils.isEmpty(acacheInfoName)){
                String acacheInfo = getACacheInstance(mContext).getAsString(acacheInfoName);
                if (!TextUtils.isEmpty(acacheInfo)) {
                    try {
                        String jsonTextDecrypt = AESUtils.decrypt(acacheInfo, AppConfig.APP_AES_KEY);
                        ACacheEntity mACacheEntity = gson.fromJson(jsonTextDecrypt, ACacheEntity.class);
                        mACacheEntityInstance = mACacheEntity;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (mACacheEntityInstance == null) {
                mACacheEntityInstance = new ACacheEntity();
            }
        }
        return mACacheEntityInstance;
    }

    private static void setACacheEntity(Context mContext, ACacheEntity mACacheEntity) {
        String randomCode= BPCodeUtil.getInstance().createCode();
        mACacheEntity.setRandomCode(randomCode);
        Gson gson = new Gson();
        String jsonText = gson.toJson(mACacheEntity);
        try {
            String acacheInfoName= getAcacheInfoName(mContext);
            if(TextUtils.isEmpty(acacheInfoName)){
               return;
            }
            String jsonTextEncrypt = AESUtils.encrypt(jsonText, AppConfig.APP_AES_KEY);
            put(mContext, acacheInfoName, jsonTextEncrypt);
            mACacheEntityInstance=null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getAcacheInfoName(Context mContext){
        String acacheInfoName= getACacheInstance(mContext).getAsString(ACACHE_INFO_USER);
        acacheInfoName= MD5Util.MD5ExamNum(acacheInfoName,AppConfig.APP_MD5_USER_ID_KEY);
        return acacheInfoName;
    }

    private static void setAcacheInfoName(Context mContext,String encryptOpenId){
        getACacheInstance(mContext).put(ACACHE_INFO_USER,encryptOpenId);
    }

    private static String getEncryptOpenId(String openId){
        String encryptOpenId= MD5Util.MD5ExamNum(openId,AppConfig.APP_MD5_USER_INFO_KEY);
        return encryptOpenId;
    }
    public static void setCheckPassword(Context mContext, String checkPassword) {
        ACacheEntity mACacheEntity = getACacheEntityInstance(mContext);
        mACacheEntity.setCheckPassword(checkPassword);
        setACacheEntity(mContext, mACacheEntity);
    }

    public static String getCheckPassword(Context mContext) {
        ACacheEntity mACacheEntity = getACacheEntityInstance(mContext);
        String mCheckPassword = mACacheEntity.getCheckPassword();
        return mCheckPassword;
    }

    public static boolean isLogin(Context mContext) {
        ACacheEntity mACacheEntity = getACacheEntityInstance(mContext);
        String mCheckPassword = mACacheEntity.getCheckPassword();
        int mLoginStatus = mACacheEntity.getLoginStatus();
        return  mLoginStatus==1 && !TextUtils.isEmpty(mCheckPassword);
    }

    public static void loginIn(Context mContext, String openId) {
        openId= getEncryptOpenId(openId);
        setAcacheInfoName(mContext,openId);
        ACacheEntity mACacheEntity = getACacheEntityInstance(mContext);
        String openIdMD5=mACacheEntity.getOpenId();
        if(TextUtils.isEmpty(openIdMD5)|| !(openIdMD5.equals(openId))){
            mACacheEntity=new ACacheEntity();
            mACacheEntity.setOpenId(openId);
        }
        mACacheEntity.setLoginStatus(1);
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
    public static void put(Context mContext, String key, String value) {
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