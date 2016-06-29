package com.yoyo.yopassword.main.entity;

/**
 * 项目名称：YoAppManage
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/28 10:04
 * 修改人：yoyo
 * 修改时间：2016/6/28 10:04
 * 修改备注：
 */
public class AppUpdateEntity{
    int versionCode;
    String updateUrl;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
