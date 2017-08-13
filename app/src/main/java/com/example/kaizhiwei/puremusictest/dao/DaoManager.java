package com.example.kaizhiwei.puremusictest.dao;

import android.os.Environment;

import com.example.kaizhiwei.puremusictest.application.PureMusicApplication;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.io.File;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public class DaoManager {
    private static final String DB_NAME = "puremusic.db";
    private static Instace intance;

    public static class Instace{
        public DaoManager instance = new DaoManager();
    }

    static {
        intance = new Instace();
    }

    protected DbManager db;

    private DaoManager(){
        initDb();
    }

    public static DaoManager getInstance(){
        return intance.instance;
    }

    protected void initDb(){
        //本地数据的初始化
        String strDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                PureMusicApplication.getInstance().getPackageName() + File.separator + "database" + File.separator;
        File file = new File(strDir);
        file.mkdirs();
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("DB_NAME") //设置数据库名
                .setDbVersion(1) //设置数据库版本,每次启动应用时将会检查该版本号,
                //发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(true)//设置是否开启事务,默认为false关闭事务
                .setDbDir(file)
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {

                    }
                })//设置数据库创建时的Listener
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //balabala...
                    }
                });//设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        //.setDbDir(null);//设置数据库.db文件存放的目录,默认为包名下databases目录下
        db = x.getDb(daoConfig);
    }

    public DbManager getDbManager(){
        return db;
    }
}
