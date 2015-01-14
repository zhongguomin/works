package com.g365.softmanager;

import java.util.ArrayList;

import android.content.Context;
import android.content.IntentFilter;

import com.g365.download.AppDownloadManager;
import com.g365.download.FileDownloader;
import com.g365.download.UpdateAllDownloader;
import com.g365.download.interfaces.AppOnStateChangeRepainter;
import com.g365.entity.UpdataInfo;
import com.g365.utils.UpdateAllStateChangeReceiver;

/**
 * 全部更新类
 * 
 * @author nova 日期 2013年1月22日13:57:33
 */
public class SoftAllUpdate1 {

	/** 最大同时进行任务数 */
	private static final int MAX_TASK_NUMS = 3;

	/** 等待更新的任务列表 */
	private ArrayList<UpdataInfo> updateLists = new ArrayList<UpdataInfo>();

	/** 当前任务数 */
	private int currentTaskNum = 0;

	/** 当前任务位置 */
	private int currentPosition = 0;

	/** 上下文对象 */
	private Context context;

	/** 状态改变的广播接收器 */
	private UpdateAllStateChangeReceiver allstateChangeReceiver;

	/** 是否全部暂停 */
	private boolean isAllStop = false;

	private AppOnStateChangeRepainter repainter;

	private static SoftAllUpdate1 softAllUpdate;

	private UpdataInfo updataInfo;

	private SoftAllUpdate1(Context context, ArrayList<UpdataInfo> updateLists) {
		this.context = context;
		this.updateLists.clear();

		for (int i = 0; i < updateLists.size(); i++) {
			updataInfo = updateLists.get(i);
			if (updataInfo.state == FileDownloader.STATE_DOWNLOAD
					|| updataInfo.state == FileDownloader.STATE_RESUME) {
				if (updataInfo.url != null && !updataInfo.url.equals("")
						&& updataInfo.url.length() > 0) {
					this.updateLists.add(updataInfo);
				}
			}
		}

		allstateChangeReceiver = new UpdateAllStateChangeReceiver(null);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(UpdateAllStateChangeReceiver.ACTION_UPDATE_STATE_CHANGED);
		intentFilter
				.addAction(UpdateAllDownloader.ACTION_UPDATEALL_DOWNLOAD_CHANCE);
		context.registerReceiver(allstateChangeReceiver, intentFilter);

	}

	/**
	 * 获取 SoftAllUpdate唯一实例
	 * 
	 * @return
	 */
	public static SoftAllUpdate1 getInstance(Context context,
			ArrayList<UpdataInfo> updateLists) {
		if (softAllUpdate == null) {
			softAllUpdate = new SoftAllUpdate1(context, updateLists);
		}
		return softAllUpdate;

	}

	/**
	 * 获取全部更新需要的大小
	 */
	public long getAllUpdateSize() {

		long allUpdateSize = 0;
		for (int i = 0; i < updateLists.size(); i++) {
			allUpdateSize += updateLists.get(i).app_size;
		}

		return allUpdateSize;
	}

	/**
	 * 全部更新
	 */
	public synchronized void allSoftUpdate() {
		isAllStop = false;
		for (int i = 0; i < updateLists.size(); i++) {
			currentTaskNum = AppDownloadManager.getDownLoadSize();
			if (currentTaskNum < MAX_TASK_NUMS) {
				updataInfo = updateLists.get(i);
				UpdateAllDownloader.getInstance(context, updataInfo)
						.startDownload();
				currentPosition++;
			}
		}
	}

	/**
	 * 停止全部更新
	 */
	public void stopAllUpdate() {

		isAllStop = true;
		for (int i = 0; i < updateLists.size(); i++) {
			UpdateAllDownloader.getInstance(context, updataInfo).stopDownload();
			System.out.println("-----停止全部更新-----" + updateLists.get(i).url);
		}

	}

	/**
	 * 取消注册
	 */
	public void finish() {
		context.unregisterReceiver(allstateChangeReceiver);
	}
}
