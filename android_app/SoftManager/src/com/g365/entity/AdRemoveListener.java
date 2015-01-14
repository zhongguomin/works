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
 * @ 日期 2013年2月27日14:39:10
 * @ 是应用被卸载的监听器
 *
 */
public class AdRemoveListener  implements OnAppRemoveListener{

	public void onRemove(Context context, String packName) {
		
		if(packName!=null){
			AppAdDefaultStateHandler stateHandler=new AppAdDefaultStateHandler(context);
			
			/* 将已安装包名的对应版本删除掉 */
			ArrayList<AppWallDownloadInfo> list=stateHandler
					.getAppWallDownloadInfos(packName);
			
			for (AppWallDownloadInfo  info: list) {
				if(info.state==AppAdDefaultStateHandler.STATE_RUN){
					stateHandler.delFile(packName, info.versioncode);
					info.state=AppAdDefaultStateHandler.STATE_FINISH;
					AppStateChangeReceiver.sendStateChange(context, info);
					CustomPrint.d(getClass(), "remove：packName--->" + packName
							+ " versionCode：" + info.versioncode);
					break;
				}
			}
		}
	}

}
