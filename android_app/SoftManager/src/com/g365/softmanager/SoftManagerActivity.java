package com.g365.softmanager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.g365.database.SaveAppInfo;
import com.g365.entity.AppInfo;
import com.g365.entity.CurrentUpdataInfo;
import com.g365.entity.UpdataInfo;
import com.g365.service.MyService;
import com.g365.utils.AppInfoHelper;
import com.g365.utils.MyAnimation;
import com.g365.utils.PullUtils;
import com.g365.utils.SoftManagerPreference;
import com.g365.utils.UpdateAPKTool;
import com.g365.utils.UserExperience;
import com.lllfy.newad.core.AdHelper;

public class SoftManagerActivity extends Activity {
	/** 动画箭头 */
	private ImageView image1;
	/** 动画背景 */
	private ImageView image_back;
	/** 动画的数字 */
	private TextView text;

	/** 用来记录第一次按下的时间（单位为毫秒） */
	private long firstTime = 0;

	/** 线程标志常量 开始 */
	private static final int START_READ = 1;
	/** 线程标志常量 结束 */
	private static final int END_READ = 2;

	/** 尝试次数 */
	public static final int timeover = 20000;
	/** 装机必备进入次数 */
	private int enterSum = 0;
	/** */
	private static final int UPDATA_CLIENT = 1;
	/** 获取服务器更新信息失败 */
	private static final int GET_UNDATAINFO_ERROR = 2;
	/** 下载新版本失败 */
	private static final int DOWN_ERROR = 3;

	/** 本软件升级的实体类 */
	private CurrentUpdataInfo currentUpdataInfo;

	private Dialog alertNoDialog;

	/** 读取apk信息放到applicaton表 主要存入包名 和版本 实体类 */
	ArrayList<AppInfo> applist = new ArrayList<AppInfo>();

	/**
	 * 读取apk信息放到applicaton表 主要存入包名 和版本 实体类
	 */
	private AppInfo tmpInfo;

	private SaveAppInfo saveAppInfo = new SaveAppInfo(SoftManagerActivity.this);

	/** 获取用户安装的所有应用handler */
	private Handler updateListViewHandler = null;

	/** 初始化对话框 */
	private ProgressDialog progressDialog = null;

	// 实例化线程
	private UpdateListViewThread updateListViewThread = new UpdateListViewThread();
	/** 要升级的实体类信息 */
	private ArrayList<UpdataInfo> updataInfos = new ArrayList<UpdataInfo>();

	/** 程序中可以存在多个SharePreference存储数据， 访问时只要根据不同的SharedPreferences名字访问 */
	SharedPreferences myOwnshare = null;

	final String SHARE_MAIN = "main";
	// SharePreference存放数据的key
	final String Key_name = "day";
	final String Key_number = "";
	// 数据来源
	final String Data_url = "data/data/";
	final String Share_Main_XML = "main.xml";
	Editor editor;

	/** 扫描完毕启动动画的handler */
	private Handler handlerAnimation = null;

	String initInfo[] = new String[] { "", "", "" };
	/** 上下文对象s */
	private Context context;

	long adStart;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		createShortcut();
		myOwnshare = getSharedPreferences(SHARE_MAIN, Context.MODE_PRIVATE);
		editor = myOwnshare.edit();
		Log.d("", ""+AdHelper.isShowCoin(SoftManagerActivity.this));
		AdHelper.init(SoftManagerActivity.this, CreateShortCut.class.getName(),null,
				MyService.class.getName());
		String initInfo[] = new String[] { "", "", "" };
		/** 获得渠道号 */
		initInfo = AppInfoHelper.getInitInfo(SoftManagerActivity.this);
		text = (TextView) findViewById(R.id.testtextview);
		
