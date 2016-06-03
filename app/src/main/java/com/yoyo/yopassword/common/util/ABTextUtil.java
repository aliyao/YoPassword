package com.yoyo.yopassword.common.util;

import java.util.List;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/6/3 10:48
 * 修改人：yoyo
 * 修改时间：2016/6/3 10:48
 * 修改备注：
 */
public class ABTextUtil {

    public static boolean isEmpty(List list){
        if(list==null||list.size()<=0){
            return true;
        }
        return false;
    }
}
