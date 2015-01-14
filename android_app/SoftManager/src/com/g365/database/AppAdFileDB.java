package com.g365.database;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import comns.database.TableOperator;
import comns.database.TableReadOperator;
import comns.database.TableWriteOperator;

/**
 * 是管理广告状态的类
 * @author nova
 * @日期 2012年12月20日17:35:04
 *
 */
public class AppAdFileDB extends SQLiteOpenHelper {

	/** 数据库名 */
	public static final String DB_NAME = "appad_download.db";
	/** 数据库版本号 */
	public static final int DB_VERSION = 1;
	/** 下载完成文件表 */
	public static final String TABLE_FILE_FINISH = "appfile_download_finish";
	
	public static final String KEY_ID = "_id";
	/** 文件的链接 */
	public static final String KEY_FILE_URL = "file_url";
	/** APK状态 */
	public static final String KEY_FILE_STATE = "file_state";
	/** 包名 */
	public static final String KEY_APK_PACKNAME = "apk_packname";
	/** 版本号 */
	public static final String KEY_APK_VERSIONCODE = "apk_versioncode";
	
	/** 广告的 ID */
	public static final String KEY_APK_ID = "apk_id";
	
	private static volatile AppAdFileDB downloadDB;
	private TableOperator operator;
	
	public static AppAdFileDB getInstance(Context context) {

		if (downloadDB == null) {
			downloadDB = new AppAdFileDB(context);
		}

		return downloadDB;
	}

	public AppAdFileDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		operator=new TableOperator();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sqlStr = "create table " + TABLE_FILE_FINISH + "(" + KEY_ID
				+ " integer primary key autoincrement," + KEY_FILE_URL
				+ " varchar(100)," + KEY_APK_PACKNAME + " varchar(100)," + KEY_APK_VERSIONCODE
				+ " integer,"+ KEY_APK_ID+" integer," + KEY_FILE_STATE + " integer)";
		db.execSQL(sqlStr);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + TABLE_FILE_FINISH);
		onCreate(db);
	}
	
	/**
	 * 对下载数据库表进行读操作
	 * 
	 * @param readOperator
	 *            读接口
	 * @return 是否成功
	 */
	public boolean readOperator(TableReadOperator readOperator) {

		return operator.read(readOperator, this);
	}
	
	
	/**
	 * 对下载数据库表进行写操作
	 * 
	 * @param writeOperator
	 *            写接口
	 */
	public boolean writeOperator(TableWriteOperator writeOperator) {

		return operator.write(writeOperator, this);
	}

//	public void deleteUpdateFormPackageName(String updateurl) {
//
//		SQLiteDatabase db = downloadDB.getWritableDatabase();
//		db.execSQL("delete from appfile_download_finish where file_url='"
//				+ updateurl + "'");
//		// 关闭数据库
//		//db.close();
//
//	}
	
}
