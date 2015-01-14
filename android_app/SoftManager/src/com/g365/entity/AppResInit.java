package com.g365.entity;

import com.g365.softmanager.R;

public class AppResInit {

	/** 广告通知布局资源 id */
	public static int r_layout_common_notification;
	/** 广告通知图标 id */
	public static int r_id_notification_iv_icon;
	/** 广告通知标题 id */
	public static int r_id_notification_tv_title;
	/** 广告通知简介 id */
	public static int r_id_notification_tv_intro;
	
	/**
	 * 初始化广告资源 id
	 */
	public static void initAdRes() {

		r_layout_common_notification = R.layout.common_notification;
		r_id_notification_iv_icon = R.id.notification_iv_icon;
		r_id_notification_tv_title = R.id.notification_tv_title;
		r_id_notification_tv_intro = R.id.notification_tv_intro;
	}
	
	/** 通知栏下载布局资源 id */
	public static int r_layout_common_notification_download;
	/** 通知栏下载图标 id */
	public static int r_id_notification_download_iv_icon;
	/** 通知栏下载进度 id */
	public static int r_id_notification_download_tv_progress;
	/** 通知栏下载标题 id */
	public static int r_id_notification_download_tv_title;
	/** 通知栏下载速度 id */
	public static int r_id_notification_download_tv_speed;
	/** 通知栏下载进度条 id */
	public static int r_id_notification_download_pb_progressbar;
	
	
	/**
	 * 初始化通知栏下载资源
	 */
	public static void initNotificationDownloadRes() {

		r_layout_common_notification_download = R.layout.common_notification_download;
		r_id_notification_download_iv_icon = R.id.notification_download_iv_icon;
		r_id_notification_download_tv_progress = R.id.notification_download_tv_progress;
		r_id_notification_download_tv_title = R.id.notification_download_tv_title;
		r_id_notification_download_tv_speed = R.id.notification_download_tv_speed;
		r_id_notification_download_pb_progressbar = R.id.notification_download_pb_progressbar;
	}
	
	
	/** 广告墙 item的布局资源 id */
	public static int r_layout_common_apk_wall_item;
	/** 广告墙 item的图标 id */
	public static int r_id_apk_wall_iv_icon;
	/** 广告墙 item的标题 id */
	public static int r_id_apk_wall_tv_title;
	/** 广告墙 item的星级 id */
	public static int r_id_apk_wall_tv_star;
	
	/** 广告墙 item的大小 id */
	public static int r_id_apk_wall_tv_size;
	/** 广告墙 item的下载 id */
	public static int r_id_apk_wall_tv_download;
	
	/**
	 * 初始化广告墙 item资源
	 */
	public static void initAdWallItem() {

		r_layout_common_apk_wall_item = R.layout.softinstall_necessary_listview;
		r_id_apk_wall_iv_icon = R.id.re_appicon;
		r_id_apk_wall_tv_title = R.id.re_appName;
		r_id_apk_wall_tv_star = R.id.restar_ratingbar;
		r_id_apk_wall_tv_size=R.id.re_installnecessaryappSize;
		r_id_apk_wall_tv_download = R.id.apk_wall_tv_download;
	}

	/** 广告墙详细信息的布局资源 id */
	public static int r_layout_common_apk_wall_detail;
	/** 广告墙详细信息的布局父类 id */
	public static int r_id_apk_wall_detail_rl_parent;
	/** 广告墙详细信息的图标 id */
	public static int r_id_apk_wall_detail_iv_icon;
	/** 广告墙详细信息的标题 id */
	public static int r_id_apk_wall_detail_tv_title;
	/** 广告墙详细信息的最低系统版本 id */
	public static int r_id_apk_wall_detail_tv_applythefirmware;
	/** 广告墙详细信息的关闭 id */
	public static int r_id_apk_wall_detail_ll_close;
	/** 广告墙详细信息的下载 id */
	public static int r_id_apk_wall_detail_ib_download;
	/** 广告墙详细信息的简介 id */
	public static int r_id_apk_wall_detail_tv_intro;
	
	
	/**
	 * 初始化广告墙详细信息资源
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
	 * 初始化所有资源
	 */
	public static void initAll() {

		initAdRes();
		initNotificationDownloadRes();
		initAdWallItem();
		initAdWallDetail();
	}
	
}
