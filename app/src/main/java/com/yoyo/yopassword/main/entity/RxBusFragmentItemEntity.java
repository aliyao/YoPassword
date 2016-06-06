package com.yoyo.yopassword.main.entity;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/3 17:34
 * 修改人：yoyo
 * 修改时间：2016/6/3 17:34
 * 修改备注：
 */
public class RxBusFragmentItemEntity {
    long oldGroupingId;
    long newGroupingId;

    public RxBusFragmentItemEntity( long oldGroupingId,long newGroupingId){
        this.oldGroupingId=oldGroupingId;
        this.newGroupingId=newGroupingId;
    }

    public RxBusFragmentItemEntity(){

    }

    public long getOldGroupingId() {
        return oldGroupingId;
    }

    public void setOldGroupingId(long oldGroupingId) {
        this.oldGroupingId = oldGroupingId;
    }

    public long getNewGroupingId() {
        return newGroupingId;
    }

    public void setNewGroupingId(long newGroupingId) {
        this.newGroupingId = newGroupingId;
    }
}
