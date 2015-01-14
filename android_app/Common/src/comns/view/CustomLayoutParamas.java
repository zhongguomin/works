package comns.view;

import android.widget.LinearLayout.LayoutParams;

/**
 * @类名: CustomLayoutParamas
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2013-4-16 下午03:52:56
 * 
 * @描述: 类<code>CustomLayoutParamas</code>是用来获取自定义布局属性的类</p>
 * 
 *      Copyright 2013。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class CustomLayoutParamas {

	/**
	 * 获取宽和高的填充方式
	 * 
	 * @param widthMatch
	 *            宽是否填充父控件
	 * @param heightMach
	 *            高是否填充父控件
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
