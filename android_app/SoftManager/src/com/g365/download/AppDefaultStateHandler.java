package com.g365.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.g365.database.AppDownloadDB;
import com.g365.download.interfaces.AppDownloadStateHandler;
import comns.database.TableReadOperator;
import comns.database.TableWriteOperator;

/**
 * 
 * @author nova
 * @日期 2012年12月20日14:29:36 是用来管理下载状态的类
 */
public class AppDefaultStateHandler implements AppDownloadStateHandler {
	private AppDownloadDB db;

	public AppDefaultStateHandler(Context context) {
		db = AppDownloadDB.getInstance(context);
	}

	public boolean isNewFile(final String fileUrl) {
		TableReadOperator readOperator = new TableReadOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {

				String sql = "select * from " + AppDownloadDB.TABLE_FILE
						+ " where " + AppDownloadDB.KEY_URL + " = '" + fileUrl
						+ "'";
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToNext()) {
					result = false;
				}
			}
		};
		db.readOperator(readOperator);
		if (readOperator.result != null) {
			return (Boolean) readOperator.result;
		} else {
			return true;
		}

	}

	public boolean addNewFile(final String fileUrl, final int fileSize,
			final int fileState) {

		TableWriteOperator writeOperator = new TableWriteOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {

				ContentValues contentValues = new ContentValues();
				contentValues.put(AppDownloadDB.KEY_URL, fileUrl);
				contentValues.put(AppDownloadDB.KEY_FILE_SIZE, fileSize);
				contentValues.put(AppDownloadDB.KEY_FILE_STATE, fileState);
				db.insert(AppDownloadDB.TABLE_FILE, null, contentValues);
			}
		};
		return db.writeOperator(writeOperator);
	}

	public boolean delFile(final String fileUrl) {

		TableWriteOperator writeOperator = new TableWriteOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {

				String sql = "delete from " + AppDownloadDB.TABLE_FILE
						+ " where " + AppDownloadDB.KEY_URL + " = '" + fileUrl
						+ "'";
				db.execSQL(sql);
			}
		};
		return db.writeOperator(writeOperator);
	}

	public boolean updateFile(final String fileUrl, final int fileSize,
			final int fileState) {
		TableWriteOperator writeOperator = new TableWriteOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {

				ContentValues contentValues = new ContentValues();
				contentValues.put(AppDownloadDB.KEY_FILE_SIZE, fileSize);
				contentValues.put(AppDownloadDB.KEY_FILE_STATE, fileState);
				db.update(AppDownloadDB.TABLE_FILE, contentValues,
						AppDownloadDB.KEY_URL + " = '" + fileUrl + "'", null);
			}
		};
		return db.writeOperator(writeOperator);
	}

	/**
	 * 更新文件状态
	 * 
	 * @param fileUrl
	 *            文件链接
	 * @param fileState
	 *            更新状态
	 * @return
	 */
	public boolean updateFileState(final String fileUrl, final int fileState) {

		TableWriteOperator writeOperator = new TableWriteOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {

				ContentValues contentValues = new ContentValues();
				contentValues.put(AppDownloadDB.KEY_FILE_STATE, fileState);
				db.update(AppDownloadDB.TABLE_FILE, contentValues,
						AppDownloadDB.KEY_URL + " = '" + fileUrl + "'", null);
			}
		};

		return db.writeOperator(writeOperator);

	}

	/**
	 * 获取文件的下载状态
	 * 
	 * @param context
	 *            上下文
	 * @param fileUrl
	 *            文件链接
	 * @return
	 */
	public int getDownloadState(final String fileUrl) {
		int state = AppFileDownloader.STATE_DOWNLOAD;
		TableReadOperator readOperator = new TableReadOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {

				String sqlStr = "select " + AppDownloadDB.KEY_FILE_STATE
						+ " from " + AppDownloadDB.TABLE_FILE + " where "
						+ AppDownloadDB.KEY_URL + " = '" + fileUrl + "'";
				cursor = db.rawQuery(sqlStr, null);
				if (cursor.moveToNext()) {
					result = cursor.getInt(cursor
							.getColumnIndex(AppDownloadDB.KEY_FILE_STATE));
				}

			}
		};
		db.readOperator(readOperator);
		if (readOperator.result != null) {
			state = (Integer) readOperator.result;
		}
		return state;

	}

	public int getFileSize(final String fileUrl) {
		TableReadOperator readOperator = new TableReadOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {

				String sql = "select " + AppDownloadDB.KEY_FILE_SIZE + " from "
						+ AppDownloadDB.TABLE_FILE + " where "
						+ AppDownloadDB.KEY_URL + " = '" + fileUrl + "'";
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToNext()) {
					result = cursor.getInt(cursor
							.getColumnIndex(AppDownloadDB.KEY_FILE_SIZE));
				}
			}
		};
		db.readOperator(readOperator);

		if (readOperator.result != null) {
			return (Integer) readOperator.result;
		} else {
			return 0;
		}

	}

	public ArrayList<HashMap<Integer, Integer>> getThreadState(
			final String fileUrl) {

		TableReadOperator readOperator = new TableReadOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {
				String sql = "select * from " + AppDownloadDB.TABLE_THREAD
						+ " where " + AppDownloadDB.KEY_URL + " = '" + fileUrl
						+ "'";
				cursor = db.rawQuery(sql, null);
				List<HashMap<Integer, Integer>> threadList = new ArrayList<HashMap<Integer, Integer>>();

				while (cursor.moveToNext()) {
					HashMap<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
					tempMap.put(
							cursor.getInt(cursor
									.getColumnIndex(AppDownloadDB.KEY_THREAD_ID)),
							cursor.getInt(cursor
									.getColumnIndex(AppDownloadDB.KEY_DOWNLOAD_POS)));
					threadList.add(tempMap);
				}
				result=threadList;
			}
		};
		
		db.readOperator(readOperator);
		
		if(readOperator.result!=null){
			return (ArrayList<HashMap<Integer, Integer>>) readOperator.result;
		}else{
			return new ArrayList<HashMap<Integer,Integer>>();
		}
		
	}

	public boolean addNewThreadTask(final String fileUrl, final int threadId,
			final HashMap<Integer, Integer> threadData) {
		
		TableWriteOperator writeOperator=new TableWriteOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {
				ContentValues contentValues = new ContentValues();
				contentValues.put(AppDownloadDB.KEY_URL, fileUrl);
				contentValues.put(AppDownloadDB.KEY_THREAD_ID, threadId);
				contentValues.put(AppDownloadDB.KEY_DOWNLOAD_POS,
						threadData.get(threadId));
				db.insert(AppDownloadDB.TABLE_THREAD, null, contentValues);
			}
		};
		return db.writeOperator(writeOperator);
	}

	public boolean updateThreadTask(final String fileUrl, final int threadId,
			final HashMap<Integer, Integer> threadData) {
		
		TableWriteOperator writeOperator=new TableWriteOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {
				
				ContentValues contentValues = new ContentValues();
				contentValues.put(AppDownloadDB.KEY_DOWNLOAD_POS,
						threadData.get(threadId));
				db.update(AppDownloadDB.TABLE_THREAD, contentValues,
						AppDownloadDB.KEY_URL + " = '" + fileUrl + "' and "
								+ AppDownloadDB.KEY_THREAD_ID + " = " + threadId,
						null);
			}
		};
		return db.writeOperator(writeOperator);
	}

	public boolean deleteThreadTask(final String fileUrl) {
		
		TableWriteOperator writeOperator=new TableWriteOperator() {
			
			@Override
			public void doWork(SQLiteDatabase db) {
				
				String sql = "delete from " + AppDownloadDB.TABLE_THREAD
						+ " where " + AppDownloadDB.KEY_URL + " = '" + fileUrl
						+ "'";
				db.execSQL(sql);
			}
		};
		return db.writeOperator(writeOperator);
	}

}
