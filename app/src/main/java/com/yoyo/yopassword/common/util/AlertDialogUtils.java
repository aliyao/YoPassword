package com.yoyo.yopassword.common.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.common.view.OnToDoItemClickListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 项目名称：YoPassword
 * 类描述：AlertDialog
 * 创建人：yoyo
 * 创建时间：2016/5/26 9:57
 * 修改人：yoyo
 * 修改时间：2016/5/26 9:57
 * 修改备注：
 */
public class AlertDialogUtils {
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
        showAlertDialog(context,0,rStrMessage,R.string.btn_ok, R.string.btn_cancle, 0,null,false,onToDoItemClickListener);
    }

    public static AlertDialog showAlertDialog(final Context context, int rStrTitle, int rStrMessage, int rStrPositiveButtonText, int rStrNegativeButtonText, int rStrNeutralButtonText, View view, boolean isShowKeyboard, final OnToDoItemClickListener onToDoItemClickListener){
        return showAlertDialog(context, rStrTitle,rStrMessage, rStrPositiveButtonText, rStrNegativeButtonText, rStrNeutralButtonText, view, isShowKeyboard,true,onToDoItemClickListener);
    }

    public static AlertDialog showAlertDialog(final Context context, int rStrTitle, int rStrMessage, int rStrPositiveButtonText, int rStrNegativeButtonText, int rStrNeutralButtonText, View view, boolean isShowKeyboard,boolean cancelable, final OnToDoItemClickListener onToDoItemClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        if(rStrTitle>0){
            builder.setTitle(rStrTitle);
        }
        if(view!=null){
            builder.setView(view);
            if(isShowKeyboard){
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) context
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    }

                }, 500);
            }
        }
        if(rStrMessage>0){
            builder.setMessage(rStrMessage);
        }

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
        AlertDialog alertDialog=builder.show();
        return alertDialog;
    }

    public static AlertDialog showAlertDialogEditText(Context context,int rStrTitle,View view,OnToDoItemClickListener onToDoItemClickListener){
        return showAlertDialog(context, rStrTitle, 0,R.string.btn_ok, R.string.btn_cancle, 0,view,true,onToDoItemClickListener);
    }

    public static AlertDialog showAlertDialogInfo(Context context,int rStrTitle,View view){
        return showAlertDialog(context, rStrTitle, 0,0, R.string.btn_cancle, 0,view,false,null);
    }
}
