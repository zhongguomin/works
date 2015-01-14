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
 * ȫ��������
 * 
 * @author nova ���� 2013��1��22��13:57:33
 */
public class SoftAllUpdate1 {

	/** ���ͬʱ���������� */
	private static final int MAX_TASK_NUMS = 3;

	/** �ȴ����µ������б� */
	private ArrayList<UpdataInfo> updateLists = new ArrayList<UpdataInfo>();

	/** ��ǰ������ */
	private int currentTaskNum = 0;

	/** ��ǰ����λ�� */
	private int currentPosition = 0;

	/** �����Ķ��� */
	private Context context;

	/** ״̬�ı�Ĺ㲥������ */
	private UpdateAllStateChangeReceiver allstateChangeReceiver;

	/** �Ƿ�ȫ����ͣ */
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
	 * ��ȡ SoftAllUpdateΨһʵ��
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
	 * ��ȡȫ��������Ҫ�Ĵ�С
	 */
	public long getAllUpdateSize() {

		long allUpdateSize = 0;
		for (int i = 0; i < updateLists.size(); i++) {
			allUpdateSize += updateLists.get(i).app_size;
		}

		return allUpdateSize;
	}

	/**
	 * ȫ������
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
	 * ֹͣȫ������
	 */
	public void stopAllUpdate() {

		isAllStop = true;
		for (int i = 0; i < updateLists.size(); i++) {
			UpdateAllDownloader.getInstance(context, updataInfo).stopDownload();
			System.out.println("-----ֹͣȫ������-----" + updateLists.get(i).url);
		}

	}

	/**
	 * ȡ��ע��
	 */
	public void finish() {
		context.unregisterReceiver(allstateChangeReceiver);
	}
}
