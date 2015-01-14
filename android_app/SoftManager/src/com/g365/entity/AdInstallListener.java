package com.g365.entity;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.g365.download.AppAdDefaultStateHandler;
import com.g365.receiver.interfaces.OnAppInstallListener;
import com.g365.utils.AppInfoHelper;
import com.g365.utils.AppStateChangeReceiver;
import com.g365.utils.CustomPrint;
import com.g365.utils.UrlHelper;
import com.lllfy.newad.core.AdHelper;
/**
 * 
 * @author nova
 * @ 日期 2013年2月27日14:32:40
 * @ 是广告被安装后执行的接口
 *
 */
public class AdInstallListener  implements OnAppInstallListener{

	public void onInstall(Context context, PackageInfo packageInfo) {
		
		if(packageInfo!=null){
			String filePath=packageInfo.applicationInfo.publicSourceDir;
			String packName=packageInfo.packageName;
			int versionCode=packageInfo.versionCode;
			AppAdDefaultStateHandler stateHandler=new AppAdDefaultStateHandler(context);
			
			if(!stateHandler.isNewFile(packName, versionCode)){
				stateHandler.updateState(packName, versionCode,
						AppAdDefaultStateHandler.STATE_RUN);
				
				AppWallDownloadInfo  adInfo=stateHandler
						.getAppWallDownloadInfo(packName, versionCode);
				adInfo.state=AppAdDefaultStateHandler.STATE_RUN;
				
				AppStateChangeReceiver.sendStateChange(context, adInfo);
				/* 向接口提交数据 */
				try {
					
					String[] info = AppInfoHelper
							.getInitInfo(context);
					// http://cp.g365.cn/app_download.php?userid=1&type=1&aid=1
					// http://cp.g365.cn/app_download.php?userid=15&type=1&aid=2
					final String urlStr = "http://cp.g365.cn/app_download.php?userid=="
								+ AdHelper.getUserId(context)
								+ "&product=" + info[0] + "&ad=" + adInfo.app_id;

					new Thread() {

						public void run() {

							int result = Integer.parseInt(UrlHelper
									.getTextFromUrl(urlStr));

							CustomPrint.d(getClass(),
									"adwall install post："
											+ (result == 1 ? "success"
													: "fail"));
						}
					}.start();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				CustomPrint.d(getClass(), "install by packName：" + packName);
			}
		}
	}

}
