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
 * @类名: AsyncImageLoader
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-11-12 上午10:20:07
 * 
 * @描述: 类<code>AsyncImageLoader</code>
 *      是异步加载图片的类。</p>本类先根据图片链接读取本地对应缓存值，若没有再联网获取加载并保存到本地。</p>
 * 
 *      Copyright 2013 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class AsyncImageLoader {

	/** 默认图片缓存文件夹 */
	private static final String DEFAULT_CACHE_FOLDER = "Cache/ImageCache/";

	private static volatile AsyncImageLoader imageLoader;

	/** 缓存图片存放路径 */
	private String imageFolderDir;
	private HashMap<String, SoftReference<Drawable>> imageCache;

	/**
	 * @param context
	 *            上下文
	 * @param imageCacheFolder
	 *            缓存图片存放文件夹名，如"ImageCache/"，如存放 SDCARD根目录则指定成""
	 */
	private AsyncImageLoader(String imageCacheFolder) {

		this.imageFolderDir = FileHelper.SDCARD_PATH + imageCacheFolder;
		this.imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	/**
	 * @param context
	 *            上下文
	 * @param imageCacheFolder
	 *            缓存图片存放文件夹名，如"ImageCache/"，如存放 SDCARD根目录则指定成""
	 * @return AsyncImageLoader实例
	 */
	public static AsyncImageLoader getInstance(String imageCacheFolder) {

		if (imageLoader == null) {
			imageLoader = new AsyncImageLoader(imageCacheFolder);
		}

		return imageLoader;
	}

	/**
	 * @param context
	 *            上下文
	 * @return AsyncImageLoader实例
	 */
	public static AsyncImageLoader getInstance() {

		if (imageLoader == null) {
			imageLoader = new AsyncImageLoader(DEFAULT_CACHE_FOLDER);
		}

		return imageLoader;
	}

	/**
	 * 根据图片链接异步获取图片
	 * 
	 * @param imageUrl
	 *            图片链接
	 * @param listener
	 *            图片加载成功监听器
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

					CustomPrint.d(getClass(), "loadImage：from soft reference"
							+ " url：" + imageUrl);
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
	 * 从缓存或网络获取图片
	 * 
	 * @param imageUrl
	 *            图片链接
	 * @return
	 */
	public Drawable getImage(String imageUrl) {

		if (imageUrl.length() <= 0 || imageUrl == null) {
			return null;
		}

		Drawable cacheDrawable = null;

		/* 如果 SDCARD可用，则从 SDCARD加载，若本地没有则联网写入本地，如果 SDCARD不可用则联网加载 */
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

				CustomPrint.d(getClass(), "getImage：from cache file" + " url："
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
					CustomPrint.d(getClass(), "getImage：from url and to sdcard"
							+ " url：" + imageUrl);
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

			CustomPrint.d(getClass(), "getImage：from url" + " url：" + imageUrl);
		}

		return cacheDrawable;
	}

	/**
	 * @类名: ImageLoadListener
	 * 
	 * @作者: ChellyChi
	 * 
	 * @版本: V1.0
	 * 
	 * @日期: 2012-11-12 上午10:26:38
	 * 
	 * @描述: 类<code>ImageLoadListener</code>是图片加载成功的监听器。</p>
	 * 
	 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
	 * 
	 *      该源码允许添加、删除和修改。
	 * 
	 * 
	 */
	public interface ImageLoadListener {

		/**
		 * 图片加载成功的回调接口
		 * 
		 * @param view
		 *            要设置图片的控件
		 * @param imageUrl
		 *            图片链接
		 * @param imageDrawable
		 *            要设置的图片
		 */
		public void onImageLoaded(String imageUrl, Drawable imageDrawable);
	}

	/**
	 * 为 ImageView异步加载图片
	 * 
	 * @param imageUrl
	 *            图片链接
	 * @param imageView
	 *            要加载的 ImageView
	 * @param view
	 *            ImageView的父View
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
