package com.yoyo.yopassword.common.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by yoyo on 2015/5/19.
 */
public class AppInfoUtil {
    public static final String BaiduDiskPackageName = "com.baidu.netdisk";
    public static final String XunleiPackageName = "com.xunlei.downloadprovider";
    public static final String UCMobilePackageName = "com.UCMobile";

    public static String getVersionName(Context mContext) {
        PackageManager manager;

        PackageInfo info = null;

        manager = mContext.getPackageManager();

        try {

            info = manager.getPackageInfo(mContext.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;

        }
        return info.versionName;
       /* info.versionCode;

        info.versionName;

        info.packageName;

        info.signatures;*/
    }

    public static int getVersionCode(Context mContext) {
        PackageManager manager;

        PackageInfo info = null;

        manager = mContext.getPackageManager();

        try {

            info = manager.getPackageInfo(mContext.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;

        }
        return info.versionCode;
       /* info.versionCode;

        info.versionName;

        info.packageName;

        info.signatures;*/
    }

    public static boolean isRunningApp(Context context,String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
           // Log.e("appProcess.processName",appProcess.processName);
            if (appProcess.processName.equals(packageName)) {
                return true;
               // return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            }
        }
        return false;

    }
}