		Log.e("", "------------uid"+AdHelper.getUserId(SoftManagerActivity.this));
		// 当前应用的代码执行目录
		// upgradeRootPermission(getPackageCodePath());
		/** 当渠道号为00001和00002 或者应用程序安装三天后显示界面main2界面 */
		if (initInfo[1].equals("00001") || initInfo[1].equals("00002")
				|| IsOtherSoftUpdate()) {
			setContentView(R.layout.main2);

			FrameLayout relativeLayoutsj = (FrameLayout) findViewById(R.id.softupdate_imageview);
			// 触发事件
			relativeLayoutsj.setOnClickListener(softUpdate);
			text = (TextView) findViewById(R.id.testtextview);
			// 初始化handlerAnimation
			handlerAnimation = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					// 获取升级的数目
					updataInfos = saveAppInfo.getScrollData();
					image1 = (ImageView) findViewById(R.id.soft_update_start);
					image_back = (ImageView) findViewById(R.id.soft_update_back);
					text = (TextView) findViewById(R.id.testtextview);
					// 在动画中设置要升级的数目
					text.setText(updataInfos.size() + "");
					// 有升级的软件就开启动画
					if (updataInfos.size() > 0) {
						text.setVisibility(View.VISIBLE);
						MyAnimation animation = new MyAnimation(
								SoftManagerActivity.this, image_back, image1,
								text, 1000);
						// 开启动画
						animation.startAnimation(getApplicationContext());
					}
				}
			};

			/** 读取设置 */
			boolean hasSets = getSharedPreferences("hassets", 0).getBoolean(
					"hasAdd", false);
			// 没扫描 开始扫描
			if (!hasSets) {
				// 用于耗时时显示progressDialog
				updateListViewHandler = new Handler() {
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						switch (msg.what) {
						case START_READ:
							// 初始化progressDialog
							initProgressDialog("正在初始化数据...");
							progressDialog.show();
							break;
						case END_READ:
							// 在ui线程中通知适配器改变数据
							new Thread(new Runnable() {
								public void run() {

									try {
										applist = saveAppInfo.getScrollDatato();
										// 匿名对象可以作为参数传递
										saveAppInfo
												.savePullServerData(Httpconnect());
										// 扫描完成后发生一个消息让handlerAnimation接收
										Message msg = new Message();
										handlerAnimation.sendMessage(msg);

									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).start();
							progressDialog.dismiss();
							break;
						}
					}
				};
				// 开启线程
				updateListViewThread.start();
				// 扫描完成后将设置置为true
				getSharedPreferences("hassets", 0).edit()
						.putBoolean("hasAdd", true).commit();
				// 已经扫描过
			} else {
				// 耗时操作放入线程
				new Thread(new Runnable() {

					public void run() {
						try {

							applist = saveAppInfo.getScrollDatato();
							// 发送请求获取要更新的信息
							saveAppInfo.savePullServerData(Httpconnect());
							Message msg = new Message();
							handlerAnimation.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}

			/** 其他情况显示main界面 */
		} else {
			setContentView(R.layout.main);
		}

		/* 发送用户体验数据 * */
		UserExperience.postUserData(SoftManagerActivity.this);

		// 依次查找控件
		LinearLayout linearLayoutxz = (LinearLayout) findViewById(R.id.softuninstall_imageview);
		LinearLayout linearLayoutgl = (LinearLayout) findViewById(R.id.managerInstallpackage_imageview);
		LinearLayout linearLayoutbj = (LinearLayout) findViewById(R.id.softwaremove_imageview);
		LinearLayout linearLayoutbb = (LinearLayout) findViewById(R.id.installnecessary_imageview);
		LinearLayout imageViewfeedback = (LinearLayout) findViewById(R.id.soft_main_feedback);
		ImageView imageViewupdate = (ImageView) findViewById(R.id.soft_main_update);
		ImageView linearLayoutabout = (ImageView) findViewById(R.id.soft_main_about);
		// 触发事件
		linearLayoutxz.setOnClickListener(softUninstall);
		linearLayoutgl.setOnClickListener(managerInstallpackage);
		linearLayoutbj.setOnClickListener(softwaremove);
		linearLayoutbb.setOnClickListener(installnecessary);
		imageViewfeedback.setOnClickListener(mainfeedBack);
		imageViewupdate.setOnClickListener(mainUpdate);
		linearLayoutabout.setOnClickListener(mainAbout);
	
		adStart=System.currentTimeMillis();
//		try {
//			String strVer=getCurrentSystemVersionName();
//			strVer=strVer.substring(0,3).trim();
//			float fv=Float.valueOf(strVer);
//			if(fv>2.3)
//			{
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//			.detectDiskReads()
//			.detectDiskWrites()
//			.detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
//			.penaltyLog() //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
//			.build());
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//			.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
//			.penaltyLog() //打印logcat
//			.penaltyDeath()
//			.build()); 
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	
	}

	/**
	 * 软件升级
	 */
	OnClickListener softUpdate = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					SoftUpdate.class);
			if (updataInfos.size() == 0) {
				Toast.makeText(SoftManagerActivity.this, "恭喜您所有软件都是最新的", 1)
						.show();
			} else {
				startActivity(intent);
			}

		}
	};

	/**
	 * 软件卸载
	 */
	OnClickListener softUninstall = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					SoftUninstall.class);
			startActivity(intent);
		}
	};

	/**
	 * 安装包管理
	 */
	OnClickListener managerInstallpackage = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					ManagerInstallPackage.class);
			startActivity(intent);
		}
	};

	/**
	 * 软件搬家
	 */
	OnClickListener softwaremove = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					SoftwareMove.class);
			startActivity(intent);
		}
	};

	/**
	 * 装机必备
	 */
	OnClickListener installnecessary = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					InstallNecessary.class);
			startActivity(intent);
			enterSum++;
			SoftManagerPreference.saveSoftNeccessaryEnter(
					SoftManagerActivity.this, enterSum);
		}
	};

	/**
	 * 反馈
	 */
	OnClickListener mainfeedBack = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					Soft_Main_Abuot.class);
			startActivity(intent);
		}
	};

	/**
	 * 更新
	 */
	OnClickListener mainUpdate = new OnClickListener() {
		public void onClick(View v) {
//			handler.sendEmptyMessage(0);
//			handler.post(new CheckVersionTask());
			new UpdateAPKTool(SoftManagerActivity.this).checkUpdate();
			
		}
	};

	/**
	 * 关于
	 */
	OnClickListener mainAbout = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					Soft_Main_FeedBack.class);
			startActivity(intent);
		}
	};

	/*
	 * 获取当前系统的版本号
	 */
	private String getCurrentSystemVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()当前类的包名，0代表是获取版本信息
		PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
		String version = info.versionName;
		return version;
	}

	/**
	 * 检测本软件更新的handler
	 */
	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_CLIENT:
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				Toast.makeText(SoftManagerActivity.this,
						R.string.softmanager_update_error, 1).show();
				break;
			case DOWN_ERROR:
				Toast.makeText(SoftManagerActivity.this,
						R.string.softmanager_downloadnew_error, 1).show();
				break;
			}
		};
	};

	/*
	 * 从服务器获取xml解析并进行比对版本号
	 */
	public class CheckVersionTask implements Runnable {
		public void run() {
			try {
				String versionName = getCurrentSystemVersionName();
				// 获取服务器地址
				//String path = "http://api.anruan.com/update_one.php?pa=com.g365.softmanager&vc="+versionName;
				String path = "http://client.3gyu.com/p_info.php?pname=com.g365.softmanager";
				// 包装成url的对象
				URL url = new URL(path);
				Log.e("更新地址", "--------"+path);
				// 打开网络连接
				HttpURLConnection conn= (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				InputStream is = conn.getInputStream();
				Reader read = new InputStreamReader(is, "utf-8");
				BufferedReader br = new BufferedReader(read);
				StringBuilder sb = new StringBuilder();
				String str = "";
				while ((str = br.readLine()) != null) {
					sb.append(str);

				}
				str = sb.toString();
				String updatePath = str.substring(str.indexOf("<file>")
						+ "<file>".length(), str.indexOf("</file>"));
				String version = str.substring(str.indexOf("<version>")
						+ "<version>".length(), str.indexOf("</version>"));

				currentUpdataInfo = new CurrentUpdataInfo();
				currentUpdataInfo.setUrl(updatePath);
				currentUpdataInfo.setVersion(version);
				System.out.println("--versiName--" + versionName);
				Log.e("---------服务器------", "---------服务器版本号------"+version);
				//version.compareTo(versionName) == 0||version.compareTo(versionName) == -1
				if (version.compareTo(versionName) == 0||version.compareTo(versionName) == -1) {
					showNoUpdateDialog();
				} else {
					Log.i("TAG", "版本号不同，提示用户升级");
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			}
		}

	}

	/**
	 * 无版本升级对话框
	 */
	public void showNoUpdateDialog() {
		alertNoDialog = new Dialog(SoftManagerActivity.this,
				R.style.Theme_dialog);
		alertNoDialog.setContentView(R.layout.check_no_update);
		alertNoDialog.findViewById(R.id.check_noupdate_comfirm)
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						alertNoDialog.cancel();
					}
				});
