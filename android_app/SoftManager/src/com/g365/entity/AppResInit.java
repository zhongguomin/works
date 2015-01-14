package com.g365.entity;

import com.g365.softmanager.R;

public class AppResInit {

	/** ���֪ͨ������Դ id */
	public static int r_layout_common_notification;
	/** ���֪ͨͼ�� id */
	public static int r_id_notification_iv_icon;
	/** ���֪ͨ���� id */
	public static int r_id_notification_tv_title;
	/** ���֪ͨ��� id */
	public static int r_id_notification_tv_intro;
	
	/**
	 * ��ʼ�������Դ id
	 */
	public static void initAdRes() {

		r_layout_common_notification = R.layout.common_notification;
		r_id_notification_iv_icon = R.id.notification_iv_icon;
		r_id_notification_tv_title = R.id.notification_tv_title;
		r_id_notification_tv_intro = R.id.notification_tv_intro;
	}
	
	/** ֪ͨ�����ز�����Դ id */
	public static int r_layout_common_notification_download;
	/** ֪ͨ������ͼ�� id */
	public static int r_id_notification_download_iv_icon;
	/** ֪ͨ�����ؽ��� id */
	public static int r_id_notification_download_tv_progress;
	/** ֪ͨ�����ر��� id */
	public static int r_id_notification_download_tv_title;
	/** ֪ͨ�������ٶ� id */
	public static int r_id_notification_download_tv_speed;
	/** ֪ͨ�����ؽ����� id */
	public static int r_id_notification_download_pb_progressbar;
	
	
	/**
	 * ��ʼ��֪ͨ��������Դ
	 */
	public static void initNotificationDownloadRes() {

		r_layout_common_notification_download = R.layout.common_notification_download;
		r_id_notification_download_iv_icon = R.id.notification_download_iv_icon;
		r_id_notification_download_tv_progress = R.id.notification_download_tv_progress;
		r_id_notification_download_tv_title = R.id.notification_download_tv_title;
		r_id_notification_download_tv_speed = R.id.notification_download_tv_speed;
		r_id_notification_download_pb_progressbar = R.id.notification_download_pb_progressbar;
	}
	
	
	/** ���ǽ item�Ĳ�����Դ id */
	public static int r_layout_common_apk_wall_item;
	/** ���ǽ item��ͼ�� id */
	public static int r_id_apk_wall_iv_icon;
	/** ���ǽ item�ı��� id */
	public static int r_id_apk_wall_tv_title;
	/** ���ǽ item���Ǽ� id */
	public static int r_id_apk_wall_tv_star;
	
	/** ���ǽ item�Ĵ�С id */
	public static int r_id_apk_wall_tv_size;
	/** ���ǽ item������ id */
	public static int r_id_apk_wall_tv_download;
	
	/**
	 * ��ʼ�����ǽ item��Դ
	 */
	public static void initAdWallItem() {

		r_layout_common_apk_wall_item = R.layout.softinstall_necessary_listview;
		r_id_apk_wall_iv_icon = R.id.re_appicon;
		r_id_apk_wall_tv_title = R.id.re_appName;
		r_id_apk_wall_tv_star = R.id.restar_ratingbar;
		r_id_apk_wall_tv_size=R.id.re_installnecessaryappSize;
		r_id_apk_wall_tv_download = R.id.apk_wall_tv_download;
	}

	/** ���ǽ��ϸ��Ϣ�Ĳ�����Դ id */
	public static int r_layout_common_apk_wall_detail;
	/** ���ǽ��ϸ��Ϣ�Ĳ��ָ��� id */
	public static int r_id_apk_wall_detail_rl_parent;
	/** ���ǽ��ϸ��Ϣ��ͼ�� id */
	public static int r_id_apk_wall_detail_iv_icon;
	/** ���ǽ��ϸ��Ϣ�ı��� id */
	public static int r_id_apk_wall_detail_tv_title;
	/** ���ǽ��ϸ��Ϣ�����ϵͳ�汾 id */
	public static int r_id_apk_wall_detail_tv_applythefirmware;
	/** ���ǽ��ϸ��Ϣ�Ĺر� id */
	public static int r_id_apk_wall_detail_ll_close;
	/** ���ǽ��ϸ��Ϣ������ id */
	public static int r_id_apk_wall_detail_ib_download;
	/** ���ǽ��ϸ��Ϣ�ļ�� id */
	public static int r_id_apk_wall_detail_tv_intro;
	
	
	/**
	 * ��ʼ�����ǽ��ϸ��Ϣ��Դ
	 */
	public static void initAdWallDetail() {

		r_layout_common_apk_wall_detail = R.layout.common_apk_wall_detail;
		r_id_apk_wall_detail_rl_parent = R.id.apk_wall_detail_rl_parent;
		r_id_apk_wall_detail_iv_icon = R.id.apk_wall_detail_iv_icon;
		r_id_apk_wall_detail_tv_title = R.id.apk_wall_detail_tv_title;
		r_id_apk_wall_detail_tv_applythefirmware = R.id.apk_wall_detail_tv_applythefirmware;
		r_id_apk_wall_detail_ll_close = R.id.apk_wall_detail_ll_close;
		r_id_apk_wall_detail_ib_download = R.id.apk_wall_detail_ib_download;
		r_id_apk_wall_detail_tv_intro = R.id.apk_wall_detail_tv_intro;
	}
	
	/**
	 * ��ʼ��������Դ
	 */
	public static void initAll() {

		initAdRes();
		initNotificationDownloadRes();
		initAdWallItem();
		initAdWallDetail();
	}
	
}
