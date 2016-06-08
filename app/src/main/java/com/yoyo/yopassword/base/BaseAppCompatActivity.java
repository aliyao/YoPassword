package com.yoyo.yopassword.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yoyo.yopassword.common.util.ActivityManager;

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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //if(!isNoCheck){
           // startActivity(new Intent(BaseAppCompatActivity.this, CheckPasswordActivity.class));
       // }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }
}
