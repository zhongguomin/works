package comns.system;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * @����: SystemIntent
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-11-2 ����11:03:20
 * 
 * @����: ��<code>SystemIntent</code>�Ƿ�װ����ϵͳӦ�ú�������</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class SystemIntent {

	/**
	 * ���� IE ������
	 * 
	 * @param context
	 *            ������
	 * @param url
	 *            ���ӵ�ַ
	 */
	public static void openUrl(Context context, String url) {

		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	/**
	 * ��װӦ�ó���
	 * 
	 * @param context
	 *            ������
	 * @param filePath
	 *            APK��·��
	 */
	public static void installApk(Context context, String filePath) {

		Uri uri = Uri.parse(filePath);
		Intent notificationIntent = new Intent(Intent.ACTION_VIEW, uri);
		notificationIntent.setData(uri);
		notificationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		notificationIntent.setClassName("com.android.packageinstaller",
				"com.android.packageinstaller.PackageInstallerActivity");
		context.startActivity(notificationIntent);// ���ַ�ʽ���ܴ��ݲ���
	}

	/**
	 * ж��Ӧ�ó���
	 * 
	 * @param context
	 *            ������
	 * @param Packname
	 *            Ҫж�صİ���
	 */
	public static void unInstallApk(Context context, String packName) {

		try {
			Uri packageURI = Uri.parse("package:" + packName);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
					packageURI);
			context.startActivity(uninstallIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ����Ӧ�ó���
	 * 
	 * @param context
	 *            ������
	 * @param packName
	 *            Ҫ���еĳ������
	 */
	public static void runApplication(Context context, String packName) {

		try {
			PackageManager pm = context.getPackageManager();
			Intent startIntent = pm.getLaunchIntentForPackage(packName);
			context.startActivity(startIntent);
		} catch (Exception e) {
			CustomAlertDialog customAlertDialog = new CustomAlertDialog(context);
			customAlertDialog.showYesNoDialog("��Ӧ�ó���û�н���", null);
		}
	}

	/**
	 * ����ڷ�ʽ��������ΪcomponentNames[0]�е���ΪcomponentNames[1]��Acitivity
	 * 
	 * @param context
	 *            ������
	 * @param componentNames
	 *            Activity����·�������ַ�������
	 */
	public static void startMainActivity(Activity context,
			String[] componentNames) {

		Intent startIntent = new Intent();
		try {
			startIntent.setComponent(new ComponentName(componentNames[0],
					componentNames[1]));
			startIntent.setAction("android.intent.action.MAIN");
			startIntent.addCategory("android.intent.category.LAUNCHER");
			startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(startIntent);
			context.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ڷ�ʽ������Ӧ�õ�Activity
	 * 
	 * @param context
	 *            ������
	 * @param activityName
	 *            Ҫ������Activity����
	 */
	public static void startMainActivity(Activity context, String activityName) {
		startMainActivity(context, new String[] { context.getPackageName(),
				activityName });
	}

	/**
	 * ��ʾ�������ý���
	 * 
	 * @param context
	 *            ������
	 */
	public static void showNetSetting(Context context) {

		Intent intent = new Intent();
		if (android.os.Build.VERSION.SDK_INT > 10) {
			intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		} else {
			ComponentName component = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}
		context.startActivity(intent);
	}
}
