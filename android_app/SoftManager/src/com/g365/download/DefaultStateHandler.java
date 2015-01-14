package com.g365.download;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import comns.database.TableReadOperator;
import comns.database.TableWriteOperator;
import comns.file.download.database.DownloadDB;
import comns.file.download.interfaces.DownloadStateHandler;

/**
 * @类名: DefaultStateHandler
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-6 上午09:54:42
 * 
 * @描述: 类<code>DefaultStateHandler</code>是用来管理下载状态的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class DefaultStateHandler implements DownloadStateHandler {

	private DownloadDB db;

	public DefaultStateHandler(Context context) {

		db = DownloadDB.getInstance(context);
	}

	
	public boolean isNewFile(final String fileUrl) {
		// TODO Auto-generated method stub

		TableReadOperator readOperator = new TableReadOperator() {

			
			public void doWork(SQLiteDatabase db) {
				// TODO Auto-generated method stub

				String sql = "select * from " + DownloadDB.TABLE_FILE
						+ " where " + DownloadDB.KEY_URL + " = '" + fileUrl
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

	
	public boolean addNewFile(final String fileUrl, final String fileMd5,
			final long fileSize, final int fileState) {
		// TODO Auto-generated method stub

		TableWriteOperator writeOperator = new TableWriteOperator() {

		
			public void doWork(SQLiteDatabase db) {
				// TODO Auto-generated method stub

				ContentValues contentValues = new ContentValues();
				contentValues.put(DownloadDB.KEY_URL, fileUrl);
				contentValues.put(DownloadDB.KEY_FILE_MD5, fileMd5);
				contentValues.put(DownloadDB.KEY_FILE_SIZE, fileSize);
				contentValues.put(DownloadDB.KEY_FILE_STATE, fileState);
				db.insert(DownloadDB.TABLE_FILE, null, contentValues);
			}
		};

		return db.writeOperator(writeOperator);
	}

	
	public boolean delFile(final String fileUrl) {
		// TODO Auto-generated method stub

		TableWriteOperator writeOperator = new TableWriteOperator() {

		
			public void doWork(SQLiteDatabase db) {
				// TODO Auto-generated method stub

				String sql = "delete from " + DownloadDB.TABLE_FILE + " where "
						+ DownloadDB.KEY_URL + " = '" + fileUrl + "'";
				db.execSQL(sql);
			}
		};

		return db.writeOperator(writeOperator);
	}


	public boolean updateFile(final String fileUrl, final String fileMd5,
			final long fileSize, final int fileState) {
		// TODO Auto-generated method stub

		TableWriteOperator writeOperator = new TableWriteOperator() {

		
			public void doWork(SQLiteDatabase db) {
				// TODO Auto-generated method stub

				ContentValues contentValues = new ContentValues();
				contentValues.put(DownloadDB.KEY_FILE_MD5, fileMd5);
				contentValues.put(DownloadDB.KEY_FILE_SIZE, fileSize);
				contentValues.put(DownloadDB.KEY_FILE_STATE, fileState);
				db.update(DownloadDB.TABLE_FILE, contentValues,
						DownloadDB.KEY_URL + " = '" + fileUrl + "'", null);
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
		// TODO Auto-generated method stub

		TableWriteOperator writeOperator = new TableWriteOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {
				// TODO Auto-generated method stub

				ContentValues contentValues = new ContentValues();
				contentValues.put(DownloadDB.KEY_FILE_STATE, fileState);
				db.update(DownloadDB.TABLE_FILE, contentValues,
						DownloadDB.KEY_URL + " = '" + fileUrl + "'", null);
			}
		};

		return db.writeOperator(writeOperator);
	}

	/**
	 * 获取文件的下载状态
	 * 
	 * @param fileUrl
	 *            文件链接
	 * @return
	 */
	public int getDownloadState(final String fileUrl) {

		int state = FileDownloader.STATE_DOWNLOAD;

		TableReadOperator readOperator = new TableReadOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {
				// TODO Auto-generated method stub

				String sqlStr = "select " + DownloadDB.KEY_FILE_STATE
						+ " from " + DownloadDB.TABLE_FILE + " where "
						+ DownloadDB.KEY_URL + " = '" + fileUrl + "'";
				cursor = db.rawQuery(sqlStr, null);
				if (cursor.moveToNext()) {
					result = cursor.getInt(cursor
							.getColumnIndex(DownloadDB.KEY_FILE_STATE));
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
			public void doWork(SQLiteDatabase db) {

				String sql = "select " + DownloadDB.KEY_FILE_SIZE + " from "
						+ DownloadDB.TABLE_FILE + " where "
						+ DownloadDB.KEY_URL + " = '" + fileUrl + "'";
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToNext()) {
					result = cursor.getInt(cursor
							.getColumnIndex(DownloadDB.KEY_FILE_SIZE));
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

			public void doWork(SQLiteDatabase db) {

				String sql = "select * from " + DownloadDB.TABLE_THREAD
						+ " where " + DownloadDB.KEY_URL + " = '" + fileUrl
						+ "'";
				cursor = db.rawQuery(sql, null);
				List<HashMap<Integer, Integer>> threadList = new ArrayList<HashMap<Integer, Integer>>();
				while (cursor.moveToNext()) {

					HashMap<Integer, Integer> tempMap = new HashMap<Integer, Integer>();
					tempMap.put(
							cursor.getInt(cursor
									.getColumnIndex(DownloadDB.KEY_THREAD_ID)),
							cursor.getInt(cursor
									.getColumnIndex(DownloadDB.KEY_DOWNLOAD_POS)));
					threadList.add(tempMap);
				}
				result = threadList;
			}

		};
		db.readOperator(readOperator);

		if (readOperator.result != null) {
			return (ArrayList<HashMap<Integer, Integer>>) readOperator.result;
		} else {
			return new ArrayList<HashMap<Integer, Integer>>();
		}
	}


	public boolean addNewThreadTask(final String fileUrl, final int threadId,
			final HashMap<Integer, Integer> threadData) {

		TableWriteOperator writeOperator = new TableWriteOperator() {

			@Override
			public void doWork(SQLiteDatabase db) {

				ContentValues contentValues = new ContentValues();
				contentValues.put(DownloadDB.KEY_URL, fileUrl);
				contentValues.put(DownloadDB.KEY_THREAD_ID, threadId);
				contentValues.put(DownloadDB.KEY_DOWNLOAD_POS,
						threadData.get(threadId));
				db.insert(DownloadDB.TABLE_THREAD, null, contentValues);
			}
		};

		return db.writeOperator(writeOperator);
	}

	
	public boolean updateThreadTask(final String fileUrl, final int threadId,
			final HashMap<Integer, Integer> threadData) {

		TableWriteOperator writeOperator = new TableWriteOperator() {

			public void doWork(SQLiteDatabase db) {

				ContentValues contentValues = new ContentValues();
				contentValues.put(DownloadDB.KEY_DOWNLOAD_POS,
						threadData.get(threadId));
				db.update(DownloadDB.TABLE_THREAD, contentValues,
						DownloadDB.KEY_URL + " = '" + fileUrl + "' and "
								+ DownloadDB.KEY_THREAD_ID + " = " + threadId,
						null);
			}
		};

		return db.writeOperator(writeOperator);
	}

	public boolean deleteThreadTask(final String fileUrl) {

		TableWriteOperator writeOperator = new TableWriteOperator() {

			public void doWork(SQLiteDatabase db) {

				String sql = "delete from " + DownloadDB.TABLE_THREAD
						+ " where " + DownloadDB.KEY_URL + " = '" + fileUrl
						+ "'";
				db.execSQL(sql);
			}
		};

		return db.writeOperator(writeOperator);
	}


	

}
