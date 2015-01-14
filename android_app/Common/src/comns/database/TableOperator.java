package comns.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @����: TableOperator
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-11 ����12:02:55
 * 
 * @����: ��<code>TableOperator</code>�������Ա��д����</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class TableOperator {

	public static byte[] TABLE_LOCK = new byte[0];

	/**
	 * �Ա���в�ѯ����
	 * 
	 * @param readOperator
	 *            �������ӿ�
	 * @param openHelper
	 *            Ҫ���ж����������ݿ������
	 * @return ��ȡ�Ƿ�ɹ�
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
	 * �Ա��������ɾ���Ĳ���
	 * 
	 * @param writeOperator
	 *            д�����ӿ�
	 * @param openHelper
	 *            Ҫ����д���������ݿ������
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