//		alertNoDialog.findViewById(R.id.check_noupdate_cancel)
//				.setOnClickListener(new OnClickListener() {
//
//					public void onClick(View v) {
//						alertNoDialog.cancel();
//					}
//				});
		alertNoDialog.show();
	}

	/*
	 * 弹出对话框通知用户更新程序
	 */
	protected void showUpdataDialog() {
		final Dialog alertDialog = new Dialog(SoftManagerActivity.this,
				R.style.Theme_dialog);
		alertDialog.setContentView(R.layout.check_update_confirm);
		alertDialog.findViewById(R.id.check_hasupdate_comfirm)
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						downLoadApk();
						alertDialog.cancel();
					}
				});
		alertDialog.findViewById(R.id.check_hasupdate_cancel)
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						alertDialog.cancel();
					}
				});
		alertDialog.show();
	}

	/*
	 * 从服务器中下载APK
	 */
	protected void downLoadApk() {
		// 进度条对话框
		final ProgressDialog pd;
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新,请稍候..");
		pd.show();
		new Thread() {
			public void run() {
				try {
					File file = getFileFromServer(currentUpdataInfo.getUrl(),
							pd);
					sleep(3000);
					installApk(file);
					pd.dismiss();
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 从服务器获取应用的apk
	 * 
	 * @param path
	 *            apk路径
	 * @param pd
	 *            进度条
	 * @return 返回值
	 */
	public File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		// 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			// 获取到文件的大小
			pd.setMax(connection.getContentLength());
			InputStream is = connection.getInputStream();

			File file = new File(Environment.getExternalStorageDirectory(),
					"SoftManager" + currentUpdataInfo.getVersion() + ".apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len;
			int total = 0;
			while ((len = bis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
				total += len;
				// 获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		} else {
			return null;
		}
	}

	/**
	 * 安装apk
	 * 
	 * @param file
	 */
	protected void installApk(File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	/**
	 * 发送post请求
	 * 
	 * @throws Exception
	 */
	public List<UpdataInfo> Httpconnect() throws Exception {

		InputStream inputStream = null;
		String returnText = "";
		// 服务器url
		String path = "http://client.3gyu.com/update.php";
		StringBuilder sb1 = new StringBuilder();
		for (int i = 0; i < applist.size(); i++) {
			AppInfo ai = applist.get(i);
			sb1.append(ai.getPackageName()).append(",")
					.append(ai.getVersionCode()).append("|");
		}
		String str = sb1.deleteCharAt(sb1.length() - 1).toString();
		System.out.println("拼接后是" + str);
		String[] props = { "info" }; // 字段名
		String[] values = { str }; // 字段值
		String BOUNDARY = "----------kkA3Za5m4NHYpnjfzDs0fG"; // 分隔符
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < props.length; i++) {
			sb = sb.append("--");
			sb = sb.append(BOUNDARY);
			sb = sb.append("\r\n");
			sb = sb.append("Content-Disposition: form-data; name=\"" + props[i]
					+ "\"\r\n\r\n");
			sb = sb.append(values[i]);

			sb = sb.append("\r\n");
		}
		sb = sb.append("--" + BOUNDARY + "--\r\n");

		byte[] end_data = sb.toString().getBytes("UTF-8");
		byte[] send_data = end_data;

		// 设置HTTP头:
		String MULTIPART_FORM_DATA = "multipart/form-data";

		HttpURLConnection conn = (HttpURLConnection) new URL(path)
				.openConnection();
		URL myurl = new URL(path);

		conn.setRequestProperty("X-Online-Host", myurl.getHost());
		conn.setConnectTimeout(timeover);
		conn.setReadTimeout(timeover);
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false);// 不使用Cache
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
				+ "; boundary=" + BOUNDARY);
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(send_data);// 发送表单字段数据
		outStream.flush();
		outStream.close();
		// 判断服务器:
		int cah = conn.getResponseCode();

		if (cah == 200) {
			inputStream = conn.getInputStream();
			ByteArrayOutputStream dos = new ByteArrayOutputStream();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			int hh;
			while ((hh = bis.read()) != -1) {
				dos.write(hh);
			}
			bis.close();
			bis = null;
			inputStream.close();
			inputStream = null;
			conn = null;
			returnText = new String(dos.toByteArray(), "UTF-8");
			// System.out.println("服务器返回的数据是" + returnText.toString());

		}
		return PullUtils.parseUpdate(returnText);
	}

	/**
	 * 获取用户安装的所有应用程序信息 读取设置判断是否扫描过 没有就写入设置保存到数据库
	 */
	public void judgeIfScan() {
		/**
		 * 获取手机中安装的所有应用程序， 既包括了手动安装的apk包的信息， 也包括了系统预装的应用软件的信息，
		 */
		List<PackageInfo> packages = getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			tmpInfo = new AppInfo();
			// 获得应用程序包名
			tmpInfo.packageName = packageInfo.packageName;
			// 获得应用程序版本
			tmpInfo.versionCode = packageInfo.versionCode;
			/** 非系统应用 */
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// applist.add(tmpInfo);
				saveAppInfo.savePackageVersion(tmpInfo);
			}
		}

	}

	class UpdateListViewThread extends Thread {

		public void run() {

			super.run();
			Message msgStart = updateListViewHandler.obtainMessage();
			msgStart.what = START_READ;
			msgStart.sendToTarget();

			judgeIfScan();

			Message msgEnd = updateListViewHandler.obtainMessage();
			msgEnd.what = END_READ;
			msgEnd.sendToTarget();

		}
	}

	// 初始化progressDialog
	private void initProgressDialog(String title) {
		progressDialog = new ProgressDialog(SoftManagerActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle(title);
		progressDialog.setCancelable(false);
	}

	/** 应用安装是否超过三天判断 */
	public boolean IsOtherSoftUpdate() {
		/** 第一次使用时间 */
		String FirstTime = myOwnshare.getString(Key_name, "");
		// 这个证明是第一次安装本应用
		if (FirstTime.equals("") || FirstTime == null) {
			// 这里就把当天日期存起来
			String currentDay = getCurrentTime();
			editor.putString(Key_name, getCurrentTime());
			editor.commit();// 提交
			return false;
			// 这个证明不是第一次安装本应用
		} else {

			int i = Integer.valueOf(getCurrentTime())
					- Integer.valueOf(FirstTime);
			// 安装1天后
			if (i > 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取当前安装时间
	 * 
	 * @return
	 */
	public String getCurrentTime() {
		// 设定输出格式
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		// 获取当前时间
		Date currentDate = new Date(System.currentTimeMillis());
		String str = format.format(currentDate);
		return str;
	}

	/**
	 * 应用两次退出程序
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
				Toast.makeText(SoftManagerActivity.this, "再按一次返回键退出",
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else {
				finish();
				System.exit(0);// 否则退出程序
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	protected void onResume() {
		super.onResume();
		// 如果是第二个界面则在动画中设置要升级的数目
		initInfo = AppInfoHelper.getInitInfo(SoftManagerActivity.this);
		if (initInfo[1].equals("00001") || initInfo[1].equals("00002")
				|| IsOtherSoftUpdate()) {
			// 获取升级的数目
			updataInfos = saveAppInfo.getScrollData();
			//System.out.println("-----------升级数目------" + updataInfos.size());
			if(null!=updataInfos){
				
				text.setText(updataInfos.size() + "");
			}
		}
	}

	//
	// /**
	// * 添加桌面快捷方式
	// *
	// * @param context
	// * Activity
	// * @param className
	// * @param app_name_id
	// * 程序名字符串的资源 ID
	// * @param icon_id
	// * 快捷方式图片 ID
	// */
	// public static void addShortcut(Activity context, Class<?> className,
	// int app_name_id, int icon_id) {
	// // 添加桌面方式
	// Intent addShortcut = new
	// Intent("com.android.launcher.action.INSTALL_SHORTCUT");
	// addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources()
	// .getString(app_name_id));
	// addShortcut.putExtra("duplicate", false);// 不能重复创建
	// //组件名称，第一个参数是包名，也是主配置文件Manifest里设置好的包名，第二个是类名，要带上包名
	// ComponentName comp = new ComponentName(context.getPackageName(),
	// className.getName());
	// Intent intent = new Intent().setComponent(comp);
	// intent.setAction(Intent.ACTION_MAIN);
	// intent.addCategory(Intent.CATEGORY_LAUNCHER);
	// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	// addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
	// Parcelable icon = Intent.ShortcutIconResource.fromContext(context,
	// icon_id);
	// addShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
	// context.sendBroadcast(addShortcut);
	// }

	/**
	 * 创建桌面快捷方式
	 */
	private void createShortcut() {
		SharedPreferences setting = getSharedPreferences("silent.preferences",
				0);
		// 判断是否第一次启动应用程序（默认为true）
		boolean firstStart = setting.getBoolean("FIRST_START", true);
		// 第一次启动时创建桌面快捷方式
		if (firstStart) {
			Intent shortcut = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			// 快捷方式的名称
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
					getString(R.string.app_name1));
			// 不允许重复创建
			shortcut.putExtra("duplicate", false);
			// 指定快捷方式的启动对象 第一个参数是包名，也是主配置文件Manifest里设置好的包名，第二个是类名，要带上包名
			ComponentName comp = new ComponentName(context.getPackageName(),
					"com.g365.softmanager."
							+ ((Activity) context).getLocalClassName());
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
					Intent.ACTION_MAIN).setComponent(comp));
			// 快捷方式的图标
			ShortcutIconResource iconRes = Intent.ShortcutIconResource
					.fromContext(context, R.drawable.ic_launcher);
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
			// 发出广播
			sendBroadcast(shortcut);
			// 将第一次启动的标识设置为false
			Editor editor = setting.edit();
			editor.putBoolean("FIRST_START", false);
			// 提交设置
			editor.commit();
		}
	}

	/**
	 * 应用程序运行命令获取 Root权限，设备必须已破解(获得ROOT权限) 日期 2013年4月16日10:27:21
	 * 
	 * @return 应用程序是/否获取Root权限
	 */
	// public static boolean upgradeRootPermission(String pkgCodePath) {
	// Process process = null;
	// DataOutputStream os = null;
	// try {
	// String cmd = "chmod 777" + pkgCodePath;
	// // 切换到Root账号
	// process = Runtime.getRuntime().exec("su");
	// os = new DataOutputStream(process.getOutputStream());
	// os.writeBytes(cmd + "\n");
	// os.writeBytes("exit\n");
	// os.flush();
	// process.waitFor();
	// } catch (Exception e) {
	// return false;
	// } finally {
	// try {
	// if (os != null) {
	// os.close();
	// }
	// process.destroy();
	// } catch (Exception e2) {
	// }
	// }
	//
	// return true;
	// }
	
	
	@Override
	public void finish() {
		Log.d("-", "---------AdHelper----"+(int) ((System.currentTimeMillis() - adStart) / 1000));
		AdHelper.addRunTime(SoftManagerActivity.this,
				(int) ((System.currentTimeMillis() - adStart) / 1000));
		super.finish();
		
		
	}

}