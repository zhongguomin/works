package comns.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * @����: TableWriteOperator
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-11 ����11:55:50
 * 
 * @����: ��<code>TableWriteOperator</code>�������Ա��������ɾ���ĵĽӿ�</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public abstract class TableWriteOperator {

	public abstract void doWork(SQLiteDatabase db);
}
