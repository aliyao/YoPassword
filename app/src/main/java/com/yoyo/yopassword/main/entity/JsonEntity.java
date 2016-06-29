package com.yoyo.yopassword.main.entity;

/**
 * 项目名称：YoAppManage
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/28 10:21
 * 修改人：yoyo
 * 修改时间：2016/6/28 10:21
 * 修改备注：
 */
public class JsonEntity<T> {
    int state;
    T result;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
