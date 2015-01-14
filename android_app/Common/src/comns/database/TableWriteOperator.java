package comns.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * @类名: TableWriteOperator
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-11 上午11:55:50
 * 
 * @描述: 类<code>TableWriteOperator</code>是用来对表进行增、删、改的接口</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public abstract class TableWriteOperator {

	public abstract void doWork(SQLiteDatabase db);
}
