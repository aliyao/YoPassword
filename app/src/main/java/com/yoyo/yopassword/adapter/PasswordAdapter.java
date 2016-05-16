package com.yoyo.yopassword.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.yoyo.yopassword.bean.PasswordInfoBean;

import java.util.List;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/16 17:28
 * 修改人：yoyo
 * 修改时间：2016/5/16 17:28
 * 修改备注：
 */
public class PasswordAdapter extends YoBaseAdapter<PasswordInfoBean> {

    public PasswordAdapter(Context context, @NonNull List<PasswordInfoBean> mData) {
        super(context, mData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
