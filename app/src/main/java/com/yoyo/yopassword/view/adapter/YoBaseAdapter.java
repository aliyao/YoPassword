package com.yoyo.yopassword.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class YoBaseAdapter<T> extends BaseAdapter {
    protected List<T> mData = new ArrayList<T>();
    public List<Object> clickGray = new ArrayList<Object>();//点击变色
    protected LayoutInflater mInflater;
    protected Context context;

    public YoBaseAdapter(Context context, @NonNull List<T> mData) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        this.context = context;
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
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}


