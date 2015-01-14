package comns.system;

import java.util.Random;

/**
 * @类名: MathHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-27 上午01:18:26
 * 
 * @描述: 类<code>MathHelper</code>是和数学计算相关的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class MathHelper {

	/**
	 * 返回半开区域[begin,end)之间的随机值
	 * 
	 * @param begin
	 *            起始值
	 * @param end
	 *            终止值（不包括）
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
