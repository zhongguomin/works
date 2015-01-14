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
 * ���ж��
 */
public class SoftUninstall extends Activity {

	ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
	// �Ѱ�װӦ��
	private TextView textViewHadInstallapp;
	// �ֻ�ʣ��ռ�
	private TextView textViewSdspaceavailable;
	// ȫѡ����ȫ��ѡ
	private CheckBox allOrNotcheckbox;
	// ж��ѡ�е�Ӧ��
	private Button getcheckinstall;
	// ȷ��ж��
	private Button confirmunstall;
	// ȡ��ж��
	private Button closedDialogs;
	private MemoryStatus memoryStatus;
	private AppAdapter appAdapter;
	// ����
	LinearLayout linearLayoutUninstall;
	//
	private ListView app_listView;
	// ���벼��
	private LayoutInflater vi;
	private Handler handler = new Handler();
	// ѡ�е���Щ
	private TextView textViewHas;
	/** �ۼӵĴ���*/
	private int count=0;

	/** ��ʱ����ɨ����ȵ�handler*/
	private Handler inTimeHandler; 
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_uninstall_main);

		
		app_listView = (ListView) findViewById(R.id.softuninstalllistview);
		app_listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ��ȡ��Ӧappinfo�е�ischeck������ֵ true false

				boolean flag = appList.get(position).isChecked;
				System.out.println("----------" + flag);
				// ��ѡ�е�ischeck������ֵ������
				appList.get(position).isChecked = !flag;
				handler.post(new Runnable() {

					public void run() {
						// ֪ͨ���������ݶ�Ӧ��appinfo��ischeck������ֵȻ�����ı� �����checkbox��״̬
						appAdapter.notifyDataSetChanged();
						// textViewHas.setText("�Ƿ�ж����ѡ��"+appList.size()+"�����");
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
				// �����Ի���
				showDialog();

			}

		});

		/**
		 * ȫѡ����ȫ��ѡ
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
	 * ����ʱ���µ��ò�ѯ �������ݸ���
	 */
	protected void onResume() {

		super.onResume();
		queryAppInfo();
		appAdapter.notifyDataSetChanged();

	}

	/**
	 * �����Ի���
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
		// ����ж�صĸ�������
		textViewHas.setText("�Ƿ�ж����ѡ��" + count + " �����");
		dialog.setContentView(view);
		if (count == 0) {
			Toast.makeText(SoftUninstall.this,
					R.string.softuninstall_not_check, Toast.LENGTH_SHORT)
					.show();

		} else {
			dialog.show();
		}
		// �رնԻ���
		closedDialogs = (Button) view.findViewById(R.id.canceluninstall);
		// ȷ��ж�ضԻ�
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
	 * ���Ӧ�ó�����Ϣ
	 */
	public void queryAppInfo() {
		appList.clear();
		
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				/**
				 * ��ȡ�ֻ��а�װ������Ӧ�ó���
				 */
				List<PackageInfo> packages = getPackageManager()
						.getInstalledPackages(0);
				for (int i = 0; i < packages.size(); i++) {
					PackageInfo packageInfo = packages.get(i);
					AppInfo tmpInfo = new AppInfo();
					// ���Ӧ�ó����ͼ��
					tmpInfo.icon = packageInfo.applicationInfo
							.loadIcon(getPackageManager());
					// ���Ӧ�ó���Ա��
					tmpInfo.appName = packageInfo.applicationInfo.loadLabel(
							getPackageManager()).toString();
					// ���Ӧ�ó������
					tmpInfo.packageName = packageInfo.packageName;

					// ���Ӧ�ó���Ĵ�С
					String dir = packageInfo.applicationInfo.publicSourceDir;
					String size = MemoryStatus.formatFileSize(new File(dir).length());
					tmpInfo.appSize = size;

					// ���ֻ���
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
	 * �Զ��������� ������ݰ�
	 */
	public class AppAdapter extends BaseAdapter {

		Context context;

		/**
		 * ������������ݵĳ�ʼ��
		 * 
		 * @param context
		 */
		public AppAdapter(Context context) {
			this.context = context;
			// �õ�ϵͳ���õĲ���������
			vi = (LayoutInflater) context
					.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		/**
		 * ��������
		 */
		public int getCount() {
			return appList.size();
		}

		/**
		 * ȡ������ֵ
		 */
		public Object getItem(int position) {
			return appList.get(position);
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

			// final int index = position;
			final AppInfo appUnit = appList.get(position);
			ViewCache cache;
			if (convertView == null) {
				// �󶨵Ľ���
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
			// ����ischeck��ֵcheckbox��״̬
			cache.checkBox.setChecked(appUnit.isChecked);

			/**
			 * ��ʾ����
			 */

			cache.appName.setText(appUnit.appName);
			cache.appIcon.setImageDrawable(appUnit.icon);
			cache.appSize.setText(appUnit.appSize);

			// �Ѱ�װӦ��
			textViewHadInstallapp.setText(appList.size() + "");

			@SuppressWarnings("static-access")
			String sd = memoryStatus.formatFileSize(memoryStatus  
					.getAvailableInternalMemorySize());
			// �ֻ�ʣ��ռ�
			textViewSdspaceavailable.setText(sd);
			return convertView;
		}

		/**
		 * ���湦��
		 */
		private final class ViewCache {
			public TextView appName;
			public ImageView appIcon;
			public TextView appSize;
			public CheckBox checkBox;

		}

	}

	/**
	 * ж�ط���
	 * 
	 * @param packname
	 *            ����
	 */
	public void uninstall(String packname) {
		Uri packageURI = Uri.parse("package:" + packname);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE);
		uninstallIntent.setData(packageURI);
		startActivity(uninstallIntent);

	}

	/**
	 * ��ȡѡ�еİ�������ж��
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
