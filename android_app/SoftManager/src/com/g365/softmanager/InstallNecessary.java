package com.g365.softmanager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.g365.download.AppAdDefaultStateHandler;
import com.g365.download.AppDefaultStateHandler;
import com.g365.download.AppFileDownloader;
import com.g365.download.AppOneAdDownloader;
import com.g365.download.interfaces.AppOnStateChangeRepainter;
import com.g365.entity.AppPage;
import com.g365.entity.AppResInit;
import com.g365.entity.AppWallDownloadInfo;
import com.g365.entity.ImageInfo;
import com.g365.utils.AppInfoHelper;
import com.g365.utils.AppStateChangeReceiver;
import com.g365.utils.ApplyAdWallHelper;
import com.g365.utils.ApplyAdWallHelper.OnApplyListener;
import com.g365.utils.AsyncImageLoader;
import com.g365.utils.CustomPrint;
import com.g365.utils.MemoryStatus;
import com.g365.utils.PullUtils;
import com.g365.utils.SoftManagerPreference;
import com.g365.utils.UrlHelper;
import com.lllfy.newad.core.AdHelper;

import comns.system.SystemIntent;

public class InstallNecessary extends Activity {

	/** �򿪹��ǽ��Ҫִ�����ز������ݹ����������ع����Ϣ�ֶ� */
	public static final String DATA_KEY_ADWALL_INFO = "appwalldownloadinfo";

	/** ListView�ײ�View */
	private View moreView;
	/** ���ظ��� */
	private Button loadmore;
	/** ������ */
	private ProgressBar pg;

	/** ���ظ����handler */
	private Handler loadmorehandler;

	/** ��ʼ�������һҳ */
	private int pageNowsoft = 1;
	/** ��ʼ����Ϸ��һҳ */
	private int pageNowgame = 1;
	/** ������Ϸ������� */
	private int differencetype = 1;

	private AppPage appPages1;

	/**
	 * ���벼��
	 */
	private LayoutInflater li;
	/** ���� */
	private LinearLayout installback;
	/** ͼƬlist */
	private List<ImageInfo> getImages = new ArrayList<ImageInfo>();
	/** app��Ϣlist */
	private List<AppWallDownloadInfo> getApplistInfos = new ArrayList<AppWallDownloadInfo>();

	/** �Զ��������� */
	private ApplistAdapter applistAdapter;
	/** Ҫ�����list */
	private ListView app_listView;

	/** ������ */
	private TextView click_software;
	/** �����Ϸ */
	private TextView click_softgame;
	/**
	 * android-support-v4�еĻ������
	 */
	private ViewPager viewPager;
	/**
	 * ������ͼƬ����
	 */
	private List<ImageView> imageViews = new ArrayList<ImageView>();
	/**
	 * ͼƬ�������ĵ��Ǹ���
	 */
	private List<View> dots;
	/**
	 * ��ǰͼƬ��������
	 */
	private int currentItem = 0;

	/**
	 * һ��ExecutorService,���Ե���������������һ���������ӳٻ���ִ�С�
	 */
	private ScheduledExecutorService scheduledExecutorService;

	/** �����Ķ���s */
	private Context context;

	private AppStateChangeReceiver stateChangeReceiver;

	/** ���ش��� */
	private int downloadsum = 0;
	/** ��װ���� */
	private int installsum = 0;

	/**
	 * �������ڼ��ز�����handler
	 */
	private Handler disposalHandler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			// ��ȡhandler���͹�������Ϣ��ӵ������list��
			getApplistInfos.addAll((ArrayList<AppWallDownloadInfo>) msg.obj);

