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

	/** ��������ĸ��� */
	private int softupdateSums = 0;
	/** ȫ�����°�ť����״̬ */
	public static final int SOFT_UPDATE_UPDATE = 1;
	/** ȫ�����°�ť��ͣ״̬ */
	public static final int SOFT_UPDATE_STOP = 2;

	/** ȫ����ť����״̬����ֵ */
	public static int updateState = SOFT_UPDATE_UPDATE;

	private ArrayList<UpdataInfo> updataInfos = new ArrayList<UpdataInfo>();

	/** �����listview */
	private ListView listView;
	/** ���벼�� */
	private LayoutInflater vi;
	/** �Զ��������� */
	private AppBaseAdapter appBaseAdapter;

	/** ���ٸ�������� softupdatesums ��Ҫ������ softupdateflow */
	private TextView softupdatesums, softupdateflow;

	/** ȫ������ */
	private Button updateAllApp;
	/** ����״ֵ̬ */
	int fileState1;
	Context context;
	private SaveAppInfo saveAppInfo = new SaveAppInfo(this);
	/** ���״̬�ı�Ĺ㲥������ */
	private UpdateAllStateChangeReceiver allstateChangeReceiver;

   private AppDownloadDB appDownloadDB=new AppDownloadDB(this);


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ��������
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

						// ����״̬�Ĺ㲥
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
							// ���ܽ��ȵĹ㲥
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

		// ��������������ָ��action��ʹ֮���ڽ���ͬaction�Ĺ㲥
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(UpdateAllStateChangeReceiver.ACTION_UPDATE_STATE_CHANGED);
		intentFilter
				.addAction(UpdateAllDownloader.ACTION_UPDATEALL_DOWNLOAD_CHANCE);

		// ע��㲥
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
		// updateAllApp.setText("ȫ������(" + updataInfos.size() + ")");
		updateAllApp.setText("ȫ������");

		/** ȫ������ */
		updateAllApp.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (updateState == SOFT_UPDATE_UPDATE) {
					updateAllApp
							.setBackgroundResource(R.drawable.soft_update_allstop);
					updateAllApp.setText("ȫ����ͣ");

					SoftAllUpdate softAllUpdate2 = SoftAllUpdate.getInstance(
							context, updataInfos);
					softAllUpdate2.allSoftUpdate();

					// �ı�״ֵ̬
					updateState = SOFT_UPDATE_STOP;

					System.out.println("----------��ʼȫ������----------");

				} else if (updateState == SOFT_UPDATE_STOP) {
					updateAllApp
							.setBackgroundResource(R.drawable.soft_update_allupdate);
					updateAllApp.setText("ȫ������");
					SoftAllUpdate softAllUpdate1 = SoftAllUpdate.getInstance(
							context, updataInfos);
					// ȫ����ͣ
					softAllUpdate1.stopAllUpdate();
					System.out.println("------------ֹͣȫ������-------------");
					// �ٴθı�״ֵ̬
					updateState = SOFT_UPDATE_UPDATE;
				}
			}
		});
		
		
		// ��������
		softupdatesums.setText(updataInfos.size() + "");
		// ��Ҫ������
		softupdateflow.setText(MemoryStatus.formatFileSize(countSums()));
		
		
		//ImageView start_necessary_main=(ImageView) findViewById(R.id.start_necessary_main);
	}

	/**
	 * Ӧ�ó������ٵ�ʱ�� Ҫ�ѹ㲥������һ�����١�
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(allstateChangeReceiver);
	}

	/**
	 * �Զ���������
	 */
	public class AppBaseAdapter extends BaseAdapter {
		Context context;

		public AppBaseAdapter(Context context) {
			this.context = context;
			// �õ�ϵͳ���õĲ���������
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
			// ����ͼƬ
			AsyncImageLoader.loadViewImage(updataInfos.get(position)
					.getIconurl(), viewCache.imageView, convertView);
			// ��������
			viewCache.app_download_name.setText(updataInfo.getName());
			String convertsize = MemoryStatus.formatFileSize(updataInfo
					.getSize());
			// ���´�С
			viewCache.app_download_size.setText("���´�С:" + convertsize);
			// ���°汾
			viewCache.app_download_version.setText("���°汾:"
					+ updataInfo.getVersion());
			

			final UpdateAllDownloader updateAllDownloader = UpdateAllDownloader
					.getInstance(context, updataInfo);

			// ״̬
			int state;
			// ����ֵ
			int progress;
			// System.out.println("----����״̬----updataInfo.state------"
			// + updataInfo.state + " " + updataInfo.curFileSize);

			// ����״̬
			if (updataInfo.state == FileDownloader.STATE_DOWNLOAD) {
				state = DownloadProgressBar.DOWNLOAD_PROGRESSBAR_UPDATE;
				progress = 0;
				// ��װ״̬
			} else if (updataInfo.state == FileDownloader.STATE_FINISH) {
				state = DownloadProgressBar.DOWNLOAD_PROGRESSBAR_INSTALL;
				progress = 0;
				// ����״̬
			} else if (updataInfo.state == FileDownloader.STATE_RESUME) {
				state = DownloadProgressBar.DOWNLOAD_PROGRESSBAR_CONTINUE;
				progress = 0;
				// �������ƶ�״̬
			} else {
				state = DownloadProgressBar.DOWNLOAD_PROGRESSBAR_MOVE;
				int fileSize = updataInfo.fileSize;
				progress = 0;
				if (fileSize != 0) {
					progress = (int) (((float) (updataInfo.curFileSize) / fileSize) * 100);
				}

			}
			viewCache.downloadProgressBar.setMaxProgress(100);
			
			// ��������
			softupdatesums.setText(updataInfos.size() + "");
			// ��Ҫ������
			softupdateflow.setText(MemoryStatus.formatFileSize(countSums()));
			final int pro = progress;
			// ��״̬�ı�ɽ�����
			viewCache.downloadProgressBar.setState(state, progress);

			viewCache.downloadProgressBar
					.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {

							if (updataInfo.state == FileDownloader.STATE_DOWNLOAD) {
								// ��ʼ����
								updateAllDownloader.startDownload();
								viewCache.downloadProgressBar
										.setState(
												DownloadProgressBar.DOWNLOAD_PROGRESSBAR_MOVE,
												pro);
							} else if (updataInfo.state == FileDownloader.STATE_FINISH) {
                                     
							if(AppInfoHelper.isApKFileOk(context, 
									AppFileDownloader.getDefaultPath(updataInfo.url))){
								AppAdDefaultStateHandler stateHandler=new AppAdDefaultStateHandler(context);
								// �����ݿ��������
								if (stateHandler.isNewFile(updataInfo.packagename,
										updataInfo.versioncode)) {
									stateHandler.addNewFile(
											updataInfo.url,
											updataInfo.packagename,
											updataInfo.versioncode,
											updataInfo.app_id,
											AppAdDefaultStateHandler.STATE_FINISH);
								}
								// ��װ
								SystemIntent.installApk(context, FileDownloader
										.getDefaultPath(updataInfo.getUrl()));
								viewCache.downloadProgressBar
										.setState(
												DownloadProgressBar.DOWNLOAD_PROGRESSBAR_MOVE,
												pro);
								
								
							}else {
								updataInfo.state = FileDownloader.STATE_DOWNLOAD;
								System.out.println("��������������������������������������"+updataInfo.url);
								appDownloadDB.deleteUpdateFormPackageName(updataInfo.url);
								notifyDataSetChanged();
								CustomPrint.show(context, "�ļ��Ѳ����ڣ�����������");
								
							}
							softupdateSums++;
							SoftManagerPreference.saveSoftUpdate(
									SoftUpdate.this, softupdateSums);
							} else if (updataInfo.state == FileDownloader.STATE_PAUSE) {
								// ��ͣ����
								updateAllDownloader.stopDownload();
								viewCache.downloadProgressBar
										.setState(
												DownloadProgressBar.DOWNLOAD_PROGRESSBAR_CONTINUE,
												pro);
							} else if (updataInfo.state == FileDownloader.STATE_RESUME) {
								// �ٴ�����
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

		/** ������ */
		private final class ViewCache {
			/** Ӧ��ͼ�� */
			ImageView imageView;
			/** Ӧ������ */
			TextView app_download_name;
			/** Ӧ�ô�С */
			TextView app_download_size;
			/** Ӧ�ð汾 */
			TextView app_download_version;
			/** ��ʾ���� */
			TextView continue_textview;
			/** Ӧ�ý����� ����״̬ ���� ������ ��װ */
			DownloadProgressBar downloadProgressBar;
		}

	}

	/**
	 * ����ͳ��
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
        //ɾ�����ݺ��ٴβ�ѯ �������ٴ�ˢ��
		updataInfos.clear();
		updataInfos = saveAppInfo.getScrollData();
		
		//��ȡ��������״̬
		for (int i = 0; i < updataInfos.size(); i++) {
			updataInfos.get(i).state = getAdState(
					SoftUpdate.this,
					updataInfos.get(i).url);
		}
		appBaseAdapter.notifyDataSetChanged();

	}

	/**
	 * ��ȡ�Ѿ����ص�״̬
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
