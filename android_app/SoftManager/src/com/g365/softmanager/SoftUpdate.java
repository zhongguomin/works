package com.g365.softmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.g365.database.AppDownloadDB;
import com.g365.database.SaveAppInfo;
import com.g365.download.AppAdDefaultStateHandler;
import com.g365.download.AppDefaultStateHandler;
import com.g365.download.AppFileDownloader;
import com.g365.download.FileDownloader;
import com.g365.download.UpdateAllDownloader;
import com.g365.download.interfaces.AppOnStateChangeRepainter;
import com.g365.entity.UpdataInfo;
import com.g365.utils.AppInfoHelper;
import com.g365.utils.AsyncImageLoader;
import com.g365.utils.CustomPrint;
import com.g365.utils.MemoryStatus;
import com.g365.utils.SoftManagerPreference;
import com.g365.utils.UpdateAllStateChangeReceiver;
import comns.system.SystemIntent;

public class SoftUpdate extends Activity {

	/** 软件升级的个数 */
	private int softupdateSums = 0;
	/** 全部更新按钮升级状态 */
	public static final int SOFT_UPDATE_UPDATE = 1;
	/** 全部更新按钮暂停状态 */
	public static final int SOFT_UPDATE_STOP = 2;

	/** 全部按钮更新状态常量值 */
	public static int updateState = SOFT_UPDATE_UPDATE;

	private ArrayList<UpdataInfo> updataInfos = new ArrayList<UpdataInfo>();

	/** 适配的listview */
	private ListView listView;
	/** 导入布局 */
	private LayoutInflater vi;
	/** 自定义适配器 */
	private AppBaseAdapter appBaseAdapter;

	/** 多少个软件升级 softupdatesums 需要的流量 softupdateflow */
	private TextView softupdatesums, softupdateflow;

	/** 全部更新 */
	private Button updateAllApp;
	/** 下载状态值 */
	int fileState1;
	Context context;
	private SaveAppInfo saveAppInfo = new SaveAppInfo(this);
	/** 广告状态改变的广播接收器 */
	private UpdateAllStateChangeReceiver allstateChangeReceiver;

   private AppDownloadDB appDownloadDB=new AppDownloadDB(this);


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		
		setContentView(R.layout.soft_update_main);

		listView = (ListView) findViewById(R.id.softupdatelistview);
		LinearLayout linearLayoutback = (LinearLayout) findViewById(R.id.softupdate_back);
		softupdatesums = (TextView) findViewById(R.id.softupdatesums);
		softupdateflow = (TextView) findViewById(R.id.softupdateflow);
		updateAllApp = (Button) findViewById(R.id.soft_update_alldownload);
		context = this;
		allstateChangeReceiver = new UpdateAllStateChangeReceiver(
				new AppOnStateChangeRepainter() {

					public void repaint(Intent intent) {

						// 接受状态的广播
						if (intent
								.getAction()
								.equals(UpdateAllStateChangeReceiver.ACTION_UPDATE_STATE_CHANGED)) {

							UpdataInfo updataInfo = (UpdataInfo) intent
									.getSerializableExtra(UpdateAllStateChangeReceiver.DATA_KEY_ADWALL_STATE_INFO);
							if (updataInfo != null) {
								for (int i = 0; i < updataInfos.size(); i++) {
									if (updataInfos.get(i).url
											.equals(updataInfo.url)) {
										updataInfos.get(i).state = updataInfo.state;
										break;
									}
								}
								appBaseAdapter.notifyDataSetChanged();
							}
							// 接受进度的广播
						} else if (intent
								.getAction()
								.equals(UpdateAllDownloader.ACTION_UPDATEALL_DOWNLOAD_CHANCE)) {
							UpdataInfo updataInfo = (UpdataInfo) intent
									.getSerializableExtra(UpdateAllDownloader.DATA_KEY_ONEADWALL_DOWNLOAD_INFO);
							if (updataInfo != null) {
								for (int j = 0; j < updataInfos.size(); j++) {
									updataInfos.get(j).curFileSize = updataInfo.curFileSize;
									updataInfos.get(j).fileSize = updataInfo.fileSize;
									break;
								}
							}
							appBaseAdapter.notifyDataSetChanged();
						}
					}
				});

		// 创建过滤器，并指定action，使之用于接收同action的广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(UpdateAllStateChangeReceiver.ACTION_UPDATE_STATE_CHANGED);
		intentFilter
				.addAction(UpdateAllDownloader.ACTION_UPDATEALL_DOWNLOAD_CHANCE);

		// 注册广播
		registerReceiver(allstateChangeReceiver, intentFilter);



