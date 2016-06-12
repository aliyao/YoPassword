package com.yoyo.yopassword.common.config;

/**
 * 项目名称：YoPassword
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2016/5/23 15:54
 * 修改人：yoyo
 * 修改时间：2016/5/23 15:54
 * 修改备注：
 */
public class AppConfig {
    public final static boolean isDebug=true;
    public final static String logTag="YoPassword";
    public final static int minPageSize = 18;//判断是否显示加载更多
    //上下拉刷新延迟时间
    public final static long RefreshViewTime = 500;//毫秒
    public final static int DBVersion = 1;//数据库版本
    public final static String DBName = "YoPasswordDB";
    //默认的组ID
    public final static long DefaultGroupingId = 1;
    public final static boolean Is_SSOSetting=false;

    public final static String APP_KEY="3hD4XA8yv12L9X04EMFzWQ2XD5hK0sE1cN86n";
}
