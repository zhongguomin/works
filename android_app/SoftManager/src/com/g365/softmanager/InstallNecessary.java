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

	/** 打开广告墙需要执行下载操作传递过来的需下载广告信息字段 */
	public static final String DATA_KEY_ADWALL_INFO = "appwalldownloadinfo";

	/** ListView底部View */
	private View moreView;
	/** 加载更多 */
	private Button loadmore;
	/** 进度条 */
	private ProgressBar pg;

	/** 加载更多的handler */
	private Handler loadmorehandler;

	/** 初始化软件第一页 */
	private int pageNowsoft = 1;
	/** 初始化游戏第一页 */
	private int pageNowgame = 1;
	/** 区别游戏还是软件 */
	private int differencetype = 1;

	private AppPage appPages1;

	/**
	 * 导入布局
	 */
	private LayoutInflater li;
	/** 返回 */
	private LinearLayout installback;
	/** 图片list */
	private List<ImageInfo> getImages = new ArrayList<ImageInfo>();
	/** app信息list */
	private List<AppWallDownloadInfo> getApplistInfos = new ArrayList<AppWallDownloadInfo>();

	/** 自定义适配器 */
	private ApplistAdapter applistAdapter;
	/** 要适配的list */
	private ListView app_listView;

	/** 点击软件 */
	private TextView click_software;
	/** 点击游戏 */
	private TextView click_softgame;
	/**
	 * android-support-v4中的滑动组件
	 */
	private ViewPager viewPager;
	/**
	 * 滑动的图片集合
	 */
	private List<ImageView> imageViews = new ArrayList<ImageView>();
	/**
	 * 图片标题正文的那个点
	 */
	private List<View> dots;
	/**
	 * 当前图片的索引号
	 */
	private int currentItem = 0;

	/**
	 * 一个ExecutorService,可以调度命令来运行在一个给定的延迟或定期执行。
	 */
	private ScheduledExecutorService scheduledExecutorService;

	/** 上下文对象s */
	private Context context;

	private AppStateChangeReceiver stateChangeReceiver;

	/** 下载次数 */
	private int downloadsum = 0;
	/** 安装次数 */
	private int installsum = 0;

	/**
	 * 处理正在加载操作的handler
	 */
	private Handler disposalHandler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			// 获取handler发送过来的消息添加到适配的list中
			getApplistInfos.addAll((ArrayList<AppWallDownloadInfo>) msg.obj);

			/** 软件的最大数目 */
			if (pageNowsoft == msg.arg1) {
				// 软件数据全部加载完 把moreView remove掉
				app_listView.removeFooterView(moreView);
				Toast.makeText(InstallNecessary.this, "数据全部加载完成，没有更多数据！",
						Toast.LENGTH_LONG).show();
			}
			/** 游戏的最大数目 */
			if (pageNowgame == msg.arg2) {
				// if(pageNowgame<msg.arg2){
				// app_listView.addFooterView(moreView);
				// }
				// 游戏数据全部加在完 把moreView remove掉
				app_listView.removeFooterView(moreView);
				Toast.makeText(InstallNecessary.this, "数据全部加载完成，没有更多数据！",
						Toast.LENGTH_LONG).show();
			}
			// 适配器改变
			applistAdapter.notifyDataSetChanged();
			super.handleMessage(msg);
		};
	};
	/**
	 * 切换当前显示的图片
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
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_install_necessary);
		// 查找控件
		click_software = (TextView) findViewById(R.id.necessary_click_software);
		click_softgame = (TextView) findViewById(R.id.necessary_click_softgame);

		/** 初始化颜色的值 */
		//click_software.setTextColor(Color.RED);
		//click_softgame.setTextColor(Color.BLACK);
		
		/** 初始化颜色的值 */
		click_software.setTextColor(getResources().getColor(R.color.appmovebule));
		click_softgame.setTextColor(getResources().getColor(R.color.appmoveblack));
		// 初始化
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
		// 适配的listview
		app_listView = (ListView) findViewById(R.id.installnecessarylistview);
		// 实例化底部布局
		moreView = getLayoutInflater().inflate(
				R.layout.install_necessary_moredata, null);
		loadmore = (Button) moreView.findViewById(R.id.bt_load);
		pg = (ProgressBar) moreView.findViewById(R.id.pg);

		loadmorehandler = new Handler();

		/** 加上底部View，注意要放在setAdapter方法前 */
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

		/** 五个小圆圈 */
		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));
		dots.add(findViewById(R.id.v_dot3));
		dots.add(findViewById(R.id.v_dot4));

		/**
		 * 设置填充ViewPager页面的适配器
		 */
		viewPager.setAdapter(new MyAdapter());
		/**
		 * 设置一个监听器，当ViewPager中的页面改变时调用
		 */
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
		installback = (LinearLayout) findViewById(R.id.installnecessary);
		// 返回
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

		/** 点击软件按钮 */
		click_software.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 设置背景改变
				LinearLayout ll_softgame = (LinearLayout) findViewById(R.id.common_necessary_softgame);
				ll_softgame
						.setBackgroundResource(R.drawable.install_necessary_softbutton);
				// 设置字体改变
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

		/** 点击游戏按钮 */
		click_softgame.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				LinearLayout ll_softgame = (LinearLayout) findViewById(R.id.common_necessary_softgame);
				// 设置背景改变
				ll_softgame
						.setBackgroundResource(R.drawable.install_necessary_gamebutton);
				differencetype = 2;

				// 设置字体改变
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
				pg.setVisibility(View.VISIBLE);// 将进度条可见
				loadmore.setVisibility(View.GONE);// 按钮不可见
				loadmorehandler.postDelayed(new Runnable() {

					public void run() {
						pageNowsoft++;
						pageNowgame++;
						loadMoreDate(differencetype, pageNowsoft);// 加载更多数据
						pg.setVisibility(View.GONE);// 将进度条不可见
						loadmore.setVisibility(View.VISIBLE);// 按钮可见
						applistAdapter.notifyDataSetChanged();// 通知listView刷新数据
					}
				}, 2000);
			}
		});

	}

	final Handler handler1 = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Map<String, Object> ma = (Map<String, Object>) msg.obj;
			// 是软件的时候就清空imageViews
			if (differencetype == 1) {
				getImages = (List<ImageInfo>) ma.get("a1");

				/** 初始化图片资源 */
				imageViews.clear();

				for (int i = 0; i < getImages.size(); i++) {
					ImageView imageView = new ImageView(InstallNecessary.this);
					// 根据图片url下载图片
					AsyncImageLoader.loadViewImage(getImages.get(i)
							.getApp_imageurl(), imageView, viewPager);
					imageView.setScaleType(ScaleType.CENTER_CROP);
					imageViews.add(imageView);
				}
			}

			getApplistInfos = (List<AppWallDownloadInfo>) ma.get("b1");
			//读取下载后显示按钮的状态
			for (int i = 0; i < getApplistInfos.size(); i++) {
				getApplistInfos.get(i).state = getAdState(
						InstallNecessary.this,
						getApplistInfos.get(i).app_download);
			}
			applistAdapter.notifyDataSetChanged();
		}
	};

	/**
	 * 获取广告的状态
	 * 
	 * @param context
	 *            上下文
	 * @param fileUrl
	 *            广告链接
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
	 * 当Activity显示出来后，每两秒钟切换一次图片显示
	 */
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 3,
				TimeUnit.SECONDS);
		super.onStart();

	}

	/**
	 * 当Activity不可见的时候停止切换
	 */
	protected void onStop() {
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	/** 换行切换任务 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				/** 通过Handler切换图片 */
				handler.obtainMessage(currentItem).sendToTarget();
			}
		}

	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		private int oldPosition = 0;

		/**
		 * 这个方法时将调用一个新的页面变得选定的。 position :位置的索引页面的新选择。
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
	 * 填充ViewPager页面的适配器
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
	 * app 应用程序适配器
	 */
	class ApplistAdapter extends BaseAdapter {

		// 上下文
		Context context;

		/**
		 * 构造器完成数据的初始化
		 * 
		 * @param context
		 */
		public ApplistAdapter(Context context) {
			this.context = context;
			// 得到系统内置的布局填充服务
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
//			System.out.println("---------装机必备图标------"
//					+ getApplistInfos.get(position).getApp_icon());
			// 设置图片
			AsyncImageLoader.loadViewImage(getApplistInfos.get(position)
					.getApp_icon(), cache.apkIcon, convertView);
			cache.apkName.setText(appListUnit.app_Name);
			cache.apkRatingBar.setRating(appListUnit.app_score);
			String convertsize = MemoryStatus
					.formatFileSize(appListUnit.app_size);

			cache.apkSize.setText(convertsize);

			cache.downloadTv.setText(getDownloadText(appListUnit.state));
			// System.out.println("-------装机必备下载状态-----" + appListUnit.state);
			// 下载按钮的触发事件
			cache.downloadTv.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					if (cache.downloadTv.getText().equals("下载")) {
						AppOneAdDownloader.getInstance(context, appListUnit)
								.startDownload();
						downloadsum++;
						SoftManagerPreference.saveSoftNeccessaryDownload(
								InstallNecessary.this, downloadsum);
					} else if (cache.downloadTv.getText().equals("暂停")) {
						AppOneAdDownloader.getInstance(context, appListUnit)
								.stopDownload();
					} else if (cache.downloadTv.getText().equals("继续")) {
						AppOneAdDownloader.getInstance(context, appListUnit)
								.startDownload();
					} else if (cache.downloadTv.getText().equals("安装")) {

						if (AppInfoHelper.isApKFileOk(
								context,
								AppFileDownloader
										.getDefaultPath(appListUnit.app_download))) {

							AppAdDefaultStateHandler stateHandler = new AppAdDefaultStateHandler(
									context);
							// 向数据库添加数据
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
							CustomPrint.show(context, "文件已不存在，请重新下载");
						}
						installsum++;
						SoftManagerPreference.saveSoftNeccessaryInstall(
								InstallNecessary.this, installsum);
					} else if (cache.downloadTv.getText().equals("运行")) {

						if (AppInfoHelper.isAppOk(context,
								appListUnit.packagename)) {
							SystemIntent.runApplication(context,
									appListUnit.packagename);
						} else {
							CustomPrint.show(context, "此应用不存在或无界面");
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

			// 点击条目启动另外一个activity
			convertView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					ApplyAdWallHelper.showAdDetail(context,
							getApplistInfos.get(index));

				}
			});

			return convertView;
		}

		/**
		 * 缓存功能
		 */
		class ViewCache {
			/** 应用图标 */
			ImageView apkIcon;
			/** 应用名字 */
			TextView apkName;
			/** 应用星级 */
			RatingBar apkRatingBar;
			/** 应用大小 */
			TextView apkSize;
			/** 下载 */
			TextView downloadTv;

		}

	}

	/**
	 * 获得不同状态对应的文字
	 * 
	 * @param state
	 *            状态值
	 * @return
	 */

	@SuppressWarnings("unused")
	private String getDownloadText(int state) {
		/** 区别下载状态的标签 */
		String tag = "";
		switch (state) {
		case AppFileDownloader.STATE_DOWNLOAD:
			tag = "下载";
			break;
		case AppFileDownloader.STATE_PAUSE:
			tag = "暂停";
			break;
		case AppFileDownloader.STATE_RESUME:
			tag = "继续";
			break;
		case AppFileDownloader.STATE_FINISH:
			tag = "安装";
			break;
		case AppAdDefaultStateHandler.STATE_RUN:
			tag = "运行";
			break;
		default:
			break;
		}
		return tag;
	}

	/**
	 * 单击按钮重新取数据适配
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
					// 发送get请求
					BufferedInputStream bis = new BufferedInputStream(
							UrlHelper.getInputStreamFromUrl(urlStr));
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int readNum = -1;
					while ((readNum = bis.read(buffer)) != -1) {
						baos.write(buffer, 0, readNum);
					}
					byte[] data = baos.toByteArray();
					// 利用pull解析数据存入到map
					map = PullUtils.getApplistinfo(new String(data));
					// 获取解析的页数
					appPages1 = (AppPage) map.get("c1");
					// 网络获取的新数据
					List<AppWallDownloadInfo> newApplistInfos = (List<AppWallDownloadInfo>) map
							.get("b1");
					Message msg = new Message();
					msg.obj = newApplistInfos;
					msg.what = appPages1.getCurrentPage();
					// 软件的总共页数
					msg.arg1 = appPages1.getAllPage();
					// 游戏的总共页数
					msg.arg2 = appPages1.getAllPage();
					// handler发送消息
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
