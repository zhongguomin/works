package com.g365.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * @����: NotificationHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-24 ����08:33:45
 * 
 * @����: ��<code>NotificationHelper</code>�Ǻ�֪ͨ��ص���</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class CustomNotification {

	/** ֪ͨ������ */
	private NotificationManager nm;
	/** ������ */
	private Context context;
	/** ֪ͨ�� id */
	private int id;
	/** ֪ͨ */
	private Notification notification;
	/** �����ִ�е� PendingIntent */
	private PendingIntent pendingIntent;

	/** ����һ������ȡ����֪ͨ */
	public static final int MODE_SHOW_ONGOING = 0;
	/** ����һ����ȡ����֪ͨ */
	public static final int MODE_SHOW_CANCEL = 1;

	/**
	 * @param context
	 *            ������
	 * @param id
	 *            ֪ͨ��id
	 * @param iconResId
	 *            ֪ͨ��ͼ����Դid
	 * @param title
	 *            ����֪ͨ��ʱ�ı���
	 * @param mode
	 *            �����ִ�еĲ������ͣ�0:Acitivity;1:Service;2:Broadcast��
	 * @param intent
	 *            �����ִ�е� Intent
	 */
	public CustomNotification(Context context, int id, int iconResId,
			String title, int mode, Intent intent) {

		this.context = context;
		this.id = id;
		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(iconResId, title,
				System.currentTimeMillis());
		setPendingIntent(mode, intent);
	}

	/**
	 * ���� PendingIntent
	 * 
	 * @param mode
	 * @param intent
	 */
	public void setPendingIntent(int mode, Intent intent) {

		pendingIntent = SystemHelper
				.getPendingIntent(context, id, mode, intent);
		notification.contentIntent = pendingIntent;
	}

	/**
	 * ����֪ͨ����ʽ
	 ** 
	 * @param remoteViews
	 *            ֪ͨ�Ĳ���
	 * @param mode
	 *            ֪ͨ����ʾģʽ��0������ȡ����1����ȡ����
	 */
	public void updateRemoteViews(RemoteViews remoteViews, int mode) {

		if (mode == MODE_SHOW_ONGOING) {
			notification.flags = Notification.FLAG_ONGOING_EVENT
					| Notification.FLAG_INSISTENT;
		} else {
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_SOUND;
		}
		notification.contentView = remoteViews;
		nm.notify(id, notification);
	}

	/**
	 * ����֪ͨ
	 * 
	 * @param title
	 *            ֪ͨ�ı���
	 * @param text
	 *            ֪ͨ������
	 * @param mode
	 *            ֪ͨ����ʾģʽ��0������ȡ����1����ȡ����
	 */
	public void update(String title, String text, int mode) {

		if (mode == MODE_SHOW_ONGOING) {
			notification.flags = Notification.FLAG_ONGOING_EVENT
					| Notification.FLAG_INSISTENT;
		} else {
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_SOUND;
		}
		notification.setLatestEventInfo(context, title, text, pendingIntent);
		nm.notify(id, notification);
	}

	/**
	 * ȡ��֪ͨ
	 */
	public void cancel() {
		nm.cancel(id);
	}

}
