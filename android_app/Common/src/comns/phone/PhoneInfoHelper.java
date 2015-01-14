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
 * @����: PhoneInfoHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-23 ����04:16:17
 * 
 * @����: ��<code>PhoneInfoHelper</code>���ֻ���Ϣ�����࣬�ṩ��ȡ�ֻ��ĸ�����Ϣ����</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class PhoneInfoHelper {

	/** ����Ӫ�� */
	public static final int PROVIDER_NULL = 0;
	/** �й��ƶ� */
	public static final int PROVIDER_CMCC = 1;
	/** �й���ͨ */
	public static final int PROVIDER_CUCC = 2;
	/** �й����� */
	public static final int PROVIDER_CTCC = 3;

	/**
	 * SIM��״̬�Ƿ�����
	 * 
	 * @param context
	 *            ������
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
	 * @return �ֻ�����IMEI
	 */
	public static String getImei(Context context) {

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * @return �ֻ���IMSI��û�з���null
	 */
	public static String getImsi(Context context) {

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId();
	}

	/**
	 * @return �ֻ��ֱ��ʣ�int[0]��ʾ��int[1]��ʾ��
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
	 * ��ȡ��ǰ�ֻ��������ʹ�������֣�0��û���κ����ӣ�1��CMWAP��2��CMNET��3��WIFI��4��CT��
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static int getNetType(Context context) {

		return NetInfoHelper.getNetType(context);
	}

	/**
	 * @return ��ȡϵͳ�汾
	 */
	public static String getOSVersion() {

		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * @return ��ȡϵͳ�汾��
	 */
	public static int getOSVersionCode() {

		return android.os.Build.VERSION.SDK_INT;
	}

	/**
	 * @return ��ȡ�ֻ��ͺ�
	 */
	public static String getPhoneType() {

		return android.os.Build.MODEL;
	}

	/**
	 * @return �ֻ��Ƿ��� Root
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
	 * ��ȡ�ֻ�����Ӫ�̣�0��û�У�1��CMCC��2��CUCC��3��CTCC��
	 * 
	 * @param context
	 *            ������
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
	 * �ж��ֻ���Ļ�Ƿ����
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	public static boolean isScreenOn(Context context) {

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

}