		linearLayoutback.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		appBaseAdapter = new AppBaseAdapter(SoftUpdate.this);
		listView.setDividerHeight(2);
		if (listView != null) {
			listView.setAdapter(appBaseAdapter);
		}
		// updateAllApp.setText("全部更新(" + updataInfos.size() + ")");
		updateAllApp.setText("全部更新");

		/** 全部更新 */
		updateAllApp.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (updateState == SOFT_UPDATE_UPDATE) {
					updateAllApp
							.setBackgroundResource(R.drawable.soft_update_allstop);
					updateAllApp.setText("全部暂停");

					SoftAllUpdate softAllUpdate2 = SoftAllUpdate.getInstance(
							context, updataInfos);
					softAllUpdate2.allSoftUpdate();

					// 改变状态值
					updateState = SOFT_UPDATE_STOP;

					System.out.println("----------开始全部更新----------");

				} else if (updateState == SOFT_UPDATE_STOP) {
					updateAllApp
							.setBackgroundResource(R.drawable.soft_update_allupdate);
					updateAllApp.setText("全部更新");
					SoftAllUpdate softAllUpdate1 = SoftAllUpdate.getInstance(
							context, updataInfos);
					// 全部暂停
					softAllUpdate1.stopAllUpdate();
					System.out.println("------------停止全部更新-------------");
					// 再次改变状态值
					updateState = SOFT_UPDATE_UPDATE;
				}
			}
		});
		
		
		// 升级总数
		softupdatesums.setText(updataInfos.size() + "");
		// 需要的流量
		softupdateflow.setText(MemoryStatus.formatFileSize(countSums()));
		
		
		//ImageView start_necessary_main=(ImageView) findViewById(R.id.start_necessary_main);
	}

	/**
	 * 应用程序销毁的时候， 要把广播接收器一起销毁。
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(allstateChangeReceiver);
	}

	/**
	 * 自定义适配器
	 */
	public class AppBaseAdapter extends BaseAdapter {
		Context context;

		public AppBaseAdapter(Context context) {
			this.context = context;
			// 得到系统内置的布局填充服务
			vi = (LayoutInflater) context
					.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return updataInfos.size();
		}

		public Object getItem(int position) {
			return updataInfos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			final UpdataInfo updataInfo = updataInfos.get(position);
			final ViewCache viewCache;
			if (null == convertView) {
				convertView = vi.inflate(R.layout.soft_update_listview, null);
				viewCache = new ViewCache();
				viewCache.imageView = (ImageView) convertView
						.findViewById(R.id.icon);
				viewCache.app_download_name = (TextView) convertView
						.findViewById(R.id.appName);
				viewCache.app_download_size = (TextView) convertView
						.findViewById(R.id.appSize);
				viewCache.app_download_version = (TextView) convertView
						.findViewById(R.id.appVersion);

				viewCache.downloadProgressBar = (DownloadProgressBar) convertView
						.findViewById(R.id.downloadProgressBar);
				convertView.setTag(viewCache);
			} else {
				viewCache = (ViewCache) convertView.getTag();
			}
			// 设置图片
			AsyncImageLoader.loadViewImage(updataInfos.get(position)
					.getIconurl(), viewCache.imageView, convertView);
			// 更新名字
			viewCache.app_download_name.setText(updataInfo.getName());
			String convertsize = MemoryStatus.formatFileSize(updataInfo
					.getSize());
			// 更新大小
			viewCache.app_download_size.setText("更新大小:" + convertsize);
			// 更新版本
			viewCache.app_download_version.setText("更新版本:"
					+ updataInfo.getVersion());
			

			final UpdateAllDownloader updateAllDownloader = UpdateAllDownloader
					.getInstance(context, updataInfo);

			// 状态
			int state;
			// 进度值
			int progress;
			// System.out.println("----下载状态----updataInfo.state------"
			// + updataInfo.state + " " + updataInfo.curFileSize);

			// 升级状态
			if (updataInfo.state == FileDownloader.STATE_DOWNLOAD) {
				state = DownloadProgressBar.DOWNLOAD_PROGRESSBAR_UPDATE;
				progress = 0;
				// 安装状态
			} else if (updataInfo.state == FileDownloader.STATE_FINISH) {
				state = DownloadProgressBar.DOWNLOAD_PROGRESSBAR_INSTALL;
				progress = 0;
				// 继续状态
			} else if (updataInfo.state == FileDownloader.STATE_RESUME) {
				state = DownloadProgressBar.DOWNLOAD_PROGRESSBAR_CONTINUE;
				progress = 0;
				// 进度条移动状态
			} else {
				state = DownloadProgressBar.DOWNLOAD_PROGRESSBAR_MOVE;
				int fileSize = updataInfo.fileSize;
				progress = 0;
				if (fileSize != 0) {
					progress = (int) (((float) (updataInfo.curFileSize) / fileSize) * 100);
				}

			}
			viewCache.downloadProgressBar.setMaxProgress(100);
			
			// 升级总数
			softupdatesums.setText(updataInfos.size() + "");
			// 需要的流量
			softupdateflow.setText(MemoryStatus.formatFileSize(countSums()));
			final int pro = progress;
			// 将状态改变成进度条
			viewCache.downloadProgressBar.setState(state, progress);

			viewCache.downloadProgressBar
					.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {

							if (updataInfo.state == FileDownloader.STATE_DOWNLOAD) {
								// 开始下载
								updateAllDownloader.startDownload();
								viewCache.downloadProgressBar
										.setState(
												DownloadProgressBar.DOWNLOAD_PROGRESSBAR_MOVE,
												pro);
							} else if (updataInfo.state == FileDownloader.STATE_FINISH) {
                                     
							if(AppInfoHelper.isApKFileOk(context, 
									AppFileDownloader.getDefaultPath(updataInfo.url))){
								AppAdDefaultStateHandler stateHandler=new AppAdDefaultStateHandler(context);
								// 向数据库添加数据
								if (stateHandler.isNewFile(updataInfo.packagename,
										updataInfo.versioncode)) {
									stateHandler.addNewFile(
											updataInfo.url,
											updataInfo.packagename,
											updataInfo.versioncode,
											updataInfo.app_id,
											AppAdDefaultStateHandler.STATE_FINISH);
								}
								// 安装
								SystemIntent.installApk(context, FileDownloader
										.getDefaultPath(updataInfo.getUrl()));
								viewCache.downloadProgressBar
										.setState(
												DownloadProgressBar.DOWNLOAD_PROGRESSBAR_MOVE,
												pro);
								
								
							}else {
								updataInfo.state = FileDownloader.STATE_DOWNLOAD;
								System.out.println("————————————————包名是"+updataInfo.url);
								appDownloadDB.deleteUpdateFormPackageName(updataInfo.url);
								notifyDataSetChanged();
								CustomPrint.show(context, "文件已不存在，请重新下载");
								
							}
							softupdateSums++;
							SoftManagerPreference.saveSoftUpdate(
									SoftUpdate.this, softupdateSums);
							} else if (updataInfo.state == FileDownloader.STATE_PAUSE) {
								// 暂停下载
								updateAllDownloader.stopDownload();
								viewCache.downloadProgressBar
										.setState(
												DownloadProgressBar.DOWNLOAD_PROGRESSBAR_CONTINUE,
												pro);
							} else if (updataInfo.state == FileDownloader.STATE_RESUME) {
								// 再次下载
								updateAllDownloader.startDownload();
								viewCache.downloadProgressBar
										.setState(
												DownloadProgressBar.DOWNLOAD_PROGRESSBAR_MOVE,
												pro);
							}
						}
					});

			return convertView;
		}

		/** 缓存类 */
		private final class ViewCache {
			/** 应用图标 */
			ImageView imageView;
			/** 应用名字 */
			TextView app_download_name;
			/** 应用大小 */
			TextView app_download_size;
			/** 应用版本 */
			TextView app_download_version;
			/** 显示继续 */
			TextView continue_textview;
			/** 应用进度条 三种状态 升级 进度条 安装 */
			DownloadProgressBar downloadProgressBar;
		}

	}

	/**
	 * 流量统计
	 */
	public Long countSums() {
		long flow = 0;
		for (int i = 0; i < updataInfos.size(); i++) {
			flow += updataInfos.get(i).getSize();
		}
		return flow;
	}



	@Override
	protected void onResume() {
		super.onResume();
        //删除数据后再次查询 适配器再次刷新
		updataInfos.clear();
		updataInfos = saveAppInfo.getScrollData();
		
		//读取下载完后的状态
		for (int i = 0; i < updataInfos.size(); i++) {
			updataInfos.get(i).state = getAdState(
					SoftUpdate.this,
					updataInfos.get(i).url);
		}
		appBaseAdapter.notifyDataSetChanged();

	}

	/**
	 * 读取已经下载的状态
	 * 
	 * @param context
	 * @param fileUrl
	 * @return
	 */
	public static int getAdState(Context context, String fileUrl) {

		int state = 0;

		AppDefaultStateHandler dHandler = new AppDefaultStateHandler(context);
		AppAdDefaultStateHandler aHandler = new AppAdDefaultStateHandler(
				context);

		state = dHandler.getDownloadState(fileUrl);
		if (state == AppFileDownloader.STATE_FINISH) {
			state = aHandler.getFileSate(fileUrl);
		}

		return state;
	}
}
