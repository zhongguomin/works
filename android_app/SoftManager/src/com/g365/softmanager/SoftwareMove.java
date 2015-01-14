package com.g365.softmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
import com.g365.utils.MyApplication;
import com.g365.utils.SoftManagerPreference;


/**
 * 
 * @author nova
 * ��������
 */
public class SoftwareMove extends Activity {
     
	private List<AppInfo> appInfoList=new ArrayList<AppInfo>();
	// �ֻ����ÿռ�
	private TextView Phonespaceavailable;
	// �洢�����ÿռ�
	private TextView SdCardspaceavailable;
	// ȫѡ����ȫ��ѡ
	private CheckBox softmoveallOrNot;
	// �Զ���������
	private SoftMoveAdapter softMoveAdapter;
	// ���벼��
	private LayoutInflater vi;
	// ������
	private MemoryStatus memoryStatus;
	// handler����
	private Handler handler = new Handler();
	// �����list
	private ListView softmove_listView;
	// һ����ֲ
	private Button SoftMoveToSdcCard;
	// �ֻ���
	List<AppInfo> phoneSaveAppLists = new ArrayList<AppInfo>();
	// sdcard��
	List<AppInfo> sdCardSaveAppLists = new ArrayList<AppInfo>();
	// �ֻ�����ֲ
	private TextView phonemove;
	// sd������ֲ
	private TextView sdmove,linerphonemove;
	
	/** �ֻ���ʱ����ɨ����ȵ�handler*/
	private Handler inTimePhoneHandler; 
	
	/** sd����ʱ����ɨ����ȵ�handler*/
	private Handler inTimeSDcardHandler;

	boolean ak;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// ȥ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.software_move_main);
		//getOnSDOrPhone();
		
		
		
		softmove_listView = (ListView) findViewById(R.id.softwaremovelistview);
		softmove_listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<AppInfo> appInfoList = softMoveAdapter.getAppInfoList();
				// ��ȡ��Ӧappinfo�е�ischeck������ֵ true false
				boolean flag = appInfoList.get(position).isChecked;
				appInfoList.get(position).isChecked = !flag;
				
