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
 * @���� 2012��12��20��11:38:25
 * @���� �Ǵ洢�ļ����غ��߳�����״̬�����ݿ�
 *
 */
public class AppDownloadDB extends SQLiteOpenHelper{
	
	/** ���ݿ��� */
	public static final String DB_NAME = "appdownload.db";
	/** ���ݿ�汾�� */
	public static final int DB_VERSION = 2;
	/** �ļ����ر� */
	public static final String TABLE_FILE = "file_download";
	/** �߳����ر� */
	public static final String TABLE_THREAD = "thread_download";

	public static final String KEY_ID = "_id";
	
	/** �������� */
	public static final String KEY_URL = "url";
	/** �����߳� id */
	public static final String KEY_THREAD_ID = "thread_id";
	/** ������λ�� */
	public static final String KEY_DOWNLOAD_POS = "download_pos";
	
	/** �ļ���С */
	public static final String KEY_FILE_SIZE = "file_size";

	/** �ļ�����״̬ */
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
	 * ���������ݿ����ж�����
	 * @param readOperator
	 *         ���ӿ�
	 * @return �Ƿ�ɹ�
	 */
	public boolean readOperator(TableReadOperator readOperator){
		return operator.read(readOperator, this);
		
	}
	
	/**
	 * ���������ݿ�����д����
	 * @param writeOperator д�ӿ�
	 * @return 
	 */
	public boolean writeOperator(TableWriteOperator writeOperator){
		return operator.write(writeOperator, this);
		
	}
	
	
	public void deleteUpdateFormPackageName(String updateurl) {

		SQLiteDatabase db = appDownloadDB.getWritableDatabase();
		db.execSQL("delete from file_download where url='"
				+ updateurl + "'");
		// �ر����ݿ�
		//db.close();

	}
}
