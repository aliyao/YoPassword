package com.yoyo.yopassword.common.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.yoyo.yopassword.grouping.activity.GroupingActivity;
import com.yoyo.yopassword.password.activity.AddPasswordActivity;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/3 14:43
 * 修改人：yoyo
 * 修改时间：2016/6/3 14:43
 * 修改备注：
 */
public class YoStartActivityTools {
    public static final String ToGroupingActivity_IsSelect = "ToGroupingActivity_IsSelect";

    public static final String ToAddPasswordActivity_IsUpdate = "ToAddPasswordActivity_IsUpdate";
    public static final String ToAddPasswordActivity_PasswordInfoId = "ToAddPasswordActivity_PasswordInfoId";

    public static void toAddPasswordActivity_Add(Context context) {
        context.startActivity(new Intent(context, AddPasswordActivity.class));
    }
    public static void toAddPasswordActivity_Update(Context context, long passwordInfoId) {
        context.startActivity(new Intent(context, AddPasswordActivity.class).putExtra(ToAddPasswordActivity_PasswordInfoId, passwordInfoId).putExtra(ToAddPasswordActivity_IsUpdate, true));
    }
    public static void toGroupingActivity(Context context){
        context.startActivity(new Intent(context, GroupingActivity.class));
    }
    public static void toGroupingActivity_Select(Context context){
        context.startActivity(new Intent(context, GroupingActivity.class).putExtra(ToGroupingActivity_IsSelect,true));
    }

}
