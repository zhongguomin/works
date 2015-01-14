package com.g365.softmanager;

import com.g365.download.DefaultStateHandler;
import com.g365.download.FileDownloader;
import com.g365.download.interfaces.DownloadProgressListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

public class Soft_Main_Abuot extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// È¥³ý±êÌâÀ¸
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_main_about);
		
		LinearLayout linearLayoutabout = (LinearLayout) findViewById(R.id.softmainabout_back);

		linearLayoutabout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
//				Intent intent = new Intent(Soft_Main_Abuot.this,
//						SoftManagerActivity.class);
//				startActivity(intent);
				finish();

			}
		});
		
		FileDownloader fileDownloader=new FileDownloader(this, "", new DefaultStateHandler(this), new DownloadProgressListener() {
			
			public void onDownload(int arg0, int arg1, int arg2, boolean arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		fileDownloader.startDownload();
	}

}
