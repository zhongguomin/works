package com.g365.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.g365.entity.AutoUpdateInfo;
import com.g365.softmanager.R;


/**
 * @类名: UpdateAPKTool
 * 
 * @作者: Nova
 * 
 * @日期: 2014年1月17日11:22:40
 * 
 * @描述: 类<code>UpdateAPKTool</code>当前软件升级的工具类</p>
 * 
 *      Copyright 2014 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 */
public class UpdateAPKTool {

	private final static String TAG = "com.youdu.utils.UpdateAPKTool";
	private final static int DOWN = 0;
	private final static int DOWN_FALIE = -1;
	private final static int FINISH = 1;
	private final static int CHECK_FAILE = 4;
	private final static int CHECK_NO = 2;
	private final static int CHECK_SUCCESS = 3;
	private Context context;
	/** 视频缓存文件夹 */
	private static final String DEFAULT_FODLER = "/MyCache/download/";
	private Dialog downLoadDialog;
	/** 应用名 */
	private String apkName;
	/** 下载进度*/
	private int progress;
	
	/** 下载目录*/
	private String dir;
	/** 更新接口地址*/
	private String updateURL;
	private String checkContent;
	
	private ProgressDialog pd;
	private Map<String, AutoUpdateInfo> infoList = new HashMap<String, AutoUpdateInfo>();
	private final static String KEY = "update";
	int versionCode;
	public UpdateAPKTool(Context context) {
		this.context = context;
	}

