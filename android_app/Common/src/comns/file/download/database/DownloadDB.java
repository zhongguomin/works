package comns.file.download.database;

import comns.database.TableOperator;
import comns.database.TableReadOperator;
import comns.database.TableWriteOperator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @����: DownloadDB
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-11-3 ����05:16:23
 * 
 * @����: ��<code>DownloadDB</code>�Ǵ洢�ļ����غ��߳�����״̬�����ݿ�</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class DownloadDB extends SQLiteOpenHelper {

	/** ���ݿ��� */
	public static final String DB_NAME = "download.db";
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
	/** �ļ��� md5 */ 
	public static final String KEY_FILE_MD5 = "file_md5";
	/** �ļ�����״̬ */
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
	 * ���������ݿ����ж�����
	 * 
	 * @param readOperator
	 *            ���ӿ�
	 * @return �Ƿ�ɹ�
	 */
	public boolean readOperator(TableReadOperator readOperator) {

		return operator.read(readOperator, this);
	}

	/**
	 * ���������ݿ�����д����
	 * 
	 * @param writeOperator
	 *            д�ӿ�
	 */
	public boolean writeOperator(TableWriteOperator writeOperator) {

		return operator.write(writeOperator, this);
	}

}
