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
// * ��Ӧ�ó���״̬�ı�Ľ�����
// * @author nova
// *  ���� 2013��1��22��19:23:01
// */
//public class ApplicationReceiver extends BroadcastReceiver {
//
//    /**����ı� */
//	public static final String ACTION_APPLICATION_STATE="application_change";
//	/**����*/
//	public static final String APPLICATION_PACKNAME = "application_packname";
//	/**״̬*/
//	public static final String APPLICATION_STATE = "application_state";
//	/** �汾*/
//	public static final String APPLICATION_VERSION = "application_version";
//	/** �汾��*/
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
