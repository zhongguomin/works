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
 * ����   2013��1��4��11:19:26
 * �û�������ص���
 */
public class UserExperience {
	
	/** �������ݵĵ�ַǰ�� */
	private static final String POST_URL_PRE="http://cp.g365.cn/app_feel.php?userid=";
	
	/** ���������ʱ��� */
	public static long startTime = 0;
	/** �˳������ʱ��� */
	public static long endTime = 0;

	/**
	 * ׷�Ӵ����û�����ʹ������
	 * 
	 * @param context
	 *            ������
	 */
	public static void saveUserReadData(Context context){
		
		SoftManagerPreference userSmp=new SoftManagerPreference(context,
				"soft_pref_"+SoftManagerPreference.PREF_NAME_USER_DATA);
		// ��ȡ��ʷʹ������
		String useData=userSmp.readString(SoftManagerPreference.KEY_USE_DATA, "");
		if(!useData.equals("")){// �����Ϊ�����ں����һ��"|"
			useData+="|";
		}
		StringBuilder sb=new StringBuilder(useData);
		// ��ȡ����ʹ������
		String data=startTime+"-"+endTime;
		sb.append(data);
		userSmp.saveString(SoftManagerPreference.KEY_USE_DATA, sb.toString());
	}
	
	/**
	 * ���� POST������������ύ�û���������
	 *  @param context
	 *         ������
	 */
	public static void postUserData(final Context context){
		
		new Thread(){

			@Override
			public void run() {
				
				SoftManagerPreference userSmp=new SoftManagerPreference(context,
						SoftManagerPreference.PREF_NAME_USER_DATA);
				long lastPostTime=userSmp.readLong(
						SoftManagerPreference.KEY_LAST_POST_TIME, 0);
				//��ǰʱ��
				long nowtime=System.currentTimeMillis();
				// ���ϴη���ʱ�䳬��һ���ٴη���
//				nowtime-lastPostTime>=24*60*60*1000
				if(nowtime-lastPostTime>=24*60*60*1000){
					
					String destUrl = POST_URL_PRE+AdHelper.getUserId(context);
				    //����post�ύ����
					HttpEntityEnclosingRequestBase httpRequest=new HttpPost(destUrl);
					//����
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("setting", getUserPreStr(context)));
					params.add(new BasicNameValuePair("times", getUserReadData(context)));
				
				try {
					//���ñ���
					httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
				    HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
				//����ɹ�
				if(httpResponse.getStatusLine().getStatusCode()==200){
					
					String resultStr=EntityUtils.toString(httpResponse.getEntity());
					if("\n1".equals(resultStr)||"1".equals(resultStr)){// ���ͳɹ���ɾ����ǰ������
						userSmp.saveString(SoftManagerPreference.KEY_USE_DATA, "");
					
						userSmp.saveLong(SoftManagerPreference.KEY_LAST_POST_TIME, 
								System.currentTimeMillis());
					}
					CustomPrint.d(UserExperience.class, 
							"postUserData��result-->" + resultStr);
				}else{
					CustomPrint.d(UserExperience.class,
							"postUserData��Error response-->"
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
	 * ��ȡ�û�ƫ�õ��ַ���
	 * 
	 * @param context
	 *            ������
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
	 * ��ȡ�û�ȫ��ʹ������
	 * 
	 * @param context
	 *            ������
	 * @return
	 */
	
	private static String getUserReadData(Context context){
		SoftManagerPreference userSmp=new SoftManagerPreference(context, 
				SoftManagerPreference.PREF_NAME_USER_DATA);
		// ��ȡ��ʷʹ������
		String useData=userSmp.readString(SoftManagerPreference.KEY_USE_DATA, "");
		return useData;
	}

}
