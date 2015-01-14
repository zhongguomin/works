package comns.view;

import android.widget.LinearLayout.LayoutParams;

/**
 * @����: CustomLayoutParamas
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2013-4-16 ����03:52:56
 * 
 * @����: ��<code>CustomLayoutParamas</code>��������ȡ�Զ��岼�����Ե���</p>
 * 
 *      Copyright 2013�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class CustomLayoutParamas {

	/**
	 * ��ȡ��͸ߵ���䷽ʽ
	 * 
	 * @param widthMatch
	 *            ���Ƿ���丸�ؼ�
	 * @param heightMach
	 *            ���Ƿ���丸�ؼ�
	 * @return
	 */
	public static LayoutParams getCustomParams(boolean widthMatch,
			boolean heightMach) {
		return new LayoutParams(widthMatch ? LayoutParams.FILL_PARENT
				: LayoutParams.WRAP_CONTENT,
				heightMach ? LayoutParams.FILL_PARENT
						: LayoutParams.WRAP_CONTENT);
	}

}