	public void checkUpdate() {
		 versionCode = getVetsionCode();
		updateURL = "http://api.anruan.com/update_one.php?pa="
				+ context.getPackageName() + "&vc=" + versionCode;
		System.out.println("dddddddddddddddd"+updateURL);
		new Thread(new checkUpdateRunnable()).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case DOWN_FALIE:// 下载失败
				downLoadDialog.dismiss();
				break;
			case DOWN:// 下载中
				pd.setProgress(progress);
				break;
			case FINISH:// 下载成功
				installOwnApk(context, dir, apkName);
				pd.cancel();
				break;
			case CHECK_FAILE: // 更新失败
				// 更新失败
				Log.e(TAG, "更新失败，包名不存在");
				break;
			case CHECK_NO:// 不需要更新
				showNoUpdateDialog();
				Log.e(TAG, "已经是最新版本，不需要更新");
				break;
			case CHECK_SUCCESS:// 更新成功
				
				
					Log.e(TAG, "-------------更新成功---");
					showUpdateVersionDialog();
				
				break;

			default:
				break;
			}
		}
	};

	private void saveAutoUpdateInfo(String autoUpdateInfo) {
		try {
			JSONObject json_data = new JSONObject(autoUpdateInfo);
			
				AutoUpdateInfo info = new AutoUpdateInfo(
						json_data.getString("id"),
						json_data.getString("type"),
						json_data.getString("icon"),
						json_data.getString("size"),
						json_data.getString("versioncode"),
						json_data.getString("language"),
						json_data.getString("down"),
						json_data.getString("star"),
						json_data.getString("lastupdate"),
						
						
						json_data.getString("file"));
				infoList.put(KEY, info);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void downloadApk() {
		new Thread(new downloadApkRunnable()).start();
	}

	/** 更新提示 */
	public void showUpdateVersionDialog() {
//		AlertDialog dialog = new AlertDialog.Builder(context)
//				.setTitle(context.getString(R.string.check_update1))
//				.setMessage(context.getString(R.string.check_update2))
//				.setPositiveButton(R.string.check_update3,
//						new OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								dialog.dismiss();
//								showUpdataDialog();
//							}
//						})
//				.setNegativeButton(R.string.check_update4,
//						new OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,
//									int which) {
//								dialog.dismiss();
//
//							}
//						}).create();
//		dialog.show();
//		dialog.setCanceledOnTouchOutside(false);
		
		final Dialog alertDialog = new Dialog(context,
				R.style.Theme_dialog);
		alertDialog.setContentView(R.layout.check_update_confirm);
		alertDialog.findViewById(R.id.check_hasupdate_comfirm)
				.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						//downLoadApk();
						alertDialog.cancel();
						showUpdataDialog();
					}
				});

		alertDialog.findViewById(R.id.check_hasupdate_cancel)
				.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						alertDialog.cancel();
					}
				});
		alertDialog.show();

	}

	/**
	 * 弹出对话框通知用户更新程序
	 */
	private void showUpdataDialog() {
	
		pd = new ProgressDialog(context);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新,请稍候..");
		pd.setMax(100);
		
		
		pd.show();
		// 现在文件
		downloadApk();

	}

	/**
	 * 无版本升级对话框
	 */
	private void showNoUpdateDialog() {
//		AlertDialog.Builder  builder = new Builder(context);
//		
//		//builder.setTitle(context.getString(R.string.check_update6));
//		final LayoutInflater inflater = LayoutInflater.from(context);
//		View v = inflater.inflate(R.layout.check_no_update, null);
//	   Button but = (Button)v.findViewById(R.id.check_no_updateconfirm);
//	   	
//	   
//		builder.setView(v);
//		final AlertDialog dialog = builder.create();
//		
//		but.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		    });
//	Window window=dialog.getWindow();
//	window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//	window.setGravity(Gravity.CENTER);
//		dialog.show();
		/*downLoadDialog = builder.setNegativeButton(R.string.video_play_confirm,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						downLoadDialog.dismiss();
					}
				}).create();*/
		//downLoadDialog.show();
	//	downLoadDialog.setCanceledOnTouchOutside(false);
		
		final Dialog 	alertNoDialog = new Dialog(context, R.style.Theme_dialog);
		alertNoDialog.setContentView(R.layout.check_no_update);
		alertNoDialog.findViewById(R.id.check_noupdate_comfirm)
				.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						alertNoDialog.cancel();

					}
				});
		alertNoDialog.show();
		
		
		
		
		

	}

	private class checkUpdateRunnable implements Runnable {
	
		public void run() {
			checkContent = UrlHelper.getInputStreamString(updateURL.trim());
			//Log.e(TAG, "22222222222222222222"+checkContent);
			Message msg = new Message();
			if (checkContent == null || checkContent.equals("")) {
				msg.what = CHECK_NO;
				//msg.what = CHECK_SUCCESS;
				Log.e(TAG, "checkContent == null || checkContent.endsWith");
			} else if (checkContent.equals("-1")) { //不存在包名 返回-1
				msg.what = CHECK_FAILE;
			} else if (checkContent.equals("0")) {//没有新版本返回0
				Log.e(TAG, "checkContent.equals(333333333)");
				msg.what = CHECK_NO;
				//msg.what = CHECK_SUCCESS;
			} else {//其它返回有更新
				 saveAutoUpdateInfo(checkContent);
				 Log.e(TAG, "---8888888888888888---"+versionCode);
				if(Integer.parseInt(infoList.get(KEY).getVersion())>versionCode){//版本比较
					 Log.e(TAG, "-------------"+versionCode);
				   msg.what = CHECK_SUCCESS;
				}else{
					msg.what = CHECK_NO;
				}
			}

			handler.sendMessage(msg);
		}
	}

	public int getVetsionCode() {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Log.e("TAG", "版本获取异常");
		}
		return versionCode;
	}

	private class downloadApkRunnable implements Runnable {
		public void run() {
			doDownload();
		}
	}

	public void doDownload() {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				dir = Environment.getExternalStorageDirectory()
						+ DEFAULT_FODLER;
				File file = new File(dir);
				if (!file.exists()) {
					file.mkdirs();
				}
				//String sre="http://file.3gyu.com/soft/0/anruan/00001_00003/anruanMarket_v1.4.3.apk";
				URL url = new URL(infoList.get(KEY).getFileurl());
				//URL url = new URL(sre);
				// 创建连接
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// conn.setReadTimeout(5 * 1000);// 设置超时时间
				// conn.setRequestMethod("GET");
				conn.connect();
				// 获取文件大小
				int length = conn.getContentLength();
				// 创建输入
				is = conn.getInputStream();
				apkName = FileHelper.subFileName1(infoList.get(KEY)
						.getFileurl());
				//apkName = StringTools.subFileName1(sre);
				File apkFile = new File(dir, apkName);
				fos = new FileOutputStream(apkFile);
				int count = 0;
				int numread = -1;
				// 缓存
				byte buf[] = new byte[1024];

				while ((numread = is.read(buf)) > 0) {
					count += numread;
					// 计算进度条
					progress = (int) (((float) count / length) * 100);
					Log.e(TAG, progress + "");
					// 更新进度
					Message message = new Message();
					message.what = DOWN;
					handler.sendMessage(message);
					fos.write(buf, 0, numread);
				}
				fos.flush();
				// 下载完成
				Message message2 = new Message();
				message2.what = FINISH;
				handler.sendMessage(message2);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "doDownload");
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * {安装该程序}
	 * @param       {Context，String,String}   {上下文对象，文件存储路径，文件名}
	 * @return      {null}   {null}
	 * @exception   {包名不存在}
	 */
	public static void installOwnApk(Context context,String path, String name){
		
		File file = new File(path, name);
		if(!file.exists()){
			return ;
		}
		 // 通过Intent安装APK文件   
		Intent intent = new Intent(Intent.ACTION_VIEW);  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		intent.setDataAndType(
				Uri.fromFile(file), "application/vnd.android.package-archive"
			);  
		context.startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	
	
	
	
	
	public static void checkUpdate(Context context) {

		if (!NetInfoHelper.isNetworkAvailable(context)) {
			//no available
			return;
		}
		String url = "http://api.anruan.com";
		HttpClient client = new DefaultHttpClient();
		HttpGet hg = new HttpGet(url + "?pa=" + context.getPackageName());
		BufferedReader br = null;
		try {
			HttpResponse response = client.execute(hg);
			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jobject = new JSONObject(sb.toString());
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			context.getSharedPreferences("daily_check_update", Context.MODE_PRIVATE).edit().putLong("point", System.currentTimeMillis()).commit();
			if (pinfo.versionCode < jobject.getInt("versioncode")) {
				String size = jobject.getString("size");
				String file = jobject.getString("file");
				//
			} else {
				//no update
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
