package com.g365.softmanager;

import java.util.ArrayList;

import android.content.Context;
import android.content.IntentFilter;

import com.g365.download.FileDownloader;
import com.g365.download.UpdateAllDownloader;
import com.g365.entity.UpdataInfo;
import com.g365.utils.UpdateAllStateChangeReceiver;

/**
 * ȫ��������
 * ����ģʽ
 * @author nova ���� 2013��1��22��13:57:33
 */
public class SoftAllUpdate {

	/** ���ͬʱ���������� */
	private static final int MAX_TASK_NUMS = 3;
	/** �ȴ����µ������б� */
	private ArrayList<UpdataInfo> taskList = new ArrayList<UpdataInfo>();
	/** ��ǰ������ */
	public static int currentTaskNum = 0;
	/** �����Ķ��� */
	private Context context;
	/** ״̬�ı�Ĺ㲥������ */
	private UpdateAllStateChangeReceiver allstateChangeReceiver;
	private IntentFilter intentFilter;
	/** �Ƿ�ȫ����ͣ */
	private boolean isAllStop = false;

	private static SoftAllUpdate softAllUpdate;



	/**
	 *  ���캯��˽�л� ֻ�ܴ���һ��SoftAllUpdateʵ��
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
		// ע��㲥
		context.registerReceiver(allstateChangeReceiver, intentFilter);

	}

	/**
	 * ��ȡ SoftAllUpdateΨһʵ��
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
	 * ȫ������
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
	 * ֹͣȫ������
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
		System.out.println("-------ȫ����ͣ------");
	}

	/**
	 * ȡ��ע��
	 */
	public void unregisterReceiver() {
		context.unregisterReceiver(allstateChangeReceiver);
	}
	
	
	/**
	 * ע��㲥������
	 */
	public void registerReceiver() {
		context.registerReceiver(allstateChangeReceiver, intentFilter);
	}
}
