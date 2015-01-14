package com.g365.download.interfaces;

import android.content.Intent;

/**
 * 是应用状态改变后重绘控件的接口
 * @author nova
 * 日期 2012年12月21日11:52:39
 */
public interface AppOnStateChangeRepainter {

	/**
	 * 重绘接口
	 * 
	 * @param view
	 *            要重绘的控件
	 * @param appWallDownloadInfo
	 *            重绘数据
	 */
	public void repaint(Intent intent);
}
