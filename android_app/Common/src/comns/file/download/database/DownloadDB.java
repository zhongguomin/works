package comns.file.download.database;

import comns.database.TableOperator;
import comns.database.TableReadOperator;
import comns.database.TableWriteOperator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @类名: DownloadDB
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-3 下午05:16:23
 * 
 * @描述: 类<code>DownloadDB</code>是存储文件下载和线程下载状态的数据库</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class DownloadDB extends SQLiteOpenHelper {

	/** 数据库名 */
	public static final String DB_NAME = "download.db";
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
	/** 文件的 md5 */ 
	public static final String KEY_FILE_MD5 = "file_md5";
	/** 文件下载状态 */
	public static final String KEY_FILE_STATE = "download_state";

	private static volatile DownloadDB downloadDB;
	private TableOperator operator;

	public static DownloadDB getInstance(Context context) {

		if (downloadDB == null) {
			downloadDB = new DownloadDB(context);
		}

		return downloadDB;
	}

	private DownloadDB(Context context) {

		super(context, DB_NAME, null, DB_VERSION);
		operator = new TableOperator();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String sqlStr = "create table " + TABLE_FILE + "(" + KEY_ID
				+ " integer primary key autoincrement," + KEY_URL
				+ " varchar(100)," + KEY_FILE_MD5 + " varchar(100),"
				+ KEY_FILE_SIZE + " integer," + KEY_FILE_STATE + " integer)";
		db.execSQL(sqlStr);

		sqlStr = "create table " + TABLE_THREAD + "(" + KEY_ID
				+ " integer primary key autoincrement," + KEY_URL
				+ " varchar(100)," + KEY_THREAD_ID + " integer,"
				+ KEY_DOWNLOAD_POS + " integer)";
		db.execSQL(sqlStr);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("drop table if exists " + TABLE_FILE);
		db.execSQL("drop table if exists " + TABLE_THREAD);
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

}
