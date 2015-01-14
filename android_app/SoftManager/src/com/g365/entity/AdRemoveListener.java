package com.g365.entity;

import java.util.ArrayList;

import android.content.Context;

import com.g365.download.AppAdDefaultStateHandler;
import com.g365.receiver.interfaces.OnAppRemoveListener;
import com.g365.utils.AppStateChangeReceiver;
import com.g365.utils.CustomPrint;
/**
 * 
 * @author nova
 * @ ���� 2013��2��27��14:39:10
 * @ ��Ӧ�ñ�ж�صļ�����
 *
 */
public class AdRemoveListener  implements OnAppRemoveListener{

	public void onRemove(Context context, String packName) {
		
		if(packName!=null){
			AppAdDefaultStateHandler stateHandler=new AppAdDefaultStateHandler(context);
			
			/* ���Ѱ�װ�����Ķ�Ӧ�汾ɾ���� */
			ArrayList<AppWallDownloadInfo> list=stateHandler
					.getAppWallDownloadInfos(packName);
			
			for (AppWallDownloadInfo  info: list) {
				if(info.state==AppAdDefaultStateHandler.STATE_RUN){
					stateHandler.delFile(packName, info.versioncode);
					info.state=AppAdDefaultStateHandler.STATE_FINISH;
					AppStateChangeReceiver.sendStateChange(context, info);
					CustomPrint.d(getClass(), "remove��packName--->" + packName
							+ " versionCode��" + info.versioncode);
					break;
				}
			}
		}
	}

}
