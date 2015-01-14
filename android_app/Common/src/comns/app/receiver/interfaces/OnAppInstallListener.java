package comns.app.receiver.interfaces;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * @����: OnAppInstallListener
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-11-15 ����03:50:27
 * 
 * @����: ��<code>OnAppInstallListener</code>��Ӧ�ñ���װ�ļ�����</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public interface OnAppInstallListener {

	/**
	 * Ӧ�ñ���װ
	 * 
	 * @param context
	 *            ������
	 * @param packageInfo
	 *            ��Ӧ�õ� PackageInfo
	 */
	public void onInstall(Context context, PackageInfo packageInfo);
}
