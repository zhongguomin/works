package com.g365.softmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
  
	//���ݿ�����
	private  static  final String DATABASE_NAME="softmanagers.db";
	//���ݿ�汾
	private  static  final int DATABASE_VERSION=1;
	
	
	/**
	 * ������
	 * @param context ������
	 * @param DATABASE_NAME ���ݿ�����
	 * @param factory  null
	 * @param DATABASE_VERSION  �汾
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

     /**
      * ��һ�δ������ݿ�ִ��
      */
	public void onCreate(SQLiteDatabase db) {
		
		/**
		 * ������Ϣ��
		 * _id  ����������
		 * itemtype  ����apk״̬    ����װ  δ��װ��
		 * name  Ӧ�ó�����
		 * packname ����
		 * version  �汾
		 * versioncode �汾��
		 */
		
		db.execSQL("CREATE TABLE TABLE_APPLICATION("+
				"_id integer primary key autoincrement, "+
				"itemtype int, name varchar(20), "+
				"packname varchar(50),version varchar(20),"+
				" versioncode integer)");
		/**
		 * _id  ����������
		 * url ����url
		 * iconurl ͼ��url
		 * size apk��С
		 * uid  �������
		 * tid  ������
		 * type  �������  sorf ����game
		 * state  ����״̬
		 * name  �����
		 * packname ����
		 * version  �汾
		 * versioncode �汾��
		 */
		
		db.execSQL("CREATE TABLE TABLE_FILE("+
				"_id integer primary key autoincrement, url varchar(100),"+
				" iconurl varchar(100),size INTEGER, "+
				"uid INTEGER, tid int, type varchar(10), "+
				"state int, name varchar(20), packname varchar(50),"+
				" version varchar(20), versioncode integer)");
		
		/**
		 * _id       ����������
		 * uid       �������
		 * threadid  �߳�id
		 * position  ����λ��
		 */
		db.execSQL("CREATE TABLE  TABLE_FILEDOWN("+
				"_id integer primary key autoincrement,"+
				" uid INTEGER, threadid int, position integer)");
		
		/**
		 * ���������ص���Ϣ��
		 * _id  ����������
		 * uid  �������
		 * type ������� soft ���� game
		 * tid  ������
		 * iconurl ͼ��url
		 * name  Ӧ�ó�����
		 * packname ����
		 * version  �汾
		 * versioncode �汾��
		 * md5hash 
		 * star �Ǽ� 
		 * size apk��С
		 * url ����url
		 * lastdate ���һ�θ���ʱ��
		 */
		db.execSQL("CREATE TABLE TABLE_UPDATE(" +
				"_id integer primary key autoincrement, "+
				"uid INTEGER,type varchar(10),tid int, iconurl varchar(100),"+
				"name varchar(50),packname varchar(50),version varchar(50),"+
				" versioncode INTEGER, md5hash varchar(50),star integer,"+
				"size INTEGER,url varchar(100),lastdate INTEGER)");
	}

	/**
	 * ���ݿ�汾����ʱִ��
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Log.v("SoftManager", "365����ܼ� " + oldVersion + " to " + newVersion);
		db.execSQL("DROP TABLE IF EXISTS TABLE_APPLICATION");
		db.execSQL("DROP TABLE IF EXISTS TABLE_FILE");
		db.execSQL("DROP TABLE IF EXISTS TABLE_FILEDOWN");
		db.execSQL("DROP TABLE IF EXISTS TABLE_UPDATE");
		onCreate(db);
	}

}
