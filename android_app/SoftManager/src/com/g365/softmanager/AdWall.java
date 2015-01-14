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

import com.g365.download.AppOneAdDownloader;
import com.g365.entity.AppWallDownloadInfo;
import com.g365.utils.ApplyAdWallHelper;
import com.g365.utils.AsyncImageLoader;
import com.g365.utils.CustomPrint;
import com.g365.utils.SoftManagerPreference;

/**
 * app 应用下载
 * @author Administrator
 *
 */
public class AdWall extends Activity {

	
	
	private RelativeLayout parentRl;
	/** 图标 */
	private ImageView iconIv;
	/** 标题 */
	private TextView titleTv;
	/** 适用固件 */
	private TextView applythefirmwareTv;
	
	/** 下载按钮 */
	private ImageButton downloadIb;
	/** 关闭按钮 */
	private LinearLayout  close;
	/** 简介 */
	private TextView introTv;
	
	/** 传入的广告墙广告 */
	private AppWallDownloadInfo appWallDownloadInfo;
	/** 下载次数*/
	private int downloadsum=0;
	
	public void onCreate(Bundle savedInstanceState) {
		// 设置全屏模式
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_apk_wall_detail);
		initViews();
	}
	
	
	private void initViews() {
		findViewsById();
		Intent intent=getIntent();
		if(intent!=null){
			appWallDownloadInfo=(AppWallDownloadInfo) intent.getSerializableExtra(ApplyAdWallHelper.DATA_KEY_AD_WALL_INFO);
		  if(appWallDownloadInfo!=null){
			  AsyncImageLoader.loadViewImage(appWallDownloadInfo.app_icon, iconIv, parentRl);
			  titleTv.setText("软件名称："+appWallDownloadInfo.app_Name);
			  applythefirmwareTv.setText("适用固件：Android"+appWallDownloadInfo.app_sdkversion+"+");
			  introTv.setText(appWallDownloadInfo.app_intro);
			  
		  }
		
		}
		
		downloadIb.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				AppOneAdDownloader adDownloader=AppOneAdDownloader.getInstance(AdWall.this, appWallDownloadInfo);
			     
				if(adDownloader.isDownloading()){
					CustomPrint.show(AdWall.this,
							appWallDownloadInfo.app_Name + "已在下载队列中");
				}else{
					adDownloader.startDownload();
					downloadsum++;
					SoftManagerPreference.saveSoftNeccessaryDownload(AdWall.this, downloadsum);
				}
			
				finish();
			
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
