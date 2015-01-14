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
 * 安装包管理类
 */
public class ManagerInstallPackage extends Activity {

	/**文件集合*/
	private ArrayList<File> list = new ArrayList<File>();
	/** 读取手机 sdcard 剩余空间工具类*/
	private MemoryStatus memoryStatus;
	/** 要导入的布局 */
	private LayoutInflater managerLi;
	/**自定义适配器 */
	private ManagerAdapter managerAdapter;
	/** 要填充的listview */
	private ListView managerListView;
	/**安装*/
	private Button managerInstall_Install;
	/** 确认安装*/
	private Button confirmInstall;
	/** 不安装 */
	private Button cancelInstall;
	/** 提醒安装 */
	private TextView remindInstall;
	/** 删除 */
	private Button managerInstall_Delete;
	/** 确认删除 */
	private Button confirmdelete;
	/** 取消删除 */
	private Button canceldelete;
	private TextView remindDelete;
	/** 全选或者全不选 */
	private CheckBox managerIsOrNot;
	/** handler对象 */
	private Handler handler = new Handler();
	/** 安装包数量 占用大小 */
	private TextView managerSum, occupySize;
	/**适配的数据*/
	private List<AppInfo> saveAppInfos = new ArrayList<AppInfo>();

	private AppInfo appInfo;
	/**
	 * 未安装apk
	 */
	public static final int APK_UNINSTALL = 0;
	/**
	 * 已安装apk
	 */
	public static final int APK_INSTALL = 1;
	/**
	 * 低版本apk
	 */
	public static final int APK_LOWINSTALL = 2;
	/**
	 * 高版本apk
	 */
	public static final int APK_HIGHINSTALL = 3;
	/**
	 * 重复的apk
	 */
	public static final int APK_REPEATINSTALL = 4;

	private Handler handler1;
	/**
	 * 上下文对象
	 */
	private Context context;
	
