package com.g365.test;
import android.test.AndroidTestCase;

import com.g365.database.SaveAppInfo;
import com.g365.entity.AppInfo;
import com.g365.entity.UpdataInfo;
import com.g365.softmanager.DatabaseHelper;

/**
 * 单元测试
 * @author Administrator
 *
 */
public class Test extends AndroidTestCase {
	private AppInfo appInfo;
	private UpdataInfo updataInfo;
	
	
	public void testCreateDB() throws Exception {
		DatabaseHelper dbOpenHelper = new DatabaseHelper(getContext());

		dbOpenHelper.getWritableDatabase();
	}
	
	public void testsave() throws Exception {
		SaveAppInfo saveAppInfo=new SaveAppInfo(this.getContext());
		appInfo=new AppInfo();
		appInfo.setPackageName("dsad");
		appInfo.setVersionCode(1111);
		saveAppInfo.savePackageVersion(appInfo);
	}
	
	public  void testSaveUpdate() throws Exception{
		System.out.println("进入测试方法---------");
		SaveAppInfo saveAppInfo1=new SaveAppInfo(this.getContext());
		updataInfo=new UpdataInfo();
//		saveAppInfo1.savePullServerData(updataInfo);
//		
	}
}
