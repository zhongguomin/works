package com.g365.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.g365.download.interfaces.AppOnStateChangeRepainter;
import com.g365.entity.AppWallDownloadInfo;

/**
 * 广告状态改变的广播接收器
 * @author nova
 * 日期 2012年12月21日14:07:59
 *
 */
public class AppStateChangeReceiver extends BroadcastReceiver{

	/** 播放广告改变状态的 Action */
	public static final String ACTION_AD_STATE_CHANGED = "action_ad_state_change";

	/** 广播广告状态改变数据的 AdWallDownloadInfo字段 */
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
	 * 获得广告状态改变的信息类
	 * @param appWallDownloadInfo
	 *        广告墙广告
	 * @param state
	 *         要改变的状态
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
