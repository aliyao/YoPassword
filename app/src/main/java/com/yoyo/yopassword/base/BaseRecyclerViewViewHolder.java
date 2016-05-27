package com.yoyo.yopassword.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/23 17:15
 * 修改人：yoyo
 * 修改时间：2016/5/23 17:15
 * 修改备注：
 */
public class BaseRecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public int position;
    public OnBaseRecyclerViewListener onRecyclerViewListener;

    public BaseRecyclerViewViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (null != onRecyclerViewListener) {
            onRecyclerViewListener.onItemClick(position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (null != onRecyclerViewListener) {
            return onRecyclerViewListener.onItemLongClick(position);
        }
        return true;
    }
}
