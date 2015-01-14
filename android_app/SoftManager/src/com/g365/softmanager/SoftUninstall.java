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
import android.net.Uri;
import android.os.Bundle;
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
 * 软件卸载
 */
public class SoftUninstall extends Activity {

	ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
	// 已安装应用
	private TextView textViewHadInstallapp;
	// 手机剩余空间
	private TextView textViewSdspaceavailable;
	// 全选或者全不选
	private CheckBox allOrNotcheckbox;
	// 卸载选中的应用
	private Button getcheckinstall;
	// 确认卸载
	private Button confirmunstall;
	// 取消卸载
	private Button closedDialogs;
	private MemoryStatus memoryStatus;
	private AppAdapter appAdapter;
	// 返回
	LinearLayout linearLayoutUninstall;
	//
	private ListView app_listView;
	// 导入布局
	private LayoutInflater vi;
	private Handler handler = new Handler();
	// 选中的那些
	private TextView textViewHas;
	/** 累加的次数*/
	private int count=0;

	/** 及时更新扫描进度的handler*/
	private Handler inTimeHandler; 
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_uninstall_main);

		
		app_listView = (ListView) findViewById(R.id.softuninstalllistview);
		app_listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取对应appinfo中的ischeck变量的值 true false

				boolean flag = appList.get(position).isChecked;
				System.out.println("----------" + flag);
				// 把选中的ischeck变量的值反过来
				appList.get(position).isChecked = !flag;
				handler.post(new Runnable() {

					public void run() {
						// 通知适配器根据对应的appinfo中ischeck变量的值然后来改变 上面的checkbox的状态
						appAdapter.notifyDataSetChanged();
						// textViewHas.setText("是否卸载所选的"+appList.size()+"个软件");
					}
				});
			}
		});
		appAdapter = new AppAdapter(SoftUninstall.this);
		app_listView.setDividerHeight(2);
		if (app_listView != null) {
			app_listView.setAdapter(appAdapter);
		}

		linearLayoutUninstall = (LinearLayout) findViewById(R.id.softuninstall_back);
		textViewHadInstallapp = (TextView) findViewById(R.id.hadinstallapp);
		textViewSdspaceavailable = (TextView) findViewById(R.id.sdspaceavailable);
		allOrNotcheckbox = (CheckBox) findViewById(R.id.checkboxallornot);
		getcheckinstall = (Button) findViewById(R.id.softuninstallchecked);

		linearLayoutUninstall.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				Intent intent = new Intent(SoftUninstall.this,
//						SoftManagerActivity.class);
//				startActivity(intent);
				finish();

			}
		});

		getcheckinstall.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 弹出对话框
				showDialog();

			}

		});

		/**
		 * 全选或者全不选
		 */
		allOrNotcheckbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						for (int i = 0; i < appList.size(); i++) {

							appList.get(i).isChecked = true;
							appList.get(i).isChecked = isChecked;

						}
						handler.post(new Runnable() {

							public void run() {

								appAdapter.notifyDataSetChanged();

							}
						});

					}
				});

		
		
		inTimeHandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				appList.add((AppInfo) msg.obj);
				int position=msg.what;
				appAdapter.notifyDataSetChanged();
				app_listView.setSelection(position);
				
				
			}
		};
		
		
	}

	/**
	 * 返回时重新调用查询 进行数据更新
	 */
	protected void onResume() {

		super.onResume();
		queryAppInfo();
		appAdapter.notifyDataSetChanged();

	}

	/**
	 * 弹出对话框
	 */
	private void showDialog() {

		final Dialog dialog = new Dialog(SoftUninstall.this, R.style.dialog1);
		View view = View.inflate(SoftUninstall.this,
				R.layout.softuninstall_main_dialogs, null);
		textViewHas = (TextView) view
				.findViewById(R.id.softuninstallhaschecked);
		 count = 0;

		for (int i = 0; i < appList.size(); i++) {
			if (appList.get(i).isChecked) {
				count++;
			}
		}
		// 设置卸载的个数提醒
		textViewHas.setText("是否卸载所选的" + count + " 个软件");
		dialog.setContentView(view);
		if (count == 0) {
			Toast.makeText(SoftUninstall.this,
					R.string.softuninstall_not_check, Toast.LENGTH_SHORT)
					.show();

		} else {
			dialog.show();
		}
		// 关闭对话框
		closedDialogs = (Button) view.findViewById(R.id.canceluninstall);
		// 确认卸载对话
		confirmunstall = (Button) view.findViewById(R.id.comfirmuninstall);

		closedDialogs.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		confirmunstall.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				uninstallSelected();
				SoftManagerPreference.saveSoftUninstall(SoftUninstall.this, count);
				dialog.dismiss();
			}
		});
	}

	/**
	 * 获得应用程序信息
	 */
	public void queryAppInfo() {
		appList.clear();
		
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				/**
				 * 获取手机中安装的所有应用程序，
				 */
				List<PackageInfo> packages = getPackageManager()
						.getInstalledPackages(0);
				for (int i = 0; i < packages.size(); i++) {
					PackageInfo packageInfo = packages.get(i);
					AppInfo tmpInfo = new AppInfo();
					// 获得应用程序的图标
					tmpInfo.icon = packageInfo.applicationInfo
							.loadIcon(getPackageManager());
					// 获得应用程序员名
					tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
							getPackageManager()).toString();
					// 获得应用程序包名
					tmpInfo.packageName = packageInfo.packageName;

					// 获得应用程序的大小
					String dir = packageInfo.applicationInfo.publicSourceDir;
					String size = MemoryStatus.formatFileSize(new File(dir).length());
					tmpInfo.appSize = size;

					// 在手机上
					if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
						//appList.add(tmpInfo);
						Message  message=new Message();
						message.obj=tmpInfo;
						message.what=appList.size()-1;	
						inTimeHandler.sendMessage(message);
					}

					
				}
			}
		}).start();
		
	}

	/**
	 * 自定义适配器 完成数据绑定
	 */
	public class AppAdapter extends BaseAdapter {

		Context context;

		/**
		 * 构造器完成数据的初始化
		 * 
		 * @param context
		 */
		public AppAdapter(Context context) {
			this.context = context;
			// 得到系统内置的布局填充服务
			vi = (LayoutInflater) context
					.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		/**
		 * 数据总量
		 */
		public int getCount() {
			return appList.size();
		}

		/**
		 * 取得索引值
		 */
		public Object getItem(int position) {
			return appList.get(position);
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

			// final int index = position;
			final AppInfo appUnit = appList.get(position);
			ViewCache cache;
			if (convertView == null) {
				// 绑定的界面
				convertView = vi
						.inflate(R.layout.soft_uninstall_listview, null);

				cache = new ViewCache();
				cache.appName = (TextView) convertView
						.findViewById(R.id.appName);
				cache.appIcon = (ImageView) convertView.findViewById(R.id.icon);
				cache.appSize = (TextView) convertView
						.findViewById(R.id.appSize);
				cache.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkitem);
				convertView.setTag(cache);
			} else {
				cache = (ViewCache) convertView.getTag();
			}
			// 根据ischeck的值checkbox的状态
			cache.checkBox.setChecked(appUnit.isChecked);

			/**
			 * 显示出来
			 */

			cache.appName.setText(appUnit.appName);
			cache.appIcon.setImageDrawable(appUnit.icon);
			cache.appSize.setText(appUnit.appSize);

			// 已安装应用
			textViewHadInstallapp.setText(appList.size() + "");

			@SuppressWarnings("static-access")
			String sd = memoryStatus.formatFileSize(memoryStatus  
					.getAvailableInternalMemorySize());
			// 手机剩余空间
			textViewSdspaceavailable.setText(sd);
			return convertView;
		}

		/**
		 * 缓存功能
		 */
		private final class ViewCache {
			public TextView appName;
			public ImageView appIcon;
			public TextView appSize;
			public CheckBox checkBox;

		}

	}

	/**
	 * 卸载方法
	 * 
	 * @param packname
	 *            包名
	 */
	public void uninstall(String packname) {
		Uri packageURI = Uri.parse("package:" + packname);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
		uninstallIntent.setData(packageURI);
		startActivity(uninstallIntent);

	}

	/**
	 * 获取选中的包名进行卸载
	 * 
	 * @return
	 */
	public void uninstallSelected() {
		for (int i = 0; i < appList.size(); i++) {
			if (appList.get(i).isChecked) {
				uninstall(appList.get(i).packageName);
			}
		}
	}

}
