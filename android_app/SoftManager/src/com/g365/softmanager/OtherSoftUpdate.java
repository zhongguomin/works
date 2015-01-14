package com.g365.softmanager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.g365.entity.CurrentUpdataInfo;
import com.g365.entity.UpdataInfo;
import com.g365.utils.MyAnimation;
import com.g365.utils.SoftManagerPreference;

/**
 * 切换后的主界面
 * 
 * @author nova 日期 2013年2月19日10:16:41
 * 
 */
public class OtherSoftUpdate extends Activity {

	/** 用来记录第一次按下的时间（单位为毫秒） */
	private long firstTime = 0;
	/** 要启动的动画控件 */
	private ImageView image1;
	private ImageView image_back;
	private TextView text;
	private SaveAppInfo saveAppInfo = new SaveAppInfo(this);
	private ArrayList<UpdataInfo> updataInfos = new ArrayList<UpdataInfo>();
	/** 本软件升级的实体类 */
	private CurrentUpdataInfo currentUpdataInfo;
	private Dialog alertNoDialog;
	/** 装机必备进入次数 */
	private int enterSum = 0;
	/** */
	private static final int UPDATA_CLIENT = 1;
	/** 获取服务器更新信息失败 */
	private static final int GET_UNDATAINFO_ERROR = 2;
	/** 下载新版本失败 */
	private static final int DOWN_ERROR = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main2);

		// 依次查找控件
		FrameLayout relativeLayoutsj = (FrameLayout) findViewById(R.id.softupdate_imageview);
		// 触发事件
		relativeLayoutsj.setOnClickListener(softUpdate);
		image1 = (ImageView) findViewById(R.id.soft_update_start);
		image_back = (ImageView) findViewById(R.id.soft_update_back);
		text = (TextView) findViewById(R.id.testtextview);
		// 获取升级的数目
		updataInfos = saveAppInfo.getScrollData();
		// relativeLayoutsj.setBackgroundColor(Color.TRANSPARENT);
		// 在动画中设置要升级的数目
		text.setText(updataInfos.size() + "");
		// if(updataInfos.size()>0){
		text.setVisibility(View.VISIBLE);
		MyAnimation animation = new MyAnimation(this, image_back, image1, text,
				1000);
		animation.startAnimation(getApplicationContext());
		// }
		
		LinearLayout linearLayoutxz = (LinearLayout) findViewById(R.id.softuninstall_imageview);
		LinearLayout linearLayoutgl = (LinearLayout) findViewById(R.id.managerInstallpackage_imageview);
		LinearLayout linearLayoutbj = (LinearLayout) findViewById(R.id.softwaremove_imageview);
		LinearLayout linearLayoutbb = (LinearLayout) findViewById(R.id.installnecessary_imageview);
		LinearLayout imageViewfeedback = (LinearLayout) findViewById(R.id.soft_main_feedback);
		ImageView imageViewupdate = (ImageView) findViewById(R.id.soft_main_update);
		ImageView linearLayoutabout = (ImageView) findViewById(R.id.soft_main_about);

		linearLayoutxz.setOnClickListener(softUninstall);
		linearLayoutgl.setOnClickListener(managerInstallpackage);
		linearLayoutbj.setOnClickListener(softwaremove);
		linearLayoutbb.setOnClickListener(installnecessary);
		imageViewfeedback.setOnClickListener(mainfeedBack);
		imageViewupdate.setOnClickListener(mainUpdate);
		linearLayoutabout.setOnClickListener(mainAbout);
	}

	/**
	 * 软件升级
	 */
	OnClickListener softUpdate = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this, SoftUpdate.class);
			if(updataInfos.size()==0){
				Toast.makeText(OtherSoftUpdate.this, "恭喜您所有软件都是最新的", 1).show();
			}else{
				startActivity(intent);
			}
			
			
		}
	};

	/**
	 * 软件卸载
	 */
	OnClickListener softUninstall = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this,
					SoftUninstall.class);
			startActivity(intent);
		}
	};

	/**
	 * 安装包管理
	 */
	OnClickListener managerInstallpackage = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this,
					ManagerInstallPackage.class);
			startActivity(intent);
		}
	};

	/**
	 * 软件搬家
	 */
	OnClickListener softwaremove = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this, SoftwareMove.class);
			startActivity(intent);
		}
	};

	/**
	 * 装机必备
	 */
	OnClickListener installnecessary = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this,
					InstallNecessary.class);
			startActivity(intent);
			enterSum++;
			SoftManagerPreference.saveSoftNeccessaryEnter(OtherSoftUpdate.this,
					enterSum);
		}
	};

	/**
	 * 反馈
	 */
	OnClickListener mainfeedBack = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this,
					Soft_Main_Abuot.class);
			startActivity(intent);
		}
	};

	/**
	 * 更新
	 */
	OnClickListener mainUpdate = new OnClickListener() {
		public void onClick(View v) {
			handler.post(new CheckVersionTask());
		}
	};
	/**
	 * 关于
	 */
	OnClickListener mainAbout = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this,
					Soft_Main_FeedBack.class);
			startActivity(intent);
		}
	};

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
				Toast.makeText(getApplicationContext(),
						R.string.softmanager_update_error, 1).show();
				break;
			case DOWN_ERROR:
				Toast.makeText(getApplicationContext(),
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
				String path = "http://client.3gyu.com/p_info.php?pname=com.g365.softmanager";
				// 包装成url的对象
				URL url = new URL(path);
				// 打开网络连接
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setConnectTimeout(5000);
				InputStream is = connection.getInputStream();
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
				if (version.equals(versionName)) {
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

	/*
	 * 弹出对话框通知用户更新程序
	 */
	protected void showUpdataDialog() {
		final Dialog alertDialog = new Dialog(OtherSoftUpdate.this,
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
	 * 无版本升级对话框
	 */
	public void showNoUpdateDialog() {
		alertNoDialog = new Dialog(OtherSoftUpdate.this, R.style.Theme_dialog);
		alertNoDialog.setContentView(R.layout.check_no_update);
		alertNoDialog.findViewById(R.id.check_noupdate_comfirm)
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						alertNoDialog.cancel();
					}
				});
		alertNoDialog.show();
	}

	/**
	 * 应用两次退出程序
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 1000) {// 如果两次按键时间间隔大于800毫秒，则不退出
				Toast.makeText(OtherSoftUpdate.this, "再按一次返回键退出",
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
				return true;
			} else {
				System.exit(0);// 否则退出程序
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 返回到主界面 从新刷新升级的数目
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// 获取升级的数目
		updataInfos = saveAppInfo.getScrollData();
		// relativeLayoutsj.setBackgroundColor(Color.TRANSPARENT);
		// 在动画中设置要升级的数目
		//text.setText(updataInfos.size() + "");
	}
}
