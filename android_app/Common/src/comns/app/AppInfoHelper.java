package comns.app;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

import comns.system.CommandHelper;
import comns.system.CustomPrint;

/**
 * @类名: AppInfoHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-23 下午05:31:30
 * 
 * @描述: 类<code>AppInfoHelper</code>是和应用相关的帮助类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class AppInfoHelper {

	/** 保存产品号、渠道号和预装性信息的文件名 */
	public static String INIT_FILE_NAME = "init.xml";

	/**
	 * 获取本应用的版本
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static String getVersion(Context context) {

		String version = null;

		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return version;
	}

	/**
	 * 获取本应用的版本号
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static int getVersionCode(Context context) {

		int versionCode = 0;

		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			versionCode = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return versionCode;
	}

	/**
	 * 获取产品号String[0]、渠道号String[1]和预装性String[2]（没有读取到为""）
	 * 
	 * @param context
	 *            上下文
	 * @return
	 * @deprecated
	 * @see AppInfoHelper#getManifestInitInfo(Context)
	 */
	public static String[] getInitInfo(Context context) {

		String initInfo[] = new String[] { "", "", "" };

		try {
			InputStream inputStream = context.getAssets().open(INIT_FILE_NAME);
			byte[] buffer = new byte[1024];
			int len = 0;
			String text = "";
			while ((len = inputStream.read(buffer)) != -1) {
				String temp = new String(buffer, 0, len);
				text += temp;
			}
			initInfo[0] = text.substring(text.indexOf("<product>")
					+ "<product>".length(), text.indexOf("</product>"));
			initInfo[1] = text.substring(
					text.indexOf("<chanel>") + "<chanel>".length(),
					text.indexOf("</chanel>"));
			initInfo[2] = text.substring(
					text.indexOf("<type>") + "<type>".length(),
					text.indexOf("</type>"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return initInfo;
	}

	/**
	 * 从manifest获取产品号String[0]、渠道号String[1]、预装性String[2]和来源商String[3]（没有读取到为""）
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static String[] getManifestInitInfo(Context context) {

		String initInfo[] = new String[] { "", "", "", "" };

		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle bundle = appInfo.metaData;
			initInfo[0] = bundle.getInt("product") + "";
			initInfo[1] = bundle.getInt("channel") + "";
			initInfo[2] = bundle.getInt("type") + "";
			initInfo[3] = bundle.getString("source");

			CustomPrint.d(AppInfoHelper.class, "getInitInfo-->PID:"
					+ initInfo[0] + " CID:" + initInfo[1] + " TID:"
					+ initInfo[2] + " SID:" + initInfo[3]);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return initInfo;
	}

	/**
	 * 根据指定的包名获取该包名的 PackageInfo，没有返回 null
	 * 
	 * @param context
	 *            上下文
	 * @param packageName
	 *            要获取的包名
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context, String packageName) {

		PackageInfo packageInfo = null;
		PackageManager pm = context.getPackageManager();

		try {
			packageInfo = pm.getPackageInfo(packageName, 0);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return packageInfo;
	}

	/**
	 * 检测 APK包是否存在且完整
	 * 
	 * @param apkPath
	 *            APK文件路径
	 * @return
	 */
	public static boolean isApKFileOk(Context context, String apkPath) {

		boolean fileIsOk = false;

		File file = new File(apkPath);
		if (file.exists()) {
			try {
				PackageInfo packageInfo = context.getPackageManager()
						.getPackageArchiveInfo(apkPath,
								PackageManager.GET_ACTIVITIES);
				ApplicationInfo applicationInfo = packageInfo.applicationInfo;
				if (applicationInfo != null) {
					fileIsOk = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		CustomPrint.d(AppInfoHelper.class, "isApKFileOk：" + fileIsOk);

		return fileIsOk;
	}

	/**
	 * 判断某已安装应用是否完整
	 * 
	 * @param context
	 *            上下文
	 * @param packName
	 *            包名
	 * @return
	 */
	public static boolean isAppOk(Context context, String packName) {

		boolean appIsOk = false;

		try {
			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(packName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			if (applicationInfo != null) {
				appIsOk = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return appIsOk;
	}

	/**
	 * 判断是否是系统应用
	 * 
	 * @param context
	 *            上下文
	 * @param packName
	 *            应用包名
	 * @return
	 */
	public static boolean isSysApp(Context context, String packName) {

		boolean isSysApp = false;

		try {

			ApplicationInfo applicationInfo = context.getPackageManager()
					.getApplicationInfo(packName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			if (applicationInfo != null) {
				if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {// 系统应用
					isSysApp = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return isSysApp;
	}

	/**
	 * 将某普通应用转换成系统应用（需要设备已 Root）
	 * 
	 * @param context
	 *            上下文
	 * @param packName
	 *            包名
	 */
	public static void transToSysApp(Context context, String packName) {

		if (!isSysApp(context, packName)) {
			try {
				ApplicationInfo applicationInfo = context.getPackageManager()
						.getApplicationInfo(packName, 0);
				StringBuffer sb = new StringBuffer();
				sb.append("mount -o remount,rw -t yaffs2 /dev/block/mtdblock3 /system\n");
				sb.append("cat " + applicationInfo.publicSourceDir + " > "
						+ "/system/app/" + packName.replace(".", "_")
						+ ".apk\n");
				sb.append("mount -o remount,ro -t yaffs2 /dev/block/mtdblock3 /system\n");
				sb.append("rm " + applicationInfo.publicSourceDir + "\n");
				sb.append("reboot\n");
				CommandHelper.execRootCmd(sb.toString());
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
