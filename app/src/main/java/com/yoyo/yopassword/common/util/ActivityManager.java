package com.yoyo.yopassword.common.util;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/8 10:10
 * 修改人：yoyo
 * 修改时间：2016/6/8 10:10
 * 修改备注：
 */
public class ActivityManager {
    private List<Activity> activityList = new LinkedList<>();
    static ActivityManager instance;

    public static ActivityManager getInstance() {

        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            activityList = null;
            instance = null;
            System.exit(0);
        }
    }

    public void finishOthers() {
        List<Activity> activityListFinish = new LinkedList<>();
        try {
            activityListFinish.addAll(activityList);
            activityListFinish.remove(activityList.size()-1);//去除栈顶
            activityList.removeAll(activityListFinish);//只留栈顶
            for (Activity activity : activityListFinish) {
                if (activity != null) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isToMainActivity() {
       if(activityList!=null&& activityList.size()>1){
           return false;
       }
        return true;
    }

    public boolean isOneActivity() {
        if(activityList == null|| activityList.size()<=1){
            return true;
        }
        return false;
    }
}
