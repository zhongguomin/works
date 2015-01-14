package comns.app.receiver.interfaces;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * @����: OnAppReplaceListener
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-11-22 ����10:24:48
 * 
 * @����: ��<code>OnAppReplaceListener</code>��Ӧ�ó����滻�ļ�����</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public interface OnAppReplaceListener {

	/**
	 * Ӧ�ó����滻
	 * 
	 * @param context
	 *            ������
	 * @param packageInfo
	 *            ��Ӧ�õ� PackageInfo
	 */
	public void onReplace(Context context, PackageInfo packageInfo);
}
