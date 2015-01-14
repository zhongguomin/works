package comns.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @类名: TableOperator
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-11 下午12:02:55
 * 
 * @描述: 类<code>TableOperator</code>是用来对表读写的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class TableOperator {

	public static byte[] TABLE_LOCK = new byte[0];

	/**
	 * 对表进行查询操作
	 * 
	 * @param readOperator
	 *            读操作接口
	 * @param openHelper
	 *            要进行读操作的数据库帮助类
	 * @return 读取是否成功
	 */
	public boolean read(TableReadOperator readOperator,
			SQLiteOpenHelper openHelper) {

		synchronized (TABLE_LOCK) {

			boolean ret = false;
			SQLiteDatabase db = null;

			try {
				db = openHelper.getReadableDatabase();
				db.beginTransaction();
				readOperator.doWork(db);
				db.setTransactionSuccessful();
				ret = true;
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("TableOperator", "read error:" + e.getMessage());
			} finally {
				if (readOperator.cursor != null) {
					readOperator.cursor.close();
				}
				if (db != null) {
					db.endTransaction();
					db.close();
				}
			}

			return ret;
		}
	};

	/**
	 * 对表进行增、删、改操作
	 * 
	 * @param writeOperator
	 *            写操作接口
	 * @param openHelper
	 *            要进行写操作的数据库帮助类
	 * @return
	 */
	public boolean write(TableWriteOperator writeOperator,
			SQLiteOpenHelper openHelper) {

		synchronized (TABLE_LOCK) {

			boolean ret = false;
			SQLiteDatabase db = null;

			try {
				db = openHelper.getWritableDatabase();
				db.beginTransaction();
				writeOperator.doWork(db);
				db.setTransactionSuccessful();
				ret = true;
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("TableOperator", "write error:" + e.getMessage());
			} finally {
				if (db != null) {
					db.endTransaction();
					db.close();
				}
			}

			return ret;
		}
	};
}
