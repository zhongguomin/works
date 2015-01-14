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
 * @author nova 保存升级的类
 * 
 */
public class SaveAppInfo {

	private DatabaseHelper databaseHelper;
	private Context context;

	/**
	 * 构造器完成初始化功能
	 * 
	 * @param context
	 */
	public SaveAppInfo(Context context) {
		this.databaseHelper = new DatabaseHelper(context);
		this.context=context;
	}

	/**
	 * 保存apk 包名和版本信息方法
	 */
	public void savePackageVersion(AppInfo appInfo) {
		// getWritableDatabase具有数据库缓存功能 针对同一个对象
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL(
				"insert into TABLE_APPLICATION(packname,versionCode) values(?,?)",
				new Object[] { appInfo.getPackageName(),
						appInfo.getVersionCode() });
		// 关闭数据库
		//db.close();
	}
	
	/**
	 * 更新版本号
	 */
	public void updateVersionCode(AppInfo appInfo){
		// getWritableDatabase具有数据库缓存功能 针对同一个对象
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("versionCode", appInfo.getVersionCode());
		db.update("TABLE_APPLICATION", values, "packname=?", new String[]{appInfo.getPackageName()});
	}

	/**
	 * 根据包名 删除用户安装的应用程序信息 2013年1月29日21:05:32
	 */
	public void deleteFormPackageName(String packageInstall) {

		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL("delete from TABLE_UPDATE where packname='"
				+ packageInstall + "'");
		// 关闭数据库
		//db.close();

	}


	
	/**
	 * 用户卸载应用 从TABLE_APPLICATION表中把数据删除
	 */
	public void deleteIfUninstall(String packageUninstall){
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		db.execSQL("delete from TABLE_APPLICATION where packname='"
				+ packageUninstall + "'");
		// 关闭数据库
		//db.close();
	}
	
	/**
	 * 将解析服务器返回的最新数据保存到TABLE_UPDATE 
	 * 表中 用于软件升级判断
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
		// 关闭数据库
		//db.close();
	}

	/**
	 * 取出TABLE_UPDATE表的数据 
	 * 显示要升级的软件
	 * @return
	 */
	public ArrayList<UpdataInfo> getScrollData() {
		System.out.println("-------------开始查询数据------------");
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
		System.out.println("--------得到数据------------" + updataVersionInfos);
		//cursor.close();
		//db.close();
		return updataVersionInfos;
	}
	
	
	
	/**
	 * 取出用户安装的所有应用
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
