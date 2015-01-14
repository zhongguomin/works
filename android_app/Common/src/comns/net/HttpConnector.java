package comns.net;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import comns.net.HttpConnectable.HttpState;
import comns.system.CustomPrint;

/**
 * @ClassName: HttpConnector
 * 
 * @author: chellychi
 * 
 * @version: V1.0
 * 
 * @Date: 2013-7-16 ����5:21:18
 * 
 * @Description: ��<code>HttpConnector</code>��ʵ�����������߼���ʵ����</p>
 * 
 *               Copyright 2013�� All rights reserved.
 * 
 *               You have the permissions to modify.
 * 
 */
public class HttpConnector implements Runnable {

	/** ��ʱʱ�� */
	private static final int CONNECT_TIME_OUT = 20000;
	/** ʧ���ٴγ������Ӽ�� */
	private static final int CONNECT_INTERVAL = 30000;

	private Context context;
	private HttpConnectable mHttpcConnect;

	public HttpConnector(Context context, HttpConnectable httpConnectable) {

		this.context = context;
		this.mHttpcConnect = httpConnectable;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		boolean startOk = startTask();
		CustomPrint.d(getClass(), "startTask:" + startOk);
	}

	/**
	 * ��ʼִ������
	 * 
	 * @return �Ƿ�ɹ�
	 */
	private boolean startTask() {

		try {
			HttpState end = HttpState.SUCCESS;
			end = httpConnect();
			if (end == HttpState.ERROR)
				mHttpcConnect.handleError();

			mHttpcConnect.hanleFinish(end);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * ����ִ������
	 */
	private HttpState httpConnect() {

		HttpState status = HttpState.ERROR;
		String connURL = mHttpcConnect.getConnectUrl();
		if (TextUtils.isEmpty(connURL)) {
			CustomPrint.d(getClass(), "httpConnect:connURL is empty");
			return status;
		}

		int trytimes = 0;
		int maxTryTimes = mHttpcConnect.getTryConnectionNum();

		while (status != HttpState.SUCCESS && trytimes <= maxTryTimes) {

			URLConnection conn = null;
			ByteArrayOutputStream dos = null;
			BufferedInputStream bis = null;
			InputStream is = null;
			byte[] downim = null;

			try {
				URL myurl = new URL(connURL);
				conn = myurl.openConnection();
				conn.setConnectTimeout(CONNECT_TIME_OUT);
				conn.setReadTimeout(CONNECT_TIME_OUT);

				CustomPrint.d(getClass(), "httpConnect:openConnection OK");

				// get network type
				ConnectivityManager cm = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = cm.getActiveNetworkInfo();
				int netType = networkInfo.getType();

				if (netType == ConnectivityManager.TYPE_MOBILE)
					setMobileConnection(conn, connURL);

				is = conn.getInputStream();

				CustomPrint.d(getClass(), "httpConnect:getInputStream OK");

				bis = new BufferedInputStream(is);
				dos = new ByteArrayOutputStream();
				int hh;
				while ((hh = bis.read()) != -1) {
					dos.write(hh);
				}

				downim = dos.toByteArray();

				bis.close();
				is.close();

				CustomPrint.d(getClass(), "httpConnect:readFromInternet OK");

				String temp = EncodingUtils.getString(downim, "UTF-8");

				status = mHttpcConnect.parseContent(temp);
				if (status == HttpState.ERROR) {
					trytimes++;
				}

			} catch (Exception ex) {
				trytimes++;
				status = HttpState.ERROR;
				// ��һ�����ӵ��쳣,ֱ���˳�����
				ex.printStackTrace();
				if (trytimes < maxTryTimes) {
					try {
						Thread.sleep(CONNECT_INTERVAL);// 30���ͣ��ʱ��
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		CustomPrint.d(getClass(), "httpConnect:state-->" + status);

		return status;
	}

	/**
	 * �����ֻ�����Э��
	 * 
	 * @param conn
	 *            URLConnection
	 * @param connURL
	 *            String
	 */
	private void setMobileConnection(URLConnection conn, String connURL) {

		if (connURL.indexOf("http://") != -1)
			conn.setRequestProperty(
					"X-Online-Host",
					connURL.substring(connURL.indexOf("http://") + 7,
							connURL.indexOf("/", 7)));
		else
			conn.setRequestProperty("X-Online-Host",
					connURL.substring(0, connURL.indexOf("/", 7)));// ���ش���
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("User-Agent",
				System.getProperty("microedition.platform"));

		CustomPrint.d(getClass(), "setMobileConnection");
	}
}
