package com.yoyo.yopassword.grouping.view.adapter.holder;

import android.view.View;
import android.widget.CheckBox;
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
public class GroupingViewHolder extends BaseRecyclerViewViewHolder{
    public TextView grouping_item_name;
    public CheckBox cb_is_select;

    public GroupingViewHolder(View itemView) {
        super(itemView);
        grouping_item_name=(TextView)itemView.findViewById(R.id.grouping_item_name);
        cb_is_select=(CheckBox)itemView.findViewById(R.id.cb_is_select);
    }
}
