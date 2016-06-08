package com.yoyo.yopassword.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yoyo.yopassword.check.CheckPasswordActivity;
import com.yoyo.yopassword.common.util.ActivityManager;

import java.util.List;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/23 16:16
 * 修改人：yoyo
 * 修改时间：2016/5/23 16:16
 * 修改备注：
 */
public class BaseAppCompatActivity extends AppCompatActivity {
    boolean  isNoCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        init();
    }

    public void init() {
    }

    public void setNoCheck(boolean noCheck) {
        isNoCheck = noCheck;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
         if(!isNoCheck&& !isAppRunningForeground(BaseAppCompatActivity.this) && !ActivityManager.getInstance().isOneActivity()){
            startActivity(new Intent(BaseAppCompatActivity.this, CheckPasswordActivity.class));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }
    public static boolean isAppRunningForeground(Context var0) {
        android.app.ActivityManager var1 = (android.app.ActivityManager)var0.getSystemService(Context.ACTIVITY_SERVICE);
        List var2 = var1.getRunningTasks(1);
        return var0.getPackageName().equalsIgnoreCase(((android.app.ActivityManager.RunningTaskInfo)var2.get(0)).baseActivity.getPackageName());
    }
}
