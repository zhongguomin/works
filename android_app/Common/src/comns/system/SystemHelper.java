package comns.system;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * @����: SystemHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-26 ����10:39:27
 * 
 * @����: ��<code>SystemHelper</code>�Ƿ�װ��һЩ��ϵͳ��صĳ��õĺ�������</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class SystemHelper {

	/** getPendingIntent</P>��ȡһ������Acitivity �� PendingIntent */
	public static final int MODE_PI_ACTIVITY = 0;
	/** getPendingIntent</P>��ȡһ������Service �� PendingIntent */
	public static final int MODE_PI_SERVICE = 1;
	/** getPendingIntent</P>��ȡһ���㲥 �� PendingIntent */
	public static final int MODE_PI_BROADCAST = 2;

	/**
	 * ��ȡһ���û�ָ���� PendingIntent
	 * 
	 * @param context
	 *            ������
	 * @param id
	 *            PendingIntent��Ψһ�� ID
	 * @param mode
	 *            �����ִ�еĲ������ͣ�0:Acitivity;1:Service;2:Broadcast��
	 * @param intent
	 *            �����ִ�е� Intent
	 * @return
	 */
	public static PendingIntent getPendingIntent(Context context, int id,
			int mode, Intent intent) {

		PendingIntent pendingIntent;

		switch (mode) {// ����ģʽ��ȡ��Ӧ�� PendingIntent
		case MODE_PI_SERVICE:
			pendingIntent = PendingIntent.getService(context, id, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		case MODE_PI_BROADCAST:
			pendingIntent = PendingIntent.getBroadcast(context, id, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		default:
			pendingIntent = PendingIntent.getActivity(context, id, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			break;
		}

		return pendingIntent;
	}
	
	/**
	 * ������Ļ����
	 * 
	 * @param activity
	 *            Ҫ���õ� Activity
	 * @param orientation
	 *            ����ActivityInfo�ж���� intֵ��
	 */
	public static void setScreenOrientation(Activity activity, int orientation) {

		activity.setRequestedOrientation(orientation);
	}

}