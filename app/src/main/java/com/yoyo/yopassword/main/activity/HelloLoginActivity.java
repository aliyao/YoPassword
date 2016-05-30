package com.yoyo.yopassword.main.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;

import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.base.BaseUiListener;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.util.X3DBUtils;
import com.yoyo.yopassword.common.util.YoLogUtils;
import com.yoyo.yopassword.common.view.YoToast;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;

import java.util.Date;

public class HelloLoginActivity extends BaseAppCompatActivity {
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    View fullscreen_content;
    View fullscreen_content_controls;


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

    //qq授权登录
    private BaseUiListener baseUiListener;
    private Tencent mTencent;

    public void init(){
        super.init();
        setContentView(R.layout.activity_hello_login);
        fullscreen_content_controls = findViewById(R.id.fullscreen_content_controls);
        fullscreen_content = findViewById(R.id.fullscreen_content);
        initApp();
        initQQAuth();
    }
    private void initApp(){
        GroupingInfo groupingInfo= X3DBUtils.findItem(GroupingInfo.class,AppConfig.DefaultGroupingId);
        if(groupingInfo==null|| TextUtils.isEmpty(groupingInfo.getGroupingName())){
            groupingInfo = new GroupingInfo(HelloLoginActivity.this.getResources().getString(R.string.action_default_grouping_name), new Date().getTime());
            X3DBUtils.save(groupingInfo);
        }
    }

    /**
     * qq授权初始化
     */
    private void initQQAuth() {
        mTencent = Tencent.createInstance(AppConfig.KEY_APP_ID, this.getApplicationContext());
        baseUiListener = new BaseUiListener() {
            @Override
            protected void doComplete(Object o) {
                YoLogUtils.i(o.toString());
                YoToast.show(HelloLoginActivity.this,R.string.qq_auth_completel);
                startActivity(new Intent(HelloLoginActivity.this,MainActivity.class));
            }

            @Override
            protected void doError(UiError e) {
                YoToast.show(HelloLoginActivity.this,e.errorMessage);
            }

            @Override
            protected void doCancel() {
                YoToast.show(HelloLoginActivity.this,R.string.qq_auth_cancel);
            }
        };
    }

    public void onYoClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                loginQQ();
                break;
        }
    }

    /**
     * qq授权登录
     */
    public void loginQQ() {
        if (!mTencent.isSessionValid()) {
            startActivity(new Intent(HelloLoginActivity.this,MainActivity.class));
            finish();
            //mTencent.login(this, YoConfig.KEY_SCOPE, baseUiListener);
        }
    }

    /**
     * qq授权登录退出
     */
    public void logoutQQ() {
        mTencent.logout(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(0);
        delayedShow(2000);
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
        Tencent.onActivityResultData(requestCode, resultCode, data, baseUiListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logoutQQ();
    }
}
