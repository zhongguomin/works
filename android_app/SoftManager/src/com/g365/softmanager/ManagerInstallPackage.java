package com.g365.softmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.g365.entity.AppInfo;
import com.g365.utils.MemoryStatus;
import com.g365.utils.SoftManagerPreference;

/**
 * ��װ��������
 */
public class ManagerInstallPackage extends Activity {

	/**�ļ�����*/
	private ArrayList<File> list = new ArrayList<File>();
	/** ��ȡ�ֻ� sdcard ʣ��ռ乤����*/
	private MemoryStatus memoryStatus;
	/** Ҫ����Ĳ��� */
	private LayoutInflater managerLi;
	/**�Զ��������� */
	private ManagerAdapter managerAdapter;
	/** Ҫ����listview */
	private ListView managerListView;
	/**��װ*/
	private Button managerInstall_Install;
	/** ȷ�ϰ�װ*/
	private Button confirmInstall;
	/** ����װ */
	private Button cancelInstall;
	/** ���Ѱ�װ */
	private TextView remindInstall;
	/** ɾ�� */
	private Button managerInstall_Delete;
	/** ȷ��ɾ�� */
	private Button confirmdelete;
	/** ȡ��ɾ�� */
	private Button canceldelete;
	private TextView remindDelete;
	/** ȫѡ����ȫ��ѡ */
	private CheckBox managerIsOrNot;
	/** handler���� */
	private Handler handler = new Handler();
	/** ��װ������ ռ�ô�С */
	private TextView managerSum, occupySize;
	/**���������*/
	private List<AppInfo> saveAppInfos = new ArrayList<AppInfo>();

	private AppInfo appInfo;
	/**
	 * δ��װapk
	 */
	public static final int APK_UNINSTALL = 0;
	/**
	 * �Ѱ�װapk
	 */
	public static final int APK_INSTALL = 1;
	/**
	 * �Ͱ汾apk
	 */
	public static final int APK_LOWINSTALL = 2;
	/**
	 * �߰汾apk
	 */
	public static final int APK_HIGHINSTALL = 3;
	/**
	 * �ظ���apk
	 */
	public static final int APK_REPEATINSTALL = 4;

	private Handler handler1;
	/**
	 * �����Ķ���
	 */
	private Context context;
	
