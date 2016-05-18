package com.yoyo.yopassword.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yoyo.yopassword.data.bean.PasswordInfoBean;
import com.yoyo.yopassword.view.adapter.viewholder.PasswordViewHolder;

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
public class PasswordAdapter extends YoBaseAdapter<PasswordInfoBean,PasswordViewHolder> {

    public PasswordAdapter(@NonNull List<PasswordInfoBean> mData) {
        super(mData);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}


