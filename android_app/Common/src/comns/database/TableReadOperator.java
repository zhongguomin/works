package comns.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @类名: TableReadOperator
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-11 上午11:56:31
 * 
 * @描述: 类<code>TableReadOperator</code>是对表进行查询的接口</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public abstract class TableReadOperator {

	/** 返回的结果集 */
	public Object result = null;
	/** 查询的游标 */
	public Cursor cursor = null;

	public abstract void doWork(SQLiteDatabase db);
}