	/** 累计安装次数*/
	private int count=0;
	/** 累计删除次数*/
	private int count1=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.manager_install_package_main);
		//多线程this 初始化
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
				// 获取对应appinfo中的ischeck变量的值 true false
				boolean flag = saveAppInfos.get(position).isChecked;
				// 把选中的ischeck变量的值反过来
				saveAppInfos.get(position).isChecked = !flag;
				handler.post(new Runnable() {

					public void run() {

						// 通知适配器根据对应的appinfo中ischeck变量的值然后来改变 上面的checkbox的状态
						managerAdapter.notifyDataSetChanged();
					}
				});
			}
		});

		// 全选或者全不选
		managerIsOrNot = (CheckBox) findViewById(R.id.managerallornot);
		// 安装
		managerInstall_Install = (Button) findViewById(R.id.managerinstall_install);
		// 删除
		managerInstall_Delete = (Button) findViewById(R.id.manageinstall_delete);

		managerSum = (TextView) findViewById(R.id.managersum);
		occupySize = (TextView) findViewById(R.id.occupysize);

		linearLayoutManager.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		/**
		 * 确认安装
		 */
		managerInstall_Install.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				// 弹出对话框
				showDialog();
			}
		});

		/**
		 * 确认删除
		 */
		managerInstall_Delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 弹出对话框
				showDialog1();
			}
		});

		managerAdapter = new ManagerAdapter(ManagerInstallPackage.this);
		managerListView.setDividerHeight(2);
		if (managerListView != null) {
			managerListView.setAdapter(managerAdapter);
		}
		/**
		 * 全选或者全不选
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
	 * 安装对话框
	 */
	private void showDialog() {
		final Dialog dialog = new Dialog(ManagerInstallPackage.this,
				R.style.dialog1);
		View view = View.inflate(ManagerInstallPackage.this,
				R.layout.managerinstall_install_dialog, null);
		// 安装提醒
		remindInstall = (TextView) view
				.findViewById(R.id.managerinstallhaschecked);
		count = 0;
		for (int i = 0; i < saveAppInfos.size(); i++) {
			if (saveAppInfos.get(i).isChecked) {
				count++;
			}
		}
		remindInstall.setText("是否安装所选的" + count + "个软件");
		dialog.setContentView(view);
		if (count == 0) {
			Toast.makeText(ManagerInstallPackage.this,
					R.string.manageruninstall_not_choice, Toast.LENGTH_SHORT)
					.show();
		} else {
			dialog.show();
		}
		// 确认安装
		confirmInstall = (Button) view
				.findViewById(R.id.managercomfirmuninstall);
		// 取消安装
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
	 * 删除对话框
	 */
	private void showDialog1() {
		final Dialog dialog = new Dialog(ManagerInstallPackage.this,
				R.style.dialog1);
		View view1 = View.inflate(ManagerInstallPackage.this,
				R.layout.managerinstall_delete_dialogs, null);
		// 删除提醒
		remindDelete = (TextView) view1
				.findViewById(R.id.managedeletehaschecked);
		 count1 = 0;
		for (int i = 0; i < saveAppInfos.size(); i++) {
			if (saveAppInfos.get(i).isChecked) {
				count1++;
			}
		}
		remindDelete.setText("是否删除所选的" + count1 + "个安装包");
		dialog.setContentView(view1);
		if (count1 == 0) {
			Toast.makeText(ManagerInstallPackage.this,
					R.string.manageruninstall_deletenot_choice,
					Toast.LENGTH_SHORT).show();
		} else {
			dialog.show();
		}
		// 确认删除
		confirmdelete = (Button) view1.findViewById(R.id.managercomfirdelete);
		// 取消删除
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
	 * 自定义适配器
	 */

	public class ManagerAdapter extends BaseAdapter { // 上下文对象 Context context;

		private Context context;

		// private List<AppInfo> saveAppInfos;

		/**
		 * 构造器完成数据的初始化
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
		 * 数据总量
		 */
		public int getCount() {
			return saveAppInfos.size();
		}

		/**
		 * 取得索引值
		 */
		public Object getItem(int position) {
			return saveAppInfos.get(position);
		}

		/**
		 * 取得索引的id
		 */
		public long getItemId(int position) {
			return position;
		}

		/**
		 * 取得条目的界面
		 */
		public View getView(int position, View convertView, ViewGroup parent) {

			final AppInfo managerUnit = saveAppInfos.get(position);
			ViewCache cache;
			if (convertView == null) {
				// 绑定的界面
				convertView = managerLi.inflate(
						R.layout.manager_installpackages_listview, null);
				// 创建对象
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
			// 条根据ischeck的值更改checkbox的状态
			cache.appIcon.setImageDrawable(managerUnit.icon);
			cache.appName.setText(managerUnit.appName);
			cache.versionName.setText("版本：" + managerUnit.versionName + "版");
			cache.appSize.setText(managerUnit.appSize);
			cache.checkBox.setChecked(managerUnit.isChecked);
			//cache.compareVersion.setTextSize(14);
			//cache.compareVersion.setTextColor(#149aff);
			
			
			// 安装包数量
			managerSum.setText(saveAppInfos.size() + "");
			
			@SuppressWarnings("static-access")
			String sd = memoryStatus.formatFileSize(memoryStatus
					.getAvailableInternalMemorySize());
			occupySize.setText(sd);
			
			// 根据标志设置
			if (managerUnit.getVersionflag() == ManagerInstallPackage.APK_UNINSTALL) {
				cache.compareVersion.setText("未安装");
				
			} else if (managerUnit.getVersionflag() == ManagerInstallPackage.APK_INSTALL) {
				cache.compareVersion.setText("已安装");
			} else if (managerUnit.getVersionflag() == ManagerInstallPackage.APK_LOWINSTALL) {
				cache.compareVersion.setText("低版本");
			} else if (managerUnit.getVersionflag() == ManagerInstallPackage.APK_HIGHINSTALL) {
				cache.compareVersion.setText("高版本");
			} else if (managerUnit.getVersionflag() == ManagerInstallPackage.APK_REPEATINSTALL) {
				cache.compareVersion.setText("重复");
			}
			
		
			
			return convertView;
		}

		/**
		 * 缓存功能
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
	 * 安装应用程序
	 * 
	 * @param context
	 *            上下文
	 * @param sdcardpath
	 *            APK的路径
	 */
//	public static void installApk(Context context, String filePath) {
//		Uri uri = Uri.parse(filePath);
//		Intent notificationIntent = new Intent(Intent.ACTION_VIEW, uri);
//		notificationIntent.setData(uri);
//		notificationIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
//				| Intent.FLAG_ACTIVITY_NEW_TASK);
//		notificationIntent.setClassName("com.android.packageinstaller",
//				"com.android.packageinstaller.PackageInstallerActivity");
//		context.startActivity(notificationIntent);// 这种方式不能传递参数
//		//android.os.Process.killProcess(android.os.Process.myPid());
//	}
	
	
	/** 调用系统安装APK */
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
	 * 程序安装方法
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
	 * 删除apk包
	 */
	public void deleteSelected() {
		// 逆向遍历
		for (int i = saveAppInfos.size() - 1; i >= 0; i--) {
			if (saveAppInfos.get(i).isChecked) {
				File file = new File(saveAppInfos.get(i).getFilepath());
				file.delete();
				// remove当前删除的apk文件
				saveAppInfos.remove(i);
			}
		}
		// 通知适配器改变
		managerAdapter.notifyDataSetChanged();
	}

	/**
	 * SD卡上的文件目录 运用递归的思想，
	 * 递归去找每个目录下面的apk文件 得到sd卡所有apk方法
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
							//System.out.println("----------扫描sd卡--------" + f);
							getAllFile(f);
						} else {
							if (f.getName().indexOf(".apk") > 0) {
							appInfo = getOneApkInfomaton(f);
								//appInfo1=appInfo;
								// 过滤破损的apk信息
								if (!(appInfo.appSize).equals("")) {
									//扫描到一个添加到适配的saveAppInfos中
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
	 * 得到一个apk信息
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

				// 获取sk卡上apk的图标
				appInfo.sourceDir = file.getAbsolutePath();
				appInfo.publicSourceDir = file.getAbsolutePath();
				Drawable icon = appInfo.loadIcon(pm);
				myAppInfo.setIcon(icon);
				// 获得应用程序名
				String appName = appInfo.loadLabel(pm).toString();
				myAppInfo.setAppName(appName);
				// 获得包名
				String packageName = packageInfo.packageName;
				myAppInfo.setPackageName(packageName);
				// 获得apk的版本名称
				String versionName = packageInfo.versionName;
				myAppInfo.setVersionName(versionName);

				// 获得apk的版本号码
				int versionCode = packageInfo.versionCode;
				myAppInfo.setVersionCode(versionCode);

				// 获得应用程序的大小
				String dir = file.getAbsolutePath();
				String size = MemoryStatus.formatFileSize(new File(dir)
						.length());
				// 获得path存入到实体类
				myAppInfo.setFilepath(file.getPath());

				myAppInfo.setAppSize(size);

				try {
					PackageInfo pInfo = pm.getPackageInfo(appInfo.packageName,
							0);
					// ApplicationInfo applicationInfo = pInfo.applicationInfo;
					// 已安装
					if (versionCode == pInfo.versionCode) {
						myAppInfo.setVersionflag(APK_INSTALL);
						// 高版本
					} else if (versionCode > pInfo.versionCode) {
						myAppInfo.setVersionflag(APK_HIGHINSTALL);
						// 低版本
					} else if (versionCode < pInfo.versionCode) {
						myAppInfo.setVersionflag(APK_LOWINSTALL);
					}

				} catch (Exception e) {
					// 未安装
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
