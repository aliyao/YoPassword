package com.yoyo.yopassword.password.view.adapter;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.yoyo.yopassword.password.entity.PasswordInfo;
import com.yoyo.yopassword.base.YoBaseAdapter;
import com.yoyo.yopassword.password.view.adapter.holder.PasswordViewHolder;

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
public class PasswordAdapter extends YoBaseAdapter<PasswordInfo, PasswordViewHolder> {

    public PasswordAdapter(@NonNull List<PasswordInfo> mData) {
        super(mData);
    }

    @Override
    public PasswordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(PasswordViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}