				handler.post(new Runnable() {

					public void run() {
						// ֪ͨ���������ݶ�Ӧ��appinfo��ischeck������ֵȻ�����ı� �����checkbox��״̬
						softMoveAdapter.notifyDataSetChanged();
					}
				});

			}
		});

		softMoveAdapter = new SoftMoveAdapter(SoftwareMove.this,
				phoneSaveAppLists);
		softmove_listView.setDividerHeight(2);
		if (softmove_listView != null) {
			softmove_listView.setAdapter(softMoveAdapter);
		}

		LinearLayout linearLayoutMove = (LinearLayout) findViewById(R.id.softmove_back);
		Phonespaceavailable = (TextView) findViewById(R.id.phonespaceavailabel);
		SdCardspaceavailable = (TextView) findViewById(R.id.sdcardspaceavailabel);
		softmoveallOrNot = (CheckBox) findViewById(R.id.softmovecheckboxallornot);
		SoftMoveToSdcCard = (Button) findViewById(R.id.softmovetosdcard);
		phonemove = (TextView) findViewById(R.id.textviewphone);
		linerphonemove=(TextView) findViewById(R.id.linearlayoutphone);
		sdmove = (TextView) findViewById(R.id.linearlayoutsd);

		linerphonemove.setTextColor(getResources().getColor(R.color.appmovebule));
		sdmove.setTextColor(getResources().getColor(R.color.appmoveblack));
		linearLayoutMove.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				finish();

			}
		});

		softmoveallOrNot
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						List<AppInfo> appInfoList = softMoveAdapter.getAppInfoList();
						for (int i = 0; i < appInfoList.size(); i++) {
							appInfoList.get(i).isChecked = true;
							ak=true;
							appInfoList.get(i).isChecked = isChecked;
						    
						}
						
						handler.post(new Runnable() {

							public void run() {
								softMoveAdapter.notifyDataSetChanged();
							}
						});
					}
				});

		SoftMoveToSdcCard.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				List<AppInfo> appInfoList = softMoveAdapter.getAppInfoList();
				int count = 0;

				for (int i = 0; i < appInfoList.size(); i++) {
					if (appInfoList.get(i).isChecked) {
						count++;
					}
				}
				if (count == 0) {
					Toast.makeText(SoftwareMove.this,
							R.string.softmove_sdcard_choose, Toast.LENGTH_SHORT)
							.show();

				} else {
					SoftManagerPreference.saveSoftMove(SoftwareMove.this, count);
					softMoveSelected();
				}

			}
		});
		
		//�ֻ�����ֲ�����¼�
		linerphonemove.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				ak=false;
				softmoveallOrNot.setChecked(ak);
				softMoveAdapter.setAppInfoList(phoneSaveAppLists);
				// ���ñ����ı�
				LinearLayout phonemove = (LinearLayout) findViewById(R.id.common_software_sdphone);
				phonemove
						.setBackgroundResource(R.drawable.install_necessary_softbutton);
				
				linerphonemove.setTextColor(getResources().getColor(R.color.appmovebule));
				
				sdmove.setTextColor(getResources().getColor(R.color.appmoveblack));
				softMoveAdapter.notifyDataSetChanged();
				
			}
		});

		//sdcard����ֲ�����¼�
		sdmove.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ak=false;
				softmoveallOrNot.setChecked(ak);
				softMoveAdapter.setAppInfoList(sdCardSaveAppLists);
				LinearLayout ll_sdcard= (LinearLayout) findViewById(R.id.common_software_sdphone);
				// ���ñ����ı�
				ll_sdcard
						.setBackgroundResource(R.drawable.install_necessary_gamebutton);
				linerphonemove.setTextColor(getResources().getColor(R.color.appmoveblack));
				sdmove.setTextColor(getResources().getColor(R.color.appmovebule));
				softMoveAdapter.notifyDataSetChanged();
				
			}
		});
		
		inTimePhoneHandler=new Handler(){
			
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				phoneSaveAppLists.add((AppInfo) msg.obj);
				int position=msg.what;
				softMoveAdapter.notifyDataSetChanged();
				softmove_listView.setSelection(position);
			}
		};
		
		inTimeSDcardHandler=new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
			    sdCardSaveAppLists.add((AppInfo) msg.obj);
				int position=msg.what;
				softMoveAdapter.notifyDataSetChanged();
				softmove_listView.setSelection(position);
			}
		};
		
		
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getOnSDOrPhone();
		
		softMoveAdapter.notifyDataSetChanged();
	}

	/**
	 * !=0 ��SD���ϣ�==0���ֻ��� ��ȡװ���ֻ����ϻ���SD��
	 */
	public void getOnSDOrPhone() {
        
		//�������
		phoneSaveAppLists.clear();
		sdCardSaveAppLists.clear();
		
		new Thread(new Runnable() {
			
			public void run() {
				// ��ȡ�û���װ������Ӧ�ó���
				List<PackageInfo> packages = getPackageManager()
						.getInstalledPackages(0);
				for (int i = 0; i < packages.size(); i++) {
					PackageInfo packageInfo = packages.get(i);
					// ���ֻ��� �޳���װ��sd���ϵ�
					if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_EXTERNAL_STORAGE) == 0
							&& (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0
							&& MyApplication.checkDataSource(
									getApplicationContext(), packageInfo)) {
						AppInfo phoneAppInfo = new AppInfo();
						// ���Ӧ�ó����ͼ��
						phoneAppInfo.icon = packageInfo.applicationInfo
								.loadIcon(getPackageManager());
						// ���Ӧ�ó���Ա��
						phoneAppInfo.appName = packageInfo.applicationInfo.loadLabel(
								getPackageManager()).toString();
						// ���Ӧ�ó������
						phoneAppInfo.packageName = packageInfo.packageName;
						String phoneDir = packageInfo.applicationInfo.publicSourceDir;
						String phoneSize = MemoryStatus.formatFileSize(new File(
								phoneDir).length());
						phoneAppInfo.appSize = phoneSize;
						
						
						//������Ϣ
						Message message=new Message();
						message.obj=phoneAppInfo;
						message.what=phoneSaveAppLists.size()-1;	
						inTimePhoneHandler.sendMessage(message);
						//phoneSaveAppLists.add(phoneAppInfo);
						// ��sd����
					} else if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
						AppInfo sdcardAppInfo = new AppInfo();
						// ���Ӧ�ó����ͼ��
						sdcardAppInfo.icon = packageInfo.applicationInfo
								.loadIcon(getPackageManager());
						// ���Ӧ�ó���Ա��
						sdcardAppInfo.appName = packageInfo.applicationInfo.loadLabel(
								getPackageManager()).toString();
						// ���Ӧ�ó������
						sdcardAppInfo.packageName = packageInfo.packageName;
						String phoneDir = packageInfo.applicationInfo.publicSourceDir;
						String phoneSize = MemoryStatus.formatFileSize(new File(
								phoneDir).length());
						sdcardAppInfo.appSize = phoneSize;
						
						//������Ϣ
						Message message=new Message();
						message.obj=sdcardAppInfo;
						message.what=sdCardSaveAppLists.size()-1;	
						inTimeSDcardHandler.sendMessage(message);
						//sdCardSaveAppLists.add(sdcardAppInfo);

					}
				}
				
			}
		}).start();
	
	}
    

	/**
	 * �Զ���������
	 */
	public class SoftMoveAdapter extends BaseAdapter {

		Context context;
		List<AppInfo> appInfoList;

		public SoftMoveAdapter(Context context, List<AppInfo> appInfoList) {
			this.context = context;
			this.appInfoList = appInfoList;
			vi = (LayoutInflater) context
					.getSystemService(LAYOUT_INFLATER_SERVICE);

		}

		public List<AppInfo> getAppInfoList() {
			return appInfoList;
		}

		public void setAppInfoList(List<AppInfo> appInfoList) {
			this.appInfoList = appInfoList;
		}

		public int getCount() {

			return appInfoList.size();
		}

		public Object getItem(int position) {

			return appInfoList.get(position);
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final AppInfo softmovelist = appInfoList.get(position);
			ViewCache viewCache;
			if (convertView == null) {
				convertView = vi.inflate(R.layout.soft_move_listview, null);

				viewCache = new ViewCache();
				viewCache.softmoveappIcon = (ImageView) convertView
						.findViewById(R.id.softmoveicon);
				viewCache.softmoveappName = (TextView) convertView
						.findViewById(R.id.softmoveappName);
				viewCache.softmoveappSize = (TextView) convertView
						.findViewById(R.id.softmoveappSize);
				viewCache.softmovecheckBox = (CheckBox) convertView
						.findViewById(R.id.softmovecheckitem);
				convertView.setTag(viewCache);

			} else {
				viewCache = (ViewCache) convertView.getTag();
			}

			viewCache.softmovecheckBox.setChecked(softmovelist.isChecked);

			// ��ʾ����
			viewCache.softmoveappIcon.setImageDrawable(softmovelist.icon);
			viewCache.softmoveappName.setText(softmovelist.appName);
			viewCache.softmoveappSize.setText(softmovelist.appSize);

		
			System.out.println("---------��"+appInfoList.size()+"���������ֲ");
			// �ֻ����ÿռ�
			@SuppressWarnings("static-access")
			String phonespaceavailable = memoryStatus
					.formatFileSize(memoryStatus
							.getAvailableInternalMemorySize());
			// sdcard�����ÿռ�
			@SuppressWarnings("static-access")
			String sdcardspaceavaiable = memoryStatus
					.formatFileSize(memoryStatus
							.getAvailableExternalMemorySize());
			Phonespaceavailable.setText(phonespaceavailable);
			SdCardspaceavailable.setText(sdcardspaceavaiable);
			return convertView;
		}

		private final class ViewCache {
			public TextView softmoveappName;
			public ImageView softmoveappIcon;
			public TextView softmoveappSize;
			public CheckBox softmovecheckBox;
		}

	}
	
	/**
	 * ��ȡѡ�еİ������а��
	 */
	public void softMoveSelected() {
		List<AppInfo> appInfoList = softMoveAdapter.getAppInfoList();
		for (int i = 0; i < appInfoList.size(); i++) {
			if (appInfoList.get(i).isChecked) {
				MyApplication.showInstalledAppDetails(SoftwareMove.this,
						appInfoList.get(i).packageName);
			}

		}
	}
}
