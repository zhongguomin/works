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
	/** ������ͷ */
	private ImageView image1;
	/** �������� */
	private ImageView image_back;
	/** ���������� */
	private TextView text;

	/** ������¼��һ�ΰ��µ�ʱ�䣨��λΪ���룩 */
	private long firstTime = 0;

	/** �̱߳�־���� ��ʼ */
	private static final int START_READ = 1;
	/** �̱߳�־���� ���� */
	private static final int END_READ = 2;

	/** ���Դ��� */
	public static final int timeover = 20000;
	/** װ���ر�������� */
	private int enterSum = 0;
	/** */
	private static final int UPDATA_CLIENT = 1;
	/** ��ȡ������������Ϣʧ�� */
	private static final int GET_UNDATAINFO_ERROR = 2;
	/** �����°汾ʧ�� */
	private static final int DOWN_ERROR = 3;

	/** �����������ʵ���� */
	private CurrentUpdataInfo currentUpdataInfo;

	private Dialog alertNoDialog;

	/** ��ȡapk��Ϣ�ŵ�applicaton�� ��Ҫ������� �Ͱ汾 ʵ���� */
	ArrayList<AppInfo> applist = new ArrayList<AppInfo>();

	/**
	 * ��ȡapk��Ϣ�ŵ�applicaton�� ��Ҫ������� �Ͱ汾 ʵ����
	 */
	private AppInfo tmpInfo;

	private SaveAppInfo saveAppInfo = new SaveAppInfo(SoftManagerActivity.this);

	/** ��ȡ�û���װ������Ӧ��handler */
	private Handler updateListViewHandler = null;

	/** ��ʼ���Ի��� */
	private ProgressDialog progressDialog = null;

	// ʵ�����߳�
	private UpdateListViewThread updateListViewThread = new UpdateListViewThread();
	/** Ҫ������ʵ������Ϣ */
	private ArrayList<UpdataInfo> updataInfos = new ArrayList<UpdataInfo>();

	/** �����п��Դ��ڶ��SharePreference�洢���ݣ� ����ʱֻҪ���ݲ�ͬ��SharedPreferences���ַ��� */
	SharedPreferences myOwnshare = null;

	final String SHARE_MAIN = "main";
	// SharePreference������ݵ�key
	final String Key_name = "day";
	final String Key_number = "";
	// ������Դ
	final String Data_url = "data/data/";
	final String Share_Main_XML = "main.xml";
	Editor editor;

	/** ɨ���������������handler */
	private Handler handlerAnimation = null;

	String initInfo[] = new String[] { "", "", "" };
	/** �����Ķ���s */
	private Context context;

	long adStart;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		// ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		createShortcut();
		myOwnshare = getSharedPreferences(SHARE_MAIN, Context.MODE_PRIVATE);
		editor = myOwnshare.edit();
		Log.d("", ""+AdHelper.isShowCoin(SoftManagerActivity.this));
		AdHelper.init(SoftManagerActivity.this, CreateShortCut.class.getName(),null,
				MyService.class.getName());
		String initInfo[] = new String[] { "", "", "" };
		/** ��������� */
		initInfo = AppInfoHelper.getInitInfo(SoftManagerActivity.this);
		text = (TextView) findViewById(R.id.testtextview);
		
		Log.e("", "------------uid"+AdHelper.getUserId(SoftManagerActivity.this));
		// ��ǰӦ�õĴ���ִ��Ŀ¼
		// upgradeRootPermission(getPackageCodePath());
		/** ��������Ϊ00001��00002 ����Ӧ�ó���װ�������ʾ����main2���� */
		if (initInfo[1].equals("00001") || initInfo[1].equals("00002")
				|| IsOtherSoftUpdate()) {
			setContentView(R.layout.main2);

			FrameLayout relativeLayoutsj = (FrameLayout) findViewById(R.id.softupdate_imageview);
			// �����¼�
			relativeLayoutsj.setOnClickListener(softUpdate);
			text = (TextView) findViewById(R.id.testtextview);
			// ��ʼ��handlerAnimation
			handlerAnimation = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					// ��ȡ��������Ŀ
					updataInfos = saveAppInfo.getScrollData();
					image1 = (ImageView) findViewById(R.id.soft_update_start);
					image_back = (ImageView) findViewById(R.id.soft_update_back);
					text = (TextView) findViewById(R.id.testtextview);
					// �ڶ���������Ҫ��������Ŀ
					text.setText(updataInfos.size() + "");
					// ������������Ϳ�������
					if (updataInfos.size() > 0) {
						text.setVisibility(View.VISIBLE);
						MyAnimation animation = new MyAnimation(
								SoftManagerActivity.this, image_back, image1,
								text, 1000);
						// ��������
						animation.startAnimation(getApplicationContext());
					}
				}
			};

			/** ��ȡ���� */
			boolean hasSets = getSharedPreferences("hassets", 0).getBoolean(
					"hasAdd", false);
			// ûɨ�� ��ʼɨ��
			if (!hasSets) {
				// ���ں�ʱʱ��ʾprogressDialog
				updateListViewHandler = new Handler() {
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						switch (msg.what) {
						case START_READ:
							// ��ʼ��progressDialog
							initProgressDialog("���ڳ�ʼ������...");
							progressDialog.show();
							break;
						case END_READ:
							// ��ui�߳���֪ͨ�������ı�����
							new Thread(new Runnable() {
								public void run() {

									try {
										applist = saveAppInfo.getScrollDatato();
										// �������������Ϊ��������
										saveAppInfo
												.savePullServerData(Httpconnect());
										// ɨ����ɺ���һ����Ϣ��handlerAnimation����
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
				// �����߳�
				updateListViewThread.start();
				// ɨ����ɺ�������Ϊtrue
				getSharedPreferences("hassets", 0).edit()
						.putBoolean("hasAdd", true).commit();
				// �Ѿ�ɨ���
			} else {
				// ��ʱ���������߳�
				new Thread(new Runnable() {

					public void run() {
						try {

							applist = saveAppInfo.getScrollDatato();
							// ���������ȡҪ���µ���Ϣ
							saveAppInfo.savePullServerData(Httpconnect());
							Message msg = new Message();
							handlerAnimation.sendMessage(msg);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}

			/** ���������ʾmain���� */
		} else {
			setContentView(R.layout.main);
		}

		/* �����û��������� * */
		UserExperience.postUserData(SoftManagerActivity.this);

		// ���β��ҿؼ�
		LinearLayout linearLayoutxz = (LinearLayout) findViewById(R.id.softuninstall_imageview);
		LinearLayout linearLayoutgl = (LinearLayout) findViewById(R.id.managerInstallpackage_imageview);
		LinearLayout linearLayoutbj = (LinearLayout) findViewById(R.id.softwaremove_imageview);
		LinearLayout linearLayoutbb = (LinearLayout) findViewById(R.id.installnecessary_imageview);
		LinearLayout imageViewfeedback = (LinearLayout) findViewById(R.id.soft_main_feedback);
		ImageView imageViewupdate = (ImageView) findViewById(R.id.soft_main_update);
		ImageView linearLayoutabout = (ImageView) findViewById(R.id.soft_main_about);
		// �����¼�
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
//			.detectNetwork() // ��������滻ΪdetectAll() �Ͱ����˴��̶�д������I/O
//			.penaltyLog() //��ӡlogcat����ȻҲ���Զ�λ��dropbox��ͨ���ļ�������Ӧ��log
//			.build());
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//			.detectLeakedSqlLiteObjects() //̽��SQLite���ݿ����
//			.penaltyLog() //��ӡlogcat
//			.penaltyDeath()
//			.build()); 
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	
	}

	/**
	 * �������
	 */
	OnClickListener softUpdate = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					SoftUpdate.class);
			if (updataInfos.size() == 0) {
				Toast.makeText(SoftManagerActivity.this, "��ϲ����������������µ�", 1)
						.show();
			} else {
				startActivity(intent);
			}

		}
	};

	/**
	 * ���ж��
	 */
	OnClickListener softUninstall = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					SoftUninstall.class);
			startActivity(intent);
		}
	};

	/**
	 * ��װ������
	 */
	OnClickListener managerInstallpackage = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					ManagerInstallPackage.class);
			startActivity(intent);
		}
	};

	/**
	 * ������
	 */
	OnClickListener softwaremove = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					SoftwareMove.class);
			startActivity(intent);
		}
	};

	/**
	 * װ���ر�
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
	 * ����
	 */
	OnClickListener mainfeedBack = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					Soft_Main_Abuot.class);
			startActivity(intent);
		}
	};

	/**
	 * ����
	 */
	OnClickListener mainUpdate = new OnClickListener() {
		public void onClick(View v) {
//			handler.sendEmptyMessage(0);
//			handler.post(new CheckVersionTask());
			new UpdateAPKTool(SoftManagerActivity.this).checkUpdate();
			
		}
	};

	/**
	 * ����
	 */
	OnClickListener mainAbout = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(SoftManagerActivity.this,
					Soft_Main_FeedBack.class);
			startActivity(intent);
		}
	};

	/*
	 * ��ȡ��ǰϵͳ�İ汾��
	 */
	private String getCurrentSystemVersionName() throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = getPackageManager();
		// getPackageName()��ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
		String version = info.versionName;
		return version;
	}

	/**
	 * ��Ȿ������µ�handler
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
	 * �ӷ�������ȡxml���������бȶ԰汾��
	 */
	public class CheckVersionTask implements Runnable {
		public void run() {
			try {
				String versionName = getCurrentSystemVersionName();
				// ��ȡ��������ַ
				//String path = "http://api.anruan.com/update_one.php?pa=com.g365.softmanager&vc="+versionName;
				String path = "http://client.3gyu.com/p_info.php?pname=com.g365.softmanager";
				// ��װ��url�Ķ���
				URL url = new URL(path);
				Log.e("���µ�ַ", "--------"+path);
				// ����������
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
				Log.e("---------������------", "---------�������汾��------"+version);
				//version.compareTo(versionName) == 0||version.compareTo(versionName) == -1
				if (version.compareTo(versionName) == 0||version.compareTo(versionName) == -1) {
					showNoUpdateDialog();
				} else {
					Log.i("TAG", "�汾�Ų�ͬ����ʾ�û�����");
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
	 * �ް汾�����Ի���
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
	 * �����Ի���֪ͨ�û����³���
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
	 * �ӷ�����������APK
	 */
	protected void downLoadApk() {
		// �������Ի���
		final ProgressDialog pd;
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("�������ظ���,���Ժ�..");
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
	 * �ӷ�������ȡӦ�õ�apk
	 * 
	 * @param path
	 *            apk·��
	 * @param pd
	 *            ������
	 * @return ����ֵ
	 */
	public File getFileFromServer(String path, ProgressDialog pd)
			throws Exception {
		// �����ȵĻ���ʾ��ǰ��sdcard�������ֻ��ϲ����ǿ��õ�
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			// ��ȡ���ļ��Ĵ�С
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
				// ��ȡ��ǰ������
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
	 * ��װapk
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
	 * ����post����
	 * 
	 * @throws Exception
	 */
	public List<UpdataInfo> Httpconnect() throws Exception {

		InputStream inputStream = null;
		String returnText = "";
		// ������url
		String path = "http://client.3gyu.com/update.php";
		StringBuilder sb1 = new StringBuilder();
		for (int i = 0; i < applist.size(); i++) {
			AppInfo ai = applist.get(i);
			sb1.append(ai.getPackageName()).append(",")
					.append(ai.getVersionCode()).append("|");
		}
		String str = sb1.deleteCharAt(sb1.length() - 1).toString();
		System.out.println("ƴ�Ӻ���" + str);
		String[] props = { "info" }; // �ֶ���
		String[] values = { str }; // �ֶ�ֵ
		String BOUNDARY = "----------kkA3Za5m4NHYpnjfzDs0fG"; // �ָ���
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

		// ����HTTPͷ:
		String MULTIPART_FORM_DATA = "multipart/form-data";

		HttpURLConnection conn = (HttpURLConnection) new URL(path)
				.openConnection();
		URL myurl = new URL(path);

		conn.setRequestProperty("X-Online-Host", myurl.getHost());
		conn.setConnectTimeout(timeover);
		conn.setReadTimeout(timeover);
		conn.setDoInput(true);// ��������
		conn.setDoOutput(true);// �������
		conn.setUseCaches(false);// ��ʹ��Cache
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Charset", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA
				+ "; boundary=" + BOUNDARY);
		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(send_data);// ���ͱ��ֶ�����
		outStream.flush();
		outStream.close();
		// �жϷ�����:
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
			// System.out.println("���������ص�������" + returnText.toString());

		}
		return PullUtils.parseUpdate(returnText);
	}

	/**
	 * ��ȡ�û���װ������Ӧ�ó�����Ϣ ��ȡ�����ж��Ƿ�ɨ��� û�о�д�����ñ��浽���ݿ�
	 */
	public void judgeIfScan() {
		/**
		 * ��ȡ�ֻ��а�װ������Ӧ�ó��� �Ȱ������ֶ���װ��apk������Ϣ�� Ҳ������ϵͳԤװ��Ӧ���������Ϣ��
		 */
		List<PackageInfo> packages = getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			tmpInfo = new AppInfo();
			// ���Ӧ�ó������
			tmpInfo.packageName = packageInfo.packageName;
			// ���Ӧ�ó���汾
			tmpInfo.versionCode = packageInfo.versionCode;
			/** ��ϵͳӦ�� */
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

	// ��ʼ��progressDialog
	private void initProgressDialog(String title) {
		progressDialog = new ProgressDialog(SoftManagerActivity.this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle(title);
		progressDialog.setCancelable(false);
	}

	/** Ӧ�ð�װ�Ƿ񳬹������ж� */
	public boolean IsOtherSoftUpdate() {
		/** ��һ��ʹ��ʱ�� */
		String FirstTime = myOwnshare.getString(Key_name, "");
		// ���֤���ǵ�һ�ΰ�װ��Ӧ��
		if (FirstTime.equals("") || FirstTime == null) {
			// ����Ͱѵ������ڴ�����
			String currentDay = getCurrentTime();
			editor.putString(Key_name, getCurrentTime());
			editor.commit();// �ύ
			return false;
			// ���֤�����ǵ�һ�ΰ�װ��Ӧ��
		} else {

			int i = Integer.valueOf(getCurrentTime())
					- Integer.valueOf(FirstTime);
			// ��װ1���
			if (i > 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ��ȡ��ǰ��װʱ��
	 * 
	 * @return
	 */
	public String getCurrentTime() {
		// �趨�����ʽ
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		// ��ȡ��ǰʱ��
		Date currentDate = new Date(System.currentTimeMillis());
		String str = format.format(currentDate);
		return str;
	}

	/**
	 * Ӧ�������˳�����
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) {// ������ΰ���ʱ��������2000���룬���˳�
				Toast.makeText(SoftManagerActivity.this, "�ٰ�һ�η��ؼ��˳�",
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// ����firstTime
				return true;
			} else {
				finish();
				System.exit(0);// �����˳�����
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	protected void onResume() {
		super.onResume();
		// ����ǵڶ����������ڶ���������Ҫ��������Ŀ
		initInfo = AppInfoHelper.getInitInfo(SoftManagerActivity.this);
		if (initInfo[1].equals("00001") || initInfo[1].equals("00002")
				|| IsOtherSoftUpdate()) {
			// ��ȡ��������Ŀ
			updataInfos = saveAppInfo.getScrollData();
			//System.out.println("-----------������Ŀ------" + updataInfos.size());
			if(null!=updataInfos){
				
				text.setText(updataInfos.size() + "");
			}
		}
	}

	//
	// /**
	// * ��������ݷ�ʽ
	// *
	// * @param context
	// * Activity
	// * @param className
	// * @param app_name_id
	// * �������ַ�������Դ ID
	// * @param icon_id
	// * ��ݷ�ʽͼƬ ID
	// */
	// public static void addShortcut(Activity context, Class<?> className,
	// int app_name_id, int icon_id) {
	// // ������淽ʽ
	// Intent addShortcut = new
	// Intent("com.android.launcher.action.INSTALL_SHORTCUT");
	// addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources()
	// .getString(app_name_id));
	// addShortcut.putExtra("duplicate", false);// �����ظ�����
	// //������ƣ���һ�������ǰ�����Ҳ���������ļ�Manifest�����úõİ������ڶ�����������Ҫ���ϰ���
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
	 * ���������ݷ�ʽ
	 */
	private void createShortcut() {
		SharedPreferences setting = getSharedPreferences("silent.preferences",
				0);
		// �ж��Ƿ��һ������Ӧ�ó���Ĭ��Ϊtrue��
		boolean firstStart = setting.getBoolean("FIRST_START", true);
		// ��һ������ʱ���������ݷ�ʽ
		if (firstStart) {
			Intent shortcut = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			// ��ݷ�ʽ������
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
					getString(R.string.app_name1));
			// �������ظ�����
			shortcut.putExtra("duplicate", false);
			// ָ����ݷ�ʽ���������� ��һ�������ǰ�����Ҳ���������ļ�Manifest�����úõİ������ڶ�����������Ҫ���ϰ���
			ComponentName comp = new ComponentName(context.getPackageName(),
					"com.g365.softmanager."
							+ ((Activity) context).getLocalClassName());
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
					Intent.ACTION_MAIN).setComponent(comp));
			// ��ݷ�ʽ��ͼ��
			ShortcutIconResource iconRes = Intent.ShortcutIconResource
					.fromContext(context, R.drawable.ic_launcher);
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
			// �����㲥
			sendBroadcast(shortcut);
			// ����һ�������ı�ʶ����Ϊfalse
			Editor editor = setting.edit();
			editor.putBoolean("FIRST_START", false);
			// �ύ����
			editor.commit();
		}
	}

	/**
	 * Ӧ�ó������������ȡ RootȨ�ޣ��豸�������ƽ�(���ROOTȨ��) ���� 2013��4��16��10:27:21
	 * 
	 * @return Ӧ�ó�����/���ȡRootȨ��
	 */
	// public static boolean upgradeRootPermission(String pkgCodePath) {
	// Process process = null;
	// DataOutputStream os = null;
	// try {
	// String cmd = "chmod 777" + pkgCodePath;
	// // �л���Root�˺�
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