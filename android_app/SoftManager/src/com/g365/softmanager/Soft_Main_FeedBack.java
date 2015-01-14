package com.g365.softmanager;

import java.net.URLEncoder;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.g365.entity.Feedback;
import com.g365.utils.PullUtils;
import com.g365.utils.UrlHelper;

/**
 * 
 * @author nova
 *  
 *  用户反馈
 */
public class Soft_Main_FeedBack extends Activity {
	
	/**取消 */
	private Button cancel;
	/**提交*/
	private Button submit;
	private  String machinetype,version;
	private static EditText edittext;
	Context context;
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		// 去除标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.soft_main_feedback);
		context=this;
		LinearLayout  linearLayoutfeed=(LinearLayout) findViewById(R.id.softmainfeed_back);
		cancel=(Button) findViewById(R.id.softmaincancel);
		edittext=(EditText) findViewById(R.id.edittext);
		submit=(Button) findViewById(R.id.softmainsubmit);
		linearLayoutfeed.setOnClickListener(SoftMainFeedBack);
		cancel.setOnClickListener(SoftMainFeedBack);
		submit.setOnClickListener(SoftMainSubmit);
		version=getCurrentVersion();
		// 手机机型
		machinetype = android.os.Build.MODEL;
		
	}
	
	OnClickListener SoftMainFeedBack = new OnClickListener() {

		public void onClick(View v) {
//			Intent intent = new Intent(Soft_Main_FeedBack.this,
//					SoftManagerActivity.class);
//			startActivity(intent);
			finish();
		}
	};
	
	OnClickListener SoftMainSubmit=new OnClickListener() {
		
		public void onClick(View v) {
			
			getHttpClient();
		}
	};


   public String getCurrentVersion() {
		String version = "0.0.0";
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}
   

   public void getHttpClient() {
		try {
		
			//http://aclient.ruan8.com/advice.php?id=00002
//			String url = "http://client.3gyu.com/soft_subcomment.php?id=2601&comment="
//				+ edittext.getText().toString()
//				+ "&star=3"
//				+ "&nick=软吧网友"
//				+ "&version="
//				+ version
//				+ "&machinetype="
//				+ machinetype;
			String url="http://api.anruan.com/compack.php?pa="+URLEncoder.encode(context.getPackageName())
			+"&star="+URLEncoder.encode("5")
			+"&version="+URLEncoder.encode(version)
			+"&phone="+URLEncoder.encode(machinetype)
			+"&nick=" + URLEncoder.encode("软吧网友")
			+"&content="+URLEncoder.encode(edittext.getText().toString());
			System.out.println("------url-->"+url);
		
			//List<Feedback> feedlist =  PullUtils.getLastVideos(url);
				String result=UrlHelper.getInputStreamString(url);
				String str=edittext.getText().toString();
				Log.e("测试", "------"+str);
				if("".equals(str)){
					Toast.makeText(this, R.string.softmanager_suggestion_feedback, 3000).show();
				}else{
					
						edittext.setText("");
						finish();
				//}
					
				}
				Toast.makeText(this,  R.string.softmanager_suggestion_success, 3000).show();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
   
}
