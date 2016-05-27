package com.yoyo.yopassword.main.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseAppCompatActivity;
import com.yoyo.yopassword.base.BaseUiListener;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.util.YoLogUtils;
import com.yoyo.yopassword.common.view.YoToast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_hello_login)
public class HelloLoginActivity extends BaseAppCompatActivity {
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    @ViewById
    View fullscreen_content;
    @ViewById
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

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
       // initQQAuth();
    }
    @AfterViews
    public void init(){
        super.init();
        initQQAuth();
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
            startActivity(new Intent(HelloLoginActivity.this,MainActivity_.class));
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
