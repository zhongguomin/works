package com.g365.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

/**
 * @����: AsyncImageLoader
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-11-12 ����10:20:07
 * 
 * @����: ��<code>AsyncImageLoader</code>
 *      ���첽����ͼƬ���ࡣ</p>�����ȸ���ͼƬ���Ӷ�ȡ���ض�Ӧ����ֵ����û����������ȡ���ز����浽���ء�</p>
 * 
 *      Copyright 2013 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class AsyncImageLoader {

	/** Ĭ��ͼƬ�����ļ��� */
	private static final String DEFAULT_CACHE_FOLDER = "Cache/ImageCache/";

	private static volatile AsyncImageLoader imageLoader;

	/** ����ͼƬ���·�� */
	private String imageFolderDir;
	private HashMap<String, SoftReference<Drawable>> imageCache;

	/**
	 * @param context
	 *            ������
	 * @param imageCacheFolder
	 *            ����ͼƬ����ļ���������"ImageCache/"������ SDCARD��Ŀ¼��ָ����""
	 */
	private AsyncImageLoader(String imageCacheFolder) {

		this.imageFolderDir = FileHelper.SDCARD_PATH + imageCacheFolder;
		this.imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	/**
	 * @param context
	 *            ������
	 * @param imageCacheFolder
	 *            ����ͼƬ����ļ���������"ImageCache/"������ SDCARD��Ŀ¼��ָ����""
	 * @return AsyncImageLoaderʵ��
	 */
	public static AsyncImageLoader getInstance(String imageCacheFolder) {

		if (imageLoader == null) {
			imageLoader = new AsyncImageLoader(imageCacheFolder);
		}

		return imageLoader;
	}

	/**
	 * @param context
	 *            ������
	 * @return AsyncImageLoaderʵ��
	 */
	public static AsyncImageLoader getInstance() {

		if (imageLoader == null) {
			imageLoader = new AsyncImageLoader(DEFAULT_CACHE_FOLDER);
		}

		return imageLoader;
	}

	/**
	 * ����ͼƬ�����첽��ȡͼƬ
	 * 
	 * @param imageUrl
	 *            ͼƬ����
	 * @param listener
	 *            ͼƬ���سɹ�������
	 * @return
	 */
	public Drawable loadImage(final String imageUrl,
			final ImageLoadListener listener) {

		if (imageUrl.length() <= 0 || imageUrl == null || listener == null) {
			return null;
		}

		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference != null) {
				Drawable cacheDrawable = softReference.get();
				if (cacheDrawable != null) {

					CustomPrint.d(getClass(), "loadImage��from soft reference"
							+ " url��" + imageUrl);
					return cacheDrawable;
				}
			}
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				listener.onImageLoaded(imageUrl, (Drawable) msg.obj);
			}
		};

		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				imageCache.put(imageUrl, null);
				Drawable cacheDrawable = getImage(imageUrl);
				if (cacheDrawable != null) {
					imageCache.put(imageUrl, new SoftReference<Drawable>(
							cacheDrawable));

					Message message = handler.obtainMessage(0, cacheDrawable);
					handler.sendMessage(message);
				}
			}
		}.start();

		return null;
	}

	/**
	 * �ӻ���������ȡͼƬ
	 * 
	 * @param imageUrl
	 *            ͼƬ����
	 * @return
	 */
	public Drawable getImage(String imageUrl) {

		if (imageUrl.length() <= 0 || imageUrl == null) {
			return null;
		}

		Drawable cacheDrawable = null;

		/* ��� SDCARD���ã���� SDCARD���أ�������û��������д�뱾�أ���� SDCARD���������������� */
		if (FileHelper.sdCardIsOk()) {

			File imageFolderDir = new File(this.imageFolderDir);
			if (!imageFolderDir.exists()) {
				imageFolderDir.mkdirs();
			}

			String imageCacheFilePath = this.imageFolderDir
					+ UrlHelper.getFileNameFromUrl(imageUrl);
			File imageCacheFile = new File(imageCacheFilePath);
			if (imageCacheFile.exists()) {

				cacheDrawable = Drawable.createFromPath(imageCacheFilePath);

				CustomPrint.d(getClass(), "getImage��from cache file" + " url��"
						+ imageUrl);
			} else {

				try {
					InputStream inputStream = UrlHelper
							.getInputStreamFromUrl(imageUrl);
					if (inputStream == null) {
						return null;
					}
					FileOutputStream fos = new FileOutputStream(
							imageCacheFilePath);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int readNum = 0;
					while ((readNum = inputStream.read(buffer)) != -1) {
						baos.write(buffer, 0, readNum);
					}
					byte[] byteArray = baos.toByteArray();
					fos.write(byteArray, 0, byteArray.length);
					fos.close();
					baos.close();

					cacheDrawable = Drawable.createFromPath(imageCacheFilePath);
					CustomPrint.d(getClass(), "getImage��from url and to sdcard"
							+ " url��" + imageUrl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			InputStream inputStream = UrlHelper.getInputStreamFromUrl(imageUrl);
			if (inputStream == null) {
				return null;
			}
			cacheDrawable = Drawable.createFromStream(inputStream, "src");

			CustomPrint.d(getClass(), "getImage��from url" + " url��" + imageUrl);
		}

		return cacheDrawable;
	}

	/**
	 * @����: ImageLoadListener
	 * 
	 * @����: ChellyChi
	 * 
	 * @�汾: V1.0
	 * 
	 * @����: 2012-11-12 ����10:26:38
	 * 
	 * @����: ��<code>ImageLoadListener</code>��ͼƬ���سɹ��ļ�������</p>
	 * 
	 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
	 * 
	 *      ��Դ��������ӡ�ɾ�����޸ġ�
	 * 
	 * 
	 */
	public interface ImageLoadListener {

		/**
		 * ͼƬ���سɹ��Ļص��ӿ�
		 * 
		 * @param view
		 *            Ҫ����ͼƬ�Ŀؼ�
		 * @param imageUrl
		 *            ͼƬ����
		 * @param imageDrawable
		 *            Ҫ���õ�ͼƬ
		 */
		public void onImageLoaded(String imageUrl, Drawable imageDrawable);
	}

	/**
	 * Ϊ ImageView�첽����ͼƬ
	 * 
	 * @param imageUrl
	 *            ͼƬ����
	 * @param imageView
	 *            Ҫ���ص� ImageView
	 * @param view
	 *            ImageView�ĸ�View
	 */
	public static void loadViewImage(String imageUrl, ImageView imageView,
			final View view) {

		if (imageUrl.length() <= 0 || imageUrl == null || imageView == null
				|| view == null) {
			return;
		}

		imageView.setTag(imageUrl);
		Drawable drawable = AsyncImageLoader.getInstance().loadImage(imageUrl,
				new ImageLoadListener() {

				
					public void onImageLoaded(String imageUrl,
							Drawable imageDrawable) {
					
 
						ImageView tagImageView = (ImageView) view
								.findViewWithTag(imageUrl);
						if (tagImageView != null && imageDrawable != null) {
							tagImageView.setBackgroundDrawable(imageDrawable);
						}
					}
				});

		if (drawable != null) {
			imageView.setBackgroundDrawable(drawable);
		}
	}
}
