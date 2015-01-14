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
 * �л����������
 * 
 * @author nova ���� 2013��2��19��10:16:41
 * 
 */
public class OtherSoftUpdate extends Activity {

	/** ������¼��һ�ΰ��µ�ʱ�䣨��λΪ���룩 */
	private long firstTime = 0;
	/** Ҫ�����Ķ����ؼ� */
	private ImageView image1;
	private ImageView image_back;
	private TextView text;
	private SaveAppInfo saveAppInfo = new SaveAppInfo(this);
	private ArrayList<UpdataInfo> updataInfos = new ArrayList<UpdataInfo>();
	/** �����������ʵ���� */
	private CurrentUpdataInfo currentUpdataInfo;
	private Dialog alertNoDialog;
	/** װ���ر�������� */
	private int enterSum = 0;
	/** */
	private static final int UPDATA_CLIENT = 1;
	/** ��ȡ������������Ϣʧ�� */
	private static final int GET_UNDATAINFO_ERROR = 2;
	/** �����°汾ʧ�� */
	private static final int DOWN_ERROR = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main2);

		// ���β��ҿؼ�
		FrameLayout relativeLayoutsj = (FrameLayout) findViewById(R.id.softupdate_imageview);
		// �����¼�
		relativeLayoutsj.setOnClickListener(softUpdate);
		image1 = (ImageView) findViewById(R.id.soft_update_start);
		image_back = (ImageView) findViewById(R.id.soft_update_back);
		text = (TextView) findViewById(R.id.testtextview);
		// ��ȡ��������Ŀ
		updataInfos = saveAppInfo.getScrollData();
		// relativeLayoutsj.setBackgroundColor(Color.TRANSPARENT);
		// �ڶ���������Ҫ��������Ŀ
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
	 * �������
	 */
	OnClickListener softUpdate = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this, SoftUpdate.class);
			if(updataInfos.size()==0){
				Toast.makeText(OtherSoftUpdate.this, "��ϲ����������������µ�", 1).show();
			}else{
				startActivity(intent);
			}
			
			
		}
	};

	/**
	 * ���ж��
	 */
	OnClickListener softUninstall = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this,
					SoftUninstall.class);
			startActivity(intent);
		}
	};

	/**
	 * ��װ������
	 */
	OnClickListener managerInstallpackage = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this,
					ManagerInstallPackage.class);
			startActivity(intent);
		}
	};

	/**
	 * ������
	 */
	OnClickListener softwaremove = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this, SoftwareMove.class);
			startActivity(intent);
		}
	};

	/**
	 * װ���ر�
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
	 * ����
	 */
	OnClickListener mainfeedBack = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this,
					Soft_Main_Abuot.class);
			startActivity(intent);
		}
	};

	/**
	 * ����
	 */
	OnClickListener mainUpdate = new OnClickListener() {
		public void onClick(View v) {
			handler.post(new CheckVersionTask());
		}
	};
	/**
	 * ����
	 */
	OnClickListener mainAbout = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(OtherSoftUpdate.this,
					Soft_Main_FeedBack.class);
			startActivity(intent);
		}
	};

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
	 * �ӷ�������ȡxml���������бȶ԰汾��
	 */
	public class CheckVersionTask implements Runnable {
		public void run() {
			try {
				String versionName = getCurrentSystemVersionName();
				// ��ȡ��������ַ
				String path = "http://client.3gyu.com/p_info.php?pname=com.g365.softmanager";
				// ��װ��url�Ķ���
				URL url = new URL(path);
				// ����������
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

	/*
	 * �����Ի���֪ͨ�û����³���
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
	 * �ް汾�����Ի���
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
	 * Ӧ�������˳�����
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 1000) {// ������ΰ���ʱ��������800���룬���˳�
				Toast.makeText(OtherSoftUpdate.this, "�ٰ�һ�η��ؼ��˳�",
						Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// ����firstTime
				return true;
			} else {
				System.exit(0);// �����˳�����
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * ���ص������� ����ˢ����������Ŀ
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		// ��ȡ��������Ŀ
		updataInfos = saveAppInfo.getScrollData();
		// relativeLayoutsj.setBackgroundColor(Color.TRANSPARENT);
		// �ڶ���������Ҫ��������Ŀ
		//text.setText(updataInfos.size() + "");
	}
}
