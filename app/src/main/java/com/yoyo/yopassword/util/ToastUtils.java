package com.yoyo.yopassword.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/7 18:01
 * 修改人：yoyo
 * 修改时间：2016/5/7 18:01
 * 修改备注：
 */
public class ToastUtils {
    public static void show(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
