package comns.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import comns.net.UrlHelper;

/**
 * @����: ImageHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-31 ����11:50:28
 * 
 * @����: ��<code>ImageHelper</code>�����������ͼƬ��ص���</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class ImageHelper {

	/**
	 * ��ָ����ͼƬ���ӻ�ȡ Bitmap
	 * 
	 * @param imageUrl
	 *            ͼƬ��Դ������
	 * @return
	 */
	public static Bitmap getBitmapFromUrl(String imageUrl) {

		return BitmapFactory.decodeStream(UrlHelper
				.getInputStreamFromUrl(imageUrl));
	}

	/**
	 * ��������С��ȡ������������һ�����������������ȫ����ʾ��ͼƬ
	 * 
	 * @param drawable
	 *            ԭͼ
	 * @param width
	 *            �����Ŀ�
	 * @param height
	 *            �����ĸ�
	 * @return
	 */
	public static Bitmap getCompatibleImg(Drawable drawable, int width,
			int height) {

		Bitmap logoBitmap;// ת�����ͼ
		Bitmap logo = ((BitmapDrawable) drawable).getBitmap();// ԭͼ

		/* ���ͼƬ�ķֱ��� */
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();
		float scaleWidth = (float) logoWidth / width;
		float scaleHeight = (float) logoHeight / height;
		if (scaleWidth > scaleHeight) {// �����ı��д���߰��տ�����
			logoBitmap = Bitmap.createScaledBitmap(logo, width,
					(int) (logoHeight / scaleWidth), true);
		} else {// ������ոߵı�������
			logoBitmap = Bitmap.createScaledBitmap(logo,
					(int) (logoWidth / scaleHeight), height, true);
		}

		return logoBitmap;
	}
}
