package com.yoyo.yopassword.grouping.entity;

import com.yoyo.yopassword.common.tool.TableColumnName;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by nidey on 2016/5/30.
 */
@Table(name = "PasswordGroupingInfo")
public class PasswordGroupingInfo {
    @Column(name = "passwordGroupingInfoId", isId = true)
    long passwordGroupingInfoId;
    @Column(name= TableColumnName.passwordInfoId)
    long passwordInfoId;
    @Column(name = TableColumnName.groupingId, isId = true)
    long groupingId;

    public PasswordGroupingInfo(long groupingId, long passwordInfoId){
        this.groupingId=groupingId;
        this.passwordInfoId=passwordInfoId;
    }
    public PasswordGroupingInfo(){

    }

    public long getPasswordGroupingInfoId() {
        return passwordGroupingInfoId;
    }

    public void setPasswordGroupingInfoId(long passwordGroupingInfoId) {
        this.passwordGroupingInfoId = passwordGroupingInfoId;
    }

    public long getPasswordInfoId() {
        return passwordInfoId;
    }

    public void setPasswordInfoId(long passwordInfoId) {
        this.passwordInfoId = passwordInfoId;
    }

    public long getGroupingId() {
        return groupingId;
    }

    public void setGroupingId(long groupingId) {
        this.groupingId = groupingId;
    }
}

