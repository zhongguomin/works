package com.g365.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.g365.entity.AppInfo;
import com.g365.entity.UpdataInfo;
import com.g365.softmanager.DatabaseHelper;

/**
 * 
 * @author nova ������������
 * 
 */
public class SaveAppInfo {

	private DatabaseHelper databaseHelper;
	private Context context;

	/**
	 * ��������ɳ�ʼ������
	 * 
	 * @param context
	 */
	public SaveAppInfo(Context context) {
		this.databaseHelper = new DatabaseHelper(context);
		this.context=context;
	}

	/**
	 * ����apk �����Ͱ汾��Ϣ����
	 */
	public void savePackageVersion(AppInfo appInfo) {
		// getWritableDatabase�������ݿ⻺�湦�� ���ͬһ������
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL(
				"insert into TABLE_APPLICATION(packname,versionCode) values(?,?)",
				new Object[] { appInfo.getPackageName(),
						appInfo.getVersionCode() });
		// �ر����ݿ�
		//db.close();
	}
	
	/**
	 * ���°汾��
	 */
	public void updateVersionCode(AppInfo appInfo){
		// getWritableDatabase�������ݿ⻺�湦�� ���ͬһ������
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("versionCode", appInfo.getVersionCode());
		db.update("TABLE_APPLICATION", values, "packname=?", new String[]{appInfo.getPackageName()});
	}

	/**
	 * ���ݰ��� ɾ���û���װ��Ӧ�ó�����Ϣ 2013��1��29��21:05:32
	 */
	public void deleteFormPackageName(String packageInstall) {

		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL("delete from TABLE_UPDATE where packname='"
				+ packageInstall + "'");
		// �ر����ݿ�
		//db.close();

	}


	
	/**
	 * �û�ж��Ӧ�� ��TABLE_APPLICATION���а�����ɾ��
	 */
	public void deleteIfUninstall(String packageUninstall){
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL("delete from TABLE_APPLICATION where packname='"
				+ packageUninstall + "'");
		// �ر����ݿ�
		//db.close();
	}
	
	/**
	 * ���������������ص��������ݱ��浽TABLE_UPDATE 
	 * ���� ������������ж�
	 * @param updateList
	 */
	public void savePullServerData(List<UpdataInfo> updateList) {
		if (updateList == null) {
			return;
		}
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL("delete from TABLE_UPDATE");
		for (UpdataInfo updataInfo : updateList) {
			db.execSQL(
					"insert into TABLE_UPDATE(uid,type,tid,iconurl,name,packname,version,versioncode,md5hash,star,size,url,lastdate) "
							+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { updataInfo.getUid(), updataInfo.getType(),
							updataInfo.getTid(), updataInfo.getIconurl(),
							updataInfo.getName(), updataInfo.getPackname(),
							updataInfo.getVersion(),
							updataInfo.getVersioncode(),
							updataInfo.getMd5hash(), updataInfo.getStar(),
							updataInfo.getSize(), updataInfo.getUrl(),
							updataInfo.getLastdate() });
		}
		// �ر����ݿ�
		//db.close();
	}

	/**
	 * ȡ��TABLE_UPDATE������� 
	 * ��ʾҪ���������
	 * @return
	 */
	public ArrayList<UpdataInfo> getScrollData() {
		System.out.println("-------------��ʼ��ѯ����------------");
		ArrayList<UpdataInfo> updataVersionInfos = new ArrayList<UpdataInfo>();
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from TABLE_UPDATE order by lastdate desc", null);
		while (cursor.moveToNext()) {
			String appIconurl = cursor.getString(cursor
					.getColumnIndex("iconurl"));
			String appName = cursor.getString(cursor.getColumnIndex("name"));
			int appSize = cursor.getInt(cursor.getColumnIndex("size"));
			String appVersion = cursor.getString(cursor
					.getColumnIndex("version"));
			String appPackagename = cursor.getString(cursor
					.getColumnIndex("packname"));
			String appUrl = cursor.getString(cursor.getColumnIndex("url"));
			updataVersionInfos.add(new UpdataInfo(appIconurl, appName, appSize,
					appVersion, appPackagename, appUrl));
		}
		System.out.println("--------�õ�����------------" + updataVersionInfos);
		//cursor.close();
		//db.close();
		return updataVersionInfos;
	}
	
	
	
	/**
	 * ȡ���û���װ������Ӧ��
	 * @return
	 */
	public ArrayList<AppInfo> getScrollDatato() {
		ArrayList<AppInfo> appInfoVersionInfos = new ArrayList<AppInfo>();
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select packname,versioncode from TABLE_APPLICATION", null);
		while (cursor.moveToNext()) {
			String appPackagename1 = cursor.getString(cursor
					.getColumnIndex("packname"));
			int appVersion1= cursor.getInt(cursor
					.getColumnIndex("versioncode"));
			appInfoVersionInfos.add(new AppInfo(appPackagename1,appVersion1));
		}
		//cursor.close();
		//db.close();
		return appInfoVersionInfos;
	}
	

	
}
