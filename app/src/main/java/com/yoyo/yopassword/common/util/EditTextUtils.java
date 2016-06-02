package com.yoyo.yopassword.common.util;

import android.widget.EditText;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/2 12:38
 * 修改人：yoyo
 * 修改时间：2016/6/2 12:38
 * 修改备注：
 */
public class EditTextUtils {
    public static void requestFocus(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
    }
}
