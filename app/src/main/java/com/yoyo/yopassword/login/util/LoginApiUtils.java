package com.yoyo.yopassword.login.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.common.config.AppConfig;
import com.yoyo.yopassword.common.view.YoSnackbar;
import com.yoyo.yopassword.common.view.YoToast;
import com.yoyo.yopassword.login.OnLoginListener;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class LoginApiUtils implements Handler.Callback {
    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_ERROR= 2;
    private static final int MSG_AUTH_COMPLETE = 3;

    private OnLoginListener loginListener;
    private String platform;
    private View view;
    private Handler handler;

    public LoginApiUtils() {
        handler = new Handler(Looper.getMainLooper(), this);
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public void setOnLoginListener(OnLoginListener login){
        this.loginListener=login;
    }

    public void login(final Context context,View view) {
        this.view = view;
        if (platform == null) {
            return;
        }

        //初始化SDK
        ShareSDK.initSDK(context);
        Platform plat = ShareSDK.getPlatform(platform);
        if (plat == null) {
            return;
        }

        if (plat.isAuthValid()) {
            plat.removeAccount(true);
            return;
        }

        //使用SSO授权，通过客户单授权
        plat.SSOSetting(AppConfig.Is_SSOSetting);
        plat.setPlatformActionListener(new PlatformActionListener() {
            public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_COMPLETE;
                    msg.arg2 = action;
                    msg.obj =  new Object[] {plat, res};
                    handler.sendMessage(msg);
                }
            }

            public void onError(Platform plat, int action, Throwable t) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_ERROR;
                    msg.arg2 = action;
                    msg.obj = t;
                    handler.sendMessage(msg);
                }
                t.printStackTrace();
            }

            public void onCancel(Platform plat, int action) {
                if (action == Platform.ACTION_USER_INFOR) {
                    Message msg = new Message();
                    msg.what = MSG_AUTH_CANCEL;
                    msg.arg2 = action;
                    msg.obj = plat;
                    handler.sendMessage(msg);
                }
            }
        });
        plat.showUser(null);
    }

    /**处理操作结果*/
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_AUTH_CANCEL: {
                // 取消
                YoSnackbar.showSnackbar(view, R.string.qq_auth_cancel);
            } break;
            case MSG_AUTH_ERROR: {
                // 失败
                Throwable t = (Throwable) msg.obj;
                String text = "caught error: " + t.getMessage();
                YoSnackbar.showSnackbar(view, text);
                t.printStackTrace();
            } break;
            case MSG_AUTH_COMPLETE: {
                // 成功
                Object[] objs = (Object[]) msg.obj;
                Platform plat = (Platform) objs[0];
                @SuppressWarnings("unchecked")
                HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
                if (loginListener!= null && loginListener.onLogin(plat, res)) {
                    loginListener.onLogin(plat,res);
                }
            } break;
        }
        return false;
    }
}
