package com.yoyo.yopassword.common.util;

import com.yoyo.yopassword.common.config.AppConfig;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * 项目名称：PartTimeCat
 * 类描述：
 * 创建人：yoyo
 * 创建时间：2015/11/5 20:51
 * 修改人：yoyo
 * 修改时间：2015/11/5 20:51
 * 修改备注：
 */
public class X3DBUtils {
    public static  DbManager.DaoConfig getDaoConfig(String dbName){
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName(dbName)
                .setDbVersion(AppConfig.DBVersion);
      /*  DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("test")
                .setDbDir(new File("/sdcard"))
                .setDbVersion(1)
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                    }
                });*/
        return daoConfig;
    }
    public static DbManager getDb(String dbName){
        DbManager db = x.getDb(getDaoConfig(dbName));
        return db;
    }
    public static DbManager getDb(){
        DbManager db = x.getDb( getDaoConfig(AppConfig.DBName));
        return db;
    }
    public static void save(Object items) {
        if (items == null) {
            return;
        }
        try {
            DbManager db = getDb();
            db.saveOrUpdate(items);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public static void delect(Object items) {
        if (items == null) {
            return;
        }
        try {
            DbManager db = getDb();
            db.delete(items);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
