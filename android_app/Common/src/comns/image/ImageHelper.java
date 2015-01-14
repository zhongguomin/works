package comns.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import comns.net.UrlHelper;

/**
 * @类名: ImageHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-31 上午11:50:28
 * 
 * @描述: 类<code>ImageHelper</code>是用来处理和图片相关的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class ImageHelper {

	/**
	 * 从指定的图片链接获取 Bitmap
	 * 
	 * @param imageUrl
	 *            图片资源的链接
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String imageUrl) {

		return BitmapFactory.decodeStream(UrlHelper
				.getInputStreamFromUrl(imageUrl));
	}

	/**
	 * 按容器大小获取比例不变至少一个方向填充容器且能全部显示的图片
	 * 
	 * @param drawable
	 *            原图
	 * @param width
	 *            容器的宽
	 * @param height
	 *            容器的高
	 * @return
	 */
	public static Bitmap getCompatibleImg(Drawable drawable, int width,
			int height) {

		Bitmap logoBitmap;// 转换后的图
		Bitmap logo = ((BitmapDrawable) drawable).getBitmap();// 原图

		/* 获得图片的分辨率 */
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();
		float scaleWidth = (float) logoWidth / width;
		float scaleHeight = (float) logoHeight / height;
		if (scaleWidth > scaleHeight) {// 如果宽的比列大则高按照宽缩放
			logoBitmap = Bitmap.createScaledBitmap(logo, width,
					(int) (logoHeight / scaleWidth), true);
		} else {// 否则宽按照高的比例缩放
			logoBitmap = Bitmap.createScaledBitmap(logo,
					(int) (logoWidth / scaleHeight), height, true);
		}

		return logoBitmap;
	}
}
