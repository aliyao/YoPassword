package com.yoyo.yopassword.hello.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.util.ACacheUtils;
import com.yoyo.yopassword.common.util.X3DBUtils;
import com.yoyo.yopassword.common.view.YoToast;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;
import com.yoyo.yopassword.hello.entity.LoginAuthSuccessEntity;
import com.yoyo.yopassword.login.LoginApi;
import com.yoyo.yopassword.login.YoPlatformActionListener;
import com.yoyo.yopassword.main.activity.MainActivity;

import java.util.Date;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;

public class HelloLoginActivity extends BaseAppCompatActivity{
    private static final int UI_ANIMATION_DELAY = 300;
    public static final String KEY_TO_LOGIN = "KEY_TO_LOGIN";
    private final Handler mHideHandler = new Handler();
    View fullscreen_content;
    View fullscreen_content_controls,btn_login;

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            fullscreen_content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            fullscreen_content_controls.setVisibility(View.VISIBLE);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            show();
        }
    };

    Handler loginHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    startActivity(new Intent(HelloLoginActivity.this,MainActivity.class));
                    finish();
                    break;
            }
        }
    };

    private void loginQQ() {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(QQ.NAME);
        api.setYoPlatformActionListener(new YoPlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                if(res!=null){
                    Gson gson = new Gson();
                    String entityStr=gson.toJson(res);
                    LoginAuthSuccessEntity loginAuthEntity=gson.fromJson(entityStr,LoginAuthSuccessEntity.class);
                    if(loginAuthEntity!=null){
                        String openid=platform.getDb().getUserId();
                        loginAuthEntity.setOpen_id(openid);
                        YoToast.show(HelloLoginActivity.this,R.string.qq_auth_completel);
                        ACacheUtils.loginIn(HelloLoginActivity.this,loginAuthEntity.getOpen_id());
                        GroupingInfo groupingInfo= X3DBUtils.findItem(GroupingInfo.class,AppConfig.DefaultGroupingId);
                        if(groupingInfo==null|| TextUtils.isEmpty(groupingInfo.getGroupingName())){
                            groupingInfo = new GroupingInfo(HelloLoginActivity.this.getResources().getString(R.string.action_default_grouping_name), new Date().getTime());
                            X3DBUtils.save(groupingInfo);
                        }
                        startActivity(new Intent(HelloLoginActivity.this,MainActivity.class));
                        finish();
                        return;
                    }
                }
                YoToast.show(HelloLoginActivity.this,R.string.qq_auth_fail);
            }
        });
        api.login(this);
    }

    public void init(){
        super.init();
        setContentView(R.layout.activity_hello_login);
        //初始化SDK
        ShareSDK.initSDK(HelloLoginActivity.this);
        fullscreen_content_controls = findViewById(R.id.fullscreen_content_controls);
        btn_login= findViewById(R.id.btn_login);
        fullscreen_content = findViewById(R.id.fullscreen_content);
        initApp();
    }
    private void initApp(){
        if(ACacheUtils.isLogin(HelloLoginActivity.this)){
            btn_login.setVisibility(View.GONE);
            loginHandler.sendEmptyMessageDelayed(1,1000);
        }else {
            btn_login.setVisibility(View.VISIBLE);
        }
    }

    public void onYoClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                loginQQ();
                break;
        }
    }


    /**
     * qq授权登录退出
     */
    public void logoutQQ() {
        try{
            Platform plat = ShareSDK.getPlatform(HelloLoginActivity.this,QQ.NAME);
            if (plat != null && plat.isValid()) {
                plat.removeAccount();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(getIntent().getBooleanExtra(KEY_TO_LOGIN,false)){
            delayedShow(0);
        }else{
            delayedHide(0);
            delayedShow(2000);
        }
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        fullscreen_content_controls.setVisibility(View.GONE);
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        fullscreen_content.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void delayedShow(int delayMillis) {
        mHideHandler.removeCallbacks(mShowRunnable);
        mHideHandler.postDelayed(mShowRunnable, delayMillis);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        logoutQQ();
        super.finish();
    }
}
