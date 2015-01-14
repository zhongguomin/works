package com.g365.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import comns.database.TableOperator;
import comns.database.TableReadOperator;
import comns.database.TableWriteOperator;

/**
 * 
 * @author nova
 * @日期 2012年12月20日11:38:25
 * @描述 是存储文件下载和线程下载状态的数据库
 *
 */
public class AppDownloadDB extends SQLiteOpenHelper{
	
	/** 数据库名 */
	public static final String DB_NAME = "appdownload.db";
	/** 数据库版本号 */
	public static final int DB_VERSION = 2;
	/** 文件下载表 */
	public static final String TABLE_FILE = "file_download";
	/** 线程下载表 */
	public static final String TABLE_THREAD = "thread_download";

	public static final String KEY_ID = "_id";
	
	/** 下载链接 */
	public static final String KEY_URL = "url";
	/** 下载线程 id */
	public static final String KEY_THREAD_ID = "thread_id";
	/** 已下载位置 */
	public static final String KEY_DOWNLOAD_POS = "download_pos";
	
	/** 文件大小 */
	public static final String KEY_FILE_SIZE = "file_size";

	/** 文件下载状态 */
	public static final String KEY_FILE_STATE = "download_state";

	private static volatile AppDownloadDB appDownloadDB;
	private TableOperator operator;
	
	public static AppDownloadDB getInstance(Context context){
		if(appDownloadDB==null){
			appDownloadDB=new AppDownloadDB(context);
		}
		return appDownloadDB;
		
	}
	
	public AppDownloadDB(Context context){
		super(context, DB_NAME, null, DB_VERSION);
		operator=new TableOperator();
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlStr="create table "+TABLE_FILE+"("+KEY_ID
				+" integer primary key autoincrement,"+KEY_URL
				+" varchar(100),"+KEY_FILE_SIZE+" integer,"+KEY_FILE_STATE
				+" integer)";
		db.execSQL(sqlStr);
		String sqlStr1="create table "+TABLE_THREAD +"("+KEY_ID
				+" integer primary key autoincrement,"+KEY_URL+
				" varchar(100),"+KEY_THREAD_ID+" integer,"+
				KEY_DOWNLOAD_POS+" integer)";
		db.execSQL(sqlStr1);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_FILE);
		db.execSQL("drop table if exists " + TABLE_THREAD);
		onCreate(db);
	}

	/**
	 * 对下载数据库表进行读操作
	 * @param readOperator
	 *         读接口
	 * @return 是否成功
	 */
	public boolean readOperator(TableReadOperator readOperator){
		return operator.read(readOperator, this);
		
	}
	
	/**
	 * 对下载数据库表进行写操作
	 * @param writeOperator 写接口
	 * @return 
	 */
	public boolean writeOperator(TableWriteOperator writeOperator){
		return operator.write(writeOperator, this);
		
	}
	
	
	public void deleteUpdateFormPackageName(String updateurl) {

		SQLiteDatabase db = appDownloadDB.getWritableDatabase();
		db.execSQL("delete from file_download where url='"
				+ updateurl + "'");
		// 关闭数据库
		//db.close();

	}
}
