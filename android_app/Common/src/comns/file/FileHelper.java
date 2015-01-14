package comns.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;

/**
 * @����: FileHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-24 ����03:00:36
 * 
 * @����: ��<code>FileHelper</code>���ļ���ز�������</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class FileHelper {

	/** SDCARD·������ File.separator��β */
	public static final String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + File.separator;

	/**
	 * @return SDCARD �Ƿ�����
	 */
	public static boolean sdCardIsOk() {

		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * �õ��ʲ��ļ����ֽ�����
	 * 
	 * @param context
	 *            ������
	 * @param fileName
	 *            �ļ�·��
	 * @return �ʲ��ļ����ֽ�����
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
