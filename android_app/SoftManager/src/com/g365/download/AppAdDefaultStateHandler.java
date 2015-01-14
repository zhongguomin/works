package com.g365.download;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.g365.database.AppAdFileDB;
import com.g365.download.interfaces.AppAdStateHandler;
import com.g365.entity.AppWallDownloadInfo;
import comns.database.TableReadOperator;
import comns.database.TableWriteOperator;

/**
 * 是管理应用状态的实体类
 * @author nova
 * 日期 2012年12月21日10:08:00
 *
 */
public class AppAdDefaultStateHandler implements AppAdStateHandler {

	/** 下载状态：完成 */
	public static final int STATE_FINISH = 3;
	/** 运行 */
	public static final int STATE_RUN = 4;
	
	private AppAdFileDB db;
	
	public  AppAdDefaultStateHandler(Context context){
	    db=AppAdFileDB.getInstance(context);	
	}
	
	
	public boolean isNewFile(final String packName, final int versionCode) {
		
		TableReadOperator readOperator=new TableReadOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {
				String sql = "select * from " + AppAdFileDB.TABLE_FILE_FINISH
						+ " where " + AppAdFileDB.KEY_APK_PACKNAME + " = '"
						+ packName + "' and " + AppAdFileDB.KEY_APK_VERSIONCODE
						+ " = " + versionCode;
				cursor = db.rawQuery(sql, null);
				
				if(cursor.moveToNext()){
					result=false;
				}
			}
		};
		
