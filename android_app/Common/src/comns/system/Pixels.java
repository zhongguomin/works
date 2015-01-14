package comns.system;

import android.content.Context;

/**
 * @����: Pixels
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2013-2-22 ����10:14:28
 * 
 * @����: ��<code>Pixels</code>��dip��px���໥ת������</p>
 * 
 *      Copyright 2013�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class Pixels {

	/**
	 * �� dip ת���� px
	 * 
	 * @param context
	 *            ������
	 * @param dipValue
	 *            dip ֵ
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * �� px ת���� dip
	 * 
	 * @param context
	 *            ������
	 * @param pxValue
	 *            px ֵ
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
