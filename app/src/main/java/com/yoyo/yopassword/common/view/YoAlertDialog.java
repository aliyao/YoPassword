package com.yoyo.yopassword.common.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/2 14:09
 * 修改人：yoyo
 * 修改时间：2016/6/2 14:09
 * 修改备注：
 */
public class YoAlertDialog extends AlertDialog {
    protected YoAlertDialog(Context context) {
        super(context);
    }

    protected YoAlertDialog(Context context, int theme) {
        super(context, theme);
    }

    protected YoAlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

}