		db.readOperator(readOperator);
		if(readOperator.result!=null){
			return (Boolean) readOperator.result;
		}else{
			return true;
		}
		
	}

	public boolean addNewFile(final String fileUrl,final String packName,
			final int versionCode,final int appid,final int fileState) {
		
		TableWriteOperator writeOperator=new TableWriteOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {
				
				ContentValues contentValues = new ContentValues();
				contentValues.put(AppAdFileDB.KEY_FILE_URL, fileUrl);
				contentValues.put(AppAdFileDB.KEY_APK_PACKNAME, packName);
				contentValues.put(AppAdFileDB.KEY_APK_VERSIONCODE, versionCode);
				contentValues.put(AppAdFileDB.KEY_APK_ID, appid);
				contentValues.put(AppAdFileDB.KEY_FILE_STATE, fileState);
				db.insert(AppAdFileDB.TABLE_FILE_FINISH, null, contentValues);
			}
		};
		return db.writeOperator(writeOperator);
	}

	public boolean delFile(final String packName, final int versionCode) {
		
		TableWriteOperator writeOperator=new TableWriteOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {
				
				String sql = "delete from " + AppAdFileDB.TABLE_FILE_FINISH
						+ " where " + AppAdFileDB.KEY_APK_PACKNAME + " = '"
						+ packName + "' and " + AppAdFileDB.KEY_APK_VERSIONCODE
						+ " = " + versionCode;
				db.execSQL(sql);
			}
		};
		
		return db.writeOperator(writeOperator);
	}

	public boolean updateFile(final String fileUrl,final String packName,
			final int versionCode,final int appid,final int fileState) {
		
		TableWriteOperator writeOperator=new TableWriteOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {
				
				ContentValues contentValues = new ContentValues();
				contentValues.put(AppAdFileDB.KEY_APK_PACKNAME, packName);
				contentValues.put(AppAdFileDB.KEY_APK_VERSIONCODE, versionCode);
				contentValues.put(AppAdFileDB.KEY_APK_ID, appid);
				contentValues.put(AppAdFileDB.KEY_FILE_STATE, fileState);
				db.update(AppAdFileDB.TABLE_FILE_FINISH, contentValues,
						AppAdFileDB.KEY_FILE_URL + " = '" + fileUrl + "'", null);
			}
		};
		return db.writeOperator(writeOperator);
	}

	public boolean updateState(final String packName,final int versionCode, 
			final int fileState) {
		
		TableWriteOperator writeOperator=new TableWriteOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {
				
				ContentValues contentValues = new ContentValues();
				contentValues.put(AppAdFileDB.KEY_FILE_STATE, fileState);
				db.update(AppAdFileDB.TABLE_FILE_FINISH, contentValues,
						AppAdFileDB.KEY_APK_PACKNAME + " = '" + packName
								+ "' and " + AppAdFileDB.KEY_APK_VERSIONCODE
								+ " = " + versionCode, null);
			}
		};
		return db.writeOperator(writeOperator);
	}

	public AppWallDownloadInfo getAppWallDownloadInfo(final String packName,
			final int versionCode) {
		
		TableReadOperator  readOperator=new TableReadOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {

				String sql = "select * from " + AppAdFileDB.TABLE_FILE_FINISH
						+ " where " + AppAdFileDB.KEY_APK_PACKNAME + " = '"
						+ packName + "' and " + AppAdFileDB.KEY_APK_VERSIONCODE
						+ " = " + versionCode;

				cursor = db.rawQuery(sql, null);
				if(cursor.moveToNext()){
					
					AppWallDownloadInfo info=new AppWallDownloadInfo();
					info.app_download=cursor.getString(cursor
							.getColumnIndex(AppAdFileDB.KEY_FILE_URL));
					info.packagename=cursor.getString(cursor
							.getColumnIndex(AppAdFileDB.KEY_APK_PACKNAME));
					info.versioncode=cursor.getInt(cursor
							.getColumnIndex(AppAdFileDB.KEY_APK_VERSIONCODE));
					info.app_id=cursor.getInt(cursor
							.getColumnIndex(AppAdFileDB.KEY_APK_ID));
					info.state=cursor.getInt(cursor
							.getColumnIndex(AppAdFileDB.KEY_FILE_STATE));
					result=info;
				}
			}
		};
		
		db.readOperator(readOperator);
		
		if(readOperator.result!=null){
			return (AppWallDownloadInfo) readOperator.result;
		}else{
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	public ArrayList<AppWallDownloadInfo> getAppWallDownloadInfos(
			final String packName) {
		
		TableReadOperator readOperator=new TableReadOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {
				
				ArrayList<AppWallDownloadInfo> list=new ArrayList<AppWallDownloadInfo>();
				String sql = "select * from " + AppAdFileDB.TABLE_FILE_FINISH
						+ " where " + AppAdFileDB.KEY_APK_PACKNAME + " = '"
						+ packName + "'";
				cursor=db.rawQuery(sql, null);
				if(cursor.moveToNext()){
					AppWallDownloadInfo info=new AppWallDownloadInfo();
					info.app_download = cursor.getString(cursor
							.getColumnIndex(AppAdFileDB.KEY_FILE_URL));
					info.packagename = cursor.getString(cursor
							.getColumnIndex(AppAdFileDB.KEY_APK_PACKNAME));
					info.versioncode = cursor.getInt(cursor
							.getColumnIndex(AppAdFileDB.KEY_APK_VERSIONCODE));
					info.app_id=cursor.getInt(cursor
							.getColumnIndex(AppAdFileDB.KEY_APK_ID));
					info.state = cursor.getInt(cursor
							.getColumnIndex(AppAdFileDB.KEY_FILE_STATE));
					list.add(info);
				}
				result=list;
			}
		};
		db.readOperator(readOperator);
		return (ArrayList<AppWallDownloadInfo>) readOperator.result;
	}

	public int getFileSate(final String fileUrl) {
		
		TableReadOperator readOperator=new TableReadOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {
				
				int state=AppAdDefaultStateHandler.STATE_FINISH;
				String sql = "select " + AppAdFileDB.KEY_FILE_STATE + " from "
						+ AppAdFileDB.TABLE_FILE_FINISH + " where "
						+ AppAdFileDB.KEY_FILE_URL + " = '" + fileUrl + "'";
			     cursor=db.rawQuery(sql, null);
			     if(cursor.moveToNext()){
			    	 
			    	 state=cursor.getInt(cursor
			    			 .getColumnIndex(AppAdFileDB.KEY_FILE_STATE));
			     }
			     result=state;
			}
		};
		
		db.readOperator(readOperator);
		return (Integer) readOperator.result;
	}


	

}
