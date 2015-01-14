//package com.g365.download;
//
//import com.g365.entity.UpdataInfo;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//
///**
// * 是应用程序状态改变的接收器
// * @author nova
// *  日期 2013年1月22日19:23:01
// */
//public class ApplicationReceiver extends BroadcastReceiver {
//
//    /**程序改变 */
//	public static final String ACTION_APPLICATION_STATE="application_change";
//	/**名称*/
//	public static final String APPLICATION_PACKNAME = "application_packname";
//	/**状态*/
//	public static final String APPLICATION_STATE = "application_state";
//	/** 版本*/
//	public static final String APPLICATION_VERSION = "application_version";
//	/** 版本号*/
//	public static final String APPLICATION_VERSION_CODE = "application_version_code";
//	/**ID */
//	public static final String APPLICATION_ID = "application_id";
//	
//	
//	
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		
//		if(intent!=null
//				&&intent.getAction().equals(ACTION_APPLICATION_STATE)){
//			Bundle bundle=intent.getExtras();
//			if(bundle!=null){
//				UpdataInfo updataInfo=new UpdataInfo();
//				updataInfo.tid=intent.getExtras().getInt(APPLICATION_ID);
//				updataInfo.packname=intent.getExtras().getString(APPLICATION_PACKNAME);
//				updataInfo.version=intent.getExtras().getString(APPLICATION_VERSION);
//				updataInfo.versioncode=intent.getExtras().getInt(APPLICATION_VERSION_CODE);
//				
//			}
//			
//		}
//	}
//	
//	public static void sendApplicationChange(Context context,
//			UpdataInfo updataInfo){
//		Intent intent=new Intent(ACTION_APPLICATION_STATE);
//		intent.putExtra(APPLICATION_PACKNAME, updataInfo.packname);
//		intent.putExtra(APPLICATION_STATE, updataInfo.state);
//		intent.putExtra(APPLICATION_VERSION, updataInfo.version);
//		intent.putExtra(APPLICATION_VERSION_CODE, updataInfo.versioncode);
//		intent.putExtra(APPLICATION_ID, updataInfo.tid);
//		context.sendBroadcast(intent);
//	}
//
//}