			/** ����������Ŀ */
			if (pageNowsoft == msg.arg1) {
				// �������ȫ�������� ��moreView remove��
				app_listView.removeFooterView(moreView);
				Toast.makeText(InstallNecessary.this, "����ȫ��������ɣ�û�и������ݣ�",
						Toast.LENGTH_LONG).show();
			}
			/** ��Ϸ�������Ŀ */
			if (pageNowgame == msg.arg2) {
				// if(pageNowgame<msg.arg2){
				// app_listView.addFooterView(moreView);
				// }
				// ��Ϸ����ȫ�������� ��moreView remove��
				app_listView.removeFooterView(moreView);
				Toast.makeText(InstallNecessary.this, "����ȫ��������ɣ�û�и������ݣ�",
						Toast.LENGTH_LONG).show();
			}
			// �������ı�
			applistAdapter.notifyDataSetChanged();
			super.handleMessage(msg);
		};
	};
	/**
	 * �л���ǰ��ʾ��ͼƬ
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem((int) msg.what);
			AsyncImageLoader.loadViewImage(getImages.get((int) msg.what)
					.getApp_imageurl(), imageViews.get((int) msg.what),
					viewPager);

		};
	};

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_install_necessary);
		// ���ҿؼ�
		click_software = (TextView) findViewById(R.id.necessary_click_software);
		click_softgame = (TextView) findViewById(R.id.necessary_click_softgame);

		/** ��ʼ����ɫ��ֵ */
		//click_software.setTextColor(Color.RED);
		//click_softgame.setTextColor(Color.BLACK);
		
		/** ��ʼ����ɫ��ֵ */
		click_software.setTextColor(getResources().getColor(R.color.appmovebule));
		click_softgame.setTextColor(getResources().getColor(R.color.appmoveblack));
		// ��ʼ��
		context = this;

		stateChangeReceiver = new AppStateChangeReceiver(
				new AppOnStateChangeRepainter() {

					public void repaint(Intent intent) {

						AppWallDownloadInfo appWallDownloadInfo = (AppWallDownloadInfo) intent
								.getSerializableExtra(AppStateChangeReceiver.DATA_KEY_ADWALL_STATE_INFO);

						if (appWallDownloadInfo != null) {
							for (int i = 0; i < getApplistInfos.size(); i++) {
								if (getApplistInfos.get(i).app_download
										.equals(appWallDownloadInfo.app_download)) {
									getApplistInfos.get(i).state = appWallDownloadInfo.state;
									break;
								}
							}
							applistAdapter.notifyDataSetChanged();
						}
					}
				});
		registerReceiver(stateChangeReceiver, new IntentFilter(
				AppStateChangeReceiver.ACTION_AD_STATE_CHANGED));

		imageViews.clear();
		for (int i = 0; i < 5; i++) {
			ImageView imageView = new ImageView(InstallNecessary.this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		viewPager = (ViewPager) findViewById(R.id.vp);
		// �����listview
		app_listView = (ListView) findViewById(R.id.installnecessarylistview);
		// ʵ�����ײ�����
		moreView = getLayoutInflater().inflate(
				R.layout.install_necessary_moredata, null);
		loadmore = (Button) moreView.findViewById(R.id.bt_load);
		pg = (ProgressBar) moreView.findViewById(R.id.pg);

		loadmorehandler = new Handler();

		/** ���ϵײ�View��ע��Ҫ����setAdapter����ǰ */
		app_listView.addFooterView(moreView);

		applistAdapter = new ApplistAdapter(InstallNecessary.this);
		app_listView.setDividerHeight(2);
		if (app_listView != null) {
			app_listView.setAdapter(applistAdapter);
		}

		new ApplyAdWallHelper(this, new OnApplyListener() {

			public void readLocalCacheAppInfo(Map<String, Object> maplocal) {
				Message msg = handler1.obtainMessage();
				msg.obj = maplocal;
				msg.sendToTarget();
			}

			public void readOnlineNewDataAppInfo(Map<String, Object> mapOnline) {
				Message msg = handler1.obtainMessage();
				msg.obj = mapOnline;
				msg.sendToTarget();
			}

		}, 1).showInfoList();

		/** ���СԲȦ */
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));
		dots.add(findViewById(R.id.v_dot3));
		dots.add(findViewById(R.id.v_dot4));

		/**
		 * �������ViewPagerҳ���������
		 */
		viewPager.setAdapter(new MyAdapter());
		/**
		 * ����һ������������ViewPager�е�ҳ��ı�ʱ����
		 */
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		installback = (LinearLayout) findViewById(R.id.installnecessary);
		// ����
		installback.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Intent intent = new Intent(InstallNecessary.this,
				// SoftManagerActivity.class);
				// startActivity(intent);
				finish();
			}
		});

		AppWallDownloadInfo appWallDownloadInfo = (AppWallDownloadInfo) getIntent()
				.getSerializableExtra(DATA_KEY_ADWALL_INFO);

		if (appWallDownloadInfo != null) {
			AppOneAdDownloader.getInstance(context, appWallDownloadInfo)
					.startDownload();
		}

		/** ��������ť */
		click_software.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// ���ñ����ı�
				LinearLayout ll_softgame = (LinearLayout) findViewById(R.id.common_necessary_softgame);
				ll_softgame
						.setBackgroundResource(R.drawable.install_necessary_softbutton);
				// ��������ı�
				//click_software.setTextColor(Color.RED);
				//click_softgame.setTextColor(Color.BLACK);
				
				click_softgame.setTextColor(getResources().getColor(R.color.appmoveblack));
				click_software.setTextColor(getResources().getColor(R.color.appmovebule));
				differencetype = 1;

				new ApplyAdWallHelper(context, new OnApplyListener() {

					public void readLocalCacheAppInfo(
							Map<String, Object> maplocal) {

						Message msg = handler1.obtainMessage();
						msg.obj = maplocal;
						msg.sendToTarget();
					}

					public void readOnlineNewDataAppInfo(
							Map<String, Object> mapOnline) {
						Message msg = handler1.obtainMessage();
						msg.obj = mapOnline;
						msg.sendToTarget();
					}

				}, 1).showInfoList();

				stateChangeReceiver = new AppStateChangeReceiver(
						new AppOnStateChangeRepainter() {

							public void repaint(Intent intent) {

								AppWallDownloadInfo appWallDownloadInfo = (AppWallDownloadInfo) intent
										.getSerializableExtra(AppStateChangeReceiver.DATA_KEY_ADWALL_STATE_INFO);

								if (appWallDownloadInfo != null) {
									for (int i = 0; i < getApplistInfos.size(); i++) {
										if (getApplistInfos.get(i).app_download
												.equals(appWallDownloadInfo.app_download)) {
											getApplistInfos.get(i).state = appWallDownloadInfo.state;
											break;
										}
									}
									applistAdapter.notifyDataSetChanged();
								}
							}
						});
				registerReceiver(stateChangeReceiver, new IntentFilter(
						AppStateChangeReceiver.ACTION_AD_STATE_CHANGED));

				applistAdapter = new ApplistAdapter(InstallNecessary.this);
				app_listView.setDividerHeight(2);
				if (app_listView != null) {
					app_listView.setAdapter(applistAdapter);
				}
			}
		});

		/** �����Ϸ��ť */
		click_softgame.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				LinearLayout ll_softgame = (LinearLayout) findViewById(R.id.common_necessary_softgame);
				// ���ñ����ı�
				ll_softgame
						.setBackgroundResource(R.drawable.install_necessary_gamebutton);
				differencetype = 2;

				// ��������ı�
				//click_softgame.setTextColor(Color.RED);
				//click_software.setTextColor(Color.BLACK);
				
				click_softgame.setTextColor(getResources().getColor(R.color.appmovebule));
				click_software.setTextColor(getResources().getColor(R.color.appmoveblack));

				new ApplyAdWallHelper(context, new OnApplyListener() {

					public void readLocalCacheAppInfo(
							Map<String, Object> maplocal) {

						Message msg = handler1.obtainMessage();
						msg.obj = maplocal;
						msg.sendToTarget();
					}

					public void readOnlineNewDataAppInfo(
							Map<String, Object> mapOnline) {
						Message msg = handler1.obtainMessage();
						msg.obj = mapOnline;
						msg.sendToTarget();
					}

				}, 2).showInfoList1();

				stateChangeReceiver = new AppStateChangeReceiver(
						new AppOnStateChangeRepainter() {

							public void repaint(Intent intent) {

								AppWallDownloadInfo appWallDownloadInfo = (AppWallDownloadInfo) intent
										.getSerializableExtra(AppStateChangeReceiver.DATA_KEY_ADWALL_STATE_INFO);

								if (appWallDownloadInfo != null) {
									for (int i = 0; i < getApplistInfos.size(); i++) {
										if (getApplistInfos.get(i).app_download
												.equals(appWallDownloadInfo.app_download)) {
											getApplistInfos.get(i).state = appWallDownloadInfo.state;
											break;
										}
									}
									applistAdapter.notifyDataSetChanged();
								}
							}
						});
				registerReceiver(stateChangeReceiver, new IntentFilter(
						AppStateChangeReceiver.ACTION_AD_STATE_CHANGED));

				applistAdapter = new ApplistAdapter(InstallNecessary.this);
				app_listView.setDividerHeight(2);
				if (app_listView != null) {
					app_listView.setAdapter(applistAdapter);
				}

				AppWallDownloadInfo appWallDownloadInfo = (AppWallDownloadInfo) getIntent()
						.getSerializableExtra(DATA_KEY_ADWALL_INFO);

				if (appWallDownloadInfo != null) {
					AppOneAdDownloader
							.getInstance(context, appWallDownloadInfo)
							.startDownload();
				}
			}
		});

		loadmore.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				pg.setVisibility(View.VISIBLE);// ���������ɼ�
				loadmore.setVisibility(View.GONE);// ��ť���ɼ�
				loadmorehandler.postDelayed(new Runnable() {

					public void run() {
						pageNowsoft++;
						pageNowgame++;
						loadMoreDate(differencetype, pageNowsoft);// ���ظ�������
						pg.setVisibility(View.GONE);// �����������ɼ�
						loadmore.setVisibility(View.VISIBLE);// ��ť�ɼ�
						applistAdapter.notifyDataSetChanged();// ֪ͨlistViewˢ������
					}
				}, 2000);
			}
		});

	}

	final Handler handler1 = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Map<String, Object> ma = (Map<String, Object>) msg.obj;
			// �������ʱ������imageViews
			if (differencetype == 1) {
				getImages = (List<ImageInfo>) ma.get("a1");

				/** ��ʼ��ͼƬ��Դ */
				imageViews.clear();

				for (int i = 0; i < getImages.size(); i++) {
					ImageView imageView = new ImageView(InstallNecessary.this);
					// ����ͼƬurl����ͼƬ
					AsyncImageLoader.loadViewImage(getImages.get(i)
							.getApp_imageurl(), imageView, viewPager);
					imageView.setScaleType(ScaleType.CENTER_CROP);
					imageViews.add(imageView);
				}
			}

			getApplistInfos = (List<AppWallDownloadInfo>) ma.get("b1");
			//��ȡ���غ���ʾ��ť��״̬
			for (int i = 0; i < getApplistInfos.size(); i++) {
				getApplistInfos.get(i).state = getAdState(
						InstallNecessary.this,
						getApplistInfos.get(i).app_download);
			}
			applistAdapter.notifyDataSetChanged();
		}
	};

	/**
	 * ��ȡ����״̬
	 * 
	 * @param context
	 *            ������
	 * @param fileUrl
	 *            �������
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

	/**
	 * ��Activity��ʾ������ÿ�������л�һ��ͼƬ��ʾ
	 */
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
		super.onStart();

	}

	/**
	 * ��Activity���ɼ���ʱ��ֹͣ�л�
	 */
	protected void onStop() {
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	/** �����л����� */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				/** ͨ��Handler�л�ͼƬ */
				handler.obtainMessage(currentItem).sendToTarget();
			}
		}

	}

	/**
	 * ��ViewPager��ҳ���״̬�����ı�ʱ����
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		private int oldPosition = 0;

		/**
		 * �������ʱ������һ���µ�ҳ����ѡ���ġ� position :λ�õ�����ҳ�����ѡ��
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * ���ViewPagerҳ���������
	 */
	private class MyAdapter extends PagerAdapter {

		public int getCount() {
			return imageViews.size();
		}

		public Object instantiateItem(View arg0, int arg1) {
			final int index1 = arg1;
			((ViewPager) (arg0)).addView(imageViews.get(arg1));
			imageViews.get(arg1).setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					ApplyAdWallHelper.showSlideAdDetail(context,
							getImages.get(index1));
				}
			});
			return imageViews.get(arg1);
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) (arg0)).removeView((View) arg2);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {
		}

		public void finishUpdate(View arg0) {
		}

	}

	/**
	 * app Ӧ�ó���������
	 */
	class ApplistAdapter extends BaseAdapter {

		// ������
		Context context;

		/**
		 * ������������ݵĳ�ʼ��
		 * 
		 * @param context
		 */
		public ApplistAdapter(Context context) {
			this.context = context;
			// �õ�ϵͳ���õĲ���������
			li = (LayoutInflater) context
					.getSystemService(LAYOUT_INFLATER_SERVICE);
			AppResInit.initAdWallItem();

		}

		public int getCount() {
			return getApplistInfos.size();

		}

		public Object getItem(int position) {

			return getApplistInfos.get(position);
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			final int index = position;
			final AppWallDownloadInfo appListUnit = getApplistInfos
					.get(position);
			final ViewCache cache;

			if (convertView == null) {
				convertView = li.inflate(
						R.layout.softinstall_necessary_listview, null);
				cache = new ViewCache();
				cache.apkIcon = (ImageView) convertView
						.findViewById(R.id.re_appicon);
				cache.apkName = (TextView) convertView
						.findViewById(R.id.re_appName);
				cache.apkRatingBar = (RatingBar) convertView
						.findViewById(R.id.restar_ratingbar);
				cache.apkSize = (TextView) convertView
						.findViewById(R.id.re_installnecessaryappSize);

				cache.downloadTv = (TextView) convertView
						.findViewById(R.id.apk_wall_tv_download);
				convertView.setTag(cache);
			} else {
				cache = (ViewCache) convertView.getTag();
			}
//			System.out.println("---------װ���ر�ͼ��------"
//					+ getApplistInfos.get(position).getApp_icon());
			// ����ͼƬ
			AsyncImageLoader.loadViewImage(getApplistInfos.get(position)
					.getApp_icon(), cache.apkIcon, convertView);
			cache.apkName.setText(appListUnit.app_Name);
			cache.apkRatingBar.setRating(appListUnit.app_score);
			String convertsize = MemoryStatus
					.formatFileSize(appListUnit.app_size);

			cache.apkSize.setText(convertsize);

			cache.downloadTv.setText(getDownloadText(appListUnit.state));
			// System.out.println("-------װ���ر�����״̬-----" + appListUnit.state);
			// ���ذ�ť�Ĵ����¼�
			cache.downloadTv.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					if (cache.downloadTv.getText().equals("����")) {
						AppOneAdDownloader.getInstance(context, appListUnit)
								.startDownload();
						downloadsum++;
						SoftManagerPreference.saveSoftNeccessaryDownload(
								InstallNecessary.this, downloadsum);
					} else if (cache.downloadTv.getText().equals("��ͣ")) {
						AppOneAdDownloader.getInstance(context, appListUnit)
								.stopDownload();
					} else if (cache.downloadTv.getText().equals("����")) {
						AppOneAdDownloader.getInstance(context, appListUnit)
								.startDownload();
					} else if (cache.downloadTv.getText().equals("��װ")) {

						if (AppInfoHelper.isApKFileOk(
								context,
								AppFileDownloader
										.getDefaultPath(appListUnit.app_download))) {

							AppAdDefaultStateHandler stateHandler = new AppAdDefaultStateHandler(
									context);
							// �����ݿ��������
							if (stateHandler.isNewFile(appListUnit.packagename,
									appListUnit.versioncode)) {
								stateHandler.addNewFile(
										appListUnit.app_download,
										appListUnit.packagename,
										appListUnit.versioncode,
										appListUnit.app_id,
										AppAdDefaultStateHandler.STATE_FINISH);
							}
							SystemIntent.installApk(context, AppFileDownloader
									.getDefaultPath(appListUnit.app_download));

						} else {
							appListUnit.state = AppFileDownloader.STATE_DOWNLOAD;
							notifyDataSetChanged();
							CustomPrint.show(context, "�ļ��Ѳ����ڣ�����������");
						}
						installsum++;
						SoftManagerPreference.saveSoftNeccessaryInstall(
								InstallNecessary.this, installsum);
					} else if (cache.downloadTv.getText().equals("����")) {

						if (AppInfoHelper.isAppOk(context,
								appListUnit.packagename)) {
							SystemIntent.runApplication(context,
									appListUnit.packagename);
						} else {
							CustomPrint.show(context, "��Ӧ�ò����ڻ��޽���");
							appListUnit.state = AppAdDefaultStateHandler.STATE_FINISH;

							notifyDataSetChanged();
							AppAdDefaultStateHandler stateHandler = new AppAdDefaultStateHandler(
									context);
							if (!stateHandler.isNewFile(
									appListUnit.packagename,
									appListUnit.versioncode)) {
								stateHandler.updateState(
										appListUnit.packagename,
										appListUnit.versioncode,
										AppAdDefaultStateHandler.STATE_FINISH);
							}
						}
					}

				}
			});

			// �����Ŀ��������һ��activity
			convertView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					ApplyAdWallHelper.showAdDetail(context,
							getApplistInfos.get(index));

				}
			});

			return convertView;
		}

		/**
		 * ���湦��
		 */
		class ViewCache {
			/** Ӧ��ͼ�� */
			ImageView apkIcon;
			/** Ӧ������ */
			TextView apkName;
			/** Ӧ���Ǽ� */
			RatingBar apkRatingBar;
			/** Ӧ�ô�С */
			TextView apkSize;
			/** ���� */
			TextView downloadTv;

		}

	}

	/**
	 * ��ò�ͬ״̬��Ӧ������
	 * 
	 * @param state
	 *            ״ֵ̬
	 * @return
	 */

	@SuppressWarnings("unused")
	private String getDownloadText(int state) {
		/** ��������״̬�ı�ǩ */
		String tag = "";
		switch (state) {
		case AppFileDownloader.STATE_DOWNLOAD:
			tag = "����";
			break;
		case AppFileDownloader.STATE_PAUSE:
			tag = "��ͣ";
			break;
		case AppFileDownloader.STATE_RESUME:
			tag = "����";
			break;
		case AppFileDownloader.STATE_FINISH:
			tag = "��װ";
			break;
		case AppAdDefaultStateHandler.STATE_RUN:
			tag = "����";
			break;
		default:
			break;
		}
		return tag;
	}

	/**
	 * ������ť����ȡ��������
	 */
	private void loadMoreDate(final int type, final int index) {

		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {

				Map<String, Object> map = new HashMap<String, Object>();
				String urlStr = "http://cp.g365.cn/app.php?userid="
						+ AdHelper.getUserId(context) + "&type=" + type
						+ "&pageno=" + index;
				try {
					// ����get����
					BufferedInputStream bis = new BufferedInputStream(
							UrlHelper.getInputStreamFromUrl(urlStr));
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int readNum = -1;
					while ((readNum = bis.read(buffer)) != -1) {
						baos.write(buffer, 0, readNum);
					}
					byte[] data = baos.toByteArray();
					// ����pull�������ݴ��뵽map
					map = PullUtils.getApplistinfo(new String(data));
					// ��ȡ������ҳ��
					appPages1 = (AppPage) map.get("c1");
					// �����ȡ��������
					List<AppWallDownloadInfo> newApplistInfos = (List<AppWallDownloadInfo>) map
							.get("b1");
					Message msg = new Message();
					msg.obj = newApplistInfos;
					msg.what = appPages1.getCurrentPage();
					// ������ܹ�ҳ��
					msg.arg1 = appPages1.getAllPage();
					// ��Ϸ���ܹ�ҳ��
					msg.arg2 = appPages1.getAllPage();
					// handler������Ϣ
					disposalHandler.sendMessage(msg);
					bis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			};
		}.start();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(stateChangeReceiver);
		super.onDestroy();
	}
}
