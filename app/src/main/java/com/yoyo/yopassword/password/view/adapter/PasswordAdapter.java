package com.yoyo.yopassword.password.view.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.common.util.DateUtils;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_item, null);
        //view.setBackgroundColor(Color.RED);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        PasswordViewHolder viewHolder=new PasswordViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PasswordViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        PasswordInfo passwordInfo=getItem(position);
        holder.password_item_account.setText(passwordInfo.isHideAccount()?"********":passwordInfo.getAccount());
        holder.password_item_remarks.setText(getRemarksText(passwordInfo.getRemarks()));
        holder.password_item_title.setText(passwordInfo.getTitle());
        holder.password_item_save_info_time.setText(DateUtils.getTimestampString(passwordInfo.getSaveInfoTime()));
        holder.password_item_top.setVisibility(passwordInfo.isTop()?View.VISIBLE:View.GONE);
    }

    public String getRemarksText(String remarks){
        if(TextUtils.isEmpty(remarks)){
            return "无";
        }
        return remarks;
    }
}


