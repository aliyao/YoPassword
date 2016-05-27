package com.yoyo.yopassword.common.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.yoyo.yopassword.grouping.activity.GroupingActivity;
import com.yoyo.yopassword.password.activity.AddPasswordActivity;

/**
 * Created by nidey on 2016/5/27.
 */
public class StartActivityTools {
    public static final String ToGroupingActivity_IsSelect="ToGroupingActivity_IsSelect";
    public static final String ToGroupingActivity_GroupingId="ToGroupingActivity_GroupingId";

    public static final String ToAddPasswordActivity_IsUpdate="ToAddPasswordActivity_IsUpdate";
    public static final String ToAddPasswordActivity_PasswordInfoId="ToAddPasswordActivity_PasswordInfoId";

    public static final int ToAddPasswordActivity_RequestCode=10;
    public static final int ToAddPasswordActivity_ResultCode=10000;

    public static final int ToGroupingActivity_RequestCode=11;
    public static final int ToGroupingActivity_ResultCode=10001;

    public static void toGroupingActivity(Activity activity, boolean isSelect,boolean isForResult){
        if(isForResult){
            activity.startActivityForResult(new Intent(activity, GroupingActivity.class).putExtra(ToGroupingActivity_IsSelect,isSelect),ToGroupingActivity_RequestCode);
        }else {
            activity.startActivity(new Intent(activity, GroupingActivity.class).putExtra(ToGroupingActivity_IsSelect,isSelect));
        }
    }
    public static void doGroupingActivitySetResult(Activity activity,long groupingId){
        activity.setResult(ToGroupingActivity_ResultCode,new Intent().putExtra(ToGroupingActivity_GroupingId,groupingId));
    }

    public static void doAddPasswordActivitySetResult(Activity activity,long passwordInfoId){
        activity.setResult(ToAddPasswordActivity_ResultCode,new Intent().putExtra(ToAddPasswordActivity_PasswordInfoId,passwordInfoId));
    }

    public static void toAddPasswordActivity(Object object, boolean isUpdate, boolean isForResult,long passwordInfoId){
        if(isForResult){
            if(object instanceof Activity){
                Activity activity=((Activity)object);
                activity.startActivityForResult(new Intent(activity, AddPasswordActivity.class).putExtra(ToAddPasswordActivity_IsUpdate,isUpdate).putExtra(ToAddPasswordActivity_PasswordInfoId,passwordInfoId),ToAddPasswordActivity_RequestCode);
            }else if(object instanceof Fragment){
                Fragment mFragment=((Fragment)object);
                mFragment.startActivityForResult(new Intent(mFragment.getContext(), AddPasswordActivity.class).putExtra(ToAddPasswordActivity_IsUpdate,isUpdate).putExtra(ToAddPasswordActivity_PasswordInfoId,passwordInfoId),ToAddPasswordActivity_RequestCode);
            }
        }else {
            if(object instanceof Context){
                Context context=((Context)object);
                context.startActivity(new Intent(context, AddPasswordActivity.class).putExtra(ToAddPasswordActivity_IsUpdate,isUpdate).putExtra(ToAddPasswordActivity_PasswordInfoId,passwordInfoId));
            }

        }
    }


}
