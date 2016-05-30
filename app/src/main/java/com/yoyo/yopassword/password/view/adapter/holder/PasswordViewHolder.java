package com.yoyo.yopassword.password.view.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.yoyo.yopassword.R;
import com.yoyo.yopassword.base.BaseRecyclerViewViewHolder;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/18 17:45
 * 修改人：yoyo
 * 修改时间：2016/5/18 17:45
 * 修改备注：
 */
public class PasswordViewHolder extends BaseRecyclerViewViewHolder{
    public TextView password_item_title;
    public TextView password_item_save_info_time;
    public TextView password_item_account;
    public TextView password_item_remarks;
    public View password_item_top;

    public PasswordViewHolder(View itemView) {
        super(itemView);
        password_item_title=(TextView)itemView.findViewById(R.id.password_item_title);
        password_item_save_info_time=(TextView)itemView.findViewById(R.id.password_item_save_info_time);
        password_item_account=(TextView)itemView.findViewById(R.id.password_item_account);
        password_item_remarks=(TextView)itemView.findViewById(R.id.password_item_remarks);
        password_item_top=itemView.findViewById(R.id.password_item_top);
    }
}
