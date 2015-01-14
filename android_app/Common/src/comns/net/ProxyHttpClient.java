package comns.net;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * @ClassName: ProxyHttpClient
 * 
 * @author: chellychi
 * 
 * @version: V1.0
 * 
 * @Date: 2013-8-19 下午9:38:32
 * 
 * @Description: 类<code>ProxyHttpClient</code>是</p>
 * 
 *               Copyright 2013。 All rights reserved.
 * 
 *               You have the permissions to modify.
 * 
 */
public class ProxyHttpClient extends DefaultHttpClient {

	private static final int HTTP_TIMEOUT_MS = 30 * 1000;

	private static final int BUFFER_SIZE = 1024 * 8;

	private static final String TAG = ProxyHttpClient.class.getSimpleName();

	private RuntimeException mLeakedException = new IllegalStateException(
			"ProxyHttpClient created and never closed");

	private String mPort;

	private String mProxy;

	private boolean mUseWap;

	public ProxyHttpClient(Context context) {
		this(context, null, null);
	}

	public ProxyHttpClient(Context context, ApnInfoGetter manager) {
		this(context, null, manager);
	}

	public ProxyHttpClient(Context context, String userAgent) {
		this(context, userAgent, null);
	}

	public ProxyHttpClient(Context context, String userAgent, ApnInfoGetter manager) {
		if (manager == null) {
			manager = new ApnInfoGetter(context);
		}

		this.mUseWap = manager.isWapNetwork();
		this.mProxy = manager.getProxy();
		this.mPort = manager.getProxyPort();
		if (this.mUseWap) {
			HttpHost host = new HttpHost(this.mProxy, Integer.valueOf(
					this.mPort).intValue());
			getParams().setParameter(ConnRouteParams.DEFAULT_PROXY, host); // 设置代理
		}
		HttpConnectionParams.setConnectionTimeout(getParams(), HTTP_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(getParams(), HTTP_TIMEOUT_MS);
		HttpConnectionParams.setSocketBufferSize(getParams(), BUFFER_SIZE);
		if (!TextUtils.isEmpty(userAgent)) {
			HttpProtocolParams.setUserAgent(getParams(), userAgent);
		}
	}

	public void close() {
		if (this.mLeakedException != null) {
			getConnectionManager().shutdown();
			this.mLeakedException = null;
		}
	}

	protected HttpParams createHttpParams() {
		HttpParams params = super.createHttpParams();
		HttpProtocolParams.setUseExpectContinue(params, false);
		return params;
	}

	protected void finalize() throws Throwable {
		super.finalize();
		if (this.mLeakedException != null) {
			Log.e(TAG, "Leak found", this.mLeakedException);
		}
	}
}
