package com.yoyo.yopassword.password.entity;

/**
 * 项目名称：YoPassword
 * 类描述：密码的信息
 * 创建人：yoyo
 * 创建时间：2016/5/13 17:54
 * 修改人：yoyo
 * 修改时间：2016/5/13 17:54
 * 修改备注：
 */
public class PasswordInfo {
    int passwordInfoId;
    String account;
    String password;
    String title;
    boolean isTop;
    long saveInfoTime;
    String remarks;
    int groupingId;

    public int getPasswordInfoId() {
        return passwordInfoId;
    }

    public void setPasswordInfoId(int passwordInfoId) {
        this.passwordInfoId = passwordInfoId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public long getSaveInfoTime() {
        return saveInfoTime;
    }

    public void setSaveInfoTime(long saveInfoTime) {
        this.saveInfoTime = saveInfoTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getGroupingId() {
        return groupingId;
    }

    public void setGroupingId(int groupingId) {
        this.groupingId = groupingId;
    }
}
