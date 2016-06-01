package com.yoyo.yopassword.login.util;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/1 16:59
 * 修改人：yoyo
 * 修改时间：2016/6/1 16:59
 * 修改备注：
 */
public interface YoPlatformActionListener {
    void onComplete(Platform plat, int action, HashMap<String, Object> res);
}
