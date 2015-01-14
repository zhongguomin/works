package com.g365.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lllfy.newad.core.AdHelper;

import android.content.Context;

/**
 *  
 * @author nova
 * 日期   2013年1月4日11:19:26
 * 用户体验相关的类
 */
public class UserExperience {
	
	/** 发送数据的地址前段 */
	private static final String POST_URL_PRE="http://cp.g365.cn/app_feel.php?userid=";
	
	/** 进入软件的时间点 */
	public static long startTime = 0;
	/** 退出软件的时间点 */
	public static long endTime = 0;

	/**
	 * 追加存入用户本次使用数据
	 * 
	 * @param context
	 *            上下文
	 */
	public static void saveUserReadData(Context context){
		
		SoftManagerPreference userSmp=new SoftManagerPreference(context,
				"soft_pref_"+SoftManagerPreference.PREF_NAME_USER_DATA);
		// 读取历史使用数据
		String useData=userSmp.readString(SoftManagerPreference.KEY_USE_DATA, "");
		if(!useData.equals("")){// 如果不为空现在后面加一个"|"
			useData+="|";
		}
		StringBuilder sb=new StringBuilder(useData);
		// 获取本次使用数据
		String data=startTime+"-"+endTime;
		sb.append(data);
		userSmp.saveString(SoftManagerPreference.KEY_USE_DATA, sb.toString());
	}
	
	/**
	 * 采用 POST方法向服务器提交用户体验数据
	 *  @param context
	 *         上下文
	 */
	public static void postUserData(final Context context){
		
		new Thread(){

			@Override
			public void run() {
				
				SoftManagerPreference userSmp=new SoftManagerPreference(context,
						SoftManagerPreference.PREF_NAME_USER_DATA);
				long lastPostTime=userSmp.readLong(
						SoftManagerPreference.KEY_LAST_POST_TIME, 0);
				//当前时间
				long nowtime=System.currentTimeMillis();
				// 距上次发送时间超过一天再次发送
//				nowtime-lastPostTime>=24*60*60*1000
				if(nowtime-lastPostTime>=24*60*60*1000){
					
					String destUrl = POST_URL_PRE+AdHelper.getUserId(context);
				    //采用post提交数据
					HttpEntityEnclosingRequestBase httpRequest=new HttpPost(destUrl);
					//参数
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("setting", getUserPreStr(context)));
					params.add(new BasicNameValuePair("times", getUserReadData(context)));
				
				try {
					//设置编码
					httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				    HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
				//请求成功
				if(httpResponse.getStatusLine().getStatusCode()==200){
					
					String resultStr=EntityUtils.toString(httpResponse.getEntity());
					if("\n1".equals(resultStr)||"1".equals(resultStr)){// 发送成功后删除以前的数据
						userSmp.saveString(SoftManagerPreference.KEY_USE_DATA, "");
					
						userSmp.saveLong(SoftManagerPreference.KEY_LAST_POST_TIME, 
								System.currentTimeMillis());
					}
					CustomPrint.d(UserExperience.class, 
							"postUserData：result-->" + resultStr);
				}else{
					CustomPrint.d(UserExperience.class,
							"postUserData：Error response-->"
									+ httpResponse.getStatusLine());
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
				}
			}
		}.start();
		
	}
	
	
	/**
	 * 获取用户偏好的字符串
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public static String getUserPreStr(Context context){
		
		SoftManagerPreference readSmp=new SoftManagerPreference(context, 
				SoftManagerPreference.KEY_USE_DATA);
		
		Integer softUpdate=readSmp.readInt(SoftManagerPreference.KEY_SOFT_UPDATE, 0);
		Integer softUninstall=readSmp.readInt(SoftManagerPreference.KEY_SOFT_UNINSTALL, 0);
		Integer installPagesDelete=readSmp.readInt(SoftManagerPreference.KEY_SOFT_INSTALLPAGES_DELETE, 0);
		Integer installPagesInstall=readSmp.readInt(SoftManagerPreference.KEY_SOFT_INSTALLPAGES_INSTALL, 0);
		Integer softMove=readSmp.readInt(SoftManagerPreference.KEY_SOFT_MOVE, 0);
		Integer softNecessaryEnter=readSmp.readInt(SoftManagerPreference.KEY_SOFT_NECESSARY_ENTER, 0);
		Integer softNecessaryDownload=readSmp.readInt(SoftManagerPreference.KEY_SOFT_NECESSARY_DOWNLOAD, 0);
		Integer softNecessaryInstall=readSmp.readInt(SoftManagerPreference.KEY_SOFT_NECESSARY_INSTALL, 0);
		
		StringBuilder sb=new StringBuilder();
		sb.append(softUpdate).append("|")
		         .append(softUninstall).append("|")
		         .append(installPagesDelete).append("|")
		         .append(installPagesInstall).append("|")
		         .append(softMove).append("|")
		         .append(softNecessaryEnter).append("|")
		         .append(softNecessaryDownload).append("|")
		         .append(softNecessaryInstall);
		         
		return sb.toString();
		
	}
	
	/**
	 * 获取用户全部使用数据
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	
	private static String getUserReadData(Context context){
		SoftManagerPreference userSmp=new SoftManagerPreference(context, 
				SoftManagerPreference.PREF_NAME_USER_DATA);
		// 读取历史使用数据
		String useData=userSmp.readString(SoftManagerPreference.KEY_USE_DATA, "");
		return useData;
	}

}
