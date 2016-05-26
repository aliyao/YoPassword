package com.yoyo.yopassword.common.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.yoyo.yopassword.R;

/**
 * 项目名称：YoPassword
 * 类描述：AlertDialog
 * 创建人：yoyo
 * 创建时间：2016/5/26 9:57
 * 修改人：yoyo
 * 修改时间：2016/5/26 9:57
 * 修改备注：
 */
public class YoAlertDialog {
    public final static int  PositiveButtonOnClick=-1;
    public final static int  NegativeButtonOnClick=-2;
    public final static int  NeutralButtonOnClick=-3;
    public static void showAlertDialogList(Context context, String[] toDo,final OnToDoItemClickListener onToDoItemClickListener){
        if(toDo==null||toDo.length==0){
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(toDo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(onToDoItemClickListener==null){
                    return;
                }
                onToDoItemClickListener.onItemClick(dialog, which);
            }
        });
        builder.show();
    }
    public static void showAlertDialog(Context context,int rStrMessage, OnToDoItemClickListener onToDoItemClickListener){
        showAlertDialog(context,0,rStrMessage,R.string.btn_ok, R.string.btn_cancle, 0,onToDoItemClickListener);
    }

    public static void showAlertDialog(Context context,int rStrTitle, int rStrMessage, int rStrPositiveButtonText,int rStrNegativeButtonText,int rStrNeutralButtonText,final OnToDoItemClickListener onToDoItemClickListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(rStrTitle>0){
            builder.setTitle(rStrTitle);
        }
        builder.setMessage(rStrMessage);
        if(rStrPositiveButtonText>0) {
            builder.setPositiveButton(rStrPositiveButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(onToDoItemClickListener==null){
                        return;
                    }
                    onToDoItemClickListener.onItemClick(dialog, PositiveButtonOnClick);
                    onToDoItemClickListener.onPositiveClick(dialog, PositiveButtonOnClick);
                }
            });
        }
        if(rStrNegativeButtonText>0){
            builder.setNegativeButton(rStrNegativeButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(onToDoItemClickListener==null){
                        return;
                    }
                    onToDoItemClickListener.onItemClick(dialog, NegativeButtonOnClick);
                }
            });
        }
        if(rStrNeutralButtonText>0) {
            builder.setNeutralButton(rStrNeutralButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(onToDoItemClickListener==null){
                        return;
                    }
                    onToDoItemClickListener.onItemClick(dialog, NeutralButtonOnClick);
                }
            });
        }
        builder.show();
    }
}
