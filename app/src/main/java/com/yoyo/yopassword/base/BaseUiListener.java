package com.yoyo.yopassword.base;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.yoyo.yopassword.common.util.YoLogUtils;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/7 17:10
 * 修改人：yoyo
 * 修改时间：2016/5/7 17:10
 * 修改备注：
 */
public abstract class BaseUiListener implements IUiListener {

    @Override
    public void onComplete(Object o) {
        doComplete(o);
    }
    protected abstract void doComplete(Object o);
    @Override
    public void onError(UiError e) {
      YoLogUtils.d("onError:", "code:" + e.errorCode + ", msg:"
                + e.errorMessage + ", detail:" + e.errorDetail);
        doError(e);
    }
    @Override
    public void onCancel() {
        YoLogUtils.d("onCancel", "");
        doCancel();
    }
    protected abstract void doError(UiError e);

    protected abstract void doCancel();

}