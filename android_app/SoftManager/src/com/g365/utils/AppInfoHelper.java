package com.g365.utils;


import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


/**
 * @����: AppInfoHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-23 ����05:31:30
 * 
 * @����: ��<code>AppInfoHelper</code>�Ǻ�Ӧ����صİ�����</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class AppInfoHelper {

	/** �����Ʒ�š������ź�Ԥװ����Ϣ���ļ��� */
	public static String INIT_FILE_NAME = "init.xml";

	/**
	 * ��ȡ��Ӧ�õİ汾
	 * 
	 * @param context
	 *            ������
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
	 * ��ȡ��Ӧ�õİ汾��
	 * 
	 * @param context
	 *            ������
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
	 * ��ȡ��Ʒ��String[0]��������String[1]��Ԥװ��String[2]��û�ж�ȡ��Ϊ""��
	 * 
	 * @param context
	 *            ������
	 * @return
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
	 * ����ָ���İ�����ȡ�ð����� PackageInfo��û�з��� null
	 * 
	 * @param context
	 *            ������
	 * @param packageName
	 *            Ҫ��ȡ�İ���
	 * @return
	 */
	public static PackageInfo getPackageInfo(Context context, String packageName) {

		PackageInfo packageInfo = null;
		PackageManager pm = context.getPackageManager();

		try {
			packageInfo = pm.getPackageInfo(packageName, 0);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return packageInfo;
	}

	/**
	 * ��� APK���Ƿ����������
	 * 
	 * @param apkPath
	 *            APK�ļ�·��
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
			}
		}

		CustomPrint.d(AppInfoHelper.class, "isApKFileOk��" + fileIsOk);

		return fileIsOk;
	}

	/**
	 * �ж�ĳ�Ѱ�װӦ���Ƿ�����
	 * 
	 * @param context
	 *            ������
	 * @param packName
	 *            ����
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
		}

		return appIsOk;
	}
}
