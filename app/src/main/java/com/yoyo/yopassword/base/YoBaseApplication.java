package com.yoyo.yopassword.base;

import android.app.Application;
import android.support.design.BuildConfig;
import org.xutils.x;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/23 15:58
 * 修改人：yoyo
 * 修改时间：2016/5/23 15:58
 * 修改备注：
 */
public class YoBaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        initXutils();
    }

    public void initXutils(){
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
