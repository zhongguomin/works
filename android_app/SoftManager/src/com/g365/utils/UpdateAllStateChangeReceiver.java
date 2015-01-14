package com.g365.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.g365.download.UpdateAllDownloader;
import com.g365.download.interfaces.AppOnStateChangeRepainter;
import com.g365.entity.UpdataInfo;

/**
 * ״̬�ı�Ĺ㲥������
 * 
 * @author nova ���� 2013��1��21��14:52:26
 * 
 */
public class UpdateAllStateChangeReceiver extends BroadcastReceiver {

	/** �ı�״̬�� Action */
	public static final String ACTION_UPDATE_STATE_CHANGED = "action_updateall_state_change";

	/** ״̬�ı����ݵ� AdWallDownloadInfo�ֶ� */
	public static final String DATA_KEY_ADWALL_STATE_INFO = "updateInfos";

	private AppOnStateChangeRepainter repainter;

	public UpdateAllStateChangeReceiver(){
		
	}
	public UpdateAllStateChangeReceiver(AppOnStateChangeRepainter repainter) {
		this.repainter = repainter;
	}

	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(ACTION_UPDATE_STATE_CHANGED)
				|| intent.getAction().equals(
						UpdateAllDownloader.ACTION_UPDATEALL_DOWNLOAD_CHANCE)) {

			if (repainter != null) {
				repainter.repaint(intent);
			}
		}
	}

	/**
	 * ��ù��״̬�ı����Ϣ��
	 * @param UpdataInfo
	 * @param state
	 *            Ҫ�ı��״̬
	 * @return
	 */

	public static UpdataInfo getUpdataInfo(UpdataInfo updataInfos, int state) {
		updataInfos.state = state;
		return updataInfos;
	}

	public static void sendStateChange(Context context, UpdataInfo updataInfos) {
		Intent intent = new Intent(ACTION_UPDATE_STATE_CHANGED);
		intent.putExtra(DATA_KEY_ADWALL_STATE_INFO, updataInfos);
		context.sendBroadcast(intent);
	}

}
