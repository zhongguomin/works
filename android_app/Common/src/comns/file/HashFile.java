package comns.file;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * @����: HashFile
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2013-2-22 ����11:28:45
 * 
 * @����: ��<code>HashFile</code>�ǻ�ȡ�ļ�HASH�����</p>
 * 
 *      Copyright 2013�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class HashFile {

	public static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/** HASH���� MD5 */
	public static final String HASH_TYPE_MD5 = "MD5";
	/** HASH���� SHA1 */
	public static final String HASH_TYPE_SHA1 = "SHA1";
	/** HASH���� SHA-256 */
	public static final String HASH_TYPE_SHA_256 = "SHA-256";
	/** HASH���� SHA-384 */
	public static final String HASH_TYPE_SHA_384 = "SHA-384";
	/** HASH���� SHA-512 */
	public static final String HASH_TYPE_SHA_512 = "SHA-512";

	/**
	 * ��ȡ�ļ��� HASHֵ����ȡʧ�ܷ��� null��
	 * 
	 * @param filePath
	 *            �ļ���·��
	 * @param hashType
	 *            Ҫ������ HASH����
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
	 * �� byte����ת�����ַ���
	 * 
	 * @param b
	 *            Ҫת��������
	 * @return ת������ַ���
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