package com.yoyo.yopassword.password.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 项目名称：YoPassword
 * 类描述：分组
 * 创建人：yoyo
 * 创建时间：2016/5/13 17:59
 * 修改人：yoyo
 * 修改时间：2016/5/13 17:59
 * 修改备注：
 */
@Table(name = "GroupingInfo")
public class GroupingInfo {
    @Column(name ="groupingId",isId=true)
    long groupingId;
    @Column(name ="groupingName")
    String groupingName;

    public long getGroupingId() {
        return groupingId;
    }

    public void setGroupingId(long groupingId) {
        this.groupingId = groupingId;
    }

    public String getGroupingName() {
        return groupingName;
    }

    public void setGroupingName(String groupingName) {
        this.groupingName = groupingName;
    }
}
