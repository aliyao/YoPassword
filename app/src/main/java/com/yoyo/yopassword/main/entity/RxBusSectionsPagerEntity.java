package com.yoyo.yopassword.main.entity;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/6 14:26
 * 修改人：yoyo
 * 修改时间：2016/6/6 14:26
 * 修改备注：
 */
public class RxBusSectionsPagerEntity {
    long delGroupingId;

    public RxBusSectionsPagerEntity(long delGroupingId){
        this.delGroupingId=delGroupingId;
    }
    public RxBusSectionsPagerEntity(){

    }
    public long getDelGroupingId() {
        return delGroupingId;
    }

    public void setDelGroupingId(long delGroupingId) {
        this.delGroupingId = delGroupingId;
    }
}
