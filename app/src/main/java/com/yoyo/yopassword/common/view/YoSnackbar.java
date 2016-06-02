package com.yoyo.yopassword.common.view;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/26 9:50
 * 修改人：yoyo
 * 修改时间：2016/5/26 9:50
 * 修改备注：
 */
public class YoSnackbar {
    public static void showSnackbar(View view,int rStr){
        Snackbar.make(view, rStr, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static void showSnackbar(View view,String text){
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
