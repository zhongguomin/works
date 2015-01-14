package com.g365.softmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
  
	//数据库名称
	private  static  final String DATABASE_NAME="softmanagers.db";
	//数据库版本
	private  static  final int DATABASE_VERSION=1;
	
	
	/**
	 * 构造器
	 * @param context 上下文
	 * @param DATABASE_NAME 数据库名称
	 * @param factory  null
	 * @param DATABASE_VERSION  版本
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

     /**
      * 第一次创建数据库执行
      */
	public void onCreate(SQLiteDatabase db) {
		
		/**
		 * 本地信息表
		 * _id  主键自增长
		 * itemtype  本地apk状态    （安装  未安装）
		 * name  应用程序名
		 * packname 包名
		 * version  版本
		 * versioncode 版本号
		 */
		
		db.execSQL("CREATE TABLE TABLE_APPLICATION("+
				"_id integer primary key autoincrement, "+
				"itemtype int, name varchar(20), "+
				"packname varchar(50),version varchar(20),"+
				" versioncode integer)");
		/**
		 * _id  主键自增长
		 * url 下载url
		 * iconurl 图标url
		 * size apk大小
		 * uid  软件分类
		 * tid  软件编号
		 * type  软件分类  sorf 或者game
		 * state  下载状态
		 * name  软件名
		 * packname 包名
		 * version  版本
		 * versioncode 版本号
		 */
		
		db.execSQL("CREATE TABLE TABLE_FILE("+
				"_id integer primary key autoincrement, url varchar(100),"+
				" iconurl varchar(100),size INTEGER, "+
				"uid INTEGER, tid int, type varchar(10), "+
				"state int, name varchar(20), packname varchar(50),"+
				" version varchar(20), versioncode integer)");
		
		/**
		 * _id       主键自增长
		 * uid       软件分类
		 * threadid  线程id
		 * position  下载位置
		 */
		db.execSQL("CREATE TABLE  TABLE_FILEDOWN("+
				"_id integer primary key autoincrement,"+
				" uid INTEGER, threadid int, position integer)");
		
		/**
		 * 服务器返回的信息表
		 * _id  主键自增长
		 * uid  软件分类
		 * type 软件分类 soft 或者 game
		 * tid  软件编号
		 * iconurl 图标url
		 * name  应用程序名
		 * packname 包名
		 * version  版本
		 * versioncode 版本号
		 * md5hash 
		 * star 星级 
		 * size apk大小
		 * url 下载url
		 * lastdate 最后一次更新时间
		 */
		db.execSQL("CREATE TABLE TABLE_UPDATE(" +
				"_id integer primary key autoincrement, "+
				"uid INTEGER,type varchar(10),tid int, iconurl varchar(100),"+
				"name varchar(50),packname varchar(50),version varchar(50),"+
				" versioncode INTEGER, md5hash varchar(50),star integer,"+
				"size INTEGER,url varchar(100),lastdate INTEGER)");
	}

	/**
	 * 数据库版本更新时执行
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Log.v("SoftManager", "365软件管家 " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS TABLE_APPLICATION");
		db.execSQL("DROP TABLE IF EXISTS TABLE_FILE");
		db.execSQL("DROP TABLE IF EXISTS TABLE_FILEDOWN");
		db.execSQL("DROP TABLE IF EXISTS TABLE_UPDATE");
		onCreate(db);
	}

}
