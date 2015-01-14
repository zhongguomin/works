package comns.bytes;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * @����: ByteHelper
 * 
 * @����: ChellyChi
 * 
 * @�汾: V1.0
 * 
 * @����: 2012-10-24 ����03:14:42
 * 
 * @����: ��<code>ByteHelper</code>�Ƕ��ַ������ַ����ֽڵȽ��и��ִ������</p>
 * 
 *      Copyright 2012 ������Ϣ�������޹�˾�� All rights reserved.
 * 
 *      ��Դ��������ӡ�ɾ�����޸ġ�
 * 
 * 
 */
public class ByteHelper {

	/**
	 * ��ȡ����ַ���
	 * 
	 * @param length
	 *            Ҫ��ȡ�ĳ���
	 * @return ָ�����ȵ�����ַ���
	 */
	public static String randomStr(int length) {

		String hash = "";
		String charStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
		char chars[] = charStr.toCharArray();
		int max = chars.length;
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			hash += chars[Math.abs(random.nextInt()) % max];
		}

		return hash;
	}

	/**
	 * ���ֽ�������base64������ַ�������
	 * 
	 * @param bytes
	 *            Ҫ������ֽ�����
	 * @return ���ܺ���ַ���
	 */
	public static String base64Encoder(byte[] bytes) {

		StringBuilder result = new StringBuilder();
		String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

		int prevByteBitCount = 0, nextByteBitCount = 6;// prevByteBitCount��ʾ��ǰһ���ֽ�ȡ�õ�bit����nextByteBitCount��ʾ�Ӻ�һ���ֽ�ȡ�õ�bit��
		int i = 0, n = 0;// i��ʾ��ǰ������������n��ʾ�Ѿ������λ��
		int byteCount = 8 * bytes.length;// byteCount��ʾ�ܵ�λ��
		byte b = 0;

		while (true) {
			/* �����ǰ�������ֽ�ȡ��λ������� */
			if (prevByteBitCount > 0 && nextByteBitCount > 0) {
				/*
				 * ��ǰһ���ֽڵĵ�λ������nextByteBitCount��bit����ʹ��һ���ֽڵĸ�λ��nextByteBitCountָ����λ��
				 * �����Ƶ��ֽڵ����λ��
				 * Ȼ������λ�ƽ�������߼���Ҳ���ǽ���ǰһ���ֽںͺ�һ���ֽ�ȡ�õ���Ӧ��bit�ϲ�Ϊһ���ֽڵĵ�λ
				 */
				b = (byte) (((0xff & bytes[i]) << nextByteBitCount) | ((0xff & bytes[i + 1]) >> (8 - nextByteBitCount)));
				/* ���߼����Ľ�����������bit�ó�0 */
				b = (byte) (b & 0x3f);
				prevByteBitCount = 8 - nextByteBitCount;
				nextByteBitCount = 6 - prevByteBitCount;
			}
			/* ����Ӻ�һ���ֽ�ȡ�ø�6λ����� */
			else if (prevByteBitCount == 0) {
				b = (byte) ((0xff & bytes[i]) >> (8 - nextByteBitCount));// ��һ���ֽڵĸ�6λ���ƶ���6λ
				/* ��������λʱ�����Ǵ�ǰһ���ֽ�ȡ2��bit���Ӻ�һ������ȡ4��bit */
				prevByteBitCount = 2;
				nextByteBitCount = 4;
			}
			/* �����ǰһ���ֽ�ȡ�õ�6λ����� */
			else if (nextByteBitCount == 0) {
				b = (byte) (0x3f & bytes[i]);// ��ǰһ���ֽڵ��������bit�ó�0
				/* ��������λʱ���Ӻ�һ���ֽ�ȡ6��bit */
				prevByteBitCount = 0;
				nextByteBitCount = 6;

			}
			result.append(base64.charAt(b));
			n += 6;
			i = n / 8;
			int remainBitCount = byteCount - n;
			if (remainBitCount < 6) {
				/* ��ʣ���bit��0����Ȼ��Ҫ��base64������в�����Ӧ���ַ�������ӵ�����ַ�������� */
				if (remainBitCount > 0) {
					b = bytes[bytes.length - 1];
					b = (byte) (0x3f & (b << (6 - remainBitCount)));
					result.append(base64.charAt(b));
				}
				break;
			}
		}
		/* �����bit����3������Ϊ1����һ����=����Ϊ2����������=�� */
		n = byteCount % 3;
		for (i = 0; i < n; i++)
			result.append("=");

		return result.toString();
	}

	/**
	 * ���ַ����е� HTML�����滻�ɶ�Ӧ�ַ�
	 * 
	 * @param source
	 *            Դ�ַ���
	 * @return �������ַ���
	 */
	public static String replaceHtmlString(String source) {

		if (source == null || source.length() <= 0)
			return source;

		source = source.replace("&#xd;", "");
		source = source.replace("&quot;", "\"");
		source = source.replace("&#039;", "'");
		source = source.replace("&apos;", "'");
		source = source.replace("&lt;", "<");
		source = source.replace("&gt;", ">");
		source = source.replace("&amp;", "&");
		source = source.replace("&ldquo;", "��");
		source = source.replace("&rdquo;", "��");
		source = source.replace("&nbsp;", " ");

		return source;
	}

	/**
	 * �������ַ��磺'&','?','=','.','>','<','\'',';',':','/'ת��'_'
	 * 
	 * @param source
	 *            Դ�ַ���
	 * @return
	 */
	public static String replaceSpecialString(String source) {

		if (source == null || source.length() <= 0) {
			return source;
		}

		source = source.replace('&', '_');
		source = source.replace('?', '_');
		source = source.replace('=', '_');
		source = source.replace('>', '_');
		source = source.replace('<', '_');
		source = source.replace('\'', '_');
		source = source.replace('\"', '_');
		source = source.replace(';', '_');
		source = source.replace(':', '_');
		source = source.replace('/', '_');

		return source;
	}

	/**
	 * �� length��Сת���� G,MB,KB��B ����ȷ��С�������λ
	 * 
	 * @param length
	 *            �ļ���С
	 * @return
	 */
	public static String formatFileSize(long length) {

		String result = null;

		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + (length % 1073741824 > 0 ? 3 : 0)) + "G";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0, sub_string
					+ (length % 1048576 > 0 ? 3 : 0))
					+ "M";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0, sub_string
					+ (length % 1024 > 0 ? 3 : 0))
					+ "KB";
		} else if (length < 1024)
			result = Long.toString(length) + "B";

		return result;
	}

	/**
	 * ��������ת�����ַ���
	 * 
	 * @param inputStream
	 *            ������
	 * @param encode
	 *            ���루use default encode if null��
	 * @return null if inputStream is null
	 */
	public static String inStream2Str(InputStream inputStream, String encode) {

		String result = null;

		if (inputStream != null) {

			try {
				BufferedInputStream bis = new BufferedInputStream(inputStream);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int position = 0;
				while ((position = bis.read()) != -1) {
					baos.write((byte) position);
				}
				if (encode == null) {
					result = baos.toString();
				} else {
					result = baos.toString(encode);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * ��Դ����ȡ������ʽ��ƥ����ַ����б�
	 * 
	 * @param source
	 *            Դ��
	 * @param regEx
	 *            ������ʽ
	 * @return
	 */
	public static ArrayList<String> getRegExStr(String source, String regEx) {

		ArrayList<String> resultList = new ArrayList<String>();

		if (TextUtils.isEmpty(source) || TextUtils.isEmpty(regEx)) {
			return resultList;
		}

		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			resultList.add(matcher.group());
		}

		return resultList;
	}
}
