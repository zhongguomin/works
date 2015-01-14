package comns.file;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @类名: HashFile
 * 
 * @作者: ChellyChi
 * 
 * @版本: V1.0
 * 
 * @日期: 2013-2-22 上午11:28:45
 * 
 * @描述: 类<code>HashFile</code>是获取文件HASH码的类</p>
 * 
 *      Copyright 2013。 All rights reserved.
 * 
 *      该源码允许添加、删除和修改。
 * 
 * 
 */
public class HashFile {

	public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/** HASH类型 MD5 */
	public static final String HASH_TYPE_MD5 = "MD5";
	/** HASH类型 SHA1 */
	public static final String HASH_TYPE_SHA1 = "SHA1";
	/** HASH类型 SHA-256 */
	public static final String HASH_TYPE_SHA_256 = "SHA-256";
	/** HASH类型 SHA-384 */
	public static final String HASH_TYPE_SHA_384 = "SHA-384";
	/** HASH类型 SHA-512 */
	public static final String HASH_TYPE_SHA_512 = "SHA-512";

	/**
	 * 获取文件的 HASH值（获取失败返回 null）
	 * 
	 * @param filePath
	 *            文件的路径
	 * @param hashType
	 *            要解析的 HASH类型
	 * @return
	 * @throws Exception
	 */
	public static String getHash(String filePath, String hashType) {

		String hashStr = null;

		try {

			InputStream fis = new FileInputStream(filePath);
			byte[] buffer = new byte[1024];
			MessageDigest md5 = MessageDigest.getInstance(hashType);
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			hashStr = toHexString(md5.digest());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return hashStr;
	}

	/**
	 * 将 byte数组转化成字符串
	 * 
	 * @param b
	 *            要转换的数组
	 * @return 转换后的字符串
	 */
	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}
}