	/** �ۼư�װ����*/
	private int count=0;
	/** �ۼ�ɾ������*/
	private int count1=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.manager_install_package_main);
		//���߳�this ��ʼ��
		context=this;
		LinearLayout linearLayoutManager = (LinearLayout) findViewById(R.id.manageruninstall_back);
		managerListView = (ListView) findViewById(R.id.managerinstallpackagelistview);
		handler1 = new Handler() {

			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				saveAppInfos.add((AppInfo) msg.obj);
				int position = msg.what;
				managerListView.requestLayout();
				managerAdapter.notifyDataSetChanged();
				managerListView.setSelection(position);
			
			}
		};

		managerListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ��ȡ��Ӧappinfo�е�ischeck������ֵ true false
				boolean flag = saveAppInfos.get(position).isChecked;
				// ��ѡ�е�ischeck������ֵ������
				saveAppInfos.get(position).isChecked = !flag;
				handler.post(new Runnable() {

					public void run() {

						// ֪ͨ���������ݶ�Ӧ��appinfo��ischeck������ֵȻ�����ı� �����checkbox��״̬
						managerAdapter.notifyDataSetChanged();
					}
				});
			}
		});

		// ȫѡ����ȫ��ѡ
		managerIsOrNot = (CheckBox) findViewById(R.id.managerallornot);
		// ��װ
		managerInstall_Install = (Button) findViewById(R.id.managerinstall_install);
		// ɾ��
		managerInstall_Delete = (Button) findViewById(R.id.manageinstall_delete);

		managerSum = (TextView) findViewById(R.id.managersum);
		occupySize = (TextView) findViewById(R.id.occupysize);

		linearLayoutManager.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		/**
		 * ȷ�ϰ�װ
		 */
		managerInstall_Install.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// �����Ի���
				showDialog();
			}
		});

		/**
		 * ȷ��ɾ��
		 */
		managerInstall_Delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// �����Ի���
				showDialog1();
			}
		});

		managerAdapter = new ManagerAdapter(ManagerInstallPackage.this);
		managerListView.setDividerHeight(2);
		if (managerListView != null) {
			managerListView.setAdapter(managerAdapter);
		}
		/**
		 * ȫѡ����ȫ��ѡ
		 */
		managerIsOrNot
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						for (int i = 0; i < saveAppInfos.size(); i++) {
							saveAppInfos.get(i).isChecked = true;
							saveAppInfos.get(i).isChecked = isChecked;
						}
						handler.post(new Runnable() {
							public void run() {
								managerAdapter.notifyDataSetChanged();
								
							}
						});
					}
				});
		getAllFile(Environment.getExternalStorageDirectory());
		
		
		
	}

	public ManagerInstallPackage() {
	}

	public ManagerInstallPackage(Context context) {
		super();
		this.context = context;
	}

	/**
	 * ��װ�Ի���
	 */
	private void showDialog() {
		final Dialog dialog = new Dialog(ManagerInstallPackage.this,
				R.style.dialog1);
		View view = View.inflate(ManagerInstallPackage.this,
				R.layout.managerinstall_install_dialog, null);
		// ��װ����
		remindInstall = (TextView) view
				.findViewById(R.id.managerinstallhaschecked);
		count = 0;
		for (int i = 0; i < saveAppInfos.size(); i++) {
			if (saveAppInfos.get(i).isChecked) {
				count++;
			}
		}
		remindInstall.setText("�Ƿ�װ��ѡ��" + count + "�����");
		dialog.setContentView(view);
		if (count == 0) {
			Toast.makeText(ManagerInstallPackage.this,
					R.string.manageruninstall_not_choice, Toast.LENGTH_SHORT)
					.show();
		} else {
			dialog.show();
		}
		// ȷ�ϰ�װ
		confirmInstall = (Button) view
				.findViewById(R.id.managercomfirmuninstall);
		// ȡ����װ
		cancelInstall = (Button) view.findViewById(R.id.managercanceluninstall);
		cancelInstall.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		confirmInstall.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				installSelected();
				SoftManagerPreference.saveSoftInstallPages(ManagerInstallPackage.this, count);
				dialog.dismiss();
			}
		});
	}

	/**
	 * ɾ���Ի���
	 */
	private void showDialog1() {
		final Dialog dialog = new Dialog(ManagerInstallPackage.this,
				R.style.dialog1);
		View view1 = View.inflate(ManagerInstallPackage.this,
				R.layout.managerinstall_delete_dialogs, null);
		// ɾ������
		remindDelete = (TextView) view1
				.findViewById(R.id.managedeletehaschecked);
		 count1 = 0;
		for (int i = 0; i < saveAppInfos.size(); i++) {
			if (saveAppInfos.get(i).isChecked) {
				count1++;
			}
		}
		remindDelete.setText("�Ƿ�ɾ����ѡ��" + count1 + "����װ��");
		dialog.setContentView(view1);
		if (count1 == 0) {
			Toast.makeText(ManagerInstallPackage.this,
					R.string.manageruninstall_deletenot_choice,
					Toast.LENGTH_SHORT).show();
		} else {
			dialog.show();
		}
		// ȷ��ɾ��
		confirmdelete = (Button) view1.findViewById(R.id.managercomfirdelete);
		// ȡ��ɾ��
		canceldelete = (Button) view1.findViewById(R.id.managercanceldelete);
		canceldelete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		confirmdelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				deleteSelected();
				SoftManagerPreference.saveSoftInstalldelete(ManagerInstallPackage.this, count1);
				dialog.dismiss();
				Toast.makeText(ManagerInstallPackage.this,
						R.string.manager_install_deletesuccess,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * �Զ���������
	 */

	public class ManagerAdapter extends BaseAdapter { // �����Ķ��� Context context;

		private Context context;

		// private List<AppInfo> saveAppInfos;

		/**
		 * ������������ݵĳ�ʼ��
		 * 
		 * @param context
		 * @param saveAppInfos
		 */
		public ManagerAdapter(Context context) {
			this.context = context;
			// this.saveAppInfos=saveAppInfos;
			managerLi = (LayoutInflater) context
					.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		/**
		 * ��������
		 */
		public int getCount() {
			return saveAppInfos.size();
		}

		/**
		 * ȡ������ֵ
		 */
		public Object getItem(int position) {
			return saveAppInfos.get(position);
		}

		/**
		 * ȡ��������id
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * ȡ����Ŀ�Ľ���
		 */
		public View getView(int position, View convertView, ViewGroup parent) {

			final AppInfo managerUnit = saveAppInfos.get(position);
			ViewCache cache;
			if (convertView == null) {
				// �󶨵Ľ���
				convertView = managerLi.inflate(
						R.layout.manager_installpackages_listview, null);
				// ��������
				cache = new ViewCache();
				cache.appIcon = (ImageView) convertView
						.findViewById(R.id.manageicon);
				cache.appName = (TextView) convertView
						.findViewById(R.id.managerappName);
				cache.versionName = (TextView) convertView
						.findViewById(R.id.managerversionName);
				cache.compareVersion = (TextView) convertView
						.findViewById(R.id.compareversion);
				cache.appSize = (TextView) convertView
						.findViewById(R.id.managerappSize);
				cache.checkBox = (CheckBox) convertView
						.findViewById(R.id.managercheckitem);

				convertView.setTag(cache);
			} else {
				cache = (ViewCache) convertView.getTag();
			}
			// ������ischeck��ֵ����checkbox��״̬
			cache.appIcon.setImageDrawable(managerUnit.icon);
			cache.appName.setText(managerUnit.appName);
			cache.versionName.setText("�汾��" + managerUnit.versionName + "��");
			cache.appSize.setText(managerUnit.appSize);
			cache.checkBox.setChecked(managerUnit.isChecked);
			//cache.compareVersion.setTextSize(14);
			//cache.compareVersion.setTextColor(#149aff);
			
			
			// ��װ������
			managerSum.setText(saveAppInfos.size() + "");
			
			@SuppressWarnings("static-access")
			String sd = memoryStatus.formatFileSize(memoryStatus
					.getAvailableInternalMemorySize());
			occupySize.setText(sd);
			
			// ���ݱ�־����
			if (managerUnit.getVersionflag() == ManagerInstallPackage.APK_UNINSTALL) {
				cache.compareVersion.setText("δ��װ");
				
			} else if (managerUnit.getVersionflag() == ManagerInstallPackage.APK_INSTALL) {
				cache.compareVersion.setText("�Ѱ�װ");
			} else if (managerUnit.getVersionflag() == ManagerInstallPackage.APK_LOWINSTALL) {
				cache.compareVersion.setText("�Ͱ汾");
			} else if (managerUnit.getVersionflag() == ManagerInstallPackage.APK_HIGHINSTALL) {
				cache.compareVersion.setText("�߰汾");
			} else if (managerUnit.getVersionflag() == ManagerInstallPackage.APK_REPEATINSTALL) {
				cache.compareVersion.setText("�ظ�");
			}
			
		
			
			return convertView;
		}

		/**
		 * ���湦��
		 */

		private final class ViewCache {
			public ImageView appIcon;
			public TextView appName;
			public TextView versionName;
			public TextView compareVersion;
			public TextView appSize;
			public CheckBox checkBox;

		}
	}

	/**
	 * ��װӦ�ó���
	 * 
	 * @param context
	 *            ������
	 * @param sdcardpath
	 *            APK��·��
	 */
//	public static void installApk(Context context, String filePath) {
//		Uri uri = Uri.parse(filePath);
//		Intent notificationIntent = new Intent(Intent.ACTION_VIEW, uri);
//		notificationIntent.setData(uri);
//		notificationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//				| Intent.FLAG_ACTIVITY_NEW_TASK);
//		notificationIntent.setClassName("com.android.packageinstaller",
//				"com.android.packageinstaller.PackageInstallerActivity");
//		context.startActivity(notificationIntent);// ���ַ�ʽ���ܴ��ݲ���
//		//android.os.Process.killProcess(android.os.Process.myPid());
//	}
	
	
	/** ����ϵͳ��װAPK */
//	public void installApk22(Context context ,String file) {
//	File rootDir = Environment.getExternalStorageDirectory();
//	File path = new File(rootDir.toString() + "//AnRuan//Download");
//
//	String fileName = StringTools.subFileName(file);
//
//	File fileNamePath = new File(path, fileName);
//
//	Intent intent = new Intent(Intent.ACTION_VIEW);
//	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	intent.setDataAndType(Uri.fromFile(fileNamePath),
//	"application nd.android.package-archive");
//	startActivity(intent);
//	}
	
	
	void kk(Context context, String filePath){
		Uri uri = Uri.parse(filePath);
		Intent intent = new Intent();  
		Intent notificationIntent = new Intent(Intent.ACTION_VIEW, uri);
		notificationIntent.setData(uri);
		intent.setAction(android.content.Intent.ACTION_VIEW);  
		intent.setDataAndType(Uri.parse(filePath), "application/vnd.android.package-archive");  
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		context.startActivity(intent);  
	}
	
	public static void installApk(Context context, String apk) {
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.fromFile(new File(apk)), "application/vnd.android.package-archive");
		context.startActivity(i);
	}


	/**
	 * ����װ����
	 * 
	 * @return
	 */
	public void installSelected() {
		for (int i = 0; i < saveAppInfos.size(); i++) {
			if (saveAppInfos.get(i).isChecked) {
				File file = new File(saveAppInfos.get(i).getFilepath());
				installApk(ManagerInstallPackage.this, file.getPath());
			}
		}
	}
	
	/**
	 * ɾ��apk��
	 */
	public void deleteSelected() {
		// �������
		for (int i = saveAppInfos.size() - 1; i >= 0; i--) {
			if (saveAppInfos.get(i).isChecked) {
				File file = new File(saveAppInfos.get(i).getFilepath());
				file.delete();
				// remove��ǰɾ����apk�ļ�
				saveAppInfos.remove(i);
			}
		}
		// ֪ͨ�������ı�
		managerAdapter.notifyDataSetChanged();
	}

	/**
	 * SD���ϵ��ļ�Ŀ¼ ���õݹ��˼�룬
	 * �ݹ�ȥ��ÿ��Ŀ¼�����apk�ļ� �õ�sd������apk����
	 * 
	 * @param root
	 */
	public void getAllFile(final File root) {

		new Thread(new Runnable() {
			public void run() {
				File files[] = root.listFiles();
				if (files != null)
					for (File f : files) {
						if (f.isDirectory()) {
							//System.out.println("----------ɨ��sd��--------" + f);
							getAllFile(f);
						} else {
							if (f.getName().indexOf(".apk") > 0) {
							appInfo = getOneApkInfomaton(f);
								//appInfo1=appInfo;
								// ���������apk��Ϣ
								if (!(appInfo.appSize).equals("")) {
									//ɨ�赽һ����ӵ������saveAppInfos��
									//saveAppInfos.add(appInfo);
									Message msg =new Message();
									msg.obj=appInfo;
									msg.what=saveAppInfos.size()-1;
									handler1.sendMessage(msg);
								
								}
							}
						}
					}
			}
		}).start();
	}

	/**
	 * �õ�һ��apk��Ϣ
	 * 
	 * @param file
	 * @return
	 */
	public AppInfo getOneApkInfomaton(File file) {

		AppInfo myAppInfo = new AppInfo();
		PackageManager pm = context.getPackageManager();
		PackageInfo packageInfo = pm.getPackageArchiveInfo(
				file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
		if (packageInfo != null) {
			ApplicationInfo appInfo = packageInfo.applicationInfo;
			if (appInfo != null) {

				// ��ȡsk����apk��ͼ��
				appInfo.sourceDir = file.getAbsolutePath();
				appInfo.publicSourceDir = file.getAbsolutePath();
				Drawable icon = appInfo.loadIcon(pm);
				myAppInfo.setIcon(icon);
				// ���Ӧ�ó�����
				String appName = appInfo.loadLabel(pm).toString();
				myAppInfo.setAppName(appName);
				// ��ð���
				String packageName = packageInfo.packageName;
				myAppInfo.setPackageName(packageName);
				// ���apk�İ汾����
				String versionName = packageInfo.versionName;
				myAppInfo.setVersionName(versionName);

				// ���apk�İ汾����
				int versionCode = packageInfo.versionCode;
				myAppInfo.setVersionCode(versionCode);

				// ���Ӧ�ó���Ĵ�С
				String dir = file.getAbsolutePath();
				String size = MemoryStatus.formatFileSize(new File(dir)
						.length());
				// ���path���뵽ʵ����
				myAppInfo.setFilepath(file.getPath());

				myAppInfo.setAppSize(size);

				try {
					PackageInfo pInfo = pm.getPackageInfo(appInfo.packageName,
							0);
					// ApplicationInfo applicationInfo = pInfo.applicationInfo;
					// �Ѱ�װ
					if (versionCode == pInfo.versionCode) {
						myAppInfo.setVersionflag(APK_INSTALL);
						// �߰汾
					} else if (versionCode > pInfo.versionCode) {
						myAppInfo.setVersionflag(APK_HIGHINSTALL);
						// �Ͱ汾
					} else if (versionCode < pInfo.versionCode) {
						myAppInfo.setVersionflag(APK_LOWINSTALL);
					}

				} catch (Exception e) {
					// δ��װ
					myAppInfo.setVersionflag(APK_UNINSTALL);
				}
			}
		}
		return myAppInfo;
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		managerListView.setAdapter(managerAdapter);
		managerAdapter.notifyDataSetChanged();
		
	}
}
