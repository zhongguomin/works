package com.g365.softmanager;

import java.util.ArrayList;

import android.content.Context;
import android.content.IntentFilter;

import com.g365.download.FileDownloader;
import com.g365.download.UpdateAllDownloader;
import com.g365.entity.UpdataInfo;
import com.g365.utils.UpdateAllStateChangeReceiver;

/**
 * 全部更新类
 * 单例模式
 * @author nova 日期 2013年1月22日13:57:33
 */
public class SoftAllUpdate {

	/** 最大同时进行任务数 */
	private static final int MAX_TASK_NUMS = 3;
	/** 等待更新的任务列表 */
	private ArrayList<UpdataInfo> taskList = new ArrayList<UpdataInfo>();
	/** 当前任务数 */
	public static int currentTaskNum = 0;
	/** 上下文对象 */
	private Context context;
	/** 状态改变的广播接收器 */
	private UpdateAllStateChangeReceiver allstateChangeReceiver;
	private IntentFilter intentFilter;
	/** 是否全部暂停 */
	private boolean isAllStop = false;

	private static SoftAllUpdate softAllUpdate;



	/**
	 *  构造函数私有化 只能创建一个SoftAllUpdate实例
	 * @param context
	 * @param updateLists
	 */
	private SoftAllUpdate(Context context, ArrayList<UpdataInfo> updateLists) {
		this.context = context;
		taskList = updateLists;
		System.out.println("-----updateLists-----"+updateLists);
		allstateChangeReceiver = new UpdateAllStateChangeReceiver();
		intentFilter = new IntentFilter();
		intentFilter
				.addAction(UpdateAllStateChangeReceiver.ACTION_UPDATE_STATE_CHANGED);
		intentFilter
				.addAction(UpdateAllDownloader.ACTION_UPDATEALL_DOWNLOAD_CHANCE);
		// 注册广播
		context.registerReceiver(allstateChangeReceiver, intentFilter);

	}

	/**
	 * 获取 SoftAllUpdate唯一实例
	 * 
	 * @return
	 */
	public static SoftAllUpdate getInstance(Context context,
			ArrayList<UpdataInfo> updateLists) {
		if (softAllUpdate == null) {
			softAllUpdate = new SoftAllUpdate(context, updateLists);
		}
		return softAllUpdate;
	}

	/**
	 * 全部更新
	 */
	public synchronized void allSoftUpdate() {
		isAllStop = false;
		for (int i = 0; i < taskList.size(); i++) {
			if (SoftAllUpdate.currentTaskNum >= MAX_TASK_NUMS) {
				return;
			}
			UpdataInfo updataInfo = taskList.get(i);
			if (updataInfo.state == FileDownloader.STATE_DOWNLOAD||
					updataInfo.state == FileDownloader.STATE_RESUME) {
				UpdateAllDownloader.getInstance(context, updataInfo).startDownload();
				SoftAllUpdate.currentTaskNum++;
			}
		}
	}

	/**
	 * 停止全部更新
	 */
	public void stopAllUpdate() {

		for (int i = 0; i < taskList.size(); i++) {
			UpdataInfo updataInfo = taskList.get(i);
			if (updataInfo.state ==  FileDownloader.STATE_PAUSE) {
				UpdateAllDownloader updateAllDownloader = UpdateAllDownloader
						.getInstance(context, updataInfo);
				if (updateAllDownloader != null) {
					updateAllDownloader.stopDownload();
				}
			}
			if (updataInfo.state == FileDownloader.STATE_PAUSE) {
				updataInfo.state = FileDownloader.STATE_DOWNLOAD;
			}
		}
		isAllStop = true;
		currentTaskNum = 0;
		System.out.println("-------全部暂停------");
	}

	/**
	 * 取消注册
	 */
	public void unregisterReceiver() {
		context.unregisterReceiver(allstateChangeReceiver);
	}
	
	
	/**
	 * 注册广播接收器
	 */
	public void registerReceiver() {
		context.registerReceiver(allstateChangeReceiver, intentFilter);
	}
}
