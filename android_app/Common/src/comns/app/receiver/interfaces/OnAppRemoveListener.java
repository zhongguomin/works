package comns.app.receiver.interfaces;

import android.content.Context;

/**
 * @����: OnAppRemoveListener
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-11-15 ����03:50:01
 * 
 * @����: ��<code>OnAppRemoveListener</code>��Ӧ�ñ�ж�صļ�����</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public interface OnAppRemoveListener {

	/**
	 * Ӧ�ñ�ɾ��
	 * 
	 * @param context
	 *            ������
	 * @param packName
	 *            ����
	 */
	public void onRemove(Context context, String packName);
}
