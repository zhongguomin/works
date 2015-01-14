package comns.phone;

import java.io.File;

import android.content.Context;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import comns.net.NetInfoHelper;
import comns.system.CustomPrint;

/**
 * @类名: PhoneInfoHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-23 下午04:16:17
 * 
 * @描述: 类<code>PhoneInfoHelper</code>是手机信息帮助类，提供获取手机的各种信息函数</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class PhoneInfoHelper {

	/** 无运营商 */
	public static final int PROVIDER_NULL = 0;
	/** 中国移动 */
	public static final int PROVIDER_CMCC = 1;
	/** 中国联通 */
	public static final int PROVIDER_CUCC = 2;
	/** 中国电信 */
	public static final int PROVIDER_CTCC = 3;

	/**
	 * SIM卡状态是否良好
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static boolean isSimOk(Context context) {

		boolean isOk = false;

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
			isOk = true;
		}

		CustomPrint.d(PhoneInfoHelper.class, "isSimOk:" + isOk);

		return isOk;
	}

	/**
	 * @return 手机串号IMEI
	 */
	public static String getImei(Context context) {

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * @return 手机的IMSI，没有返回null
	 */
	public static String getImsi(Context context) {

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId();
	}

	/**
	 * @return 手机分辨率，int[0]表示宽，int[1]表示高
	 */
	public static int[] getResolution(Context context) {

		int[] resolution = new int[2];

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		resolution[0] = outMetrics.widthPixels;
		resolution[1] = outMetrics.heightPixels;

		return resolution;
	}

	/**
	 * 获取当前手机网络类型代表的数字（0：没有任何连接；1：CMWAP；2：CMNET；3：WIFI；4：CT）
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static int getNetType(Context context) {

		return NetInfoHelper.getNetType(context);
	}

	/**
	 * @return 获取系统版本
	 */
	public static String getOSVersion() {

		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * @return 获取系统版本号
	 */
	public static int getOSVersionCode() {

		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * @return 获取手机型号
	 */
	public static String getPhoneType() {

		return android.os.Build.MODEL;
	}

	/**
	 * @return 手机是否已 Root
	 */
	public static boolean isPhoneRoot() {

		boolean isRoot = false;
		String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/",
				"/system/sbin/", "/sbin/", "/vendor/bin/" };
		for (int i = 0; i < kSuSearchPaths.length; i++) {
			File suFile = new File(kSuSearchPaths[i] + "su");
			if (suFile.exists()) {
				isRoot = true;
			}
		}
		return isRoot;
	}

	/**
	 * 获取手机卡运营商（0：没有；1：CMCC；2：CUCC；3：CTCC）
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static int getSimProvider(Context context) {

		int provider = PROVIDER_NULL;

		String imsi = getImsi(context);
		if (imsi != null) {
			if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
				provider = PROVIDER_CMCC;
			} else if (imsi.startsWith("46001")) {
				provider = PROVIDER_CUCC;
			} else if (imsi.startsWith("46003")) {
				provider = PROVIDER_CTCC;
			}
		}

		return provider;
	}

	/**
	 * 判断手机屏幕是否点亮
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static boolean isScreenOn(Context context) {

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

}
