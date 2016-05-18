package com.yoyo.yopassword.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.yoyo.yopassword.view.adapter.viewholder.PasswordViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class YoBaseAdapter<T,L extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    protected List<T> mData = new ArrayList<T>();
    public List<Object> clickGray = new ArrayList<Object>();//点击变色

    public YoBaseAdapter(@NonNull List<T> mData) {
        this.mData = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void addData(@NonNull List<T> list) {
        this.mData.addAll(list);
    }
    public void clearData() {
        this.mData.clear();
        this.clickGray.clear();
    }

    public List<T> getmData() {
        return mData;
    }

    public void setmData(@NonNull List<T> mData) {
        this.mData = mData;
    }

    @Override
    public L onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}


