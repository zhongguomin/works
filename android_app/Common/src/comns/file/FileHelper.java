package comns.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;

/**
 * @类名: FileHelper
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2012-10-24 下午03:00:36
 * 
 * @描述: 类<code>FileHelper</code>是文件相关操作的类</p>
 * 
 *      Copyright 2012 艾秀信息技术有限公司。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class FileHelper {

	/** SDCARD路径，以 File.separator结尾 */
	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + File.separator;

	/**
	 * @return SDCARD 是否正常
	 */
	public static boolean sdCardIsOk() {

		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 得到资产文件的字节数组
	 * 
	 * @param context
	 *            上下文
	 * @param fileName
	 *            文件路径
	 * @return 资产文件的字节数组
	 */
	public static byte[] openAssets(Context context, String fileName) {

		byte[] tempByte = null;
		try {
			InputStream inputStream = context.getAssets().open(fileName);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			tempByte = baos.toByteArray();
			baos.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return tempByte;
	}
}
