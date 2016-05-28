package com.yoyo.yopassword.grouping.view.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.YoBaseAdapter;
import com.yoyo.yopassword.grouping.entity.GroupingInfo;
import com.yoyo.yopassword.grouping.view.adapter.holder.GroupingViewHolder;

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
public class GroupingAdapter extends YoBaseAdapter<GroupingInfo, GroupingViewHolder> {

    public GroupingAdapter(@NonNull List<GroupingInfo> mData) {
        super(mData);
    }

    @Override
    public GroupingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grouping_item, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        GroupingViewHolder viewHolder=new GroupingViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GroupingViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        GroupingInfo groupingInfo=getItem(position);
        holder.grouping_item_name.setText(groupingInfo.getGroupingName());
    }
}


