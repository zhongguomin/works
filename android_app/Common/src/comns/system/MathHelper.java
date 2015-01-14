package comns.system;

import java.util.Random;

/**
 * @����: MathHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-27 ����01:18:26
 * 
 * @����: ��<code>MathHelper</code>�Ǻ���ѧ������ص���</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class MathHelper {

	/**
	 * ���ذ뿪����[begin,end)֮������ֵ
	 * 
	 * @param begin
	 *            ��ʼֵ
	 * @param end
	 *            ��ֵֹ����������
	 * @return
	 */
	public static int getRandomInt(int begin, int end) {

		int randomInt = 0;

		if (begin == end) {
			randomInt = begin;
		} else {

			if (begin > end) {
				begin = begin + end;
				end = begin - end;
				begin = begin - end;
			}

			Random random = new Random(System.currentTimeMillis());
			int n = end - begin;
			randomInt = begin + random.nextInt(n);
		}

		return randomInt;
	}
}
