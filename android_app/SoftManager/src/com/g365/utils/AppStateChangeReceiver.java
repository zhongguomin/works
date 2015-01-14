package com.g365.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.g365.download.interfaces.AppOnStateChangeRepainter;
import com.g365.entity.AppWallDownloadInfo;

/**
 * ���״̬�ı�Ĺ㲥������
 * @author nova
 * ���� 2012��12��21��14:07:59
 *
 */
public class AppStateChangeReceiver extends BroadcastReceiver{

	/** ���Ź��ı�״̬�� Action */
	public static final String ACTION_AD_STATE_CHANGED = "action_ad_state_change";

	/** �㲥���״̬�ı����ݵ� AdWallDownloadInfo�ֶ� */
	public static final String DATA_KEY_ADWALL_STATE_INFO = "appadwallstateinfo";
	
	
	private AppOnStateChangeRepainter repainter;
	
	public AppStateChangeReceiver(AppOnStateChangeRepainter repainter){
		this.repainter=repainter;
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals(ACTION_AD_STATE_CHANGED)){
			
			if(repainter!=null){
				repainter.repaint(intent);
			}
		}
	}
	
	/**
	 * ��ù��״̬�ı����Ϣ��
	 * @param appWallDownloadInfo
	 *        ���ǽ���
	 * @param state
	 *         Ҫ�ı��״̬
	 * @return
	 */
	public static AppWallDownloadInfo getAppWallStateInfo(
			AppWallDownloadInfo appWallDownloadInfo ,int state){
			
		appWallDownloadInfo.state=state;
		
		return appWallDownloadInfo;
	}
	
	
	public static void sendStateChange(Context context,
			AppWallDownloadInfo appWallDownloadInfo){
		Intent intent=new Intent(ACTION_AD_STATE_CHANGED);
		intent.putExtra(DATA_KEY_ADWALL_STATE_INFO, appWallDownloadInfo);
		context.sendBroadcast(intent);
		
	}
	
	

}
