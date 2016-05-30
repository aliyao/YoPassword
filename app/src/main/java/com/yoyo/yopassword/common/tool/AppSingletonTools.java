package com.yoyo.yopassword.common.tool;

import com.yoyo.yopassword.main.activity.MainActivity;

/**
 * Created by nidey on 2016/5/30.
 */
public class AppSingletonTools {
    private AppSingletonTools() {}
    private static AppSingletonTools single=null;
    //静态工厂方法
    public static synchronized AppSingletonTools getInstance() {
        if (single == null) {
            single = new AppSingletonTools();
        }
        return single;
    }

    private static MainActivity instanceMainActivity;

    public void initMainActivity(MainActivity instanceMainActivity){
        this.instanceMainActivity=instanceMainActivity;
    }

    public void destroyMainActivity(){
        this.instanceMainActivity=null;
    }

    public static void destroy(){
        instanceMainActivity=null;
        single=null;
    }

    public void refreshGrouping(){
        if(instanceMainActivity!=null){
            instanceMainActivity.mSectionsPagerAdapter.refreshData(true);
        }
    }

    public void refreshPassword(){
        if(instanceMainActivity!=null){
            instanceMainActivity.mSectionsPagerAdapter.refreshData(true);
        }
    }
}
