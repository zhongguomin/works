package comns.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @����: TableReadOperator
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-11 ����11:56:31
 * 
 * @����: ��<code>TableReadOperator</code>�ǶԱ���в�ѯ�Ľӿ�</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public abstract class TableReadOperator {

	/** ���صĽ���� */
	public Object result = null;
	/** ��ѯ���α� */
	public Cursor cursor = null;

	public abstract void doWork(SQLiteDatabase db);
}
