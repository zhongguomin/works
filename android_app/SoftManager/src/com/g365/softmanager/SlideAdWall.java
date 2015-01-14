package com.g365.softmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.g365.download.ImageDownload;
import com.g365.entity.AppResInit;
import com.g365.entity.ImageInfo;
import com.g365.utils.ApplyAdWallHelper;
import com.g365.utils.AsyncImageLoader;
import com.g365.utils.CustomPrint;
import com.g365.utils.SoftManagerPreference;

/**
 *  ����ͼƬ����
 * @author Administrator
 *
 */
public class SlideAdWall extends Activity{

	private RelativeLayout parentRl;
	/** ͼ�� */
	private ImageView iconIv;
	/** ���� */
	private TextView titleTv;
	/** ���ù̼� */
	private TextView applythefirmwareTv;
	
	/** ���ذ�ť */
	private ImageButton downloadIb;
	/** �رհ�ť */
	private LinearLayout  close;
	/** ��� */
	private TextView introTv;
	/** ����Ĺ��ǽ��� */
	private ImageInfo imageInfo;
	/** ���ش���*/
	private int downloadsum=0;
	public  void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_apk_wall_detail);
		initViews();
		
	}
	
	
	
	
	private void initViews(){
		findViewsById();
		
		Intent intent=getIntent();
		if(intent!=null){
			imageInfo=(ImageInfo) intent.getSerializableExtra(ApplyAdWallHelper.DATA_KEY_AD_SLIDEWALL_INFO);
		  if(imageInfo!=null){
			  AsyncImageLoader.loadViewImage(imageInfo.apps_icon, iconIv, parentRl);
			  titleTv.setText("������ƣ�"+imageInfo.apps_Name);
			  applythefirmwareTv.setText("���ù̼���Android"+imageInfo.apps__sdkversion+"+");
			  introTv.setText(imageInfo.apps_intro);
			  
		  }
		
		}
		downloadIb.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				SlideAdWall.this.finish();
				AppResInit.initNotificationDownloadRes();
				if(ImageDownload.downMap.containsKey(imageInfo.apps_downloads)){
					CustomPrint.show(SlideAdWall.this,
							imageInfo.apps_Name + "�����������");
				}else{
					downloadsum++;
					SoftManagerPreference.saveSoftNeccessaryDownload(SlideAdWall.this, downloadsum);
					ImageDownload.downLoad(SlideAdWall.this, imageInfo);
					
				}
				//finish();
				//System.out.println("------------2013----------");
			}
		});
		
     close.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
	private void  findViewsById(){
		parentRl=(RelativeLayout) findViewById(R.id.apk_wall_detail_rl_parent);
		iconIv=(ImageView) findViewById(R.id.apk_wall_detail_iv_icon);
		titleTv=(TextView) findViewById(R.id.apk_wall_detail_tv_title);
		applythefirmwareTv=(TextView) findViewById(R.id.apk_wall_detail_tv_applythefirmware);
		downloadIb=(ImageButton) findViewById(R.id.apk_wall_detail_ib_download);
		close=(LinearLayout) findViewById(R.id.apk_wall_detail_ll_close);
		introTv=(TextView) findViewById(R.id.apk_wall_detail_tv_intro);
	
	}
}
