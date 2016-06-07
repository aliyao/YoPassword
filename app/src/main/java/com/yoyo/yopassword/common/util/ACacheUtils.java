package com.yoyo.yopassword.common.util;


import android.content.Context;
import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by yoyo on 2015/4/22.
 */
public class ACacheUtils {
    static ACache mCache;
    final  static String OpenId="OpenId";//userid
    final  static String LoginStatus="LoginStatus";//1登录
    final  static String FirstOpen="FirstOpen";//1第一次打开
    final  static String CheckPassword="CheckPassword";

    public static boolean isFirstOpen(Context mContext){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        Object mFirstOpen=mCache.getAsObject(FirstOpen);
        return !(mFirstOpen!=null&&mFirstOpen.equals(1));
    }
    public static void setFirstOpen1(Context mContext){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        Object mObject=null;
        mCache.put(FirstOpen,1);

    }

    public static void setCheckPassword(Context mContext,String checkPassword){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        putTime3Day( mContext, CheckPassword,checkPassword);
    }

    public static String getCheckPassword(Context mContext){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        Object mCheckPassword=mCache.getAsObject(CheckPassword);
        return mCheckPassword==null?null:mCheckPassword.toString();
    }
    public static boolean isLogin(Context mContext){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        Object mCheckPassword=mCache.getAsObject(CheckPassword);
        Object mLoginStatus=mCache.getAsObject(LoginStatus);
        return (mLoginStatus!=null&&mLoginStatus.equals(1))&& mCheckPassword!=null&&!TextUtils.isEmpty(mCheckPassword.toString());
    }
    /* public static void loginOut(Context mContext){
         if(mCache==null){
             mCache = ACache.get(mContext);
         }
         put(mContext,LoginStatus,0);
         Tencent.createInstance(GlobalKey.APP_ID, mContext.getApplicationContext()).logout(mContext);
     }*/
    public static void loginIn(Context mContext,String openId){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        putTime3Day( mContext, LoginStatus,1);
        putTime3Day(mContext,OpenId,openId);
    }
    public static void signOut(Context mContext){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        put( mContext, LoginStatus,0);
        //put(mContext,OpenId,"");
       // put(mContext,CheckPassword,"");
    }

    public static Object getAsObject(Context mContext,String key){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        return mCache.getAsObject(key);
    }

    public static String getOpenId(Context mContext){
        return getAsObject(mContext,OpenId)+"";
    }
    public static Object getAsObjectDefault(Context mContext,String key,Object defaultObj){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        if(mCache.getAsObject(key)==null){
            return defaultObj;
        }
        return mCache.getAsObject(key);
    }
    public static void put(Context mContext,String key, Serializable value){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        mCache.put(key, value);
    }
    public static void putTimeS(Context mContext,String key, Serializable value,int s){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        mCache.put(key, value,s);
      /*  mCache.put("test_key2", "test value", s);//保存10秒，如果超过10秒去获取这个key，将为null
        mCache.put("test_key3", "test value", 2 * ACache.TIME_DAY);//保存两天，如果超过两天去获取这个key，将为null*/
    }
    public static void putTimeDay(Context mContext,String key, Serializable value,int day){
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        mCache.put(key, value,day* ACache.TIME_DAY);
      /*  mCache.put("test_key2", "test value", s);//保存10秒，如果超过10秒去获取这个key，将为null
        mCache.put("test_key3", "test value", 2 * ACache.TIME_DAY);//保存两天，如果超过两天去获取这个key，将为null*/
    }
    public static void putTime3Day(Context mContext,String key, Serializable value){
        int Day=3;
        if(mCache==null){
            mCache = ACache.get(mContext);
        }
        mCache.put(key, value,Day* ACache.TIME_DAY);
      /*  mCache.put("test_key2", "test value", s);//保存10秒，如果超过10秒去获取这个key，将为null
        mCache.put("test_key3", "test value", 2 * ACache.TIME_DAY);//保存两天，如果超过两天去获取这个key，将为null*/
    }

}
