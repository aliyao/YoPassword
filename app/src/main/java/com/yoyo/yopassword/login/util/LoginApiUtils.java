package com.yoyo.yopassword.login.util;

import android.content.Context;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.common.view.YoToast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class LoginApiUtils {
    private String platform;
    YoPlatformActionListener yoPlatformActionListener;

    public LoginApiUtils() {
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void login(final Context context) {
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
        plat.SSOSetting(true);
        plat.setPlatformActionListener(new PlatformActionListener() {
            public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
                if (action == Platform.ACTION_USER_INFOR) {

                    if(yoPlatformActionListener!=null){
                        yoPlatformActionListener.onComplete(plat,action,res);
                    }
                }
            }

            public void onError(Platform plat, int action, Throwable t) {
                if (action == Platform.ACTION_USER_INFOR) {
                    // 失败
                    String text = "caught error: " + t.getMessage();
                    YoToast.show(context, text);
                    t.printStackTrace();
                }
                t.printStackTrace();
            }

            public void onCancel(Platform plat, int action) {
                if (action == Platform.ACTION_USER_INFOR) {
                    // 取消
                    YoToast.show(context, R.string.qq_auth_cancel);
                }
            }
        });
        plat.showUser(null);
    }


    public void setYoPlatformActionListener(YoPlatformActionListener yoPlatformActionListener) {
        this.yoPlatformActionListener = yoPlatformActionListener;
    }
}
