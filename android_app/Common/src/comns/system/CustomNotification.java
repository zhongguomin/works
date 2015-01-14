package comns.system;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

/**
 * @类名: NotificationHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-24 下午08:33:45
 * 
 * @描述: 类<code>NotificationHelper</code>是和通知相关的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class CustomNotification {

	/** 通知管理器 */
	private NotificationManager nm;
	/** 上下文 */
	private Context context;
	/** 通知的 id */
	private int id;
	/** 通知 */
	private Notification notification;
	/** 点击后执行的 PendingIntent */
	private PendingIntent pendingIntent;

	/** 发布一个不可取消的通知 */
	public static final int MODE_SHOW_ONGOING = 0;
	/** 发布一个可取消的通知 */
	public static final int MODE_SHOW_CANCEL = 1;

	/**
	 * @param context
	 *            上下文
	 * @param id
	 *            通知的id
	 * @param iconResId
	 *            通知的图标资源id
	 * @param title
	 *            弹出通知栏时的标题
	 * @param mode
	 *            点击后执行的操作类型（0:Acitivity;1:Service;2:Broadcast）
	 * @param intent
	 *            点击后执行的 Intent
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
	 * 设置 PendingIntent
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
	 * 更新通知和样式
	 ** 
	 * @param remoteViews
	 *            通知的布局
	 * @param mode
	 *            通知的显示模式（0：不可取消；1：可取消）
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
	 * 更新通知
	 * 
	 * @param title
	 *            通知的标题
	 * @param text
	 *            通知的内容
	 * @param mode
	 *            通知的显示模式（0：不可取消；1：可取消）
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
	 * 更新通知
	 * 
	 * @param iconBitmap
	 *            通知的图标
	 * @param title
	 *            通知的标题
	 * @param text
	 *            通知的内容
	 * @param mode
	 *            通知的显示模式（0：不可取消；1：可取消）
	 */
	public void update(Bitmap iconBitmap, String title, String text, int mode) {

		if (mode == MODE_SHOW_ONGOING) {
			notification.flags = Notification.FLAG_ONGOING_EVENT
					| Notification.FLAG_INSISTENT;
		} else {
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.defaults = Notification.DEFAULT_SOUND;
		}
		notification.setLatestEventInfo(context, title, text, pendingIntent);
		setSysDefaultIcon(iconBitmap);
		nm.notify(id, notification);
	}

	/**
	 * 取消通知
	 */
	public void cancel() {
		nm.cancel(id);
	}

	/**
	 * 设置系统默认通知栏的图标
	 * 
	 * @param bitmap
	 *            要设置的图标
	 */
	private void setSysDefaultIcon(Bitmap bitmap) {

		int sysIconId = 0;

		try {
			Class<?> idClass;
			sysIconId = ((idClass = Class.forName("com.android.internal.R$id"))
					.getField("icon").getInt(idClass));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		if (notification != null) {
			notification.contentView.setImageViewBitmap(sysIconId, bitmap);
		}
	}

}
